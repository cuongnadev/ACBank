package com.example.javafx.Server;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThreadBus {
    private final List<ServerThread> serverThreadList;


    public ServerThreadBus(){
        serverThreadList = new ArrayList<>();
    }

    public List<ServerThread> getServerThreadList(){
        return serverThreadList;
    }

    // Add Server Thread into List.
    public void addServerThread(ServerThread serverThread){
        serverThreadList.add(serverThread);
    }

    // Get the size of the serverList.
    public int getServerThreadListSize(){
        return serverThreadList.size();
    }

    //remove
    public void removeServerThread(String clientID){
        for(int i = 0; i < BankServer.serverThreadBus.getServerThreadListSize(); i++){
            if(BankServer.serverThreadBus.getServerThreadList().get(i).getClientID().equals(clientID)){
                BankServer.serverThreadBus.serverThreadList.remove(i);
            }
        }
    }

    public synchronized void notifyClient(String clientId, String message) {
        for(ServerThread serverThread : BankServer.serverThreadBus.getServerThreadList()){
            if(serverThread.getClientID().equals(clientId)){
                try{
                    System.out.println("ok");
                    serverThread.writeMessage(message);
                    break;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }



}
