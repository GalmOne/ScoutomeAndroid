package galmapp.scoutappv2;

import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import DataAccess.MySingleton;
import Business.*;

public class MainActivity extends AppCompatActivity {

    private static boolean init = true;
    private ArrayList<Child> lstChild = new ArrayList<Child>();
    private Business business = new Business();

    SectionsPagerAdapter mSectionsPagerAdapter;
    static int cpt = 0 ;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(init)
            initChild ();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            switch(position){

                case 0 : return  PlaceholderFragment.newInstance(position + 1);
                case 1 : return FragmentRight.newInstance();
                //default: return PlaceholderFragment.newInstance(position + 1);
/* It is better to use default so that it always returns a fragment and no problems would ever occur */
            }
            return null;

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Button historyBut, childrenBut, settingBut;

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            historyBut = (Button) rootView.findViewById(R.id.buttonHistory);
            historyBut.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    Intent goToHistory = new Intent(getActivity(), HistoryActivity.class);
                    startActivity(goToHistory);
                }
            });

            childrenBut = (Button) rootView.findViewById(R.id.buttonChildren);
            childrenBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent goToChildren = new Intent(getActivity(), ChildrenActivity.class); // Changer la classe a appellé

                    startActivity(goToChildren);

                }
            });


            return rootView;
        }
    }

    public void initChild ()
    {
        init = false;
        String chargement = getString(R.string.Chargement);
        ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", chargement , true);
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        JsonArrayRequest requestObj = new JsonArrayRequest(Request.Method.GET, "http://scoutomev2.azurewebsites.net/api/animes", new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {

                try {
                    String name="";
                    JSONArray jsarrayEnfant = new JSONArray();
                    for(int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsobj = response.getJSONObject(i);
                        Child child = new Child ();
                        ArrayList<Child> listChild = new ArrayList<Child>();

                        child.setCodeAnime(jsobj.getInt("codeAnime"));
                        child.setName(jsobj.getString("nom"));
                        child.setFirstname(jsobj.getString("prenom"));

                        lstChild.add(child);
                    }
                    business.addChildrenList(lstChild);
                    finish();
                    startActivity(getIntent());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String erreurRepAnime = getString(R.string.ErrorConnexion);
                Toast.makeText(MainActivity.this, erreurRepAnime, Toast.LENGTH_SHORT).show();

                openAlert();
            }
        });

        requestObj.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(requestObj);



    }
    private void openAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        String errorInternet = getString(R.string.ErrorConnexion);
        alertDialogBuilder.setMessage(errorInternet);
        alertDialogBuilder.setNeutralButton(R.string.exit, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                // exit the app and go to the HOME
                MainActivity.this.finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }
}
