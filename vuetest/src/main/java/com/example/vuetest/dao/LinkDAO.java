package com.example.vuetest.dao;

import com.example.vuetest.pojo.Link;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface LinkDAO extends JpaRepository<Link,Integer> {
    Sort sort = null;
    List<Link> findByUseridIs(int userid,Sort sort);

    Link getByUseridAndStoreid(int userid, int storeid);
    @Modifying
    @Transactional
    void deleteByStoreid(int storeid);
}
