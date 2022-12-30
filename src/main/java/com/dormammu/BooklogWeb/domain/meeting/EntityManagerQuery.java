package com.dormammu.BooklogWeb.domain.meeting;

import com.dormammu.BooklogWeb.dto.GetCategoryRes;
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
        Query query = entityManager.createNativeQuery("SELECT m.name as name, m.image as image, m.onoff as onoff, m.cur_num as cur_num, m.max_num as max_num" +
                        " FROM meeting m WHERE m.name like :mName")
                .setParameter("mName", "%"+mName+"%");
        List<GetMeetingRes> getMeetingRes = result.list(query, GetMeetingRes.class);
        return getMeetingRes;
    }

    public List<GetCategoryRes> mfindByCategory(String tagName){
        JpaResultMapper result = new JpaResultMapper();
        Query query = entityManager.createNativeQuery("SELECT m.id as id, m.name as name, m.image as image, m.onoff as onoff, m.max_num as max_num, m.cur_num as cur_num, \n" +
                        "m.info as info, m.hashTag_id as hashTag_id,  h.tag1 as tag1, h.tag2 as tag2, h.tag3 as tag3, h.tag4 as tag4, h.tag5 as tag5\n" +
                        "FROM meeting m left outer join hastag h on h.id = m.hashTag_id\n" +
                        "WHERE h.tag1 like :tagName1 OR\n" +
                        "h.tag2 like :tagName2 OR\n" +
                        "h.tag3 like :tagName3 OR\n" +
                        "h.tag4 like :tagName4 OR\n" +
                        "h.tag5 like :tagName5")
                .setParameter("tagName1", "%"+tagName+"%")
                .setParameter("tagName2", "%"+tagName+"%")
                .setParameter("tagName3", "%"+tagName+"%")
                .setParameter("tagName4", "%"+tagName+"%")
                .setParameter("tagName5", "%"+tagName+"%");  // 해당 태그가 있는지 확인하고, 포함되어있다면 반환하기
        System.out.println("query 끝");
        List<GetCategoryRes> getCategoryRes = result.list(query, GetCategoryRes.class);
        return getCategoryRes;
    }
}
