# Plan de desarrollo - Practico de imagenes

## Objetivo
Desarrollar una aplicacion con interfaz grafica, en la cual se pueda cargar una imagen mediante url u archivo local, y
esa aplicacion tenga las opciones de redimensionar(tambien debe tener una opcion de mantener proporcion en la cuales 
las proporciones sean iguales a las de la imagen original, ya sea modificando el alto o el ancho) tanto el ancho como el alto,
y tambien tenga las opciones de cifrar(desordenar todos los pixeles de la imagen) y descifrar(que todos los pixeles desordenados vuelvan a su lugar ).

## Condiciones de trabajo
* la aplicacion debe realizarse con el lenguaje java y usar la libreria swing para la interfaz grafica
* las clases deben estar estructuradas con el patron MVC(la vista actuara como el controlador, no se necesita 
* usar el paquete controlador, basta con el modelo y vista)
* usar el patron observer cuando sea necesario y si es necesario en el practico
* usar el patron strategy 
* la imagen al redimensionarse no tiene que perder datos(que todas las imagenes redimensionadas partan de 
  la imagen original)
* usa un menu despleglabe en la esquina superior izquierda para cargar las imagenes seun corresponda
* a lado del menu desplegable deberia estar la opcion de redimensionar
* el boton redimensionar , tendria que tener los campos ancho t alto y un ckecbox para la opcion de mantener proporcion
* la clase del modelo de la imgen debe tener los algoritmos de agrandaar, achicar, cifrar y descifrar
* usa los principios solid, clean code, no te repitas a ti mismo
* los nombres, tanto de las clases, variables, metodos ,atributos, etc. deben estar en español y seben ser entendibles por 
 el usuario
* usa esta clase lectorimagen para cargar la imagen en el practico:
 ```text
    LectorImagen{
    private static Logger logger = LogManager.getRootLogger();

    public static BufferedImage leerImagen(String ruta){
        logger.info("Inicio de lectura de imagen");
        try {
            if (ruta.startsWith("http://") || ruta.startsWith("https://")){
                logger.debug("Ruta web detectada, usando URL");
                return  ImageIO.read(new URL(ruta));
            }else {
                logger.debug("Ruta de archivo detectada, usando archivo");
                return  ImageIO.read(new File(ruta));
            }

        }catch (Exception e){
            logger.error("Error al leer imagen: " + ruta, e);
            return null;
        }
    }
}
```
* Usa logs en todas las clases y partes que sean nacesarias, desde el nivel info, esta es la libreia q se esta usando:
```text
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
```
* para los logs usa getRootLogger, en vez del otro
## Ubicacion
Este proyecto debe estar dentro del paquete VisorDeImagenes
```text
   package VisorDeImagenes
```

## Paso 1: definir la estructura del programa
```text
  📁 VisorDeImagenes
    ├── 📁 utileria
    │   └── 📄 LectorImagen.java         
    │
    ├── 📁 modelo
    │   ├── 📄 ModeloImagen.java         (El "Director" y Sujeto Observable)
    │   ├── 📄 Redimensionable.java      (Interfaz 1)
    │   ├── 📄 RedimensionImagen.java     (Implementa Redimensionable)
    │   ├── 📄 Cifrable.java   (Interfaz 2)
    │   └── 📄 CifradoImagen.java         (Implementa Cifrable)
    │
    ├── 📁 vista
        ├── 📄 VentanaPrincipal.java     (El JFrame con los botones, adema tendra el metodo main para correr la app)
        └── 📄 PanelImagen.java          (El JPanel que dibuja e implementa PropertyChangeListener)
  
```
## Paso 2: LectorImagen
 Esta clase solo se encarga de cargar archivos de imagen ya sean web o locales, esta clase ya esta hecha:
 ```text
  public class LectorImagen{
    private static Logger logger = LogManager.getRootLogger();

    public static BufferedImage leerImagen(String ruta){
        logger.info("Inicio de lectura de imagen");
        try {
            if (ruta.startsWith("http://") || ruta.startsWith("https://")){
                logger.debug("Ruta web detectada, usando URL");
                return  ImageIO.read(new URL(ruta));
            }else {
                logger.debug("Ruta de archivo detectada, usando archivo");
                return  ImageIO.read(new File(ruta));
            }

        }catch (Exception e){
            logger.error("Error al leer imagen: " + ruta, e);
            return null;
        }
    }
}
 ```
## Paso3: Interfaz Cifrable
Esta interfaz tiene q tener dos metodos q deben implementar cualquier clase q implemente esta interfaz:
```text
G cifrar (G dato, String clave);
G descifrar(G dato, String clave);
```

