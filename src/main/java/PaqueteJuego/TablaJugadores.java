package PaqueteJuego;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TablaJugadores {
	private Jugador[] tabla;
	private int n;  // cantidad de jugadores en la tabla
	private int max;  // capacidad de la tabla
		
	public TablaJugadores(String nombreFich){
		File fichero = new File(nombreFich);
		
		n=0;
		max=20;
		tabla = new Jugador[max];

		try{
			FileReader flujoLectura = new FileReader(fichero);
			BufferedReader flujoBuffer = new BufferedReader(flujoLectura);
			String linea;
			String datos[];
			Jugador e = null;
			while ((linea = flujoBuffer.readLine()) != null){
				datos = linea.split(" ");
				e = new Jugador(datos[0],Boolean.parseBoolean(datos[1]),datos[2],Integer.parseInt(datos[3]));
				if (n < max){
					tabla[n] = e;
					n++;
				}				
			}
			flujoBuffer.close();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}			
	}
	
	public int obtenerNumJugadores(){
		return n;
	}
	
	public String obtenerEquipo(int iJ){
		return tabla[iJ].obtenerEquipo();
	}
	
	public boolean obtenerEsPre(int iJ){
		return tabla[iJ].obtenerEsPre();
	}
	
	public int obtenerNivel(int iJ){
		return tabla[iJ].obtenerNivel();
	}
	
	public void actualizarNivel(int iJ, int inc){
		tabla[iJ].actualizarNivel(inc);
	}
	
	public void mostrarJugadores(){
		for (int i = 0; i<n; i++)
			tabla[i].mostrarJugador();
	}
}
