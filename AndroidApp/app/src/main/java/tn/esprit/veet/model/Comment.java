package tn.esprit.veet.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment_table",
        foreignKeys = @ForeignKey(entity = Post.class,
                parentColumns = "id",
                childColumns = "post_id",
                onDelete = ForeignKey.CASCADE))
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "post_id")
    private int postId;

    @ColumnInfo(name = "text")
    private String text;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
