package TaskSphere.demo.service.factory;

public class PersonalTask implements TaskBehavior {
    @Override
    public String creationMessage(String title) {
        return "Personal task created: " + title;
    }
}
