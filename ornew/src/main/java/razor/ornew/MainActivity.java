package razor.ornew;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    private List<EditText> editTextList = new ArrayList<EditText>();
    TableLayout layout;
    EditText row, col;
    int width;
    Button random, calculate;
    int[][] ID;
    int ROW = 0, COL = 0;
    int INPUT[][];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        row = (EditText) findViewById(R.id.rowsEditTextId);
        col = (EditText) findViewById(R.id.columnsEditTextId);
        random = (Button) findViewById(R.id.randomButtonId);
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRandoms();
            }
        });
        calculate = (Button) findViewById(R.id.calculateButtonId);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWork();
                for (int t = 0; t < ROW; t++) {
                    TableRow row = (TableRow) layout.getChildAt(t);
                    for (int r = 0; r < COL; r++) {
                        EditText text = (EditText) row.findViewById(ID[t][r]);
                        INPUT[t][r] = Integer.parseInt(text.getText().toString().trim() + "");
                    }
                }

            }
        });
        col.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int co = 5;
                int rt = 5;
                try {
                    co = Integer.parseInt(s.toString().trim());
                    rt = Integer.parseInt(row.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (co > 0 && rt > 0) {
                    ID = new int[rt][co];
                    ROW = rt;
                    COL = co;
                    generator(getApplicationContext(), layout, rt, co, (width - 50) / 5);
                    INPUT = new int[ROW][COL];
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        layout = (TableLayout) findViewById(R.id.tableLayoutId);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
    }

    class ElemGroup {
        Point point;
        int value;

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private void doWork() {
        List<ElemGroup> list = new ArrayList<>();
        int[] max = new int[ROW];
        for (int i = 0; i < ROW; i++) {
            ElemGroup gp;
            gp = getMinValueObj(INPUT[i]);
            gp.getPoint().x = i;
            list.add(gp);
            max[i] = gp.getValue();
            Log.d("sd", gp.getValue() + "");
        }
        int MaxInMins = getMaxValue(max);
        Log.d("sdMax", MaxInMins + "");
        ElemGroup FIRST = new ElemGroup();
        for (ElemGroup gu : list) {
            if (gu.getValue() == MaxInMins) {
                Log.d("First_Yes", "" + gu.getValue() + "\t" + gu.getPoint().toString());
                FIRST = gu;
            }
        }
// Min in Max
        list = new ArrayList<>();
        int[] min = new int[COL];
        for (int i = 0; i < COL; i++) {
            ElemGroup gp;
            int[] colArray = new int[ROW];
            for (int row = 0; row < ROW; row++) {
                colArray[row] = INPUT[row][i];
            }
            gp = getMaxValueObj(colArray);
            gp.getPoint().y = i;
            list.add(gp);
            min[i] = gp.getValue();
        }
        int MaxInMinss = getMinValue(min);
        Log.d("DSD", MaxInMinss + "");
        ElemGroup SECOND = new ElemGroup();
        for (ElemGroup gu : list) {
            if (gu.getValue() == MaxInMinss) {
                Log.d("Secod _Yes", "" + gu.getValue() + "\t" + gu.getPoint().toString());
                SECOND = gu;
            }
        }
        if (FIRST.getPoint().x == SECOND.getPoint().x) {
            if (FIRST.getPoint().y == SECOND.getPoint().y) {
//                Toast.makeText(this, "Saddle Point Exists at\t" + FIRST.getPoint().toString(), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "No Saddle Point Exists", Toast.LENGTH_SHORT).show();
                dominance();
            }
        }
    }

    private void dominance() {
        int flag = 1;
        int flag1;
        while (flag == 1) {
            flag = 0;
            flag1 = 1;
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < ROW; j++) {
                    flag1 = 1;
                    if (j == i)
                        continue;
                    for (int k = 0; k < COL; k++)//checking for a dominant row
                    {
                        if (INPUT[i][k] > INPUT[j][k])
                            flag1 = 0;
                    }
                    if (flag1 == 1)//dominant row detected
                    {

                        flag = 1;
                        for (int l = i; l < ROW - 1; l++) {
                            for (int k = 0; k < COL; k++) {
                                INPUT[l][k] = INPUT[l + 1][k];
                            }
                        }
                        ROW = ROW - 1;
                        i = i - 1;
                        break;
                    }

                }

            }
            flag1 = 1;
            for (int i = 0; i < COL; i++) {
                for (int j = 0; j < COL; j++) {
                    flag1 = 1;
                    if (j == i)
                        continue;
                    for (int k = 0; k < ROW; k++) {
                        if (INPUT[k][i] < INPUT[k][j])
                            flag1 = 0;
                    }
                    if (flag1 == 1) {
                        flag = 1;
                        for (int l = i; l < COL - 1; l++) {
                            for (int k = 0; k < ROW; k++) {
                                INPUT[k][l] = INPUT[k][l + 1];
                            }
                        }
                        COL = COL - 1;
                        i = i - 1;
                        break;
                    }

                }

            }
        }
        StringBuilder build = new
                StringBuilder();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                build.append(INPUT[i][j] + "\t");
            }
            build.append("\n");
        }
        Log.d("ddf", build.toString());
    }

    public static int getMaxValue(int[] array) {
        int maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }
        return maxValue;
    }

    public ElemGroup getMaxValueObj(int[] array) {
        ElemGroup gp = new ElemGroup();
        Point pt = new Point();
        pt.y = -1;
        int minValue = array[0];
        pt.x = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > minValue) {
                minValue = array[i];
                pt.x = i;
            }
        }
        gp.setValue(minValue);
        gp.setPoint(pt);
        return gp;
    }

    public ElemGroup getMinValueObj(int[] array) {
        ElemGroup gp = new ElemGroup();
        Point pt = new Point();
        pt.x = -1;
        int minValue = array[0];
        pt.y = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
                pt.y = i;
            }
        }
        gp.setValue(minValue);
        gp.setPoint(pt);
        return gp;
    }

    public static int getMinValue(int[] array) {
        int minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            }
        }
        return minValue;
    }

    private void setRandoms() {
        for (int t = 0; t < ROW; t++) {
            TableRow row = (TableRow) layout.getChildAt(t);
            for (int r = 0; r < COL; r++) {
                EditText text = (EditText) row.findViewById(ID[t][r]);
                int n = new Random().nextInt(99);
                text.setText(n + "");
                INPUT[t][r] = n;
            }
        }
    }

    private void generator(Context context, TableLayout tablelayout, int i, int j, int k) {
        tablelayout.removeAllViews();
        for (int l = 0; l < i; l++) {
            TableRow tablerow = new TableRow(context);
            tablelayout.addView(tablerow);
            int i1 = 0;
            while (i1 < j) {
                EditText edittext = new EditText(context);
                edittext.setWidth(k);
                edittext.setId(j * l + i1);
                ID[l][i1] = j * l + i1;
                TableRow.LayoutParams tlparams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                edittext.setTextSize(2, 14F);
                edittext.setInputType(InputType.TYPE_CLASS_TEXT);
                tablerow.addView(edittext, tlparams);
                if (i1 == j - 1 && l == i - 1) {
                    edittext.setImeOptions(6);
                } else {
                    edittext.setImeOptions(5);
                    edittext.setNextFocusRightId(edittext.getId() + 1);
                }
                i1++;
            }
        }

    }

}