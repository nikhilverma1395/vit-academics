
package razor.orproj.pack;

import java.text.DecimalFormat;

// Referenced classes of package a.a:
//            c

public final class b
{

    private final int a;
    private final c b;
    private final double c[];
    private final double d[];
    private final double e;
    private final int f;
    private final int g;
    private final int h;

    b(int i, c c1, int j, double ad[], double ad1[])
    {
        a = i;
        b = c1;
        g = j;
        c = ad;
        d = ad1;
        for (i = 0; i < b.b(); i++)
        {
            ad = c;
            ad[i] = ad[i] + c1.a(g, i);
        }

        double d1 = c[0];
        j = 0;
        for (i = 1; i < c1.b();)
        {
            double d2 = d1;
            if (d1 > c[i])
            {
                d2 = c[i];
                j = i;
            }
            i++;
            d1 = d2;
        }

        h = j;
        for (i = 0; i < c1.a(); i++)
        {
            ad = d;
            ad[i] = ad[i] + c1.a(i, j);
        }

        double d3 = d[0];
        j = 0;
        for (i = 1; i < c1.a();)
        {
            double d4 = d3;
            if (d3 < d[i])
            {
                d4 = d[i];
                j = i;
            }
            i++;
            d3 = d4;
        }

        f = j;
        e = (d3 + d1) / 2D / (double)a;
    }

    public final int a()
    {
        return g;
    }

    public final int b()
    {
        return h;
    }

    public final double c()
    {
        return e;
    }

    public final double[] d()
    {
        return c;
    }

    public final double[] e()
    {
        return d;
    }

    public final int f()
    {
        return a;
    }

    public final b g()
    {
        return new b(a + 1, b, f, (double[])c.clone(), (double[])d.clone());
    }

    public final String toString()
    {
        boolean flag = false;
        StringBuilder stringbuilder = new StringBuilder("");
        DecimalFormat decimalformat = new DecimalFormat("#.00");
        stringbuilder.append("A").append(g).append(" ");
        for (int i = 0; i < c.length; i++)
        {
            stringbuilder.append(decimalformat.format(c[i])).append(" ");
        }

        stringbuilder.append("B").append(h).append(" ");
        for (int j = ((flag) ? 1 : 0); j < d.length; j++)
        {
            stringbuilder.append(decimalformat.format(d[j])).append(" ");
        }

        stringbuilder.append(decimalformat.format(e));
        return stringbuilder.toString();
    }
}
