package com.github.joelqj.watchtogether;

public class Main {

	public static Player player;
	public static NetworkClient cliente;
	public static String version = "0.0.11";
	public static void main(String[] args) {

		player = new Player();
		cliente = new NetworkClient(args.length > 0 ? args[0] : "127.0.0.1", 1411);


	}


}
