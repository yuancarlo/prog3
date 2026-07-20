# Plan de desarrollo - Practico de Acceso a Datos

## Objetivo
Desarrollar una aplicacion con interfaz grafica(en swing), que se conecte a una base de datos sqlite mediante hibernate y 
permita hacer operaciones CRUD(ABM) simples.Las operaciones a implementar son las siguientes:
###  Operaciones a implementar

1. Crear un nuevo item de la tabla. Debe haber un formulario en el que se
   validen y llenen los datos.
2. Actualizar un ítem ya presente en la table. Debe haber un formulario
   que se carga, valida los datos y permite sus cambios.
3. Eliminar un ítem de la lista. Antes de eliminar un ítem el programa lanza
   una venta de confirmación.
4. Listar los ítems de la lista. En lo posible la lista debe estar paginada
   para que solamente salgan 20 elementos por ejemplo. La lista debe tener un
   campo al principio donde el usuario puede colocar un texto y con el botón "Buscar"
   la lista presenta solamente los ítems que cumplan con la búsqueda.
5. Todas las operaciones se deben loggear.

### El modelo de datos es el siguiente(lo actualizas a los datos de sqlite):

* Columna id de tipo entero Primary key
* Columna varchar(200)
* Columna boolean
* Columna tipo entero
* Columna tipo fecha

Solamente una tabla es necesario para el práctico. Hazlo con la tabla persona

### Interfaz
La interfaz se debe implementar con swing. Preparar el programa en capas.
Cada excepción que pueda aparecer está con throws en las capas que NO sean
de presentación y con try/catch solamente en la capa de presentación.


## Condiciones de trabajo
* la aplicacion debe realizarse con el lenguaje java y usar la libreria swing para la interfaz grafica
* las clases deben estar estructuradas con el patron MVC(la vista actuara como el controlador, no se necesita 
 usar el paquete controlador, basta con el modelo y vista)
* usar el patron dao yu singleton
* la interfza debe contar con todas las operaciones mencionades anterioremente
* usa los principios solid, clean code, no te repitas a ti mismo
* El titulo de la ventana debe decir registro
* Que el proyecto sea legible, sin codigo spaggueti, que las nombres de las variables y metodos se entiendan y esten en español
* trata de q las clases no sean tan largas

## Ubicacion
Este proyecto debe estar dentro del paquete AccesoADatos
```text
   package AccesoADatos
```

Estos son los archivos de configuracion de hibernate y pom.xml, para q lo tengas en cuenta:
``` text
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>Practicos-ProIII</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.24.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.24.3</version>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.11.0</version>
        </dependency>
        <dependency>
            <groupId>jakarta.persistence</groupId>
            <artifactId>jakarta.persistence-api</artifactId>
            <version>3.1.0</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>6.4.4.Final</version>
        </dependency>
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.45.1.0</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-community-dialects</artifactId>
            <version>6.4.4.Final</version>
        </dependency>
    </dependencies>
```
```text
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">org.sqlite.JDBC</property>
        <property name="hibernate.connection.url">jdbc:sqlite:base_datos.db</property>

        <property name="hibernate.dialect">org.hibernate.community.dialect.SQLiteDialect</property>

        <property name="hibernate.show_sql">true</property>
        <property name="hibernate.format_sql">true</property>

        <property name="hibernate.hbm2ddl.auto">update</property>

    </session-factory>
</hibernate-configuration>

```
