package razor.nikhil.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Verma on 7/26/2015.
 */
public class Slots_Timings {
    public class Time_Code {
        private String time;
private String lab_slot;
private String lab_time;

        public void setLab_slot(String lab_slot) {
            this.lab_slot = lab_slot;
        }

        public void setLab_time(String lab_time) {
            this.lab_time = lab_time;
        }

        public String getTime() {
            return time;
        }

        public String getLab_time() {
            return lab_time;
        }

        public String getLab_slot() {
            return lab_slot;
        }

        public String getSlot() {
            return slot;
        }

        public void setTime(String time) {
            this.time = time;
        }

        private String slot;

        public void setSlot(String slot) {
            this.slot = slot;
        }
    }

    public List<Time_Code> getTimings(String day) {
        List<Time_Code> list = new ArrayList<>();
        if (day == "mon") {
            Time_Code rt = new Time_Code();
            rt.setTime("8:00am - 8:50am");
            rt.setLab_slot("L1+L2");
            rt.setLab_time("8:00am - 10:00am");
            rt.setSlot("A1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("9:00am - 9:50am");
            rt.setSlot("F1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("10:00am - 10:50am");
            rt.setSlot("C1");
            rt.setLab_slot("L3+L4");
            rt.setLab_time("10:00am - 12:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("11:00am - 11:50am");
            rt.setSlot("E1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("12:00pm - 12:50pm");
            rt.setSlot("TD1");
            rt.setLab_slot("L5+L6");
            rt.setLab_time("11:50am - 1:30pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("2:00pm - 2:50pm");
            rt.setSlot("A2");
            rt.setLab_slot("L31+L32");
            rt.setLab_time("2:00pm - 4:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("3:00pm - 3:50pm");
            rt.setSlot("F2");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("4:00pm - 4:50pm");
            rt.setSlot("C2");
            rt.setLab_slot("L33+L34");
            rt.setLab_time("4:00pm - 6:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("5:00pm - 5:50pm");
            rt.setSlot("E2");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("6:00pm - 6:50pm");
            rt.setSlot("TD2");
            rt.setLab_slot("L35+L36");
            rt.setLab_time("5:50pm - 7:30pm");
            list.add(rt);
            return list;
        }
        if (day == "tue") {
            Time_Code rt = new Time_Code();
            rt.setTime("8:00am - 8:50am");
            rt.setSlot("B1");
            rt.setLab_slot("L7+L8");
            rt.setLab_time("8:00am - 10:00am");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("9:00am - 9:50am");
            rt.setSlot("G1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("10:00am - 10:50am");
            rt.setSlot("D1");

            rt.setLab_slot("L9+L10");
            rt.setLab_time("10:00am - 12:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("11:00am - 11:50am");
            rt.setSlot("TA1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("12:00pm - 12:50pm");
            rt.setSlot("TF1");

            rt.setLab_slot("L11+L12");
            rt.setLab_time("11:50am - 1:30pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("2:00pm - 2:50pm");
            rt.setSlot("B2");

            rt.setLab_slot("L37+L38");
            rt.setLab_time("2:00pm - 4:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("3:00pm - 3:50pm");
            rt.setSlot("G2");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("4:00pm - 4:50pm");
            rt.setSlot("D2");

            rt.setLab_slot("L39+L40");
            rt.setLab_time("4:00pm - 6:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("5:00pm - 5:50pm");
            rt.setSlot("TA2");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("6:00pm - 6:50pm");
            rt.setSlot("TF2");

            rt.setLab_slot("L41+L42");
            rt.setLab_time("5:50pm - 7:30pm");
            list.add(rt);
            return list;
        }
        if (day == "wed") {
            Time_Code rt = new Time_Code();
            rt.setTime("8:00am - 8:50am");
            rt.setSlot("C1");

            rt.setLab_slot("L13+L14");
            rt.setLab_time("8:00am - 10:00am");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("9:00am - 9:50am");
            rt.setSlot("F1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("10:00am - 10:50am");
            rt.setSlot("E1");

            rt.setLab_slot("L15+L16");
            rt.setLab_time("10:00am - 12:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("11:00am - 11:50am");
            rt.setSlot("TB1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("12:00pm - 12:50pm");
            rt.setSlot("TG1");

            rt.setLab_slot("L17+L18");
            rt.setLab_time("11:50am - 1:30pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("2:00pm - 2:50pm");
            rt.setSlot("C2");

            rt.setLab_slot("L43+L44");
            rt.setLab_time("2:00pm - 4:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("3:00pm - 3:50pm");
            rt.setSlot("F2");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("4:00pm - 4:50pm");
            rt.setSlot("E2");

            rt.setLab_slot("L45+L46");
            rt.setLab_time("4:00pm - 6:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("5:00pm - 5:50pm");
            rt.setSlot("TB2");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("6:00pm - 6:50pm");
            rt.setSlot("TG2");

            rt.setLab_slot("L47+L48");
            rt.setLab_time("5:50pm - 7:30pm");
            list.add(rt);
            return list;
        }
        if (day == "thur") {
            Time_Code rt = new Time_Code();
            rt.setTime("8:00am - 8:50am");
            rt.setSlot("D1");

            rt.setLab_slot("L19 + L20");
            rt.setLab_time("8:00am - 10:00am");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("9:00am - 9:50am");
            rt.setSlot("A1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("10:00am - 10:50am");
            rt.setSlot("F1");
            rt.setLab_slot("L21 + L22");
            rt.setLab_time("10:00am - 12:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("11:00am - 11:50am");
            rt.setSlot("C1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("12:00pm - 12:50pm");
            rt.setSlot("TE1");

            rt.setLab_slot("L23 + L24");
            rt.setLab_time("11:50am - 1:30pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("2:00pm - 2:50pm");
            rt.setSlot("D2");

            rt.setLab_slot("L49 + L50");
            rt.setLab_time("2:00pm - 4:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("3:00pm - 3:50pm");
            rt.setSlot("A2");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("4:00pm - 4:50pm");
            rt.setSlot("F2");

            rt.setLab_slot("L51 + L52");
            rt.setLab_time("4:00pm - 6:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("5:00pm - 5:50pm");
            rt.setSlot("C2");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("6:00pm - 6:50pm");
            rt.setLab_slot("L53 + L54");
            rt.setLab_time("5:50pm - 7:30pm");
            rt.setSlot("TE2");
            list.add(rt);
            return list;
        }
        if (day == "fri") {
            Time_Code rt = new Time_Code();
            rt.setTime("8:00am - 8:50am");
            rt.setSlot("E1");

            rt.setLab_slot("L25 + L26");
            rt.setLab_time("8:00am - 10:00am");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("9:00am - 9:50am");
            rt.setSlot("B1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("10:00am - 10:50am");

            rt.setLab_slot("L27 + L28");
            rt.setLab_time("10:00am - 12:00pm");
            rt.setSlot("G1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("11:00am - 11:50am");
            rt.setSlot("D1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("12:00pm - 12:50pm");
            rt.setLab_slot("L29 + L30");
            rt.setLab_time("11:50am - 1:30pm");
            rt.setSlot("TC1");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("2:00pm - 2:50pm");
            rt.setSlot("E2");
            rt.setLab_slot("L55 + L56");
            rt.setLab_time("2:00pm - 4:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("3:00pm - 3:50pm");
            rt.setSlot("B2");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("4:00pm - 4:50pm");
            rt.setSlot("G2");

            rt.setLab_slot("L57 + L58");
            rt.setLab_time("4:00pm - 6:00pm");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("5:00pm - 5:50pm");
            rt.setSlot("D2");
            list.add(rt);
            rt=null;
            rt=new Time_Code();
            rt.setTime("6:00pm - 6:50pm");
            rt.setSlot("TC2");

            rt.setLab_slot("L59 + L60");
            rt.setLab_time("5:50pm - 7:30pm");
            list.add(rt);
            return list;
        }
        return null;
    }
}
