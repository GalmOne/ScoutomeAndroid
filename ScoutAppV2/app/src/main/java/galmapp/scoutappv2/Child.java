package galmapp.scoutappv2;

/**
 * Created by Arnaud on 19-10-15.
 */
public class Child {

    private int codeAnime;
    private String name;
    private String firstname;


    public Child (String n, String f)
    {
        name = n;
        firstname = f;

    }

    public Child ()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public String toString ()
    {
        return name + " " + firstname;
    }

    public int getCodeAnime() {
        return codeAnime;
    }

    public void setCodeAnime(int codeAnime) {
        this.codeAnime = codeAnime;
    }
}
