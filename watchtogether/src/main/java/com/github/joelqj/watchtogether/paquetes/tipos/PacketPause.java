package com.github.joelqj.watchtogether.paquetes.tipos;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;
import com.github.joelqj.watchtogether.WatchTogetherMain;

public class PacketPause extends Packet {

	private boolean pausa;
	
	
	public PacketPause() {
	}
	
	public PacketPause(boolean pausa) {
		this.pausa = pausa;
	}
	
	
	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
	
		WatchTogetherMain.player.setPausar(data.getBoolean("pause"));
	}

	
	@Override
	public String toString() {
		JSONObject prueba = new JSONObject();
		prueba.put("tipo", "pause");
		prueba.put("pause", pausa);
		return prueba.toString();
	} 
	
}
