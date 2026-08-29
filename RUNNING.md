# Running the ERP backend locally

Spring Boot 4.1 / Java 21 modular monolith. Two modules so far: `identity` and `sales`.

## Option A - zero setup (in-memory H2)

This is the **default** profile. No database to install; data is wiped on restart.

```bash
./mvnw spring-boot:run
```

App starts on <http://localhost:8080>. Hibernate creates the `identity` and `sales`
schemas and tables in an in-memory H2 database (PostgreSQL-compatibility mode).

## Option B - real PostgreSQL (`local` profile)

Closest to production - one database, one schema per bounded context.

```bash
brew install postgresql@17
brew services start postgresql@17

# create the role + database the app expects (defaults: erp / erp / erp)
psql postgres -c "CREATE ROLE erp LOGIN PASSWORD 'erp' SUPERUSER;"
createdb -O erp erp

./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

`SUPERUSER` (or at least `CREATE` on the database) is needed so Hibernate can create
the per-context schemas. Override with env vars: `DB_NAME`, `DB_USER`, `DB_PASSWORD`.

> `ddl-auto` is `update` here for convenience. Replace with Flyway migrations per
> schema before this goes anywhere real (ARCHITECTURE.md section 5).

## Smoke test

```bash
# health + module structure
curl -s localhost:8080/actuator/health
curl -s localhost:8080/actuator/modulith | jq

# register a user
curl -s -XPOST localhost:8080/api/users -H 'Content-Type: application/json' -d '{
  "email": "ops@acme.test", "password": "s3cret-pw",
  "companyId": "11111111-1111-1111-1111-111111111111",
  "branchId":  "22222222-2222-2222-2222-222222222222"
}'
# -> 201 { "id": "<userId>", ... }

# create an order, add a line, then try to place it
OID=$(curl -s -XPOST localhost:8080/api/orders -H 'Content-Type: application/json' -d '{
  "customerId":"33333333-3333-3333-3333-333333333333",
  "companyId":"11111111-1111-1111-1111-111111111111",
  "branchId":"22222222-2222-2222-2222-222222222222"}' | jq -r .id)

curl -s -XPOST "localhost:8080/api/orders/$OID/lines" -H 'Content-Type: application/json' -d '{
  "productId":"44444444-4444-4444-4444-444444444444",
  "quantity":2, "unitPrice":19.99, "currency":"USD"}'

# fails 403 until the user is granted "sales:order:create"
curl -s -XPOST "localhost:8080/api/orders/$OID/place?actingUserId=<userId>"
```

## Tests

```bash
./mvnw test          # unit + Modulith boundary verification (uses H2)
```

`ModularityTests` fails the build if a module reaches past another module's `api` package.
