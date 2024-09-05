package filetransfer;

import java.net.ServerSocket;
import java.net.Socket;

import integrity.Hasher;
import util.Files;

public class FileTransferServer {
	public static final int PORT = 4000;

	Hasher hasher = new Hasher();
	private ServerSocket listener;
	private Socket serverSideSocket;
	
	private int port;
	
	public FileTransferServer() {
		System.out.println("Johan A. Ospina - Jul 19/2024");
		System.out.println("File transfer server is running on port: " + this.port);
	}

	public FileTransferServer(int port) {
		this.port = port;
		System.out.println("Johan A. Ospina - Jul 19/2024");
		System.out.println("File transfer server is running on port: " + this.port);
	}
	
	private void init() throws Exception {
		listener = new ServerSocket(PORT);

		while (true) {
			serverSideSocket = listener.accept();

			protocol(serverSideSocket);
		}
	}
	
	public void protocol(Socket socket) throws Exception {
		
		Files.receiveFolder("Adios", "Server", socket);
		//Files.sendFile("CVF.pdf", socket);
		
		Files.sendFolder( "./Client","Hola", socket);
		
		
		System.out.println("Integridad archivos destino server:");
        Hasher.checkIntegrityFile("Server/Adios/hasherfile.txt");
	
	}
	
	public static void main(String[] args) throws Exception{

		FileTransferServer fts = null;
		if (args.length == 0) {
			fts = new FileTransferServer();
		} else {
			int port = Integer.parseInt(args[0]);
			fts = new FileTransferServer(port);
		}
		fts.init();	
		
		
	}
}
