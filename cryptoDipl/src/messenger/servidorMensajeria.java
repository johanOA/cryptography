package messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.Files;
import util.Util;

public class servidorMensajeria {
    public static final int PORT = 3400;
    private ServerSocket listener;
    private Map<String, String> users = new HashMap<>();
    private Map<String, ArrayList<String>> email = new HashMap<>();
     
    

    public servidorMensajeria() {
        System.out.println("Johan A. Ospina - Jul 19/2024");
        System.out.println("Messenger server is running on port: " + PORT);
    }

    private void handleClient(Socket clientSocket) throws Exception {
        try (
            PrintWriter toNetwork = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader fromNetwork = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String message;
            while ((message = fromNetwork.readLine()) != null) {
                System.out.println("[Server] Received: " + message);
                String[] parts = message.split(" ", 3);
                String command = parts[0];
                String username, response;

                switch (command) {
                    case "REGISTER":
                        username = parts[1];
                        ArrayList<String> userMessages = new ArrayList<>();

                        if (users.containsKey(username)) {
                            response = "El usuario " + username + " ya está registrado.";
                        } else {
                            byte[] userbyte = Util.objectToByteArray(username);
                            String userB64 = util.Base64.encode(userbyte);
                            users.put(username, userB64);
                            email.put(username, userMessages);
                            response = "Bienvenido " + username;
                        }
                        toNetwork.println(response);
                        break;
                    case "GETKEY":
                        username = parts[1];
                        
                        if (users.containsKey(username)) {
                            response = "Llave pública de " + username + ": " + users.get(username);
                        } else {
                            response = "ERROR: el usuario " + username + " no está registrado.";
                        }
                        toNetwork.println(response);
                        break;
                    case "SEND":
                        username = parts[1];
                        String msg = parts[2];
                        
                        if (users.containsKey(username) && email.containsKey(username)) {
                            response = "Mensaje enviado a " + username;
                            // Obtener el ArrayList existente y agregar el nuevo mensaje
                            ArrayList<String> getuserMessages = email.get(username);
                            getuserMessages.add(msg);
                        } else {
                            response = "ERROR: el usuario "+username+" no esta registrado";
                        }
                        
                        toNetwork.println(response);
                        break;
                    case "READ":
                        username = parts[1];
                        int count = 0;
                        if (users.containsKey(username)) {
                            ArrayList<String> getuserMessages2 = email.get(username); 
                            if (getuserMessages2 == null || getuserMessages2.isEmpty()) {
                                response = "El usuario "+username+" tiene " +count+" mensajes";
                            } else {
                                for (String aux : getuserMessages2) {
                                    toNetwork.println(aux);
                                    Files.pause(200);
                                    count++;
                                }
                                response = "El usuario " +username+" tiene " +count+" mensajes.";
                            }   
                        } else {
                            response = "ERROR. El usuario" + username + " no esta registrado.";
                        }
                        toNetwork.println(response);
                        toNetwork.println("END");
                        break;
                    default:
                        response = "Unknown command";
                        toNetwork.println(response);
                        break;
                }
                toNetwork.println("END");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        listener = new ServerSocket(PORT);
        System.out.println("Server is listening on port " + PORT);

        while (true) {
            Socket clientSocket = listener.accept();
            new Thread(() -> {
                try {
                    handleClient(clientSocket);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static void main(String[] args) throws Exception {
        servidorMensajeria server = new servidorMensajeria();
        server.init();
    }
    public void printMessages() {
        // Imprimir todos los mensajes
        for (Map.Entry<String, ArrayList<String>> entry : email.entrySet()) {
            System.out.println("User: " + entry.getKey());
            for (String msg : entry.getValue()) {
                System.out.println("Message: " + msg);
            }
        }
    }
}
