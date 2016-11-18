FROM centos:7

RUN yum -y update && yum -y install epel-release
RUN yum -y update && yum -y install java-1.7.0-openjdk nginx wget curl

ENV container=docker
EXPOSE 80

COPY scripts/bootstrap.sh /bootstrap.sh
COPY scripts/startup.sh /startup.sh
RUN chmod +x /bootstrap.sh && chmod +x /startup.sh

COPY nginx-controller-api-0.0.1-SNAPSHOT-fatjar.jar /proxy-api.jar

CMD /bin/bash -c "/startup.sh"
