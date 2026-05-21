package Controller;

import Model.Service.MazoService;
import Model.Entities.Carta;
import Model.Entities.Jugador;
import Model.Exceptions.MazoException;
import View.MenuMazos.VentanaGestionMazo;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MazoController {

    private final MazoService mazoService;
    private final Jugador jugador;

    // Listas en memoria para gestionar los cambios antes de confirmar con la BD
    private List<Carta> inventarioCompleto;
    private final List<Carta> mazoTemporal;

    public MazoController(Jugador jugador) {
        this.jugador = jugador;
        this.mazoService = new MazoService();
        this.mazoTemporal = new ArrayList<>();

        // Inicializamos los datos del jugador desde la base de datos
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        this.inventarioCompleto = mazoService.obtenerInventario(jugador.getId());
        try {
            // Intentamos cargar su mazo activo actual
            this.mazoTemporal.addAll(mazoService.cargarMazoJugador(jugador.getId()));
        } catch (MazoException e) {
            System.out.println("ℹ️ El jugador no tiene un mazo de 5 cartas válido configurado. Se iniciará vacío.");
        }
    }

    /**
     * 🔍 FILTRADOR POR TIPO: Filtra el inventario según el tipo seleccionado.
     * Si se selecciona "TODOS", devuelve la lista completa sin restricciones.
     */
    public List<Carta> filtrarInventarioPorTipo(String tipoSeleccionado) {
        if (tipoSeleccionado.equalsIgnoreCase("TODOS") || tipoSeleccionado.isEmpty()) {
            return inventarioCompleto;
        }
        return inventarioCompleto.stream()
                .filter(carta -> carta.getTipo().toString().equalsIgnoreCase(tipoSeleccionado))
                .collect(Collectors.toList());
    }

    /**
     * ➕ Agrega una carta del inventario al mazo temporal si cumple las reglas básicas.
     */
    public boolean agregarCartaAlMazo(Carta carta) {
        if (mazoTemporal.size() >= 5) {
            return false; // El mazo ya está completo
        }
        // Evitamos que agregue exactamente la misma carta física dos veces
        if (mazoTemporal.stream().anyMatch(c -> c.getId() == carta.getId())) {
            return false;
        }
        mazoTemporal.add(carta);
        return true;
    }

    /**
     * ➖ Remueve una carta seleccionada del mazo temporal.
     */
    public void removerCartaDelMazo(Carta carta) {
        mazoTemporal.removeIf(c -> c.getId() == carta.getId());
    }

    /**
     * 💾 Guarda de forma definitiva las 5 cartas seleccionadas en la base de datos.
     */
    public boolean guardarMazoDefinitivo() {
        if (mazoTemporal.size() != 5) {
            return false; // Regla estricta del juego: el mazo debe ser de exactamente 5 cartas
        }

        // Mapeamos la lista de objetos 'Carta' a una lista de puros enteros 'ID'
        List<Integer> idsElegidos = mazoTemporal.stream()
                .map(Carta::getId)
                .collect(Collectors.toList());

        return mazoService.modificarMazo(jugador.getId(), idsElegidos);
    }

    // Getters indispensables para la UI
    public List<Carta> getMazoTemporal() { return mazoTemporal; }
    public Jugador getJugador() { return jugador; }
}