from django.shortcuts import get_object_or_404
from django.contrib.auth.models import User
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.permissions import IsAuthenticated
from movie.models import Movie, Review
from user.serializers import MovieSerializer
from django.shortcuts import render,get_object_or_404
from django.shortcuts import render ,redirect
from django.contrib.auth.models import User
from django.contrib import messages
from user.forms import SignupForm,EditProfileForm
from django.contrib.auth.decorators import login_required
from .models import Profile
from django.template import loader
from django.http import HttpResponse
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import AllowAny
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework.permissions import IsAuthenticated
from rest_framework_simplejwt.views import TokenObtainPairView
from rest_framework.response import Response
from user.serializers import UserProfileSerializer, MovieSerializer, ReviewSerializer
import logging
from movie.models import Movie, Review
from rest_framework.decorators import api_view
from django.contrib.auth import authenticate
from django.contrib.auth import login
import os
logger = logging.getLogger(__name__)
from django.db import transaction

@api_view(['GET'])
def movie_details(request, imdbID):
    try:
        movie = get_object_or_404(Movie, imdbID=imdbID)
        reviews = Review.objects.filter(movie=movie)
        
        movie_serializer = MovieSerializer(movie)
        review_serializer = ReviewSerializer(reviews, many=True)
        
        return Response({
            'movie': movie_serializer.data,
            'reviews': review_serializer.data
        }, status=status.HTTP_200_OK)
    except Exception as e:
        return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

@api_view(['POST'])
def add_review(request):
    user_id = request.data.get('userId')
    movie_id = request.data.get('movieId')
    imdbID = request.data.get('imdbID')
    text = request.data.get('text')
    rate = request.data.get('rate')
    movie_response = request.data.get('movie')
    
    if movie_response is None:
        return Response({'error': 'Movie data not provided'}, status=status.HTTP_400_BAD_REQUEST)
    
    # Przetwarzanie obiektu InfoResponse
    movie_data = {
        'Title': movie_response.get('Title'),
        'Year': movie_response.get('Year'),
        'Rated': movie_response.get('Rated'),
        'Released': movie_response.get('Released'),
        'Runtime': movie_response.get('Runtime'),
        'Director': movie_response.get('Director'),
        'Writer': movie_response.get('Writer'),
        'Actors': movie_response.get('Actors'),
        'Plot': movie_response.get('Plot'),
        'Language': movie_response.get('Language'),
        'Country': movie_response.get('Country'),
        'Awards': movie_response.get('Awards'),
        'Poster': "none",
        'Poster_url': movie_response.get('Poster'), #movie_response.get('Poster_url'),
        'Type': movie_response.get('Type'),
        'Metascore': movie_response.get('Metascore'),
        'imdbRating': movie_response.get('imdbRating')

    }
    print("Movie data:", movie_data)
    
    try:
        with transaction.atomic():
            user = User.objects.get(id=user_id)
            profile = Profile.objects.get(user=user)
            
            # Tworzenie lub aktualizacja filmu
            movie, created = Movie.objects.get_or_create(imdbID=imdbID, defaults=movie_data)
            
            # Dodawanie filmu do profilu
            profile.watched.add(movie)
            
            # Aktualizacja lub dodanie recenzji
            review, created = Review.objects.update_or_create(
                user=user,
                movie=movie,
                defaults={'text': text, 'rate': rate}
            )
            
            if created:
                return Response({'message': 'Review created successfully'}, status=status.HTTP_201_CREATED)
            else:
                return Response({'message': 'Review updated successfully'}, status=status.HTTP_200_OK)

    except User.DoesNotExist:
        return Response({'error': 'User not found'}, status=status.HTTP_404_NOT_FOUND)
    except Exception as e:
        return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET'])
def MoviesWatched(request, username):
    try:
        user = get_object_or_404(User, username=username)
        profile = get_object_or_404(Profile, user=user)
        movies_watched = profile.watched.filter(Type='movie')

        serializer = MovieSerializer(movies_watched, many=True)
        return Response({'movies_watched': serializer.data})
    except Exception as e:
        print(f"Error in MoviesWatched: {str(e)}")
        return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        
