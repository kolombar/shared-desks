FROM openjdk:17

MAINTAINER Barbora Kolomazníková <kolombar@fit.cvut.cz>

RUN mkdir -p /www/kolombar/shared-desks/

COPY ./build/libs /www/kolombar/shared-desks/
WORKDIR /www/kolombar/shared-desks/

CMD ["java", "-jar", "shared-desks-0.1-SNAPSHOT.jar"]