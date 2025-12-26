import React, { useState, useEffect, useCallback, useRef } from 'react';
import axios from 'axios';
import { QRCode, Result, Button } from 'antd';

const API_URL = 'https://exploratory-slurringly-basil.ngrok-free.dev/api/login';
const QR_REFRESH_INTERVAL = 15000; // 15 giây đổi mã 1 lần
const STATUS_CHECK_INTERVAL = 2000; // 2 giây check trạng thái 1 lần

function Login() {
    const [username, setUsername] = useState('');
    const [qrData, setQrData] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    //-Make time counter UI
    const [qrCountdown, setQrCountdown] = useState(QR_REFRESH_INTERVAL / 1000);
    const countdownRef = useRef();
    
    // --- CÁC TRẠNG THÁI KẾT QUẢ ---
    const [loginSuccess, setLoginSuccess] = useState(false); 
    const [loginRejected, setLoginRejected] = useState(false); 

    // Hàm tạo Transaction mới
    const generateTransaction = useCallback(async (currentUsername) => {
        if (!currentUsername) return;
        try {
            const response = await axios.post(`${API_URL}/initiate`, {
                username: currentUsername
            });
            console.log('QR Code mới:', response.data.tx.tx);
            setQrData(response.data);
        } catch (error) {
            console.error('Lỗi tạo QR:', error);
        }
    }, []);

    // Hàm Reset để thử lại (Khi user bấm nút "Thử lại" hoặc "Đăng xuất")
    const handleRetry = () => {
        setLoginSuccess(false);
        setLoginRejected(false);
        setQrData(null);
        setUsername('');
    }

    // Hàm check trạng thái (Polling)
    const checkStatus = useCallback(async () => {
        if (!qrData || !qrData.tx) return;
        try {
            const txId = qrData.tx.tx;
            // THÊM: ?t=${Date.now()} để chống Cache
            const res = await axios.get(`${API_URL}/verify/${txId}?t=${Date.now()}`);
            
            console.log(`Check status ${txId}:`, res.data.status); // Log để kiểm tra
            
            // CASE 1: Thành công
            if (res.data.status === 'APPROVED') {
                setLoginSuccess(true);
                setQrData(null); 
            }

            // CASE 2: Bị từ chối (Logic dừng request)
            if (res.data.status === 'DENIED') {
                setLoginRejected(true); 
                setQrData(null);
            }
        } catch (error) {
            // Bỏ qua lỗi
        }
    }, [qrData]);

    const handleLogin = () => {
        if (!username) {
            alert('Vui lòng nhập username');
            return;
        }
        setIsLoading(true);
        generateTransaction(username).finally(() => setIsLoading(false));
    }

    // EFFECT 1: Tự động đổi mã QR mỗi 15s
    useEffect(() => {
        let intervalId;
        // 2. SỬA: Thêm điều kiện !loginRejected để dừng khi bị từ chối
        if (qrData && !loginSuccess && !loginRejected) {
            intervalId = setInterval(() => {
                generateTransaction(username);
                setQrCountdown(QR_REFRESH_INTERVAL / 1000); // Reset countdown
            }, QR_REFRESH_INTERVAL);
        }
        return () => clearInterval(intervalId);
    }, [qrData, username, generateTransaction, loginSuccess, loginRejected]);

    // EFFECT 2: Tự động kiểm tra trạng thái mỗi 2s
    useEffect(() => {
        let statusInterval;
        // 2. SỬA: Thêm điều kiện !loginRejected để dừng check khi bị từ chối
        if (qrData && !loginSuccess && !loginRejected) {
            statusInterval = setInterval(() => {
                checkStatus();
            }, STATUS_CHECK_INTERVAL);
        }
        return () => clearInterval(statusInterval);
    }, [qrData, checkStatus, loginSuccess, loginRejected]);

    // Countdown effect
    useEffect(() => {
        if (!qrData || loginSuccess || loginRejected) return;
        countdownRef.current && clearInterval(countdownRef.current);
        countdownRef.current = setInterval(() => {
            setQrCountdown(prev => {
                if (prev <= 1) return QR_REFRESH_INTERVAL / 1000;
                return prev - 1;
            });
        }, 1000);
        return () => clearInterval(countdownRef.current);
    }, [qrData, loginSuccess, loginRejected]);

    // --- GIAO DIỆN ---
    return (
        <div style={{ padding: '50px', textAlign: 'center', fontFamily: 'Arial, sans-serif' }}>
            <h1>Hệ thống Đăng nhập QR</h1>

            {/* TRƯỜNG HỢP 1: Đã đăng nhập thành công */}
            {loginSuccess ? (
                <Result
                    status="success"
                    title={`Chào mừng, ${username}!`}
                    subTitle="Bạn đã đăng nhập thành công trên thiết bị này."
                    extra={[
                        <Button type="primary" key="console" onClick={handleRetry}>
                            Đăng xuất
                        </Button>,
                    ]}
                />
            ) : loginRejected ? (
                /* 3. SỬA: Thêm giao diện hiển thị khi bị TỪ CHỐI */
                <Result
                    status="error"
                    title="Đăng nhập bị từ chối"
                    subTitle="Bạn đã chọn 'No' (Từ chối) trên thiết bị di động."
                    extra={[
                        <Button type="primary" key="retry" onClick={handleRetry}>
                            Thử lại
                        </Button>,
                    ]}
                />
            ) : (
                /* TRƯỜNG HỢP 3: Form nhập hoặc Mã QR */
                <div>
                    {!qrData ? (
                        /* Form nhập Username */
                        <div style={{ maxWidth: '400px', margin: '0 auto' }}>
                            <p style={{color:"black"}}>Nhập username để bắt đầu:</p>
                            <input 
                                type="text"
                                placeholder="Username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                style={{ padding: '10px', fontSize: '16px', width: '200px', marginRight: '10px' }}
                            />
                            <button 
                                onClick={handleLogin}
                                disabled={isLoading}
                                style={{ padding: '10px', fontSize: '16px', cursor: 'pointer' }}
                            >
                                {isLoading ? 'Đang tải...' : 'Lấy mã QR'}
                            </button>
                        </div>
                    ) : (
                        /* Hiển thị QR */
                        <div>
                            <h2>Đang chờ xác nhận từ: <span style={{color: 'blue'}}>{username}</span></h2>
                            
                            <div style={{ background: '#fff', padding: '20px', display: 'inline-block', borderRadius: '10px', boxShadow: '0 4px 12px rgba(0,0,0,0.1)' }}>
                                <QRCode
                                    key={qrData.qrUrl} 
                                    value={qrData.qrUrl}
                                    size={256}
                                />
                            </div>
                            {/* Countdown UI */}
                            <div style={{ marginTop: '16px', width: 256, marginLeft: 'auto', marginRight: 'auto' }}>
                                <div style={{ marginBottom: 4, color: '#555', fontSize: 14 }}>
                                    Mã sẽ đổi sau: <b>{qrCountdown}s</b>
                                </div>
                                <div style={{
                                    height: 8,
                                    background: '#eee',
                                    borderRadius: 4,
                                    overflow: 'hidden',
                                    position: 'relative'
                                }}>
                                    <div style={{
                                        width: `${(qrCountdown / (QR_REFRESH_INTERVAL / 1000)) * 100}%`,
                                        height: '100%',
                                        background: '#1890ff',
                                        transition: 'width 1s linear'
                                    }} />
                                </div>
                            </div>

                            <div style={{ marginTop: '20px', color: '#888' }}>
                                <p>Quét mã bằng điện thoại để đăng nhập</p>
                                <p style={{ fontSize: '12px' }}>Mã tự động đổi sau mỗi 15s.</p>
                                <p>ID: <code>{qrData.tx?.tx}</code></p>
                            </div>
                            
                            <button 
                                onClick={handleRetry}
                                style={{ marginTop: '20px', padding: '8px 16px' }}
                            >
                                Hủy bỏ
                            </button>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

export default Login;