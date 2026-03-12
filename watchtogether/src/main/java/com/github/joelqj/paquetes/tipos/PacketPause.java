package com.github.joelqj.paquetes.tipos;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;
import com.github.joelqj.watchtogether.Main;

public class PacketPause extends Packet {

	private boolean pausa;
	
	
	public PacketPause() {
	}
	
	public PacketPause(boolean pausa) {
		this.pausa = pausa;
	}
	
	
	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
	
		Main.player.setPausar(data.getBoolean("pause"));
	}

	
	@Override
	public String toString() {
		JSONObject prueba = new JSONObject();
		prueba.put("tipo", "pause");
		prueba.put("pause", pausa);
		return prueba.toString();
	} 
	
}
