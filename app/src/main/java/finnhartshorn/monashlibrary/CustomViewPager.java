package finnhartshorn.monashlibrary;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Finn Hartshorn on 9/05/2017.
 */

public class CustomViewPager extends ViewPager {
    // A custom ViewPager used to override functions providing swipe, this means the tabs must be used to switch between them

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}
