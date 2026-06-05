package com.github.joelqj.paquetes.tipos;

import es.pyronixstudio.util.serializable.PyronixBuff;

public class VideoFinishedPacket implements IToServerPacket {
    @Override
    public int id() { return 4; }

    @Override
    public void write(PyronixBuff buffer) {
    }
}
