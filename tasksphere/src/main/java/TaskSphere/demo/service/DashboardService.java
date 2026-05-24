package TaskSphere.demo.service;

import TaskSphere.demo.dto.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDashboard(String userId);
}
