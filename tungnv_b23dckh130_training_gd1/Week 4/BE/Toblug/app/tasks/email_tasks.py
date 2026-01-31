import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from app.core.celery_app import celery_app
from app.core.config import settings

@celery_app.task(name="send_email_welcome", bind=True, max_retries=3, default_retry_delay=30)
def send_email_welcome(self, email_to: str, username: str):
    html_content = f"""
    <html>
        <body style="font-family: Arial, sans-serif;">
            <div style="background-color: #f4f4f4; padding: 20px;">
                <h2 style="color: #333;">Chào mừng {username} đến với Toblug!</h2>
                <p>Cảm ơn bạn đã đăng ký tài khoản.</p>
                <p>Chúng tôi rất vui được đồng hành cùng bạn trên con đường lập trình.</p>
                <br>
                <a href="http://localhost:3000" style="background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Truy cập ngay</a>
                <p style="font-size: 12px; color: #888;">Đây là email tự động, vui lòng không trả lời.</p>
            </div>
        </body>
    </html>
    """

    message = MIMEMultipart("alternative")
    message["Subject"] = "Chào mừng đến Toblug"
    message["From"] = f"{settings.MAIL_FROM_NAME} <{settings.MAIL_FROM}>"
    message["To"] = email_to

    html_part = MIMEText(html_content, "html")
    message.attach(html_part)

    try:
        with smtplib.SMTP(settings.MAIL_SERVER, 587, timeout=30) as server:
            server.ehlo()
            server.starttls()
            server.ehlo()
            server.login(settings.MAIL_USERNAME, settings.MAIL_PASSWORD)
            server.send_message(message)
        
        return f"Gửi mail thành công tới {email_to}"
        
    except smtplib.SMTPAuthenticationError as e:
        return f"Lỗi xác thực: {e}"
        
    except smtplib.SMTPException as e:
        try:
            raise self.retry(exc=e, countdown=30)
        except self.MaxRetriesExceededError:
            return f"Lỗi sau {self.max_retries} lần thử: {e}"
            
    except Exception as e:
        try:
            raise self.retry(exc=e, countdown=30)
        except self.MaxRetriesExceededError:
            return f"Lỗi sau {self.max_retries} lần thử: {e}"