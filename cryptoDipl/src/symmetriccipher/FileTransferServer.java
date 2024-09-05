package symmetriccipher;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

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
		
		//SymmetricCipher.encryptFolder("./Server", "Adios", "./Adios/secretKey1.key");

		List<String> fileNames1 = hasher.getFileNames("Hola");
		
		Hasher.generateIntegrityCheckerFile("./Hola/", "hasherfile.txt", fileNames1);

		Files.pause(200);

		SymmetricCipher.encryptFolder("./Client", "Hola", "./Hola/secretKey2.key");
		
		Files.sendEncryptedFolder( "./Client","Hola", socket);
		
		Files.sendFile("./Hola/secretkey2.key", socket);

		SymmetricCipher.decryptFolder("Client/Hola", "./Client/Hola/secretkey2.key");

		System.out.println("Integridad archivos:");
        Hasher.checkIntegrityFile("Client/Hola/hasherfile.plain.txt");
	
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
