package knowmemoAPI;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ShiangChiLee on 2016/3/13.
 */
public class knowmemoAsyncTask extends AsyncTask<String , Void , String> {

    private knowmemoCallBackListener callback;
    private int httpResponseCode = -1;


    knowmemoAsyncTask(knowmemoCallBackListener callback)
    {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            URL url = new URL(params[0]);
            JSONObject header = new JSONObject(params[1]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");

            Log.i("knowmemoAPI" , "dest: " + url.toString() + " header : " + header.toString());


            String postData = params[2];
            Log.i("knowmemoAPI", "postData : " + postData);
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream() , "UTF-8");
            os.write(postData);
            os.flush();
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            StringBuffer response = new StringBuffer();

            while((line = reader.readLine()) != null)
            {
                response.append(line);
            }
            reader.close();
            Log.i("knowmemoAPI" , "response : " + response.toString());
            httpResponseCode = conn.getResponseCode();

            return response.toString();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        try
        {
            JSONObject json = new JSONObject(result);
            callback.onSuccess(json);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
