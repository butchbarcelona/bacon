package bacon.proj.bacon;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import bacon.proj.bacon.services.RestServices;

public class MainVoiceStopActivity extends AppCompatActivity {

    public final static String TAG = "Bacon";

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        // hide the action bar
        getSupportActionBar().hide();

        promptSpeechInput();
        btnSpeak.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    HashMap<String,String> params = new HashMap<String, String>();

                    String command = result.get(0);
                    switch(result.get(0).toLowerCase()){
                        case "1":
                            command = "one";
                            break;
                        case "2":
                            command = "two";
                            break;
                        case "3":
                            command = "three";
                            break;
                        case "4":
                            command = "four";
                            break;
                        case "off":
                            command = "off";
                            break;
                        case "lights off":
                            command = "lights off";
                            break;
                        case "lights on":
                            command = "lights on";
                            break;
                    }

                    params.put("args", command);
                    txtSpeechInput.setText(command);

                    //String response = RestServices.getInstance().callRest(MainVoiceStopActivity.this, params);
                    new RestAsyncTask(params, MainVoiceStopActivity.this, new RestAsyncTask.RestAsyncTaskListener() {
                        @Override
                        public void postExec(String response) {
                            showSnackBarToast(MainVoiceStopActivity.this, "Server response: "+response);
                        }
                    }).execute();





                    //promptSpeechInput();
                }
                break;
            }

        }
    }

    public void showSnackBarToast(Context context, String message){
       /* int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            Snackbar.make(((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        } else*/{
            // do something for phones running an SDK before lollipop
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
