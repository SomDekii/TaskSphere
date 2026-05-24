package TaskSphere.demo.service.factory;

public class WorkTask implements TaskBehavior {
    @Override
    public String creationMessage(String title) {
        return "Work task created: " + title;
    }
}
