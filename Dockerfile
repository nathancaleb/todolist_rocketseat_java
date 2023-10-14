# Arquivo vai construi no site Render uma imagem baseado em UBUNTU onde nossa aplicação vai rodar-
FROM ubuntu:latest AS build

#  Pegar todas atualizações do UBUNTU
RUN apt-get update
# Instalar o Java 17 (-y para dar yes em todas as perguntas)
RUN apt-get install openjdk-17-jdk -y

# Vai copiar todo conteudo do meu APP e colar dentro do diretorio do Render
COPY . .

# Instalar o Maven (-y para dar yes em todas as perguntas)
RUN apt-get install maven -y
# mvn clean install vai construir o arquivo JAR
RUN mvn clean install

FROM openjdk:17-jdk-slim
# Expor a porta 8080 da maquina virtual UBUNTU
EXPOSE 8080

# Vai pega todo conteudo de build copiar pra dentro de app.jar
COPY --from=build /target/todolist-1.0.0.jar app.jar

# Comando para executar a aplicação
ENTRYPOINT [ "java", "-jar", "app.jar"]