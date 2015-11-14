
package razor.orproj;

import android.app.AlertDialog;
import android.content.DialogInterface;

final class b
    implements DialogInterface.OnClickListener
{

    private AlertDialog a;

    b(AlertDialog alertdialog)
    {
        super();
        a = alertdialog;
    }

    public final void onClick(DialogInterface dialoginterface, int i)
    {
        a.dismiss();
    }
}
