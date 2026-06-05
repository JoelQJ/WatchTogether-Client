package com.github.joelqj.paquetes.tipos;

import com.github.joelqj.watchtogether.NetworkClient;

import es.pyronixstudio.util.serializable.PyronixBuff;

public interface IToClientPacket extends IPacket {
    void handle(PyronixBuff buffer, NetworkClient client);
}
