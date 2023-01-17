package tgechev.discoverbulgaria.custom;

import android.widget.TextView;

/**
 * Created by trend on 26.3.2017 г..
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

//Custom class for text view which uses custom font
public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/CyrillicOld.ttf");
        setTypeface(tf);
    }

}