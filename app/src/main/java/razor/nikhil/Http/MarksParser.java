package razor.nikhil.Http;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.database.CBL_Get_Set;
import razor.nikhil.database.PBL_Get_Set;
import razor.nikhil.model.Marks_Model;
import razor.nikhil.model.PBL_Model;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class MarksParser {
    private Context context;

    public MarksParser(Context context, String marksHTML) {
        this.context = context;
        parse(marksHTML);
    }

    public void parse(String data) {
        Document doc = Jsoup.parse(data);
        Elements ele = doc.getElementsByTag("table");
        Elements ele2 = ele.get(1).getElementsByTag("tr");
        Elements pblfinal = null, pbl = null, pbl1 = null, pbl2 = null, pbl3 = null, pbl4 = null, pbl5 = null;
        try {
            pblfinal = ele.get(2).getElementsByTag("tr");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pbl = pblfinal;
            Elements temp = new Elements();
            for (int i = 1; i <= 7; i++) {
                temp.add(pbl.get(i));
            }
            pbl = temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pbl1 = pblfinal;
            Elements temp = new Elements();
            for (int i = 8; i < 15; i++) {
                temp.add(pbl1.get(i));
            }
            pbl1 = temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pbl2 = pblfinal;
            Elements temp = new Elements();
            for (int i = 15; i < 22; i++) {
                temp.add(pbl2.get(i));
            }
            pbl2 = temp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            pbl3 = pblfinal;
            Elements temp = new Elements();
            for (int i = 22; i < 29; i++) {
                temp.add(pbl3.get(i));
            }
            pbl3 = temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            pbl4 = pblfinal;
            Elements temp = new Elements();
            for (int i = 29; i < 36; i++) {
                temp.add(pbl4.get(i));
            }
            pbl4 = temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            pbl5 = pblfinal;
            Elements temp = new Elements();
            for (int i = 36; i < 43; i++) {
                temp.add(pbl5.get(i));
            }
            pbl5 = temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<List<Elements>> pbldetman = new ArrayList<>();
        List<Elements> pbldet;
        if (pbl != null) {
            pbldet = new ArrayList<>();
            for (Element dr : pbl) {
                pbldet.add(dr.getElementsByTag("td"));
            }
            pbldetman.add(pbldet);
        }
        if (pbl1 != null) {
            pbldet = new ArrayList<>();
            for (Element dr : pbl1) {
                pbldet.add(dr.getElementsByTag("td"));
            }
            pbldetman.add(pbldet);
        }
        if (pbl2 != null) {
            pbldet = new ArrayList<>();
            for (Element dr : pbl2) {
                pbldet.add(dr.getElementsByTag("td"));
            }
            pbldetman.add(pbldet);
        }
        if (pbl3 != null) {
            pbldet = new ArrayList<>();
            for (Element dr : pbl3) {
                pbldet.add(dr.getElementsByTag("td"));
            }
            pbldetman.add(pbldet);
        }
        if (pbl4 != null) {
            pbldet = new ArrayList<>();
            for (Element dr : pbl4) {
                pbldet.add(dr.getElementsByTag("td"));
            }
            pbldetman.add(pbldet);
        }
        if (pbl4 != null) {
            pbldet = new ArrayList<>();
            for (Element dr : pbl5) {
                pbldet.add(dr.getElementsByTag("td"));
            }
            pbldetman.add(pbldet);
        }
        List<PBL_Model> pbl_lists;
        if (pbldetman.size() > 0) {
            pbl_lists = parseandPutPBL(pbldetman);
        } else {
            Log.d("Courses", "No PBL Courses");
            pbl_lists = null;
        }
        final List<PBL_Model> lo = pbl_lists;

        try {
            final PBL_Get_Set PGS = new PBL_Get_Set(context);
            PGS.Delete();
            if (lo != null) {
                PGS.create(lo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Elements> marks_indiv = new ArrayList<>();
        for (int wr = 2; wr < ele2.size(); wr++) {
            Elements e = ele2.get(wr).getElementsByTag("td");
            marks_indiv.add(e);
        }

        final List<Marks_Model> marks_det = new ArrayList<>();
        for (Elements d : marks_indiv) {
            if (d.get(4).html().contains("Theory")) {
                Marks_Model mm = new Marks_Model(
                        d.get(0).html().trim(),//Sl
                        d.get(1).html().trim(),//Class Nbr
                        d.get(2).html().trim(),//Code
                        d.get(3).html().trim(),//Sub Name
                        d.get(4).html().trim(),//Type
                        d.get(5).html().trim(),//Attend c1
                        d.get(6).html().trim(),//marks c1
                        d.get(7).html().trim(),
                        d.get(8).html().trim(),
                        d.get(9).html().trim(),
                        d.get(10).html().trim(),
                        d.get(11).html().trim(),
                        d.get(12).html().trim(),
                        d.get(13).html().trim(),
                        d.get(14).html().trim(),
                        d.get(15).html().trim(),
                        d.get(16).html().trim(),
                        "-",
                        "-");
                marks_det.add(mm);
            } else {
                Marks_Model mm = new Marks_Model(
                        d.get(0).html().trim(),
                        d.get(1).html().trim(),
                        d.get(2).html().trim(),
                        d.get(3).html().trim(),
                        d.get(4).html().trim(),
                        "-",
                        "-",
                        "-",
                        "-",
                        "-",
                        "-",
                        "-",
                        "-",
                        "-",
                        "-",
                        "-",
                        "-",
                        d.get(6).html().trim(),
                        d.get(7).html().trim());
                marks_det.add(mm);
            }

        }
        final CBL_Get_Set wer = new CBL_Get_Set(context);
        wer.Delete();
        wer.create(marks_det);
    }

    private List<PBL_Model> parseandPutPBL(List<List<Elements>> pbldeta) {
        List<PBL_Model> lis = new ArrayList<>();
        for (List<Elements> pbldet : pbldeta) {
            PBL_Model pb = new PBL_Model();
            pb.setClas_nbr(pbldet.get(0).get(1).html().trim());
            pb.setCourse_code(pbldet.get(0).get(2).html().trim());

            pb.setOption1_Title(ParseTimeTable.FirstCharCap(pbldet.get(0).get(6).html().trim()));
            pb.setOption2_Title(ParseTimeTable.FirstCharCap(pbldet.get(0).get(7).html().trim()));
            pb.setOption3_Title(ParseTimeTable.FirstCharCap(pbldet.get(0).get(8).html().trim()));
            pb.setOption4_Title(ParseTimeTable.FirstCharCap(pbldet.get(0).get(9).html().trim()));
            pb.setOption5_Title(ParseTimeTable.FirstCharCap(pbldet.get(0).get(10).html().trim()));

            pb.setOption1_Max_Mark(pbldet.get(1).get(1).html().trim());
            pb.setOption2_Max_Mark(pbldet.get(1).get(2).html().trim());
            pb.setOption3_Max_Mark(pbldet.get(1).get(3).html().trim());
            pb.setOption4_Max_Mark(pbldet.get(1).get(4).html().trim());
            pb.setOption5__Max_Mark(pbldet.get(1).get(5).html().trim());

            pb.setOption1_Weightage(pbldet.get(2).get(1).html().trim());
            pb.setOption2_Weightage(pbldet.get(2).get(2).html().trim());
            pb.setOption3_Weightage(pbldet.get(2).get(3).html().trim());
            pb.setOption4_Weightage(pbldet.get(2).get(4).html().trim());
            pb.setOption5_Weightage(pbldet.get(2).get(5).html().trim());

            pb.setOption1_Date(pbldet.get(3).get(1).html().trim());
            pb.setOption2_Date(pbldet.get(3).get(2).html().trim());
            pb.setOption3_Date(pbldet.get(3).get(3).html().trim());
            pb.setOption4_Date(pbldet.get(3).get(4).html().trim());
            pb.setOption5_Date(pbldet.get(3).get(5).html().trim());

            pb.setOption1_Attend(pbldet.get(4).get(1).html().trim());
            pb.setOption2_Attend(pbldet.get(4).get(2).html().trim());
            pb.setOption3_Attend(pbldet.get(4).get(3).html().trim());
            pb.setOption4_Attend(pbldet.get(4).get(4).html().trim());
            pb.setOption5_Attend(pbldet.get(4).get(5).html().trim());

            pb.setOption1_Scored(pbldet.get(5).get(1).html().trim());
            pb.setOption2_Scored(pbldet.get(5).get(2).html().trim());
            pb.setOption3_Scored(pbldet.get(5).get(3).html().trim());
            pb.setOption4_Scored(pbldet.get(5).get(4).html().trim());
            pb.setOption5_Scored(pbldet.get(5).get(5).html().trim());

            pb.setOption1_Scored_Percent(pbldet.get(6).get(1).html().trim());
            pb.setOption2_Scored_Percent(pbldet.get(6).get(2).html().trim());
            pb.setOption3_Scored_Percent(pbldet.get(6).get(3).html().trim());
            pb.setOption4_Scored_Percent(pbldet.get(6).get(4).html().trim());
            pb.setOption5_Scored_Percent(pbldet.get(6).get(5).html().trim());
            lis.add(pb);
        }
        return lis;
    }


}
