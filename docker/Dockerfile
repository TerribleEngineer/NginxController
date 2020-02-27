FROM openjdk:15-slim

RUN apt-get -y update && apt-get install -y wget curl nginx vim

ENV container=docker
EXPOSE 80

COPY web /ui
COPY scripts/startup.sh /startup.sh
RUN chmod +x /startup.sh

COPY nginx-api.jar /nginx-api.jar

CMD /bin/bash -c "/startup.sh"

RUN mkdir /www

