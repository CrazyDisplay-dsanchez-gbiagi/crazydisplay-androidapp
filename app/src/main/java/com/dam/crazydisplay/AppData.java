package com.dam.crazydisplay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AppData {
    private static final AppData instance = new AppData();
    private ClientMessageControler clientMessageControler;
    private ArrayList<MessageData> messageListArray;
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

    public ArrayList<MessageData> getMessageListArray() {
        return messageListArray;
    }

    public String getCurrentDateTimeString() {
        // Obtener la fecha y hora actual
        Date currentDate = new Date();

        // Definir el formato deseado (en este caso, "yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Formatear la fecha y hora actual como una cadena
        return dateFormat.format(currentDate);
    }
}
