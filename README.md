
# Technical Test

This README describes the REST controller UserController, its endpoints, expected formats, HTTP behavior, how to test it and run it.
## API Reference

#### Get all users

```http
  GET /users
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| |  | |

#### Get a user by id

```http
  GET /user/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| ||||

#### Register a user

```http
  POST /user
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
|       |  | |

#### Update a user

```http
  PUT /user/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `long` | **Required**. Id of user to update |

#### Delete a user

```http
  DELETE /user/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `long` | **Required**. Id of user to update |


POST /user — Create a user.\
GET /user/{id} — Retrieve a user by its id.\
GET /users — Retrieve all users.\
PUT /user/{id} — Update an existing user (partial update of non-null fields).\
DELETE /user/{id} — Delete a user by its id.\

## Format
Content-Type: application/json\
Name: 2–40 alphabetic characters\
Date: use ISO format yyyy-MM-dd for LocalDate (must be an adult birth date)\
CountryCode: 2 alphabetic characters (must be a french country code)\
Gender: F or M
## HTTP Status codes behaviors :
200 OK: successful POST creation or read.\
200 OK: GET /user/{id} found; PUT returns the updated object.\
204 No Content: successful DELETE (no body returned).\
404 Not Found: GET/PUT/DELETE for a non-existent id.\
400 Bad Request: invalid payload (JSON parsing error).\
422 Unprocessable Entity: invalid payload (validation failure from @Valid).
## Example of payload to create / update:\
    {
    "name": "Stephanie",
    "birthDate": "1976-01-01",
    "countryCode": "FR",
    "gender": "F"
    }
## Postman
import TechnicalTest.postman_collection.json into your postman collections
## Build & Run
./mvnw clean package\
./mvnw spring-boot:run

Configured port : **8888**
## Authors

- [@celestialdreams-fr](https://github.com/celestialdreams-fr/technicalTest001)


