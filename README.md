Proceso para levantar el proyecto:

Requisitos Previos Tener JDK 24 instalado.

1. Clonar el repo (IntelliJ tiene un boton que nos permite clonar el repo con UI)
   URL: https://github.com/saracco-juan/buenas-lecturas-saracco
2. Instalar H2 DataBase (En caso de tenerlo instalado, omitir este paso)
  URL: https://www.h2database.com/html/main.html
3. Volver al IDE, ir a File > Project Structure > Modules
4. Seleccionar el proyecto y apretar Dependencies (Al lado de Sources y Paths en InteliJ)
5. Apretar el '+' > JARs or Directories > Navegamos a la carpeta donde tenemos instalado H2 > bin > Seleccionamos el .jar
6. Abrir la H2 console y llenamos los siguientes campos:

  URL JDBC: jdbc:h2:~/test;DB_CLOSE_DELAY=-1
  Username: 'sa'
  Password: ''

7. Apretar en connect
8. Ejecutar el siguiente codigo SQL en la BBDD para la creacion de las tablas:

CREATE TABLE APP_USER (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(100) NOT NULL,
    EMAIL VARCHAR(100) NOT NULL,
    PASSWORD VARCHAR(100) NOT NULL
);

CREATE TABLE USER_BOOKS (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    USER_ID INT NOT NULL,
    BOOK_ISBN VARCHAR(100) NOT NULL,
    LIST_TYPE VARCHAR(50) NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES APP_USER(ID)
);

CREATE TABLE REVIEWS (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    USER_ID INT NOT NULL,
    BOOK_ISBN VARCHAR(100) NOT NULL,
    RATING INT NOT NULL,
    COMMENT TEXT,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (USER_ID) REFERENCES APP_USER(ID)
);

8. Una vez terminado esto correr el main.

9. Comentario adicional:

    El proyecto tiene agregadas 3 librerias adicionales (revisar carpeta lib). Al clonarlo deberia cargarlas correctamente, en caso de algun tipo    de error dejo adjunto los links de descarga.

   https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.17.0/
   https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.17.0/
   https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.17.0/

   Descargar la version 2.17.0.jar de los tres y repetir el paso 5 para añadirlas.

10. En caso de que no haya funcionado, agregue el ejecutable .jar. Navegar hasta la carpeta del proyecto out > artifacts y ejecutar el comando java -jar BuenasLecturas-Saracco.jar
    El proyecto se deberia ejecutar correctamente.

    Saludos!
