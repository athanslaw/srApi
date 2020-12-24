package com.edunge.srtool.config;

import com.edunge.srtool.model.Authority;
import com.edunge.srtool.model.AuthorityName;
import com.edunge.srtool.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private AuthorityRepository authorityRepository;
    @Override
    public void run(String... args) throws Exception {
        Authority user = authorityRepository.findByName(AuthorityName.ROLE_USER);
        Authority admin = authorityRepository.findByName(AuthorityName.ROLE_ADMIN);

        if(user==null || admin==null){
            Authority authUser = new Authority();
            authUser.setName(AuthorityName.ROLE_USER);
            authorityRepository.save(authUser);
            Authority authAdmin = new Authority();
            authAdmin.setName(AuthorityName.ROLE_ADMIN);
            authorityRepository.save(authAdmin);
        }
    }
}
