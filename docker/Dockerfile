# Build WAR using Ant
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Install Ant
RUN apt-get update && apt-get install -y ant && rm -rf /var/lib/apt/lists/*

COPY . .
RUN ant -buildfile build.xml

# Deploy to Tomcat
FROM tomcat:9.0-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/dist/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
