package network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import model.Usuario;
import util.Base64;
import util.Util;

public class NetworkClient {
	public static final String SERVER = "localhost";
	public static final int PORT = 3400;

	private static final Scanner SCANNER = new Scanner(System.in);

	private PrintWriter toNetwork;
	private BufferedReader fromNetwork;

	private Socket clientSideSocket;
	
	private String server;
	private int port;
	
	public NetworkClient() {
		this.server = SERVER;
		this.port = PORT;
		System.out.println("Johan A. Ospina - Jul 19/2024");
		System.out.println("Echo client is running ... connecting the server in "+ this.server + ":" + this.port);
		System.out.println("Other usage: EchoClient host port. Ex: EchoClient localhost 3600");
	}
	
	public NetworkClient(String server, int port) {
		this.server = server;
		this.port = port;
		System.out.println("Johan A. Ospina - Jul 19/2024");
		System.out.println("Echo client is running ... connecting the server in "+ this.server + ":" + this.port);
	}
	
	
	private void createStreams(Socket socket) throws IOException {
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void protocol(Socket socket) throws Exception {
		createStreams(socket);

		//Crear objeto usuario para enviar
		System.out.println("Ingrese usuario: ");
		String name = SCANNER.nextLine();
		System.out.println("Monto: ");
		double amount = Double.parseDouble(SCANNER.nextLine());
		Usuario usuario = new Usuario(name, amount);

		//Codificar
		String base64Message = encodeBase64(usuario);
		System.out.println("[Client] Codified request: "+base64Message+"\n");

		//Enviar al servidr
		toNetwork.println(base64Message);

		//leer la respuesta del servidor
		String fromServer = fromNetwork.readLine();
		System.out.println("[Client] Received Base64 from server: " + fromServer);

		byte[] fromServerBA = Base64.decode(fromServer);
		String message = (String) Util.byteArrayToObject(fromServerBA);

		System.out.println("[Client] From Server decode: "+ message);
		System.out.println("[Client] Finished.");

	}

	private String encodeBase64(Usuario usuario) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(usuario);
		} 
		byte[] bytes = baos.toByteArray();
		return Base64.encode(bytes);
	}
	
	public void init() throws Exception {
		clientSideSocket = new Socket(this.server, this.port);

		protocol(clientSideSocket);

		clientSideSocket.close();
	}
	
	public static void main(String args[]) throws Exception {
		NetworkClient ec = null;
		if (args.length == 0) {
			ec = new NetworkClient();
						
		} else {
			String server = args[0];
			int port = Integer.parseInt(args[1]);
			ec = new NetworkClient(server, port);
		}
		ec.init();
	}
}