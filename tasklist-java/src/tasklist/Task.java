package tasklist;

/**
 * 任务接口：只描述"一个任务对外应该提供什么"，不关心它怎么实现。
 *
 * 为什么要抽接口？因为 TaskManager 只依赖 Task，不依赖 SimpleTask。
 * 以后你想加带截止日期的 DeadlineTask、每天重复的 RecurringTask，
 * 只要 implements Task，管理器一行都不用改。这就是面向接口编程。
 */
public interface Task {

    /** 唯一编号，由 TaskManager 统一分配 */
    int getId();

    /** 任务标题 */
    String getTitle();

    /** 优先级 */
    Priority getPriority();

    /** 是否已完成 */
    boolean isDone();

    /** 标记为已完成（重复调用无副作用） */
    void markDone();

    /** 渲染成一行文本，例如："[x] #2 (高) 写作业" */
    String render();
}
