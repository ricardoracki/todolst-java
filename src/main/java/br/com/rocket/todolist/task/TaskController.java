package br.com.rocket.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rocket.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    // Recuperando o id do usuario da requisição que é inserido no filter
    var idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID) idUser);

    // Validações de datas
    var currentDate = LocalDateTime.now();
    if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("A data de início e de término devem ser maior que a data atual");
    }

    if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("A data de início deve ser menor que a data de término");
    }

    // Criando a tarefa e retornando
    var task = this.taskRepository.save(taskModel);
    return ResponseEntity.status(201).body(task);
  }

  @GetMapping("/")
  public List<TaskModel> list(HttpServletRequest request) {
    var tasks = this.taskRepository.findByIdUser((UUID) request.getAttribute("idUser"));
    return tasks;
  }

  @PutMapping("/{id}")
  public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
    var idUser = request.getAttribute("idUser");
    var task = this.taskRepository.findById(id).orElse(null);

    if (task == null) {
      return ResponseEntity.status(400).body("A  tarefa não encontrada");
    }

    if (!task.getIdUser().equals(idUser)) {
      return ResponseEntity.status(401).body("Esta task não é sua");
    }

    Utils.copyNonNUllProperties(taskModel, task);

    var taskSaved = this.taskRepository.save(task);
    return ResponseEntity.status(200).body(taskSaved);
  }

}
