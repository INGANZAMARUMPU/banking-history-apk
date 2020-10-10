package bi.konstrictor.bankhistory;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

public class DateForm extends Dialog {

    private final Activity context;

    public DateForm(Activity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_filter_date);
        this.context = context;
    }

    private void submit(View view) {
    }
}
