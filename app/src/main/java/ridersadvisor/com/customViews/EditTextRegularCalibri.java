package ridersadvisor.com.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by HP240-22u on 22-07-2016.
 */
public class EditTextRegularCalibri extends EditText {

    public EditTextRegularCalibri(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextRegularCalibri(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextRegularCalibri(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
            setTypeface(tf);
        }
    }

}

