package bi.konstrictor.bankhistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptateurAction extends RecyclerView.Adapter<AdaptateurAction.ViewHolder> {

    private MainActivity context;
    private ArrayList<Action> actions;

    AdaptateurAction(MainActivity context, ArrayList<Action> actions) {
        this.context = context;
        this.actions = actions;
    }
    public void setActions(ArrayList<Action> actions){
        this.actions = actions;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Action action = actions.get(position);
        holder.lbl_hist_date.setText(action.str_date);
        holder.lbl_hist_montant.setText(action.montant);
        holder.lbl_hist_solde.setText(action.solde);
        holder.lbl_hist_motif.setText(action.motif);
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_hist_date, lbl_hist_montant, lbl_hist_solde, lbl_hist_motif;
        View view;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            lbl_hist_date = itemView.findViewById(R.id.lbl_hist_date);
            lbl_hist_montant = itemView.findViewById(R.id.lbl_hist_montant);
            lbl_hist_solde = itemView.findViewById(R.id.lbl_hist_solde);
            lbl_hist_motif = itemView.findViewById(R.id.lbl_hist_motif);
        }
    }
}
