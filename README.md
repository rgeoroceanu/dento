# Dento README
# Stack
- Java
- Spring Boot
- Vaadin 8
- MariaDB
## Database
docker run -d --name mariadb-dento --env MARIADB_USER=dento --env MARIADB_PASSWORD=dento --env MARIADB_ROOT_PASSWORD=admin --env MARIADB_DATABASE=dento -p 3306:3306 mariadb:latest
