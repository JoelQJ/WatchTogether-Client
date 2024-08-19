package paquetes.tipos;

import org.json.JSONObject;

import watchtogether.Cliente;
import watchtogether.Main;

public class PacketStartMedia extends Packet {

private boolean pausa;
	
	
	public PacketStartMedia() {
	}
	
	
	
	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
	
		Main.player.setPlayPrimeraVez();
	}

	
	@Override
	public String toString() {
		JSONObject prueba = new JSONObject();
		prueba.put("tipo", "startmedia");
		prueba.put("pause", pausa);
		return prueba.toString();
	} 
	
}
