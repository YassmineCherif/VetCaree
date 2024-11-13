package tn.esprit.veet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tn.esprit.veet.R;
import tn.esprit.veet.dao.CommentDao;
import tn.esprit.veet.model.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
//    private  Context context;
    private CommentDao commentDao;
    private OnCommentActionListener listener;
    private int postId;





    public CommentAdapter(List<Comment> comments, Context context, CommentDao commentDao, OnCommentActionListener listener) {
        this.comments = comments;

        this.commentDao = commentDao;
        this.listener = listener;
    }

    public CommentAdapter(List<Comment> comments, Context context) {
    }
    public CommentAdapter(List<Comment> comments, CommentDao commentDao, int postId) {
        this.comments = comments;
        this.commentDao = commentDao;
        this.postId = postId;
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.commentText.setText(comment.getText());

        // Handle edit action
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditComment(comment);
            }
        });

        // Handle delete action
        holder.deleteButton.setOnClickListener(v -> {
            commentDao.delete(comment);
            comments.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, comments.size());
        });

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public interface OnCommentActionListener {
        void onEditComment(Comment comment);
        void onDeleteComment(Comment comment);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText;
        ImageButton deleteButton, editButton;


        CommentViewHolder(View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.comment_text);
            editButton = itemView.findViewById(R.id.edit_button);
//            deleteButton = itemView.findViewById(R.id.delete_comment_button);
        }
    }
}