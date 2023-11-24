package com.example.vuetest.dao;
import com.example.vuetest.pojo.Comment;
import com.example.vuetest.pojo.Food;
import com.example.vuetest.pojo.Link;
import com.example.vuetest.pojo.Store;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentDAO extends JpaRepository<Comment,Integer>{

    List<Comment> findByUserid(int id);

    Comment findByCommentid(int commentid);
    @Modifying
    @Transactional
    void deleteByCommentid(int commentid);

    List<Comment> findByFoodid(int foodid);

    Comment getByUseridAndFoodid(int userid, int foodid);
}
