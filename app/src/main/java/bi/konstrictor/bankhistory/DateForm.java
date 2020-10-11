package bi.konstrictor.bankhistory;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DateForm extends Dialog {

    private final MainActivity context;
    DatePicker date_picker_du, date_picker_au;
    Button btn_date_cancel, btn_date_submit;

    public DateForm(final MainActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_filter_date);
        this.context = context;

        date_picker_du = findViewById(R.id.date_picker_du);
        date_picker_au = findViewById(R.id.date_picker_au);
        btn_date_cancel = findViewById(R.id.btn_date_cancel);
        btn_date_submit = findViewById(R.id.btn_date_submit);

        btn_date_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.refresh();
                dismiss();
            }
        });
        btn_date_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
                dismiss();
            }
        });
    }
    private void submit() {
        context.setDate_de(getTimestamp(date_picker_du));
        context.setDate_a(getTimestamp(date_picker_au));
        context.filterDate();
    }
    private long getTimestamp(DatePicker date_picker){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date_picker.getYear());
        calendar.set(Calendar.MONTH, date_picker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, date_picker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }
}
