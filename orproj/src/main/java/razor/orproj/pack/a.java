
package razor.orproj.pack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

// Referenced classes of package a.a:
//            c, d, b, e, 
//            f

public final class a {

    private final c a;
    private final TreeSet b = new TreeSet();
    private final TreeSet c = new TreeSet();
    private double d;
    private int e;
    private b f[];

    public a(c c1) {
        d = 0.0D;
        a = c1;
    }

    private void f() {
        ArrayList arraylist;
        int j;
        boolean flag;
        flag = false;
        arraylist = new ArrayList();
        j = 0;
        int i;
        do {
            _L2:
            i = ((flag) ? 1 : 0);
            if (j >= a.b()) {
                break; /* Loop/switch isn't completed */
            }
            arraylist.add(Boolean.valueOf(false));
            i = 0;

            label0:
            {
                if (i >= a.b() || ((Boolean) arraylist.get(j)).booleanValue()) {
                    break label0;
                }
                int i1 = 0;
                do {
                    label1:
                    {
                        if (i1 < a.a() && j != i) {
                            if (a.a(i1, j) >= a.a(i1, i)) {
                                break label1;
                            }
                            arraylist.set(j, Boolean.valueOf(false));
                        }
                        i++;
                    }
                    if (true) {
                        break;
                    }
                    if (a.a(i1, j) > a.a(i1, i)) {
                        arraylist.set(j, Boolean.valueOf(true));
                    }
                    i1++;
                } while (true);
            }
        } while (true);
        j++;
      //  if (true)goto _L2;else goto _L1
        _L1:
        do {
            while (i < a.b()) {
                int k = i;
                if (((Boolean) arraylist.get(i)).booleanValue()) {
                    if (c.isEmpty()) {
                        c.add(Integer.valueOf(i));
                    } else {
                        Iterator iterator = c.iterator();
                        int l = i;
                        do {
                            if (!iterator.hasNext()) {
                                break;
                            }
                            if (((Integer) iterator.next()).intValue() <= l) {
                                l++;
                            }
                        } while (true);
                        c.add(Integer.valueOf(l));
                    }
                    a.b(i);
                    arraylist.remove(i);
                    k = i - 1;
                }
                i = k + 1;
            }
            return;
        } while (true);
    }

    private d g() {
        boolean flag1 = true;
        boolean flag = false;
        double ad[] = new double[a.a()];
        for (int i = 0; i < a.a(); i++) {
            ad[i] = a.a(i, 0);
        }

        for (int j = 0; j < a.a(); j++) {
            for (int l = 1; l < a.b(); l++) {
                if (a.a(j, l) < ad[j]) {
                    ad[j] = a.a(j, l);
                }
            }

        }

        double d1 = ad[0];
        int k = ((flag1) ? 1 : 0);
        int i1 = ((flag) ? 1 : 0);
        while (k < a.a()) {
            double d2 = d1;
            if (ad[k] > d1) {
                d2 = ad[k];
                i1 = k;
            }
            k++;
            d1 = d2;
        }
        return new d(i1, d1);
    }

