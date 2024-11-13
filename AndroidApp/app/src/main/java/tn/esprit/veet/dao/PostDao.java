package tn.esprit.veet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tn.esprit.veet.model.Post;

@Dao
public interface PostDao {
    @Insert
    void insert(Post post);

    @Delete
    void delete(Post post);

    @Update
    void update(Post post);

    @Query("SELECT * FROM post_table")
    List<Post> getAllPosts();
    @Query(("SELECT * FROM post_table WHERE id = :id"))
    Post getPostById(int id);
}
