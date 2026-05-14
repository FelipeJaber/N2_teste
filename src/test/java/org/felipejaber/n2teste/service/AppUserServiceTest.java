package org.felipejaber.n2teste.service;

import org.felipejaber.n2teste.model.AppUser;
import org.felipejaber.n2teste.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    void deveAutenticarUsuarioComCredenciaisValidas() {
        AppUser usuario = new AppUser("admin", "admin123");
        usuario.setId(1L);

        when(appUserRepository.findByUsernameAndPassword("admin", "admin123"))
                .thenReturn(Optional.of(usuario));

        Optional<AppUser> resultado = appUserService.findByUsernameAndPassword("admin", "admin123");

        assertTrue(resultado.isPresent());
        assertEquals("admin", resultado.get().getUsername());

        verify(appUserRepository, times(1))
                .findByUsernameAndPassword("admin", "admin123");
    }

    @Test
    void naoDeveAutenticarUsuarioComCredenciaisInvalidas() {
        when(appUserRepository.findByUsernameAndPassword("usuario_errado", "senha_errada"))
                .thenReturn(Optional.empty());

        Optional<AppUser> resultado = appUserService.findByUsernameAndPassword("usuario_errado", "senha_errada");

        assertTrue(resultado.isEmpty());

        verify(appUserRepository, times(1))
                .findByUsernameAndPassword("usuario_errado", "senha_errada");
    }
}