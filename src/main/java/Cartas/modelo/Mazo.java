package Cartas.modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Mazo {
    private static final Logger logger = LogManager.getLogger(Mazo.class);
    private List<Carta> mazo;
    private Palo[] palos = {Palo.CORAZON,Palo.TREBOL,Palo.DIAMANTE,Palo.PICA};

    public Mazo() {
        this.mazo = new ArrayList<>();
        this.llenarMazo();
    }

    private void llenarMazo() {
        logger.debug("Iniciando llenado de mazo ordenado...");

        int valor = 1;
        int numeroPalo = 0;

        for (int i = 0; i < 52; i++) {
            if (valor == 14){
                valor = 1;
                numeroPalo++;
            }
            Palo palo = palos[numeroPalo];
            this.mazo.add(new Carta(valor,palo, i));
            valor++;

        }
        logger.info("Mazo de 52 cartas generado correctamente.");
    }


    public List<Carta> getMazo() {
        return this.mazo;
    }
}
