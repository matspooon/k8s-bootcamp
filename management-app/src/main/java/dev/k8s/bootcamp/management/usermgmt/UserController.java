package dev.k8s.bootcamp.management.usermgmt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  @GetMapping
  public String page() {
    return "user-list";
  }

  @GetMapping("/data")
  @ResponseBody
  public List<User> list() {
    return service.list();
  }

  @PostMapping
  @ResponseBody
  public User save(@RequestBody User user) {
    return service.save(user);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
