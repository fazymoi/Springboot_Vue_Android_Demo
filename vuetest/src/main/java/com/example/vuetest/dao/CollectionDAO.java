package com.example.vuetest.dao;
import com.example.vuetest.pojo.Collection;
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

public interface CollectionDAO extends JpaRepository<Collection,Integer>{

    List<Collection> findByUserid(int id);

    Collection findByCollectionid(int collectionid);
    @Modifying
    @Transactional
    void deleteByCollectionid(int Collectionid);

    List<Collection> findByFoodid(int foodid);

    Collection getByUseridAndFoodid(int userid, int foodid);
}
