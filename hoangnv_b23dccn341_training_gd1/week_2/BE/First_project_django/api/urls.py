from django.urls import path
from . import views



urlpatterns = [
    path('todo', views.todo_list),
    path('user', views.user_list),
    path('user/<int:id>', views.user_detail)
]
