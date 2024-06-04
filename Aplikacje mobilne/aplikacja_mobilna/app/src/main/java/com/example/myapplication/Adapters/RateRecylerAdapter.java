package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Info.Rating;
import com.example.myapplication.R;

import java.util.List;

public class RateRecylerAdapter extends RecyclerView.Adapter<RateViewHolder> {

    Context context;
    List<Rating> list;

    public RateRecylerAdapter(Context context, List<Rating> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RateViewHolder(LayoutInflater.from(context).inflate(R.layout.rate_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RateViewHolder holder, int position) {
        holder.textView_source.setText(list.get(position).getSource());
        holder.textView_value.setText(list.get(position).getValue());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class RateViewHolder extends RecyclerView.ViewHolder
{
    TextView textView_source, textView_value;
    public RateViewHolder(@NonNull View itemView){
        super(itemView);
        textView_source = itemView.findViewById(R.id.textView_source);
        textView_value = itemView.findViewById(R.id.textView_value);
    }
}
