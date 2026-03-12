package com.github.joelqj.paquetes.tipos;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;

public interface IPacket {
	 public void handleData(JSONObject data, Cliente reproductor);
}
