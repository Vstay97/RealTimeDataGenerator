# 实时数据流生成平台

## 项目概述

实时数据流生成平台是一个用于各类数据库的实时数据流生成工具。该平台由前端和后端两部分组成，支持多种数据库类型，包括 MySQL、Oracle、PostgreSQL、DB2 和 OceanBase。

## 技术栈

### 前端
- React 18.3.1
- TypeScript 5.6.2
- Ant Design 5.21.1
- Axios 1.7.7

### 后端
- Spring Boot 3.3.4
- Java 17
- Maven

## 功能特性

1. 支持多种数据库类型
2. 可配置多线程进行数据生成
    - 自动建表
    - 自动生成模拟数据流
    - 多并发数据流生成（debugging）
3. 实时任务管理（提交、停止、查看）
4. 用户友好的 Web 界面

## 项目结构

```
.
├── frontend
│   └── realtime-data-generator-front
│       ├── public
│       ├── src
│       ├── Dockerfile
│       └── Dockerfile_base
├── backend
│   └── RealTimeDataGenerator
│       ├── src
│       ├── pom.xml
│       ├── Dockerfile
│       └── Dockerfile_base
├── docker-compose.yml
├── build_base_images.sh
└── build_runtime_images.sh
```

## 安装和运行

### 使用Docker Compose (推荐)

1. 确保已安装Docker和Docker Compose。

2. 在项目根目录下运行:
   ```
   ./build_base_images.sh
   ./build_runtime_images.sh
   docker-compose up -d
   ```

3. 访问 http://localhost:13001 使用前端应用。

### 手动安装

#### 前端

1. 进入前端目录:
   ```
   cd frontend/realtime-data-generator-front
   ```

2. 安装依赖:
   ```
   npm install --legacy-peer-deps
   ```

3. 运行开发服务器:
   ```
   npm start
   ```

   应用将在 http://localhost:13001 运行。

#### 后端

1. 进入后端目录:
   ```
   cd backend/RealTimeDataGenerator
   ```

2. 使用Maven构建项目:
   ```
   mvn clean package -DskipTests
   ```

3. 运行Spring Boot应用:
   ```
   java -jar target/RealTimeDataGenerator-0.0.1-SNAPSHOT.jar
   ```

   后端服务将在 http://localhost:18081 运行。

## Docker部署

项目提供了Dockerfile和Dockerfile_base文件,可以使用Docker进行部署。

### 前端

1. 构建基础镜像:
   ```
   docker build -t realtime-data-generator-frontend-base:v1.0.0 -f Dockerfile_base .
   ```

2. 构建应用镜像:
   ```
   docker build -t realtime-data-generator-frontend:v1.0.0 .
   ```

3. 运行容器:
   ```
   docker run -p 13001:13001 realtime-data-generator-frontend:v1.0.0
   ```

### 后端

1. 构建基础镜像:
   ```
   docker build -t realtime-data-generator-backend-base:v1.0.0 -f Dockerfile_base .
   ```

2. 构建应用镜像:
   ```
   docker build -t realtime-data-generator-backend:v1.0.0 .
   ```

3. 运行容器:
   ```
   docker run -p 18081:18081 realtime-data-generator-backend:v1.0.0
   ```
## API 文档

后端 API 文档使用 Swagger 生成，可以在运行后端服务后访问：

http://localhost:18081/swagger-ui.html

## 后续开发 ToDoList

1. 前端优化：
   - 实现数据可视化功能，展示实时数据生成统计
   - 增加显示数据生成任务的日志流功能
   - 添加全局的错误处理机制,以及更友好的用户提示
   - 添加用户认证和授权功能

2. 后端优化：
   - 多并发场景下的统一主键id处理（防重）
   - 增加统一的异常处理机制
   - 增加统一的日志处理机制
   - 增加单元测试
   - 实现数据生成模板的自定义功能，提高数据生成能力

3. 数据库支持：
   - 添加对数仓类型的支持，如 MaxCompute、Hologres 等

4. 安全性增强：
   - 实现数据加密传输
   - 多用户管理等

5. 测试和文档：
   - 增加单元测试和集成测试覆盖率
   - 完善用户使用文档和开发文档

6. 部署和运维：
   - 添加日志收集和分析功能

## 贡献

欢迎提交 Pull Requests 来改进这个项目。在提交之前，请确保您的代码符合项目的编码规范，并且通过了所有的测试。

## 许可证

本项目遵循 [AGPL-3.0](https://www.gnu.org/licenses/agpl-3.0.en.html) 开源协议。

## 联系方式

如有任何问题或建议，请提交 [Issues](https://github.com/your-repo/issues)。
