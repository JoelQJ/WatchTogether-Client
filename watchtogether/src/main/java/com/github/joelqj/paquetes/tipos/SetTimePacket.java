package com.github.joelqj.paquetes.tipos;

import com.github.joelqj.watchtogether.NetworkClient;

import es.pyronixstudio.util.serializable.PyronixBuff;

public class SetTimePacket implements IToServerPacket, IToClientPacket {
    private double time;

    public SetTimePacket() {
    }

    public SetTimePacket(double time) {
        this.time = time;
    }

    @Override
    public int id() { return 3; }

    @Override
    public void write(PyronixBuff buffer) {
        buffer.writeDouble(time);
    }

    @Override
    public void handle(PyronixBuff buffer, NetworkClient client) {
        time = buffer.readDouble();
        com.github.joelqj.watchtogether.Main.player.setTime((long) (time * 1000));
    }
}
