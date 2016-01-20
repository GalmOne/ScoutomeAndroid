package galmapp.scoutappv2;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Created by Arnaud on 23-10-15.
 */
public class Reunion implements Serializable{

    private int codeReunion;
    private String name;
    private Date date;
    private String place;
    private double price ;
    private int codeSection;
    private String jsArrayString;
    private String dateString;



    public Reunion (String n, Date d, String p, double pr)
    {
        name = n ;
        date = d;
        place = p;
        price = pr;
    }

    public Reunion ()
    {

    }

    public int getCodeSection() {
        return codeSection;
    }

    public void setCodeSection(int codeSection) {
        this.codeSection = codeSection;
    }

    public int getCodeReunion() {
        return codeReunion;
    }

    public void setCodeReunion(int codeReunion) {
        this.codeReunion = codeReunion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString ()
    {
        return name;
    }

    public String getJsArrayString() {
        return jsArrayString;
    }

    public void setJsArrayString(String jsArrayString) {
        this.jsArrayString = jsArrayString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}


