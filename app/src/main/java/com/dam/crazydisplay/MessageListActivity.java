package com.dam.crazydisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Record {
    public String textMessage;

    public Record(String _textMessage) {
        textMessage = _textMessage;
    }
}

public class MessageListActivity extends AppCompatActivity {
    AppData appData;
    ClientMessageControler clientMessageControler;
    ArrayList<MessageData> messageListArray;
    public MessageListActivity(){
        appData = AppData.getInstance(); // This is a singleton to save data between views
        clientMessageControler = appData.getClientMessageControler();
        messageListArray = appData.getMessageListArray();
    }
    protected void onCreate(Bundle savedInstanceState) {
        // Select the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        // Create variables to interact with the view
        Button buttonHome = findViewById(R.id.buttonHome);
        ArrayList<Record> records = new ArrayList<>();
        ArrayAdapter<Record> adapter;

        // Listener of the button that returns to Main
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Put all the messages in the Record
        for (MessageData msg : messageListArray) {
            records.add(new Record(msg.getMessage()));
        }

        adapter = new ArrayAdapter<Record>(this, R.layout.list_item, records ) {
            public View getView(int pos, View convertView, ViewGroup container) {
                // getView ens construeix el layout i hi "pinta" els valors de l'element en la posició pos
                if( convertView==null ) {
                    // inicialitzem l'element la View amb el seu layout
                    convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
                }
                ((TextView) convertView.findViewById(R.id.textMessage)).setText(getItem(pos).textMessage);
                ((Button) convertView.findViewById(R.id.buttonSendMessage)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MessageListActivity.this);
                        builder.setTitle("Confirmación")
                                .setMessage("¿Estás seguro de realizar esta acción?")
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String msg = getItem(pos).textMessage;
                                        JSONObject objResponse = null;
                                        try {
                                            objResponse = new JSONObject("{}");
                                            objResponse.put("type", "message");
                                            objResponse.put("format", "text");
                                            objResponse.put("value", msg);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        clientMessageControler.sendMessage(objResponse.toString());
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });

                return convertView;
            }
        };
        // busquem la ListView i li endollem el ArrayAdapter
        ListView lv = (ListView) findViewById(R.id.messageView);
        lv.setAdapter(adapter);
    }
}