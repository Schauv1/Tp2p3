package system;

public class Arista  {
	private int origen, destino, peso;
	
	public Arista(int origen, int dest, int peso) {
		this.origen = origen;
		destino = dest;
		this.peso = peso;
	}
	
	public Arista(Arista a) {
		origen = a.origen;
		destino = a.destino;
		peso = a.peso;
	}
	
	@Override
	public int hashCode() {
		if (destino != 0 && origen != 0)
			return (destino*origen)*peso;
		return (destino+origen)*peso;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj.hashCode() == this.hashCode())
			return true;
		if (obj.getClass() != Arista.class) 
			return false;	
		Arista obj1 = (Arista)obj;
		if (obj1.destino == destino && obj1.origen == origen && obj1.peso == peso)
			return true;
		return false;
	}

	public int getOrigen() {
		return origen;
	}

	public int getDestino() {
		return destino;
	}
	
	public int getPeso() {
		return peso;
	}
}
