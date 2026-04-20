package PaqueteJuego;

public class Equipo {
	private String nombre;
	private int puntos;
	
	public Equipo(String n, int p){
		nombre = n;
		puntos = p;
	}

	public String obtenerNombre(){
		return nombre;
	}
	
	public int obtenerPuntos(){
		return puntos;
	}
	
	public void mostrarEquipo(){
		System.out.println(nombre+" "+ puntos);
	}	
}
