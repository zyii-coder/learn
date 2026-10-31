package tasklist;

/**
 * 过滤条件接口 —— 接口的第二种典型用法：把"一段行为"当成参数传进去。
 *
 * 只有一个抽象方法的接口可以标 @FunctionalInterface，
 * 调用处就能直接写 lambda：manager.list(t -> !t.isDone());
 * 这里顺手提供了几个现成的过滤器常量，省得每次重复写。
 */
@FunctionalInterface
public interface TaskFilter {

    boolean test(Task task);

    TaskFilter ALL = t -> true;
    TaskFilter DONE = Task::isDone;
    TaskFilter PENDING = t -> !t.isDone();

    /** 按命令里的关键字挑过滤器 */
    static TaskFilter of(String keyword) {
        if ("done".equalsIgnoreCase(keyword)) {
            return DONE;
        }
        if ("pending".equalsIgnoreCase(keyword)) {
            return PENDING;
        }
        return ALL;
    }
}
