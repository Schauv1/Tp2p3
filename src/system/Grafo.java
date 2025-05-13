package system;

import java.util.LinkedList;

public class Grafo {
	int vertices;
	LinkedList<LinkedList<Arista>> aristas;
	
	public Grafo(int vert) {
		vertices = vert;
		aristas = new LinkedList<LinkedList<Arista>>();
		for (int i = 0 ; i < vertices ; i++) {
			aristas.add(new LinkedList<Arista>());
		}
	}
	
	public void añadirArista(int x, int y, int impact) {
		if (impact > 10 || impact < 1)
			throw new IllegalArgumentException("Peso invalido (fuera de rango 1 a 10)");
		if (!existeArista(x,y)) {
			aristas.get(x).add(new Arista(y, impact));
			aristas.get(y).add(new Arista(x, impact));
		}
	}
	
	public void eliminarArista(int x, int y) {
		if (aristas.get(x) != null)
			for(int i = 0; i< aristas.get(x).size(); i++)
				if (aristas.get(x).get(i).destino == y)
					aristas.get(x).remove(i);
		if (aristas.get(y) != null)
			for(int i = 0; i< aristas.get(y).size(); i++)
				if (aristas.get(y).get(i).destino == y)
					aristas.get(y).remove(i);
	}
	
	public boolean existeArista(int x, int y) {
		if (aristas.get(x) != null)
			for(Arista a:aristas.get(x))
				if (a.destino == y)
					return true;
		return false;
	}
	public void extenderGrafo( ) {
		aristas.add(new LinkedList<Arista>());
	}
	
	public void print() {
		for(int i = 0; i < aristas.size() ; i++) {
			for(Arista a:aristas.get(i)) {
				System.out.println("["+ i +" "+a.destino+"]");
			}
		}
	}
	
	public int getPeso(int x, int y) {
		for (Arista a:aristas.get(x))
				if (a.destino == y)
					return a.peso;
		return 0;
	}
}
