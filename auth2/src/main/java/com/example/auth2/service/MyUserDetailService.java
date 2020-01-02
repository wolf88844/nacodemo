package com.example.auth2.service;

import com.example.auth2.dao.MemberDao;
import com.example.auth2.entity.Member;
import com.example.auth2.entity.Permission;
import com.example.auth2.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName MyUserDetailService
 * @Author LIUHANPENG
 * @Date 2020/1/2 0002 17:11
 **/
@Service("userDetailService")
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private MemberDao memberDao;
    @Override
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {
        Member member = memberDao.findByMemberName(memberName);
        if(member == null){
            throw new UsernameNotFoundException(memberName);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        for(Role role:member.getRoles()){
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
            grantedAuthorities.add(grantedAuthority);
            for(Permission permission:role.getPermissions()){
                GrantedAuthority authority = new SimpleGrantedAuthority(permission.getUri());
                grantedAuthorities.add(authority);
            }
        }
        User user = new User(member.getMemberName(),member.getPassword(),
                enabled,accountNonExpired,credentialsNonExpired,accountNonLocked,grantedAuthorities);
        return user;
    }
}
