package com.github.joelqj.watchtogether.paquetes.tipos;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;
import com.github.joelqj.watchtogether.WatchTogetherMain;

public class PacketSetTime extends Packet {

private Long time;
	
	
	public PacketSetTime() {
		
	}
	
	public PacketSetTime(Long time) {
		this.time = time;
	}
	
	
	@Override
	public void handleData(JSONObject data, Cliente reproductor) {
	
		WatchTogetherMain.player.setTime(data.getLong("time"));
	}

	
	@Override
	public String toString() {
		JSONObject prueba = new JSONObject();
		prueba.put("tipo", "settime");
		prueba.put("time", time);
		return prueba.toString();
	} 
	
}
