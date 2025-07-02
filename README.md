Proceso para levantar el proyecto:

Requisitos Previos Tener JDK 24 instalado.

1. Clonar el repo (IntelliJ tiene un boton que nos permite clonar el repo con UI)
   URL: https://github.com/saracco-juan/buenas-lecturas-saracco
2. Instalar H2 DataBase
  URL: https://www.h2database.com/html/main.html
3. Volver al IDE, ir a File > Project Structure > Modules
4. Seleccionar el proyecto y apretar Dependencies (Al lado de Sources y Paths en InteliJ)
5. Apretar el '+' > JARs or Directories > Navegamos a la carpeta donde tenemos instalado H2 > bin > Seleccionamos el .jar
6. Abrir la H2 console y llenamos los siguientes campos:

  URL JDBC: jdbc:h2:~/test;DB_CLOSE_DELAY=-1
  Username: 'sa'
  Password: ''

7. Apretar en connect
