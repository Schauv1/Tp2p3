package views;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import system.Grafo;
import system.NodoInt;

public class TraductorGrafo {
	private ArrayList<ArrayList<Coordinate>> _lasCoordenadas;
	private Grafo _originalGraph;
	private NodoInt _primSequence;
	
	public TraductorGrafo(Grafo graph, ArrayList<ArrayList<Coordinate>> coords) {
		_originalGraph = graph;
		_lasCoordenadas = coords;
	}

	public ArrayList<MapPolygonImpl> primCall() {
		_primSequence = new NodoInt(_originalGraph);
		_primSequence.primBuild(_originalGraph);
		ArrayList<MapPolygonImpl> bestPath = new ArrayList<MapPolygonImpl>();
		LinkedList<Point> _primSequence1 = _primSequence.getAllNodes();
		for(Point p:_primSequence1) {
			bestPath.add(createPoly(p));
		}
		return bestPath;
	}
	
	public LinkedList<Integer> weightTracker() {
		LinkedList<Integer> allWeights = new LinkedList<Integer>();
		LinkedList<Point> _primSequence1 = _primSequence.getAllNodes();
		for(Point p:_primSequence1) {
			allWeights.add(_originalGraph.getPeso(p.x, p.y));
		}
		return allWeights;
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
		_originalGraph.extenderGrafo();
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
		_primSequence = null;
	}
}