## Paso4: Interfaz Redimensionable
Esta interfaz tiene q tener dos metodos q deben implementar cualquier clase q implemente esta interfaz:
```text
G agrandar (G objeto, int nuevoAncho, int nuevoAlto);
G achicar(G objeto, int nuevoAncho, int nuevoAlto);
```
## Paso5: Clase RedimensionImagen
Esta clase es una clase obrera , q implementara la interfaz redimensionable y tendra que escribir esos metodos para usarlos en la clase modeloImagen,
```text
public int[][] achicar(int[][] matriz2D, int nuevoancho, nuevo alto){}
public int[][] agrandar(int[][] matriz2D, int nuevoancho, nuevo alto){}
```
### algoritmo de achicar
Para el algoritmo de achicar, se selecciona el píxel correspondiente a la
ubicación de la relación entre el
antiguo y el nuevo tamaño. Por ejemplo,
si tengo un arreglo de 10 enteros y lo achico
a 3 enteros, entonces tomo el primer píxul y lo
pongo en la primera posición, luego se toma
la relación (10 / 3 = 3) y ya sé que tengo
que saltar los 3 próximos píxeles para recien colocar el 2do píxel en el resultado.

### algoritmo de agrandar
El algoritmo que se debe utilizar es una aproximación lineal para los píxeles intermedios.
Ejemplo: imagen de 3 pixeles crece a imagen e 15 pixeles. En el canal rojo tenemos:
20, 50, 60 (para los 3 pixeles)
Entre el primer y segundo pixel tenemos 50-20 = 30 tonos de diferencia.
Y tenemos 15px / 3px = 5px a llenar. Entonces se divide 30 tonos entre 5px = 6.

Esto nos permite saber que los 5 primeros pixeles de la imagen de 15 tendr{an los siguiente valores de rojo:
20, (20+6) = 26, 32, 38, 44

# Paso6: Clase CifradoImagen
Esta clase debe implementar los metodos de la interfaz cifrable, q igual se usaran en la clase modeloImagen
```text
public int[][] cifrar(int[][] matriz, String clave){}
public int[][] descifrar(int[][] matriz, String clave){}
```
### algoritmo de cifrado
se tendria q usar el hascode del string(q este seria la clave ingresada por el usuario),
para cirar, se tendria que recorrer la matriz intercambiando cada pixel con una posicion aleatoria(random) en base a la
clave, para poder obtener una imagen con los pixeles desordenados y ruidosa.
### algoritmo de descifrado
se tendria q usar el mismo algoritmo de cifrado pero a la inversa, si la clave es la indicada(o sea la misma q se uso para cifrar)
la imagen tendria lospixeles tendrian q volver a su posicion originbal antes de cifrar, si la clave es diferente, el algoritmo 
igualmente deberia funcionar, pero obiamente no tendria q volver a la imagen original(por q no seria el mismo hascode para producir los mismos numeros aleatorios)

# Paso7: Clase ModeloImagen
Esta clase es el nucleo del proyecto, representa una imagen pero en sus datos mas puros(una matriz bidimensional de pixeles), aqui 
quiero q implementes los metodos y atributos adecuados para esta clase
* uno de los varios metodos seria
el de redimensionar(aui debes usar los metodos de redimensionImagen para poder redimensionar la imagen, y tambien tendrias que manejar la opcion de mantener proporcion
tanto si el usuario cambia el alto como el ancho, o solo cambia uno de los dos), en este metodo para nop dañr lños datos de la imagen original,
se tendria q devolver una nueva imagen y no la imagen original
* otro serian los metodos de cifrar y descifrar(estos metodos usarian los metodos de cifradoImagen)
* aplica todos los getters y setters necesarios
* al construir la imagen  el parametro q recibe es un buferredImagen, el cual se deberia convertir a una matriz bidimensional

````text
 # Posdata
 En todas las clases del paquete modelo cada metodo se tiene q encargar de una sola cosa,
  si se necesita hacer otros algoritmos adentro del metodo pa q este funcione,
 crea metodos q se encarguen de ese algoritmo y solo llamalas en le metodo necesario , 
 osea si son necesarios mas metodos crealos, ya sen private o public, tambien incluye a los
 constructores
````

# Paso9: Clase PanelImagen
Esta clase es el panel grafico de la imagen, crea todos los metodos, atributos y el constructor necesaario en base a los requerimientos del proyecto

# Paso10: Clase VentanaPrincipal
Esta es la clase de la interfaz grafica, deberia tener arriba en la esquina superior izquierda tres botones:
* boton desplegable cargar(desde el cual se abre un submenu con la opcion de url y archivo local, al apretar cualquiera de estos dos botones, debe aparecer una ventana
emergente  para poder pegar la url o en caso del boton archivo local, navegar por el sistema)
* boton de redimensionar en la cual se abra una ventana emergente, donde se pueda poner el nuevo ancho y alto y tambien la opcion de mantener proporcion
* boton de cifrar, que al apretar este boton,aparezca una ventana emergente para poner la clave y en base a esta clave la imagen se cifrara
* boton de descifrar, que al apretar este boton, aparezca una ventana emergenta para poner la clave y asi poder descifra si la clave es  correcta, la imagen vuelva a su estado original
* tendria q tener igual el metodo main para correr la aplicacion
