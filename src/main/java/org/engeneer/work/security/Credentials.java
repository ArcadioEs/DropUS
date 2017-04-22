package org.engeneer.work.security;

/**
 *
 */
public class Credentials {
    private Long userId;
    private String password;

    public Credentials(Long userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
