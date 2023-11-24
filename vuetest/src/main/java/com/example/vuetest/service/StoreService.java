package com.example.vuetest.service;
import com.example.vuetest.dao.StoreDAO;
import com.example.vuetest.dao.UserDAO;
import com.example.vuetest.pojo.User;
import com.example.vuetest.pojo.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
@Service
public class StoreService {
    @Autowired
    StoreDAO storeDAO;
    public Store findByStoreid(int storeid) {
        return storeDAO.findByStoreidIs(storeid);
    }
    public boolean isStorenameExist(String Storename) {
        Store store = storeDAO.findByStorenameIs(Storename);
        return null!=store;
    }
    public void UpdateStore(Store store) {
        System.out.println(store.getStorename());
        Store storenew = storeDAO.findByStoreidIs(store.getStoreid());
        storenew.setContact(store.getContact());
        storenew.setIntroduction(store.getIntroduction());
        storenew.setOpeninghours(store.getOpeninghours());
        storenew.setStoreaddr(store.getStoreaddr());
        storenew.setStorecode(store.getStorecode());
        storenew.setStatus(store.getStatus());
        storenew.setStorename(store.getStorename());
        storenew.setCover(store.getCover());
        storeDAO.save(storenew);
    }

    public int AddStore(Store store) {

        storeDAO.save(store);
        return store.getStoreid();

    }

    public void deleteByStoreid(int id) {
        storeDAO.deleteByStoreid(id);
    }


    public boolean isStorecodeExist(String storecode) {
        Store store = storeDAO.findByStorecodeIs(storecode);
        return null!=store;
    }

    public int getsameStorecodeStoreid(String storecode) {
        Store store = storeDAO.findByStorecodeIs(storecode);
        if(store==null)
            return 0;
        else
            return store.getStoreid();
    }

    public int getsameStorenameStoreid(String storename) {
        Store store = storeDAO.findByStorenameIs(storename);
        if(store==null)
            return 0;
        else
            return store.getStoreid();
    }

    public void passStore(Store store) {
        Store storenew = storeDAO.findByStoreidIs(store.getStoreid());
        storenew.setStatus("审核通过");
        storeDAO.save(storenew);
    }
    public List<Store> search_stores(String searchstore){
        Sort sort = Sort.by(Sort.Direction.ASC, "storeid");
        searchstore="%"+searchstore+"%";
        String status="审核通过";
        List<Store> stores = storeDAO.findByQuery(searchstore,status,sort);
        return stores;
    }
    public void unpassStore(Store store, String status) {
        Store storenew = storeDAO.findByStoreidIs(store.getStoreid());
        storenew.setStatus(status);
        storeDAO.save(storenew);
    }
    public List<Store> allstores(){
        String status="审核通过";
        List<Store> stores = storeDAO.findstores(status);
        return stores;
    }
}
