package com.github.joelqj.watchtogether.paquetes.tipos;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;
import com.github.joelqj.watchtogether.WatchTogetherMain;

public class PacketTerminado extends Packet {
	
	
	public PacketTerminado() {
		
	}
	
	
	
	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
		WatchTogetherMain.player.terminar();
	}

	
	@Override
	public String toString() {
		JSONObject prueba = new JSONObject();
		prueba.put("tipo", "finished");
		return prueba.toString();
	} 
	
}
