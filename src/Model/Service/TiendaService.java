package Model.Service;

import Model.DAO.InventarioDAOImpl;
import Model.DAO.JugadorDAOImpl;
import Model.Entities.*;
import Model.Exceptions.MonedasInsuficientesException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

    public List<Carta> comprarSobre(Jugador jugador, TipoSobre rarezasobre, int region) throws MonedasInsuficientesException {
        int precio = 0;

        // 1. Determinamos los precios del sobre
        switch (rarezasobre) {
            case TipoSobre.BRONCE -> precio = 100;
            case TipoSobre.PLATA -> precio = 400;
            case TipoSobre.ORO -> precio = 1000;
        }

        // 2. Comprobamos si el usuario se puede permitir el sobre
        if (jugador.getPuntosBatalla() >= precio) {
            // Si le alcanza, le cobramos y el flujo sigue adelante
            jugador.setPuntosBatalla(jugador.getPuntosBatalla() - precio);
        } else {
            // Si no le alcanza, lanzamos la excepción personalizada
            throw new MonedasInsuficientesException("No tienes suficientes Puntos de Batalla. Necesitas " + precio + " PB.");
        }

        // Lista donde guardaremos las 5 cartas finales del sobre
        List<Carta> cartasObtenidas = new ArrayList<>();

        // 3. El bucle principal controla que se generen EXACTAMENTE 5 cartas
        for (int i = 0; i < 5; i++) {
            int id = 0;
            int intentos = 0;
            boolean esUltimaEvo = false;

            // 4. Bucle secundario: Reintentos (re-roll) para conseguir una última evolución en el slot actual
            do {
                // Generamos un ID aleatorio según la región seleccionada
                switch (region) {
                    case 1 -> id = random.nextInt(1, 152);   // Kanto: 1 al 151
                    case 2 -> id = random.nextInt(152, 252); // Johto: 152 al 251
                    case 3 -> id = random.nextInt(252, 387); // Hoenn: 252 al 386
                    case 4 -> id = random.nextInt(387, 494); // Sinnoh: 387 al 493
                    default -> id = random.nextInt(1, 152);
                }

                // Consultamos a la PokéAPI a través de tu método para comprobar la etapa evolutiva
                esUltimaEvo = esUltimaEvolucionAPI(id);
                intentos++;

                // Se repite MIENTRAS NO sea última evolución Y todavía queden intentos disponibles según el sobre
            } while (!esUltimaEvo && intentos < rarezasobre.getProbUltiEvo());

            // 5. Una vez que salimos del do-while (por éxito o por agotar intentos), cargamos el Pokémon definitivo
            Pokemon pokemonBase = api.cargarDatosPokemon(id);

            // 6. Calculamos la rareza correspondiente de la carta usando tu lógica de dados de siempre
            Rareza rarezaPremiada = calcularRarezaPremiada(rarezasobre);

            // 7. Instanciamos la nueva carta y la añadimos a la colección del sobre
            Carta nuevaCarta = new Carta(pokemonBase, rarezaPremiada);
            cartasObtenidas.add(nuevaCarta);
        }

        // 8. Persistencia: Guardamos las 5 cartas obtenidas de un solo golpe en la Base de Datos
        try {
            inventarioDAO.guardarCartasEnInventario(jugador.getId(), cartasObtenidas);
            System.out.println("💾 ¡Colección actualizada en la Base de Datos con éxito!");
        } catch (Exception e) {
            // Si falla Postgres, mandamos un log de error pero permitimos que el jugador vea sus cartas en memoria
            System.err.println("❌ Error crítico al guardar el sobre en la BD: " + e.getMessage());
        }

        // Retornamos las 5 cartas para que el controlador se las envíe a la VentanaTiendaSobres y al PanelRevelarCartas
        return cartasObtenidas;
    }

    // ... Dentro de tu TiendaService ...

    /**
     * Comprueba a través de la PokéAPI si un Pokémon por su ID es una última evolución.
     * * @param idPokemon El ID del Pokémon a verificar.
     * @return true si es etapa final (o no evoluciona), false si aún puede evolucionar.
     */
    public boolean esUltimaEvolucionAPI(int idPokemon) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // 1. Consultar el endpoint de la especie
            String urlEspecie = "https://pokeapi.co/api/v2/pokemon-species/" + idPokemon + "/";
            HttpRequest requestEspecie = HttpRequest.newBuilder().uri(URI.create(urlEspecie)).GET().build();
            HttpResponse<String> responseEspecie = client.send(requestEspecie, HttpResponse.BodyHandlers.ofString());

            if (responseEspecie.statusCode() != 200) return false;

            JsonObject jsonEspecie = JsonParser.parseString(responseEspecie.body()).getAsJsonObject();

            // 🟢 CORREGIDO: El nombre está en la raíz del JSON de la especie
            String nombrePokemon = jsonEspecie.get("name").getAsString();

            // 🟢 CORREGIDO: 'evolution_chain' está en la raíz y dentro tiene el campo 'url'
            JsonObject evolutionChainObj = jsonEspecie.getAsJsonObject("evolution_chain");
            if (evolutionChainObj == null || evolutionChainObj.isJsonNull()) return false;
            String urlCadenaEvo = evolutionChainObj.get("url").getAsString();

            // 2. Consultar la cadena de evolución real
            HttpRequest requestCadena = HttpRequest.newBuilder().uri(URI.create(urlCadenaEvo)).GET().build();
            HttpResponse<String> responseCadena = client.send(requestCadena, HttpResponse.BodyHandlers.ofString());

            if (responseCadena.statusCode() != 200) return false;

            JsonObject jsonCadena = JsonParser.parseString(responseCadena.body()).getAsJsonObject();

            // 🟢 CORREGIDO: Validamos que 'chain' exista antes de usarlo
            if (!jsonCadena.has("chain") || jsonCadena.get("chain").isJsonNull()) return false;
            JsonObject nodoActual = jsonCadena.getAsJsonObject("chain");

            // 3. Recorrer el árbol evolutivo recursivo buscando a nuestro Pokémon
            return buscarYVerificarSiEsFinDeCadena(nodoActual, nombrePokemon);

        } catch (Exception e) {
            System.err.println("❌ Error al verificar evolución en PokéAPI: " + e.getMessage());
            // Devolvemos true por seguridad para que el sobre avance y no se quede en bucle infinito
            return true;
        }
    }
    /**
     * Método auxiliar recursivo para navegar por los nodos 'evolves_to' de la API.
     */
    private boolean buscarYVerificarSiEsFinDeCadena(JsonObject nodo, String nombreBuscado) {
        if (nodo == null || nodo.isJsonNull()) return false;

        // Extraemos el nombre del Pokémon del nodo actual
        String nombreEspecieActual = nodo.getAsJsonObject("species").get("name").getAsString();
        JsonArray evolucionesSiguientes = nodo.getAsJsonArray("evolves_to");

        // Si coincide con el que estamos buscando en el sobre...
        if (nombreEspecieActual.equalsIgnoreCase(nombreBuscado)) {
            // Si no tiene más ramas evolutivas ('evolves_to' está vacío), ¡es última evolución!
            return evolucionesSiguientes.isEmpty();
        }

        // Si no es el Pokémon buscado, recorremos las ramas (por si es una evolución intermedia)
        for (int i = 0; i < evolucionesSiguientes.size(); i++) {
            boolean encontrado = buscarYVerificarSiEsFinDeCadena(evolucionesSiguientes.get(i).getAsJsonObject(), nombreBuscado);
            if (encontrado) {
                return encontrado;
            }
        }

        return false;
    }
}