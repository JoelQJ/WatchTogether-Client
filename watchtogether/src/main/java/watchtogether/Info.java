package watchtogether;

import uk.co.caprica.vlcj.player.base.Track;

public class Info {

	private int indice;
	private String nombre;
	private String idioma;
	private Track track;
	public Info(int indice, Track track, String nombre, String idioma) {
		this.indice = indice;
		this.idioma = idioma;
		this.track = track;
		setNombre(nombre);
	}

	public int getIndice() {
		return indice;
	}

	public Track getTrack() {
		return track;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = String.format("%s [%s]", nombre, this.indice);
	}
	
	public String debug(){
		return String.format("[Info Nombre:%s Idioma:%s]", nombre, idioma);
	}

	@Override
	public String toString() {
		String idiomaFormat = idioma.equals(nombre) || idioma.isEmpty() ? "" : String.format("(%s)", idioma);
		return String.format("%s %s", nombre, idiomaFormat);
	}
	
}
