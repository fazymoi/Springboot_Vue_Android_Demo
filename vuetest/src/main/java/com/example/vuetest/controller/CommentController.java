package com.example.vuetest.controller;


import com.example.vuetest.pojo.*;
import com.example.vuetest.result.Result;
import com.example.vuetest.result.ResultFactory;
import com.example.vuetest.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Controller

public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    FoodService foodService;
    @Autowired
    UserService userService;
    @Autowired
    StoreService storeService;
    @CrossOrigin
    @PostMapping("/api/mycomment")
    @ResponseBody
    public List<Comment> clist(@RequestBody User user, HttpSession session){
        System.out.println("!!!mycomment!!!");
        System.out.println(session.getId());
        List<Comment> clist=commentService.findByUserid((Integer) session.getAttribute("userid"));
        return clist;
    }

    @CrossOrigin
    @PostMapping("/api/foodcomment")
    @ResponseBody
    public List<Comment> foodclist(@RequestParam("storeid")int storeid, HttpSession session){
        System.out.println("!!!foodcomment!!!");
        System.out.println(session.getId());
        int foodid= (int) session.getAttribute("foodid");
        List<Comment> clist=commentService.findByFoodid(foodid);
        for(int i=0;i<clist.size();i++)
        {
            Comment c=clist.get(i);
            System.out.println(c.getNickname());
        }
        return clist;
    }
    @CrossOrigin
    @PostMapping("/api/foodcomment2")
    @ResponseBody
    public List<Comment> foodclist2(@RequestParam("storeid")String storeid, HttpSession session){
        System.out.println("!!!foodcomment2!!!");
        System.out.println(session.getId());
        int foodid= (int) session.getAttribute("foodid");
        List<Comment> clist=commentService.findByFoodid(foodid);
        for(int i=0;i<clist.size();i++)
        {
            Comment c=clist.get(i);
            System.out.println(c.getNickname());
        }
        return clist;
    }
    @CrossOrigin
    @PostMapping("/api/updatecomment")
    @ResponseBody
    public Result updatecomment(@RequestBody Comment comment, HttpSession session){
        System.out.println("!!!updatecomment!!!");
        System.out.println(session.getId());
        int userid= (int) session.getAttribute("userid");
        System.out.println(comment.getCommentid());
        if(comment.getScore()>5)
            return ResultFactory.buildFailResult("评分不得大于5");
        else {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.now();
            String localTime = df.format(time);

            commentService.updateComment(comment,localTime);

            List<Comment> flist=commentService.findByFoodid(comment.getFoodid());
            int tempscore=0;
            for(int i=0;i<flist.size();i++)
            {
                Comment c=flist.get(i);
                tempscore=tempscore+c.getScore();
            }
            tempscore=tempscore/ flist.size();
            foodService.updatescore(comment.getFoodid(),tempscore);

            return ResultFactory.buildResult(200,"修改成功",null);
        }
    }
    @CrossOrigin
    @PostMapping("/api/updatecomment2")
    @ResponseBody
    public Result updatecomment(@RequestParam("content")String content,
                                @RequestParam("score")String score,
                                @RequestParam("commentid")String commentid,
                                HttpSession session){
        System.out.println("!!!updatecomment!!!");
        System.out.println(session.getId());
        int userid= (int) session.getAttribute("userid");
        Comment comment=commentService.findByCommentid(Integer.parseInt(commentid));
        comment.setScore((int) Float.parseFloat(score));
        comment.setContent(content);
        if(comment.getScore()>5)
            return ResultFactory.buildFailResult("评分不得大于5");
        else {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.now();
            String localTime = df.format(time);

            commentService.updateComment(comment,localTime);

            List<Comment> flist=commentService.findByFoodid(comment.getFoodid());
            int tempscore=0;
            for(int i=0;i<flist.size();i++)
            {
                Comment c=flist.get(i);
                tempscore=tempscore+c.getScore();
            }
            tempscore=tempscore/ flist.size();
            foodService.updatescore(comment.getFoodid(),tempscore);

            return ResultFactory.buildResult(200,"修改成功",null);
        }
    }


    @CrossOrigin
    @PostMapping("/api/deletecomment")
    @ResponseBody
    public Result deletecomment(@RequestParam("commentid")int commentid, HttpSession session){
        System.out.println("!!!deletecomment!!!");
        System.out.println(session.getId());

        Comment comment=commentService.findByCommentid(commentid);
        List<Comment> flist=commentService.findByFoodid(comment.getFoodid());
        int tempscore=0;
        for(int i=0;i<flist.size();i++)
        {
            Comment c=flist.get(i);
            tempscore=tempscore+c.getScore();
        }
        tempscore=tempscore/ flist.size();
        foodService.updatescore(comment.getFoodid(),tempscore);

        commentService.deleteByCommentid(commentid);
        return ResultFactory.buildResult(200,"删除成功",null);
    }

    @CrossOrigin
    @PostMapping("/api/deletecomment2")
    @ResponseBody
    public Result deletecomment2(@RequestParam("commentid")String commentid, HttpSession session){
        System.out.println("!!!deletecomment!!!");
        System.out.println(session.getId());

        Comment comment=commentService.findByCommentid(Integer.parseInt(commentid));
        List<Comment> flist=commentService.findByFoodid(comment.getFoodid());
        int tempscore=0;
        for(int i=0;i<flist.size();i++)
        {
            Comment c=flist.get(i);
            tempscore=tempscore+c.getScore();
        }
        tempscore=tempscore/ flist.size();
        foodService.updatescore(comment.getFoodid(),tempscore);

        commentService.deleteByCommentid(Integer.parseInt(commentid));
        return ResultFactory.buildResult(200,"删除成功",null);
    }
    @CrossOrigin
    @PostMapping("/api/addcomment")
    @ResponseBody
    public Result addcomment(
                             @RequestParam("content")String content,
                             @RequestParam("score")int score,
                             HttpSession session){
        System.out.println("!!!addcomment!!!");
        System.out.println(session.getId());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String submittime= df.format(time);
        int foodid= (int) session.getAttribute("foodid");
        int userid= (int) session.getAttribute("userid");
        String foodname=(foodService.findByFoodid(foodid)).getFoodname();
        String storename=storeService.findByStoreid(
                foodService.findByFoodid(foodid).getStoreid()).getStorename();
        String nickname=userService.findById(userid).getNickname();
        if(commentService.isExistUseridAndFoodid(userid,foodid)==true)
            return ResultFactory.buildFailResult("不得重复提交评论");
        else if(content==""||score==0)
            return ResultFactory.buildFailResult("评论和评分不得为空");
        else{
            Comment comment = new Comment(userid,foodid,submittime,content,score,foodname,storename,nickname);

            commentService.addComment(comment);

            List<Comment> flist=commentService.findByFoodid(comment.getFoodid());
            int tempscore=0;
            for(int i=0;i<flist.size();i++)
            {
                Comment c=flist.get(i);
                tempscore=tempscore+c.getScore();
            }
            tempscore=tempscore/ flist.size();
            foodService.updatescore(comment.getFoodid(),tempscore);

            return ResultFactory.buildResult(200,"添加成功",null);
        }

    }

    @CrossOrigin
    @PostMapping("/api/addcomment2")
    @ResponseBody
    public Result addcomment2(
            @RequestParam("content")String content,
            @RequestParam("score")String score2,
            HttpSession session){
        System.out.println("!!!addcomment!!!");
        System.out.println(session.getId());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String submittime= df.format(time);
        int score=Integer.parseInt(score2);
        int foodid= (int) session.getAttribute("foodid");
        int userid= (int) session.getAttribute("userid");
        String foodname=(foodService.findByFoodid(foodid)).getFoodname();
        String storename=storeService.findByStoreid(
                foodService.findByFoodid(foodid).getStoreid()).getStorename();
        String nickname=userService.findById(userid).getNickname();
        if(commentService.isExistUseridAndFoodid(userid,foodid)==true)
            return ResultFactory.buildFailResult("不得重复提交评论");
        else if(content==""||score==0)
            return ResultFactory.buildFailResult("评论和评分不得为空");
        else{
            Comment comment = new Comment(userid,foodid,submittime,content,score,foodname,storename,nickname);

            commentService.addComment(comment);

            List<Comment> flist=commentService.findByFoodid(comment.getFoodid());
            int tempscore=0;
            for(int i=0;i<flist.size();i++)
            {
                Comment c=flist.get(i);
                tempscore=tempscore+c.getScore();
            }
            tempscore=tempscore/ flist.size();
            foodService.updatescore(comment.getFoodid(),tempscore);

            return ResultFactory.buildResult(200,"添加成功",null);
        }

    }
}

