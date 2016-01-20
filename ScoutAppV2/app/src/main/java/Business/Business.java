package Business;

import java.util.ArrayList;

import DataAccess.DataAcess;
import galmapp.scoutappv2.Child;
import galmapp.scoutappv2.Reunion;

/**
 * Created by Arnaud on 30-10-15.
 */
public class Business {

    private DataAcess dataAccess;

    public Business()
    {
        dataAccess = new DataAcess();
    }






    public ArrayList<Child> getChildrenList ()
    {
        return dataAccess.getChildrenList();
    }

    public void addChildrenList(ArrayList<Child> childrenLst) {

        dataAccess.addChildrenList(childrenLst);
    }

}
