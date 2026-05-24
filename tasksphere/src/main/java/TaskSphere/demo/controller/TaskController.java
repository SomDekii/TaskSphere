package TaskSphere.demo.controller;

import TaskSphere.demo.dto.StatusUpdateRequest;
import TaskSphere.demo.dto.TaskRequest;
import TaskSphere.demo.dto.TaskResponse;
import TaskSphere.demo.entity.User;
import TaskSphere.demo.service.CurrentUserService;
import TaskSphere.demo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final CurrentUserService currentUserService;

    public TaskController(TaskService taskService, CurrentUserService currentUserService) {
        this.taskService = taskService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<TaskResponse> getTasks(Authentication authentication,
                                       @RequestParam(required = false) String search,
                                       @RequestParam(required = false) String status,
                                       @RequestParam(required = false) String priority,
                                       @RequestParam(required = false) String type,
                                       @RequestParam(required = false) String sortBy,
                                       @RequestParam(required = false) String assignee) {
        User user = currentUserService.getUser(authentication);
        return taskService.getTasks(user.getId(), search, status, priority, type, sortBy, assignee);
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(Authentication authentication, @PathVariable String id) {
        User user = currentUserService.getUser(authentication);
        return taskService.getTask(user.getId(), id);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(Authentication authentication,
                                                   @Valid @RequestBody TaskRequest request) {
        User user = currentUserService.getUser(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(user.getId(), request));
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(Authentication authentication,
                                   @PathVariable String id,
                                   @Valid @RequestBody TaskRequest request) {
        User user = currentUserService.getUser(authentication);
        return taskService.updateTask(user.getId(), id, request);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(Authentication authentication,
                                     @PathVariable String id,
                                     @Valid @RequestBody StatusUpdateRequest request) {
        User user = currentUserService.getUser(authentication);
        return taskService.updateStatus(user.getId(), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(Authentication authentication, @PathVariable String id) {
        User user = currentUserService.getUser(authentication);
        taskService.deleteTask(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
