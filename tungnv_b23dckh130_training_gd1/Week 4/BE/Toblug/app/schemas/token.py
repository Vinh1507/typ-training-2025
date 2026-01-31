from pydantic import BaseModel

class Token(BaseModel):
    access_token: str
    refresh_token: str
    token_type: str

class TokenPayLoad(BaseModel):
    sub: str | None = None
    type: str | None = None