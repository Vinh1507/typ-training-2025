from typing import Any
from sqlalchemy.ext.declarative import as_declarative, declared_attr

# Định nghĩa là một class đặc biệt
# Chương trình hiểu rằng mọi class con kế thừa tương ứng một bảng trong db
@as_declarative()
class Base:
    id: Any
    __name__:str

    @declared_attr
    def __tablename__(cls) -> str:
        return cls.__name__.lower()