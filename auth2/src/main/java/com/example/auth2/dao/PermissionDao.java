package com.example.auth2.dao;

import com.example.auth2.entity.Permission;

import java.util.List;

/**
 * {@link Permission}
 * @ClassName PermissionDao
 * @Author LIUHANPENG
 * @Date 2020/1/2 0002 17:22
 **/
public interface PermissionDao {

    /**
     * 根据角色id查找权限列表
     * @param roleId
     * @return
     */
    List<Permission> findByRoleId(Integer roleId);
}
