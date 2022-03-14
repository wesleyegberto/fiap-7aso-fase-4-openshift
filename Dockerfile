FROM maven:3.6-jdk-11 as builder
WORKDIR /app

COPY pom.xml .
RUN mvn verify --fail-never

COPY src ./src
RUN mvn package -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM adoptopenjdk/openjdk11-openj9:alpine-slim
WORKDIR /app
EXPOSE 8080

ENV JAVA_OPTIONS="-Xmx512m -Xms50m -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Xshareclasses -Xquickstart -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_OPTIONS="--spring.config.location=classpath:/ --spring.jmx.enabled=false -Dspring.backgroundpreinitializer.ignore=true"

ARG DEPENDENCY=/app/target/dependency

COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib lib/
COPY --from=builder ${DEPENDENCY}/META-INF META-INF/
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes .

ENTRYPOINT ["java", "-cp", ".:./lib/*", "com.fiap.produto.ProdutoApplication"]
