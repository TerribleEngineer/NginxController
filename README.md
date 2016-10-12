# NginxController

## Introduction
This project aims to have a dockerized nginx reverse proxy which can have its routes updated via a REST API.

## Prerequisites
- Docker Engine installed (Works with native linux/windows variants; boot2docker and other VM based engines are currently unsupported)
- Two Available Ports
- Maven 3.x


## Getting Started
Note: For steps 3 and 4, you may need to precede these with sudo depending upon how you've configured your accounts during Docker installation


Note: The Host system's ports are set in the following example commands to the same ports as they would be within the container, they can be altered to meet your particular needs.  The Controller REST API and UI reside on port 7777 and the reverse proxy traffic all flows through port 80.


1. Clone Repository
  git clone [repository URL]
2. Enter Project Directory
  cd NginxController/
3. Have Maven obtain dependencies, build the project, and create a docker image
  mvn install
4. Run the Docker Image within a Container
  docker run -d -p 7777:7777 -p 80:80 goetz/nginx


The Service's UI can be accessed from http://localhost:7777/ui/index.html


The REST endpoints can be found at http://localhost:7777/nginx/application.wadl


## Future Developments
Include additional configuration elements for each proxy route's location directive:
- Allows setting of headers
- Allow proxy buffering / allocating buffers / setting buffer size(s)
- Allowing proxy binding for systems with multiple network interfaces
- Addition forms of reverse proxy request handling (fastcgi, uwsgi, scgi, and memcached are all currently unsupported)

Include support for distributed logging mechanisms (fluentd or logstash)

Include better HA support (incorporate Zookeeper to synchronize multiple containers running this application)
