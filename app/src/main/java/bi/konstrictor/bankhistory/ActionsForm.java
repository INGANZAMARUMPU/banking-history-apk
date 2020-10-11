package bi.konstrictor.bankhistory;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class ActionsForm extends Dialog {

    private final MainActivity context;
    private CheckBox check_retraits, check_depots;

    public ActionsForm(final MainActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_filter_actions);
        this.context = context;
        check_retraits = findViewById(R.id.check_retraits);
        check_depots = findViewById(R.id.check_depots);
        Button btn_actions_cancel = findViewById(R.id.btn_actions_cancel);
        Button btn_actions_submit = findViewById(R.id.btn_actions_submit);

        btn_actions_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.refresh();
                dismiss();
            }
        });
        btn_actions_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        check_retraits.setChecked(context.isRetrait());
        check_depots.setChecked(context.isDepot());
    }
    private void submit() {
        context.setRetrait(check_retraits.isChecked());
        context.setDepot(check_depots.isChecked());
        context.filterMovment();
        dismiss();
    }
}
