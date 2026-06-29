package ServidorWeb.Modelo;

import Utilidades.ArbolAritmetico.ArbolAritmetico;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Importamos las constantes nativas de Java para los códigos HTTP
import static java.net.HttpURLConnection.*;

public class ProtocoloWeb implements Runnable {
    private final Socket clt;
    private final OutputStream salida;
    private final InputStream entrada;
    private static final Logger logger = LogManager.getRootLogger();

    private static final String REGEX_GET = "^GET\\s/op\\?q=(.+)\\sHTTP/1\\.1$";
    private static final String MIME_HTML = "text/html";
    private static final String MIME_JPG = "image/jpeg";

    // Mantenemos la compatibilidad exacta con tu ServidorWeb actual
    public final PropertyChangeSupport observadorProtocolo;

    public ProtocoloWeb(Socket cliente) throws IOException {
        this.clt = cliente;
        this.salida = clt.getOutputStream();
        this.entrada = clt.getInputStream();
        this.observadorProtocolo = new PropertyChangeSupport(this);
    }

    @Override
    public void run() {
        long bytesIn = 0;
        long bytesOut = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8))) {
            String peticion = reader.readLine();

            if (peticion == null || peticion.trim().isEmpty()) {
                return;
            }

            bytesIn = peticion.getBytes(StandardCharsets.UTF_8).length;
            Matcher matcher = Pattern.compile(REGEX_GET).matcher(peticion);

            if (matcher.find()) {
                String expresionCodificada = matcher.group(1);
                bytesOut = procesarExpresionMatematica(expresionCodificada);
            } else {
                bytesOut = procesarRutaDesconocida(peticion);
            }

        } catch (Exception e) {
            logger.error("Error crítico inesperado en el hilo de conexión", e);
            bytesOut = responderError(HTTP_INTERNAL_ERROR, "Internal Server Error",
                    "Error Interno", "Fallo imprevisto en el servidor.");
        } finally {
            // Notificamos a la ventana (UI) manteniendo tu compatibilidad
            observadorProtocolo.firePropertyChange("NUEVO_TRAFICO", null, new long[]{bytesIn, bytesOut});
            cerrarSocket();
        }
    }

    // --- MÉTODOS CONTROLADORES DE RUTAS ---

    private long procesarExpresionMatematica(String expresionCodificada) {
        try {
            String expresion = decodificarUrl(expresionCodificada);
            ArbolAritmetico arbol = ParserAritmetico.construirDesdeString(expresion);
            return responderImagen(arbol);

        } catch (IllegalArgumentException e) {
            logger.warn("URL mal codificada: " + e.getMessage());
            return responderError(HTTP_BAD_REQUEST, "Bad Request", "Error de Codificación",
                    "Falta codificar correctamente caracteres como el % o el +.");
        } catch (Exception e) {
            logger.warn("Error aritmético: " + e.getMessage());
            return responderError(HTTP_BAD_REQUEST, "Bad Request", "Expresión Inválida",
                    "Verifica los paréntesis y operadores de la expresión matemática.");
        }
    }

    private long procesarRutaDesconocida(String peticion) {
        if (peticion.contains("favicon.ico")) {
            return responderError(HTTP_NOT_FOUND, "Not Found", "Favicon No Encontrado", "");
        } else {
            logger.warn("Ruta no soportada: " + peticion);
            return responderError(HTTP_BAD_REQUEST, "Bad Request", "Ruta Incorrecta",
                    "Usa el formato: /op?q={expresion_codificada}");
        }
    }

    // --- MÉTODOS DE RESPUESTA (VISTAS) ---

    private long responderImagen(ArbolAritmetico arbol) {
        byte[] imgBytes = crearBufferImagen(arbol);
        if (imgBytes == null) return responderError(HTTP_INTERNAL_ERROR, "Error", "Fallo Gráfico", "No se pudo generar la imagen");

        String encabezado = crearEncabezado(HTTP_OK, "OK", MIME_JPG, imgBytes.length);
        enviarRespuesta(encabezado, imgBytes);

        return encabezado.getBytes(StandardCharsets.UTF_8).length + imgBytes.length;
    }

    private long responderError(int codigoHttp, String mensajeHttp, String titulo, String descripcion) {
        String html = descripcion.isEmpty() ? "" : crearHtmlError(codigoHttp, mensajeHttp, titulo, descripcion);
        byte[] contenido = html.getBytes(StandardCharsets.UTF_8);

        String encabezado = crearEncabezado(codigoHttp, mensajeHttp, MIME_HTML, contenido.length);
        enviarRespuesta(encabezado, contenido);

        return encabezado.getBytes(StandardCharsets.UTF_8).length + contenido.length;
    }

    // --- MÉTODOS UTILITARIOS (HERRAMIENTAS BASE) ---

    private byte[] crearBufferImagen(ArbolAritmetico arbol) {
        try {
            BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            DibujoArbolAritmetico dibujante = new DibujoArbolAritmetico(arbol);
            dibujante.dibujar(g2d, 50, 50);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            logger.error("Error al escribir los bytes de la imagen", e);
            return null;
        }
    }

    private String crearHtmlError(int codigo, String mensaje, String titulo, String descripcion) {
        return "<html><body style='font-family: \"Segoe UI\", Arial, sans-serif; background-color: #f8f9fa; color: #495057; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0;'>" +
                "<div style='background: white; padding: 40px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.05); text-align: center; max-width: 500px; border-top: 5px solid #e74c3c;'>" +
                "<div style='font-size: 72px; font-weight: 800; color: #e74c3c; line-height: 1; margin-bottom: 10px;'>" + codigo + "</div>" +
                "<h2 style='color: #2c3e50; margin: 0 0 15px 0; font-size: 20px;'>" + mensaje + " — " + titulo + "</h2>" +
                "<p style='font-size: 14px; color: #6c757d; margin: 0;'>" + descripcion + "</p>" +
                "</div></body></html>";
    }

    private String crearEncabezado(int code, String msg, String mime, int contentLength) {
        return "HTTP/1.1 " + code + " " + msg + "\r\n" +
                "Content-Type: " + mime + "; charset=UTF-8\r\n" +
                "Content-Length: " + contentLength + "\r\n" +
                "Connection: close\r\n\r\n";
    }

    private void enviarRespuesta(String encabezado, byte[] contenido) {
        try {
            salida.write(encabezado.getBytes(StandardCharsets.UTF_8));
            if (contenido.length > 0) {
                salida.write(contenido);
            }
            salida.flush();
        } catch (IOException e) {
            logger.error("Fallo al escribir en el canal de salida hacia el cliente", e);
        }
    }

    private String decodificarUrl(String encoded) {
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }

    private void cerrarSocket() {
        try {
            if (clt != null && !clt.isClosed()) clt.close();
        } catch (IOException e) {
            logger.error("Error cerrando socket del cliente", e);
        }
    }

    public void addObserver(PropertyChangeListener listener) {
        this.observadorProtocolo.addPropertyChangeListener(listener);
    }
}