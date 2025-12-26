import React, { useState } from 'react';
import { Card, Button, Typography, Space, Result } from 'antd';
import { useParams, useNavigate } from 'react-router-dom'; // Thêm useNavigate
import axios from 'axios';

const { Paragraph } = Typography;
const API_URL = 'https://exploratory-slurringly-basil.ngrok-free.dev/api/confirm';
 


function Confirm() {
    const [isLoading, setIsLoading] = useState(false);
    const [resultStatus, setResultStatus] = useState(null); // null, 'SUCCESS', 'EXPIRED', 'CONFLICT', 'REJECTED'
    const { txId } = useParams();
    const navigate = useNavigate(); // Hook để chuyển trang

    const handleAction = async (isConfirmed) => {
        if (isLoading) return;

        try {
            setIsLoading(true);
            const actionText = isConfirmed ? 'confirm' : 'reject';

            // Gọi API
            await axios.post(`${API_URL}/confirm`, {
                tx: txId,
                action: actionText
            });

            // Case 1: 200 OK -> Thành công
            setResultStatus(isConfirmed ? 'SUCCESS' : 'REJECTED');

        } catch(error) {
            if (error.response) {
                const status = error.response.status;
                
                // Case 2: 404 Not Found -> Hết hạn
                if (status === 404) {
                    setResultStatus('EXPIRED');
                } 
                // Case 3: 409 Conflict -> Đã có người khác xử lý
                else if (status === 409) {
                    setResultStatus('CONFLICT');
                } else {
                    setResultStatus('ERROR');
                }
            } else {
                setResultStatus('ERROR');
            }
        } finally {
            setIsLoading(false);
        }
    }

    // --- GIAO DIỆN KẾT QUẢ ---
    
    if (resultStatus === 'SUCCESS') {
        return (
            <div style={styles.container}>
                <Result
                    status="success"
                    title="Đăng nhập thành công!"
                    subTitle="Bạn có thể tắt trình duyệt này và kiểm tra trên máy tính."
                />
            </div>
        );
    }

    if (resultStatus === 'REJECTED') {
        return (
            <div style={styles.container}>
                <Result
                    status="warning"
                    title="Đã từ chối"
                    subTitle="Bạn đã hủy yêu cầu đăng nhập này."
                />
            </div>
        );
    }

    if (resultStatus === 'CONFLICT') {
        return (
            <div style={styles.container}>
                <Result
                    status="info"
                    title="Đã hoàn tất"
                    subTitle="Giao dịch này đã được xử lý trước đó."
                />
            </div>
        );
    }

    if (resultStatus === 'EXPIRED') {
        return (
            <div style={styles.container}>
                <Result
                    status="error"
                    title="Mã QR Hết hạn"
                    subTitle="Mã này không còn hiệu lực."
                    extra={[
                        <Button type="primary" key="home" onClick={() => navigate('/')}>
                            Về trang tạo mã mới
                        </Button>
                    ]}
                />
            </div>
        );
    }

    // --- GIAO DIỆN CONFIRM ---
    return (
        <div style={styles.container}>
            <Card title="Xác nhận đăng nhập" style={{ width: 400, textAlign: 'center' }}>
                <div style={{ marginBottom: 20 }}>
                    <Paragraph>
                        ID Giao dịch: <br/>
                        <code style={{fontSize: '12px', color: '#666'}}>{txId}</code>
                    </Paragraph>
                </div>
                <Space size="middle">
                    <Button 
                        onClick={() => handleAction(false)} 
                        disabled={isLoading} 
                        danger
                        size="large"
                    >
                        Từ chối
                    </Button>
                    <Button 
                        type="primary" 
                        onClick={() => handleAction(true)} 
                        loading={isLoading}
                        size="large"
                    >
                        Đồng ý
                    </Button>
                </Space>
            </Card>
        </div>
    );
}

const styles = {
    container: {
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center', 
        minHeight: '100vh', 
        background: '#f0f2f5'
    }
};

export default Confirm;