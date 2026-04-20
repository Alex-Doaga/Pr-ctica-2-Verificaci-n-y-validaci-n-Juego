package PaqueteJuego;

import java.net.URLDecoder;

public class pruebaNiveles {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String fJ = "jugadores.txt";
		String resourcePathFJ = URLDecoder.decode(pruebaNiveles.class.getResource("/"+fJ).getPath());
		TablaJugadores tJ = new TablaJugadores(resourcePathFJ);
		System.out.println("Tabla de jugadores ANTES DE actualizar niveles...");
		tJ.mostrarJugadores();
		System.out.println();

		String fE = "equipos.txt";
		String resourcePathFE = URLDecoder.decode(pruebaNiveles.class.getResource("/"+fE).getPath());
		TablaEquipos tE = new TablaEquipos(resourcePathFE);
		System.out.println("Tabla de equipos...");
		tE.mostrarEquipos();
		System.out.println();

		int incrementoEspecial = 3;
		int umbralNivel = 50;
		System.out.println("Incremento especial = " + incrementoEspecial);
		System.out.println("Umbral nivel = " + umbralNivel);
		System.out.println();

		Niveles l = new Niveles(incrementoEspecial, umbralNivel);
		int codErr = l.actualizarNiveles(tJ, tE);
		System.out.println("Resultados...");
		System.out.println("Valor devuelto = " + codErr);
		System.out.println("Tabla de jugadores TRAS actualizar niveles...");
		tJ.mostrarJugadores();
		System.out.println();

	}

}
