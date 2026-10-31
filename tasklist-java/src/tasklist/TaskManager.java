package tasklist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 任务仓库。数据就存在一个 ArrayList 里 —— 这题的主角。
 *
 * ArrayList 的几个点在这里都用到了：
 *   add(e)          尾部追加，均摊 O(1)
 *   get(i)          按下标随机访问，O(1)（底层是数组，这是它比 LinkedList 强的地方）
 *   remove(int)     按下标删，返回被删元素
 *   remove(Object)  按对象删（走 equals），返回 boolean —— 和上面那个长得很像，容易写错
 *   removeIf(条件)   按条件批量删，内部会自己处理下标偏移
 */
public class TaskManager {

    /** 只依赖接口 Task，不依赖 SimpleTask —— 换实现类这个字段不用动 */
    private final ArrayList<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    /** 新增任务，返回刚创建的那个（方便调用方继续用） */
    public Task add(String title, Priority priority) {
        Task task = new SimpleTask(nextId++, title, priority);
        tasks.add(task);
        return task;
    }

    /**
     * 按条件查询。注意返回的是新的 ArrayList 副本：
     * 如果直接把内部那个 list 交出去，外面一句 list.clear() 就把你的数据删了。
     */
    public List<Task> list(TaskFilter filter) {
        List<Task> result = new ArrayList<>();
        for (Task t : tasks) {
            if (filter.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    /** 按编号找任务。找不到用 Optional 明确表达"可能没有"，比返回 null 安全 */
    public Optional<Task> find(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    /** 标记完成，返回是否真的找到了这个 id */
    public boolean complete(int id) {
        Optional<Task> found = find(id);
        found.ifPresent(Task::markDone);
        return found.isPresent();
    }

    /**
     * 按编号删除。
     * 这里必须用 tasks.remove(i)（下标版），而不是 tasks.remove(task)（对象版）——
     * 后者要靠 equals 判断相等，而 SimpleTask 没有重写 equals，永远删不掉。
     */
    public Optional<Task> remove(int id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                return Optional.of(tasks.remove(i));
            }
        }
        return Optional.empty();
    }

    /** 清空时连编号一起归零，下次新增从 #1 开始 */
    public void clear() {
        tasks.clear();
        nextId = 1;
    }

    public int size() {
        return tasks.size();
    }

    public long pendingCount() {
        long n = 0;
        for (Task t : tasks) {
            if (!t.isDone()) {
                n++;
            }
        }
        return n;
    }

    /**
     * 展示顺序：未完成在前 -> 优先级高的在前 -> 编号小的在前。
     * 枚举的 compareTo 按声明顺序比，所以 Priority 里 HIGH 写在最前面正好用得上。
     */
    public Comparator<Task> defaultOrder() {
        return Comparator.comparing(Task::isDone)
                .thenComparing(Task::getPriority)
                .thenComparingInt(Task::getId);
    }
}
