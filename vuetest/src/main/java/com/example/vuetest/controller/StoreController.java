package com.example.vuetest.controller;

import com.example.vuetest.pojo.*;
import com.example.vuetest.result.Result;
import com.example.vuetest.result.ResultFactory;
import com.example.vuetest.service.CheckService;
import com.example.vuetest.service.LinkService;
import com.example.vuetest.service.StoreService;
import com.example.vuetest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

@Controller
public class StoreController {

    @Autowired
    StoreService storeService;
    @Autowired
    LinkService linkService;

    @Autowired
    CheckService checkService;

    @CrossOrigin
    @PostMapping("/api/mystore")
    @ResponseBody
    public List<Store> list(@RequestBody User user,HttpSession session){
        System.out.println("!!!mystore!!!");
        System.out.println(session.getId());
        int userid= (int) session.getAttribute("userid");
        System.out.println(linkService.link_mystore(userid));
        int storeid;
        Store temp;
        List<Link> mystorelist=linkService.link_mystore(userid);
        List<Store> mystore = new ArrayList<Store>();
        for (int i = 0; i < mystorelist.size(); i++) {
            Link l = mystorelist.get(i);
            storeid=l.getStoreid();
            System.out.println(storeid);
            temp = storeService.findByStoreid(storeid);
            mystore.add(temp);
        }
        return mystore;
    }

    @CrossOrigin
    @PostMapping("/api/addstore")
    @ResponseBody
    public Result addstore(@RequestBody Store store,HttpSession session){
        System.out.println("!!!addstore!!!");
        System.out.println(session.getId());
        System.out.println(store.getStatus());
        System.out.println(store.getIntroduction());
        int userid= (int) session.getAttribute("userid");
        if(store.getStorename()=="" || store.getIntroduction()=="" || store.getOpeninghours() ==""
        || store.getStoreaddr()=="" ||store.getStorecode() == "" || store.getContact()=="" ||
                store
                .getCover()=="")
            return ResultFactory.buildFailResult("信息不能为空");
        if  (storeService.isStorenameExist((store.getStorename()))==true)
            return ResultFactory.buildFailResult("已有同名商家");
        else if(storeService.isStorecodeExist((store.getStorecode()))==true)
            return ResultFactory.buildFailResult("商家已存在");
        else {
            int storeid = storeService.AddStore(store);
            System.out.println(storeid+userid);
            Link link = new Link(userid,storeid);
            linkService.Addlink(link);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.now();
            String localTime = df.format(time);
            Check check = new Check(userid,storeid,localTime);
            checkService.AddCheck(check);
            return ResultFactory.buildResult(200,"添加成功",null);
        }
    }
    @CrossOrigin
    @PostMapping("/api/deletestore")
    @ResponseBody
    public Result deletestore(@RequestBody Store store,HttpSession session){
        System.out.println("!!!deletestore!!!");
        System.out.println(store.getStoreid());
        System.out.println(session.getId());
        int userid= (int) session.getAttribute("userid");
        linkService.deleteByStoreid(store.getStoreid());
        storeService.deleteByStoreid(store.getStoreid());
        return ResultFactory.buildResult(200,"删除成功",null);

    }
    @CrossOrigin
    @PostMapping("/api/updatestore")
    @ResponseBody
    public Result updatestore(@RequestBody Store store,HttpSession session){
        System.out.println("!!!updatestore!!!");
        System.out.println(session.getId());
        int userid= (int) session.getAttribute("userid");
        System.out.println(store.getStoreid());
        System.out.println(storeService.getsameStorenameStoreid((store.getStorename())));
        if(store.getStorename()=="" || store.getIntroduction()=="" || store.getOpeninghours() ==""
                || store.getStoreaddr()=="" ||store.getStorecode() == "" || store.getContact()=="" ||
                store
                        .getCover()=="")
            return ResultFactory.buildFailResult("信息不能为空");
        if  (storeService.isStorenameExist((store.getStorename()))==true &&
                store.getStoreid() != storeService.getsameStorenameStoreid((store.getStorename())))
        {
                return ResultFactory.buildFailResult("已有同名商家");
        }
        else if(storeService.isStorecodeExist((store.getStorecode()))==true &&
                store.getStoreid() != storeService.getsameStorecodeStoreid((store.getStorecode())))
            return ResultFactory.buildFailResult("商家已存在");
        else {
            storeService.UpdateStore(store);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.now();
            String localTime = df.format(time);
            if(null != checkService.getsameUseridandStoreid(userid,store.getStoreid()))
                checkService.UpdateCheck(userid,store.getStoreid(),localTime);
            else{
                Check check = new Check(userid,store.getStoreid(),localTime);
                checkService.AddCheck(check);
            }
            return ResultFactory.buildResult(200,"修改成功",null);
        }
    }

    @CrossOrigin
    @PostMapping("/api/cover")
    @ResponseBody
    public String uploadimg(@RequestParam("a")MultipartFile mFile) throws IOException {
        System.out.println("!!!img!!!");
        // 定义存储图片的地址
        String folder = "D:/workspace/img";
        // 文件夹
        File imgFolder = new File(folder);
        // 获取文件名
        String fname = mFile.getOriginalFilename();
        // 获取文件后缀
        String ext = "." + fname.substring(fname.lastIndexOf(".")+1);
        // 获取时间字符串
        String dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        // 生成新的文件名
        String newFileName = dateTimeFormatter + UUID.randomUUID().toString().replaceAll("-","") + ext;
        // 文件在本机的全路径
        File filePath = new File(imgFolder, newFileName);
        if (!filePath.getParentFile().exists()){
            filePath.getParentFile().mkdirs();
        }
        try{
            mFile.transferTo(filePath);
            // 返回文件名
            String imgURL = "http://localhost:8443/api/file/" + filePath.getName();
            return imgURL;
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    @CrossOrigin
    @PostMapping("/api/cover2")
    @ResponseBody
    public URL uploadimg2(@RequestParam("a")MultipartFile mFile) throws IOException {
        System.out.println("!!!img!!!");
        // 定义存储图片的地址
        String folder = "D:/workspace/img";
        // 文件夹
        File imgFolder = new File(folder);
        // 获取文件名
        String fname = mFile.getOriginalFilename();
        // 获取文件后缀
        String ext = "." + fname.substring(fname.lastIndexOf(".")+1);
        // 获取时间字符串
        String dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        // 生成新的文件名
        String newFileName = dateTimeFormatter + UUID.randomUUID().toString().replaceAll("-","") + ext;
        // 文件在本机的全路径
        File filePath = new File(imgFolder, newFileName);
        if (!filePath.getParentFile().exists()){
            filePath.getParentFile().mkdirs();
        }
        try{
            mFile.transferTo(filePath);
            // 返回文件名
            String imgURL = "http://localhost:8443/api/file/" + filePath.getName();
            URL url=new URL();
            url.setName("url");
            url.setUrl(imgURL);
            return url;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
