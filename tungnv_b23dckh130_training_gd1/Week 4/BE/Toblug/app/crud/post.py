import random
import string
from sqlalchemy.orm import Session, joinedload
from slugify import slugify
from markdown import markdown

from app.schemas.post import PostCreate
from app.models.post import Post


def create_post(
    db: Session,
    post_in: PostCreate,
    user_id: int
):
    slug = slugify(post_in.title)
    random_string = ''.join(random.choices(string.ascii_lowercase + string.digits, k=6))
    slug = f"{slug}-{random_string}"
    content_html = markdown(post_in.content_raw)

    db_post = Post(
        title=post_in.title,
        slug=slug,
        content_raw=post_in.content_raw,
        content_html=content_html,
        view_count=0,
        is_published=post_in.is_published,
        user_id=user_id
    )

    db.add(db_post)
    db.commit()
    db.refresh(db_post)
    return db_post

def get_multi(db: Session, skip: int = 0, limit: int = 10):
    return db.query(Post).offset(skip).limit(limit).all()

def get_by_slug(db: Session, slug: str):
    return db.query(Post).options(joinedload(Post.owner)).filter(slug == Post.slug).first()

def update_view_count(
    db: Session,
    post_id: int
):
    post = db.query(Post).filter(Post.id == post_id).first()
    if post:
        post.view_count += 1
        db.commit()
        db.refresh(post)
    return post