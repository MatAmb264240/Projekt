from rest_framework import serializers
from django.contrib.auth.models import User
from movie.models import Movie, Genre, Rating, Review



class ReviewSerializer(serializers.ModelSerializer):
    class Meta:
        model = Review
        fields = '__all__'
class UserProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['first_name', 'last_name', 'email']

class GenreSerializer(serializers.ModelSerializer):
    class Meta:
        model = Genre
        fields = ['title', 'slug']

class RatingSerializer(serializers.ModelSerializer):
    class Meta:
        model = Rating
        fields = ['source', 'rating']

class MovieSerializer(serializers.ModelSerializer):
    #Genre = GenreSerializer(many=True, read_only=True)
    Ratings = RatingSerializer(many=True, read_only=True)
    #Poster = serializers.SerializerMethodField()

    class Meta:
        model = Movie
        fields = [
            'Title', 'Year', 'Rated', 'Released', 'Runtime', 'Director',
            'Writer', 'Actors', 'Plot', 'Language', 'Country', 'Awards', 'Poster',
            'Poster_url', 'imdbID', 'Type', 'Ratings', 'Metascore', 'imdbRating'
        ]

    def get_Poster(self, obj):
        return obj.Poster.url if obj.Poster else obj.Poster_url

class ReviewSerializer(serializers.ModelSerializer):
    user = serializers.CharField(source='user.username')  # Include username instead of user id
    
    class Meta:
        model = Review
        fields = ['user', 'text', 'rate', 'date']