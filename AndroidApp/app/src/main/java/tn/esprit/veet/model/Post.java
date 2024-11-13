package tn.esprit.veet.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_table")
public class Post {


        @PrimaryKey(autoGenerate = true)
        private int id;



        @ColumnInfo(name = "content")
        private String content;
        @ColumnInfo(name = "image_uri") // Store the URI of the image
        private String imageUri;


        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }


        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getImageUri() { return imageUri; }
        public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    }

