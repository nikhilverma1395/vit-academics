// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package razor.orproj.pack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public final class c
{

    private ArrayList a;

    public c(double ad[][])
    {
        a = new ArrayList();
        int k = ad.length;
        int l = ad[0].length;
        for (int i = 0; i < k; i++)
        {
            if (ad[i].length != l)
            {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
            a.add(new ArrayList(l));
            for (int j = 0; j < l; j++)
            {
                ((ArrayList)a.get(i)).add(Double.valueOf(ad[i][j]));
            }

        }

    }

    public final double a(int i, int j)
    {
        return ((Double)((ArrayList)a.get(i)).get(j)).doubleValue();
    }

    public final int a()
    {
        return a.size();
    }

    public final void a(double d)
    {
        for (int i = 0; i < a.size(); i++)
        {
            for (int j = 0; j < ((ArrayList)a.get(i)).size(); j++)
            {
                ((ArrayList)a.get(i)).set(j, Double.valueOf(((Double)((ArrayList)a.get(i)).get(j)).doubleValue() + d));
            }

        }

    }

    public final void a(int i)
    {
        a.remove(i);
    }

    public final int b()
    {
        if (!a.isEmpty())
        {
            return ((ArrayList)a.get(0)).size();
        } else
        {
            throw new IndexOutOfBoundsException("Empty array.");
        }
    }

    public final void b(int i)
    {
        for (Iterator iterator = a.iterator(); iterator.hasNext(); ((ArrayList)iterator.next()).remove(i)) { }
    }

    public final String toString()
    {
        DecimalFormat decimalformat = new DecimalFormat("#0.0");
        StringBuilder stringbuilder = new StringBuilder("");
        for (Iterator iterator = a.iterator(); iterator.hasNext(); stringbuilder.append("\n"))
        {
            for (Iterator iterator1 = ((ArrayList)iterator.next()).iterator(); iterator1.hasNext(); stringbuilder.append(decimalformat.format(((Double)iterator1.next()).doubleValue())).append(" ")) { }
        }

        return stringbuilder.toString();
    }
}
