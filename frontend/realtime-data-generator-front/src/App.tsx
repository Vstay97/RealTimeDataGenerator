import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Link, Routes } from 'react-router-dom';
import { Layout, Menu, Form, Input, Button, Select, InputNumber, Table, Space, message, Typography } from 'antd';
import { UserOutlined, LaptopOutlined } from '@ant-design/icons';
import axios from 'axios';
import { ColumnsType } from 'antd/es/table';

const { Header, Content, Sider } = Layout;
const { Option } = Select;
const { Title } = Typography;

interface Task {
    taskName: string;
    taskType: string;
    threadCount: number;
}

interface SubmitTaskFormValues {
    dbType: string;
    databaseName: string;
    threadCount: number;
    jdbcUrl: string;
    username: string;
    password: string;
}

interface SubmitTaskPageProps {
    onTaskSubmit: () => void;
}

interface TaskListPageProps {
    tasks: Task[];
    onStopTask: (dbType: string, databaseName: string) => void;
}

// 提交任务页面
const SubmitTaskPage: React.FC<SubmitTaskPageProps> = ({ onTaskSubmit }) => {
    const [form] = Form.useForm<SubmitTaskFormValues>();

    const handleSubmit = async (values: SubmitTaskFormValues) => {
        try {
            const response = await axios.post('http://localhost:18081/generator/start', values);
            if (response.data === "success") {
                message.success('任务提交成功');
                onTaskSubmit();
            } else {
                message.error('任务提交失败');
            }
        } catch (error) {
            message.error('提交任务时发生错误');
            console.error('Error submitting task:', error);
        }
        form.resetFields();
    };

    return (
        <div>
            <h2>提交新任务</h2>
            <Form form={form} onFinish={handleSubmit} layout="vertical">
                <Form.Item name="dbType" label="数据库类型" rules={[{ required: true }]}>
                    <Select placeholder="选择类型">
                        <Option value="mysql">MySQL</Option>
                        <Option value="oracle">Oracle</Option>
                        <Option value="pgsql">PostgreSQL</Option>
                        <Option value="db2">DB2</Option>
                        <Option value="ob_mysql">OceanBase（MySQL 模式）</Option>
                        <Option value="ob_oracle">OceanBase（Oracle 模式）</Option>
                    </Select>
                </Form.Item>
                <Form.Item name="databaseName" label="数据库名称" rules={[{ required: true }]}>
                    <Input />
                </Form.Item>
                <Form.Item name="threadCount" label="线程数" rules={[{ required: true }]}>
                    <InputNumber min={1} />
                </Form.Item>
                <Form.Item name="jdbcUrl" label="JDBC URL" rules={[{ required: true }]}>
                    <Input />
                </Form.Item>
                <Form.Item name="username" label="用户名" rules={[{ required: true }]}>
                    <Input />
                </Form.Item>
                <Form.Item name="password" label="密码" rules={[{ required: true }]}>
                    <Input.Password />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit">
                        提交任务
                    </Button>
                </Form.Item>
            </Form>
        </div>
    );
};

const TaskListPage: React.FC<TaskListPageProps> = ({ tasks, onStopTask }) => {
    const columns: ColumnsType<Task> = [
        {
            title: '任务名称',
            dataIndex: 'taskName',
            key: 'taskName',
            render: (taskName: string) => {
                const [dbType, databaseName] = taskName.split('-');
                return `${databaseName} (${dbType})`;
            },
        },
        {
            title: '数据库类型',
            dataIndex: 'taskType',
            key: 'taskType',
        },
        {
            title: '线程数',
            dataIndex: 'threadCount',
            key: 'threadCount',
        },
        {
            title: '操作',
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <Button onClick={() => {
                        const [dbType, databaseName] = record.taskName.split('-');
                        onStopTask(dbType, databaseName);
                    }}>停止</Button>
                </Space>
            ),
        },
    ];

    return (
        <div>
            <h2>任务列表</h2>
            <Table columns={columns} dataSource={tasks} rowKey="taskName" />
        </div>
    );
};

// 主应用组件
const App: React.FC = () => {
    const [tasks, setTasks] = useState<Task[]>([]);

    const fetchTasks = async () => {
        try {
            const response = await axios.get<Task[]>('http://localhost:18081/generator/tasks');
            setTasks(response.data);
        } catch (error) {
            console.error('Error fetching tasks:', error);
            message.error('获取任务列表失败');
        }
    };

    useEffect(() => {
        fetchTasks();
    }, []);

    const handleTaskSubmit = () => {
        fetchTasks();
    };

    const handleStopTask = async (dbType: string, databaseName: string) => {
        try {
            const response = await axios.post('http://localhost:18081/generator/stop', { dbType, databaseName });
            if (response.data === "success") {
                message.success('任务已停止');
                fetchTasks();
            } else {
                message.error('停止任务失败');
            }
        } catch (error) {
            console.error('Error stopping task:', error);
            message.error('停止任务时发生错误');
        }
    };

    return (
        <Router>
            <Layout style={{ minHeight: '100vh' }}>
                <Header className="header" style={{ background: '#003153', padding: '0 20px' }}>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-start', height: '100%', position: 'relative' }}>
                        <Title level={3} style={{ margin: '0 20px 0 0', color: '#f0fcff' }}>实时数据流生成平台</Title>
                        <Title level={5} style={{ margin: 0, color: '#f0fcff', position: 'absolute', bottom: 0, left: '150px' }}>数据服务团队</Title>
                    </div>
                </Header>


                <Layout>
                    <Sider width={200} className="site-layout-background" >
                        <Menu
                            mode="inline"
                            defaultSelectedKeys={['1']}
                            style={{ height: '100%', borderRight: 0 }}
                        >
                            <Menu.Item key="1" icon={<UserOutlined />}>
                                <Link to="/">提交任务</Link>
                            </Menu.Item>
                            <Menu.Item key="2" icon={<LaptopOutlined />}>
                                <Link to="/tasks">任务列表</Link>
                            </Menu.Item>
                        </Menu>
                    </Sider>
                    <Layout style={{ padding: '0 24px 24px' }}>
                        <Content
                            className="site-layout-background"
                            style={{
                                padding: 24,
                                margin: 0,
                                minHeight: 280,
                            }}
                        >
                            <Routes>
                                <Route path="/" element={<SubmitTaskPage onTaskSubmit={handleTaskSubmit} />} />
                                <Route path="/tasks" element={<TaskListPage tasks={tasks} onStopTask={handleStopTask} />} />
                            </Routes>
                        </Content>
                    </Layout>
                </Layout>
            </Layout>
        </Router>
    );
};

// export default App;

export default App;
