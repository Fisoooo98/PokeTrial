package Test;

import Model.Entities.Carta;
import Model.Service.MazoService;
import Model.Exceptions.MazoException; // Asegúrate de apuntar a tu paquete de excepciones

import java.util.ArrayList;
import java.util.List;

public class PruebaMazoService {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TEST DE GESTIÓN DE MAZOS ===");

        MazoService mazoService = new MazoService();
        int idJugadorTest = 2; // El ID de "Fisooo" en tu BD

        // =================================================================
        // ESCENARIO 1: MODIFICAR EL MAZO (Asignar 5 cartas)
        // =================================================================
        System.out.println("\n🔄 1. Intentando registrar un mazo de 5 cartas en la BD...");

        // Simula 5 IDs de registros existentes en tu tabla 'inventarios'
        List<Integer> idsInventarioElegidas = new ArrayList<>();
        idsInventarioElegidas.add(18);
        idsInventarioElegidas.add(19);
        idsInventarioElegidas.add(20);
        idsInventarioElegidas.add(21);
        idsInventarioElegidas.add(22);

        boolean modificadoOk = mazoService.modificarMazo(idJugadorTest, idsInventarioElegidas);

        if (modificadoOk) {
            System.out.println("✅ ÉXITO: El mazo se procesó y actualizó en la base de datos.");
        } else {
            System.out.println("❌ ERROR: El mazo fue rechazado por el servicio.");
        }

        // =================================================================
        // ESCENARIO 2: CARGAR Y VALIDAR EL MAZO DE LA BD
        // =================================================================
        System.out.println("\n📂 2. Intentando recuperar el mazo recién guardado...");
        try {
            List<Carta> mazoCargado = mazoService.cargarMazoJugador(idJugadorTest);

            System.out.println("✅ ÉXITO: Mazo cargado correctamente desde PostgreSQL.");
            System.out.println("Cantidad de cartas en la mano: " + mazoCargado.size() + " (Esperado: 5)");

            // Listamos los Pokémon cargados en el mazo para verificar el mapeo
            for (Carta carta : mazoCargado) {
                System.out.println("   - [" + carta.getRareza() + "] " + carta.getPokemon().getNombre());
            }

        } catch (MazoException e) {
            System.out.println("❌ ERROR: Saltó MazoException. El mazo recuperado no tenía exactamente 5 cartas.");
            System.out.println("Mensaje de la excepción: " + e.getMessage());
        }

        // =================================================================
        // ESCENARIO 3: CONTROL DE ERRORES (Mazo con tamaño incorrecto)
        // =================================================================
        System.out.println("\n⚠️ 3. Intentando enviar un mazo inválido (solo 3 cartas)...");
        List<Integer> mazoIncompleto = java.util.List.of(1, 2, 3);

        boolean mazoInvalidoRechazado = !mazoService.modificarMazo(idJugadorTest, mazoIncompleto);
        if (mazoInvalidoRechazado) {
            System.out.println("✅ ÉXITO: El sistema rechazó correctamente el mazo incompleto.");
        } else {
            System.out.println("❌ ERROR: El sistema permitió guardar un mazo de menos de 5 cartas.");
        }

        System.out.println("\n=== FIN DEL TEST DE MAZOS ===");
    }
}