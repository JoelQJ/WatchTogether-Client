package watchtogether;

public class Info {

	private int indice;
	private String nombre;
	
	public Info(int indice, String nombre) {
		this.indice = indice;
		this.nombre =  String.format("%s - [%s]", nombre, indice);
	}

	public int getIndice() {
		return indice;
	}

	public String getNombre() {
		return nombre;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombre;
	}
	
}
