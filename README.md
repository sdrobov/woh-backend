# WoH.ru backend

## API

- [User API](#user-api)
  * [GET `/user`](#get-user)
  * [GET `/user/{id:[0-9]*}`](#get-userid0-9)
  * [POST `/user/login`](#post-userlogin)
  * [POST `/user/register`](#post-userregister)
  * [POST `/user/save`](#post-usersave)
  * [POST `/user/password`](#post-userpassword)
  * [POST `/user/avatar/`](#post-useravatar)
  * [POST `/user/avatar/drop/`](#post-useravatardrop)
- [Post API](#post-api)
  * [GET `/`](#get-)
  * [GET `/by-tag/{tag:.*}`](#get-by-tagtag)
  * [GET `/{id:[0-9]*}`](#get-id0-9)
  * [POST `/{id:[0-9]*}`](#post-id0-9)
  * [POST `/add`](#post-add)
  * [POST `/{id:[0-9]*}/delete`](#post-id0-9delete)
  * [POST `/{id:[0-9]*}/approve`](#post-id0-9approve)
  * [POST `/{id:[0-9]*}/dismiss`](#post-id0-9dismiss)
  * [POST `/{id:[0-9]*}/like`](#post-id0-9like)
  * [POST `/{id:[0-9]*}/dislike`](#post-id0-9dislike)
  * [GET `/published/`](#get-published)
  * [GET `/published-waiting/`](#get-published-waiting)
  * [GET `/published-waiting/{date:.*}/`](#get-published-waitingdate)
  * [GET `/moderation-waiting/`](#get-moderation-waiting)
- [Source API](#source-api)
  * [GET `/source/`](#get-source)
  * [GET `/source/{id:[0-9]*}/`](#get-sourceid0-9)
  * [GET `/source/run/{id:[0-9]*}/`](#get-sourcerunid0-9)
  * [POST `/source/add/`](#post-sourceadd)
  * [POST `/source/edit/`](#post-sourceedit)
  * [POST `/source/delete/{id:[0-9]*}/`](#post-sourcedeleteid0-9)
- [Post preview API](#post-preview-api)
  * [GET `/post-preview/`](#get-post-preview)
  * [GET `/post-preview/{id:[0-9]+}`](#get-post-previewid0-9)
  * [GET `/post-preview/by-source/{id:[0-9]+}`](#get-post-previewby-sourceid0-9)
- [Image API](#image-api)
  * [GET `/image/{id:.*}`](#get-imageid)
- [Comment API](#comment-api)
  * [GET `/{id:[0-9]*}/comments`](#get-id0-9comments)
  * [POST `/{id:[0-9]*}/comments`](#post-id0-9comments)
  * [POST `/{id:[0-9]*}/comments/edit/`](#post-id0-9commentsedit)
  * [POST `/{postId:[0-9]*}/comments/delete/{id:[0-9]*}`](#post-postid0-9commentsdeleteid0-9)
  * [POST `/{postId:[0-9]*}/comments/like/{id:[0-9]*}`](#post-postid0-9commentslikeid0-9)
  * [POST `/{postId:[0-9]*}/comments/dislike/{id:[0-9]*}`](#post-postid0-9commentsdislikeid0-9)

### User API

#### GET `/user`

- RolesAllowed: USER, MODER, ADMIN
- JSON Response:

```java
class UserView {
    Long id;
    String email;
    String name;
    String avatar;
    String role;
    String annotation;
}
```

#### GET `/user/{id:[0-9]*}`

- RolesAllowed: ANONYMOUS, USER, MODER, ADMIN
- JSON Response:

```java
class UserView
```

#### POST `/user/login`

- RolesAllowed: ANONYMOUS
- JSON Payload:

```java
class LoginRequest {
    String email;
    String password;
}
```

- JSON Response:

```java
class UserExtView extends UserView {
    String token;
}
```

#### POST `/user/register`

- RolesAllowed: ANONYMOUS
- JSON Payload:

```java
class RegistrationRequest extends LoginRequest {
    String name  =  "";
}
```

- JSON Response

```java
class UserView
```

#### POST `/user/save`

- RolesAllowed: USER, MODER, ADMIN
- JSON Payload:

```java
class UserView
```

- JSON Response:

```java
class UserView
```

#### POST `/user/password`

- RolesAllowed: USER, MODER, ADMIN
- JSON Payload:

```java
class ChangePasswordRequest {
    String password;
    String password2;
}
```

- _No response_

#### POST `/user/avatar/`

- RolesAllowed: USER, MODER, ADMIN
- Multipart/Form-data Payload:

```java
class AvatarChangeRequest {
    MultipartFile file;
    Integer x1;
    Integer y1;
    Integer x2;
    Integer y2;
    Integer h;
    Integer w;
}
```

- _No response_

#### POST `/user/avatar/drop/`

- RolesAllowed: USER, MODER, ADMIN
- _No payload_
- _No response_

### Post API

#### GET `/`

- RolesAllowed: ANONYMOUS, USER, MODER, ADMIN
- JSON Response:

```java
class PostListView {
    Long totalCount;
    Long totalPages;
    Long currentPage;
    List<PostView> posts;

    class PostView {
        Long id;
        String title;
        String text;
        String announce;
        String source;
        Date createdAt;
        List<CommentView> comments;
        List<String> tags;
        RatingView rating;
        Long totalComments;
        UserView proposedBy;

        class CommentView {
            Long id;
            String text;
            Date createdAt;
            Date updatedAt;
            UserView user;
            ReplyTo replyTo;
            RatingView rating;

            class ReplyTo {
                Long id;
                String text;
                UserView user;
                Date createdAt;
            }
        }

        class RatingView {
            Long count;
            Boolean like;
            Boolean dislike;
        }
    }
}
```
#### GET `/by-tag/{tag:.*}/`

- RolesAllowed: ANONYMOUS, USER, MODER, ADMIN
- JSON Response:

```java
class PostListView
```

#### GET `/{id:[0-9]*}`

- RolesAllowed: ANONYMOUS, USER, MODER, ADMIN
- JSON Response:

```java
class PostExtView {
    PostView post;
    List<PostView> prev;
    List<PostView> next;
}
```

#### POST `/{id:[0-9]*}`

- RolesAllowed: MODER, ADMIN
- JSON Payload:

```java
class PostView
```

- JSON Response:

```java
class AdminPostView extends PostView {
    Date updatedAt;
    Date moderatedAt;
    UserView moderator;
    Boolean isAllowed;
    Boolean isModerated;
}
```

#### POST `/add`

- RolesAllowed: MODER, ADMIN
- JSON Payload:

```java
class PostView
```

- JSON Response:

```java
class AdminPostView
```

#### POST `/{id:[0-9]*}/delete`

- RolesAllowed: MODER, ADMIN
- _No payload_
- _No response_

#### POST `/{id:[0-9]*}/approve`

- RolesAllowed: MODER, ADMIN
- _No payload_
- JSON Response:

```java
class AdminPostView
```

#### POST `/{id:[0-9]*}/dismiss`

- RolesAllowed: MODER, ADMIN
- _No payload_
- JSON Response:

```java
class AdminPostView
```

#### POST `/{id:[0-9]*}/like`

- RolesAllowed: USER, MODER, ADMIN
- _No payload_
- JSON Response:

```java
class PostView
```

#### POST `/{id:[0-9]*}/dislike`

- RolesAllowed: USER, MODER, ADMIN
- _No payload_
- JSON Response:

```java
class PostView
```

#### GET `/published/`

- RolesAllowed: MODER, ADMIN
- JSON Response:

```java
class PostListView
```

#### GET `/published-waiting/`

- RolesAllowed: MODER, ADMIN
- JSON Response:

```java
class PostListView
```

#### GET `/published-waiting/{date:.*}/`

- RolesAllowed: MODER, ADMIN
- JSON Response:

```java
class PostListView
```

#### GET `/moderation-waiting/`

- RolesAllowed: MODER, ADMIN
- JSON Response:

```java
class PostListView
```

### Source API

#### GET `/source/`

- RolesAllowed: ADMIN
- JSON Response:

```java
List<SourceView>
class SourceView {
    Long id;
    String name; // уникальное название
    String url; // ссылка на корень сайта
    SourceSettings settings;
    Date createdAt;
    Date lastPostDate;
    Boolean isLocked; // флаг, говорящий о том что данный источник в данный момент парсится
    List<PostView> posts;

    class SourceSettings {
        String url; // ссылка на страницу статей/новостей/rss/канал
        String linksSelector; // css-селектор для страницы новостей, который выбирает все ссылки на новости
        String descriptionSelector; // css-селектор для страницы новостей который выбирает анонсы если они там есть
        String titlesSelector; // css-селектор для страницы конкретной новости, который выбирает заголовок
        String titleSelector; // css-селектор для страницы конкретной новости, который выбирает заголовок новости
        String datesSelector; // css-селектор для страницы конкретной новости, который выбирает дату новости, если она там есть
        String dateFormat; // поле, куда можно ввести формат даты, если вдруг дата указана в каком-нибудь ебанутом формате
        String nextSelector; // css-селектор для страницы новостей на кнопку "далее" или ее аналог
        String contentSelector; // css-селектор для страницы конкретной новости, который выбирает текст новости
        String nextContentSelector; // css-селектор для страницы конкретной новости, который выбирает следующую страницу, если новость разбита на страницы
        String previewSelector; // css-селектор для страницы конкретной новости, который выбирает картинку-превью
        Boolean isApproved; // флаг, показывающий что источник можно лить в posts
        Integer type; // 1 - обычный сайт, 2 - rss-сайт, 3 - ютуб
    }
}

```
#### GET `/source/{id:[0-9]*}/`

- RolesAllowed: ADMIN
- JSON Response:

```java
class SourceView
```

#### GET `/source/run/{id:[0-9]*}/`

- RolesAllowed: ADMIN
- _No response_

#### POST `/source/add/`

- RolesAllowed: ADMIN
- JSON Payload:

```java
class SourceView
```

- JSON Response:

```java
class SourceView
```

#### POST `/source/edit/`

- RolesAllowed: ADMIN
- JSON Payload:

```java
class SourceView
```

- JSON Response:

```java
class SourceView
```

#### POST `/source/delete/{id:[0-9]*}/`

- RolesAllowed: ADMIN
- _No payload_
- _No response_

### Post preview API

#### GET `/post-preview/`

- RolesAllowed: MODER, ADMIN
- JSON Response:

```java
List<PostPreviewView>
class PostPreviewView {
    Long id;
    String title;
    String text;
    String announce;
    Date createdAt;
    SourceView source;
}
```

#### GET `/post-preview/{id:[0-9]+}`

- RolesAllowed: MODER, ADMIN
- JSON Response:

```java
class PostPreviewView
```

#### GET `/post-preview/by-source/{id:[0-9]+}`

- RolesAllowed: MODER, ADMIN
- JSON Response:

```java
List<PostPreviewView>
```

### Image API

#### GET `/image/{id:.*}`

- RolesAllowed: ANONYMOUS, USER, MODER, ADMIN
- Response: `byte[]`

### Comment API

#### GET `/{id:[0-9]*}/comments`

- RolesAllowed: ANONYMOUS, USER, MODER, ADMIN
- JSON Response:

```java
class CommentView
```

#### POST `/{id:[0-9]*}/comments`

- RolesAllowed: USER, MODER, ADMIN
- JSON Payload:

```java
class CommentView
```

- JSON Response:

```java
class CommentView
```

#### POST `/{id:[0-9]*}/comments/edit/`

- RolesAllowed: USER, MODER, ADMIN
- JSON Payload:

```java
class CommentView
```

- JSON Response:

```java
class CommentView
```

#### POST `/{postId:[0-9]*}/comments/delete/{id:[0-9]*}`

- RolesAllowed: USER, MODER, ADMIN
- _No payload_
- _No response_

#### POST `/{postId:[0-9]*}/comments/like/{id:[0-9]*}`

- RolesAllowed: USER, MODER, ADMIN
- _No payload_
- JSON Response:

```java
class CommentView
```

#### POST `/{postId:[0-9]*}/comments/dislike/{id:[0-9]*}`

- RolesAllowed: USER, MODER, ADMIN
- _No payload_
- JSON Response:

```java
class CommentView
```
