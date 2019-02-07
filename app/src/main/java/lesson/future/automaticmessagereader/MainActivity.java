package lesson.future.automaticmessagereader;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements MessageListener{
    private static final int SEND_SMS_REQUEST_CODE = 1;

    private Button resetButton;
    private Button sendButton;
    private EditText receiverMessage;
    private EditText receiverNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        checkForSmsPermission();
        //Register sms listener
        MessageReceiver.bindListener(this);
        resetButton = findViewById(R.id.resetBtn);
        sendButton = findViewById(R.id.sendBtn);
        receiverNumber = findViewById(R.id.numberText);
        receiverMessage = findViewById(R.id.messageText);

        sendButton.setEnabled(false);
        if(checkPermissions(Manifest.permission.SEND_SMS)){
            sendButton.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_REQUEST_CODE);
        }
    }

    @Override
    public void messageReceived(String message) {
        Toast.makeText(this, "New Message Received: " + message, Toast.LENGTH_SHORT).show();
        receiverMessage.setText(message);
    }


    public void resetText(View view){
        receiverMessage.setText("This is where the message would be placed.");
    }

    public void sendMessage(View view){

        String number = receiverNumber.getText().toString();
        String message = receiverMessage.getText().toString();
        if(number == null || number.length() == 0 || message == null || message.length() == 0)
            return;

        if (checkPermissions(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean checkPermissions(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private void disableSmsButton() {
        sendButton.setVisibility(View.INVISIBLE);
    }

    private void enableSmsButton() {
        sendButton.setVisibility(View.VISIBLE);
    }
}
