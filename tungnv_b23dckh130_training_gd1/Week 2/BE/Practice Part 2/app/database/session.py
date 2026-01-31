from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import os
from dotenv import load_dotenv
from pathlib import Path

env_path = Path(__file__).parent.parent.parent / ".env"
load_dotenv(dotenv_path=env_path)

DATABASE_URL = os.getenv("DATABASE_URL")

if not DATABASE_URL:
    raise ValueError(
        f"ERROR: DATABASE_URL khong tim thay!\n"
        f"File .env: {env_path}\n"
        f"File .env co ton tai? {env_path.exists()}\n"
        "Vui long tao file .env voi noi dung:\n"
        "DATABASE_URL=mysql+pymysql://root@localhost:3306/items_db"
    )

engine = create_engine(
    DATABASE_URL,
    pool_pre_ping=True,
    pool_recycle=3600,
    pool_size=5,
    max_overflow=10,
    echo=False
)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
