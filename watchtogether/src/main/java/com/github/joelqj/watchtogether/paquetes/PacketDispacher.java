package com.github.joelqj.watchtogether.paquetes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.json.JSONObject;

import com.github.joelqj.watchtogether.Cliente;
import com.github.joelqj.watchtogether.paquetes.tipos.IPacket;
import com.github.joelqj.watchtogether.paquetes.tipos.Packet;
import com.github.joelqj.watchtogether.paquetes.tipos.PacketPause;
import com.github.joelqj.watchtogether.paquetes.tipos.PacketPing;
import com.github.joelqj.watchtogether.paquetes.tipos.PacketSetMedia;
import com.github.joelqj.watchtogether.paquetes.tipos.PacketSetTime;
import com.github.joelqj.watchtogether.paquetes.tipos.PacketStartMedia;
import com.github.joelqj.watchtogether.paquetes.tipos.PacketTerminado;

public class PacketDispacher {

	private Map<String, Supplier<Packet>> paquetes = new HashMap<>();

	public PacketDispacher() {
		registerPackets();
	}

	private void registerPacket(String name, Supplier<Packet> packetClass) {
		paquetes.put(name, packetClass);
	}

	private void registerPackets() {
		//AQUI SE REGISTRAN LOS PAQUETES LOS CUALES PUEDE ENVIAR EL SERVER, LOS QUE ENVIA EL CLIENTE NO.
		registerPacket("pause", PacketPause::new);
		registerPacket("setmedia", PacketSetMedia::new);
		registerPacket("settime", PacketSetTime::new);
		registerPacket("ping", PacketPing::new);
		registerPacket("startmedia", PacketStartMedia::new);
		registerPacket("terminated", PacketTerminado::new);
	}

	public void dispachPacket(String data, Cliente jugador) {
		try {
			JSONObject jsonData = new JSONObject(data);
			String tipo = jsonData.getString("tipo");
			if (paquetes.containsKey(tipo)) {
				Packet packet = paquetes.get(tipo).get();
				if (packet instanceof IPacket)
					((IPacket) packet).handleData(jsonData,jugador);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
