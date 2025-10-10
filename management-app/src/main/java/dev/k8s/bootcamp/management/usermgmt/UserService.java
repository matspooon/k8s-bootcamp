package dev.k8s.bootcamp.management.usermgmt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repo;

  public List<User> list() {
    return repo.findAll();
  }

  public User save(User user) {
    return repo.save(user);
  }

  public void delete(Long id) {
    repo.deleteById(id);
  }
}
