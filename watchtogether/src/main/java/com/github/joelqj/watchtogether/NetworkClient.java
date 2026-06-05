package com.github.joelqj.watchtogether;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.joelqj.paquetes.PacketDispatcher;
import com.github.joelqj.paquetes.tipos.HandShakePacket;
import com.github.joelqj.paquetes.tipos.IToServerPacket;
import com.github.joelqj.paquetes.tipos.PingPacket;
import es.pyronixstudio.util.serializable.PyronixBuff;

public class NetworkClient {

    private String ip;
    private int port;
    private int httpPort;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ExecutorService executor = Executors.newFixedThreadPool(10);
    private PacketDispatcher dispatcher = new PacketDispatcher();

    public NetworkClient(String ip, int port) {
        this(ip, port, 8080);
    }

    public NetworkClient(String ip, int port, int httpPort) {
        this.ip = ip;
        this.port = port;
        this.httpPort = httpPort;
        connect();
    }

    public String getIp() { return ip; }
    public int getHttpPort() { return httpPort; }

    private void connect() {
        try {
            socket = new Socket(ip, port);
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            sendPacket(new HandShakePacket());
            System.out.println("[NetworkClient] Conectado a " + ip + ":" + port);

            executor.submit(this::readLoop);

            executor.submit(() -> {
                Timer timer = new Timer(true);
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        sendPacket(new PingPacket());
                    }
                }, 1000 * 25, 1000 * 60);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(IToServerPacket packet) {
        try {
            PyronixBuff buffer = PyronixBuff.empty();
            buffer.setByteOrder(ByteOrder.BIG_ENDIAN);
            buffer.writeInt(packet.id());
            packet.write(buffer);

            byte[] payload = buffer.toByteArray();

            out.writeInt(payload.length);
            out.write(payload);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    private void readLoop() {
        try {
            while (!socket.isClosed()) {
                int payloadSize = in.readInt();
                byte[] payload = new byte[payloadSize];
                in.readFully(payload);

                PyronixBuff buffer = PyronixBuff.wrap(payload);
                buffer.setByteOrder(ByteOrder.BIG_ENDIAN);
                int packetId = buffer.readInt();
                dispatcher.dispatch(packetId, buffer, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) { }
    }
}
