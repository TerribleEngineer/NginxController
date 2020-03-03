FROM openjdk:15-slim

RUN apt-get -y update && apt-get install -y nginx --no-install-recommends && rm -rf /var/lib/apt/lists/*

ENV container=docker
EXPOSE 80

COPY web /ui
COPY scripts/startup.sh /startup.sh
COPY nginx-api.jar /nginx-api.jar

RUN chmod +x /startup.sh && mkdir /www

CMD /bin/bash -c "/startup.sh"