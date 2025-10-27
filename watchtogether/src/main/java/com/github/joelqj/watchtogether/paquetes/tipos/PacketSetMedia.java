package com.github.joelqj.watchtogether.paquetes.tipos;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;
import com.github.joelqj.watchtogether.WatchTogetherMain;

public class PacketSetMedia extends Packet {
	
	
	public PacketSetMedia() {
		
	}

	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
	
		WatchTogetherMain.player.setMedia(data.getString("path").replace("%ip%", reproductor.ip()));
	}

	
	@Override
	public String toString() {
		JSONObject prueba = new JSONObject();
		prueba.put("tipo", "setmedia");
		prueba.put("value", "setted");
		return prueba.toString();
	} 
	
}
