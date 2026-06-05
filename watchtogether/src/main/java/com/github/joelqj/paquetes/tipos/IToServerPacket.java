package com.github.joelqj.paquetes.tipos;

import es.pyronixstudio.util.serializable.PyronixBuff;

public interface IToServerPacket extends IPacket {
    void write(PyronixBuff buffer);
}
