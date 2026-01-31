from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    DATABASE_URL: str
    SECRET_KEY: str
    ACCESS_TOKEN_EXPIRE_MINUTES: int

    # REDIS
    REDIS_HOST: str
    REDIS_PORT: int

    # RABBITMQ
    RABBIT_HOST: str
    RABBIT_PORT: int
    RABBIT_USER: str
    RABBIT_PASSWORD: str

    class Config:
        env_file = ".env"

settings = Settings()