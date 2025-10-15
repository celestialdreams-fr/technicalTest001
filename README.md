
# Technical Test

This README describes the REST controller UserController, its endpoints, expected formats, HTTP behavior, how to test it and run it.\
***!!! i created a new branch (test002) to add CustomProperties in order to define a default allowed countries and to set the min age for adult status!!!***
## API Reference

#### Get all users

```http
  GET /users
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| - | - | Returns all users as an iterable collection|

Responses:\
    200 OK — returns array of User objects.

Response example:\
  {
    "id": 1,
    "name": "Arnaud",
    "birthDate": "1975-01-01",
    "countryCode": "FR",
    "gender": "M"
  }


#### Get a user by id

```http
  GET /user/${id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
|id | long | Id of the user to retrieve|

Response example:\
  {
    "id": 1,
    "name": "Arnaud",
    "birthDate": "1975-01-01",
    "countryCode": "FR",
    "gender": "M"
  }

Responses:\
    200 OK — returns User JSON when found.\
    404 Not Found — no user with given id.
    
#### Register a user

```http
  POST /user
  Content-Type: application/json
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
|  body     | object   | 	User payload to create; validated|

Request body example:\
  {
    "name": "Arnaud",
    "birthDate": "1975-01-01",
    "countryCode": "FR",
    "gender": "M"
  }

Responses:\
    201 Created — returns created user object.\
    422 Unprocessable Entity — validation errors returned as JSON object with field-level messages.
    
#### Update a user

```http
  PUT /user/${id}
  Content-Type: application/json
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `long` | **Required**. Id of user to update  |
|  body     | object   | 	Partial or full User payload; only non-null fields are applied|

Request body example (partial update):\
    {
        "name": "Stephanie",
        "gender": "F"
    }

Responses:\
    200 OK — returns updated user object.\
    404 Not Found — user with given id does not exist.\
    422 Unprocessable Entity — validation errors for provided fields.
    
#### Delete a user

```http
  DELETE /user/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `long` | **Required**. Id of user to delete |

Responses:\
    204 No Content — user successfully deleted.\
    404 Not Found — user with given id does not exist.

## Payload Format
Content-Type: application/json\
Name: 2–40 alphabetic characters\
Date: use ISO format yyyy-MM-dd for LocalDate (must be an adult birth date)\
CountryCode: 2 alphabetic characters (must be a french country code)\
Gender: F or M

## Example of payload to create / update:
    {
    "name": "Stephanie",
    "birthDate": "1976-01-01",
    "countryCode": "FR",
    "gender": "F",
    "phoneNumber": "+33"
    }

## Postman
import TechnicalTest.postman_collection.json into your postman collections

## Build & Run
  git clone git@github.com:celestialdreams-fr/technicalTest001.git\
  cd technicalTest001\
  ./mvnw clean package\
  ./mvnw spring-boot:run

Configured port : **8888**

## Authors
- [@celestialdreams-fr](https://github.com/celestialdreams-fr/technicalTest001)

## Specification 
 A Springboot API that exposes two services:\
• one that allows to register a user => user post/put endpoint\
• one that displays the details of a registered user => users + user/{id}\
A user is defined by: => data/sql + .model/User.class\
• a user name\
• a birthdate\
• a country of residence\
A user has optional attributes:\
• a phone number\
• a gender\
Only adult French residents are allowed to create an account => MinAge + CountryCode validator\
Inputs must be validated and return proper error messages/http statuses => @validate + ErrorHandler\
Deliverables:\
• Source code (Github) => https://github.com/celestialdreams-fr/technicalTest001
• Documentation (how to build from sources, how to use the API) => this readme.md\
• Request samples (I.e. Postman collection) => TechnicalTest.postman_collection.json
