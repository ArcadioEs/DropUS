package org.engeneer.work.model;

import javax.persistence.*;


/**
 * Entity representing User in the system.
 */
@Entity
@Table(name = "USER_ENTITY")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String username;

    public UserEntity(String userName) {
        this.username = userName;
    }

    protected UserEntity() {
    }

    @Override
    public String toString() {
        return String.format(
                "User with id=%d and username='%s'",
                id, username);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
