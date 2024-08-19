package paquetes.tipos;

import org.json.JSONObject;

import watchtogether.Cliente;
import watchtogether.Main;

public class PacketSetMedia extends Packet {
	
	
	public PacketSetMedia() {
		
	}

	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
	
		Main.player.setMedia(data.getString("path").replace("%ip%", reproductor.ip()));
	}

	
	@Override
	public String toString() {
		JSONObject prueba = new JSONObject();
		prueba.put("tipo", "setmedia");
		prueba.put("value", "setted");
		return prueba.toString();
	} 
	
}
