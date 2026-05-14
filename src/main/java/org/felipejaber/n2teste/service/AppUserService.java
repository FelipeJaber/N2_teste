package org.felipejaber.n2teste.service;

import org.felipejaber.n2teste.model.AppUser;
import org.felipejaber.n2teste.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> findById(Long id) {
        return appUserRepository.findById(id);
    }

    public AppUser save(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public void deleteById(Long id) {
        appUserRepository.deleteById(id);
    }

    public Optional<AppUser> findByUsernameAndPassword(String username, String password) {
        return appUserRepository.findByUsernameAndPassword(username, password);
    }
}
