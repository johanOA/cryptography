package util;

import java.lang.Thread;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Files {
	
	
	public static void sendFolder(String routefolder, String foldername, Socket socket) throws Exception {
		
		File folder1 = new File(foldername);
		File newFolder = new File(routefolder, foldername);

		if (newFolder.mkdirs()) {
			System.out.println("Creado exitosamente.");
		} else {
			System.out.println("No se pudo crear");
		}

		if(folder1.exists() && folder1.isDirectory()) {
			File[] files = folder1.listFiles();

			if (files != null) {
			System.out.println("Número de archivos en la carpeta: " + files.length);
				for (File file : files) {
					sendFile(foldername+"/"+file.getName(), socket);
					System.out.println("Envia: " + file.getName());
				}
			}
		}
	}

	public static void sendEncryptedFolder(String routefolder, String foldername, Socket socket) throws Exception {
		
		File folder1 = new File(foldername);
		File newFolder = new File(routefolder, foldername);

		if (newFolder.mkdirs()) {
			System.out.println("Creado exitosamente.");
		} else {
			System.out.println("No se pudo crear");
		}

		if(folder1.exists() && folder1.isDirectory()) {
			File[] files = folder1.listFiles();

			if (files != null) {
			System.out.println("Número de archivos en la carpeta: " + files.length);
				for (File file : files) {
					if (file.isFile() && file.getName().endsWith(".encrypted")) {
						sendFile(foldername+"/"+file.getName(), socket);
						System.out.println("Envia: " + file.getName());	
					}
				}
			}
		}
	}

	public static void receiveFolder(String foldername, String folder, Socket socket) throws Exception {
		
		File folder1 = new File(foldername);

		File[] files = folder1.listFiles();

			for (File file : files) {

				receiveFile(folder+"/"/*foldername*/, socket);
			}
		
	}
	

	public static void sendFile(String filename, Socket socket) throws Exception {
		System.out.println("File to send: " + filename);
		File localFile = new File(filename);
		BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(localFile));

		// send the size of the file (in bytes)
		long size = localFile.length();
		System.out.println("Size: " + size);

		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
		printWriter.println(filename);

		printWriter.println("Size:" + size);

		BufferedOutputStream toNetwork = new BufferedOutputStream(socket.getOutputStream());

		pause(50);
		
		// the file is sent one block at a time
		byte[] blockToSend = new byte[1024];
		int in;
		while ((in = fromFile.read(blockToSend)) != -1) {
			toNetwork.write(blockToSend, 0, in);
		}
		// the stream linked to the socket is flushed and closed
		toNetwork.flush();
		fromFile.close();

		pause(50);
	}

	public static String receiveFile(String folder, Socket socket) throws Exception {
		File fd = new File (folder);
		if (fd.exists()==false) {
			fd.mkdir();
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		BufferedInputStream fromNetwork = new BufferedInputStream(socket.getInputStream());

		String filename = reader.readLine();
		filename = folder + File.separator + filename;

		BufferedOutputStream toFile = new BufferedOutputStream(new FileOutputStream(filename));

		System.out.println("File to receive: " + filename);

		String sizeString = reader.readLine();

		// the sender sends "Size:" + size, so here it is separated
		// long size = Long.parseLong(sizeString.subtring(5));
		long size = Long.parseLong(sizeString.split(":")[1]); 
		System.out.println("Size: " + size);
		
		// the file is received one block at a time
		byte[] blockToReceive = new byte[512];
		int in;
		long remainder = size; // lo que falta
		while ((in = fromNetwork.read(blockToReceive)) != -1) {
			toFile.write(blockToReceive, 0, in);
			remainder -= in;
			if (remainder == 0)
				break;
		}

		pause(50);
		
		// the stream linked to the file is flushed and closed
		toFile.flush();
		toFile.close();
		System.out.println("File received: " + filename);

		return filename;
	}

	public static void pause(int miliseconds) throws Exception {
		Thread.sleep(miliseconds);
	}
}