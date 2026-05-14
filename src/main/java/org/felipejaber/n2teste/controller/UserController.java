package org.felipejaber.n2teste.controller;

import org.felipejaber.n2teste.model.AppUser;
import org.felipejaber.n2teste.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AppUserService appUserService;

    @GetMapping
    public List<AppUser> getAllUsers() {
        return appUserService.findAll();
    }

    @GetMapping("/{id}")
    public AppUser getUserById(@PathVariable Long id) {
        return appUserService.findById(id).orElse(null);
    }

    @PostMapping
    public AppUser createUser(@RequestBody AppUser appUser) {
        return appUserService.save(appUser);
    }

    @PutMapping("/{id}")
    public AppUser updateUser(@PathVariable Long id, @RequestBody AppUser appUser) {
        appUser.setId(id);
        return appUserService.save(appUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        appUserService.deleteById(id);
    }

    @PostMapping("/login")
    public AppUser login(@RequestBody AppUser loginUser) {
        Optional<AppUser> user = appUserService.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());
        return user.orElse(null);
    }
}
