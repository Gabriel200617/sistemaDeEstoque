# CRIAÇÃO DA IMAGEM DOCKER
FROM tomcat:11.0-jdk21-temurin

# REMOÇÃO DOS ARQUIVOS WEBAPPS DO TOMCAT
RUN rm -rf /usr/local/tomcat/webapps/*

#COPIA DOS ARQUIVOS DO LOCALHOST PARA A IMAGEM DOCKER.
COPY target/app.war /usr/local/tomcat/webapps/ROOT.war

# ABRIR A PORTA 8080
EXPOSE 8080

# EXECUÇÃO DO DOCKER
CMD ["catalina.sh", "run"]
