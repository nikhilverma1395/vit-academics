// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package razor.orproj;

import android.app.AlertDialog;
import android.content.DialogInterface;

final class c implements DialogInterface.OnClickListener {

    private AlertDialog a;

    c(AlertDialog alertdialog) {
        super();
        a = alertdialog;
    }

    public final void onClick(DialogInterface dialoginterface, int i) {
        a.dismiss();
    }
}
