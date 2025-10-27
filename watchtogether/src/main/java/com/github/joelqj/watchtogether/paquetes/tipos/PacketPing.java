package com.github.joelqj.watchtogether.paquetes.tipos;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;

public class PacketPing extends Packet {

	public PacketPing() {

	}

	@Override
	public void handleData(JSONObject data, Cliente reproductor) {

	}

	@Override
	public String toString() {
		JSONObject prueba = new JSONObject();
		prueba.put("tipo", "ping");
		return prueba.toString();
	}

}
