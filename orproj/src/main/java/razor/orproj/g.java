
package razor.orproj;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


final class g implements TextWatcher
{

    private EditText a;
    private e b;

    g(e e1, EditText edittext)
    {
        super();
        b = e1;
        a = edittext;
    }

    public final void afterTextChanged(Editable editable)
    {
        MainActivity.b(b.a(), a);
    }

    public final void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
    {
    }

    public final void onTextChanged(CharSequence charsequence, int i, int j, int k)
    {
    }
}
