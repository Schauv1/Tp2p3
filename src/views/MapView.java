package views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import system.Lector;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MapView 
{
	private Lector lector;
	private TraductorGrafo tr;
	private JFrame frame;
	private JPanel panelMapa;
	private JPanel panelControles;
	private JMapViewer _mapa;
	private ArrayList<ArrayList<Coordinate>> _lasCoordenadas;
	private ArrayList<MapPolygonImpl> _losPoligonos;
	private ArrayList<MapMarkerDot> markerList;
	private MapPolygonImpl _poligono;
	private JButton btnDibujarPolgono ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() {
				try {
					MapView window = new MapView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MapView() {
		initialize();
	}

	private void initialize() 
	{
		_lasCoordenadas = new ArrayList<ArrayList<Coordinate>>();
		_losPoligonos = new ArrayList<MapPolygonImpl>();
		markerList = new ArrayList<MapMarkerDot>();
		lector = new Lector();
		tr = new TraductorGrafo(lector.graphs.get(0), lector.coords);
		frame = new JFrame();
		frame.setBounds(100, 100, 725, 506);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panelMapa = new JPanel();
		panelMapa.setBounds(10, 11, 437, 446);
		frame.getContentPane().add(panelMapa);
		
		panelControles = new JPanel();
		panelControles.setBounds(457, 11, 242, 446);
		frame.getContentPane().add(panelControles);		
		panelControles.setLayout(null);
		
		_mapa = new JMapViewer();
		_mapa.setZoomControlsVisible(false);
		_mapa.setDisplayPosition(new Coordinate(-34.472, -58.73), 15);
				
		panelMapa.add(_mapa);
		for (int i = 0; i<tr.get_lasCoordenadas().size(); i++) {
			for(Coordinate c: tr.get_lasCoordenadas().get(i)) {
				//System.out.println(i+""+c);
				if (!markerList.contains(c))
				markerList.add(new MapMarkerDot(i+"a",c));
			}
		}
		for (MapMarkerDot d:markerList) {
			d.setColor(Color.red);
			_mapa.addMapMarker(d);
		}
		
		for(MapPolygonImpl pol:tr.primCall())
			_mapa.addMapPolygon(pol);

		detectarCoordenadas();
		dibujarPoligono();	
	}
	
	private void detectarCoordenadas() {
		_mapa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
			if (e.getButton() == MouseEvent.BUTTON1) {
				Coordinate _mouseCoords = (Coordinate)_mapa.getPosition(e.getPoint());
				System.out.println(_mouseCoords);
				if (!_mapa.getMapMarkerList().isEmpty()) {
					if ((_mouseCoords.getLon() >= _mapa.getMapMarkerList().get(0).getCoordinate().getLon()-0.1) &&
							(_mouseCoords.getLon() <= _mapa.getMapMarkerList().get(0).getCoordinate().getLon()+0.1) &&
							(_mouseCoords.getLat() >= _mapa.getMapMarkerList().get(0).getCoordinate().getLat()-0.1) &&
							(_mouseCoords.getLat() <= _mapa.getMapMarkerList().get(0).getCoordinate().getLat()+0.1)) {
						if (!_losPoligonos.isEmpty()) {
							_losPoligonos.get(0).setVisible(true);
							_mapa.repaint();
						}
						else
							return;
						}
					}
				
				}
			}
		});
	}

	private void dibujarPoligono() {
		btnDibujarPolgono = new JButton("Dibujar Pol\u00EDgono");
		btnDibujarPolgono.setBounds(10, 11, 195, 23);
		panelControles.add(btnDibujarPolgono);
	}
	private void dibujarArista(ArrayList<Coordinate> coords, int weight) {
		_poligono = new MapPolygonImpl(coords);
		if(weight < 4)
			_poligono.setColor(Color.green);
		else
			if (weight < 7)
				_poligono.setColor(Color.yellow);
			else 
				if (weight <= 10)
					_poligono.setColor(Color.red);
		_mapa.addMapPolygon(_poligono);
		_poligono.setVisible(false);
		_losPoligonos.add(_poligono);

	}	
}

