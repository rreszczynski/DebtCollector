package com.example.dusigrosz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DebtorAdapter extends RecyclerView.Adapter<DebtorAdapter.DebtorHolder> {
    private List<Debtor> debtors = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    @NonNull
    @Override
    public DebtorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.debtor_item, parent, false);
        return new DebtorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtorHolder holder, int position) {
        Debtor currentDebtor = debtors.get(position);
        holder.textViewDebtorName.setText(currentDebtor.getName());
        holder.textViewDebt.setText(String.valueOf(currentDebtor.getDebt()));
    }

    @Override
    public int getItemCount() {
        return debtors.size();
    }

    public void setDebtors(List<Debtor> debtors) {
        this.debtors = debtors;
        notifyDataSetChanged();
    }

    class DebtorHolder extends RecyclerView.ViewHolder {
        private TextView textViewDebtorName;
        private TextView textViewDebt;

        public DebtorHolder(@NonNull View itemView) {
            super(itemView);
            textViewDebtorName = itemView.findViewById(R.id.text_view_debtor_name);
            textViewDebt = itemView.findViewById(R.id.text_view_debt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemClickListener.OnItemClick(debtors.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (onItemLongClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemLongClickListener.OnItemLongClick(debtors.get(position));
                        return true;
                    } else return false;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(Debtor debtor);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(Debtor debtor);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
}
