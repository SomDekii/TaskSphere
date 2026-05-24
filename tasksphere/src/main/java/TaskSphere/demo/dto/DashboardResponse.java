package TaskSphere.demo.dto;

import java.util.List;

public class DashboardResponse {
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long inProgressTasks;
    private List<TaskResponse> upcomingDeadlines;
    private List<NotificationResponse> recentNotifications;

    public DashboardResponse(long totalTasks, long completedTasks, long pendingTasks, long inProgressTasks,
                             List<TaskResponse> upcomingDeadlines,
                             List<NotificationResponse> recentNotifications) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
        this.inProgressTasks = inProgressTasks;
        this.upcomingDeadlines = upcomingDeadlines;
        this.recentNotifications = recentNotifications;
    }

    public long getTotalTasks() { return totalTasks; }
    public long getCompletedTasks() { return completedTasks; }
    public long getPendingTasks() { return pendingTasks; }
    public long getInProgressTasks() { return inProgressTasks; }
    public List<TaskResponse> getUpcomingDeadlines() { return upcomingDeadlines; }
    public List<NotificationResponse> getRecentNotifications() { return recentNotifications; }
}
