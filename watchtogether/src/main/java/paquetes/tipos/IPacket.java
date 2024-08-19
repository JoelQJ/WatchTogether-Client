package paquetes.tipos;

import org.json.JSONObject;

import watchtogether.Cliente;

public interface IPacket {
	 public void handleData(JSONObject data, Cliente reproductor);
}
