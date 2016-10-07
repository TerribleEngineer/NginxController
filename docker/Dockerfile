FROM centos:7

RUN yum -y update && yum -y install epel-release
RUN yum -y update && yum -y install java-1.7.0-openjdk nginx

ENV container=docker

EXPOSE 7777
EXPOSE 80

RUN mkdir proxy

COPY ./start.sh /proxy
COPY nginx-controller-api-0.0.1-SNAPSHOT-fatjar.jar /proxy

RUN mv /proxy/nginx-controller-api-0.0.1-SNAPSHOT-fatjar.jar /proxy-api.jar
RUN mv /proxy/start.sh /start.sh
RUN chmod +x /start.sh

CMD ["/start.sh"]
