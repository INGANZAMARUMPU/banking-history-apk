package bi.konstrictor.bankhistory;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;

import java.util.Calendar;

public class FilterForm extends Dialog {

    private final MainActivity context;
    private DatePicker date_picker_du, date_picker_au;
    private CheckBox check_retraits, check_depots;

    public FilterForm(final MainActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_filter);
        this.context = context;

        date_picker_du = findViewById(R.id.date_picker_du);
        date_picker_au = findViewById(R.id.date_picker_au);
        Button btn_date_cancel = findViewById(R.id.btn_date_cancel);
        Button btn_date_submit = findViewById(R.id.btn_date_submit);
        check_retraits = findViewById(R.id.check_retraits);
        check_depots = findViewById(R.id.check_depots);

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
        check_retraits.setChecked(context.isRetrait());
        check_depots.setChecked(context.isDepot());
    }
    private void submit() {
        context.setDate_de(getTimestamp(date_picker_du));
        context.setDate_a(getTimestamp(date_picker_au));
        context.setRetrait(check_retraits.isChecked());
        context.setDepot(check_depots.isChecked());
        context.performFilter();
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
