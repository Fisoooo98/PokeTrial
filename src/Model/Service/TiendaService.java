package Model.Service;

import Model.DAO.InventarioDAOImpl;
import Model.DAO.JugadorDAOImpl;
import Model.Entities.*;
import Model.Exceptions.MonedasInsuficientesException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TiendaService {
    PokeApiService api = new PokeApiService();
    InventarioDAOImpl inventarioDAO = new InventarioDAOImpl();
    JugadorDAOImpl jugadorDAO = new JugadorDAOImpl();
    Random random = new Random();

    public Rareza calcularRarezaPremiada(TipoSobre sobre) {
        // Genera un número entre 1 y 100
        int numeroAleatorio = random.nextInt(100) + 1;

        // Escalón 1: Comunes
        int limiteActual = sobre.getProbComun();
        if (numeroAleatorio <= limiteActual) {
            return Rareza.COMUN;
        }

        // Escalón 2: Raras (Acumulamos su probabilidad)
        limiteActual += sobre.getProbRaro();
        if (numeroAleatorio <= limiteActual) {
            return Rareza.RARO;
        }

        // Escalón 3: Épicas
        limiteActual += sobre.getProbEpico();
        if (numeroAleatorio <= limiteActual) {
            return Rareza.EPICO;
        }

        // Escalón 4: Si no fue ninguna de las anteriores, por descarte es Legendaria
        return Rareza.LEGENDARIO;
    }

    public List<Carta> comprarSobre(Jugador jugador, TipoSobre rarezasobre) throws MonedasInsuficientesException {
        int precio = 0;
        //Metemos los precios de las cartas
        switch (rarezasobre) {
            case TipoSobre.BRONCE -> precio = 100;
            case TipoSobre.PLATA -> precio = 400;
            case TipoSobre.ORO -> precio = 1000;
        }
        //Combrobamos que el usuario se puede permitir él sobre
        if (jugador.getPuntosBatalla() >= precio) {
            // Si le alcanza, le cobramos y el flujo sigue adelante
            jugador.setPuntosBatalla(jugador.getPuntosBatalla() - precio);
        } else {
            // SI NO LE ALCANZA, acá es donde tú fabricas y lanzas la excepción
            throw new MonedasInsuficientesException("No tienes suficientes Puntos de Batalla. Necesitas " + precio);
        }
       //Generamos 5 pokemons aletorios
        List<Carta> cartasObtenidas = new ArrayList<>();
       for (int i=0;i<5;i++) {
           int id = random.nextInt(100);
           Pokemon pokemonBase = api.cargarDatosPokemon(id);
           //Generamos las cartas con sus rarezas
           Rareza rarezaPremiada = calcularRarezaPremiada(rarezasobre);
           Carta nuevaCarta = new Carta(pokemonBase, rarezaPremiada);
           cartasObtenidas.add(nuevaCarta);
       }
        try {
            // Le pasamos el ID del jugador y la lista entera de cartas que le tocaron
            inventarioDAO.guardarCartasEnInventario(jugador.getId(), cartasObtenidas);
            System.out.println("💾 ¡Colección actualizada en la Base de Datos con éxito!");
        } catch (Exception e) {
            // Si falla la BD, lanzamos un aviso pero no rompemos el retorno de las cartas en memoria
            System.err.println("❌ Error crítico al guardar el sobre en la BD: " + e.getMessage());
        }
       return cartasObtenidas;
    }
}
