package br.com.rocket.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data // Para geração automatica de getters e setters
@Entity(name = "tb_users") // Define como uma tabela do banco de dados
public class UserModel {

  @Id
  @GeneratedValue(generator = "UUID") // Para geração automatica
  private UUID id;

  @Column(unique = true)
  private String username;
  private String name;
  private String password;

  @CreationTimestamp
  private LocalDateTime createdAt;

}
