package com.vesper.mobilecare.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by vesperhan on 2017/3/22.
 */

public class FocusTextView extends android.support.v7.widget.AppCompatTextView {

    public FocusTextView(Context context) {
        super(context);
    }

    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
