# quotesapp

![Build status](https://github.com/threehundredbytes/quotesapp/actions/workflows/default.yml/badge.svg?branch=master)
[![codecov](https://codecov.io/gh/threehundredbytes/quotesapp/branch/master/graph/badge.svg)](https://codecov.io/gh/threehundredbytes/quotesapp)

A Spring Boot REST API

# How to run

You can run application using Java:

    gradlew clean build
    java -jar /build/libs/quotesapp-1.0.0.jar

Or build Docker image and run it:

    gradlew clean build
    docker build --tag=quotesapp:1.0.0 .
    docker run -p 8080:8080 quotesapp:1.0.0

Or pull Docker image from DockerHub and run it:

    docker image pull threehundredbytes/quotesapp:latest
    docker run -p 8080:8080 threehundredbytes/quotesapp:latest

# Endpoints

## User endpoints (/api/v1/users)

### Create user endpoint

409 Conflict if a user with the same username/email already exists.

```
curl --location --request POST 'http://localhost:8080/api/v1/users' \
    --header 'Content-Type: application/json' \
    --data-raw '{ "username": "user" }'
```

## Quote endpoints (/api/v1/quotes)

### Find all quotes endpoint

```
curl --location --request GET 'http://localhost:8080/api/v1/quotes'
```

This endpoint is paginated so you can use request params such as 
`limit` (aka page size, default = `10`),
`offset` (aka page number, default = `0`),
`sort` (values: `VOTE_ASC`, `VOTE_DESC`, default = `VOTE_ASC`)

- Find top 10 quotes by votes:
  ```
  curl --location --request GET 'http://localhost:8080/api/v1/quotes?sort=VOTE_DESC'
  ```

- Find flop 10 quotes by votes:
  ```
  curl --location --request GET 'http://localhost:8080/api/v1/quotes?sort=VOTE_ASC'
  ```

### Random quote endpoint

404 Not found if the database contains 0 quotes.

```
curl --location --request GET 'http://localhost:8080/api/v1/quotes/random'
```

### Create quote endpoint

- Because there are no authentication and authorization mechanisms, you must explicitly pass
`userId` as a query parameter just to identify that this user posted this quote.
- 404 Not Found if the user does not exist.

```
curl --location --request POST 'http://localhost:8080/api/v1/quotes?userId=1' \
--header 'Content-Type: application/json' \
--data-raw '{ "text": "Now or never!" }'
```

### Update quote endpoint

- Any user can update any quote. Not just the user who posted it.
- 404 Not Found if the quote does not exist.

```
curl --location --request PUT 'http://localhost:8080/api/v1/quotes/${quoteId}' \
--header 'Content-Type: application/json' \
--data-raw '{ "text": "I never dreamed about success, I worked for it." }'
```

### Delete quote endpoint

- Any user can delete any quote. Not just the user who posted it.
- 404 Not Found if quote does not exist.

```
curl --location --request DELETE 'http://localhost:8080/api/v1/quotes/${quoteId}'
```

## Vote endpoints (/api/v1/quotes/${quoteId}/votes)

### Get all votes on the quote

404 Not Found if the quote does not exist.

```
curl --location --request GET 'http://localhost:8080/api/v1/quotes/${quoteId}/votes/'
```

### Get user vote on the quote

404 Not Found if the user/quote does not exist.

```
curl --location --request GET 'http://localhost:8080/api/v1/quotes/${quoteId}/votes/users/${userId}'
```

### Create vote

- Because there are no authentication and authorization mechanisms, you must 
  explicitly pass `userId` as a query parameter just to identify that this
  user has voted.
- Vote state is an enum value with values `UPVOTE` and `DOWNVOTE` 
  (also `NOT_VOTED`, but not used here)
- 404 Not Found if the user/quote does not exist.
- 409 Conflict if this user upvotes and has already upvoted this quote.
- 409 Conflict if this user downvotes and has already downvoted this quote.
- 422 Unprocessable Entity if the voteState in request = `NOT_VOTED`.

```
curl --location --request POST 'http://localhost:8080/api/v1/quotes/2/votes?userId=1' \
--header 'Content-Type: application/json' \
--data-raw '{ "voteState": "UPVOTE" }'
```

### Delete vote

404 Not Found if the user/quote/vote does not exist.

```
curl --location --request DELETE 'http://localhost:8080/api/v1/quotes/${quoteId}/votes?userId=${userId}'
```

### Vote history

- Updates every midnight. May be empty if the application 
has not passed midnight after the quote was created.
- 404 Not Found if the quote does not exist.

```
curl --location --request GET 'http://localhost:8080/api/v1/quotes/${quoteId}/votes/history'
```