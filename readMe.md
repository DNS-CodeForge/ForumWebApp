# ForumWebApp

## Project Description
ForumWebApp is a forum system where users can create posts, add comments, and upvote/downvote the content they like or dislike. This specific forum revolves around discussions on [your chosen topic, e.g., "crypto trading"]. Users can share their ideas, perspectives, and engage in meaningful discussions on the topic.
***
#
# APIs
#
***
### AdminController
- **GET /admin**: Returns a welcome message for admin users.
- **GET /admin/post**: Retrieves all posts with optional filtering by title, description, user, tags, and sorting.
- **GET /admin/post/{id}**: Retrieves a specific post by ID.
- **POST /admin/post**: Creates a new post.
- **DELETE /admin/post/{id}**: Deletes a post by ID.
***
### AuthenticationController
### **POST /auth/register**:
Registers a new user.
### Fields
- **firstName** (required)
- **lastName** (required)
- **email** (required)
- **password** (required)
- **username** (required)
- **photoUrl**



### **POST /auth/login**:
Authenticates a user and returns a login response.

### Fields
- **username** (required)
- **password** (required)
- **returns JWT**
****

### CommentController

### **GET /api/comments**
Retrieves all comments.

### **GET /api/comments/{id}**
Retrieves a specific comment by ID.

### **POST /api/comments/post/{postId}**
Creates a new comment for a specific post.

### Fields
- **content** (required)

### **POST /api/comments/{id}**
Updates an existing comment by ID.

### Fields
- **content** (required)

### **DELETE /api/comments/{id}**
Deletes a comment by ID.
***

# PostController

### **GET /api/post**
Retrieves all posts with optional filtering by title, description, tags, and sorting.

### **GET /api/post/{id}**
Retrieves a specific post by ID.

### **POST /api/post**
Creates a new post.

### Fields
- **title** (required)
- **description** (required)
- **tagNames** (optional)

### **POST /api/post/{id}**
Updates an existing post by ID.

### Fields
- **title** (required)
- **description** (required)
- **tags** (optional)

### **DELETE /api/post/{id}**
Deletes a post by ID.
***

# TagController

### **GET /api/tag**
Retrieves all tags.

### **GET /api/tag/{id}**
Retrieves a specific tag by ID.

### **POST /api/tag**
Creates a new tag.

### Fields
- **name** (required)

### **DELETE /api/tag/{id}**
Deletes a tag by ID.
***

# UserController

### **POST /user/profile/info**
Updates the user's profile information.

### Fields
- **password** (optional)
- **firstName** (optional)
- **lastName** (optional)

### **GET /user/profile/info**
Retrieves the user's profile information.

### **GET /user/{id}**
Retrieves a specific user by ID.

### **GET /user**
Retrieves all users.

### **DELETE /user/{id}**
Deletes a user by ID.
***
#
# Models
### **SQL Scripts for generating Database and Mock Data (dbCreation.sql & mockData.sql).**
***
### User
- **Attributes**:
    - `id`: Unique identifier for the user.
    - `firstName`: First name (4-32 characters).
    - `lastName`: Last name (4-32 characters).
    - `email`: Valid and unique email.
    - `username`: Unique username.
    - `password`: User's password.
    - `role`: Role of the user (USER, MODERATOR, ADMIN, BANNED).
    - `profilePhotoUrl`: URL of the user's profile photo.
- **Relationships**:
    - A user can have multiple posts.
    - A user can have multiple comments.

### Post
- **Attributes**:
    - `id`: Unique identifier for the post.
    - `user`: The user who created the post.
    - `title`: Title of the post (16-64 characters).
    - `content`: Content of the post (32-8192 characters).
    - `likes`: Number of likes the post has received.
    - `comments`: List of comments on the post.
    - `tags`: List of tags associated with the post.
- **Relationships**:
    - A post is created by a user.
    - A post can have multiple comments.
    - A post can have multiple tags.

### Comment
- **Attributes**:
    - `id`: Unique identifier for the comment.
    - `user`: The user who created the comment.
    - `post`: The post the comment belongs to.
    - `content`: Content of the comment.
- **Relationships**:
    - A comment is created by a user.
    - A comment belongs to a post.

### Tag
- **Attributes**:
    - `id`: Unique identifier for the tag.
    - `name`: Name of the tag.
- **Relationships**:
    - A tag can be associated with multiple posts.

#

#

#
