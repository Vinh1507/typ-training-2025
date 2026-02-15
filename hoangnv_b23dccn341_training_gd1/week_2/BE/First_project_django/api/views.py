from django.shortcuts import render

# Create your views here.
from django.http import JsonResponse
import json
from django.views.decorators.csrf import csrf_exempt
# Dữ liệu giả lưu tạm trong RAM
USERS = [
    {"id": 1, "name": "Hoàng", "age": 20},
    {"id": 2, "name": "Mèo", "age": 22},
    {"id": 3, "name": "Tý", "age": 25},
]

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
                res = [u for u in USERS if u['age'] == age]
                data = {"user": res}
                status_code = 200
            except ValueError:
                data = {"error": "Tham số age phải là số"}
                status_code = 400
        else:
            data = {"users": USERS}
            status_code = 200

    elif request.method == 'POST':
        try:
            body = json.loads(request.body)
            name = body.get('name')
            age = body.get('age')
            if not name or not age:
                data = {"error": "Thiếu thông tin name hoặc age"}
                status_code = 400
            else:
                new_id = USERS[-1]["id"] + 1 if USERS else 1
                new_user = {"id": new_id, "name": name, "age": age}
                USERS.append(new_user)

                data = {"message": f"Tạo user {name}, tuổi {age} thành công"}
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
    user = next((u for u in USERS if u["id"] == id), None)

    if not user:
        return JsonResponse({"error": "Không tìm thấy user"}, status=404)

    if request.method == "GET":
        return JsonResponse(user, status=200)
    elif request.method == "DELETE":
        USERS.remove(user)
        return JsonResponse({"message": f"Đã xóa user {id}"}, status=200)
    elif request.method == 'PUT':
        try:
            body = json.loads(request.body)
            user["name"] = body.get("name", user["name"])
            user["age"] = body.get("age", user["age"])
            return JsonResponse({"message": "Cập nhật thành công", "user": user}, status=200)
        except json.JSONDecodeError:
            return JsonResponse({"error": "JSON không hợp lệ"}, status=400)
    else:
        return JsonResponse({"error": "Phương thức không hỗ trợ"}, status=405)

def todo_list(request):
    data = {"todos": ["Học Django", "Viết REST API"]}
    return JsonResponse(data)
