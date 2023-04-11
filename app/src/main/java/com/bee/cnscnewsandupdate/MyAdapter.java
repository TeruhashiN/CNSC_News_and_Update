package com.bee.cnscnewsandupdate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass> datalist;

    public MyAdapter(Context context, List<DataClass> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_admin_announcement,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(datalist.get(position).getDataImage()).into(holder.recImage);
        holder.recTitle.setText(datalist.get(position).getDataTitle());
        holder.recDesc.setText(datalist.get(position).getDataDesc());
        holder.recDate.setText(datalist.get(position).getDataDate());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", datalist.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Description", datalist.get(holder.getAdapterPosition()).getDataDesc());
                intent.putExtra("Title", datalist.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("Date", datalist.get(holder.getAdapterPosition()).getDataDate());

                context.startActivity(intent);



            }
        });


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle, recDesc, recDate;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recTitle = itemView.findViewById(R.id.recTitle);
        recDesc = itemView.findViewById(R.id.recDesc);
        recDate = itemView.findViewById(R.id.recDate);
        recCard = itemView.findViewById(R.id.recCard);

    }
}