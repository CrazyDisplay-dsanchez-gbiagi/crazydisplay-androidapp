package com.dam.crazydisplay;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

public class ClientMessageControler extends WebSocketClient {
    AppData appData;
    public ClientMessageControler(URI serverUri) {
        super(serverUri);
        appData = AppData.getInstance();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        appData.setConnected();
    }

    @Override
    public void onMessage(String message) {
        try {
            JSONObject objRequest = new JSONObject(message);
            String type = objRequest.getString("type");

            if (type.equals("login")) {
                if (objRequest.getBoolean("valid")) {
                    JSONObject objResponse = new JSONObject("{}");
                    objResponse.put("type", "platform");
                    objResponse.put("name", "Android");
                    sendMessage(objResponse.toString());
                    appData.setLogged();
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
    public void onClose(int i, String s, boolean b) {
        // Connection closed
        System.out.println("Conexion cerrada");
    }

    @Override
    public void onError(Exception e) {
        // Connection error
        Log.e("ConnError", "La conexion fallo");
    }

    public void sendMessage(String message) {
        send(message);
    }

}
