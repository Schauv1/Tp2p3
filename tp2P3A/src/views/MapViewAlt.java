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
import javax.swing.JRadioButton;

public class MapViewAlt
{
	private Lector lector;
	private TraductorGrafo _translator;
	private JFrame frame;
	private JPanel panelMapa;
	private JPanel panelControles;
	private JMapViewer _mapa;
	private ArrayList<MapPolygonImpl> _lasAristasPrim;
	private ArrayList<MapPolygonImpl> _lasAristas;
	private ArrayList<MapMarkerDot> _markerList;
	private JTextField espacioPeso;
	private JComboBox<String> coordOrigen;
	private JComboBox<String> coordDestino;
	private JButton btnArista;
	private JComboBox<String> graphSelector;
	private JButton btnSetDeAristas;
	private JComboBox<String> coordsSelector;
	private JButton btnClear;
	private JRadioButton aristasPrimbtn;
	private JRadioButton mostrarAristasbtn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() {
				try {
					MapViewAlt window = new MapViewAlt();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MapViewAlt() {
		initialize();
	}

	private void initialize() 
	{
		lector = new Lector();
		_lasAristasPrim = new ArrayList<MapPolygonImpl>();
		_lasAristas = new ArrayList<MapPolygonImpl>();
		_markerList = new ArrayList<MapMarkerDot>();
		_translator = new TraductorGrafo(lector.getGraph(), lector.getCoords(), lector.getNombres());
		initViews();
		initFunctions();
		updateGraphsByFile();
		detectarCoordenadas();	
	}

	private void detectarCoordenadas() {
		_mapa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)  {
				Coordinate _mouseCoords = (Coordinate)_mapa.getPosition(e.getPoint());
				{
					if (e.getButton() == MouseEvent.BUTTON1) {
						añadirPunto(_mouseCoords);
					}
				}
			}
		});
	}
	private void añadirPunto(Coordinate coords) {
		_translator.añadirCoordenadas(coords);
		String nombre = JOptionPane.showInputDialog("Nombre del puesto");
		if (nombre == null) {
			return;
		}	
		while (nombre.isBlank())
			nombre = ""+JOptionPane.showInputDialog("Debe elegir un nombre");
		MapMarkerDot mark = new MapMarkerDot(nombre, coords);
		_markerList.add(mark);
		_mapa.addMapMarker(mark);
		coordOrigen.addItem(mark.getName());
		coordDestino.addItem(mark.getName());
	}

	private void dibujarAristaPrim(MapPolygonImpl coords, int weight) {
		if(weight < 4)
			coords.setColor(Color.green);
		else
			if (weight < 7)
				coords.setColor(Color.yellow);
			else 
				if (weight <= 10)
					coords.setColor(Color.red);
		coords.setName(""+weight);
		if (!aristasPrimbtn.isSelected())
			coords.setVisible(false);
		_mapa.addMapPolygon(coords);
		_lasAristasPrim.add(coords);
		_mapa.repaint();
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
		if (!mostrarAristasbtn.isSelected())
			coords.setVisible(false);
		_mapa.addMapPolygon(coords);
		_lasAristas.add(coords);
		_mapa.repaint();
	}	
	
	private boolean pertenece(ArrayList<MapMarkerDot> list, MapMarkerDot mark) {
		for (MapMarkerDot m:list)
			if (m.getLat() == mark.getLat() && m.getLon() == mark.getLon()) {
				return true;
			}
		return false;
	}
	private void clear() {
		_lasAristasPrim.clear();
		_lasAristas.clear();
		_mapa.removeAllMapMarkers();
		_mapa.removeAllMapPolygons();
		_markerList.clear();
		_translator.clear();
		coordDestino.removeAllItems();
		coordOrigen.removeAllItems();
		_mapa.repaint();
	}

	private void updateGraphsByFile() {
		try {
			_mapa.removeAllMapPolygons();
		for (int i = 0; i<_translator.get_lasCoordenadas().size(); i++) {
			MapMarkerDot mark = new MapMarkerDot(_translator.get_nombres().get(i),_translator.get_lasCoordenadas().get(i).get(0));
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
			dibujarAristaPrim(pol,pesos.get(iterator));
			iterator ++;
		}
		iterator = 0;
		for (MapPolygonImpl pol: _translator.todasLasAristas()) {
			LinkedList<Integer> pesos = _translator.globalWeightTracker();
			dibujarArista(pol,pesos.get(iterator));
			iterator ++;
		}
		}
		catch (Exception E) {
			JOptionPane.showMessageDialog(null, "No hay aristas/puntos suficientes");
		}
	}
	
	private void updateGraphs() {
		try {
			_mapa.removeAllMapPolygons();
		int iterator = 0;
		for(MapPolygonImpl pol:_translator.primCall()) {
			LinkedList<Integer> pesos = _translator.weightTracker();
			dibujarAristaPrim(pol,pesos.get(iterator));
			iterator ++;
		}
		iterator = 0;
		for (MapPolygonImpl pol: _translator.todasLasAristas()) {
			LinkedList<Integer> pesos = _translator.globalWeightTracker();
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
		graphSelector.addItem("graph.txt");
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
					int mark1= 0,mark2= 0;
					for (int marcador = 0; marcador < _markerList.size(); marcador++) {
						if (_markerList.get(marcador).getName() == (String)coordOrigen.getSelectedItem()) {
							mark1 = marcador;
							_translator.añadirCoordenadas(_markerList.get(marcador).getCoordinate());
						}
						if (_markerList.get(marcador).getName() == (String)coordDestino.getSelectedItem()) {
							mark2 = marcador;
							_translator.añadirCoordenadas(_markerList.get(marcador).getCoordinate());
						}
					}
					_translator.añadirArista(mark1, mark2, Integer.parseInt(espacioPeso.getText()));
					updateGraphs();
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Asegurate de poner un peso entre 1 y 10");
				}
			}
		});
		btnArista.setBounds(20, 121, 212, 23);
		panelControles.add(btnArista);

		btnSetDeAristas = new JButton("Seleccionar archivos");
		btnSetDeAristas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
				lector.readNew((String)graphSelector.getSelectedItem(), (String)coordsSelector.getSelectedItem());
				_translator.set_lasCoordenadas(lector.getCoords());
				_translator.set_originalGraph(lector.getGraph());
				updateGraphsByFile();
			}
		});
		btnSetDeAristas.setBounds(25, 199, 200, 28);
		panelControles.add(btnSetDeAristas);

		btnClear = new JButton("Eliminar puntos y aristas");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		btnClear.setBounds(27, 412, 198, 23);
		panelControles.add(btnClear);
		
		mostrarAristasbtn = new JRadioButton("Mostrar aristas");
		mostrarAristasbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mostrarAristasbtn.isSelected()) {
					for (MapPolygonImpl a: _lasAristas)
						a.setVisible(true);
				}
				else {
					for (MapPolygonImpl a: _lasAristas)
						a.setVisible(false);
				}
				_mapa.repaint();
			}
		});
		mostrarAristasbtn.setBounds(27, 248, 141, 23);
		panelControles.add(mostrarAristasbtn);
		
		aristasPrimbtn = new JRadioButton("Mostrar aristas prim");
		aristasPrimbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (aristasPrimbtn.isSelected()) {
					for (MapPolygonImpl a: _lasAristasPrim)
						a.setVisible(true);
				}
				else {
					for (MapPolygonImpl a: _lasAristasPrim)
						a.setVisible(false);
				}
				_mapa.repaint();
			}
		});
		aristasPrimbtn.setBounds(27, 287, 198, 23);
		aristasPrimbtn.setSelected(true);
		panelControles.add(aristasPrimbtn);
	}
}