    public final e a(int i) {
        int j4;
        int l4;
        e = i;
        f = new b[i];
        f f1 = e();
        double d1 = a.a(0, 0);
        for (int j = 0; j < a.a(); j++) {
            for (int l2 = 0; l2 < a.b(); ) {
                double d2 = d1;
                if (a.a(j, l2) < d1) {
                    d2 = a.a(j, l2);
                }
                l2++;
                d1 = d2;
            }

        }

        if (d1 < 0.0D) {
            a.a(Math.abs(d1));
            d = Math.abs(d1);
        }
        d = 0.0D;
        if (f1 != null) {
            return new e(f1, a.a(), a.b());
        }
        l4 = Math.min(a.a(), a.a());
        j4 = 0;
        ArrayList arraylist = null;
        int k = 0;
        do {
            _L4:
            if (j4 >= l4 - 1) {
                break; /* Loop/switch isn't completed */
            }
            arraylist = new ArrayList();
            k = 0;
            _L2:
            if (k >= a.a()) {
                break; /* Loop/switch isn't completed */
            }
            arraylist.add(Boolean.valueOf(false));
            int i3 = 0;

            label0:
            {
                if (i3 >= a.a() || ((Boolean) arraylist.get(k)).booleanValue()) {
                    break label0;
                }
                int k4 = 0;
                do {
                    label1:
                    {
                        if (k4 < a.b() && k != i3) {
                            if (a.a(k, k4) <= a.a(i3, k4)) {
                                break label1;
                            }
                            arraylist.set(k, Boolean.valueOf(false));
                        }
                        i3++;
                    }
                    if (true) {
                        break;
                    }
                    if (a.a(k, k4) < a.a(i3, k4)) {
                        arraylist.set(k, Boolean.valueOf(true));
                    }
                    k4++;
                } while (true);
            }
        } while (true);
        k++;
      //  if (true)goto _L2;else goto _L1

        int l = 0;
        _L1:
        while (l < a.a()) {
            int j3 = l;
            if (((Boolean) arraylist.get(l)).booleanValue()) {
                if (b.isEmpty()) {
                    b.add(Integer.valueOf(l));
                } else {
                    Iterator iterator = b.iterator();
                    int k3 = l;
                    do {
                        if (!iterator.hasNext()) {
                            break;
                        }
                        if (((Integer) iterator.next()).intValue() <= k3) {
                            k3++;
                        }
                    } while (true);
                    b.add(Integer.valueOf(k3));
                }
                a.a(l);
                arraylist.remove(l);
                j3 = l - 1;
            }
            l = j3 + 1;
        }
        f();
        j4++;
      //  if (true)goto _L4;else goto _L3;
        double ad[] = new double[a.b()];
        double ad1[] = new double[a.a()];
        int i1 = (int) g().a();
        _L3:
        f[0] = new b(1, a, i1, ad, ad1);
        for (int j1 = 0; j1 < i - 1; j1++) {
            f[j1 + 1] = f[j1].g();
        }

        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        for (int k1 = 0; k1 < a.a(); k1++) {
            arraylist1.add(Double.valueOf(0.0D));
        }
        for (int l1 = 0; l1 < a.b(); l1++) {
            arraylist2.add(Double.valueOf(0.0D));
        }

        for (int i2 = 0; i2 < i; i2++) {
            for (int l3 = 0; l3 < a.a(); l3++) {
                if (f[i2].a() == l3) {
                    arraylist1.set(l3, Double.valueOf(Double.parseDouble(((Double) arraylist1.get(l3)).toString()) + 1.0D));
                }
            }

            for (int i4 = 0; i4 < a.b(); i4++) {
                if (f[i2].b() == i4) {
                    arraylist2.set(i4, Double.valueOf(Double.parseDouble(((Double) arraylist2.get(i4)).toString()) + 1.0D));
                }
            }

        }

        for (int j2 = 0; j2 < a.a(); j2++) {
            arraylist1.set(j2, Double.valueOf(Double.parseDouble(((Double) arraylist1.get(j2)).toString()) / (double) i));
        }

        for (int k2 = 0; k2 < a.b(); k2++) {
            arraylist2.set(k2, Double.valueOf(Double.parseDouble(((Double) arraylist2.get(k2)).toString()) / (double) i));
        }

        for (Iterator iterator1 = b.iterator(); iterator1.hasNext(); arraylist1.add(((Integer) iterator1.next()).intValue(), Double.valueOf(0.0D))) {
        }
        for (Iterator iterator2 = c.iterator(); iterator2.hasNext(); arraylist2.add(((Integer) iterator2.next()).intValue(), Double.valueOf(0.0D))) {
        }
        return new e(arraylist1, arraylist2, f[i - 1].c());
    }

    public final TreeSet a() {
        return b;
    }

    public final b b(int i) {
        if (i >= e || i < 0) {
            throw new IndexOutOfBoundsException("Iteration number must be a positive number less than the number of iterations");
        } else {
            return f[i];
        }
    }

    public final TreeSet b() {
        return c;
    }

    public final int c() {
        return e;
    }

    public final double d() {
        return d;
    }

    public final f e() {
        boolean flag = true;
        d d3 = g();
        double ad[] = new double[a.b()];
        for (int i = 0; i < a.b(); i++) {
            ad[i] = a.a(0, i);
        }

        for (int j = 0; j < a.b(); j++) {
            for (int l = 1; l < a.a(); l++) {
                if (a.a(l, j) > ad[j]) {
                    ad[j] = a.a(l, j);
                }
            }

        }

        double d1 = ad[0];
        int i1 = 0;
        for (int k = ((flag) ? 1 : 0); k < a.b(); ) {
            double d2 = d1;
            if (ad[k] < d1) {
                d2 = ad[k];
                i1 = k;
            }
            k++;
            d1 = d2;
        }

        d d4 = new d(i1, d1);
        if (d3.b() == d4.b()) {
            return new f((int) d3.a(), (int) d4.a(), d3.b());
        } else {
            return null;
        }
    }
}
