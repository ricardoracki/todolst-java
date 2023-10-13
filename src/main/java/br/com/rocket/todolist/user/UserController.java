package br.com.rocket.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity<String> create(@RequestBody UserModel userModel) {

    var user = this.userRepository.findByUsername(userModel.getName());
    if (user != null) {
      return ResponseEntity.status(400).body("Usuário já existe");
    }

    var passwordHashered = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
    userModel.setPassword(passwordHashered);

    this.userRepository.save(userModel);
    return ResponseEntity.status(201).body("UserCreated");
  }
}
