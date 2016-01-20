package DataAccess;

import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import galmapp.scoutappv2.Child;
import galmapp.scoutappv2.MainActivity;
import galmapp.scoutappv2.Reunion;

/**
 * Created by Arnaud on 30-10-15.
 */
public class DataAcess {

    private static ArrayList<Reunion> ReunionList = new ArrayList<Reunion>();
    private static ArrayList<Child> childrenList = new ArrayList<Child>();
    static int i = 0;






    public ArrayList<Child> getChildrenList ()
    {
        return childrenList;
    }

    public void addChildrenList(ArrayList<Child> childrenLst)
    {
        childrenList = childrenLst;
    }



}
