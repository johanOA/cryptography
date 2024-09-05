package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


import model.Usuario;

import util.Base64;
import util.Util;

import java.io.*;


public class NetworkServer {
	public static final int PORT = 3400;

	private ServerSocket listener;
	private Socket serverSideSocket;
	
	private PrintWriter toNetwork;
	private BufferedReader fromNetwork;
	private Map<String, Double> dataMap;
	
	private int port;
	
	public NetworkServer() {
		dataMap = new HashMap<>();
		this.port = PORT;
		System.out.println("Johan A. Ospina - Jul 19/2024");
		System.out.println("Echo server is running on port: " + this.port);
		//System.out.println("Run: java -jar EchoClient.jar hostname " + this.port);
	}
	
	public NetworkServer(int port) {
		this.port = port;
		System.out.println("Johan A. Ospina - Jul 19/2024");
		System.out.println("Echo server is running on port: " + this.port);
		//System.out.println("Run: java -jar EchoClient.jar hostname " + this.port);
	}
	
	private void createStreams(Socket socket) throws IOException {
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	private void protocol(Socket socket) throws IOException, ClassNotFoundException {
		
		String answer = "";
		createStreams(socket);


		//Leer de Cliente
		String base64Message = fromNetwork.readLine();
		System.out.println("[Server] Client request: "+base64Message+"\n");
		System.out.println("[Server] from client: " + base64Message);

		//decodificar el base64
		Usuario user = decodeBase64(base64Message);

		//Procesar el objeto
		String name = user.getNombre();
		Double value = user.getMonto();

		if (dataMap.containsKey(name)) {
			dataMap.put(name, dataMap.get(name) + value);
			answer = "Transaccion realizada. Saldo:" + dataMap.get(name);
		} else {
			dataMap.put(name, value);
			//Respuest por almacenada
			answer = "Cuenta creada exitosamente. Saldo:"+dataMap.get(name);
		}

		// Enviar la respuesta de vuelta al cliente
		byte[] answerBA = Util.objectToByteArray(answer);
		String answerB64 = Base64.encode(answerBA);
		toNetwork.println(answerB64);
		System.out.println("[Server] Sent to client: " + answerB64);
	
		System.out.println("[Server] Waiting for a new client.");

	}
	
	private Usuario decodeBase64(String base64String) throws IOException, ClassNotFoundException {
    	byte[] data = Base64.decode(base64String);
    	try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
        	return (Usuario) ois.readObject();
    	}
	}

	private void init() throws IOException, ClassNotFoundException {
		listener = new ServerSocket(this.port);

		while (true) {
			serverSideSocket = listener.accept();
			
			String ip = serverSideSocket.getInetAddress().getHostAddress();
			int port = serverSideSocket.getPort();
			System.out.println("Client IP addres: " + ip);
			System.out.println("Client number port: " + port);

			protocol(serverSideSocket);
		}
	}
	
	public static void main(String args[]) throws Exception {
		NetworkServer es = null;
		if (args.length == 0) {
			es = new NetworkServer();
		} else {
			int port = Integer.parseInt(args[0]);
			es = new NetworkServer(port);
		}
		es.init();	
	}
}