package com.github.joelqj.paquetes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.github.joelqj.paquetes.tipos.IToClientPacket;
import com.github.joelqj.paquetes.tipos.PingPacket;
import com.github.joelqj.paquetes.tipos.PlayPausePacket;
import com.github.joelqj.paquetes.tipos.SetTimePacket;
import com.github.joelqj.paquetes.tipos.SetVideoPacket;
import com.github.joelqj.watchtogether.NetworkClient;

import es.pyronixstudio.util.serializable.PyronixBuff;

public class PacketDispatcher {

    private final Map<Integer, Supplier<IToClientPacket>> packets = new HashMap<>();

    public PacketDispatcher() {
        registerPackets();
    }

    private void registerPackets() {
        packets.put(0, PingPacket::new);
        packets.put(1, PlayPausePacket::new);
        packets.put(2, SetVideoPacket::new);
        packets.put(3, SetTimePacket::new);
    }

    public void dispatch(int packetId, PyronixBuff buffer, NetworkClient client) {
        Supplier<IToClientPacket> supplier = packets.get(packetId);
        if (supplier != null) {
            IToClientPacket packet = supplier.get();
            packet.handle(buffer, client);
        }
    }

}
