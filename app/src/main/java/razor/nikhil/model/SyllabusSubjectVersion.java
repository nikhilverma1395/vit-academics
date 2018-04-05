package razor.nikhil.model;

/**
 * Created by Nikhil Verma on 11/7/2015.
 */
public class SyllabusSubjectVersion {
    private String type_POST;
    private String version_POST;
    private String academicCounsel;

    public String getAcademicCounsel() {
        return academicCounsel;
    }

    public void setAcademicCounsel(String academicCounsel) {
        this.academicCounsel = academicCounsel;
    }

    public String getVersion_POST() {
        return version_POST;
    }

    public void setVersion_POST(String version_POST) {
        this.version_POST = version_POST;
    }

    public String getType_POST() {
        return type_POST;
    }

    public void setType_POST(String type_POST) {
        this.type_POST = type_POST;
    }
}
