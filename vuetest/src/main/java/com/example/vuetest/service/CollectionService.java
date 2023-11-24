package com.example.vuetest.service;

import com.example.vuetest.dao.CollectionDAO;
import com.example.vuetest.pojo.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CollectionService {
    @Autowired
    CollectionDAO collectionDAO;
    public List<Collection> findByUserid(int id) {
        List clist=collectionDAO.findByUserid(id);
        return clist;
    }

    public void updateCollection(Collection collection, String localTime) {
        System.out.println(collection.getCollectionid());
        Collection cnew = collectionDAO.findByCollectionid(collection.getCollectionid());
        cnew.setSubmittime(localTime);
        cnew.setUserid(collection.getUserid());
        cnew.setFoodid(collection.getFoodid());
        collectionDAO.save(cnew);
    }

    public void deleteByCollectionid(int collectionid) {
        collectionDAO.deleteByCollectionid(collectionid);
    }

    public int addCollection(Collection collection) {
        collectionDAO.save(collection);
        return collection.getCollectionid();
    }


    public Collection findByCollectionid(int collectionid) {
        return collectionDAO.findByCollectionid(collectionid);
    }

    public Collection isExistUseridAndFoodid(int userid, int foodid) {
        return collectionDAO.getByUseridAndFoodid(userid,foodid);
    }

}
