## Prerequisites
- java11 https://www.oracle.com/java/technologies/downloads/
- docker https://www.docker.com/get-started

### Technology
- Java11
- Spring boot WebFlux
- R2DBC MySQL DB library
- Maven
- R2DBC
- Flyway

## APIs

- Add score

Description:
Add score for a movie

Endpoint:

```
POST /myservice/v1/scores
```

Query parameters:

```
apiKey - key for IMDB API
title  - title of the movie
score  - score number from 1 to 100
```

- Get top scores

Description:
Get a list of top scored movies sorted by office box value

Endpoint:

```
GET /myservice/v1/scores
```

Query parameters:

```
quantity - number of top movies by score
```

## Run unit tests

```
./mvnw test
```

## To test manually

### Start in Docker

```
./scripts/start.sh
``` 

### Run CURL commands

Add score for a movie by title

```
curl --request POST 'http://localhost:8088/myservice/v1/scores?apiKey=<apiKy>&title=<title>&score=<score>'
e.g.
curl --request POST 'http://localhost:8088/myservice/v1/scores?apiKey=e0c252f&title=True%20Grit&score=1'
```

Get top scored movies

```
curl --request GET 'http://localhost:8088/myservice/v1/scores?quantity=<quantity>'
e.g.
curl --request GET 'http://localhost:8088/myservice/v1/scores?quantity=10'
```

### Stop Docker

```
./scripts/stop.sh
``` 
