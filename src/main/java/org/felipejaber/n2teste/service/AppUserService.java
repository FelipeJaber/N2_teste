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

    public AppUser update(Long id, AppUser appUserDetails) {
        Optional<AppUser> userOpt = appUserRepository.findById(id);
        if (userOpt.isPresent()) {
            AppUser existingUser = userOpt.get();
            if (appUserDetails.getUsername() != null) {
                existingUser.setUsername(appUserDetails.getUsername());
            }
            if (appUserDetails.getPassword() != null) {
                existingUser.setPassword(appUserDetails.getPassword());
            }
            return appUserRepository.save(existingUser);
        }
        throw new RuntimeException("User not found with id: " + id);
    }

    public void deleteById(Long id) {
        appUserRepository.deleteById(id);
    }

    public Optional<AppUser> findByUsernameAndPassword(String username, String password) {
        return appUserRepository.findByUsernameAndPassword(username, password);
    }
}
