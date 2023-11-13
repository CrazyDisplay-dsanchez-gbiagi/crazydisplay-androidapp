package com.dam.crazydisplay;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private WebSocketClient WebSocketClient;
    private String ip;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText server_ip = findViewById(R.id.editIP);
        EditText mensaje = findViewById(R.id.editMensaje);

        // Botón para enviar el mensaje
        Button botonEnviar = findViewById(R.id.buttonEnviar);
        Button botonConectar = findViewById(R.id.buttonConectar);

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mensaje.getText().toString();
                sendMessage(msg);
                Log.println(Log.INFO, "Mensaje", msg);
            }
        });
        botonConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip = server_ip.getText().toString();
                connectWebSocket(ip);
                Log.println(Log.INFO, "IP", ip);
            }
        });
    }
    private void connectWebSocket(String ip) {
        URI uri;
        try {
            uri = new URI("ws://"+ip+":8888/websocket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        WebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                // Connection opened
                sendMessage("~AndroidClient");
            }

            @Override
            public void onMessage(String s) {
                System.out.println("Mensaje recibido: " + s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                // Connection closed
                System.out.println("Conexion cerrada");
            }

            @Override
            public void onError(Exception e) {
                // Connection error
                System.out.println("OnError");
                e.printStackTrace();
            }
        };
        WebSocketClient.connect();
    }
    private void sendMessage(String message) {
        WebSocketClient.send(message);
    }
}