package TaskSphere.demo.service.factory;

public class StudyTask implements TaskBehavior {
    @Override
    public String creationMessage(String title) {
        return "Study task created: " + title;
    }
}
