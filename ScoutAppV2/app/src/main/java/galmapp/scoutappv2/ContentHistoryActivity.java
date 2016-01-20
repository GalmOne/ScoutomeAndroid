package galmapp.scoutappv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Business.Business;

public class ContentHistoryActivity extends AppCompatActivity {

    private TextView textDate, textPlace, textPrice;
    private Business business = new Business ();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String,List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_history);

        Intent intent = getIntent();
        Reunion reuReceived = (Reunion)intent.getSerializableExtra("ObjetReunion");



        textDate = (TextView) findViewById(R.id.textViewDate);
        textPlace = (TextView) findViewById(R.id.textViewPlace);
        textPrice = (TextView) findViewById(R.id.textViewPrice);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String dateString = reuReceived.getDateString();
        String dateString8 = dateString.substring(8, 10);
        dateString8 += "/"+dateString.substring(5, 7);
        dateString8 += "/"+dateString.substring(0, 4);
        textDate.setText(dateString8);
        textPlace.setText(reuReceived.getPlace());
        textPrice.setText(String.valueOf(reuReceived.getPrice()) + " euros");

        //TRT JSonARRAY ENFANTS PRESENTS
        List<String> listEnfPresent = new ArrayList<String>() ;
        List<String> EnfantsAbsents = new ArrayList<String>();
        ArrayList<Child> listPresent = new ArrayList<Child>();
        try{
            JSONArray jsArrayEnfant = new JSONArray(reuReceived.getJsArrayString());


            for (int j = 0; j < jsArrayEnfant.length(); j++)
            {
                JSONObject jsobjEnf = jsArrayEnfant.getJSONObject(j).getJSONObject("anime");
                listEnfPresent.add(jsobjEnf.getString("prenom")+" "+jsobjEnf.getString("nom"));
                Child c = new Child ();
                c.setCodeAnime(jsobjEnf.getInt("codeAnime"));
                c.setFirstname(jsobjEnf.getString("prenom"));
                c.setName(jsobjEnf.getString("nom"));
                listPresent.add(c);
            }
        }
        catch(JSONException e)
        {
            Toast.makeText(ContentHistoryActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        ArrayList<Child> listAbsents = new ArrayList<Child>();
        for(int o = 0; o< business.getChildrenList().size(); o++)
        {
            Child ch = new Child();
            ch.setCodeAnime(business.getChildrenList().get(o).getCodeAnime());
            ch.setFirstname(business.getChildrenList().get(o).getFirstname());
            ch.setName(business.getChildrenList().get(o).getName());

            listAbsents.add(ch);
        }


        //Trt enfants absents

        for(int k = 0; k < listPresent.size(); k++)
        {

            for(int u = 0; u < listAbsents.size(); u++)
            {
                if(listPresent.get(k).getCodeAnime() == listAbsents.get(u).getCodeAnime())
                {
                    listAbsents.remove(u);
                }
            }
        }



        for(int o = 0; o<listAbsents.size(); o++)
        {
            EnfantsAbsents.add(listAbsents.get(o).getFirstname()+" "+listAbsents.get(o).getName());
        }

        expListView = (ExpandableListView) findViewById(R.id.listEnfantPresent);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        String enfantPresents = getString(R.string.EnfantPresent);
        listDataHeader.add(enfantPresents);
        String enfantAbsent = getString(R.string.EnfantAbsent);
        listDataHeader.add(enfantAbsent);

        listDataChild.put(listDataHeader.get(0), listEnfPresent); // Header, Child data
        listDataChild.put(listDataHeader.get(1), EnfantsAbsents);

        listAdapter = new ExpandableListAdapter(this,listDataHeader,listDataChild);

        expListView.setAdapter(listAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content_history, menu);
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


}
