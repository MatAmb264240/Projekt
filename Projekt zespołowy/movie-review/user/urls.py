from django.urls import path
from .views import EditProfile, Signup, UserProfile, MyTokenObtainPairView, SignupView, UserProfileView, MoviesWatched, SeriesWatched, add_review, movie_details
from django.contrib.auth import views as userViews
from rest_framework_simplejwt.views import (
    TokenRefreshView
)

urlpatterns = [
    path('login/', userViews.LoginView.as_view(template_name='user/login.html',next_page='landing'), name='login'),
    path('logout/', userViews.LogoutView.as_view(next_page='landing'), name='logout'),
    path('profile/<username>/', UserProfile, name='profile'),
    path('profile/edit', EditProfile, name='edit-profile'),
    path('signup/', Signup, name='register'),

    path('api/movies_watched/<username>/', MoviesWatched, name='movies_watched'),
    path('api/series_watched/<username>/', SeriesWatched, name='series_watched'),

    path('api/post/', SignupView.as_view(), name='signup'),
    path('api/profile/', UserProfileView.as_view(), name='profile'),
    
    path('api/token/', MyTokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('api/token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    
    path('api/add_review/', add_review, name='add_review'),
     path('api/movie_details/<str:imdbID>/', movie_details, name='movie_details'),
    
]
