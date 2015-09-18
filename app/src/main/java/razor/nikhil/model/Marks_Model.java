package razor.nikhil.model;

/**
 * Created by Nikhil Verma on 7/28/2015.
 */
public class Marks_Model {
    private String no;
    private String classnbr;
    private String subcode;
    private long id;
    private String subname;
    private String subtype;
    private String CAT1;
    private String CAT1_Attended;
    private String CAT2;
    private String CAT2_Attended;
    private String QUIZ1;
    private String QUIZ1_Attended;
    private String QUIZ2;
    private String QUIZ2_Attended;
    private String QUIZ3;
    private String QUIZ3_Attended;
    private String ASIIGN;
    private String ASSIGN_Attended;
    private String LABCAM;
    private String LABCAM_Attended;

    public Marks_Model() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public Marks_Model(String no, String classnbr, String code, String subname, String subtype, String CAT1_Attended, String CAT1, String CAT2_Attended, String CAT2, String QUIZ1_Attended, String QUIZ1, String QUIZ2_Attended, String QUIZ2, String QUIZ3_Attended, String QUIZ3, String ASSIGN_Attended, String ASIIGN, String LABCAM_Attended, String LABCAM) {
        this.subcode = code;
        this.no = no;
        this.classnbr = classnbr;
        this.subname = subname;
        this.subtype = subtype;
        this.CAT1 = CAT1;
        this.CAT1_Attended = CAT1_Attended;
        this.CAT2 = CAT2;
        this.CAT2_Attended = CAT2_Attended;
        this.QUIZ1 = QUIZ1;
        this.QUIZ1_Attended = QUIZ1_Attended;
        this.QUIZ2 = QUIZ2;
        this.QUIZ2_Attended = QUIZ2_Attended;
        this.QUIZ3 = QUIZ3;
        this.QUIZ3_Attended = QUIZ3_Attended;
        this.ASIIGN = ASIIGN;
        this.ASSIGN_Attended = ASSIGN_Attended;
        this.LABCAM = LABCAM;
        this.LABCAM_Attended = LABCAM_Attended;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getClassnbr() {
        return classnbr;
    }

    public void setClassnbr(String classnbr) {
        this.classnbr = classnbr;
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

    public String getCAT1() {
        return CAT1;
    }

    public void setCAT1(String CAT1) {
        this.CAT1 = CAT1;
    }

    public String getCAT1_Attended() {
        return CAT1_Attended;
    }

    public void setCAT1_Attended(String CAT1_Attended) {
        this.CAT1_Attended = CAT1_Attended;
    }

    public String getCAT2() {
        return CAT2;
    }

    public void setCAT2(String CAT2) {
        this.CAT2 = CAT2;
    }

    public String getCAT2_Attended() {
        return CAT2_Attended;
    }

    public void setCAT2_Attended(String CAT2_Attended) {
        this.CAT2_Attended = CAT2_Attended;
    }

    public String getQUIZ1() {
        return QUIZ1;
    }

    public void setQUIZ1(String QUIZ1) {
        this.QUIZ1 = QUIZ1;
    }

    public String getQUIZ1_Attended() {
        return QUIZ1_Attended;
    }

    public void setQUIZ1_Attended(String QUIZ1_Attended) {
        this.QUIZ1_Attended = QUIZ1_Attended;
    }

    public String getQUIZ2() {
        return QUIZ2;
    }

    public void setQUIZ2(String QUIZ2) {
        this.QUIZ2 = QUIZ2;
    }

    public String getQUIZ2_Attended() {
        return QUIZ2_Attended;
    }

    public void setQUIZ2_Attended(String QUIZ2_Attended) {
        this.QUIZ2_Attended = QUIZ2_Attended;
    }

    public String getQUIZ3() {
        return QUIZ3;
    }

    public void setQUIZ3(String QUIZ3) {
        this.QUIZ3 = QUIZ3;
    }

    public String getQUIZ3_Attended() {
        return QUIZ3_Attended;
    }

    public void setQUIZ3_Attended(String QUIZ3_Attended) {
        this.QUIZ3_Attended = QUIZ3_Attended;
    }

    public String getASIIGN() {
        return ASIIGN;
    }

    public void setASIIGN(String ASIIGN) {
        this.ASIIGN = ASIIGN;
    }

    public String getASSIGN_Attended() {
        return ASSIGN_Attended;
    }

    public void setASSIGN_Attended(String ASSIGN_Attended) {
        this.ASSIGN_Attended = ASSIGN_Attended;
    }

    public String getLABCAM() {
        return LABCAM;
    }

    public void setLABCAM(String LABCAM) {
        this.LABCAM = LABCAM;
    }

    public String getLABCAM_Attended() {
        return LABCAM_Attended;
    }

    public void setLABCAM_Attended(String LABCAM_Attended) {
        this.LABCAM_Attended = LABCAM_Attended;
    }

}
