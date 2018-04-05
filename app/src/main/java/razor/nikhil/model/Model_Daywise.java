package razor.nikhil.model;

/**
 * Created by Nikhil Verma on 7/27/2015.
 */
public class Model_Daywise {
    private String subname;
    private String subcode;
    private String subslot;
    private String subtimings;
    private String venue;
    private String subtype;
    long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getSubtype() {
        return subtype;
    }

    public Model_Daywise() {
    }

    public Model_Daywise(String subname, String subcode, String subslot, String subtimings, String subteacher, String venue, String subtyp) {
        this.subname = subname;
        this.subcode = subcode;
        this.subslot = subslot;
        this.subtimings = subtimings;
        this.subteacher = subteacher;
        this.venue = venue;
        this.subtype = subtyp;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getSubtimings() {
        return subtimings;
    }

    public void setSubtimings(String subtimings) {
        this.subtimings = subtimings;
    }

    public String getSubteacher() {
        return subteacher;
    }

    public void setSubteacher(String subteacher) {
        this.subteacher = subteacher;
    }

    public String getSubslot() {
        return subslot;
    }

    public void setSubslot(String subslot) {
        this.subslot = subslot;
    }

    private String subteacher;
}
