package com.github.joelqj.paquetes.tipos;

import es.pyronixstudio.util.serializable.PyronixBuff;

public class VideoLoadedPacket implements IToServerPacket {
    @Override
    public int id() { return 5; }

    @Override
    public void write(PyronixBuff buffer) {
    }
}