@api_view(['GET'])
def SeriesWatched(request, username):
        try:
            user = get_object_or_404(User, username=username)
            profile = get_object_or_404(Profile, user=user)
            movies_watched = profile.watched.filter(Type='series')  

            serializer = MovieSerializer(movies_watched, many=True)
            return Response({'movies_watched': serializer.data})
        except Exception as e:
            print(f"Error in MoviesWatched: {str(e)}")
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

        



class UserProfileView(APIView):
    permission_classes = [IsAuthenticated]

    def put(self, request, *args, **kwargs):
        user = request.user
        serializer = UserProfileSerializer(user, data=request.data, partial=True)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class SignupView(APIView):
    def post(self, request, *args, **kwargs):
        form = SignupForm(request.data)
        if form.is_valid():
            username = form.cleaned_data.get('username')
            email = form.cleaned_data.get('email')
            first_name = form.cleaned_data.get('first_name')
            last_name = form.cleaned_data.get('last_name')
            password = form.cleaned_data.get('password')
            user = User.objects.create_user(username=username, email=email, first_name=first_name, last_name=last_name, password=password)
            refresh = RefreshToken.for_user(user)
            response_data = {
                'refresh': str(refresh),
                'access': str(refresh.access_token),
            }
            print("Generated tokens:", response_data)  # Dodaj logowanie tokenów
            return Response(response_data, status=status.HTTP_201_CREATED)
        return Response(form.errors, status=status.HTTP_400_BAD_REQUEST)
    

class MyTokenObtainPairView(TokenObtainPairView):
    def post(self, request, *args, **kwargs):
        response = super().post(request, *args, **kwargs)
        
        username = request.data.get('username')
        password = request.data.get('password')
        
        print("Username:", username)
        print("Password:", password)
        
        user = authenticate(username=username, password=password)
        print("Authenticated user:", user)
        
        if user is not None:
            user_id = getattr(user, 'id', None)
            if user_id is not None:
                print("User ID:", user_id)
                data = response.data
                data['user_id'] = user_id
                return Response(data)
            else:
                print("User object does not have an ID attribute")
                return Response({'error': 'User object does not have an ID attribute'}, status=500)
        else:
            return Response({'error': 'Invalid credentials'}, status=400)

            
#Django
def Signup(request):
    if request.method == 'POST':
        form = SignupForm(request.POST)
        if form.is_valid():
            username = form.cleaned_data.get('username')
            email = form.cleaned_data.get('email')
            first_name = form.cleaned_data.get('first_name')
            last_name = form.cleaned_data.get('last_name')
            password = form.cleaned_data.get('password')
            User.objects.create_user(username=username, email=email, first_name=first_name, last_name=last_name, password=password)
            return redirect('login')
    else:
        print(form.errors)  # Dodaj logowanie błędów formularza
        form = SignupForm()

    context = {
        'form': form,
    }

    return render(request, 'user/register.html', context)


@login_required
def EditProfile(request):
    user = request.user.id
    profile = Profile.objects.get(user__id=user)

    if request.method == 'POST':
        form = EditProfileForm(request.POST, request.FILES)
        if form.is_valid():
            profile.picture = form.cleaned_data.get('picture')
            profile.first_name = form.cleaned_data.get('first_name')
            profile.last_name = form.cleaned_data.get('last_name')
            profile.location = form.cleaned_data.get('location')
            profile.url = form.cleaned_data.get('url')
            profile.profile_info = form.cleaned_data.get('profile_info')
            profile.save()
            return redirect('profile',username=profile.user.username)
    else:
        form = EditProfileForm()

    context = {
        'form': form,
    }

    return render(request, 'user/edit_profile.html', context)

def UserProfile(request,username):
    user=get_object_or_404(User,username=username)
    profile=Profile.objects.get(user=user)
    movies_watched = profile.watched.filter(Type='movie')
    series_watched = profile.watched.filter(Type='series')
    watchlist=profile.to_watch.all().order_by("-imdbRating")

    context={
        'profile':profile,
        'movies_watched':movies_watched,
        'series_watched':series_watched,
        'watchlist':watchlist,
    }
    template=loader.get_template('user/user_profile.html')
    return HttpResponse(template.render(context,request))
