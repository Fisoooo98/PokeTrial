package Model.Service;

import Model.Entities.Carta;
import Model.Entities.Direcciones;
import Model.Entities.TablaTipos;

public class CalculadoraCombateService {

    //Calcula si la carta pilla la casilla o no
    public boolean enfrentarCartas(Carta atacante, Carta defensor, Direcciones direccion) {
        double multiplicador = TablaTipos.obtenerMultiplicador(atacante.getTipo(),defensor.getTipo());
        switch (direccion) {
            //Derecha: DF --> atkE
            case Direcciones.RIGHT:
                if (atacante.getDF() * multiplicador > defensor.getAtkE()) return true;
                break;
            //Izquierda: atkE --> DF
            case Direcciones.LEFT:
                if (atacante.getAtkE() * multiplicador > defensor.getDF()) return true;
                break;
            //Arriba:  atkF --> DE
            case Direcciones.UP:
                if (atacante.getAtkF() * multiplicador > defensor.getDE()) return true;
                break;
            //Abajo: DE --> atkF
            case Direcciones.DOWN:
                if (atacante.getDE() * multiplicador > defensor.getAtkF()) return true;
                break;
        }
        return false;
    }
}
