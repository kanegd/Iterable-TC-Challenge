package com.example.garrettkaneiterabletcchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iterable.iterableapi.IterableApi;
import com.iterable.iterableapi.IterableConfig;
import com.iterable.iterableapi.IterableInAppManager;
import com.iterable.iterableapi.IterableInAppMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialize UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //debugging output
        TextView output = findViewById(R.id.Output);
        output.setText("initialized");

        //initialize Iterable SDK
        String userEmail = "twistedcircuits99@gmail.com";
        IterableConfig config = new IterableConfig.Builder().build();
        IterableApi.initialize(getApplicationContext(),"349dcc9373c74c6699c5d1204a271695",config);
        IterableApi APIinstance = IterableApi.getInstance();
        APIinstance.setEmail(userEmail);
        output.append(" | API Initialized");

        //Button variables
        Button updateProfile = findViewById(R.id.UpdateProfile);
        Button secretCode = findViewById(R.id.SecretCode);
        Button showMessage = findViewById(R.id.ShowMessage);

        //button listeners
        updateProfile.setOnClickListener(v -> {
            try {
                updateProfile(v,userEmail,APIinstance);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        secretCode.setOnClickListener(v -> {
            try {
                sendSecretCode(v,APIinstance);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        showMessage.setOnClickListener(v -> {showMessage(v,APIinstance);});
    }

    public void updateProfile(View view, String email,IterableApi APIinstance) throws JSONException {
        //execute on button press
        TextView output = findViewById(R.id.Output);
        output.append(" | Button Pressed");

        JSONObject userData = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("firstName", "Garrett");
        data.put("isRegisteredUser", true);
        data.put("SA_User_Test_Key", "completed");
        userData.put("email", email);
        userData.put("dataFields", data);
        output.append(userData.toString());
        APIinstance.updateUser(userData);
        output.append(" | Finished");
    }

    public void sendSecretCode(View view, IterableApi APIinstance) throws JSONException {
        TextView output = findViewById(R.id.Output);
        output.append(" | Secret Code Button");

        String eventName = "mobileSATestEvent";
        JSONObject eventData = new JSONObject();
        eventData.put("platform", "Android");
        eventData.put("isTestEvent", true);
        eventData.put("url", "https://iterable.com/sa-test/Garrett");
        eventData.put("secret_code_key","Code_123");
        output.append(eventData.toString());
        APIinstance.track(eventName,eventData);
    }

    public void showMessage(View view, IterableApi APIinstance){
        TextView output = findViewById(R.id.Output);
        output.append(" | Messages");

        //get messages
        IterableInAppManager inAppManager = APIinstance.getInAppManager();
        List<IterableInAppMessage> messages = inAppManager.getMessages();

        //check if message exists
        if(messages.isEmpty()){
            output.append(" | No Messages");
            return;
        }

        //display messages
        for(IterableInAppMessage message: messages) {
            inAppManager.showMessage(message);
        }
    }
}