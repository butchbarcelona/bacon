package bacon.proj.bacon.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import bacon.proj.bacon.MainVoiceStopActivity;

/**
 * Created by tonnyquintos on 11/30/15.
 */
public class RestServices {


    private static RestServices instance;

    public static String TAG = "test";

    private RestServices(){

    }

    public static RestServices getInstance(){
        if(instance == null)
            instance = new RestServices();
        return instance;
    }


    SharedPreferences sharedpreferences;

    public String callRest(Context ctx, HashMap<String, String> postDataParams) {

        sharedpreferences = ctx.getSharedPreferences(MainVoiceStopActivity.TAG, Context.MODE_PRIVATE);

        String deviceId = sharedpreferences.getString("device_id", "2b003a000e47343432313031");
        String accessToken = sharedpreferences.getString("access_token", "d7d86a6fd5398c00e3e1adf8f4f76aa12c7a7bed");


        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            String strUrl = "https://api.particle.io/v1/devices/" +deviceId+
                    "/led?access_token="+accessToken;
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());


            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing API URL", e);
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to API", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return jsonResults.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

