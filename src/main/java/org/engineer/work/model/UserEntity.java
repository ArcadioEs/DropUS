package org.engineer.work.model;

import org.engineer.work.dto.UserDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Entity representing User in the system.
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(nullable = false, unique = true)
    @Size(max = 15)
    private String username;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;
    @Column(nullable = false)
    private byte enabled;
    @Column(nullable = false)
    private String role;

    public UserEntity(final UserDTO userDTO) {
        this.setUsername(userDTO.getUsername());
        this.setPassword(userDTO.getPassword());
        this.setEnabled(userDTO.getEnabled());
        this.setRole(userDTO.getRole());
    }

    protected UserEntity() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        if (role != null) {
            this.role = role;
        } else {
            throw new IllegalArgumentException("Field role must not be null");
        }
    }

    public byte getEnabled() {
        return enabled;
    }

    public void setEnabled(final byte enabled) {
        if (enabled == (byte) 0 || enabled == (byte) 1) {
            this.enabled = enabled;
        } else {
            throw new IllegalArgumentException("Enabled has to be 0 or 1");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        if (username != null) {
            this.username = username;
        } else {
            throw new IllegalArgumentException("Field username must not be null");
        }
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        if (password != null) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Field password must not be null");
        }
    }
}
