package lt.bit.products.ui.service;

import lt.bit.products.ui.service.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

@Service
@SessionAttributes("authenticated")
public class UserService {

  private final UserRepository repository;

  boolean authenticated;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public void login(String username, String password) {
    if (repository.existsByUsernameAndPassword(username, password)) {
      setAuthenticated(true);
    }
  }

  public void logout() {
    setAuthenticated(false);
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  private void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }
}
