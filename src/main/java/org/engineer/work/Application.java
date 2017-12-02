package org.engineer.work;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.facade.UserFacade;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@SpringBootApplication
public class Application {

    @Resource
    private UserFacade userFacade;
    @Resource
    private PasswordEncoder passwordEncoder;

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {
        if (userFacade.getUserByUsername("Admin") == null) {
            final UserDTO userDTO = new UserDTO();
            userDTO.setUsername("Admin");
            userDTO.setPassword(passwordEncoder.encode("nimda"));
            userDTO.setRole(AuthorityRoles.ADMIN);
            userDTO.setEnabled((byte) 1);
            userFacade.createUser(userDTO);
        }
    }
}
