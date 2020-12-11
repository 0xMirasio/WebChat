FROM ubuntu:latest

RUN apt-get update
RUN apt-get install -y net-tools
RUN apt-get install -y maven
RUN apt-get install -y iputils-ping
RUN apt-get install -y nano
RUN cd /home ; mvn package

