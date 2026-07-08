# Simple Spring based CRUD service for TODO list handling
This is the backend part for Spring Boot based TODO list handling application. App consists of three layers:
- Controller: responsible for web requests handling and routing. As all of the CRUD functionality has been implemented, controller handles GET, POST, PUT, PATCH and DELETE requests to different REST API endpoints.
- Service: implements business logic, which is separated from web requests processing and database operations. Sometimes serves as a proxy for repository, but most often converts DTO's, handles or throws exceptions etc.
- Repository: interacts with database using Spring JDBC Template. `RowMapper` and SQL queries have been implemented manually. `update` operations are wrapped with `@Transactional` annotation.

Also, data model has been implemented as `Task` class, which consists of five members: `Long id`, `String title`, `String description`, `String status` (possible values: `TODO`, `IN_PROGRESS` and `DONE`) and `Instant createdAt`. For POST, PUT and PATCH requests different DTO's (data transfer objects) are used.

Database is deployed in Docker-container. PostgreSQL 16 is used; in order to deploy database, just execute `docker compose up -d` in the root of repository. Container and database listen 5432 port.

Application listens HTTP-requests on 8080 port. Configuration is made with `application.yml` file. Database in the container is created with 5 rows on startup thanks to `schema.sql` script and `sql.init.mode=always` property in the `application.yml`.

Implemented REST API endpoints:
- `GET /tasks`: returns list of all tasks
- `GET /tasks/{id}`: returns `Task` object with specified id
- `POST /tasks`: creates new `Task`, requires `String title` and `String description` in the request body
- `PUT /tasks/{id}`: fully updates specified `Task`, requires `String title`, `String description` and `String status` in the request body; **IMPORTANT: NOT ABLE TO CREATE NEW TASKS FOR INEXISTENT IDS, RETURNS 404 INSTEAD!!!**
- `PATCH /tasks/{id}`: updates only one of specified `Task` fields: `String title`, `String description` or `String status`; requires corresponding field in the request body
- `DELETE /tasks/{id}`: deletes specified `Task`
