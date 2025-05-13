package system;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class NodoIntTest {
	LinkedList<NodoInt> nodosCercanosTest = new LinkedList<NodoInt>();
	Grafo grafo = new Grafo(5);
	

	@Before
	public void setup() {
		
		
	}
	
	@Test
	public void primTest() {
		grafo.añadirArista(0, 1, 5);
		grafo.añadirArista(1, 4, 2);
		grafo.añadirArista(1, 2, 1);
		grafo.añadirArista(1, 3, 8);
		grafo.añadirArista(3, 4, 2);
		grafo.añadirArista(0, 2, 3);
		grafo.añadirArista(1, 4, 2);
		grafo.añadirArista(2, 4, 2);
		grafo.añadirArista(2, 3, 6);
		grafo.añadirArista(3, 4, 2);
		NodoInt nodo = new NodoInt(grafo);
		nodo.primBuild(grafo);
		nodo.mostrar();
		NodoInt nodo1 = new NodoInt(0,2,1,nodo);
		NodoInt nodo2 = new NodoInt(1,2,1,nodo);
		NodoInt nodo3 = new NodoInt(2,4,1,nodo);
		NodoInt nodo4 = new NodoInt(3,4,1,nodo);
		
		nodosCercanosTest.add(nodo1);
		nodosCercanosTest.add(nodo2);
		nodosCercanosTest.add(nodo3);
		nodosCercanosTest.add(nodo4);
		assertEquals(nodosCercanosTest, nodo.devolverNodosCercanos());
	}
	
	@Test
	public void impactoTotalTest() {
		grafo.añadirArista(0, 1, 5);
		grafo.añadirArista(1, 4, 2);
		grafo.añadirArista(1, 2, 1);
		grafo.añadirArista(1, 3, 8);
		grafo.añadirArista(3, 4, 2);
		grafo.añadirArista(0, 2, 3);
		grafo.añadirArista(1, 4, 2);
		grafo.añadirArista(2, 4, 2);
		grafo.añadirArista(2, 3, 6);
		grafo.añadirArista(3, 4, 2);
		NodoInt nodo = new NodoInt(grafo);
		nodo.primBuild(grafo);
		nodo.mostrar();
		NodoInt nodo1 = new NodoInt(0,2,1,nodo);
		NodoInt nodo2 = new NodoInt(1,2,1,nodo);
		NodoInt nodo3 = new NodoInt(2,4,1,nodo);
		NodoInt nodo4 = new NodoInt(3,4,1,nodo);
		
		nodosCercanosTest.add(nodo1);
		nodosCercanosTest.add(nodo2);
		nodosCercanosTest.add(nodo3);
		nodosCercanosTest.add(nodo4);
		assertEquals(8, nodo.impactoTotal);
	}


}
