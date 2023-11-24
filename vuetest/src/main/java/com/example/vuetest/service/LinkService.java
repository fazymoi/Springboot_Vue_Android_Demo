package com.example.vuetest.service;

import com.example.vuetest.dao.LinkDAO;
import com.example.vuetest.pojo.Link;
import com.example.vuetest.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LinkService {
    @Autowired
    LinkDAO linkDAO;

    public List<Link> link_mystore(int userid) {
        Sort sort = Sort.by(Sort.Direction.ASC, "storeid");
        List<Link> link = linkDAO.findByUseridIs(userid, sort);
        return link;
    }

    public void Addlink(Link link) {
        linkDAO.save(link);
    }
    public boolean isExist(int userid,int storeid) {
        Link link = linkDAO.getByUseridAndStoreid(userid,storeid);
        return null!=link;
    }

    public void deleteByStoreid(int id) {
        linkDAO.deleteByStoreid(id);
    }
}
