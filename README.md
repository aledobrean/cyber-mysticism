# Table of Contents

- [**Introduction**](#introduction) - A brief overview of the project.
- [**Getting Started**](#getting-started) - Instructions on how to set up and run the project.
    - [Docker](#Docker) - Instructions for Docker setup.
    - [OpenAPI commands](#openapi-commands) - Instructions for OpenAPI contracts.
- [Acknowledgements](#acknowledgements) - Recognizing those who have contributed to the project.
    - [Contributors](#contributors) - List of project contributors.

# **Introduction**

Welcome to Cyber Mysticism, a project dedicated to all things mystic. This application provides a service for performing
a three cards divination reading, returning the results in a JSON format. The readings are based on the Tarot, a tool of
divination that offers insights into the past, present, and future.

# **Getting Started**

## Docker

The app is configured to start on port 5050.

A pg-admin container also exists for development, but it needs to be uncommented from docker-compose.yml

Replace <user> and <password> with proper values.

For linux/arm64/v8:

```text
POSTGRES_USER=<user> \
POSTGRES_PASSWORD=<password> \
POSTGRES_DB=cyber_mysticism \
docker-compose up
```

For linux/amd64:

```text
SERVICE_IMAGE=aledobrean/three-cards-divination:amd64 \
POSTGRES_USER=<user> \
POSTGRES_PASSWORD=<password> \
POSTGRES_DB=cyber_mysticism \
docker-compose up
```

## OpenAPI commands

Prerequisite: change _springdoc.api-docs.enabled=true_ in application.properties

- Access this URL for Swagger UI: http://localhost:8080/swagger-ui.html (change the host and the port if needed)
- Access this URL for Swagger Docs API (json): http://localhost:8080/api-docs (change the host and the port if needed)
- Access this URL for Swagger Docs API (yaml): http://localhost:8080/api-docs.yaml (change the host and the port if
  needed)

# Acknowledgements

## Contributors

cyber-mysticism is relevant and updated regularly due to the valuable contributions from
its [contributors](https://github.com/aledobrean/cyber-mysticism/graphs/contributors).
