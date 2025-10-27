package com.github.joelqj.watchtogether.paquetes.tipos;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;
import com.github.joelqj.watchtogether.WatchTogetherMain;

public class PacketStartMedia extends Packet {
	
	
	public PacketStartMedia() {
	}
	
	
	
	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
	
		WatchTogetherMain.player.setPlayPrimeraVez();
	}

	
}
