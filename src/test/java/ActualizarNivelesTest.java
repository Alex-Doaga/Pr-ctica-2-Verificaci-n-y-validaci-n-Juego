import PaqueteJuego.Niveles;
import PaqueteJuego.TablaEquipos;
import PaqueteJuego.TablaJugadores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba experta para el método actualizarNiveles de la clase Niveles.
 * Utiliza generación de ficheros temporales para no alterar el código operativo.
 */
class ActualizarNivelesTest {

    private Niveles niveles;
    private final int UMBRAL = 50;
    private final int INCREMENTO = 5;

    @BeforeEach
    void setUp() {
        // Inicializamos Niveles con el incremento y el umbral
        niveles = new Niveles(INCREMENTO, UMBRAL);
    }

    // ========================================================================
    // MÉTODOS AUXILIARES: Mocking de la entrada de archivos
    // ========================================================================

    /**
     * Crea un archivo txt temporal con las líneas de jugadores indicadas.
     * Formato: "nombre esPre equipo nivel"
     */
    private TablaJugadores crearFicheroJugadores(String... lineas) throws IOException {
        File f = File.createTempFile("jugadores_test", ".txt");
        f.deleteOnExit(); // Se borra automáticamente al terminar la prueba
        try (FileWriter fw = new FileWriter(f)) {
            for (String linea : lineas) {
                fw.write(linea + "\n");
            }
        }
        return new TablaJugadores(f.getAbsolutePath());
    }

    /**
     * Crea un archivo txt temporal con las líneas de equipos indicadas.
     * Formato: "nombre puntos"
     */
    private TablaEquipos crearFicheroEquipos(String... lineas) throws IOException {
        File f = File.createTempFile("equipos_test", ".txt");
        f.deleteOnExit(); // Se borra automáticamente al terminar la prueba
        try (FileWriter fw = new FileWriter(f)) {
            for (String linea : lineas) {
                fw.write(linea + "\n");
            }
        }
        return new TablaEquipos(f.getAbsolutePath());
    }

    // ========================================================================
    // BATERÍA DE CASOS DE PRUEBA (Unificación CB, CN y VF)
    // ========================================================================

    @Test
    @DisplayName("CP1: Tablas vacías (Frontera e Inválido - 1T)")
    void testCP1_TablasVacias() throws IOException {
        TablaJugadores tJ = crearFicheroJugadores(); // Archivo vacío
        TablaEquipos tE = crearFicheroEquipos("A 100");

        assertEquals(1, niveles.actualizarNiveles(tJ, tE), "Debe devolver error 1 si tJ está vacía");
    }

    @Test
    @DisplayName("CP2: Tabla de jugadores nula (CI2)")
    void testCP2_JugadoresNull() throws IOException {
        TablaEquipos tE = crearFicheroEquipos("A 100");

        assertThrows(NullPointerException.class, () -> {
            niveles.actualizarNiveles(null, tE);
        }, "Debe lanzar NullPointerException si la tabla de jugadores no está instanciada");
    }

    @Test
    @DisplayName("CP3: Tabla de equipos vacía (Circuito corto - 1FT)")
    void testCP3_EquiposVacia() throws IOException {
        TablaJugadores tJ = crearFicheroJugadores("Carlos false A 40");
        TablaEquipos tE = crearFicheroEquipos(); // Archivo vacío

        assertEquals(1, niveles.actualizarNiveles(tJ, tE), "Debe devolver error 1 si tE está vacía");
    }

    @Test
    @DisplayName("CP4: Tabla de equipos nula (CI4)")
    void testCP4_EquiposNull() throws IOException {
        TablaJugadores tJ = crearFicheroJugadores("Carlos false A 40");

        assertThrows(NullPointerException.class, () -> {
            niveles.actualizarNiveles(tJ, null);
        }, "Debe lanzar NullPointerException si la tabla de equipos no está instanciada");
    }

