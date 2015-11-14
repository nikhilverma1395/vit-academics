

package razor.orproj;

import android.app.AlertDialog;
import android.content.DialogInterface;


final class a implements DialogInterface.OnClickListener {

    private AlertDialog a;

    a(MainActivity mainactivity, AlertDialog alertdialog) {
        super();
        a = alertdialog;
    }

    public final void onClick(DialogInterface dialoginterface, int i) {
        a.dismiss();
    }
}
