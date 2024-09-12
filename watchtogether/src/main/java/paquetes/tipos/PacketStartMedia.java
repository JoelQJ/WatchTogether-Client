package paquetes.tipos;

import org.json.JSONObject;

import watchtogether.Cliente;
import watchtogether.Main;

public class PacketStartMedia extends Packet {
	
	
	public PacketStartMedia() {
	}
	
	
	
	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
	
		Main.player.setPlayPrimeraVez();
	}

	
}
