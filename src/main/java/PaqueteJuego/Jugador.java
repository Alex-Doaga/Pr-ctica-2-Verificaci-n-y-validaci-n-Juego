package PaqueteJuego;

public class Jugador {
	private String nombre;
	private boolean esPre;
	private String equipo;
	private int nivel;
	
	public Jugador(String n, boolean eP, String e, int l){
		nombre = n;
		esPre = eP;
		equipo = e;
		nivel = l;
	}
	
	public boolean obtenerEsPre(){
		return esPre;
	}

	public String obtenerEquipo(){
		return equipo;
	}

	public int obtenerNivel(){
		return nivel;
	}
	
	public void actualizarNivel(int inc){
		nivel += inc;
	}
	
	public void mostrarJugador(){
		System.out.println(nombre+" "+ esPre +" "+ equipo +" "+ nivel);
	}
}

