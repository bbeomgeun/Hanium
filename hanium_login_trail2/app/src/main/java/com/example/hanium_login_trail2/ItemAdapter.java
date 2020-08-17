package com.example.hanium_login_trail2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ArrayList<String> mName = null;
    private ArrayList<String> mAddress = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    } // 외부에서 리스너 객체를 받아서 어댑터의 mListener에 전달

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        ItemAdapter.ViewHolder vh = new ItemAdapter.ViewHolder(view);

        return vh;
    }

    ItemAdapter(ArrayList<String> list, ArrayList<String> list2){
        mName = list;
        mAddress = list2;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = mName.get(position);
        holder.text_name.setText(name);

        String address = mAddress.get(position);
        holder.text_address.setText(address);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView text_name;
        TextView text_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if(mListener != null) {
                            mListener.onItemClick(v, pos); // recyclerView에 각 아이템 클릭이벤트
                        }
                    }
                }
            });
            imageView = itemView.findViewById(R.id.image);
            text_name = itemView.findViewById(R.id.text_name);
            text_address = itemView.findViewById(R.id.text_address);
        }
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

}
