package bi.konstrictor.bankhistory;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ResourceBundle;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Host {
//    public static String URL = "http://192.168.1.2:80";
//    public static String URL = "https://bank.so-mas.net";
    public static String URL = "http://10.0.2.2:8000";
    private static SharedPreferences sessionPreference;

    public static boolean isLogedIn(Context context){
        sessionPreference = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sessionPreference.getString("token", "");
        return !(token.trim().isEmpty());
    }

    public static String getSessionValue(Context context, String name){
        sessionPreference = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String value = sessionPreference.getString(name, "");
        return value.trim();
    }

    public static void logOut(Activity context){
        sessionPreference = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor session = sessionPreference.edit();
        session.clear();
        session.commit();
        context.finish();
    }
    public static void refreshToken(Context context){
        String json = "{\"refresh\":\""+getSessionValue(context, "refresh") +"\"}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL+"/refresh/").newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("==== HOST ====", e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new  JSONObject(json);
                    Log.i("==== NEW TOKEN ====", json);
                    String token = jsonObject.getString("access");
                    SharedPreferences.Editor session = sessionPreference.edit();
                    session.putString("token", token);
                    session.commit();
                    Log.i("==== NEW TOKEN ====", token);
                } catch (Exception e) {
                    Log.i("==== HOST ====", e.getMessage());
                }
            }
        });
    }
}
