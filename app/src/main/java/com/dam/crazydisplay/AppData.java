package com.dam.crazydisplay;

import java.util.ArrayList;

public class AppData {
    private static final AppData instance = new AppData();
    private ClientMessageControler clientMessageControler;
    private ArrayList<String> messageListArray;
    private boolean connected;
    private boolean logged;

    private AppData() {
        clientMessageControler = null;
        messageListArray = new ArrayList<>();
        connected = false;
        logged = false;
    }

    public void setConnected() {
        connected = !connected;
    }
    public boolean getConnected() {
        return connected;
    }
    public void setLogged() {
        logged = !logged;
    }
    public boolean getLogged() {
        return logged;
    }
    public static AppData getInstance() {
        return instance;
    }

    public ClientMessageControler getClientMessageControler() {
        return clientMessageControler;
    }

    public void setClientMessageControler(ClientMessageControler clientMessageControler) {
        this.clientMessageControler = clientMessageControler;
    }

    public ArrayList<String> getMessageListArray() {
        return messageListArray;
    }
}
