package TaskSphere.demo.controller;

import TaskSphere.demo.dto.DashboardResponse;
import TaskSphere.demo.entity.User;
import TaskSphere.demo.service.CurrentUserService;
import TaskSphere.demo.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    private final CurrentUserService currentUserService;

    public DashboardController(DashboardService dashboardService, CurrentUserService currentUserService) {
        this.dashboardService = dashboardService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public DashboardResponse getDashboard(Authentication authentication) {
        User user = currentUserService.getUser(authentication);
        return dashboardService.getDashboard(user.getId());
    }
}
