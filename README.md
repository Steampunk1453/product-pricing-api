# Product Pricing API

A Spring Boot 4 REST API for retrieving the applicable price rate for a product and brand at a given date and time

## Requirements

- Java 17
- Maven 3.6.3 or newer

## Technology stack

- Spring Boot 4.1.0
- Spring Data JPA
- SpringDoc OpenAPI 3.1.0
- H2 Database 2.x

## Running the service

Start the application with Maven:

```bash
mvn spring-boot:run
```

The API is available at `http://localhost:8080`.

To package and run the application as a JAR:

```bash
mvn clean package
java -jar target/product-pricing-api-0.0.1-SNAPSHOT.jar
```

## API usage

The service exposes one endpoint:

```http
GET /api/prices
```

Required query parameters:

| Parameter | Description | Example |
|---|---|---|
| `effectiveDate` | Date and time in ISO `yyyy-MM-dd'T'HH:mm:ss` format | `2020-06-14T16:00:00` |
| `productId` | Product identifier | `35455` |
| `brandId` | Brand identifier | `1` |

Example with cURL:

```bash
curl "http://localhost:8080/api/prices?effectiveDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

Example response:

```json
{
  "productId": 35455,
  "brandId": 1,
  "applicableRate": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45
}
```

The endpoint returns `404 Not Found` when no applicable price exists and `400 Bad Request` when a required parameter is missing or invalid.

## Swagger / OpenAPI

With the service running, interactive API documentation is available at:

```text
http://localhost:8080/swagger-ui.html
```

The OpenAPI document is available at:

```text
http://localhost:8080/v3/api-docs
```

## Running the tests

Run the integration tests with:

```bash
mvn test
```

The tests cover the five reference pricing scenarios, missing and invalid parameters, requests without an applicable price, and unexpected service failures.

## H2 database

The application uses an in-memory H2 database. The schema and sample data are initialized automatically from:

- `src/main/resources/schema.sql`
- `src/main/resources/data.sql`

The database is available only while the application is running. To access it through the H2 web console:

1. Start the application.
2. Open `http://localhost:8080/h2-console/`.
3. Enter the following connection details:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:productpricingdb` |
| User Name | `sa` |
| Password | Leave blank |

The database name must match the JDBC URL configured in `application.yml`. The `PRICES` table contains the sample price rates:

```sql
SELECT * FROM PRICES;
```

## Project structure

The code is organized by responsibility following the Hexagonal Architecture:

- `domain`: core price model
- `application`: price selection service
- `infrastructure/adapter/in/web`: REST controller and response DTOs
- `infrastructure/repository`: JPA entity and database repository
- `infrastructure/config`: OpenAPI configuration
