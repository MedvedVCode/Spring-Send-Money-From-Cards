FROM openjdk:17-jdk-alpine

EXPOSE 5500

COPY target/SendMoney-0.0.1-SNAPSHOT.jar send_money_backend.jar

CMD ["java","-jar","send_money_backend.jar"]