package views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import system.Lector;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class MapView 
{
	private Lector lector;
	private TraductorGrafo _translator;
	private JFrame frame;
	private JPanel panelMapa;
	private JPanel panelControles;
	private JMapViewer _mapa;
	private ArrayList<MapPolygonImpl> _lasAristas;
	private ArrayList<MapMarkerDot> _markerList;
	private JTextField espacioPeso;
	private JComboBox<String> coordOrigen;
	private JComboBox<String> coordDestino;
	private JButton btnArista;
	private JComboBox<String> graphSelector;
	private JButton btnSetDeAristas;
	private JButton btnCompos;
	private JComboBox<String> coordsSelector;
	private JButton btnClear;
	private JButton btnPrimUpdate;

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
		lector = new Lector();
		_lasAristas = new ArrayList<MapPolygonImpl>();
		_markerList = new ArrayList<MapMarkerDot>();
		_translator = new TraductorGrafo(lector.graphs.get(0), lector.coords);
		initViews();
		initFunctions();

		detectarCoordenadas();	
	}

	private void detectarCoordenadas() {
		_mapa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)  {
				Coordinate _mouseCoords = (Coordinate)_mapa.getPosition(e.getPoint());
				{
					if (e.getButton() == MouseEvent.BUTTON1) {
						mostrarAristas(_mouseCoords);
					}
					if (e.getButton() == MouseEvent.BUTTON2) {
						añadirPunto(_mouseCoords);
					}
				}
			}
		});
	}
	private void añadirPunto(Coordinate coords) {
		_translator.añadirCoordenadas(coords);
		String nombre = ""+_markerList.size();
		MapMarkerDot mark = new MapMarkerDot(nombre, coords);
		_markerList.add(mark);
		_mapa.addMapMarker(mark);
		coordOrigen.addItem(mark.getName());
		coordDestino.addItem(mark.getName());
	}

	private void mostrarAristas(Coordinate _mouseCoords) {
		if (!_mapa.getMapMarkerList().isEmpty()) {
			for (int i = 0; i<_mapa.getMapMarkerList().size();i++)
				if ((_mouseCoords.getLon() >= _mapa.getMapMarkerList().get(i).getCoordinate().getLon()-0.001) &&
						(_mouseCoords.getLon() <= _mapa.getMapMarkerList().get(i).getCoordinate().getLon()+0.001) &&
						(_mouseCoords.getLat() >= _mapa.getMapMarkerList().get(i).getCoordinate().getLat()-0.001) &&
						(_mouseCoords.getLat() <= _mapa.getMapMarkerList().get(i).getCoordinate().getLat()+0.001)) {
					if (!_lasAristas.isEmpty() && i < _lasAristas.size()) {
						_lasAristas.get(i).setVisible(true);
						_mapa.repaint();
					}
					else
						return;
				}
		}
	}

	private void dibujarArista(MapPolygonImpl coords, int weight) {
		if(weight < 4)
			coords.setColor(Color.green);
		else
			if (weight < 7)
				coords.setColor(Color.yellow);
			else 
				if (weight <= 10)
					coords.setColor(Color.red);
		coords.setName(""+weight);
		_mapa.addMapPolygon(coords);
		coords.setVisible(false);
		_lasAristas.add(coords);

	}	
	private boolean pertenece(ArrayList<MapMarkerDot> list, MapMarkerDot mark) {
		for (MapMarkerDot m:list)
			if (m.getLat() == mark.getLat() && m.getLon() == mark.getLon()) {
				return true;
			}
		return false;
	}
	private void clear() {
		_lasAristas.clear();
		_mapa.removeAllMapMarkers();
		_mapa.removeAllMapPolygons();
		_markerList.clear();
		_translator.clear();
		coordDestino.removeAllItems();
		coordOrigen.removeAllItems();
		_mapa.repaint();
	}

	public void updateGraphs() {
		try {
		for (int i = 0; i<_translator.get_lasCoordenadas().size(); i++) {
			MapMarkerDot mark = new MapMarkerDot(i+"",_translator.get_lasCoordenadas().get(i).get(0));
			if (!pertenece(_markerList, mark)) 
				_markerList.add(mark);
		}
		for (MapMarkerDot d:_markerList) {
			_mapa.addMapMarker(d);
			coordDestino.addItem(d.getName());
			coordOrigen.addItem(d.getName());
		}
		int iterator = 0;
		for(MapPolygonImpl pol:_translator.primCall()) {
			LinkedList<Integer> pesos = _translator.weightTracker();
			dibujarArista(pol,pesos.get(iterator));
			iterator ++;
		}
		}
		catch (Exception E) {
			JOptionPane.showMessageDialog(null, "No hay aristas/puntos suficientes");
		}
	}

	private void initViews() {
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

		coordOrigen = new JComboBox<String>();
		coordOrigen.setBounds(10, 88, 47, 22);
		panelControles.add(coordOrigen);

		coordDestino = new JComboBox<String>();
		coordDestino.setBounds(94, 88, 47, 22);
		panelControles.add(coordDestino);

		espacioPeso = new JTextField();
		espacioPeso.setBounds(185, 89, 47, 20);
		panelControles.add(espacioPeso);
		espacioPeso.setColumns(10);


		graphSelector = new JComboBox<String>();
		graphSelector.setBounds(27, 166, 97, 22);
		graphSelector.addItem("graphs.txt");
		panelControles.add(graphSelector);

		coordsSelector = new JComboBox<String>();
		coordsSelector.setBounds(134, 165, 98, 22);
		coordsSelector.addItem("coords.txt");
		panelControles.add(coordsSelector);

		_mapa = new JMapViewer();
		_mapa.setZoomControlsVisible(false);
		_mapa.setDisplayPosition(new Coordinate(-34.472, -58.73), 15);

		panelMapa.add(_mapa);
	}
	private void initFunctions() {
		btnArista = new JButton("Añadir arista");
		btnArista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					_translator.añadirArista(Integer.parseInt((String)coordOrigen.getSelectedItem()), 
							Integer.parseInt((String)coordDestino.getSelectedItem()), Integer.parseInt(espacioPeso.getText()));
					_translator.get_originalGraph().print();
					for (MapMarkerDot d:_markerList) {
						if (d.getName() == (String)coordOrigen.getSelectedItem() || d.getName() == (String)coordDestino.getSelectedItem())
							_translator.añadirCoordenadas(d.getCoordinate());
					}
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Asegurate de poner un peso entre 1 y 10");
				}
			}
		});
		btnArista.setBounds(20, 121, 212, 23);
		panelControles.add(btnArista);

		btnSetDeAristas = new JButton("Seleccionar set de puntos y aristas");
		btnSetDeAristas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
				lector.readNew((String)graphSelector.getSelectedItem(), (String)coordsSelector.getSelectedItem());
				_translator.set_lasCoordenadas(lector.coords);
				_translator.set_originalGraph(lector.graphs.get(0));
				
			}
		});
		btnSetDeAristas.setBounds(25, 199, 200, 28);
		panelControles.add(btnSetDeAristas);

		btnCompos = new JButton("Comprobar nueva composicion");
		btnCompos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateGraphs();
			}
		});
		btnCompos.setBounds(20, 11, 212, 23);
		panelControles.add(btnCompos);

		btnClear = new JButton("Eliminar puntos y aristas");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		btnClear.setBounds(27, 412, 198, 23);
		panelControles.add(btnClear);
	}
}

