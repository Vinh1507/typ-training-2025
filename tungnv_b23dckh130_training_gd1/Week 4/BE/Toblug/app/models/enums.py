from enum import Enum

class UserRole(str, Enum):
    ADMIN = "admin"
    USER = "user"

class VoteType(str, Enum):
    UPVOTE = "upvote"
    DOWNVOTE = "downvote"