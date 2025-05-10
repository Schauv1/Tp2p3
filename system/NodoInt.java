package system;

import java.awt.Point;
import java.util.LinkedList;

public class NodoInt {
	Point nodo;
	int impactoTotal, vertices, tamaño;
	LinkedList<NodoInt> nodosCercanos;
	NodoInt origen;
	
	public NodoInt(Grafo graph) {
		if (graph.aristas.get(0).size() == 0)
			throw new IllegalArgumentException("El grafo no es conexo");
		nodosCercanos = new LinkedList<NodoInt>();
		tamaño = 1;
		LinkedList<Integer> temp = new LinkedList<Integer>();
		int min = 11;
		int minPos = 0;
		for (int i = 0; i < graph.aristas.get(0).size(); i++) {
			temp.add(graph.aristas.get(0).get(i).peso);
		}
		for (int i = 0; i < temp.size(); i++) 
			if (temp.get(i) < min) {
				min = temp.get(i);
				minPos = i;
			}
		nodo = new Point(0, graph.aristas.get(0).get(minPos).destino);
		impactoTotal += graph.aristas.get(0).get(minPos).peso;
		origen = this;
	}
	public NodoInt(int x, int y, int tamaño, NodoInt origen) {
		nodo = new Point(x, y);
		nodosCercanos = new LinkedList<NodoInt>();
		this.tamaño = tamaño;
		this.origen = origen;
	}
	
	public void addNodo(int x, int y) {
		tamaño ++;
		nodosCercanos.add(new NodoInt(x, y, tamaño, origen));
	}
	public void deleteNodo(int x, int y) {
		int nodeNumber = 0;
		for (NodoInt n:nodosCercanos) {
			nodeNumber ++;
			if (n.nodo.x == x && n.nodo.y == y)
				nodosCercanos.remove(nodeNumber);
		}
		tamaño --;
	}
	
	public LinkedList<NodoInt> getCloseBy() {
		return nodosCercanos;
	}
	
	public boolean existe(int x, int y, NodoInt siguiente) {
		if (nodo.x == x && nodo.y == y)
			return true;
		if (siguiente.nodo.x == x && siguiente.nodo.y == y)
			return true;
		if (siguiente.nodo == null)
			return false;

		for(int i = 0 ; i < siguiente.nodosCercanos.size() ; i++) {
			if (siguiente.nodosCercanos.get(i).nodo.x == x && siguiente.nodosCercanos.get(i).nodo.y == y)
				return true;
			else
				if (siguiente.nodosCercanos.get(i).existe(x, y, siguiente.nodosCercanos.get(i)))
					return true;
		}
		if (siguiente.nodo.x == y && siguiente.nodo.y == x)
			return true;
		return false;
	}
	
	public void primBuild(Grafo graph) {
		if (graph == null)
			return;
		if (tamaño >= graph.vertices-1)
			return;
		int piso = 1;
		LinkedList<Integer> temp = new LinkedList<Integer>();
		LinkedList<Integer> temp2 = new LinkedList<Integer>();
		int min = 11; //minimo por encima del peso posible para encontrar uno al primer nodo posible
		int minPos = 0;
		for (int i = 0; i < graph.aristas.get(piso).size(); i++) {
			if (!existe(piso, graph.aristas.get(piso).get(i).destino, origen)) {
				temp.add(graph.aristas.get(piso).get(i).peso);
				temp2.add(i);
			}
		}
		for (int i = 0; i < temp.size(); i++) 
			if (temp.get(i) < min) {
				min = temp.get(i);
				minPos = i;
			}
		if (!temp.isEmpty()) {
			addNodo(piso, graph.aristas.get(piso).get(temp2.get(minPos)).destino);
			origen.impactoTotal += graph.aristas.get(piso).get(temp2.get(minPos)).peso;
		}
		for (int i = 0; i < nodosCercanos.size(); i++)
			nodosCercanos.get(i).primBuild(graph, piso +1);
	}
	
	private void primBuild(Grafo graph, int piso) {
		if (graph == null)
			return;
		if (tamaño >= graph.vertices-1)
			return;
		if (piso >= graph.vertices-1)
			return;
		LinkedList<Integer> temp = new LinkedList<Integer>();
		LinkedList<Integer> temp2 = new LinkedList<Integer>();
		int min = 11; //minimo por encima del peso posible para encontrar uno al primer nodo posible
		int minPos = 0;
		for (int i = 0; i < graph.aristas.get(piso).size(); i++) {
			if (!existe(piso, graph.aristas.get(piso).get(i).destino, origen)) {
				temp.add(graph.aristas.get(piso).get(i).peso);
				temp2.add(i);
				System.out.println("[" +piso + "," + graph.aristas.get(piso).get(i).destino + "]");
			}	
		}
		for (int i = 0; i < temp.size(); i++) 
			if (temp.get(i) < min) {
				min = temp.get(i);
				minPos = i;
			}
		if (!temp.isEmpty()) {
			addNodo(piso, graph.aristas.get(piso).get(temp2.get(minPos)).destino);
			origen.impactoTotal += graph.aristas.get(piso).get(temp2.get(minPos)).peso;
		}
		for (int i = 0; i < nodosCercanos.size(); i++)
			nodosCercanos.get(i).primBuild(graph, piso +1);
	}
	
	public void mostrar() {
		System.out.println(""+ nodo.x + " " + nodo.y);
		for (NodoInt n: nodosCercanos) {
			n.mostrar();
		}
	}
}
