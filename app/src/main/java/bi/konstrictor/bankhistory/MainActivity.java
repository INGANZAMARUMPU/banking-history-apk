package bi.konstrictor.bankhistory;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Menu menu;
    private TextView lbl_main_acc_num, lbl_main_acc_mount;
    private RecyclerView recycler_operations;
    ArrayList<Action> actions;
    private String account_number;
    private AdaptateurAction adaptateur;
    SwipeRefreshLayout swipe_main_refresh;
    private boolean retrait = true, depot = true;
    private long date_de = 1577884268/* 01 jan 2020 */, date_a = System.currentTimeMillis()/1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lbl_main_acc_num = findViewById(R.id.lbl_main_acc_num);
        lbl_main_acc_mount = findViewById(R.id.lbl_main_acc_mount);
        recycler_operations = findViewById(R.id.recycler_operations);
        swipe_main_refresh = findViewById(R.id.swipe_main_refresh);

        recycler_operations.setLayoutManager(new GridLayoutManager(this, 1));
        recycler_operations.addItemDecoration(new DividerItemDecoration(recycler_operations.getContext(), DividerItemDecoration.VERTICAL));
//        recycler_operations.setLayoutManager(new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP));

        actions = new ArrayList<>();
        adaptateur = new AdaptateurAction(MainActivity.this, actions);
        recycler_operations.setAdapter(adaptateur);

        if(!Host.isLogedIn(this)){
            Host.logOut(this);
        }
        getAccountInfo(false);
        getActions(false);
        swipe_main_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActions(false);
            }
        });
    }

    private void getActions(final boolean refreshed) {
        actions.clear();
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL+"/action/myactions").newBuilder();
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + Host.getSessionValue(this, "token"))
                .get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipe_main_refresh.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Erreur de Connexion", Toast.LENGTH_LONG).show();
                    }
                });
                Log.i("==== MAINACTIVITY ====", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONArray json_array = new JSONArray(json);
                    for (int i=0; i<json_array.length(); i++){
                        JSONObject json_object = json_array.getJSONObject(i);
                        actions.add(new Action(
                                json_object.getString("id"),
                                json_object.getString("solde"),
                                json_object.getString("str_montant"),
                                json_object.getString("motif"),
                                json_object.getString("date")
                        ));
                    }
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipe_main_refresh.setRefreshing(false);
                            adaptateur.notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                    if(!refreshed) {
                        Host.refreshToken(MainActivity.this);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getActions(true);
                            }
                        });
                    }else {
                        final String message = e.getMessage();
                        Log.i("==== MAIN ACTIVITY ====", e.getMessage());
                        e.printStackTrace();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipe_main_refresh.setRefreshing(false);
                                Toast.makeText(MainActivity.this, "Erreur de chargement de votre historique", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void getAccountInfo(final boolean refreshed) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL+"/account/myaccount/").newBuilder();
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + Host.getSessionValue(this, "token"))
                .get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Erreur de Connexion", Toast.LENGTH_LONG).show();
                    }
                });
                Log.i("==== MAIN ACTIVITY ====", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.i("==== MAIN ACTIVITY ====", json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    final String number = jsonObject.getString("number");
                    final String balance = jsonObject.getString("balance");
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lbl_main_acc_num.setText(number);
                            lbl_main_acc_mount.setText(balance);
                            account_number = number;
                        }
                    });

                } catch (Exception e) {
                    if(!refreshed) {
                        Host.refreshToken(MainActivity.this);
                        getAccountInfo(true);
                    }
                    final String message = e.getMessage();
                    Log.i("==== MAIN ACTIVITY ====", e.getMessage());
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lbl_main_acc_num.setText("invalid");
                            lbl_main_acc_mount.setText("invalid");
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu_filter, menu);
        this.menu = menu;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_date) {
            DateForm date_form = new DateForm(this);
            date_form.show();
        }else if(id == R.id.action_logout){
            Host.logOut(this);
        }else if(id == R.id.action_operation){
            ActionsForm action_form = new ActionsForm(this);
            action_form.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isRetrait() {
        return retrait;
    }

    public void setRetrait(boolean retrait) {
        this.retrait = retrait;
    }

    public boolean isDepot() {
        return depot;
    }

    public void setDepot(boolean depot) {
        this.depot = depot;
    }

    public long getDate_de() {
        return date_de;
    }

    public void setDate_de(long date_de) {
        this.date_de = date_de;
    }

    public long getDate_a() {
        return date_a;
    }

    public void setDate_a(long date_a) {
        this.date_a = date_a;
    }

    public void filterMovment(){
        ArrayList<Action> filtered_actions = new ArrayList<>();
        ArrayList<Action> displayed_actions = adaptateur.getActions();

        if(this.retrait == this.depot){
            filtered_actions.addAll(actions);
        }else if (this.retrait) {
            for (Action action : displayed_actions){
                if (action.is_retrait){
                    filtered_actions.add(action);
                }
            }
        }else {
            for (Action action : displayed_actions) {
                if(!action.is_retrait){
                    filtered_actions.add(action);
                }
            }
        }
        adaptateur.setActions(filtered_actions);
        adaptateur.notifyDataSetChanged();
    }
    public void filterDate(){
        ArrayList<Action> filtered_actions = new ArrayList<>();
        ArrayList<Action> displayed_actions = adaptateur.getActions();
        for (Action action : displayed_actions){
            Log.i("==== DATE ====", date_de+" "+date_a+" "+action.getTime());
            Log.i("==== DATE ====", Boolean.toString(action.getTime()>=date_de));
            Log.i("==== DATE ====", Boolean.toString(action.getTime()<=date_a));
            if ((action.getTime()>=date_de) & (action.getTime()<=date_a)){
                filtered_actions.add(action);
            }
        }
        adaptateur.setActions(filtered_actions);
        adaptateur.notifyDataSetChanged();
    }
    public void refresh(){
        adaptateur.setActions(actions);
        adaptateur.notifyDataSetChanged();
    }
}
