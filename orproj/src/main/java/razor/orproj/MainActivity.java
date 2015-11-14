package razor.orproj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

// Referenced classes of package ogorodnikov.andrew.two_person_zero_sum_game_android:
//            d, b, c, a,
//            e

public class MainActivity extends AppCompatActivity
{

    public MainActivity()
    {
    }

    private void a(c c1, int i) throws IOException
    {
        File file;
        Object obj;
        DecimalFormat decimalformat;
        obj = new DecimalFormat("#0.0");
        decimalformat = new DecimalFormat("#0.000");
        if (!"mounted".equals(Environment.getExternalStorageState()))
        {
            c1 = (new AlertDialog.Builder(this)).create();
            c1.setTitle(getString(0x7f0a002f));
            c1.setMessage(getString(0x7f0a0012));
            c1.setButton(-3, "OK", new ogorodnikov.andrew.two_person_zero_sum_game_android.d(this, c1));
            c1.show();
            return;
        }
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/tmp.html");
        FileWriter filewriter;
        filewriter = new FileWriter(file);
        filewriter.append("<html>\n");
        filewriter.append("    <head>\n");
        filewriter.append("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
        filewriter.append("    </head>\n");
        filewriter.append("    <body>\n");
        filewriter.append((new StringBuilder("        ")).append(getString(0x7f0a0032)).append("\n").toString());
        filewriter.append("        <table border=\"1\">\n");
        filewriter.append("            <tr>\n");
        filewriter.append("                <td>  </td>\n");
        int j = 0;
        _L2:
        if (j >= c1.b())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>B")).append(String.valueOf(j + 1)).append("</td>\n").toString());
        j++;
        if (true) goto _L2; else goto _L1
        _L1:
        filewriter.append("            </tr>\n");
        j = 0;
        _L6:
        if (j >= c1.a())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append("            <tr>\n");
        filewriter.append((new StringBuilder("                <td>A")).append(String.valueOf(j + 1)).append("</td>\n").toString());
        int k = 0;
        _L4:
        if (k >= c1.b())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>")).append(((DecimalFormat) (obj)).format(c1.a(j, k))).append("</td>\n").toString());
        k++;
        if (true) goto _L4; else goto _L3
        _L3:
        filewriter.append("            </tr>\n");
        j++;
        if (true) goto _L6; else goto _L5
        _L5:
        a a1;
        e e1;
        filewriter.append("        </table>\n");
        a1 = new a(c1);
        e1 = a1.a(i);
        i = 0;
        Object obj1;
        Object obj2;
        obj2 = a1.a();
        obj1 = a1.b();
        obj2 = ((TreeSet) (obj2)).iterator();
        _L9:
        if (!((Iterator) (obj2)).hasNext()) goto _L8; else goto _L7
        _L7:
        j = ((Integer)((Iterator) (obj2)).next()).intValue();
        i = 1;
        filewriter.append((new StringBuilder("        ")).append(getString(0x7f0a002e)).append(" ").append(String.valueOf(j + 1)).append(" ").append(getString(0x7f0a0042)).append(".<br />\n").toString());
        goto _L9
        _L45:
        c1 = new Intent("android.intent.action.VIEW");
        c1.setDataAndType(Uri.fromFile(file), "text/html");
        startActivity(c1);
        return;
        _L8:
        obj1 = ((TreeSet) (obj1)).iterator();
        _L10:
        if (!((Iterator) (obj1)).hasNext())
        {
            break MISSING_BLOCK_LABEL_725;
        }
        j = ((Integer)((Iterator) (obj1)).next()).intValue();
        i = 1;
        filewriter.append((new StringBuilder("        ")).append(getString(0x7f0a002e)).append(" ").append(String.valueOf(j + 1)).append(" ").append(getString(0x7f0a0014)).append(".<br />\n").toString());
        goto _L10
        if (i == 0)
        {
            break MISSING_BLOCK_LABEL_974;
        }
        filewriter.append((new StringBuilder("        ")).append(getString(0x7f0a0037)).append("<br />\n").toString());
        filewriter.append("        <table border=\"1\">\n");
        filewriter.append("            <tr>\n");
        filewriter.append("                <td>  </td>\n");
        i = 0;
        _L12:
        if (i >= c1.b())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>B")).append(String.valueOf(i + 1)).append("</td>\n").toString());
        i++;
        if (true) goto _L12; else goto _L11
        _L11:
        filewriter.append("            </tr>\n");
        i = 0;
        _L16:
        if (i >= c1.a())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append("            <tr>\n");
        filewriter.append((new StringBuilder("                <td>A")).append(String.valueOf(i + 1)).append("</td>\n").toString());
        j = 0;
        _L14:
        if (j >= c1.b())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>")).append(((DecimalFormat) (obj)).format(c1.a(i, j))).append("</td>\n").toString());
        j++;
        if (true) goto _L14; else goto _L13
        _L13:
        filewriter.append("            </tr>\n");
        i++;
        if (true) goto _L16; else goto _L15
        _L15:
        filewriter.append("        </table>\n");
        obj1 = a1.e();
        if (obj1 == null) goto _L18; else goto _L17
        _L17:
        filewriter.append((new StringBuilder("        ")).append(getString(0x7f0a0043)).append(" (").append((int)((f) (obj1)).a()).append(",").append((int)((f) (obj1)).b()).append("), ").append(getString(0x7f0a0045)).append("=").append(((DecimalFormat) (obj)).format(((f) (obj1)).c())).append("<br />\n").toString());
        _L37:
        filewriter.append("        ").append(getString(0x7f0a0041)).append("\n");
        filewriter.append("        <table border=\"1\">\n");
        filewriter.append("            <tr>\n");
        filewriter.append("                <td></td>\n");
        i = 0;
        _L20:
        if (i >= e1.a().size())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>A")).append(String.valueOf(i + 1)).append("</td>\n").toString());
        i++;
        if (true) goto _L20; else goto _L19
        _L18:
        filewriter.append((new StringBuilder("        ")).append(getString(0x7f0a0038)).append(".<br />\n").toString());
        if (a1.d() == 0.0D)
        {
            break MISSING_BLOCK_LABEL_1508;
        }
        filewriter.append("        ").append(getString(0x7f0a000f)).append(" ").append(((DecimalFormat) (obj)).format(a1.d())).append("<br />\n");
        filewriter.append((new StringBuilder("        ")).append(getString(0x7f0a0036)).append("<br />\n").toString());
        filewriter.append("        <table border=\"1\">\n");
        filewriter.append("            <tr>\n");
        filewriter.append("                <td>  </td>\n");
        i = 0;
        _L22:
        if (i >= c1.b())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>B")).append(String.valueOf(i + 1)).append("</td>\n").toString());
        i++;
        if (true) goto _L22; else goto _L21
        _L21:
        filewriter.append("            </tr>\n");
        i = 0;
        _L26:
        if (i >= c1.a())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append("            <tr>\n");
        filewriter.append((new StringBuilder("                <td>A")).append(String.valueOf(i + 1)).append("</td>\n").toString());
        j = 0;
        _L24:
        if (j >= c1.b())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>")).append(((DecimalFormat) (obj)).format(c1.a(i, j))).append("</td>\n").toString());
        j++;
        if (true) goto _L24; else goto _L23
        _L23:
        filewriter.append("            </tr>\n");
        i++;
        if (true) goto _L26; else goto _L25
        _L25:
        filewriter.append("        </table>\n");
        filewriter.append((new StringBuilder("        ")).append(getString(0x7f0a0035)).append(":<br />\n").toString());
        filewriter.append("        <table border=\"1\">\n");
        filewriter.append("            <tr>\n");
        filewriter.append("                <td>N</td>\n");
        filewriter.append("                <td>n(\u0410)</td>\n");
        i = 0;
        _L28:
        if (i >= c1.b())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>B")).append(String.valueOf(i + 1)).append("</td>\n").toString());
        i++;
        if (true) goto _L28; else goto _L27
        _L27:
        filewriter.append("                <td>n(B)</td>\n");
        i = 0;
        _L30:
        if (i >= c1.a())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>A")).append(String.valueOf(i + 1)).append("</td>\n").toString());
        i++;
        if (true) goto _L30; else goto _L29
        _L29:
        filewriter.append("                <td></td>\n");
        filewriter.append("                <td>\u03BD</td>\n");
        filewriter.append("            </tr>\n");
        i = 0;
        _L36:
        if (i >= a1.c())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append("            <tr>\n");
        filewriter.append((new StringBuilder("                <td>")).append(String.valueOf(a1.b(i).f())).append("</td>\n").toString());
        filewriter.append((new StringBuilder("                <td>A")).append(String.valueOf(a1.b(i).a() + 1)).append("</td>\n").toString());
        j = 0;
        _L32:
        if (j >= c1.b())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>")).append(((DecimalFormat) (obj)).format(a1.b(i).d()[j])).append("</td>\n").toString());
        j++;
        if (true) goto _L32; else goto _L31
        _L31:
        filewriter.append((new StringBuilder("                <td>B")).append(String.valueOf(a1.b(i).b() + 1)).append("</td>\n").toString());
        j = 0;
        _L34:
        if (j >= c1.a())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>")).append(((DecimalFormat) (obj)).format(a1.b(i).e()[j])).append("</td>\n").toString());
        j++;
        if (true) goto _L34; else goto _L33
        _L33:
        filewriter.append("                <td></td>\n");
        filewriter.append((new StringBuilder("                <td>")).append(((DecimalFormat) (obj)).format(a1.b(i).c())).append("</td>\n").toString());
        filewriter.append("            </tr>\n");
        i++;
        if (true) goto _L36; else goto _L35
        _L35:
        filewriter.append("        </table>\n");
        goto _L37
        _L19:
        filewriter.append("            </tr>\n");
        filewriter.append("            <tr>\n");
        filewriter.append("                <td>p(A)</td>\n");
        i = 0;
        _L39:
        if (i >= e1.a().size())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>")).append(decimalformat.format(e1.a().get(i))).append("</td>\n").toString());
        i++;
        if (true) goto _L39; else goto _L38
        _L38:
        filewriter.append("            </tr>\n");
        filewriter.append("        </table>\n");
        filewriter.append("        <table border=\"1\">\n");
        filewriter.append("            <tr>\n");
        filewriter.append("                <td></td>\n");
        i = 0;
        _L41:
        if (i >= e1.b().size())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>B")).append(String.valueOf(i + 1)).append("</td>\n").toString());
        i++;
        if (true) goto _L41; else goto _L40
        _L40:
        filewriter.append("            </tr>\n");
        filewriter.append("            <tr>\n");
        filewriter.append("                <td>q(B)</td>\n");
        i = 0;
        _L43:
        if (i >= e1.b().size())
        {
            break; /* Loop/switch isn't completed */
        }
        filewriter.append((new StringBuilder("                <td>")).append(decimalformat.format(e1.b().get(i))).append("</td>\n").toString());
        i++;
        if (true) goto _L43; else goto _L42
        _L42:
        try
        {
            filewriter.append("            </tr>\n");
            filewriter.append("        </table>\n");
            if (a1.d() != 0.0D)
            {
                filewriter.append(getString(0x7f0a0044)).append(" ").append(((DecimalFormat) (obj)).format(e1.c())).append(" - ").append(((DecimalFormat) (obj)).format(a1.d())).append(" = ").append(((DecimalFormat) (obj)).format(e1.c() - a1.d())).append("<bt />\n");
            }
            filewriter.append((new StringBuilder("        ")).append(getString(0x7f0a0031)).append(" = ").append(((DecimalFormat) (obj)).format(e1.c() - a1.d())).toString());
            filewriter.append("    </body>\n");
            filewriter.append("</html>\n");
            filewriter.flush();
            filewriter.close();
        }
        // Misplaced declaration of an exception variable
        catch (c c1)
        {
            obj = (new android.app.AlertDialog.Builder(this)).create();
            ((AlertDialog) (obj)).setMessage(c1.toString());
            ((AlertDialog) (obj)).show();
        }
        // Misplaced declaration of an exception variable
        catch (c c1)
        {
            obj = (new android.app.AlertDialog.Builder(this)).create();
            ((AlertDialog) (obj)).setMessage(c1.toString());
            ((AlertDialog) (obj)).show();
        }
        if (true) goto _L45; else goto _L44
        _L44:
    }

    static void a(Context context, TableLayout tablelayout, int i, int j, int k)
    {
        b(context, tablelayout, i, j, k);
    }

    static boolean a(Context context, TextView textview)
    {
        return d(context, textview);
    }

    private static void b(Context context, TableLayout tablelayout, int i, int j, int k)
    {
        tablelayout.removeAllViews();
        for (int l = 0; l < i; l++)
        {
            TableRow tablerow = new TableRow(context);
            tablelayout.addView(tablerow);
            int i1 = 0;
            while (i1 < j)
            {
                EditText edittext = new EditText(context);
                edittext.setMinWidth(k);
                edittext.setId(j * l + i1);
                edittext.setTextSize(2, 14F);
                edittext.setInputType(12290);
                tablerow.addView(edittext, new android.widget.TableRow.LayoutParams(-2, -2));
                if (i1 == j - 1 && l == i - 1)
                {
                    edittext.setImeOptions(6);
                } else
                {
                    edittext.setImeOptions(5);
                    edittext.setNextFocusRightId(edittext.getId() + 1);
                }
                i1++;
            }
        }

    }

    static boolean b(Context context, TextView textview)
    {
        return c(context, textview);
    }

    private static boolean c(Context context, TextView textview)
    {
        if (textview.getText() == null || textview.length() <= 0)
        {
            return false;
        }
        int i;
        try
        {
            i = Integer.parseInt(textview.getText().toString());
        }
        // Misplaced declaration of an exception variable
        catch (Context context)
        {
            return false;
        }
        // Misplaced declaration of an exception variable
        catch (Context context)
        {
            return false;
        }
        if (i <= 0 || i > 500)
        {
            AlertDialog alertdialog = (new android.app.AlertDialog.Builder(context)).create();
            alertdialog.setTitle(context.getString(0x7f0a002f));
            alertdialog.setButton(-3, context.getString(0x7f0a003d), new ogorodnikov.andrew.two_person_zero_sum_game_android.b(alertdialog));
            alertdialog.setMessage(context.getString(0x7f0a003a));
            alertdialog.show();
            textview.setText("");
            return false;
        } else
        {
            return true;
        }
    }

    private static boolean d(Context context, TextView textview)
    {
        if (textview.getText() == null || textview.length() <= 0)
        {
            return false;
        }
        int i;
        try
        {
            i = Integer.parseInt(textview.getText().toString());
        }
        // Misplaced declaration of an exception variable
        catch (Context context)
        {
            return false;
        }
        // Misplaced declaration of an exception variable
        catch (Context context)
        {
            return false;
        }
        if (i <= 0 || i > 20)
        {
            AlertDialog alertdialog = (new android.app.AlertDialog.Builder(context)).create();
            alertdialog.setTitle(context.getString(0x7f0a002f));
            alertdialog.setButton(-3, context.getString(0x7f0a003d), new ogorodnikov.andrew.two_person_zero_sum_game_android.c(alertdialog));
            alertdialog.setMessage(context.getString(0x7f0a003f));
            alertdialog.show();
            textview.setText("");
            return false;
        } else
        {
            return true;
        }
    }

    public void calculateButtonClick(View view)
    {
        Object obj;
        EditText edittext;
        EditText edittext1;
        TableLayout tablelayout;
        double ad[][];
        int i;
        int k;
        int l;
        int i1;
        obj = (EditText)findViewById(0x7f070052);
        edittext = (EditText)findViewById(0x7f070051);
        edittext1 = (EditText)findViewById(0x7f070053);
        boolean flag = d(this, edittext);
        boolean flag1 = d(this, ((TextView) (obj)));
        boolean flag2 = c(this, edittext1);
        view = (new android.app.AlertDialog.Builder(this)).create();
        view.setTitle(getString(0x7f0a002f));
        view.setButton(-3, "Ok", new ogorodnikov.andrew.two_person_zero_sum_game_android.a(this, view));
        if (!flag || !flag2 || !flag1)
        {
            view.setMessage(getString(0x7f0a0030));
            view.show();
            return;
        }
        k = Integer.parseInt(edittext1.getText().toString());
        tablelayout = (TableLayout)findViewById(0x7f070057);
        l = tablelayout.getChildCount();
        i1 = ((TableRow)tablelayout.getChildAt(0)).getChildCount();
        ad = (double[][])Array.newInstance(Double.TYPE, new int[] {
                l, i1
        });
        i = 0;
        _L5:
        if (i >= l) goto _L2; else goto _L1
        _L1:
        TableRow tablerow;
        int j;
        tablerow = (TableRow)tablelayout.getChildAt(i);
        j = 0;
        _L4:
        EditText edittext2;
        if (j >= i1)
        {
            continue; /* Loop/switch isn't completed */
        }
        edittext2 = (EditText)tablerow.getChildAt(j);
        ad[i][j] = Double.parseDouble(edittext2.getText().toString());
        j++;
        if (true) goto _L4; else goto _L3
        _L3:
        continue; /* Loop/switch isn't completed */
        obj;
        view.setMessage(getString(0x7f0a003e));
        view.show();
        return;
        obj;
        view.setMessage(getString(0x7f0a003e));
        view.show();
        return;
        i++;
        goto _L5
        _L2:a
        a(new c(ad), k);
        ((EditText) (obj)).setText("");
        edittext.setText("");
        edittext1.setText("");
        tablelayout.removeAllViews();
        edittext.requestFocus();
        return;
    }

    public void clearButtonClick(View view)
    {
        ((TableLayout)findViewById(0x7f070057)).removeAllViews();
        ((EditText)findViewById(0x7f070052)).setText("");
        view = (EditText)findViewById(0x7f070051);
        view.setText("");
        view.requestFocus();
        ((EditText)findViewById(0x7f070053)).setText("");
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030018);
        if (bundle == null)
        {
            b().a().a(0x7f07004e, new ogorodnikov.andrew.two_person_zero_sum_game_android.e()).a();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(0x7f0c0000, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch (menuitem.getItemId())
        {
            default:
                return super.onOptionsItemSelected(menuitem);

            case 2131165273:
                menuitem = (new android.app.AlertDialog.Builder(this)).create();
                menuitem.setTitle(getString(0x7f0a000e));
                menuitem.setMessage(getString(0x7f0a000d));
                menuitem.show();
                return true;

            case 2131165274:
                menuitem = (new android.app.AlertDialog.Builder(this)).create();
                break;
        }
        menuitem.setTitle(getString(0x7f0a0034));
        menuitem.setMessage(getString(0x7f0a0033));
        menuitem.show();
        return true;
    }

    public void randomButtonClick(View view)
    {
        view = (TableLayout)findViewById(0x7f070057);
        Object obj = (TextView)findViewById(0x7f070053);
        if (((TextView) (obj)).length() <= 0)
        {
            ((TextView) (obj)).setText("20");
        }
        obj = (TextView)findViewById(0x7f070052);
        Object obj1 = (TextView)findViewById(0x7f070051);
        int i;
        int j;
        if (((TextView) (obj1)).getText() == null || ((TextView) (obj1)).length() <= 0)
        {
            if (((TextView) (obj)).getText() == null || ((TextView) (obj)).length() <= 0)
            {
                j = (int)(Math.random() * 8D + 2D);
                i = (int)(Math.random() * 8D + 2D);
                ((TextView) (obj1)).setText(String.valueOf(j));
                ((TextView) (obj)).setText(String.valueOf(i));
            } else
            {
                j = (int)(Math.random() * 8D + 2D);
                ((TextView) (obj1)).setText(String.valueOf(j));
                i = Integer.parseInt(((TextView) (obj)).getText().toString());
            }
        } else
        if (((TextView) (obj)).getText() == null || ((TextView) (obj)).length() <= 0)
        {
            i = (int)(Math.random() * 8D + 2D);
            ((TextView) (obj)).setText(String.valueOf(i));
            j = Integer.parseInt(((TextView) (obj1)).getText().toString());
        } else
        {
            j = Integer.parseInt(((TextView) (obj1)).getText().toString());
            i = Integer.parseInt(((TextView) (obj)).getText().toString());
        }
        b(this, view, j, i, ((ScrollView)findViewById(0x7f07004f)).getWidth() / i);
        label0:
        for (int k = 0; k < j; k++)
        {
            obj = (TableRow)view.getChildAt(k);
            if (obj == null)
            {
                continue;
            }
            int l = 0;
            do
            {
                if (l >= i)
                {
                    continue label0;
                }
                obj1 = (EditText)((TableRow) (obj)).getChildAt(l);
                if (obj1 != null)
                {
                    DecimalFormatSymbols decimalformatsymbols = new DecimalFormatSymbols();
                    decimalformatsymbols.setDecimalSeparator('.');
                    ((TextView) (obj1)).setText((new DecimalFormat("#0.0", decimalformatsymbols)).format(Math.random() * 10D));
                }
                l++;
            } while (true);
        }

    }
}
