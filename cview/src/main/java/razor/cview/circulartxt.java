package razor.cview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Nikhil Verma on 9/8/2015.
 */
public class circulartxt extends View {
    protected int backcolor;
    protected int textSize;
    protected int elevation;
    protected int radius;
    protected CharSequence text;
    protected Paint paint, tpaint;

    protected void handleAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray styledAttrs;
            styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.circulartxt);
            backcolor = styledAttrs.getColor(R.styleable.circulartxt_ctxt_color, Color.CYAN);
            textSize = styledAttrs.getDimensionPixelOffset(R.styleable.circulartxt_ctxt_textSize, 20);
            text = styledAttrs.getText(R.styleable.circulartxt_text);
            //   elevation = styledAttrs.getDimensionPixelOffset(R.styleable.circulartxt_ctxt_color, 10);
            radius = styledAttrs.getInteger(R.styleable.circulartxt_ctxt_radius, 15);
            styledAttrs.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public circulartxt(Context context) {
        super(context);
        handleAttributes(context, null);
    }

    public circulartxt(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttributes(context, attrs);
        paint = new Paint();
        tpaint = new Paint();
        paint.setAntiAlias(true);
        tpaint.setAntiAlias(true);
        paint.setColor(backcolor);
        tpaint.setColor(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(100, 100, radius, paint);
        canvas.drawText((String)text, 100 -12, 100-12, tpaint);
    }
}
