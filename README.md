This README describes the REST controller UserController, its endpoints, expected formats, HTTP behavior.

Endpoints :
    POST /user — Create a user.
    GET /user/{id} — Retrieve a user by its id.
    GET /users — Retrieve all users.
    PUT /user/{id} — Update an existing user (partial update of non-null fields).
    DELETE /user/{id} — Delete a user by its id.
    
Request formats :
    Content-Type: application/json
    Name: 2–40 alphabetic characters
    Date: use ISO format yyyy-MM-dd for LocalDate
    CountryCode: 2 alphabetic characters
    Gender: F or M
    
Example payload for create / update:
{
  "name": "Stephanie",
  "birthDate": "1976-01-01",
  "countryCode": "FR",
  "gender": "F"
}

HTTP status codes and behaviors :
    200 OK: successful POST creation or read.
    200 OK: GET /user/{id} found; PUT returns the updated object.
    204 No Content: successful DELETE (no body returned).
    404 Not Found: GET/PUT/DELETE for a non-existent id.
    400 Bad Request: invalid payload (validation failure from @Valid or JSON parsing error).