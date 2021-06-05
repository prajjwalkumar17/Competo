package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StartupBBSR.competo.Models.HorizontalModel;
import com.StartupBBSR.competo.R;

import java.util.ArrayList;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.HorizontalRVViewHolder> {

Context  context;
ArrayList<HorizontalModel>arrayList;
public HorizontalRecyclerViewAdapter(Context context,ArrayList<HorizontalModel>arrayList)
    {
        this.context=context;
        this.arrayList=arrayList;
    }


    @Override
    public HorizontalRecyclerViewAdapter.HorizontalRVViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal,parent,false);
 return new HorizontalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder( HorizontalRecyclerViewAdapter.HorizontalRVViewHolder holder, int position) {
 HorizontalModel horizontalModel =arrayList.get(position);
 holder.textViewTitle.setText(horizontalModel.getName());
 holder.itemView.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Toast.makeText(context,horizontalModel.getName(),Toast.LENGTH_LONG).show();
     }
 });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class HorizontalRVViewHolder extends RecyclerView.ViewHolder{
    TextView textViewTitle;
    ImageView imageView;
        public HorizontalRVViewHolder(View itemView){
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.iv_Thumb);
            textViewTitle=(TextView) itemView.findViewById(R.id.tv_txtTitleHorizontal);

        }
    }
}