    @Test
    @DisplayName("CP5: Caso Maestro (Cobertura de Caja Blanca - Múltiples empates y umbrales)")
    void testCP5_CasoMaestro() throws IOException {
        TablaJugadores tJ = crearFicheroJugadores(
                "Carlos false A 40", // Sube 1 nivel
                "Laura true A 60",   // Sube 1 nivel (es pre pero supera umbral)
                "Carla false X 10"   // No sube (equipo no ganador)
        );
        TablaEquipos tE = crearFicheroEquipos(
                "A 100",
                "B 50",
                "C 100" // C empata como mejor equipo, pero no tiene jugadores
        );

        int resultado = niveles.actualizarNiveles(tJ, tE);

        assertEquals(2, resultado, "Debe devolver 2 porque el mejor equipo (C) no tiene jugadores");
        assertEquals(41, tJ.obtenerNivel(0));
        assertEquals(61, tJ.obtenerNivel(1));
        assertEquals(10, tJ.obtenerNivel(2));
    }

    @Test
    @DisplayName("CP6: Caso válido simple (Caja Negra)")
    void testCP6_CasoValido() throws IOException {
        TablaJugadores tJ = crearFicheroJugadores("Gil false A 40");
        TablaEquipos tE = crearFicheroEquipos("A 100");

        assertEquals(0, niveles.actualizarNiveles(tJ, tE));
        assertEquals(41, tJ.obtenerNivel(0));
    }

    @Test
    @DisplayName("CP7: Dos equipos empatados, 1 jugador premium bajo umbral (CN)")
    void testCP7_DosEquiposEmpatados() throws IOException {
        TablaJugadores tJ = crearFicheroJugadores(
                "Jose false A 100", // no-premium, sobre umbral -> sube 1
                "Diego true B 20"   // premium, bajo umbral -> sube incrementoEspecial (5)
        );
        TablaEquipos tE = crearFicheroEquipos(
                "A 50",
                "B 50" // A y B empatan como mejores equipos
        );

        assertEquals(0, niveles.actualizarNiveles(tJ, tE));
        assertEquals(101, tJ.obtenerNivel(0)); // Jose sube 1
        assertEquals(25, tJ.obtenerNivel(1));  // Diego sube 5 (incrementoEspecial)
    }

    @Test
    @DisplayName("CP8: Mejor equipo sin jugadores (CI5)")
    void testCP8_MejorEquipoSinJugadores() throws IOException {
        TablaJugadores tJ = crearFicheroJugadores("P1 false A 40");
        TablaEquipos tE = crearFicheroEquipos("A 50", "B 100"); // B gana pero no tiene jugadores

        assertEquals(2, niveles.actualizarNiveles(tJ, tE));
    }

    @Test
    @DisplayName("CP9: Puntuaciones negativas (Límites sin control - CI6)")
    void testCP9_PuntosNegativos() throws IOException {
        TablaJugadores tJ = crearFicheroJugadores("P1 false A 10");
        TablaEquipos tE = crearFicheroEquipos("A -10");

        // Puesto que el código no lanza excepciones por valores ilógicos, esperamos un 0
        assertEquals(0, niveles.actualizarNiveles(tJ, tE), "El sistema procesa puntuaciones negativas con normalidad");
    }

    @Test
    @DisplayName("CP10: Límite superior de TablaJugadores (Frontera - VF3)")
    void testCP10_FronteraJugadores() throws IOException {
        String[] lineasJugadores = new String[20];
        for (int i = 0; i < 20; i++) {
            lineasJugadores[i] = "J" + i + " false A 10";
        }
        TablaJugadores tJ = crearFicheroJugadores(lineasJugadores);
        TablaEquipos tE = crearFicheroEquipos("A 100");

        assertEquals(0, niveles.actualizarNiveles(tJ, tE));
    }

    @Test
    @DisplayName("CP11: Límite superior de TablaEquipos (Frontera - VF6)")
    void testCP11_FronteraEquipos() throws IOException {
        String[] lineasEquipos = new String[10];
        for (int i = 0; i < 10; i++) {
            lineasEquipos[i] = "E" + i + " " + (10 + i); // E9 gana con 19 puntos
        }
        TablaJugadores tJ = crearFicheroJugadores("J1 false E9 10");
        TablaEquipos tE = crearFicheroEquipos(lineasEquipos);

        assertEquals(0, niveles.actualizarNiveles(tJ, tE));
    }
}