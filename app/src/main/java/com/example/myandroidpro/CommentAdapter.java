package com.example.myandroidpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final Context context;
    private final ArrayList<CommentModel> commentModels;

    public CommentAdapter(Context context,ArrayList<CommentModel> commentModels) {
        this.context = context;
        this.commentModels = commentModels;
    }


    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card_layout,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        CommentModel model = commentModels.get(position);
        holder.userId.setText(model.getUserId());
        holder.comment_text.setText(model.getComment_text());
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView userId, comment_text;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userId = itemView.findViewById(R.id.userId);
            comment_text = itemView.findViewById(R.id.comment_text);

        }
    }
}
