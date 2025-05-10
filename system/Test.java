package system;

public class Test {
	public static void main(String[] args) {
		Grafo grafo;
		grafo = new Grafo(5);
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
		NodoInt wea = new NodoInt(grafo);
		wea.primBuild(grafo);
		wea.mostrar();
		System.out.println(wea.impactoTotal);
	}
	

}
