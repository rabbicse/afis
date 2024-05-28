FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

## Install Digital Persona UareU sdk
COPY sdk/ ./
RUN chmod +x ./install
RUN echo "Yes" | ./install

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY lib ./lib
COPY src ./src

RUN ./mvnw clean package

CMD ["./mvnw", "spring-boot:run"]