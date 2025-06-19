package system;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Grafo {
	private HashSet<Arista> _aristas;
	private int _vertices;
	
	public Grafo(int vert) {
		_vertices = vert;
		_aristas = new HashSet<Arista>();
	}
	
	public void añadirArista(int x, int y, int impact) {
		if (impact > 10 || impact < 1)
			throw new IllegalArgumentException("Peso invalido (fuera de rango 1 a 10)");
		if (x >= _vertices || y >= _vertices)
			_vertices = Math.max(x, y)+1;
		Arista aristaTemp = new Arista(x,y,impact);
		if (!existeArista(aristaTemp)) {
			_aristas.add(aristaTemp);
		}
	}
	
	public void eliminarArista(Arista a) {
		if (_aristas.contains(a))
			_aristas.remove(a);
	}
	
	public boolean existeArista(Arista a) {
		if (_aristas.contains(a))
			return true;
		return false;
	}

	public boolean conexo() {
		LinkedList<Integer> temp = new LinkedList<Integer>(); 
		for (Arista nodo:_aristas) {
			if (!temp.contains(nodo.getOrigen()))
				temp.add(nodo.getOrigen());
			if(!temp.contains(nodo.getDestino()))
				temp.add(nodo.getDestino());
		}
		if (temp.size() == _vertices)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public HashSet<Arista> getAristas() {
		return (HashSet<Arista>) _aristas.clone();
	}

	public HashSet<Arista> getVecinos(int x) {
		HashSet<Arista> vecinos = new HashSet<Arista>(); 
		for (Arista a:_aristas) {
			if (a.getOrigen() == x)
				vecinos.add(a);
		}
		return vecinos;
	}
	
	public int getVertices() {
		return _vertices;
	}
}
