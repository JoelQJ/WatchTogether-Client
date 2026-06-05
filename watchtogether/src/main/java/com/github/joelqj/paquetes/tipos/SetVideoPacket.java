package com.github.joelqj.paquetes.tipos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.github.joelqj.watchtogether.Main;
import com.github.joelqj.watchtogether.NetworkClient;

import es.pyronixstudio.util.serializable.PyronixBuff;

public class SetVideoPacket implements IToClientPacket {
    @Override
    public int id() { return 2; }

    @Override
    public void handle(PyronixBuff buffer, NetworkClient client) {
        String path = buffer.readString();
        String[] segments = path.split("/");
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < segments.length; i++) {
            if (i > 0) encoded.append("/");
            encoded.append(URLEncoder.encode(segments[i], StandardCharsets.UTF_8).replace("+", "%20"));
        }
        String url = "http://" + client.getIp() + ":" + client.getHttpPort() + "/" + encoded.toString();
        Main.player.setMedia(url);
    }
}
