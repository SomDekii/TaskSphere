package TaskSphere.demo.service;

import TaskSphere.demo.dto.StatusUpdateRequest;
import TaskSphere.demo.dto.TaskRequest;
import TaskSphere.demo.dto.TaskResponse;

import java.util.List;

public interface TaskService {
    List<TaskResponse> getTasks(String userId, String search, String status, String priority, String type, String sortBy, String assignee);
    TaskResponse getTask(String userId, String id);
    TaskResponse createTask(String userId, TaskRequest request);
    TaskResponse updateTask(String userId, String id, TaskRequest request);
    TaskResponse updateStatus(String userId, String id, StatusUpdateRequest request);
    void deleteTask(String userId, String id);
}
