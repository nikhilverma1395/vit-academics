// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package razor.orproj;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;

import com.google.android.gms.ads.AdView;


public final class e extends razor.orproj.pack
{

    public e()
    {
    }

    public final View a(LayoutInflater layoutinflater, ViewGroup viewgroup)
    {
        layoutinflater = layoutinflater.inflate(0x7f030019, viewgroup, false);
        if (layoutinflater == null)
        {
            return null;
        } else
        {
            viewgroup = (EditText)layoutinflater.findViewById(0x7f070051);
            EditText edittext = (EditText)layoutinflater.findViewById(0x7f070052);
            f f1 = new f(this, viewgroup, edittext, (TableLayout)layoutinflater.findViewById(0x7f070057), layoutinflater);
            EditText edittext1 = (EditText)layoutinflater.findViewById(0x7f070053);
            g g1 = new g(this, edittext1);
            viewgroup.addTextChangedListener(f1);
            edittext.addTextChangedListener(f1);
            edittext1.addTextChangedListener(g1);
            ((AdView)layoutinflater.findViewById(0x7f070058)).a((new c()).a());
            return layoutinflater;
        }
    }

    public final void a(Bundle bundle)
    {
        super.a(bundle);
    }
}
