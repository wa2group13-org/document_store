# Document store

- Repository: [GitHub](https://github.com/polito-WAII-2024/lab1-g13)

## Environmental variable

- `POSTGRES_URL`: url of the Postgres server
- `POSTGRES_PORT`: port of the Postgres server
- `POSTGRES_DB`: name of the Postgres database
- `POSTGRES_USERNAME`: username of the Postgers user
- `POSTGRES_PASSWORD`: password of the Postgres user
- `MULTIPART_MAX_FILE_SIZE`: how big a file can be in a multipart request (spring syntax)
- `MULTIPART_MAX_REQUEST_SIZE`: how big can a multipart request be (spring syntax)
- `SPRING_PROFILES_ACTIVE`: Spring comma-separated profiles. Default: `dev`, `no-security`, `api-docs`. Values:
    - `dev`: db logs and errors are returned on responses
    - `prod`: need to specify postgres connections
    - `no-security`: filers are disables
    - `api-docs`: enables springdoc openapi and swagger-ui
- `JWT_ISSUER_URI`: uri of the JWT issuer, e.g. `http://keycloak:9090/realms/app`
- `KAFKA_CONSUMER_BOOTSTRAP_SERVERS`: consumer bootstrap servers for Apache Kafka
- `ATTACHMENT_TOPIC`: name of the attachment topic. Default: `attachment.json`
- `OPENAPI_BASE_URL`: base url of this service that will appear in the OpenAPI documentation.
  Default `http://localhost:${PORT}`
- `PORT`: server port. Default: `8080`
