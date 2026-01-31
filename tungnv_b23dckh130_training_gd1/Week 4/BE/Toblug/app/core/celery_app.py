from celery import Celery
import os
from dotenv import load_dotenv

load_dotenv()

REDIS_BROKER_URL = os.getenv("REDIS_BROKER_URL")
REDIS_BACKEND_URL = os.getenv("REDIS_BACKEND_URL")

celery_app = Celery(
    "worker",
    broker=REDIS_BROKER_URL,
    backend=REDIS_BACKEND_URL
)

celery_app.conf.update(
    task_serializer="json", # Quy định cách giao tiếp chung
    accept_content=["json"],
    result_serializer="json",
    timezone="Asia/Ho_Chi_Minh",
    enable_utc=True,
)

celery_app.conf.imports = [
    "app.tasks.email_tasks"
]