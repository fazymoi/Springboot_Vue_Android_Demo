package com.example.vuetest.service;

import com.example.vuetest.dao.CommentDAO;
import com.example.vuetest.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;
    public List<Comment> findByUserid(int id) {
        List clist=commentDAO.findByUserid(id);
        return clist;
    }

    public void updateComment(Comment comment, String localTime) {
        System.out.println(comment.getCommentid());
        Comment cnew = commentDAO.findByCommentid(comment.getCommentid());
        cnew.setSubmittime(localTime);
        cnew.setContent(comment.getContent());
        cnew.setUserid(comment.getUserid());
        cnew.setFoodid(comment.getFoodid());
        cnew.setScore(comment.getScore());
        commentDAO.save(cnew);
    }

    public void deleteByCommentid(int commentid) {
        commentDAO.deleteByCommentid(commentid);
    }

    public int addComment(Comment comment) {
        commentDAO.save(comment);
        return comment.getCommentid();
    }

    public List<Comment> findByFoodid(int foodid) {
        List<Comment> clist=commentDAO.findByFoodid(foodid);
        return clist;
    }

    public Comment findByCommentid(int commentid) {
        return commentDAO.findByCommentid(commentid);
    }

    public boolean isExistUseridAndFoodid(int userid, int foodid) {
        return null!=commentDAO.getByUseridAndFoodid(userid,foodid);
    }
}
