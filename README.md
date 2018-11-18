# WoH.ru backend

## API

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

#### GET `/{id:[0-9]*}`

- RolesAllowed: ANONYMOUS, USER, MODER, ADMIN
- JSON Response:

```java
class PostView
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
