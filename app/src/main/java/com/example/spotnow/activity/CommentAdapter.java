package com.example.spotnow.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotnow.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<CommentInfo> commentList; // List to hold the comment data

    public CommentAdapter() {
        this.commentList = new ArrayList<>(); // Initialize the comment list as an empty ArrayList
    }

    // Method to set the comment list data
    public void setCommentList(List<CommentInfo> comments) {
        this.commentList = comments; // Set the new comment list
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each comment item and return the ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // Bind the data to the ViewHolder at the given position
        CommentInfo comment = commentList.get(position);
        holder.commentTextView.setText(comment.getComment());
        holder.usernameTextView.setText("[" + comment.getUserName() + "] 의 답글");
    }

    @Override
    public int getItemCount() {
        return commentList.size(); // Return the number of comments in the list
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView commentTextView;
        private TextView usernameTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews from the comment_item layout
            commentTextView = itemView.findViewById(R.id.CommentText);
            usernameTextView = itemView.findViewById(R.id.UsernameText);
        }
    }
}