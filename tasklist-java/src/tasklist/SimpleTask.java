package tasklist;

/**
 * Task 的一个具体实现。字段全是 private final（除完成状态），只能通过方法改，
 * 这就是封装：外面拿不到 title 的引用，也就没法绕过校验乱改。
 */
public class SimpleTask implements Task {

    private final int id;
    private final String title;
    private final Priority priority;
    private boolean done;

    public SimpleTask(int id, String title, Priority priority) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("任务标题不能为空");
        }
        this.id = id;
        this.title = title.trim();
        this.priority = (priority == null ? Priority.MEDIUM : priority);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void markDone() {
        this.done = true;
    }

    @Override
    public String render() {
        return String.format("[%s] #%-2d (%s) %s",
                done ? "x" : " ", id, priority.label(), title);
    }

    @Override
    public String toString() {
        return render();
    }
}
