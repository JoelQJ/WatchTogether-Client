package paquetes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.json.JSONObject;

import paquetes.tipos.IPacket;
import paquetes.tipos.Packet;
import paquetes.tipos.PacketPause;
import paquetes.tipos.PacketPing;
import paquetes.tipos.PacketSetMedia;
import paquetes.tipos.PacketSetTime;
import paquetes.tipos.PacketStartMedia;
import watchtogether.Cliente;

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
