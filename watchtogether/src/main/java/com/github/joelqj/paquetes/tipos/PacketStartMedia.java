package com.github.joelqj.paquetes.tipos;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;
import com.github.joelqj.watchtogether.Main;

public class PacketStartMedia extends Packet {
	
	
	public PacketStartMedia() {
	}
	
	
	
	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
	
		Main.player.setPlayPrimeraVez();
	}

	
}
