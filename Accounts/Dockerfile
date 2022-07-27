#Image containing the Java Runtime
FROM  openjdk:11-slim as build

#information around who maintains the image
MAINTAINER mufasa.com

#Accounts Jar copying from target to the container
COPY target/Accounts-0.0.1-SNAPSHOT.jar Accounts-0.0.1-SNAPSHOT.jar

#Accounts Jar to be started in start the container
ENTRYPOINT ["java","-jar","/Accounts-0.0.1-SNAPSHOT.jar"]