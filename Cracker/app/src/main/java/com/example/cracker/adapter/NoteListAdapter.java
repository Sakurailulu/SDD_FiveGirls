package com.example.cracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cracker.R;
import com.example.cracker.bean.Note;

import java.util.List;


public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;//承载上下文
    private List<Note> mDataList;//动态数组承载数据使用
    private LayoutInflater mLayoutInflater;

    public NoteListAdapter(Context mContext, List<Note> mDataList){
        this.mContext=mContext;
        this.mDataList=mDataList;
        mLayoutInflater= LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=mLayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan,parent,false);
        return new NoteListAdapter.ViewHolder(v);
    }

    //将数据绑定到控件
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Note entity=mDataList.get(position);
        if (null==entity)
            return;
        NoteListAdapter.ViewHolder viewHolder= (NoteListAdapter.ViewHolder) holder;
        viewHolder.tv_title.setText(entity.getTitle());
        viewHolder.tv_detail.setText(entity.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.setOnItemClickListener(position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener!=null){
                    listener.setOnItemLongClickListener(position);
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    //找到控件，将其初始化
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tv_title;
        TextView tv_detail;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
            tv_detail= (TextView) itemView.findViewById(R.id.tv_detail);
        }
    }
    private ItemClickListener listener;
    public void setOnItemClickListener(ItemClickListener listener ){
        this.listener = listener;
    }
    public interface ItemClickListener{
        void setOnItemClickListener(int position);
        void setOnItemLongClickListener(int position);
    }
}