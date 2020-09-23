package bi.konstrictor.bankhistory;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Action {
    public String id, solde, montant, motif, date;

    public Action(String id, String solde, String montant, String motif, String date) {
        this.id = id;
        this.solde = solde;
        this.montant = montant;
        this.motif = motif;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            this.date = "Le "+ dateFormatter.format(value);
        }catch (Exception e) {
            this.date = date;
        }
    }
}
