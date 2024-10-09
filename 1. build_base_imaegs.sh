#!/bin/bash

# 设置错误处理
set -e

# 定义镜像名称
BACKEND_IMAGE="realtime-data-generator-backend-base:v1.0.0"
FRONTEND_IMAGE="realtime-data-generator-frontend-base:v1.0.0"

# 后端构建
echo "开始构建后端基础镜像..."
if docker build -f backend/RealTimeDataGenerator/Dockerfile.base -t $BACKEND_IMAGE ./backend/RealTimeDataGenerator; then
    echo "后端基础镜像构建成功: $BACKEND_IMAGE"
else
    echo "后端基础镜像构建失败!"
    exit 1
fi

# 前端构建
echo "开始构建前端基础镜像..."
if docker build -f frontend/realtime-data-generator-front/Dockerfile.base -t $FRONTEND_IMAGE ./frontend/realtime-data-generator-front; then
    echo "前端基础镜像构建成功: $FRONTEND_IMAGE"
else
    echo "前端基础镜像构建失败!"
    exit 1
fi

# 保存镜像
echo "保存基础镜像..."
if docker save $BACKEND_IMAGE > backend-base.tar && docker save $FRONTEND_IMAGE > frontend-base.tar; then
    echo "基础镜像保存成功!"
else
    echo "基础镜像保存失败!"
    exit 1
fi

echo "一阶段构建完成!"