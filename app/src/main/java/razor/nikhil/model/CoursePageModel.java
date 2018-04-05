package razor.nikhil.model;

/**
 * Created by Nikhil Verma on 11/15/2015.
 */

public class CoursePageModel {
    String type;
    String teacher;
    String slot;
    String crsplancode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getCrsplancode() {
        return crsplancode;
    }

    public void setCrsplancode(String crsplancode) {
        this.crsplancode = crsplancode;
    }
}
