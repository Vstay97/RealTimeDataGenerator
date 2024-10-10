# docker build -t realtime-data-generator-backend:v1.0.0 ./backend/RealTimeDataGenerator

FROM realtime-data-generator-backend-base:v1.0.0

# 复制源代码
COPY src ./src

# 复制本地依赖（以防有新的依赖添加）
COPY libs ./libs

# 构建应用（使用已下载的依赖）
RUN mvn clean package -DskipTests -o

# 暴露应用端口
EXPOSE 18081

# 运行应用
CMD ["java", "-jar", "/app/target/RealTimeDataGenerator-0.0.1-SNAPSHOT.jar"]