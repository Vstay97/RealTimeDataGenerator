#!/bin/bash

# 设置错误处理
set -e

# 定义镜像名称
BACKEND_IMAGE="realtime-data-generator-backend:v1.0.0"
FRONTEND_IMAGE="realtime-data-generator-frontend:v1.0.0"

# 删除旧的运行时镜像
echo "删除旧的运行时镜像..."
docker rmi $BACKEND_IMAGE || true
docker rmi $FRONTEND_IMAGE || true

# 加载基础镜像
echo "加载后端基础镜像..."
if docker load < backend-base.tar; then
    echo "后端基础镜像加载成功!"
else
    echo "后端基础镜像加载失败!"
    exit 1
fi

echo "加载前端基础镜像..."
if docker load < frontend-base.tar; then
    echo "前端基础镜像加载成功!"
else
    echo "前端基础镜像加载失败!"
    exit 1
fi

# 后端构建
echo "开始构建后端应用镜像..."
if docker build -t $BACKEND_IMAGE ./backend/RealTimeDataGenerator; then
    echo "后端应用镜像构建成功: $BACKEND_IMAGE"
else
    echo "后端应用镜像构建失败!"
    exit 1
fi

# 前端构建
echo "开始构建前端应用镜像..."
if docker build -t $FRONTEND_IMAGE ./frontend/realtime-data-generator-front; then
    echo "前端应用镜像构建成功: $FRONTEND_IMAGE"
else
    echo "前端应用镜像构建失败!"
    exit 1
fi

echo "二阶段构建完成!"
