package com.example.mqtt_p5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    TextView text_StatusLed;
    ImageButton button_Up;
    ImageButton button_Down;
    ImageButton button_Pause;
    Button button_Lock;

    MqttAndroidClient client;
    String TAG= "Check ";
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
        pub("qn052289@gmail.com/ControlLed", "P");
        text_StatusLed.setText("PAUSE");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        pub("qn052289@gmail.com/ControlLed", "P");
        text_StatusLed.setText("PAUSE");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int[] flag = {1};
        button_Up = (ImageButton) findViewById((R.id.btn_Up));
        button_Down = (ImageButton) findViewById((R.id.btn_Down));
        button_Pause = (ImageButton) findViewById((R.id.btn_Pause));
        button_Lock = (Button) findViewById(R.id.btn_Lock);
        text_StatusLed = (TextView) findViewById(R.id.txt_StatusLed);


        button_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag[0] == 1) pub("qn052289@gmail.com/ControlLed", "U");
            }
        });
        button_Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View view) {
                if(flag[0] == 1) pub("qn052289@gmail.com/ControlLed", "D");
            }
        });
        button_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag[0] == 1) pub("qn052289@gmail.com/ControlLed", "P");
            }
        });
        button_Lock.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                    flag[0] = 0;
                    pub("qn052289@gmail.com/ControlLed", "P");
                    button_Lock.setText("Unlock");
                    text_StatusLed.setText("Locked !");

            }
        });
        button_Lock.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                flag[0] = 1;
                pub("qn052289@gmail.com/ControlLed", "P");
                button_Lock.setText("Lock");
                text_StatusLed.setText("Unlocked !");
                return false;
            }
        });

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://www.maqiatto.com:1883", clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("qn052289@gmail.com");
        options.setPassword("182739".toCharArray());


        Log.d(TAG,"Before try");
        try {
            Log.d(TAG,"Before connect");
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    sub("qn052289@gmail.com/Status", 2);
                    pub("qn052289@gmail.com/ControlLed", "P");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    void pub(String topic, String content) {
        String payload = content;
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    void sub(String topic, int qos)
    {
        try {
            client.subscribe(topic, qos);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("SUB: ", "Lost connect!!!");
                    text_StatusLed.setText("Lost Connect!!!");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    byte[] messRecived = message.getPayload();
                    String statusLed = new String(messRecived);
                    text_StatusLed.setText(statusLed);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d("SUB: ", "Delivery done!");
                }
            });
        }catch (MqttException e){
            e.printStackTrace();
        }
    }


}