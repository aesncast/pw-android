package com.aesncast.pw_android;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

public class AndroidUtil {
    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public static Activity getActivity(View view) {
        Context context = view.getContext();
        return getActivity(context);
    }
}
