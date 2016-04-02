package io.pivotal.bosh;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by pivotal on 12/17/15.
 */
public class Helpers {

    public static void longToast(Context ctx, String text) {
        Toast.makeText(ctx, text,
                Toast.LENGTH_LONG).show();

    }
}
