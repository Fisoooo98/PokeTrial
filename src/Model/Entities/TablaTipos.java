package Model.Entities;

import java.util.HashMap;
import java.util.Map;

public class TablaTipos {

    // Mapa anidado: Atacante -> (Defensor -> Multiplicador)
    private static final Map<Tipo, Map<Tipo, Double>> tabla = new HashMap<>();

    static {
        // Inicializamos los mapas internos para cada tipo existente en el Enum
        for (Tipo atacante : Tipo.values()) {
            tabla.put(atacante, new HashMap<>());
        }

        // ======================
        // FUEGO
        // ======================
        set(Tipo.FUEGO, Tipo.PLANTA, 2.0);
        set(Tipo.FUEGO, Tipo.HIELO, 2.0);
        set(Tipo.FUEGO, Tipo.BICHO, 2.0);
        set(Tipo.FUEGO, Tipo.ACERO, 2.0);
        set(Tipo.FUEGO, Tipo.FUEGO, 0.5);
        set(Tipo.FUEGO, Tipo.AGUA, 0.5);
        set(Tipo.FUEGO, Tipo.ROCA, 0.5);
        set(Tipo.FUEGO, Tipo.DRAGON, 0.5);

        // ======================
        // AGUA
        // ======================
        set(Tipo.AGUA, Tipo.FUEGO, 2.0);
        set(Tipo.AGUA, Tipo.TIERRA, 2.0);
        set(Tipo.AGUA, Tipo.ROCA, 2.0);
        set(Tipo.AGUA, Tipo.AGUA, 0.5);
        set(Tipo.AGUA, Tipo.PLANTA, 0.5);
        set(Tipo.AGUA, Tipo.DRAGON, 0.5);

        // ======================
        // PLANTA
        // ======================
        set(Tipo.PLANTA, Tipo.AGUA, 2.0);
        set(Tipo.PLANTA, Tipo.TIERRA, 2.0);
        set(Tipo.PLANTA, Tipo.ROCA, 2.0);
        set(Tipo.PLANTA, Tipo.FUEGO, 0.5);
        set(Tipo.PLANTA, Tipo.PLANTA, 0.5);
        set(Tipo.PLANTA, Tipo.VENENO, 0.5);
        set(Tipo.PLANTA, Tipo.VOLADOR, 0.5);
        set(Tipo.PLANTA, Tipo.BICHO, 0.5);
        set(Tipo.PLANTA, Tipo.DRAGON, 0.5);
        set(Tipo.PLANTA, Tipo.ACERO, 0.5);

        // ======================
        // ELECTRICO
        // ======================
        set(Tipo.ELECTRICO, Tipo.AGUA, 2.0);
        set(Tipo.ELECTRICO, Tipo.VOLADOR, 2.0);
        set(Tipo.ELECTRICO, Tipo.ELECTRICO, 0.5);
        set(Tipo.ELECTRICO, Tipo.PLANTA, 0.5);
        set(Tipo.ELECTRICO, Tipo.DRAGON, 0.5);
        set(Tipo.ELECTRICO, Tipo.TIERRA, 0.0); // Inmunidad

        // ======================
        // TIERRA
        // ======================
        set(Tipo.TIERRA, Tipo.FUEGO, 2.0);
        set(Tipo.TIERRA, Tipo.ELECTRICO, 2.0);
        set(Tipo.TIERRA, Tipo.VENENO, 2.0);
        set(Tipo.TIERRA, Tipo.ROCA, 2.0);
        set(Tipo.TIERRA, Tipo.ACERO, 2.0);
        set(Tipo.TIERRA, Tipo.PLANTA, 0.5);
        set(Tipo.TIERRA, Tipo.BICHO, 0.5);
        set(Tipo.TIERRA, Tipo.VOLADOR, 0.0); // Inmunidad

        // ======================
        // NORMAL
        // ======================
        set(Tipo.NORMAL, Tipo.ROCA, 0.5);
        set(Tipo.NORMAL, Tipo.ACERO, 0.5);
        set(Tipo.NORMAL, Tipo.FANTASMA, 0.0); // Inmunidad

        // ======================
        // LUCHA
        // ======================
        set(Tipo.LUCHA, Tipo.NORMAL, 2.0);
        set(Tipo.LUCHA, Tipo.HIELO, 2.0);
        set(Tipo.LUCHA, Tipo.ROCA, 2.0);
        set(Tipo.LUCHA, Tipo.SINIESTRO, 2.0);
        set(Tipo.LUCHA, Tipo.ACERO, 2.0);
        set(Tipo.LUCHA, Tipo.VENENO, 0.5);
        set(Tipo.LUCHA, Tipo.VOLADOR, 0.5);
        set(Tipo.LUCHA, Tipo.PSIQUICO, 0.5);
        set(Tipo.LUCHA, Tipo.BICHO, 0.5);
        set(Tipo.LUCHA, Tipo.HADA, 0.5);
        set(Tipo.LUCHA, Tipo.FANTASMA, 0.0); // Inmunidad

        // ======================
        // VOLADOR
        // ======================
        set(Tipo.VOLADOR, Tipo.PLANTA, 2.0);
        set(Tipo.VOLADOR, Tipo.LUCHA, 2.0);
        set(Tipo.VOLADOR, Tipo.BICHO, 2.0);
        set(Tipo.VOLADOR, Tipo.ELECTRICO, 0.5);
        set(Tipo.VOLADOR, Tipo.ROCA, 0.5);
        set(Tipo.VOLADOR, Tipo.ACERO, 0.5);

        // ======================
        // VENENO
        // ======================
        set(Tipo.VENENO, Tipo.PLANTA, 2.0);
        set(Tipo.VENENO, Tipo.HADA, 2.0);
        set(Tipo.VENENO, Tipo.VENENO, 0.5);
        set(Tipo.VENENO, Tipo.TIERRA, 0.5);
        set(Tipo.VENENO, Tipo.ROCA, 0.5);
        set(Tipo.VENENO, Tipo.FANTASMA, 0.5);
        set(Tipo.VENENO, Tipo.ACERO, 0.0); // Inmunidad

        // ======================
        // PSIQUICO
        // ======================
        set(Tipo.PSIQUICO, Tipo.LUCHA, 2.0);
        set(Tipo.PSIQUICO, Tipo.VENENO, 2.0);
        set(Tipo.PSIQUICO, Tipo.PSIQUICO, 0.5);
        set(Tipo.PSIQUICO, Tipo.ACERO, 0.5);
        set(Tipo.PSIQUICO, Tipo.SINIESTRO, 0.0); // Inmunidad

        // ======================
        // BICHO
        // ======================
        set(Tipo.BICHO, Tipo.PLANTA, 2.0);
        set(Tipo.BICHO, Tipo.PSIQUICO, 2.0);
        set(Tipo.BICHO, Tipo.SINIESTRO, 2.0);
        set(Tipo.BICHO, Tipo.FUEGO, 0.5);
        set(Tipo.BICHO, Tipo.LUCHA, 0.5);
        set(Tipo.BICHO, Tipo.VOLADOR, 0.5);
        set(Tipo.BICHO, Tipo.VENENO, 0.5);
        set(Tipo.BICHO, Tipo.FANTASMA, 0.5);
        set(Tipo.BICHO, Tipo.ACERO, 0.5);
        set(Tipo.BICHO, Tipo.HADA, 0.5);

        // ======================
        // ROCA
        // ======================
        set(Tipo.ROCA, Tipo.FUEGO, 2.0);
        set(Tipo.ROCA, Tipo.HIELO, 2.0);
        set(Tipo.ROCA, Tipo.VOLADOR, 2.0);
        set(Tipo.ROCA, Tipo.BICHO, 2.0);
        set(Tipo.ROCA, Tipo.LUCHA, 0.5);
        set(Tipo.ROCA, Tipo.TIERRA, 0.5);
        set(Tipo.ROCA, Tipo.ACERO, 0.5);

        // ======================
        // FANTASMA
        // ======================
        set(Tipo.FANTASMA, Tipo.PSIQUICO, 2.0);
        set(Tipo.FANTASMA, Tipo.FANTASMA, 2.0);
        set(Tipo.FANTASMA, Tipo.SINIESTRO, 0.5);
        set(Tipo.FANTASMA, Tipo.NORMAL, 0.0); // Inmunidad

        // ======================
        // DRAGON
        // ======================
        set(Tipo.DRAGON, Tipo.DRAGON, 2.0);
        set(Tipo.DRAGON, Tipo.ACERO, 0.5);
        set(Tipo.DRAGON, Tipo.HADA, 0.0); // Inmunidad

        // ======================
        // SINIESTRO
        // ======================
        set(Tipo.SINIESTRO, Tipo.PSIQUICO, 2.0);
        set(Tipo.SINIESTRO, Tipo.FANTASMA, 2.0);
        set(Tipo.SINIESTRO, Tipo.LUCHA, 0.5);
        set(Tipo.SINIESTRO, Tipo.SINIESTRO, 0.5);
        set(Tipo.SINIESTRO, Tipo.HADA, 0.5);

        // ======================
        // ACERO
        // ======================
        set(Tipo.ACERO, Tipo.HIELO, 2.0);
        set(Tipo.ACERO, Tipo.ROCA, 2.0);
        set(Tipo.ACERO, Tipo.HADA, 2.0);
        set(Tipo.ACERO, Tipo.FUEGO, 0.5);
        set(Tipo.ACERO, Tipo.AGUA, 0.5);
        set(Tipo.ACERO, Tipo.ELECTRICO, 0.5);
        set(Tipo.ACERO, Tipo.ACERO, 0.5);

        // ======================
        // HIELO
        // ======================
        set(Tipo.HIELO, Tipo.PLANTA, 2.0);
        set(Tipo.HIELO, Tipo.TIERRA, 2.0);
        set(Tipo.HIELO, Tipo.VOLADOR, 2.0);
        set(Tipo.HIELO, Tipo.DRAGON, 2.0);
        set(Tipo.HIELO, Tipo.FUEGO, 0.5);
        set(Tipo.HIELO, Tipo.AGUA, 0.5);
        set(Tipo.HIELO, Tipo.HIELO, 0.5);
        set(Tipo.HIELO, Tipo.ACERO, 0.5);

        // ======================
        // HADA
        // ======================
        set(Tipo.HADA, Tipo.LUCHA, 2.0);
        set(Tipo.HADA, Tipo.DRAGON, 2.0);
        set(Tipo.HADA, Tipo.SINIESTRO, 2.0);
        set(Tipo.HADA, Tipo.FUEGO, 0.5);
        set(Tipo.HADA, Tipo.VENENO, 0.5);
        set(Tipo.HADA, Tipo.ACERO, 0.5);
    }

    /**
     * Inserta la relación en el mapa auxiliar de efectividades.
     */
    private static void set(Tipo atacante, Tipo defensor, double valor) {
        tabla.get(atacante).put(defensor, valor);
    }

    /**
     * Obtiene el multiplicador de daño entre un tipo atacante y un único tipo defensor.
     * Si la relación no está explícitamente guardada, significa que el daño es neutro (1.0).
     */
    public static double obtenerMultiplicador(Tipo atacante, Tipo defensor) {
        if (atacante == null || defensor == null) {
            return 1.0;
        }

        // Busca el mapa del atacante, y si existe, busca el valor del defensor.
        // Si no encuentra registro del defensor, por defecto devuelve daño neutro (1.0).
        return tabla
                .getOrDefault(atacante, new HashMap<>())
                .getOrDefault(defensor, 1.0);
    }
}