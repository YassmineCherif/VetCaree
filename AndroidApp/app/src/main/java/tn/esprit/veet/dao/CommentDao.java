package tn.esprit.veet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tn.esprit.veet.model.Comment;

@Dao
public interface CommentDao {

    @Insert
    void insert(Comment comment);

    @Delete
    void delete(Comment comment);

    @Update
    void update(Comment comment);

    @Query("SELECT * FROM comment_table WHERE post_id = :postId")
    List<Comment> getCommentsForPost(int postId);
}
