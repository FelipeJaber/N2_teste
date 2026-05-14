package org.felipejaber.n2teste;

import org.felipejaber.n2teste.model.AppUser;
import org.felipejaber.n2teste.model.Book;
import org.felipejaber.n2teste.repository.AppUserRepository;
import org.felipejaber.n2teste.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final BookRepository bookRepository;

    public DataInitializer(AppUserRepository appUserRepository, BookRepository bookRepository) {
        this.appUserRepository = appUserRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            if (appUserRepository.findAll().isEmpty()) {
                AppUser admin = new AppUser("admin", "admin123");
                appUserRepository.save(admin);
                System.out.println("Usuário admin criado com sucesso");
            }

            if (bookRepository.findAll().isEmpty()) {
                bookRepository.save(new Book("O Senhor dos Anéis", "J.R.R. Tolkien", 85.90, "Fantasia"));
                bookRepository.save(new Book("1984", "George Orwell", 45.00, "Ficção Científica"));
                bookRepository.save(new Book("Dom Casmurro", "Machado de Assis", 35.50, "Romance"));
                System.out.println("Livros criados com sucesso");
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

