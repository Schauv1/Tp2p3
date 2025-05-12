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
		aristas.get(x).add(new Arista(y, impact));
		aristas.get(y).add(new Arista(x, impact));
	}
	
	public void eliminarArista(int x, int y) {
		aristas.remove(x);
		aristas.remove(y);
	}
	
	public boolean existeArista(int x, int y) {
		if (aristas.get(x) != null)
			return true;
		else
			return false;
	}
	
	public void print() {
		for(int i = 0; i < aristas.size() ; i++) {
			for(Arista a:aristas.get(i)) {
				System.out.println("["+ i +" "+a.destino+"]");
			}
		}
	}

}
