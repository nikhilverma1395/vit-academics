package razor.nikhil.model;

/**
 * Created by Nikhil Verma on 7/28/2015.
 */
public class DetailAtten {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String attend;
    private String attend_unit;
    private String no;
    private String day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    private String sub_code;

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getAttend() {
        return attend;
    }

    public void setAttend(String attend) {
        this.attend = attend;
    }

    public String getAttend_unit() {
        return attend_unit;
    }

    public void setAttend_unit(String attend_unit) {
        this.attend_unit = attend_unit;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
