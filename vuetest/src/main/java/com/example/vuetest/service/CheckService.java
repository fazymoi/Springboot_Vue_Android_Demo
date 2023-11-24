package com.example.vuetest.service;

import com.example.vuetest.dao.CheckDAO;
import com.example.vuetest.pojo.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CheckService {
    @Autowired
    CheckDAO checkDAO;
    public List<Check> findall() {
        Sort sort = Sort.by(Sort.Direction.ASC, "checkid");
        List<Check> check = checkDAO.findAll(sort);
        return check;
    }
    public void AddCheck(Check check) {
        System.out.println(check.getStoreid()+check.getUserid()+check.getSubmittime());
        checkDAO.save(check);
    }

    public void deleteByCheckid(int checkid) {
        checkDAO.deleteByCheckid(checkid);
    }

    public Check getsameUseridandStoreid(int userid, int storeid) {
        return checkDAO.getByUseridAndStoreid(userid,storeid);
    }

    public void UpdateCheck(int userid, int storeid,String localtime) {
        Check newcheck =getsameUseridandStoreid(userid,storeid);
        newcheck.setSubmittime(localtime);
        checkDAO.save(newcheck);
    }
}
