package ridersadvisor.com.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.EditText;

public class EdittextViewRalewayRegular extends EditText {

    public EdittextViewRalewayRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EdittextViewRalewayRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EdittextViewRalewayRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Roboto-Regular.ttf");
        setTypeface(tf);
    }
}
