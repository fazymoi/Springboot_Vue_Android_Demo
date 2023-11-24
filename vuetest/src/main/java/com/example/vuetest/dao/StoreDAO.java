package com.example.vuetest.dao;

import com.example.vuetest.pojo.Store;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreDAO extends JpaRepository<Store,Integer> {
    Sort sort = null;

    Store findByStorenameIs(String storename);
    Store findByStorecodeIs(String storecode);
    @Modifying
    @Transactional
    void deleteByStoreid(int id);

    Store findByStoreidIs(int storeid);
    @Query("From Store where storename like :searchstore and status =:status")
    List<Store> findByQuery(@Param("searchstore") String searchstore, @Param("status") String status, Sort sort);
    @Query("From Store where status =:status order by storeid asc ")
    List<Store> findstores(@Param("status") String status);

}
