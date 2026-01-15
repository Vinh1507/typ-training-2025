from django.shortcuts import render

# Create your views here.
from django.http import JsonResponse
import json
from django.views.decorators.csrf import csrf_exempt
from .models import User

# GET list và POST new
@csrf_exempt
def user_list(request):
    data = {}
    status_code = 200

    if request.method == 'GET':
        age = request.GET.get('age')
        if age:
            try:
                age = int(age)
                users  = User.objects.filter(age = age)
            except ValueError:
                return JsonResponse({"error": "Tham số age phải là số"}, status=400)
        else:
            users = User.objects.all()
        data = list(users.values())
        return JsonResponse({"users": data}, status=200)

    elif request.method == 'POST':
        try:
            body = json.loads(request.body)
            name = body.get('name')
            age = body.get('age')
            if not name or not age:
                data = {"error": "Thiếu thông tin name hoặc age"}
                status_code = 400
            else:
                user = User.objects.create(name=name, age=age)
                data = {"message": "Tạo thành công", "name": user.name}
                status_code = 201

        except json.JSONDecodeError:
            data = {"error": "Dữ liệu JSON không hợp lệ"}
            status_code = 400
    else:
        data = {"error": "Phương thức không được hỗ trợ"}
        status_code = 405
    return JsonResponse(data, status=status_code)


# GET one, Update and delete
@csrf_exempt
def user_detail(request, id):
    data = {}
    status_code = 200
    try:
        user = User.objects.get(id = id)
    except User.DoesNotExist:
        return JsonResponse({"error": "Không tìm thấy user"}, status=404)

    if request.method == "GET":
        return JsonResponse({"id": user.id, "name": user.name, "age": user.age}, status=200)
    elif request.method == "DELETE":
        user.delete()
        return JsonResponse({"message": f"Đã xóa user {id}"}, status=200)
    elif request.method == 'PUT':
        try:
            body = json.loads(request.body)
            user.name = body.get("name", user.name)
            user.age = body.get("age", user.age)
            user.save()
            return JsonResponse({"message": "Cập nhật thành công", "name": user.name}, status=200)
        except json.JSONDecodeError:
            return JsonResponse({"error": "JSON không hợp lệ"}, status=400)
    else:
        return JsonResponse({"error": "Phương thức không hỗ trợ"}, status=405)

def todo_list(request):
    data = {"todos": ["Học Django", "Viết REST API"]}
    return JsonResponse(data)
