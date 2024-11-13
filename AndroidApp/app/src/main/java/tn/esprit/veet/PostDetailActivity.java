package tn.esprit.veet;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tn.esprit.veet.adapter.CommentAdapter;
import tn.esprit.veet.dao.CommentDao;
import tn.esprit.veet.database.AppDatabase;
import tn.esprit.veet.model.Comment;
import tn.esprit.veet.model.Post;

public class PostDetailActivity extends AppCompatActivity implements CommentAdapter.OnCommentActionListener{

    private TextView  contentText;
    private ImageView postImage;
    private RecyclerView commentRecyclerView;
    private EditText newCommentInput;
    private Button addCommentButton, editPostButton, deletePostButton;
    private CommentAdapter commentAdapter;
    private int postId;
    private Post post;
    private AppDatabase database;
    private CommentDao commentDao;

    private List<Comment> commentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Initialize database
        database = AppDatabase.getInstance(this);

        // Retrieve post ID from intent
        postId = getIntent().getIntExtra("postId", -1);

        if (postId == -1) {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components
//        titleText = findViewById(R.id.post_detail_title);
        contentText = findViewById(R.id.post_detail_content);
        postImage = findViewById(R.id.post_detail_image);
        commentRecyclerView = findViewById(R.id.comment_recycler_view);
        newCommentInput = findViewById(R.id.new_comment_input);
        addCommentButton = findViewById(R.id.add_comment_button);
        editPostButton = findViewById(R.id.edit_post_button);
        deletePostButton = findViewById(R.id.delete_post_button);

        // Load post details and comments
        loadPostDetails();
        loadComments();

        // Add new comment
        addCommentButton.setOnClickListener(v -> addComment());

        // Edit post
        editPostButton.setOnClickListener(v -> editPost());

        // Delete post
        deletePostButton.setOnClickListener(v -> deletePost());
    }

    private void loadPostDetails() {
        post = database.postDao().getPostById(postId);
        if (post != null) {
//            titleText.setText(post.getTitle());
            contentText.setText(post.getContent());
            if (post.getImageUri() != null && !post.getImageUri().isEmpty()) {
                postImage.setImageURI(Uri.parse(post.getImageUri()));
                postImage.setVisibility(View.VISIBLE);
            } else {
                postImage.setVisibility(View.GONE);
            }
        }
    }

    private void loadComments() {

        commentList = database.commentDao().getCommentsForPost(postId);
        CommentAdapter commentAdapter = new CommentAdapter(commentList, database.commentDao(), postId);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);
    }

    private void addComment() {
        String commentText = newCommentInput.getText().toString();
        if (!commentText.isEmpty()) {
            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setText(commentText);
            database.commentDao().insert(comment);
            newCommentInput.setText("");
            loadComments(); // Refresh comments
            Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void editPost() {
        Intent intent = new Intent(this, PostItemActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    private void deletePost() {
        if (post != null) {
            database.postDao().delete(post);
            Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    public void onEditComment(Comment comment) {
        // Show dialog to edit comment
        EditText editText = new EditText(this);
        editText.setText(comment.getText());

        new AlertDialog.Builder(this)
                .setTitle("Edit Comment")
                .setView(editText)
                .setPositiveButton("Save", (dialog, which) -> {
                    String updatedText = editText.getText().toString().trim();
                    if (!updatedText.isEmpty()) {
                        comment.setText(updatedText);
                        commentDao.update(comment);
                        loadComments(); // Refresh the comment list
                        Toast.makeText(this, "Comment updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDeleteComment(Comment comment) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Comment")
                .setMessage("Are you sure you want to delete this comment?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    commentDao.delete(comment);
                    loadComments(); // Refresh the comment list
                    Toast.makeText(this, "Comment deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}