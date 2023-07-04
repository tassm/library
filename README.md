# library-application

This is a book management application built with Java and Spring Boot. It provides functionality to manage books and authors.

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven
- Docker, Docker-Compose (optional)

## Getting Started

Follow the steps below to run the application using the Maven wrapper and build a Docker image.

### 1. Clone the Repository

```shell
git clone <repository-url>
cd library
```

### 2. Run Complete Build with Tests

From the cloned directory run 'clean install' with the maven wrapper.

```shell
# Run the whole build and package flow
./mvnw clean install

# Test only
./mvnw clean test
```

### 3. Application Spring Profiles

Two different spring profiles can be used to select which database, either postgres or the in memory H2 database.

On a unix system you can use these from your shell session. Alternatively you may change the profile in `/src/main/resources/application.yaml`

```shell
EXPORT SPRING_PROFILES_ACTIVE=h2
EXPORT SPRING_PROFILES_ACTIVE=postgres
```

### 4. Running Locally (h2 database)

You can run the spring boot application like so. By default it will run with the in memory H2 database.

```shell
./mvnw spring-boot:run
```

### 5. Running Locally (postgres)

When using the `postgres` profile, the connection can be configured using the following environment variables:
- `POSTGRES_URL` The connection string for the postgres database specifying the host, port and database, default: `jdbc:postgresql://localhost:5432/postgres`
- `POSTGRES_USERNAME` The DB account username, default: `postgres`
- `POSTGRES_PASSWORD` The DB account password default: `postgres`

If using the included docker-compose container for postgres then the default values can be used.

Using docker-compose to run postgres and the application locally:
```shell
# set your database connection variables
export POSTGRES_URL=jdbc:postgresql://{your-db-host}:{your-db-port}/{your-database}
export POSTGRES_USERNAME={your-username}
export POSTGRES_PASSWORD={your-password}
# set the application to run with postgres
export SPRING_PROFILES_ACTIVE=postgres
# start the application
./mvnw spring-boot:run
```

### 6. Running it all with docker-compose! (optional)

If you have docker and docker-compose installed on your system and you want to run the application with docker-compose then that will be the easiest way!

```shell
# Build the application
./mvnw clean install
# Build the library app docker image
docker build -t library:latest .
# Run the postgres database and the app with docker-compose!
docker-compose up
```

<b>You can now access the service on `http://localhost:8080/book`!

TODO:
- Logging integration, SLF4j + Logback?
- method names alignment
- additional validation
- Separate Service and respository tests, mock repository layer in service tests