package com.nangseakheng.user;

import com.nangseakheng.common.constant.ApiConstant;
import com.nangseakheng.user.constant.Constant;
import com.nangseakheng.user.entity.Role;
import com.nangseakheng.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
        return args -> {
            Role admin = new Role();
            admin.setId(null);
            admin.setName("ADMIN");
            admin.setDescription("Administrator");
            admin.setCreatedBy(Constant.SYSTEM);
            admin.setStatus(ApiConstant.ACTIVE.getKey());

            Role user = new Role();
            user.setId(null);
            user.setName("USER");
            user.setDescription("User");
            user.setCreatedBy(Constant.SYSTEM);
            user.setStatus(ApiConstant.ACTIVE.getKey());

            List<Role> roles = new ArrayList<>();
            roles.add(admin);
            roles.add(user);

            roles.stream()
                    .filter(role -> roleRepository.findFirstByName(role.getName())
                            .isEmpty()).forEach(roleRepository::saveAndFlush);
        };
    }
}