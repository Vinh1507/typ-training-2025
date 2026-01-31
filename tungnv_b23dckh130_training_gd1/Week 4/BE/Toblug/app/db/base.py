# Nhiệm vụ giúp alembic hiểu được cấu trúc db
from app.db.base_class import Base

#import models
from app.models.comment import Comment
from app.models.post import Post
from app.models.series import Series
from app.models.seriesPost import SeriesPost
from app.models.tag import Tag
from app.models.user import User
from app.models.vote import Vote