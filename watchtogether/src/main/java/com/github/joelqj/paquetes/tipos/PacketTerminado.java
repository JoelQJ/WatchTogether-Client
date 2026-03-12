package com.github.joelqj.paquetes.tipos;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;
import com.github.joelqj.watchtogether.Main;

public class PacketTerminado extends Packet {
	
	
	public PacketTerminado() {
		
	}
	
	
	
	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
		Main.player.terminar();
	}

	
	@Override
	public String toString() {
		JSONObject prueba = new JSONObject();
		prueba.put("tipo", "finished");
		return prueba.toString();
	} 
	
}
