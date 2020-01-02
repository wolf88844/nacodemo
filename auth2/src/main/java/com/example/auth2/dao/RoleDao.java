package com.example.auth2.dao;

import com.example.auth2.entity.Role;

import java.util.List;

/**
 * {@link com.example.auth2.entity.Role}
 * @ClassName RoleDao
 * @Author LIUHANPENG
 * @Date 2020/1/2 0002 17:23
 **/
public interface RoleDao {
    /**
     * 根据用户id查找角色列表
     * @param memberId
     * @return
     */
    List<Role> findByMemberId(Integer memberId);
}
