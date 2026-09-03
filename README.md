# Product Pricing API

A Spring Boot 4 REST API for retrieving the applicable price rate for a product and brand at a given date and time

## Requirements

- Java 17
- Maven Wrapper (recommended) or Maven 3.6.3 or newer

## Technology stack

- Spring Boot 4.1.0
- Spring Data JPA
- SpringDoc OpenAPI 3.1.0
- H2 Database 2.x

## Running the service

Start the application with the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd spring-boot:run`.

The API is available at `http://localhost:8080`.

To package and run the application as a JAR:

```bash
./mvnw clean package
java -jar target/product-pricing-api-0.0.1-SNAPSHOT.jar
```

To build and run the Docker image:

```bash
./mvnw clean package
docker build -t product-pricing-api .
docker run --rm -p 8080:8080 product-pricing-api
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
  "price": 25.45,
  "currency": "EUR"
}
```

The endpoint returns `404 Not Found` with the standard error structure when no applicable price exists, and `400 Bad Request` when a required parameter is missing or invalid.

Example `404` response:

```json
{
  "timestamp": "2024-03-01T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "No applicable price rate found for the given request."
}
```

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
./mvnw test
```

JaCoCo generates the coverage report at `target/site/jacoco/index.html` when running `./mvnw verify`.

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

The code follows Hexagonal Architecture:

- `domain`: core price model, independent of frameworks
- `domain/model`: price model and domain exceptions
- `domain/port`: domain ports implemented by adapters
- `application/usecase`: use cases represented as framework-independent records
- `adapter/web`: REST controllers, responses and exception handler
- `adapter/persistence`: persistence adapter, JPA entity and Spring Data repository
- `configuration`: Spring bean composition and OpenAPI configuration
