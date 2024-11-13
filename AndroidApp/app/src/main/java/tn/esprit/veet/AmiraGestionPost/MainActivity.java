package tn.esprit.veet.AmiraGestionPost;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import tn.esprit.veet.R;
import tn.esprit.veet.adapter.PostAdapter;
import tn.esprit.veet.dao.CommentDao;
import tn.esprit.veet.dao.PostDao;
import tn.esprit.veet.database.AppDatabase;
import tn.esprit.veet.model.Comment;
import tn.esprit.veet.model.Post;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;
    private Uri imageUri;
    private AppDatabase database;
    private PostDao postDao;
    private CommentDao commentDao;
    private List<Post> postList;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = AppDatabase.getInstance(this);
        postDao = database.postDao();
        commentDao = database.commentDao();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postAdapter = new PostAdapter(postList, this, postDao, commentDao);
        recyclerView.setAdapter(postAdapter);

        findViewById(R.id.add_image_button).setOnClickListener(v -> openImageChooser());
        findViewById(R.id.save_post_button).setOnClickListener(v -> savePost());

        loadPosts();
    }

    private void loadPosts() {
        postList = postDao.getAllPosts();
        postAdapter = new PostAdapter(postList, this, database.postDao(), database.commentDao());
        recyclerView.setAdapter(postAdapter);
    }

    private void openImageChooser() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Save the selected image URI
        }
    }

    private void savePost() {
        EditText contentInput = findViewById(R.id.content_input);
        String content = contentInput.getText().toString();

        Post post = new Post();
        post.setContent(content);

        // Save the image file path if available
        if (imageUri != null) {
            String imagePath = saveImageToInternalStorage(imageUri);
            if (imagePath != null) {
                post.setImageUri(imagePath);
            }
        }

        postDao.insert(post);
        loadPosts(); // Refresh the list
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getFilesDir(), "image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(this, "Storage permission is required to select images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addComment(int postId, String commentContent) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setText(commentContent);
        AppDatabase.getInstance(getBaseContext()).commentDao().insert(comment);
    }
}
