package com.example.progres_bar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    MqttAndroidClient client;
    SeekBar sk_Red;
    SeekBar sk_Green;
    SeekBar sk_Blue;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sk_Red = (SeekBar) findViewById(R.id.seek_Red);
        sk_Green= (SeekBar) findViewById(R.id.seek_Green);
        sk_Blue = (SeekBar) findViewById(R.id.seek_Blue);

        //event for Red
        sk_Red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("RED: ", "Giatri: " + i);
                pub("qn052289@gmail.com/RGB_Red", String.valueOf(i));
                Log.d("Red", "Gia tri gui di: " + String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //event for Green
        sk_Green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("Green: ", "Giatri: " + i);
                pub("qn052289@gmail.com/RGB_Green", String.valueOf(i));
                Log.d("Green", "Gia tri gui di: " + String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //event for Blue
        sk_Blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("Blue: ", "Giatri: " + i);
                pub("qn052289@gmail.com/RGB_Blue", String.valueOf(i));
                Log.d("Blue", "Gia tri gui di: " + String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://www.maqiatto.com:1883", clientId );
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("qn052289@gmail.com");
        options.setPassword("182739".toCharArray());

        Log.d("Check", "Before try");
        try
        {
            Log.d("Check", "Before connect");
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("Check", "Success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("Check", "Fail");
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



}