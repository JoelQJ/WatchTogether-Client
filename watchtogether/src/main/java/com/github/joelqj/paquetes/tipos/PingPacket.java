package com.github.joelqj.paquetes.tipos;

import com.github.joelqj.watchtogether.NetworkClient;
import es.pyronixstudio.util.serializable.PyronixBuff;

public class PingPacket implements IToServerPacket, IToClientPacket {
    @Override
    public int id() { return 0; }

    @Override
    public void write(PyronixBuff buffer) {
        buffer.writeBoolean(true);
    }

    @Override
    public void handle(PyronixBuff buffer, NetworkClient client) {
        System.out.println("Se recibio ping del server!");
    }
}
