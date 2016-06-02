package bacon.proj.bacon;

import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;

import bacon.proj.bacon.services.RestServices;

public class RestAsyncTask extends AsyncTask<Void, Void, String> {
    HashMap<String, String> params;
    Context ctx;
    RestAsyncTaskListener listener;

    public interface RestAsyncTaskListener{
        public void postExec(String response);
    }

    public RestAsyncTask(HashMap<String, String> params, Context ctx, RestAsyncTaskListener listener) {
        this.params = params;
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... param) {
        String response = RestServices.getInstance().callRest(ctx, params);
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.postExec(s);
    }
}
