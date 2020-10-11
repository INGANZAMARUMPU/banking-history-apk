package bi.konstrictor.bankhistory;

import android.util.Log;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Action {
    public String id, solde, montant, motif, str_date;
    public Date date;
    public boolean is_retrait;

    public Action(String id, String solde, String montant, String motif, String date) {
        this.id = id;
        this.solde = solde;
        this.montant = montant;
        this.motif = motif;
        is_retrait = Double.parseDouble(montant) < 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.date = formatter.parse(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            this.str_date = "Le "+ dateFormatter.format(this.date);
        }catch (Exception e) {
            this.str_date = date;
            this.date = null;
        }
    }

    @Override
    public String toString() {
        return "Action{" +
                "montant='" + montant + '\'' +
                ", motif='" + motif + '\'' +
                '}';
    }

    public long getTime() {
        return date.getTime();
    }
}
