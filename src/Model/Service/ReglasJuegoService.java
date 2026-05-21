package Model.Service;

import Model.Entities.*;

import java.util.Random;

public class ReglasJuegoService {
    CalculadoraCombateService cs = new CalculadoraCombateService();
    Random rd = new Random();

    //Si da true: J1 primer turno, Si da false: J2 primer turno
    public boolean decidirPrimerTurno(){
       int numero = rd.nextInt(10) + 1;
       if (numero >= 5){
           return true;
       }else{
           return false;
       }
    }

    //Hace que el jugador pueda ejecutar su movimiento y lo valide
    public boolean ejecutarMovimiento(Tablero tablero, Carta carta, Jugador jugador,int fila,int columna){
        System.out.println("DEBUG -> ¿tablero.esLleno()?: " + tablero.esLleno());
        System.out.println("DEBUG -> ¿getCasilla(" + fila + "," + columna + ") es nulo?: " + (tablero.getCasilla(fila, columna) == null));
        if (tablero.esLleno() || (tablero.getCasilla(fila,columna) != null && !tablero.getCasilla(fila,columna).isVacia())){
            return false;
        }else {
            tablero.setCasilla(new Casilla(carta,jugador),fila,columna);
            // ARRIBA: Solo si fila > 0
            if (fila > 0) {
                evaluarCombateVecino(carta, tablero.getCasilla(fila - 1, columna), jugador, Direcciones.UP);
            }

            // ABAJO: Solo si fila < 2
            if (fila < 2) {
                evaluarCombateVecino(carta, tablero.getCasilla(fila + 1, columna), jugador, Direcciones.DOWN);
            }

            // IZQUIERDA: Solo si columna > 0
            if (columna > 0) {
                evaluarCombateVecino(carta, tablero.getCasilla(fila, columna - 1), jugador, Direcciones.LEFT);
            }

            // DERECHA: Solo si columna < 2
            if (columna < 2) {
                evaluarCombateVecino(carta, tablero.getCasilla(fila, columna + 1), jugador, Direcciones.RIGHT);
            }
            return true;
        }
    }

    private void evaluarCombateVecino(Carta cartaAtacante, Casilla casillaVecina, Jugador jugadorActual, Direcciones direccion) {
        //Validamos que la casilla vecina exista y tenga una carta dentro
        if (casillaVecina != null && !casillaVecina.isVacia()) {

            //Si el propietario de la vecina NO es el jugador actual, ¡es un rival!
            if (!casillaVecina.getPropietario().equals(jugadorActual)) {

                //Lanzamos el combate usando tu servicio de reglas/calculadora
                boolean ganado = cs.enfrentarCartas(cartaAtacante, casillaVecina.getCarta(), direccion);

                //Si ganamos, volteamos la carta cambiándole el dueño al jugador actual
                if (ganado) {
                    casillaVecina.setPropietario(jugadorActual);
                    System.out.println("Has volteado la carta de la dirección: " + direccion);
                }
            }
        }
    }
}
