// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package razor.orproj;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;

final class f implements TextWatcher
{

    private EditText a;
    private EditText b;
    private TableLayout c;
    private View d;
    private e e;

    f(e e1, EditText edittext, EditText edittext1, TableLayout tablelayout, View view)
    {
        super();
        e = e1;
        a = edittext;
        b = edittext1;
        c = tablelayout;
        d = view;
    }

    public final void afterTextChanged(Editable editable)
    {
        boolean flag = MainActivity.a(e.a(), a);
        boolean flag1 = MainActivity.a(e.a(), b);
        if (!flag || !flag1)
        {
            c.removeAllViews();
            return;
        } else
        {
            int i = Integer.parseInt(a.getText().toString());
            int j = Integer.parseInt(b.getText().toString());
            editable = (ScrollView)d.findViewById(R.id.scrollView);
            MainActivity.a(e.a(), c, i, j, editable.getWidth() / j);
            return;
        }
    }

    public final void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
    {
    }

    public final void onTextChanged(CharSequence charsequence, int i, int j, int k)
    {
    }
}
