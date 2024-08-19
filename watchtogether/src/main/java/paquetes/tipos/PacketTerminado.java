package paquetes.tipos;

import org.json.JSONObject;

import watchtogether.Cliente;

public class PacketTerminado extends Packet {
	
	
	public PacketTerminado() {
		
	}
	
	
	
	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
	
	}

	
	@Override
	public String toString() {
		JSONObject prueba = new JSONObject();
		prueba.put("tipo", "finished");
		return prueba.toString();
	} 
	
}
