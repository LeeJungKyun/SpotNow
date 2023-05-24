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
    private List<CommentInfo> commentList;

    // CommentAdapter 생성자
    public CommentAdapter() {
        this.commentList = new ArrayList<>();
    }

    // 댓글 목록 설정 메서드
    public void setCommentList(List<CommentInfo> comments) {
        this.commentList = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 뷰 홀더를 위한 레이아웃 파일 inflate
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentInfo comment = commentList.get(position);

        // 댓글 내용과 사용자 이름을 뷰 홀더의 텍스트뷰에 설정
        holder.commentTextView.setText(comment.getComment());
        holder.usernameTextView.setText("[" + comment.getUserName() + "] 의 답글");
    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }

    // 댓글을 표시하는 뷰 홀더 클래스
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView commentTextView;
        private TextView usernameTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.CommentText);
            usernameTextView = itemView.findViewById(R.id.UsernameText);
        }
    }
}