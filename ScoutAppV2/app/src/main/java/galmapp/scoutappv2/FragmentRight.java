package galmapp.scoutappv2;


import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import Business.Business;
import DataAccess.MySingleton;

public class FragmentRight extends Fragment {


    private CheckBox checkChild;
    private Child childS;
    private TextView dateField;
    private EditText nameField, priceField, placeField;
    private Button clear, save;
    private Business business;
    private HistoryActivity history;
    private ArrayList<Child> childrenLst = new ArrayList<Child>();
    private ListView listView;
    private RequestQueue requestQueue ;
    private Date date;
    private ArrayList<Child> listPresent;
    private int uniqueID;
    private ProgressDialog dialog;

    public static FragmentRight newInstance() {
        FragmentRight fragmentRight = new FragmentRight();
        return fragmentRight;
    }

    public FragmentRight() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_right, container, false);

        business = new Business();
        history = new HistoryActivity();
        listPresent = new ArrayList<Child>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();

        String currentDate = dateFormat.format(date);
        dateField = (TextView) rootView.findViewById(R.id.DateText);
        dateField.setText(currentDate);

        String reuniondu = getString(R.string.Reuniondu);
        String defaultName = reuniondu+currentDate;
        nameField = (EditText) rootView.findViewById(R.id.editName);
        nameField.setText(defaultName);

        priceField = (EditText) rootView.findViewById(R.id.editPrice);
        placeField = (EditText) rootView.findViewById(R.id.editPlace);

        requestQueue = MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        childrenLst = business.getChildrenList();


        listView = (ListView) rootView.findViewById(R.id.listView1);
        ArrayAdapter<Child> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, childrenLst);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);


        uniqueID = date.getYear()*100 + date.getMonth() + date.getDay() + date.getHours() + date.getMinutes() + date.getSeconds();

         // Boutton save pour enregistrer la réunion
        save = (Button) rootView.findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
                // Récupération de tous les champs
                String name = nameField.getText().toString();

                String place = placeField.getText().toString();
                if(place.equals(""))
                    place = "-";

                int price;
                if(priceField.getText().length() == 0)
                    price = 1;
                else
                   price = Integer.parseInt(priceField.getText().toString());

                Reunion newR = new Reunion (name, date, place, price);


                SparseBooleanArray checked = new SparseBooleanArray();
                checked = listView.getCheckedItemPositions();


                for(int i = 0; i < childrenLst.size(); i++){
                    if(checked.get(i))
                    {
                        listPresent.add(childrenLst.get(i));
                    }
                }

                JSONObject jsobjReuNew = new JSONObject();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                try{

                    jsobjReuNew.put("codeReunion", uniqueID);
                    jsobjReuNew.put("libelle", newR.getName());
                    jsobjReuNew.put("dateReunion", formatter.format(date));
                    jsobjReuNew.put("lieu", newR.getPlace());
                    jsobjReuNew.put("prix", newR.getPrice());

                }
                catch(JSONException e)
                {
                    String erreurConversionReunion = getString(R.string.erreurConversionReunion);
                    Toast.makeText(getActivity(), erreurConversionReunion, Toast.LENGTH_SHORT).show();
                }




                JsonObjectRequest requestReunion = new JsonObjectRequest(Request.Method.POST, "http://scoutomev2.azurewebsites.net/api/reunions",jsobjReuNew, new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {


                        addReunionToChild();
                        getActivity().finish();
                        getActivity().startActivity(getActivity().getIntent());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String erreurReponse = getString(R.string.erreurReponse);
                        Toast.makeText(getActivity(), erreurReponse + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
                requestReunion.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(requestReunion);




            }

        });

        //Boutoton Reset ( x ) pour réinitialiser tous les champs.
        clear = (Button) rootView.findViewById(R.id.buttonClear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
                getActivity().startActivity(getActivity().getIntent());


            }
            });

        return rootView;
    }

    public void addReunionToChild ()
    {
        JSONArray jsArrayPresence = new JSONArray();
        JSONObject jsobjPresence = new JSONObject();
        try{
            for(int i = 0; i< listPresent.size(); i++)
            {
                jsobjPresence.put("codeAnime", listPresent.get(i).getCodeAnime());
                jsobjPresence.put("codeReunion", uniqueID);
                jsobjPresence.put("useless", 1);

                JsonObjectRequest requestPresence = new JsonObjectRequest(Request.Method.POST, "http://scoutomev2.azurewebsites.net/api/presences",jsobjPresence, new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String echecPresence = getString(R.string.echecPresence);
                        Toast.makeText(getActivity(), echecPresence + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
                requestPresence.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(requestPresence);
            }
            String presenceAdd = getString(R.string.PresenceAdd);
            Toast.makeText(getActivity(), presenceAdd, Toast.LENGTH_LONG).show();
            dialog.hide();
        }
        catch(JSONException er)
        {

        }


    }















}


