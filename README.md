# NginxController

## Introduction
This project aims to have a dockerized nginx reverse proxy which can have its routes updated via a REST API.

## Prerequisites
- Docker Engine installed (Works with native linux/windows variants; boot2docker and other VM based engines are currently unsupported)
- Two Available Ports

## Getting Started
Note: For steps 3 and 4, you may need to precede these with sudo depending upon how you've configured your accounts during Docker installation


1. Clone Repository
2. cd NginxController/
3. mvn install
4. docker run -d -p *host port for UI*:7777 -p *host port for reverse proxy*:80 goetz/nginx

The Service's UI can be accessed from http://localhost:*host port for UI*/ui/index.html


The REST endpoints can be found at http://localhost:*host port for UI*/nginx/application.wadl

## Future Developments
Include additional configuration elements for each proxy route's location directive:
- Allows setting of headers
- Allow proxy buffering / allocating buffers / setting buffer size(s)
- Allowing proxy binding for systems with multiple network interfaces
- Addition forms of reverse proxy request handling (fastcgi, uwsgi, scgi, and memcached are all currently unsupported)
