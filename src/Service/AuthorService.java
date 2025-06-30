package Service;

import DAO.AuthorDAO;

import java.util.concurrent.CompletableFuture;

public class AuthorService {

    private final AuthorDAO authorDAO;

    public AuthorService(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    public CompletableFuture<String> findAuthorNameByKey(String authorKey) {
        // Simplemente delega la llamada al repositorio
        return authorDAO.findAuthorNameByKey(authorKey);
    }
}
