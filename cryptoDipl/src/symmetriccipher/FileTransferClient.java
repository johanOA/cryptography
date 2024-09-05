package symmetriccipher;

import java.net.Socket;

import integrity.Hasher;
import util.Files;
import util.Util;

public class FileTransferClient {
	public static final int PORT = 4000;
	public static final String SERVER = "localhost";

	Hasher hasher = new Hasher();

	private Socket clientSideSocket;
	
	private String server;
	private int port;
	
	public FileTransferClient() {
		this.server = SERVER;
		this.port = PORT;
		System.out.println("Johan A. Ospina - Jul 19/2024");
		System.out.println("File transfer client is running ... connecting the server in "+ this.server + ":" + this.port);
		System.out.println("Other usage: FileTransferClient host port. Ex: FileTransferClient localhost 3800");
	}
	
	public FileTransferClient(String server, int port) {
		this.server = server;
		this.port = port;
		System.out.println("Johan A. Ospina - Jul 19/2024");
		System.out.println("Echo client is running ... connecting the server in "+ this.server + ":" + this.port);
	}

	public void init() throws Exception {
		clientSideSocket = new Socket(SERVER, PORT);

		protocol(clientSideSocket);
		clientSideSocket.close();
	}

	public void protocol(Socket socket) throws Exception {
		
		Files.pause(200);

		Files.pause(200);

		Files.pause(200);

		Files.pause(200);
		//----------------------------------------------------
		//RECIBE EN
		Files.receiveFolder("Hola","Client", socket);
		//----------------------------------------------------
		Files.pause(200);


		
		//System.out.println("Integridad archivos:");
        //Hasher.checkIntegrityFile("Client/Hola/hasherfile.plain.txt");
		
		//System.out.println("Integridad archivos destino Client:");
        //Hasher.checkIntegrityFile("Client/Hola/hasherfile.txt");
		
	}
	
	public static void main(String[] args) throws Exception {
		FileTransferClient ftc = null;
		if (args.length == 0) {
			ftc = new FileTransferClient();
						
		} else {
			String server = args[0];
			int port = Integer.parseInt(args[1]);
			ftc = new FileTransferClient(server, port);
		}
		ftc.init();
	}
}