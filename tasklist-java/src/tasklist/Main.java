package tasklist;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 命令行入口：读一行 -> 解析成命令 -> 交给 TaskManager -> 打印结果。
 * 这一层只做输入输出，业务逻辑全在 TaskManager 里，两边可以分别改。
 */
public class Main {

    private static final TaskManager manager = new TaskManager();
    /** 匹配标题末尾的优先级标记，例如 "买牛奶 #h" */
    private static final Pattern PRIORITY_TAG = Pattern.compile("^(.*?)\\s*#([hHmMlL])$");

    public static void main(String[] args) throws Exception {
        // 不要在代码里把 System.out 硬改成 UTF-8。
        // JDK 18+ 会按控制台实际代码页输出，GBK 终端和 chcp 65001 的终端都能正常显示中文；
        // 硬写 UTF-8 反而会让中文 Windows 的默认终端（代码页 936）变乱码。

        // try-with-resources：不管从哪条路径退出（exit 命令、Ctrl+D、参数模式），
        // Scanner 都会被关掉。写成手动 in.close() 的话，前面 return 的那条路径就漏了，
        // IDE 照样会提示 Resource leak。
        try (Scanner in = new Scanner(System.in)) {
            if (args.length > 0) {
                // 支持一次性执行：java -cp out tasklist.Main add 买牛奶 #h
                runLine(String.join(" ", args));
                return;
            }

            banner();
            while (true) {
                System.out.print("task> ");
                System.out.flush();
                if (!in.hasNextLine()) {          // Ctrl+D 或管道读完，直接退出
                    System.out.println();
                    break;
                }
                String line = in.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (!runLine(line)) {              // 返回 false 代表用户要退出
                    break;
                }
            }
            System.out.println("再见。");
        }
    }

    /** 执行一条命令，返回 false 表示退出程序 */
    private static boolean runLine(String line) {
        String[] parts = line.trim().split("\\s+");
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "add":
            case "a":
                doAdd(joinFrom(parts, 1));
                return true;
            case "list":
            case "ls":
            case "l":
                doList(parts.length > 1 ? parts[1] : "all");
                return true;
            case "done":
            case "d":
                doDone(parseId(parts, "done"));
                return true;
            case "del":
            case "rm":
                doDel(parseId(parts, "del"));
                return true;
            case "clear":
                manager.clear();
                System.out.println("已清空全部任务。");
                return true;
            case "help":
            case "h":
            case "?":
                help();
                return true;
            case "exit":
            case "quit":
            case "q":
                return false;
            default:
                System.out.println("不认识的命令：" + cmd + "，输入 help 看看。");
                return true;
        }
    }

    private static void doAdd(String raw) {
        if (raw.trim().isEmpty()) {
            System.out.println("用法：add 任务标题 [#h|#m|#l]");
            return;
        }
        Priority priority = Priority.MEDIUM;
        String title = raw.trim();

        Matcher m = PRIORITY_TAG.matcher(title);
        if (m.matches()) {
            title = m.group(1).trim();
            priority = Priority.fromTag(m.group(2).charAt(0));
        }

        try {
            Task task = manager.add(title, priority);
            System.out.println("已添加 " + task.render());
        } catch (IllegalArgumentException e) {
            System.out.println("添加失败：" + e.getMessage());
        }
    }

    private static void doList(String which) {
        if (!"all".equalsIgnoreCase(which)
                && !"done".equalsIgnoreCase(which)
                && !"pending".equalsIgnoreCase(which)) {
            System.out.println("用法：list [all|done|pending]");
            return;
        }

        List<Task> items = manager.list(TaskFilter.of(which));
        items.sort(manager.defaultOrder());

        if (items.isEmpty()) {
            System.out.println("（没有符合条件的任务）");
            return;
        }
        System.out.println("---- 显示 " + items.size() + " 项（共 " + manager.size()
                + " 项，未完成 " + manager.pendingCount() + " 项）----");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + items.get(i).render());
        }
    }

    private static void doDone(int id) {
        if (id < 0) {
            return;
        }
        if (manager.complete(id)) {
            System.out.println("已完成 #" + id + "，干得漂亮。");
        } else {
            System.out.println("没有编号为 #" + id + " 的任务。");
        }
    }

    private static void doDel(int id) {
        if (id < 0) {
            return;
        }
        Optional<Task> removed = manager.remove(id);
        if (removed.isPresent()) {
            System.out.println("已删除 " + removed.get().render());
        } else {
            System.out.println("没有编号为 #" + id + " 的任务。");
        }
    }

    /** 解析命令里第二个参数当编号；不合法返回 -1 并打印用法 */
    private static int parseId(String[] parts, String cmdName) {
        if (parts.length < 2) {
            System.out.println("用法：" + cmdName + " 任务编号，例如 " + cmdName + " 1");
            return -1;
        }
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            System.out.println("\"" + parts[1] + "\" 不是合法编号。");
            return -1;
        }
    }

    /** 把 parts 从下标 from 开始重新用空格拼起来，这样标题里带空格也不会被切断 */
    private static String joinFrom(String[] parts, int from) {
        StringBuilder sb = new StringBuilder();
        for (int i = from; i < parts.length; i++) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    private static void banner() {
        System.out.println("================ 命令行任务清单 ================");
        System.out.println(" 输入 help 查看命令，exit 退出");
        System.out.println("===============================================");
    }

    private static void help() {
        System.out.println();
        System.out.println("  add <标题> [#h|#m|#l]   新增任务，默认中优先级");
        System.out.println("                           例：add 买牛奶 #h");
        System.out.println("  list [all|done|pending] 列出任务，默认 all");
        System.out.println("  done <编号>              把任务标记为已完成");
        System.out.println("  del <编号>               删除任务");
        System.out.println("  clear                    清空所有任务");
        System.out.println("  help                     显示这份帮助");
        System.out.println("  exit                     退出");
        System.out.println();
    }
}
