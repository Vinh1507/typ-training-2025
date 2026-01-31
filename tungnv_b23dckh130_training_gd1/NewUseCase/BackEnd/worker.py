import json
import pika
import sys
import os

sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from app.core.config import settings
from app.core.database import SessionLocal
from app.models.order import Order


def process_order_job(ch, method, properties, body):
    db = SessionLocal()
    try:
        data = json.loads(body)
        print(f" [x] Đang xử lý đơn của User ID: {data['user_id']} mua SP {data['product_id']}")

        new_order = Order(
            user_id=data['user_id'],
            product_id=data['product_id'],
            status="success"
        )
        db.add(new_order)
        db.commit()
        print(" [v] ✅ Đã lưu vào DB thành công!")

        # Xoa ACK
        ch.basic_ack(delivery_tag=method.delivery_tag)

    except Exception as e:
        print(f" [!] Lỗi xử lý: {e}")
    finally:
        db.close()


def start_worker():
    credentials = pika.PlainCredentials(settings.RABBIT_USER, settings.RABBIT_PASSWORD)
    parameters = pika.ConnectionParameters(host=settings.RABBIT_HOST, port=settings.RABBIT_PORT,
                                           credentials=credentials)
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()

    channel.queue_declare(queue='flashsale_orders', durable=True)

    channel.basic_qos(prefetch_count=1)

    channel.basic_consume(queue='flashsale_orders', on_message_callback=process_order_job)

    print(' [*] Worker đang chờ đơn hàng... Nhấn CTRL+C để thoát')
    channel.start_consuming()


if __name__ == '__main__':
    try:
        start_worker()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)