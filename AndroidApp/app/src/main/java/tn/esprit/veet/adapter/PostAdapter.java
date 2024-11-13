package tn.esprit.veet.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import tn.esprit.veet.R;
import tn.esprit.veet.dao.CommentDao;
import tn.esprit.veet.dao.PostDao;
import tn.esprit.veet.model.Comment;
import tn.esprit.veet.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;
    private Context context;
    private CommentDao commentDao;
    private PostDao postDao;
    private ImageView deleteCommentButton;

    public PostAdapter(List<Post> posts, Context context, PostDao postDao, CommentDao commentDao) {
        this.posts = posts;
        this.context = context;
        this.postDao = postDao;
        this.commentDao = commentDao;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.content.setText(post.getContent());

        // Load the image based on the file path saved in the database
        if (post.getImageUri() != null && !post.getImageUri().isEmpty()) {
            File imgFile = new File(post.getImageUri());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.postImage.setVisibility(View.VISIBLE);
                holder.postImage.setImageBitmap(bitmap);
            } else {
                holder.postImage.setVisibility(View.GONE);
            }
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        // Load comments for the post
        List<Comment> comments = commentDao.getCommentsForPost(post.getId());
        StringBuilder commentText = new StringBuilder();
        for (Comment comment : comments) {
            commentText.append(comment.getText()).append("\n");
        }
        holder.comments.setText(commentText.toString());

        // Add Comment Button Listener
        holder.addCommentButton.setOnClickListener(v -> {
            holder.commentInput.setVisibility(View.VISIBLE); // Show comment input field
            holder.sendCommentButton.setVisibility(View.VISIBLE); // Show send button
        });

        holder.sendCommentButton.setOnClickListener(v -> {
            String commentContent = holder.commentInput.getText().toString();
            if (!commentContent.isEmpty()) {
                Comment comment = new Comment();
                comment.setPostId(post.getId());
                comment.setText(commentContent);
                commentDao.insert(comment);

                holder.commentInput.setText("");
                holder.commentInput.setVisibility(View.GONE); // Hide comment input
                holder.sendCommentButton.setVisibility(View.GONE); // Hide send button

                // Update comments section without refreshing the whole item
                refreshComments(holder, post.getId());
            }
        });

        // Update Content Button Listener
        holder.content.setOnLongClickListener(v -> {
            holder.content.setVisibility(View.GONE); // Hide the content TextView
            holder.contentInput.setVisibility(View.VISIBLE); // Show the EditText with current content
            holder.contentInput.setText(post.getContent()); // Set EditText with the existing content
            holder.saveContentButton.setVisibility(View.VISIBLE); // Show Save button
            return true;
        });

        // Save Content Button Listener
        holder.saveContentButton.setOnClickListener(v -> {
            String updatedContent = holder.contentInput.getText().toString();
            if (!updatedContent.isEmpty()) {
                post.setContent(updatedContent);
                postDao.update(post); // Update the content in the database

                // Update the UI
                holder.content.setText(updatedContent);
                holder.content.setVisibility(View.VISIBLE); // Show the updated TextView
                holder.contentInput.setVisibility(View.GONE); // Hide the EditText
                holder.saveContentButton.setVisibility(View.GONE); // Hide Save button

                notifyItemChanged(position); // Refresh the item view
            }
        });
        holder.deletePostButton.setOnClickListener(v -> {
            postDao.delete(post);
            posts.remove(position);
            notifyItemRemoved(position);
        });
//        holder.deleteCommentButton.setOnClickListener(v->{
//
//                Comment comment = comments.get(position);
//                commentDao.delete(comment);
//                comments.remove(position);
//                notifyItemRemoved(position);
//            refreshComments(holder, post.getId());
//        });


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private void refreshComments(PostViewHolder holder, int postId) {
        List<Comment> comments = commentDao.getCommentsForPost(postId);
        StringBuilder commentText = new StringBuilder();
        for (Comment comment : comments) {
            commentText.append(comment.getText()).append("\n");
        }
        holder.comments.setText(commentText.toString());
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ImageView postImage;
        TextView content, comments;
        EditText commentInput, contentInput;
        ImageView addCommentButton, sendCommentButton, deleteCommentButton;
        Button updateContentButton, saveContentButton, deletePostButton;

        PostViewHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.post_content);
            comments = itemView.findViewById(R.id.comments);
            postImage = itemView.findViewById(R.id.post_image);
            commentInput = itemView.findViewById(R.id.comment_input);
            contentInput = itemView.findViewById(R.id.content_input);
            addCommentButton = itemView.findViewById(R.id.add_comment_button);
            sendCommentButton = itemView.findViewById(R.id.send_comment_button);
//            updateContentButton = itemView.findViewById(R.id.update_content_button);
            saveContentButton = itemView.findViewById(R.id.save_content_button);
            deletePostButton= itemView.findViewById(R.id.delete_post_button);
//            deleteCommentButton = itemView.findViewById(R.id.delete_comment_button);
        }
    }
}
