package com.github.joelqj.paquetes.tipos;

import es.pyronixstudio.util.serializable.PyronixBuff;

public class PlayPausePacket implements IToServerPacket, IToClientPacket {
    private boolean pause;

    public PlayPausePacket() {
    }

    public PlayPausePacket(boolean pause) {
        this.pause = pause;
    }

    public boolean isPause() { return pause; }

    @Override
    public int id() { return 1; }

    @Override
    public void write(PyronixBuff buffer) {
        buffer.writeBoolean(pause);
    }

    @Override
    public void handle(PyronixBuff buffer, com.github.joelqj.watchtogether.NetworkClient client) {
        //El nuevo protocolo declara Play true -false no Pause
        pause = !buffer.readBoolean();
        com.github.joelqj.watchtogether.Main.player.setPausar(pause);
    }
}
