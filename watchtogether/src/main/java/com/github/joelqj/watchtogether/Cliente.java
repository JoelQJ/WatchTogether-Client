package com.github.joelqj.watchtogether;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.joelqj.watchtogether.paquetes.PacketDispacher;
import com.github.joelqj.watchtogether.paquetes.tipos.PacketPing;

public class Cliente {

	
	private String ip;
	private int puerto;
	private DataOutputStream out;
	private DataInputStream in;
	private Socket socket;
	private ExecutorService executor = Executors.newFixedThreadPool(10);
	private Rsa criptografia = new Rsa();
	private static PacketDispacher packetDispacher;
	public Cliente(String ip, int puerto) {
		this.ip = ip;
		this.puerto = puerto;
		conectar();
	}
	
	public String ip() {
		return ip;
	}
	
	private void conectar() {
		try {
			socket = new Socket(ip, puerto);
			in =  new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			packetDispacher = new PacketDispacher();
			executor.submit(() -> {try {
				leer();
			} catch (Exception e) {
				e.printStackTrace();
			}});
			
			
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		
		executor.submit(() -> {
			

			 Timer timer = new Timer();

		        TimerTask task = new TimerTask() {
		            @Override
		            public void run() {
		               enviarPaquete(new PacketPing().toString());
		            }
		        };

		        // Programa la tarea para que se ejecute cada 5 segundos
		        long delay = 1000*25; // Tiempo inicial antes de que la tarea se ejecute por primera vez
		        long period = 1000*60; // Periodo entre ejecuciones en milisegundos (5 segundos)

		        timer.scheduleAtFixedRate(task, delay, period);
			
		});
		 


	}
	
	public void enviarPaquete(String data) {

		
		try {
			out.writeUTF(criptografia.estaLaClavePublicaDelServidor() ? criptografia.encriptarConClavePublicaServidor(data+"^") : data);
			out.flush();
		} catch (IOException e) {
			desconectar();
		}
	}
	
	private void desconectar() {
	
		
	}
	
	private void leer() {
		try {
			enviarPaquete(String.format("publicKey:%s",criptografia.getPublicKeyEncoded()));
			while (!socket.isClosed()) {
				
				String paqueteRecibido = in.readUTF();
				if(paqueteRecibido.contains("publicKey:")) {
					criptografia.cargarClavePublica(paqueteRecibido.split(":")[1]);
					continue;
					
				}
				if(criptografia.estaLaClavePublicaDelServidor()) {
					String paqueteDescriptado = criptografia.desencriptar(paqueteRecibido);
					if(paqueteDescriptado.charAt(0) == '{')
						Cliente.packetDispacher.dispachPacket(paqueteDescriptado, Cliente.this);
					
					System.out.println("[Cliente]: " + paqueteDescriptado);

					}
			}
			desconectar();
		} catch (Exception e) {
			
		}
	}
	
}
