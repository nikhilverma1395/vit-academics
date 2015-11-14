// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package razor.orproj.pack;
import java.text.DecimalFormat;
import java.util.ArrayList;

// Referenced classes of package a.a:
//            f

public final class e
{

    private final ArrayList a;
    private final ArrayList b;
    private final double c;

    e(f f1, int i, int j)
    {
        super();
        boolean flag = false;
        a = new ArrayList();
        int k = 0;
        while (k < i) 
        {
            if (k == (int)f1.a())
            {
                a.add(Double.valueOf(1.0D));
            } else
            {
                a.add(Double.valueOf(0.0D));
            }
            k++;
        }
        b = new ArrayList();
        i = ((flag) ? 1 : 0);
        while (i < j) 
        {
            if (i == (int)f1.b())
            {
                b.add(Double.valueOf(1.0D));
            } else
            {
                b.add(Double.valueOf(0.0D));
            }
            i++;
        }
        c = f1.c();
    }

    e(ArrayList arraylist, ArrayList arraylist1, double d)
    {
        a = arraylist;
        b = arraylist1;
        c = d;
    }

    public final ArrayList a()
    {
        return a;
    }

    public final ArrayList b()
    {
        return b;
    }

    public final double c()
    {
        return c;
    }

    public final String toString()
    {
        boolean flag = false;
        StringBuilder stringbuilder = new StringBuilder("");
        DecimalFormat decimalformat = new DecimalFormat("#.00");
        for (int i = 0; i < a.size(); i++)
        {
            stringbuilder.append("p").append(i).append("=").append(decimalformat.format(a.get(i))).append("; ");
        }

        stringbuilder.append("\n");
        for (int j = ((flag) ? 1 : 0); j < b.size(); j++)
        {
            stringbuilder.append("q").append(j).append("=").append(decimalformat.format(b.get(j))).append("; ");
        }

        stringbuilder.append("\n").append(decimalformat.format(c));
        return stringbuilder.toString();
    }
}
