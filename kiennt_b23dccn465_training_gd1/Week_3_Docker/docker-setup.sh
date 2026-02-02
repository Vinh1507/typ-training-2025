#!/bin/bash
for pkg in docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc
    do sudo apt-get remove $pkg
done
# Cập nhật danh sách gói và cài các gói cần thiết
sudo apt-get update
sudo apt-get upgrade
sudo apt-get install ca-certificates curl
# Tạo thư mục cho keyrings
sudo install -m 0755 -d /etc/apt/keyrings
# Tải về GPG key của Docker
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
# Cấp quyền đọc cho key
sudo chmod a+r /etc/apt/keyrings/docker.asc
# Thêm kho lưu trữ Docker vào danh sách nguồn của APT
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
# Cập nhật lại danh sách gói để nhận diện kho lưu trữ Docker mới
sudo apt-get update
# Cài đặt Docker Engine, Docker CLI và Containerd
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
# Tạo nhóm docker và thêm người dùng hiện tại vào nhóm docker
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
