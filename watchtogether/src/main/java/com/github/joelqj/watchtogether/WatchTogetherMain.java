package com.github.joelqj.watchtogether;

public class WatchTogetherMain {

	
	public static Player player;
	public static Cliente cliente;
	public static String version = "0.0.11-vlc4.x";
	public static void main(String[] args) {
		
		player = new Player();
		cliente = new Cliente(args.length > 0 ? args[0] : "127.0.0.1", 1411);
		
		 
	}

	
	
}
