package Model.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import Model.Entities.Pokemon;
import Model.Entities.Tipo;


public class PokeApiService {

    private final HttpClient httpClient;

    public PokeApiService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Auxiliar para realizar peticiones GET y devolver el cuerpo como String.
     */
    private String realizarPeticionHttp(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Error HTTP: " + response.statusCode() + " en la URL: " + url);
        }
    }

    /**
     * RFE-04: Obtiene los IDs de los Pokémon que pertenecen a un tipo elemental específico.
     */
    public List<Integer> obtenerIdsPorTipo(Tipo tipo) {
        List<Integer> ids = new ArrayList<>();
        // Traducimos el Enum de español a lo que espera la API en inglés (ej: DRAGON -> "dragon")
        String tipoApi = mapearTipoAStringApi(tipo);
        String url = "https://pokeapi.co/api/v2/type/" + tipoApi;

        try {
            String jsonResponse = realizarPeticionHttp(url);
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray pokemonArray = jsonObject.getAsJsonArray("pokemon");

            for (JsonElement element : pokemonArray) {
                JsonObject pokemonInfo = element.getAsJsonObject().getAsJsonObject("pokemon");
                String urlPokemon = pokemonInfo.get("url").getAsString();

                // La URL tiene formato: https://pokeapi.co/api/v2/pokemon/{id}/
                // Extraemos el ID numérico del final de la URL
                String[] urlParts = urlPokemon.split("/");
                int id = Integer.parseInt(urlParts[urlParts.length - 1]);

                // Opcional: Limitamos a los primeros 386 Pokémon para evitar formas alternativas raras de la API
                if (id <= 386) {
                    ids.add(id);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener IDs por tipo: " + e.getMessage());
        }
        return ids;
    }

    /**
     * RFE-04: Obtiene los IDs de los Pokémon según su generación.
     */
    public List<Integer> obtenerIdsPorGeneracion(int gen) {
        List<Integer> ids = new ArrayList<>();
        // En lugar de saturar la API con consultas pesadas de cadenas complejas,
        // aprovechamos que los IDs de la Pokédex por generación son fijos y conocidos.
        int inicio = 1;
        int fin = 151;

        switch (gen) {
            case 1: inicio = 1; fin = 151; break;     // Gen 1: Bulbasaur a Mew
            case 2: inicio = 152; fin = 251; break;   // Gen 2: Chikorita a Celebi
            case 3: inicio = 252; fin = 386; break;   // Gen 3: Treecko a Deoxys
            default: inicio = 1; fin = 151; break;
        }

        for (int i = inicio; i <= fin; i++) {
            ids.add(i);
        }
        return ids;
    }

    /**
     * RFE-04: Filtra y obtiene los IDs de Pokémon que son terceras evoluciones.
     * Nota: Debido a la estructura de la PokeAPI, comprobar esto de forma dinámica en internet
     * requiere muchas peticiones por segundo. Para proteger el rendimiento de tu juego,
     * lo ideal es predefinir una lista con IDs de terceras evoluciones populares (G1 a G3).
     */
    public List<Integer> obtenerUltimasEvoluciones() {
        List<Integer> idsFinales = new ArrayList<>();

        // Recorremos las primeras 50 cadenas evolutivas de la API (cubre G1-G3 de forma óptima)
        for (int i = 1; i <= 50; i++) {
            String url = "https://pokeapi.co/api/v2/evolution-chain/" + i;
            try {
                // 1. Petición HTTP
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                    JsonObject chain = jsonObject.getAsJsonObject("chain");

                    // 2. Arrancar la búsqueda desde la raíz de la cadena
                    buscarEvolucionesFinales(chain, idsFinales);
                }
            } catch (Exception e) {
                // Si una cadena intermedia da error en la API, saltamos a la siguiente sin romper el programa
            }
        }
        return idsFinales;
    }

    /**
     * Método auxiliar recursivo que rastrea el JSON de la API hacia abajo.
     * Si detecta que "evolves_to" está vacío, significa que el Pokémon es la última evolución.
     */
    private void buscarEvolucionesFinales(JsonObject nodoActual, List<Integer> listaIds) {
        JsonArray evolvesTo = nodoActual.getAsJsonArray("evolves_to");

        // CASO BASE: Si ya no tiene más evoluciones por delante, este es el Pokémon final
        if (evolvesTo == null || evolvesTo.size() == 0) {
            JsonObject species = nodoActual.getAsJsonObject("species");
            String urlSpecies = species.get("url").getAsString();

            // Extraemos el ID numérico del final de la URL de la especie
            String[] urlParts = urlSpecies.split("/");
            int id = Integer.parseInt(urlParts[urlParts.length - 1]);

            // Filtro de seguridad para mantenernos en el rango del juego (G1 a G3)
            if (id <= 386) {
                listaIds.add(id);
            }
        } else {
            // RECURSIVIDAD: Si tiene evoluciones, seguimos bajando por cada una de ellas
            for (JsonElement evolucion : evolvesTo) {
                buscarEvolucionesFinales(evolucion.getAsJsonObject(), listaIds);
            }
        }
    }

    /**
     * Descarga los detalles de un Pokémon por su ID y construye la entidad Pokemon para el juego.
     */
    public Pokemon cargarDatosPokemon(int idApi) {
        String url = "https://pokeapi.co/api/v2/pokemon/" + idApi;

        try {
            String jsonResponse = realizarPeticionHttp(url);
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

            String nombre = jsonObject.get("name").getAsString();

            // Extraer tipo principal
            JsonArray typesArray = jsonObject.getAsJsonArray("types");
            String nombreTipoApi = typesArray.get(0).getAsJsonObject()
                    .get("type").getAsJsonObject()
                    .get("name").getAsString().toUpperCase();

            Tipo tipoEnum = mapearTipoApiAEnum(nombreTipoApi);

            // Extraer estadísticas base
            JsonArray statsArray = jsonObject.getAsJsonArray("stats");
            int attack = statsArray.get(1).getAsJsonObject().get("base_stat").getAsInt();
            int defense = statsArray.get(2).getAsJsonObject().get("base_stat").getAsInt();
            int spAttack = statsArray.get(3).getAsJsonObject().get("base_stat").getAsInt();
            int spDefense = statsArray.get(4).getAsJsonObject().get("base_stat").getAsInt();

            // Adaptar estadísticas al rango de cartas (1-10) dividiendo entre 10
            int atkF = Math.max(1, attack / 10);
            int DE = Math.max(1, spDefense / 10);
            int atkE = Math.max(1, spAttack / 10);
            int DF = Math.max(1, defense / 10);

            // 🛠️ EXTRACCIÓN DEL SPRITE DE 4ª GENERACIÓN (Diamond & Pearl)
            JsonObject sprites = jsonObject.getAsJsonObject("sprites");
            JsonObject versions = sprites.getAsJsonObject("versions");
            JsonObject generationIv = versions.getAsJsonObject("generation-iv");
            JsonObject diamondPearl = generationIv.getAsJsonObject("diamond-pearl");

            // Obtenemos la URL de la imagen en formato String
            String spriteUrl = diamondPearl.get("front_default").getAsString();

            // Retornamos usando tu nuevo constructor con las variables mapeadas en el orden correcto
            return new Pokemon(idApi, nombre, atkE, atkF, DE, DF, tipoEnum,spriteUrl);

        } catch (Exception e) {
            System.err.println("Error al cargar datos del Pokémon " + idApi + ": " + e.getMessage());
            return null;
        }
    }

    // --- MÉTODOS AUXILIARES DE CONVERSIÓN ---

    private Tipo mapearTipoApiAEnum(String tipoApi) {
        switch (tipoApi) {
            case "STEEL":     return Tipo.ACERO;
            case "WATER":     return Tipo.AGUA;
            case "BUG":       return Tipo.BICHO;
            case "DRAGON":    return Tipo.DRAGON;
            case "ELECTRIC":  return Tipo.ELECTRICO;
            case "GHOST":     return Tipo.FANTASMA;
            case "FIRE":      return Tipo.FUEGO;
            case "FAIRY":     return Tipo.HADA;
            case "ICE":       return Tipo.HIELO;
            case "FIGHTING":  return Tipo.LUCHA;
            case "NORMAL":    return Tipo.NORMAL;
            case "GRASS":     return Tipo.PLANTA;
            case "PSYCHIC":   return Tipo.PSIQUICO;
            case "ROCK":      return Tipo.ROCA;
            case "DARK":      return Tipo.SINIESTRO;
            case "GROUND":    return Tipo.TIERRA;
            case "POISON":    return Tipo.VENENO;
            case "FLYING":    return Tipo.VOLADOR;
            default:          return Tipo.NORMAL; // Tipo por defecto seguro
        }
    }

    /**
     * Traduce el Enum en español al texto en inglés (lowercase)
     * que requiere la PokeAPI para realizar búsquedas por tipo.
     */
    private String mapearTipoAStringApi(Tipo tipo) {
        switch (tipo) {
            case ACERO:     return "steel";
            case AGUA:      return "water";
            case BICHO:     return "bug";
            case DRAGON:    return "dragon";
            case ELECTRICO: return "electric";
            case FANTASMA:  return "ghost";
            case FUEGO:     return "fire";
            case HADA:      return "fairy";
            case HIELO:     return "ice";
            case LUCHA:     return "fighting";
            case NORMAL:    return "normal";
            case PLANTA:    return "grass";
            case PSIQUICO:  return "psychic";
            case ROCA:      return "rock";
            case SINIESTRO: return "dark";
            case TIERRA:    return "ground";
            case VENENO:    return "poison";
            case VOLADOR:   return "flying";
            default:        return "normal";
        }
    }
}