package com.bee.cnscnewsandupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScholarShipRecyclerAdapter extends RecyclerView.Adapter<ScholarShipRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Scholarship> list;
    private OnItemClickListener listener;

    public ScholarShipRecyclerAdapter(List<Scholarship> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_scholarship, viewGroup, false);

        context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.init();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView title;

        ViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        private void init() {
            final int position = getAdapterPosition();
            final int index = position + 1;
            final Scholarship scholarship = list.get(position);
            final String tit = index + ". " + scholarship.getTitle();
            title.setText(tit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(scholarship);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(Scholarship scholarship);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
