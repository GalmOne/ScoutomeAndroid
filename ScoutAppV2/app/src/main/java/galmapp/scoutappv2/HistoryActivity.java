package galmapp.scoutappv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import Business.Business;
import DataAccess.MySingleton;


public class HistoryActivity extends AppCompatActivity {


    private ArrayList<Reunion> lstReunion = new ArrayList<Reunion>();
    private Gson gson = new Gson();
    TextView mTxtDisplay;
    private ListView listView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ProgressDialog dialog;

    public HistoryActivity() {

    }

    @Override
    protected void onCreate(Bundle onRestoreInstanceState) {
        super.onCreate(onRestoreInstanceState);
        setContentView(R.layout.activity_history);
        String chargement = getString(R.string.Chargement);
        dialog = ProgressDialog.show(HistoryActivity.this, "", chargement, true);


        listView = (ListView) findViewById(R.id.listReunion);


        // API

        mTxtDisplay = (TextView) findViewById(R.id.textView66);

        //RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        JsonArrayRequest requestObj = new JsonArrayRequest(Request.Method.GET, "http://scoutomev2.azurewebsites.net/api/reunions", new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {


                try {
                    String name="";
                    JSONArray jsarrayEnfant = new JSONArray();
                        for(int i = 0; i < response.length(); i++)
                        {
                            JSONObject jsobj = response.getJSONObject(i);
                            Reunion reu = new Reunion ();
                            ArrayList<Child> listChild = new ArrayList<Child>();

                            reu.setCodeReunion(jsobj.getInt("codeReunion"));
                            reu.setName(jsobj.getString("libelle"));
                            //Timestamp stamp = new Timestamp(jsobj.getString("dateReunion"));
                            //Date date1 = new Date(stamp.getTime());
                            reu.setDateString(jsobj.getString("dateReunion"));
                            reu.setPlace(jsobj.getString("lieu"));
                            reu.setPrice(jsobj.getDouble("prix"));


                            reu.setJsArrayString(jsobj.getJSONArray("presences").toString());

                            lstReunion.add(reu);
                        }

                    ArrayAdapter<Reunion> adapter = new ArrayAdapter<>(HistoryActivity.this, android.R.layout.simple_list_item_1, lstReunion);

                    listView.setAdapter(adapter);


                    dialog.hide();

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String ErreurReponseReunion = getString(R.string.ErreurReponseReunion);
                Toast.makeText(HistoryActivity.this, ErreurReponseReunion + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        requestObj.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(requestObj);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text

                Reunion reuSelected = (Reunion) parent.getItemAtPosition(position);

                Intent goToContent = new Intent(HistoryActivity.this, ContentHistoryActivity.class);
                goToContent.putExtra("ObjetReunion", reuSelected);
                startActivity(goToContent);


            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doFinish() {
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "History Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://galmapp.scoutappv2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "History Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://galmapp.scoutappv2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
