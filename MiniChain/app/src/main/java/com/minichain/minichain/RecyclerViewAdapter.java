package com.minichain.minichain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG ="RecyclerView";

    public ArrayList <String> names = new ArrayList<>();
 //   private ArrayList <String> images = new ArrayList<>();
    public ArrayList<String> hashes = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(ArrayList<String> names,  ArrayList<String> hashes, Context context) {
        this.names = names;
       // this.images = images;
        this.context = context;
        this.hashes = hashes;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");
        //Glide.with(context).asBitmap().load(images.get(i)).into(viewHolder.image);
        viewHolder.nodeName.setText(names.get(i));
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on node");
                Toast.makeText(context, names.get(i), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView nodeName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nodeName = itemView.findViewById(R.id.node_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
