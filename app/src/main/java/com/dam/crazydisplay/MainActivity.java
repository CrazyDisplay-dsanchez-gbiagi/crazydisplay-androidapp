package com.dam.crazydisplay;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AppData appData; // This is a singleton to save data between views
    ClientMessageControler clientMessageControler;
    ArrayList<String> messageListArray;
    private String ip;

    public MainActivity() {
        appData = AppData.getInstance();
        clientMessageControler = appData.getClientMessageControler();
        messageListArray = appData.getMessageListArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Select the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create variables to interact with the view
        EditText server_ip = findViewById(R.id.editIP);
        EditText mensaje = findViewById(R.id.editMensaje);
        TextView ipTitle = findViewById(R.id.textIP);
        TextView msgTitle = findViewById(R.id.textMensaje);
        Button botonEnviar = findViewById(R.id.buttonEnviar);
        Button botonConectar = findViewById(R.id.buttonConectar);
        Button botonLista = findViewById(R.id.listViewButton);
        botonLista.setBackgroundColor(Color.GRAY);

        // Check if there are a connection already up
        if (!appData.getConnected()) {
            botonEnviar.setVisibility(View.INVISIBLE);
            msgTitle.setVisibility(View.INVISIBLE);
            mensaje.setVisibility(View.INVISIBLE);
        }

        // Check if there are a login already up
        if (appData.getLogged()){
            botonConectar.setText("Desconectar");
            ipTitle.setText("Mensaje");
            server_ip.setHint("Introduce tu mensaje");
            botonLista.setBackgroundColor(Color.MAGENTA);
            botonEnviar.setVisibility(View.INVISIBLE);
            msgTitle.setVisibility(View.INVISIBLE);
            mensaje.setVisibility(View.INVISIBLE);
        } else {
            readMessageList(this, messageListArray);
        }

        // Listener of the button that change the view
        botonLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appData.getLogged()) {
                    Intent intent = new Intent(MainActivity.this, MessageListActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Listener of the button that send messages
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!appData.getConnected()) {
                    Log.println(Log.INFO, "Error", "No hay conexión");
                    Toast.makeText(MainActivity.this, "No estás conectado", Toast.LENGTH_SHORT).show();
                } else {
                    if (!appData.getLogged()) {
                        String usr = server_ip.getText().toString();
                        String psw = mensaje.getText().toString();
                        JSONObject objResponse = null;
                        try {
                            objResponse = new JSONObject("{}");
                            objResponse.put("type", "login");
                            objResponse.put("user", usr);
                            objResponse.put("password", psw);
                            appData.getClientMessageControler().sendMessage(objResponse.toString());
                            Thread.sleep(2000);
                            if (appData.getLogged()) {
                                mensaje.setText("");
                                msgTitle.setVisibility(View.INVISIBLE);
                                mensaje.setVisibility(View.INVISIBLE);
                                botonConectar.setVisibility(View.VISIBLE);
                                mensaje.setInputType((1));
                                ipTitle.setText("Mensaje");
                                server_ip.setHint("Introduce tu mensaje");
                                botonEnviar.setText("Enviar");
                                botonLista.setBackgroundColor(Color.MAGENTA);
                                server_ip.setText("");
                                Toast.makeText(MainActivity.this, "Sesion iniciada", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Usuario o contraseña erroneo", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        String msg = server_ip.getText().toString();
                        boolean saveMsg = true;
                        for (int i = 0 ; i < messageListArray.size() ; i++) {
                            if (msg.equals(messageListArray.get(i))) saveMsg = false;
                        }
                        if (saveMsg) {
                            messageListArray.add(msg);
                            saveMessageList(MainActivity.this, messageListArray);
                        }
                        JSONObject objResponse = null;
                        try {
                            objResponse = new JSONObject("{}");
                            objResponse.put("type", "message");
                            objResponse.put("format", "img");
                            objResponse.put("value", msg);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        clientMessageControler.sendMessage(objResponse.toString());
                        Log.println(Log.INFO, "Mensaje", msg);
                        Toast.makeText(MainActivity.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                        server_ip.setText("");
                        //serializeArrayList(MainActivity.this, messageListArray);
                    }
                }
            }
        });

        // Listener of the button that connect with the server
        botonConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!appData.getConnected()){
                    ip = server_ip.getText().toString();
                    clientMessageControler = connectWebSocket(ip);
                    clientMessageControler.connect();
                    appData.setClientMessageControler(clientMessageControler);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.println(Log.INFO, "IP", ip);
                    if (appData.getConnected()) {
                        botonEnviar.setVisibility(View.VISIBLE);
                        msgTitle.setVisibility(View.VISIBLE);
                        mensaje.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Esperando usuario y contraseña", Toast.LENGTH_SHORT).show();
                        botonConectar.setText("Desconectar");
                        server_ip.setText("");
                        server_ip.setHint("Introduce el usuario");
                        mensaje.setHint("Introduce la contraseña");
                        mensaje.setInputType((129));
                        botonEnviar.setText("Login");
                        ipTitle.setText("Usuario");
                        msgTitle.setText("Contraseña");
                        botonConectar.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(MainActivity.this, "La ip no es correcta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    clientMessageControler.close();
                    clientMessageControler = null;
                    appData.setConnected();
                    appData.setLogged();
                    Toast.makeText(MainActivity.this, "Desconectado", Toast.LENGTH_SHORT).show();
                    botonConectar.setText("Conectar");
                    botonLista.setBackgroundColor(Color.GRAY);
                    botonEnviar.setVisibility(View.INVISIBLE);
                    msgTitle.setVisibility(View.INVISIBLE);
                    mensaje.setVisibility(View.INVISIBLE);
                    ipTitle.setText("IP");
                    server_ip.setHint("Introduce la IP");
                }
            }
        });
    }

    // Method to connect with the server
    private ClientMessageControler connectWebSocket(String ip) {
        URI uri;
        try {
            uri = new URI("ws://"+ip+":8888/websocket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        return new ClientMessageControler(uri);
    }

    public void saveMessageList(Context context, ArrayList<String> messageListArray) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("msglist.txt", Context.MODE_PRIVATE);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            for (String msg : messageListArray) {
                bufferedWriter.write(msg);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            outputStreamWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessageList(Context context, ArrayList<String> messageListArray) {

        try {
            FileInputStream fileInputStream = context.openFileInput("msglist.txt");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                messageListArray.add(line);
            }

            bufferedReader.close();
            inputStreamReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}