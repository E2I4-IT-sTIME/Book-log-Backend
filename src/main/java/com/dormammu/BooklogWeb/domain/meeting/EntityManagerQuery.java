package com.dormammu.BooklogWeb.domain.meeting;

import com.dormammu.BooklogWeb.dto.GetMeetingRes;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class EntityManagerQuery {

    @PersistenceContext
    private EntityManager entityManager;

    // 모임이름, onoff, cur_num, max_num, 해시태그, image
    public List<GetMeetingRes> mfindBymName(String mName){
        JpaResultMapper result = new JpaResultMapper();
        Query query = entityManager.createNativeQuery("SELECT m.name as mName, m.image as image, m.onoff as onoff, m.cur_num as cur_num, m.max_num as max_num, m.tags as tags" +
                        " FROM meeting m WHERE m.name like :mName")
                .setParameter("mName", "%"+mName+"%");
        List<GetMeetingRes> getMeetingRes = result.list(query, GetMeetingRes.class);
        return getMeetingRes;
    }

    public List<GetMeetingRes> mfindByCategory(String mName){
        JpaResultMapper result = new JpaResultMapper();
        Query query = entityManager.createNativeQuery("SELECT m.name as mName, m.image as image, m.onoff as onoff, m.cur_num as cur_num, m.max_num as max_num, m.tags as tags" +
                "FROM meeting m WHERE ");  // 해당 태그가 있는지 확인하고, 포함되어있다면 반환하기
        List<GetMeetingRes> getMeetingRes = result.list(query, GetMeetingRes.class);
        return getMeetingRes;
    }
}
