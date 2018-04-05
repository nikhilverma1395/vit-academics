package razor.nikhil.model;

/**
 * Created by Nikhil Verma on 9/27/2015.
 */
public class MyTeacherDet {
    public MyTeacherDet() {
    }

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String NAME;
    private String SCHOOL;
    private String DESIGNATION;
    private String ROOM;
    private String EMAIL;
    private String DIVISION;
    private String ADDROLE;
    private String OPENHRS;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSCHOOL() {
        return SCHOOL;
    }

    public void setSCHOOL(String SCHOOL) {
        this.SCHOOL = SCHOOL;
    }

    public String getDESIGNATION() {
        return DESIGNATION;
    }

    public void setDESIGNATION(String DESIGNATION) {
        this.DESIGNATION = DESIGNATION;
    }

    public String getROOM() {
        return ROOM;
    }

    public void setROOM(String ROOM) {
        this.ROOM = ROOM;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getDIVISION() {
        return DIVISION;
    }

    public void setDIVISION(String DIVISION) {
        this.DIVISION = DIVISION;
    }

    public String getADDROLE() {
        return ADDROLE;
    }

    public void setADDROLE(String ADDROLE) {
        this.ADDROLE = ADDROLE;
    }

    public String getOPENHRS() {
        return OPENHRS;
    }

    public void setOPENHRS(String OPENHRS) {
        this.OPENHRS = OPENHRS;
    }
}
