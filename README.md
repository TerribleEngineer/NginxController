# NginxController

## Introduction
This project aims to have a dockerized nginx reverse proxy which can have its routes updated via a REST API.

## Prerequisites
- Docker Engine 
- Maven 3.x


## Getting Started

1. Clone Repository

```bash
  $ git clone [repository URL]
```
2. Enter Project Directory

```bash
  $ cd NginxController/
```
3. Have Maven obtain dependencies, build the project, and create a docker image

```bash
  mvn install
```
4. Run the Docker Image within a Container

```bash
  $ docker run -d --name tenginx -e "HOSTNAME=tenginx" -e "CONTROL_PORT=9797" -e "PROXY_PORT=80" -p 80:80 -v /{Host Directory Containing Static Content}:/www tenginx:0.2
```

On startup:

* The Service's UI can be accessed from http://localhost/ui
* The Service's API can be accessed from http://localhost/_ng
* The Service's registered APIs will be listed under http://localhost/api/{API NAME}
    * These can be found under the nginx config listing (http://localhost/_ng/config) or
    * The locations listing (http://localhost/_ng/locations)


## Recent Additions

- Reworked the REST API using the Spark Java framework
- Allows the setting of headers


## Future Developments

- Include additional configuration elements for each proxy route's location directive.
- Include support for distributed logging mechanisms (e.g., fluentd)
- Improved high availability mode
