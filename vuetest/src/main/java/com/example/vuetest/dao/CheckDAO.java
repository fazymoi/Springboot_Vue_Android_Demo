package com.example.vuetest.dao;
import com.example.vuetest.pojo.Check;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface CheckDAO extends JpaRepository<Check,Integer> {
    @Modifying
    @Transactional
    void deleteByCheckid(int checkid);

    Check getByUseridAndStoreid(int userid, int storeid);

    Check getByCheckid(int checkid);
}
