package razor.nikhil.model;

/**
 * Created by Nikhil Verma on 9/4/2015.
 */

public class AttenDetail {
    private String from_date;
    private String to_date;
    private String class_number;
    private String semcode;

    public String getClass_number() {
        return class_number;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }

    public String getSemcode() {
        return semcode;
    }

    public void setSemcode(String semcode) {
        this.semcode = semcode;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }


    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }
}
