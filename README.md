# SpringBootApp

## Local PostgreSQL setup

Option A: Docker (recommended)

1) Start PostgreSQL:
```
docker run --name springbootapp-postgres -e POSTGRES_DB=mpf_labs -e POSTGRES_USER=mpf_user -e POSTGRES_PASSWORD=mpf_pass -p 5432:5432 -d postgres:16
```

Option B: SQL (run in psql)

```
CREATE DATABASE mpf_labs;
CREATE USER mpf_user WITH PASSWORD 'mpf_pass';
GRANT ALL PRIVILEGES ON DATABASE mpf_labs TO mpf_user;
```

## Required environment variables

Core runtime:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `PORT` (optional, defaults to 8080)

Mail (optional):
- `SPRING_MAIL_USER`
- `SPRING_MAIL_PASSWORD`
- `APP_MAIL_ADMIN`
- `APP_BASE_URL`

Admin bootstrap (optional):
- `ADMIN_EMAIL`
- `ADMIN_PASSWORD`

## Build and run

Build:
```
mvn -q -pl web -am clean package
```

Run:
```
mvn -q -pl web -am spring-boot:run
```

Run packaged jar:
```
java -jar web/target/web-*.jar
```

## Docker

Build image:
```
docker build -t springbootapp-web ./web
```

Run image:
```
docker run --rm -p 8080:8080 --env-file .env springbootapp-web
```

## Render deployment

1) Create a PostgreSQL database on Render.
2) Set env vars in Render service settings:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
   - `SPRING_MAIL_USER`
   - `SPRING_MAIL_PASSWORD`
   - `APP_MAIL_ADMIN`
   - `APP_BASE_URL`
   - `ADMIN_EMAIL`
   - `ADMIN_PASSWORD`
3) Deploy using Docker:
   - Build from `web/Dockerfile`.
   - Render will expose port `8080`.
