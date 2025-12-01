
import { useState, useEffect } from 'react';
import { Card, Spin, Alert, Statistic, Row, Col, Tag, Button, Space, Typography, Divider, message } from 'antd';
import { QrcodeOutlined, CheckCircleOutlined, ClockCircleOutlined, UserOutlined, ReloadOutlined } from '@ant-design/icons';
import { qrAPI } from '../services/api';

const { Title, Text } = Typography;


function Dashboard() {
    const [qrData, setQrData] = useState(null);
    const [logs, setLogs] = useState(null);
    const [stats, setStats] = useState({ total: 0, today: 0, qrValid: false });
    const [loading, setLoading] = useState(true);
    const [logsLoading, setLogsLoading] = useState(false);
    const [currentTime, setCurrentTime] = useState(new Date());

    useEffect(() => {
        fetchQRCode();
        fetchLogs();
        fetchStats();
        
        const timer = setInterval(() => setCurrentTime(new Date()), 1000);
        
        const qrRefresh = setInterval(() => {
            fetchQRCode();
        }, 30 * 1000);
        
        return () => {
            clearInterval(timer);
            clearInterval(qrRefresh);
        };
    }, []);

    const fetchQRCode = async () => {
        setLoading(true);
        try {
            const data = await qrAPI.getQRCode();
            setQrData(data);
            message.success('QR code đã được tải');
        } catch (error) {
            console.error('Error fetching QR:', error);
            message.error('Không thể tải QR code');
        }
        setLoading(false);
    };

    const fetchLogs = async () => {
        setLogsLoading(true);
        try {
            const data = await qrAPI.getLogs(10);
            setLogs(data);
        } catch (error) {
            console.error('Error fetching logs:', error);
        }
        setLogsLoading(false);
    };

    const fetchStats = async () => {
        try {
            const data = await qrAPI.getStats();
            setStats(data);
        } catch (error) {
            console.error('Error fetching stats:', error);
        }
    };

    const handleGenerateNew = async () => {
        setLoading(true);
        try {
            const data = await qrAPI.generateQRCode();
            setQrData(data);
            message.success('QR code mới đã được tạo');
        } catch (error) {
            console.error('Error generating QR:', error);
            message.error('Không thể tạo QR code mới');
        }
        setLoading(false);
    };

    const handleRefreshLogs = async () => {
        await fetchLogs();
        await fetchStats();
        message.success('Dữ liệu đã được làm mới');
    };

    const formatTime = (date) => {
        return date.toLocaleTimeString('vi-VN', { 
        hour: '2-digit', 
        minute: '2-digit', 
        second: '2-digit' 
        });
    };

    const formatDate = (dateString) => {
        return new Date(dateString).toLocaleString('vi-VN');
    };

    return (
        <div style={{ 
        minHeight: '100vh', 
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        padding: '40px 20px'
        }}>
        <div style={{ maxWidth: 1200, margin: '0 auto' }}>
            {/* Header */}
            <div style={{ textAlign: 'center', marginBottom: 40 }} className="fade-in">
            <Title level={1} style={{ color: 'white', marginBottom: 8 }}>
                <QrcodeOutlined /> QR Code Attendance
            </Title>
            <Text style={{ color: 'rgba(255,255,255,0.9)', fontSize: 16 }}>
                Hệ thống điểm danh thông minh
            </Text>
            </div>

            {/* Stats Row */}
            <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
            <Col xs={24} sm={8}>
                <Card className="fade-in">
                <Statistic
                    title="Thời gian hiện tại"
                    value={formatTime(currentTime)}
                    prefix={<ClockCircleOutlined />}
                    valueStyle={{ color: '#3f8600', fontSize: 20 }}
                />
                </Card>
            </Col>
            <Col xs={24} sm={8}>
                <Card className="fade-in">
                <Statistic
                    title="Tổng lượt điểm danh"
                    value={stats.total || 0}
                    prefix={<CheckCircleOutlined />}
                    valueStyle={{ color: '#1890ff', fontSize: 24 }}
                />
                </Card>
            </Col>
            <Col xs={24} sm={8}>
                <Card className="fade-in">
                <Statistic
                    title="Hôm nay"
                    value={stats.today || 0}
                    prefix={<UserOutlined />}
                    valueStyle={{ color: '#cf1322', fontSize: 24 }}
                />
                </Card>
            </Col>
            </Row>

            {/* Main Content */}
            <Row gutter={[16, 16]}>
            {/* QR Code Section */}
            <Col xs={24} md={12}>
                <Card
                className="fade-in"
                title={
                    <Space>
                    <QrcodeOutlined />
                    <span>Mã QR Điểm Danh</span>
                    </Space>
                }
                extra={
                    <Button 
                    icon={<ReloadOutlined />} 
                    onClick={handleGenerateNew}
                    loading={loading}
                    type="primary"
                    >
                    Tạo mới
                    </Button>
                }
                style={{ height: '100%' }}
                >
                {loading ? (
                    <div style={{ textAlign: 'center', padding: 60 }}>
                    <Spin size="large" tip="Đang tạo mã QR..." />
                    </div>
                ) : qrData ? (
                    <div style={{ textAlign: 'center' }}>
                    <div style={{
                        display: 'inline-block',
                        padding: 20,
                        background: 'white',
                        borderRadius: 12,
                        boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
                    }}>
                        <img 
                        src={qrData.qrImage} 
                        alt="QR Code" 
                        style={{ 
                            width: '100%', 
                            maxWidth: 300,
                            border: '4px solid #667eea',
                            borderRadius: 8
                        }} 
                        />
                    </div>
                    
                    <Divider />
                    
                    <Space direction="vertical" size="small" style={{ width: '100%' }}>
                        <div>
                        <Text strong>Token:</Text>
                        <br />
                        <Tag color="blue" style={{ fontFamily: 'monospace', fontSize: 14, marginTop: 8 }}>
                            {qrData.token}
                        </Tag>
                        </div>
                        
                        <div style={{ marginTop: 12 }}>
                        <Text strong>URL:</Text>
                        <br />
                        <Text 
                            copyable 
                            style={{ 
                            fontSize: 12, 
                            wordBreak: 'break-all',
                            color: '#666'
                            }}
                        >
                            {qrData.url}
                        </Text>
                        </div>
                    </Space>

                    <Alert
                        message="Hướng dẫn sử dụng"
                        description="Quét mã QR này bằng điện thoại để điểm danh. Mỗi mã có hiệu lực trong 30 giây."
                        type="info"
                        showIcon
                        style={{ marginTop: 16, textAlign: 'left' }}
                    />
                    </div>
                ) : (
                    <Alert message="Không thể tạo mã QR" type="error" showIcon />
                )}
                </Card>
            </Col>

            {/* Logs Section */}
            <Col xs={24} md={12}>
                <Card
                className="fade-in"
                title={
                    <Space>
                    <CheckCircleOutlined />
                    <span>Lịch sử điểm danh</span>
                    </Space>
                }
                extra={
                    <Button 
                    icon={<ReloadOutlined />} 
                    onClick={handleRefreshLogs}
                    loading={logsLoading}
                    >
                    Làm mới
                    </Button>
                }
                style={{ height: '100%', maxHeight: 600, overflow: 'auto' }}
                >
                {logsLoading ? (
                    <div style={{ textAlign: 'center', padding: 60 }}>
                    <Spin size="large" />
                    </div>
                ) : logs && logs.logs && logs.logs.length > 0 ? (
                    <Space direction="vertical" size="middle" style={{ width: '100%' }}>
                    {logs.logs.map((log) => (
                        <Card
                        key={log.id}
                        size="small"
                        style={{ 
                            background: '#f5f5f5',
                            border: '1px solid #d9d9d9'
                        }}
                        >
                        <Row gutter={[8, 8]}>
                            <Col span={24}>
                            <Space>
                                <CheckCircleOutlined style={{ color: '#52c41a' }} />
                                <Text strong>Điểm danh thành công</Text>
                            </Space>
                            </Col>
                            <Col span={12}>
                            <Text type="secondary" style={{ fontSize: 12 }}>
                                <ClockCircleOutlined /> {formatDate(log.timestamp)}
                            </Text>
                            </Col>
                            <Col span={12} style={{ textAlign: 'right' }}>
                            <Tag color="green">{log.userAgent}</Tag>
                            </Col>
                            <Col span={24}>
                            <Text style={{ fontSize: 12, fontFamily: 'monospace' }}>
                                IP: {log.ip}
                            </Text>
                            </Col>
                        </Row>
                        </Card>
                    ))}
                    
                    <Divider />
                    
                    <div style={{ textAlign: 'center' }}>
                        <Text type="secondary">
                        Hiển thị {logs.logs.length} / {logs.total} bản ghi
                        </Text>
                    </div>
                    </Space>
                ) : (
                    <Alert
                    message="Chưa có dữ liệu"
                    description="Chưa có ai điểm danh. Quét mã QR để bắt đầu!"
                    type="warning"
                    showIcon
                    />
                )}
                </Card>
            </Col>
            </Row>

            {/* Footer */}
            <div style={{ 
            textAlign: 'center', 
            marginTop: 40, 
            color: 'rgba(255,255,255,0.8)' 
            }}>
            <Text style={{ color: 'white' }}>
                @ 2025 QR Attendance System | Powered by React + Ant Design + Docker + vdtry06
            </Text>
            </div>
        </div>
        </div>
    );
}

export default Dashboard;