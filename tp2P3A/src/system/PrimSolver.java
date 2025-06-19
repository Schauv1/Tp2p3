package system;

import java.util.HashSet;

public class PrimSolver {
	HashSet<Integer> _visitados, _sinVisitar;
	HashSet<Arista> _aristas;
	int _impactoTotal;
	
	public PrimSolver() {
		_aristas = new HashSet<Arista>();
		_visitados = new HashSet<Integer>();
		_sinVisitar = new HashSet<Integer>();
		_impactoTotal = 0;
	}
	
	public HashSet<Arista> solve(Grafo graph) {
		if (!graph.conexo()) 
			throw new IllegalArgumentException("Grafo no conexo, intenta con otro");
		if (graph.getVertices() < 2) 
			throw new IllegalArgumentException("Grafo demasiado corto para uso en prim");
		for (int x = 0; x < graph.getVertices(); x++) {
			_sinVisitar.add(x);
		}
		_aristas.clear();
		_visitados.clear();
		int iterator = 0;
		while (_aristas.size() < graph.getVertices()-1) {
			Arista min = menorPeso(graph.getVecinos(iterator));
			while (min.getPeso() == 11) {
				iterator ++;
				if (iterator == graph.getVertices())
					iterator = 0;
				min = menorPeso(graph.getVecinos(iterator));
			}
			if (!_visitados.contains(min.getOrigen()) || !_visitados.contains(min.getDestino())) {
					añadirVisita(min);
					_impactoTotal += min.getPeso();
				}
				min = menorPeso(graph.getVecinos(iterator));
				if (min.getPeso() < menorPeso(graph.getVecinos(iterator+1)).getPeso() || min.getDestino() == iterator+1) {
						añadirVisita(min);
						_impactoTotal += min.getPeso();
			}
				iterator ++;
				if (iterator == graph.getVertices())
					iterator = 0;
		}
		return _aristas;
	}
	
	private Arista menorPeso(HashSet<Arista> aristas) {
		Arista min = new Arista(1,1,11);
		for (Arista a:aristas) {
			if (a.getPeso() < min.getPeso() && !_aristas.contains(min) && (!_visitados.contains(a.getDestino()) || !_visitados.contains(a.getOrigen()))) {
				if (_aristas.size() > 0) {
					if (!_visitados.contains(a.getDestino()) ^ !_visitados.contains(a.getOrigen()))
					min = new Arista(a);
				}
				min = new Arista(a);
			}
		}
		return min;
	}
	
	private void añadirVisita(Arista a) {
		_aristas.add(a);
		_visitados.add(a.getDestino());
		_visitados.add(a.getOrigen());
		_sinVisitar.remove(a.getOrigen());
		_sinVisitar.remove(a.getDestino());
	}
	
	public int getImpacto() {
		return _impactoTotal;
	}
}
