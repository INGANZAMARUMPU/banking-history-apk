package bi.konstrictor.bankhistory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText field_login_username, field_login_password;
    private ProgressBar login_progress;
    private SharedPreferences sessionPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        field_login_username = findViewById(R.id.field_login_username);
        field_login_password = findViewById(R.id.field_login_password);
        login_progress = findViewById(R.id.login_progress);

        sessionPreference = getSharedPreferences("user_session", MODE_PRIVATE);

        if(Host.isLogedIn(this)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void performLogin(View view) {
        login_progress.setVisibility(View.VISIBLE);
        String json =
                "{" +
                    "\"username\":\""+field_login_username.getText() +"\", " +
                    "\"password\":\""+field_login_password.getText()
                +"\"}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL+"/login/").newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Erreur de Connexion", Toast.LENGTH_LONG).show();
                        login_progress.setVisibility(View.GONE);
                    }
                });
                Log.i("==== LOGIN ====", e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new  JSONObject(json);
                    String token = jsonObject.getString("access");
                    String refresh = jsonObject.getString("refresh");
                    SharedPreferences.Editor session = sessionPreference.edit();
                    session.putString("token", token);
                    session.putString("refresh", refresh);
                    session.commit();
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            login_progress.setVisibility(View.GONE);
                        }
                    });

                } catch (Exception e) {
                    final String message = e.getMessage();
                    Log.i("==== LOGIN ====", e.getMessage());
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "incorrect logins", Toast.LENGTH_LONG).show();
                            login_progress.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }
}
