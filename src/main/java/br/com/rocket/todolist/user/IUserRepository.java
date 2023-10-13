package br.com.rocket.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
  // extende a classe padrao do jpa
  UserModel findByUsername(String username); // Cria automaticamente um select utilizado o findBy
}
