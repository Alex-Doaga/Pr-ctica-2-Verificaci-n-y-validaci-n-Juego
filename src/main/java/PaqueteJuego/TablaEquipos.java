package PaqueteJuego;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TablaEquipos {
	private Equipo[] tabla;
	private int n;  // cantidad de equipos en la tabla
	private int max;  // capacidad de la tabla
		
	public TablaEquipos(String nombreFich){
		File fichero = new File(nombreFich);
		
		n=0;
		max=10;
		tabla = new Equipo[max];

		try{
			FileReader flujoLectura = new FileReader(fichero);
			BufferedReader flujoBuffer = new BufferedReader(flujoLectura);
			String linea;
			String datos[];
			Equipo d = null;
			while ((linea = flujoBuffer.readLine()) != null){
				datos = linea.split(" ");
				d = new Equipo(datos[0],Integer.parseInt(datos[1]));
				if (n < max){
					tabla[n] = d;
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

	public int obtenerNumEquipos(){
		return n;
	}
	
	public int obtenerPuntos(int iE){
		return tabla[iE].obtenerPuntos();
	}
	
	public String obtenerNombre(int iE){
		return tabla[iE].obtenerNombre();
	}
	
	public void mostrarEquipos(){
		for (int i = 0; i<n; i++)
			tabla[i].mostrarEquipo();
	}

}
