package system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Lector {
	public LinkedList<Grafo> graphs;
	public ArrayList<ArrayList<Coordinate>> coords;

	public Lector() {
		graphs = graphTaker();
		coords = coordTaker();
	}

	private LinkedList<Grafo> graphTaker () {
		LinkedList<Grafo> graphs = new LinkedList<Grafo>();
		try {
			File myObj = new File("src/Resources/graphs.txt");
			String data = "";
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				data = data + myReader.nextLine() + "\n";
			}
			List<String> lines = data.lines().toList();
			int graphNumber = 0;
			for (String line: lines) {
				if (line == "") {
					graphNumber ++;
				}
				else 
					if (line.charAt(0) == 'v') {
						graphs.add(new Grafo(Integer.parseInt(line.substring(4))));
						System.out.println("ok");
					}
					else {
						int[] temp = aristaReader(line);
						graphs.get(graphNumber).añadirArista(temp[0], temp[1], temp[2]);
					}
				myReader.close();
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return graphs;
	}

	private int[] aristaReader(String line) {
		int[] sequence = new int[3];
		int partOfSequence = 0;
		int lastComma = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ',' && partOfSequence == 0) {
				sequence[partOfSequence] = Integer.parseInt(line.substring(lastComma, i));
				partOfSequence ++;
				lastComma = i;
			}
			else {
				if (line.charAt(i) == ',' && partOfSequence != 0) {
					sequence[partOfSequence] = Integer.parseInt(line.substring(lastComma+2, i));
					partOfSequence ++;
					lastComma = i;
				}
			}
		}
		return sequence;
	}

	private ArrayList<ArrayList<Coordinate>> coordTaker () {
		ArrayList<ArrayList<Coordinate>> coords = new ArrayList<ArrayList<Coordinate>>();
		try {
			File myObj = new File("src/Resources/coords.txt");
			String data = "";
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				data = data + myReader.nextLine() + "\n";
			}
			List<String> lines = data.lines().toList();
			coords = coordReader(lines);
			myReader.close();

		}
		catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return coords;
	}

	private ArrayList<ArrayList<Coordinate>> coordReader(List<String> lines) {
		ArrayList<ArrayList<Coordinate>> sequence = new ArrayList<ArrayList<Coordinate>>();
		int partOfSequence = 0;
		for (String line:lines) {
			double coordLat = 0;
			double coordLon = 0;
			int lastComma = 0;
			boolean firstNumber = true;
			int temporaryPart = -1;
			for (int i = 0; i < line.length(); i++) {
				if (line.isBlank())
					return sequence;
				if (line.charAt(i) == ',' && coordLat == 0) {
					if (lastComma != 0)
					coordLat = Double.parseDouble(line.substring(lastComma+1, i));
					else
						coordLat = Double.parseDouble(line.substring(lastComma, i));
					lastComma = i;
				}
				else {
					if (line.charAt(i) == ',' && coordLon == 0.0 || i == line.length()-1) {
						if (i!=line.length()-1)
							coordLon = Double.parseDouble(line.substring(lastComma+1, i));
						if (i == line.length()-1)
							coordLon = Double.parseDouble(line.substring(lastComma+1, i+1));
						lastComma = i;
						for (int f = 0; f < sequence.size(); f++)
							if (coordLat == sequence.get(f).get(0).getLat() && coordLon == sequence.get(f).get(0).getLon() && firstNumber) {
								temporaryPart = f;
							}
						if (sequence.size() <= partOfSequence && firstNumber && temporaryPart == -1) {
							sequence.add(new ArrayList<Coordinate>());
							sequence.get(partOfSequence).add(new Coordinate(coordLat,coordLon));
							firstNumber = false;
						}
						else
							if (temporaryPart != -1 && !firstNumber) {
								sequence.get(temporaryPart).add(new Coordinate(coordLat,coordLon));
							}
							else
								if (temporaryPart != -1 && firstNumber)
									firstNumber = false;
								else
									if (temporaryPart == -1)
										sequence.get(partOfSequence).add(new Coordinate(coordLat,coordLon));
						coordLat = 0;
						coordLon = 0;
					}
				}
			}
			if (temporaryPart == -1)
				partOfSequence ++;
		}
		return sequence;
	}
}