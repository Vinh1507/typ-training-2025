import { Result, Button } from 'antd';
import { CheckCircleOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';

function SuccessPage() {
    const navigate = useNavigate();

    return (
        <div style={{ 
            minHeight: '100vh',
            background: 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            padding: '20px'
        }}>
        <div style={{
            background: 'white',
            padding: '40px',
            borderRadius: '20px',
            boxShadow: '0 20px 60px rgba(0,0,0,0.3)',
            maxWidth: '500px',
            width: '100%',
            textAlign: 'center'
        }}>
            <Result
            icon={<CheckCircleOutlined style={{ color: '#11998e' }} />}
            title="Điểm danh thành công!"
            subTitle="Bạn đã được ghi nhận tham dự. Cảm ơn bạn đã sử dụng hệ thống."
            extra={[
                <Button type="primary" key="home" onClick={() => navigate('/')}>
                Quay về trang chủ
                </Button>
            ]}
            />
            
            <div style={{
                marginTop: '20px',
                padding: '15px',
                background: '#f0f0f0',
                borderRadius: '10px',
                textAlign: 'left'
            }}>
            <p style={{ margin: '8px 0' }}>
                <strong>Thời gian:</strong> {new Date().toLocaleString('vi-VN')}
            </p>
            </div>
        </div>
        </div>
    );
}

export default SuccessPage;