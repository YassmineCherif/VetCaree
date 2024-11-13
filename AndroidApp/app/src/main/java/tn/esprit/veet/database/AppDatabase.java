package tn.esprit.veet.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import tn.esprit.veet.dao.CommentDao;
import tn.esprit.veet.dao.PostDao;
import tn.esprit.veet.model.Comment;
import tn.esprit.veet.model.Post;

@Database(entities = {Post.class, Comment.class}, version = 3, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract PostDao postDao();
    public abstract CommentDao commentDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "post")
                    .allowMainThreadQueries() // Avoid for production, use async instead
                    .build();
        }
        return instance;
    }
}