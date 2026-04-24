package checker.engine;

import java.util.LinkedHashMap;
import java.util.Map;

public class CheckResults {
    /** groupName, taskId, studentGithub, result. */
    private final Map<String, Map<String, Map<String, StudentTaskResult>>> results = new LinkedHashMap<>();
    /** studentGithub -> activity. */
    private final Map<String, StudentActivity> activity = new LinkedHashMap<>();

    public void putResult(String groupName, String taskId, String studentGithub, StudentTaskResult result) {
        results.computeIfAbsent(groupName, k -> new LinkedHashMap<>())
               .computeIfAbsent(taskId, k -> new LinkedHashMap<>())
               .put(studentGithub, result);
    }

    public Map<String, StudentTaskResult> getTaskResults(String groupName, String taskId) {
        return results.getOrDefault(groupName, Map.of()).getOrDefault(taskId, Map.of());
    }

    public void putActivity(String studentGithub, StudentActivity act) {
        activity.put(studentGithub, act);
    }

    public StudentActivity getActivity(String studentGithub) {
        return activity.getOrDefault(studentGithub, StudentActivity.UNKNOWN);
    }
}
