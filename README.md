# Java Monorepo Project

This project demonstrates a Java monorepo structure with Maven, containing multiple related microservices and shared libraries.

## Project Structure

- `common-lib`: Shared code and utilities used across all services
- `service-api`: Common API definitions and DTOs
- `user-service`: Service for user management
- `product-service`: Service for product management
- `web-app`: Web application that integrates with the services

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Git

## Building the Project

To build all modules:

```bash
mvn clean install
```

To build a specific module:

```bash
mvn clean install -pl <module-name>
```

## Running the Services

### User Service

```bash
cd user-service
mvn spring-boot:run
```

### Product Service

```bash
cd product-service
mvn spring-boot:run
```

### Web Application

```bash
cd web-app
mvn spring-boot:run
```

## Development

Each module can be developed independently, but changes to shared modules like `common-lib` and `service-api` will affect dependent modules.

## Testing

To run tests for all modules:

```bash
mvn test
```

To run tests for a specific module:

```bash
mvn test -pl <module-name>
```

## CI/CD with GitHub Actions

This project uses GitHub Actions for continuous integration. See the workflow files in the `.github/workflows` directory.
