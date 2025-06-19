package views;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import system.Arista;
import system.Grafo;
import system.PrimSolver;

public class TraductorGrafo {
	private ArrayList<ArrayList<Coordinate>> _lasCoordenadas;
	private ArrayList<String> _nombres;
	private Grafo _originalGraph;
	private PrimSolver _primSolver;
	private HashSet<Arista> _prim;
	
	public TraductorGrafo(Grafo graph, ArrayList<ArrayList<Coordinate>> coords, ArrayList<String> nombres) {
		_originalGraph = graph;
		_lasCoordenadas = coords;
		_nombres = nombres;
	}

	public ArrayList<String> get_nombres() {
		return _nombres;
	}

	public ArrayList<MapPolygonImpl> primCall() {
		_primSolver = new PrimSolver();
		_prim =  _primSolver.solve(_originalGraph);
		ArrayList<MapPolygonImpl> bestPath = new ArrayList<MapPolygonImpl>();
		LinkedList<Point> primSequence1 = pointCollector(_prim);
		for(Point p: primSequence1) {
			bestPath.add(createPoly(p));
		}
		return bestPath;
	}
	
	public ArrayList<MapPolygonImpl> todasLasAristas() {
		ArrayList<MapPolygonImpl> aristas = new ArrayList<MapPolygonImpl>();
		LinkedList<Point> sequence = pointCollector(_originalGraph.getAristas());
		for(Point p: sequence) {
			aristas.add(createPoly(p));
		}
		return aristas;
	}
	
	private MapPolygonImpl createPoly(Point p) {
		return new MapPolygonImpl(_lasCoordenadas.get(p.x).get(0),_lasCoordenadas.get(p.x).get(0),_lasCoordenadas.get(p.y).get(0));
	}
	
	public ArrayList<ArrayList<Coordinate>> get_lasCoordenadas() {
		return _lasCoordenadas;
	}

	public void set_lasCoordenadas(ArrayList<ArrayList<Coordinate>> _lasCoordenadas) {
		this._lasCoordenadas = _lasCoordenadas;
	}

	public Grafo get_originalGraph() {
		return _originalGraph;
	}

	public void set_originalGraph(Grafo _originalGraph) {
		this._originalGraph = _originalGraph;
	}
	
	public void añadirCoordenadas(Coordinate coords) {
		if (_lasCoordenadas.isEmpty()) {
			_lasCoordenadas.add(new ArrayList<Coordinate>());
			_lasCoordenadas.get(0).add(coords);
			}
		if (!_lasCoordenadas.isEmpty())
			for (int i = 0; i < _lasCoordenadas.size(); i++)
				if (coords.getLat() == _lasCoordenadas.get(i).get(0).getLat() && coords.getLon() == _lasCoordenadas.get(i).get(0).getLon())
					return;
				else
					if(coords.getLat() != _lasCoordenadas.get(i).get(0).getLat() && coords.getLon() != _lasCoordenadas.get(i).get(0).getLon()) {
						continue;
					}
		_lasCoordenadas.add(new ArrayList<Coordinate>());
		_lasCoordenadas.get(_lasCoordenadas.size()-1).add(coords);
	}
	
	public void añadirArista(int x, int y, int impact) {
		_originalGraph.añadirArista(x, y, impact);
	}
	
	public void clear() {
		_originalGraph = null;
		_originalGraph = new Grafo(1);
		_lasCoordenadas = null;
		_lasCoordenadas = new ArrayList<ArrayList<Coordinate>>();
	}
	
	private LinkedList<Point> pointCollector(HashSet<Arista> set)  {
		LinkedList<Point> points = new LinkedList<Point>();
		for (Arista a:set) {
			points.add(new Point(a.getOrigen(),a.getDestino()));
		}
		return points;
	}

	public LinkedList<Integer> weightTracker() {
		if (_prim==null || _prim.isEmpty())
			throw new IllegalArgumentException("No se llamó prim previamente");
		LinkedList<Integer> temp = new LinkedList<Integer>();
		for (Arista a:_prim)
			temp.add(a.getPeso());
		return temp;
	}
	
	public LinkedList<Integer> globalWeightTracker() {
		LinkedList<Integer> temp = new LinkedList<Integer>();
		for (Arista a:_originalGraph.getAristas())
			temp.add(a.getPeso());
		return temp;
	}
}
