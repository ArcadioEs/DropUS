package org.engineer.work.dto;

import java.util.List;

/**
 * User data transfer object.
 */
public class UserDTO {

    private String username;
    private String password;
    private byte enabled;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public byte getEnabled() {
        return enabled;
    }

    public void setEnabled(final byte enabled) {
        this.enabled = enabled;
    }
}
