package com.github.joelqj.paquetes.tipos;

import es.pyronixstudio.util.serializable.PyronixBuff;

public class HandShakePacket implements IToServerPacket {
    @Override
    public int id() { return -1; }

    @Override
    public void write(PyronixBuff buffer) {
    }
}
