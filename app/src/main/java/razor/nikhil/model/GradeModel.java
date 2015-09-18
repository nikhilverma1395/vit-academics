package razor.nikhil.model;

/**
 * Created by Nikhil Verma on 9/16/2015.
 */
public class GradeModel {
    private long id;
    private String subcode;
    private String subname;
    private String subtype;
    private String credit;
    private String grade;
    private String examheld;
    private String examresult;
    private String courseoption;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getExamheld() {
        return examheld;
    }

    public void setExamheld(String examheld) {
        this.examheld = examheld;
    }

    public String getExamresult() {
        return examresult;
    }

    public void setExamresult(String examresult) {
        this.examresult = examresult;
    }

    public String getCourseoption() {
        return courseoption;
    }

    public void setCourseoption(String courseoption) {
        this.courseoption = courseoption;
    }
}
