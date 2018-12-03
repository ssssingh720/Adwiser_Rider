package ridersadvisor.com.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by HP240-22u on 21-07-2016.
 */
public class TextViewCalibriBold extends TextView {


    public TextViewCalibriBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewCalibriBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewCalibriBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        setTypeface(tf);
    }
}
