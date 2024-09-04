package watchtogether;

public class Info {

	private int indice;
	private String nombre;
	
	public Info(int indice, String nombre) {
		this.indice = indice;
		setNombre(nombre);
	}

	public int getIndice() {
		return indice;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = String.format("%s - [%s]", nombre, this.indice);
	}
	
	@Override
	public String toString() {
		
		return nombre;
	}
	
}
