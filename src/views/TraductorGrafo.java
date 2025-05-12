package views;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import system.Grafo;
import system.NodoInt;

public class TraductorGrafo {
	private ArrayList<ArrayList<Coordinate>> _lasCoordenadas = new ArrayList<ArrayList<Coordinate>>();
	private Grafo _originalGraph;
	
	public TraductorGrafo(Grafo graph, ArrayList<ArrayList<Coordinate>> coords) {
		_originalGraph = graph;
		_lasCoordenadas = coords;
	}

	public ArrayList<MapPolygonImpl> primCall() {
		NodoInt temp = new NodoInt(_originalGraph);
		temp.primBuild(_originalGraph);
		ArrayList<MapPolygonImpl> bestPath = new ArrayList<MapPolygonImpl>();
		LinkedList<Point> temp1 = temp.getAllNodes();
		for(Point p:temp1) {
			bestPath.add(createPoly(p));
		}
		return bestPath;
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
}
