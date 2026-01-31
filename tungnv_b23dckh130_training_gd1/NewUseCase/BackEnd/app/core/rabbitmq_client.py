import pika
import json

from app.core.config import settings


class RabbitMQClient:
    def __init__(self):
        self.connection = None
        self.channel = None

    def connect(self):
        credentials = pika.PlainCredentials(settings.RABBIT_USER, settings.RABBIT_PASSWORD)
        parameters = pika.ConnectionParameters(host=settings.RABBIT_HOST, port=settings.RABBIT_PORT, credentials=credentials)
        self.connection = pika.BlockingConnection(parameters)
        self.channel = self.connection.channel()

        self.channel.queue_declare(queue='flashsale_orders', durable=True)

    def publish_message(self, message: dict):
        if not self.connection or self.connection.is_closed:
            self.connect()

        self.channel.basic_publish(
            exchange='',
            routing_key='flashsale_orders',
            body=json.dumps(message),
            properties=pika.BasicProperties(
                delivery_mode=2
            )
        )

mq_client = RabbitMQClient()