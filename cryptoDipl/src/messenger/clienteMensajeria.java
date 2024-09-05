package messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import util.Util;

public class clienteMensajeria {
    public static final String SERVER = "localhost";
    public static final int PORT = 3400;

    private static final Scanner SCANNER = new Scanner(System.in);

    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;

    private Socket clientSideSocket;

    private String server;
    private int port;

    private Map<String, String> publickeys = new HashMap<>();

    public clienteMensajeria() {
        this.server = SERVER;
        this.port = PORT;
        System.out.println("Johan A. Ospina - Jul 19/2024");
        System.out.println("Messenger client is running ... connecting to server in " + this.server + ":" + this.port);
    }

    public clienteMensajeria(String server, int port) {
        this.server = server;
        this.port = port;
        System.out.println("Johan A. Ospina - Jul 19/2024");
        System.out.println("Messenger client is running ... connecting to server in " + this.server + ":" + this.port);
    }

    private void createStreams(Socket socket) throws IOException {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void processCommand(String command) throws IOException {
        toNetwork.println(command);
        String response;
        while ((response = fromNetwork.readLine()) != null) {
            System.out.println("[Client] From server: " + response);
            if (response.equals("END")) {
                break;
            }
        }
    }

    public void protocol() throws Exception {
        
        createStreams(clientSideSocket);
        boolean running = true;

        while (running) { 
            System.out.print("Enter command: ");
            String line = SCANNER.nextLine();

            String[] parts = line.split(" ", 3);
            String command = parts[0];
            String username;

            switch (command) {
                case "REGISTER":

                    processCommand(line);

                    String split[] = line.split(" ");
                    String userAux = split[1];
                    byte[] userbyteaux = Util.objectToByteArray(userAux);
                    String userB64Aux = util.Base64.encode(userbyteaux);
                    

                    publickeys.put(userAux, userB64Aux);
                    
                    break;
                    
                case "GETKEY":
                    username = parts[1];
                    if (publickeys.containsKey(username)) {
                        System.out.println("[Client] Llave publica ya almacenada de: "+ username);
                    } else {

                        toNetwork.println(line);

                        String response = fromNetwork.readLine();
                        String splitRespondse[] = response.split(" ");

                        if (response != null && response.startsWith("Llave pública de " + username + ": ")) {
                            String publicKey = splitRespondse[4];
                            publickeys.put(username, publicKey);
                            System.out.println("[Client] Llave guardada.");
                        } else {
                            System.out.println("[Client] Error al obtener la llave pública.");
                        }
                    }
                    break;

                case "SEND":   
                   
                    String command1 = parts[0]; 
                    username = parts[1];
                    String msg =  parts[2];
                   
                    if (publickeys.containsKey(username)) {
                        System.out.println("[Client] Mensaje a enviar: " + msg);

                        String encryptedString = util.Util.encrypt(msg, publickeys.get(username));
                        System.out.println("[Client] Encrypt message: " + encryptedString);
                        toNetwork.println(command1+" "+username+" "+encryptedString);
                    } else {
                        System.out.println("[Client] El Usuario"+ username +" no existe");
                    }
                   break;
               case "READ":
                    username = parts[1];
                    String aux;
                    toNetwork.println(line);
                    while ((aux = fromNetwork.readLine()) != null) {
                        
                        
                        if (aux.equals("END")) { 
                        } else if (aux.contains("Mensaje enviado a")) {
                        } else if (aux.contains("El usuario")) {
                            System.out.println("[Cliente] Received message: " + aux);
                        } else {
                            try {
                                System.out.println("[Client] Received message: " + aux);
                                String decryptString = util.Util.decrypt(aux, publickeys.get(username));
                                System.out.println("[Client] Decrypt message: " + decryptString);
                            } catch (Exception e) {
                                System.err.println("Decryption error: " + e.getMessage());
                                e.printStackTrace();
                            }
                            if (aux.equals("END")) {
                                break;
                            }
                        }
                        
                    }
                    break;
               case "EXIT":
                   running = false;
                   toNetwork.println("EXIT");
                   break;
               default:
                   break;
            }
        }
    }

    public void init() throws Exception {
        clientSideSocket = new Socket(this.server, this.port);
        protocol();
        clientSideSocket.close();
    }

    public static void main(String[] args) throws Exception {
        clienteMensajeria client;
        if (args.length == 0) {
            client = new clienteMensajeria();
        } else {
            String server = args[0];
            int port = Integer.parseInt(args[1]);
            client = new clienteMensajeria(server, port);
        }
        client.init();
    }
}
