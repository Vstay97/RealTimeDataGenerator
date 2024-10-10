# docker build -t realtime-data-generator-frontend:v1.0.0 ./frontend/realtime-data-generator-front

FROM realtime-data-generator-frontend-base:v1.0.0

# 暴露端口
EXPOSE 13001

# 运行应用
CMD ["serve", "-s", "build", "-l", "13001"]