package org.engineer.work;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.engineer.work.dto.UserDTO;
import org.engineer.work.facade.UserFacade;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.engineer.work.service.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    private static final String DESTROY_DIRECTORIES_PROPERTY = "dropus.delete.directories";
    private static final String FILES_LOCATION = "dropus.files.root";

    @Resource
    private UserFacade userFacade;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private PropertiesService propertiesService;

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

    @PreDestroy
    public void destroyDirectories() {
        final boolean deleteDirectories = Boolean.parseBoolean(propertiesService.getProperty(DESTROY_DIRECTORIES_PROPERTY));
        if (deleteDirectories) {
            try {
                final String rootLocation = propertiesService.getProperty(FILES_LOCATION);
                FileUtils.deleteDirectory(new File(rootLocation));
            } catch (IOException e) {
                LOG.warn("Could not delete storage folders while shutting down");
            }
        }
    }
}
