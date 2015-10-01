package razor.nikhil.model;

/**
 * Created by Nikhil Verma on 10/1/2015.
 */
public class AptModel {
    public AptModel() {
    }
private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    String date;
    String session;
    String units;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}

