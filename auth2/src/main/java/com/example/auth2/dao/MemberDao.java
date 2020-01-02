package com.example.auth2.dao;

import com.example.auth2.entity.Member;

/**
 * @ClassName MemberDao
 * @Author LIUHANPENG
 * @Date 2020/1/2 0002 17:21
 **/
public interface MemberDao {
    /**
     * {@link Member}
     * 根据会员名查找会员
     * @param memberName
     * @return
     */
    Member findByMemberName(String memberName);
}
