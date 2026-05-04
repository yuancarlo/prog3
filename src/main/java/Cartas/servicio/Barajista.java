package Cartas.servicio;

import Cartas.modelo.Carta;
import Cartas.modelo.Mazo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public class Barajista {
    private static final Logger logger = LogManager.getLogger(Barajista.class);
    private Mazo mazo;

    public Barajista() {
        this.mazo = new Mazo();
    }

    public void barajarMazo() {
        logger.info("Ejecutando barajado ");
        List<Carta> lista = mazo.getMazo();
        Random rand = new Random();

        for (int i = lista.size() - 1; i > 0; i--) {

            int j = rand.nextInt(i + 1);

            Carta temp = lista.get(i);
            lista.set(i, lista.get(j));
            lista.set(j, temp);
        }
        logger.debug("Fase de barajado completada. Procediendo a actualizar índices.");

        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).setPosicionActual(i);
        }
        logger.debug("Posiciones actuales actualizadas tras el barajado. Barajado exitosamente");

    }

    public int calcularDistanciaMinima() {
        int min = mazo.getMazo().size();
        for (Carta carta : mazo.getMazo()) {
            int dist = Math.abs(carta.getPosicionActual() - carta.getPosicionOriginal());
            if (dist < min) {
                min = dist;
            }
        }
        logger.debug("Distancia mínima calculada: {}", min);
        return min;
    }

    public int calcularDistanciaMaxima() {
        int max = 0;
        for (Carta carta : mazo.getMazo()) {
            int dist = Math.abs(carta.getPosicionActual() - carta.getPosicionOriginal());
            if (dist > max) {
                max = dist;
            }
        }
        logger.debug("Distancia máxima calculada: {}", max);
        return max;
    }

    public double calcularDistanciaPromedio() {
        double suma = 0;
        List<Carta> cartas = mazo.getMazo();
        for (Carta carta : cartas) {
            suma += Math.abs(carta.getPosicionActual() - carta.getPosicionOriginal());
        }
        double promedio = suma / cartas.size();
        logger.debug("Distancia promedio calculada: {}", promedio);
        return promedio;
    }


    public String interpretarResultados() {

        int min = this.calcularDistanciaMinima();
        int max = this.calcularDistanciaMaxima();
        double promedio = this.calcularDistanciaPromedio();

        String conclusion;
        if (promedio >= 15) {
            conclusion = "ÉXITO: El mazo tiene un alto grado de dispersión.";
        } else if (promedio >= 8) {
            conclusion = "ADVERTENCIA: Mezcla moderada. Se detectan patrones.";
        } else {
            conclusion = "ERROR: Mezcla deficiente. El mazo mantiene su estructura.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("       REPORTE DE BARAJADO         \n");
        sb.append("Distancia Mínima: ").append(min).append("\n");
        sb.append("Distancia Máxima: ").append(max).append("\n");
        sb.append("Distancia Promedio: ").append(String.format("%.2f", promedio)).append("\n");
        sb.append("-----------------------------------\n");
        sb.append("ANÁLISIS: ").append(conclusion).append("\n");

        return sb.toString();
    }




}
