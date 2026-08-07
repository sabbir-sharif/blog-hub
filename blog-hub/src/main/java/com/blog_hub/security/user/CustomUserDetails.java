package com.blog_hub.security.user;

import com.blog_hub.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // We'll add roles later
        //return List.of();
        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().getName()
                )
        );
    }

    @Override
    public String getPassword() {

        return user.getPassword();

    }

    @Override
    public String getUsername() {

        // We login using email
        return user.getEmail();

    }

    @Override
    public boolean isAccountNonExpired() {

        return true;

    }

    @Override
    public boolean isAccountNonLocked() {

        return true;

    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;

    }

    @Override
    public boolean isEnabled() {

        return true;

    }

    public User getUser() {

        return user;

    }

}
