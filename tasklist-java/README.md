# 命令行任务清单（Java 练习）

用**类 + 接口 + ArrayList** 写的极简待办工具，纯控制台，无第三方依赖，Java 8 及以上都能跑。

## 目录结构

```
tasklist-java/
├── src/tasklist/
│   ├── Task.java         接口：一个任务该有什么能力（getId / getTitle / isDone / render ...）
│   ├── TaskFilter.java   接口：过滤条件，@FunctionalInterface，直接用 lambda 传
│   ├── Priority.java     枚举：高 / 中 / 低，声明顺序天然就是排序顺序
│   ├── SimpleTask.java   类：Task 的实现，字段私有、只暴露方法（封装）
│   ├── TaskManager.java  类：内部就是 ArrayList<Task>，负责增删改查和排序
│   └── Main.java         类：命令行交互，解析输入、打印结果
├── .vscode/
│   ├── settings.json     Java 扩展的源码路径与输出目录
│   └── launch.json       F5 调试配置
├── run.bat               Windows 编译 + 运行
└── run.sh                Git Bash / macOS / Linux 编译 + 运行
```

## 在 VSCode 里跑

用 VSCode 打开本目录（`tasklist-java`，不是仓库根目录），装好 Extension Pack for Java 之后：

- **按 `F5`** → 选「任务清单（交互模式）」，在集成终端里跑，可以直接敲命令
- **按 `Ctrl+Shift+B`** → 跑默认生成任务，效果同 `run.bat`（会先编译再启动）
- 或者点 `Main.java` 里 `main` 方法上方的 `Run`
- 退出：输入 `exit`，或 `Ctrl+C`

`.vscode/` 里已经配好三件事：

| 文件 | 作用 |
| --- | --- |
| `settings.json` | 告诉 Java 扩展源码在 `src/`、编译产物去 `out/` |
| `launch.json` | F5 调试配置，`console` 设成 `integratedTerminal` |
| `tasks.json` | `Ctrl+Shift+B` 调 `run.bat` |

> `console` 必须是 `integratedTerminal`。程序用 `Scanner` 读键盘，VSCode 默认的
> 调试控制台（`internalConsole`）**不能输入**，会看起来像卡死。

## 怎么跑（命令行）

需要 JDK（只装 JRE 不行，要有 `javac`）。两个脚本都会**自己找 JDK**：先看 `JAVA_HOME`，
没有再在 `~/.jdks` 下找一个能用的。所以就算终端的 `PATH` 还没刷新、甚至环境变量没生效，
直接跑也不会报"找不到 javac"。

Windows：

```bat
run.bat
```

Git Bash / macOS / Linux：

```bash
bash run.sh
```

带参数执行一条命令就退出（不进交互）：

```bat
run.bat add 买牛奶 #h
```

也可以手动编译：

```bash
javac -encoding UTF-8 -d out src/tasklist/*.java
java -cp out tasklist.Main
```

> `-encoding UTF-8` 不能省，否则中文源码在 Windows 上会编译报错。
>
> **关于中文乱码**：程序里没有硬编码输出编码，由 JVM 按控制台实际代码页决定
> （JDK 18+ 的 `stdout.encoding` 机制）。所以 VSCode 终端、`cmd` 里直接跑都正常；
> 但 Git Bash 的终端是 UTF-8、Windows 报给 JVM 的代码页却是 936，两边对不上，
> 这时候要靠 `run.sh` 里的 `-Dstdout.encoding=UTF-8` 显式声明。
> 编码交给启动脚本管，程序本身不猜 —— 这是更省心的做法。

## 命令

| 命令 | 说明 |
| --- | --- |
| `add 买牛奶 #h` | 新增任务，`#h` 高 / `#m` 中 / `#l` 低，不写默认中 |
| `list` | 列出全部（未完成在前，优先级高的在前） |
| `list pending` | 只看未完成 |
| `list done` | 只看已完成 |
| `done 1` | 把 #1 标记为完成 |
| `del 1` | 删除 #1 |
| `clear` | 清空 |
| `help` / `exit` | 帮助 / 退出 |

也支持一次性执行，方便写脚本或测试：

```bash
java -cp out tasklist.Main "add 写作业 #h"     # 注意：bash 里裸写 #h 会被当成注释
java -cp out tasklist.Main list
```

> 任务目前只存在内存里，进程退出就没了。所以一次性执行主要用于验证命令解析；
> 想要"关掉还在"，看下面第 1 条练习。

## 题目要求对应在哪

- **接口**：`Task` 定义能力，`TaskManager` 全程只认 `Task` 不认 `SimpleTask`；`TaskFilter` 演示"把行为当参数"，`manager.list(t -> !t.isDone())` 这样传。
- **类**：`SimpleTask` 做实现，`TaskManager` 管数据，`Main` 管输入输出，三层各管一件事。
- **ArrayList**：`TaskManager.tasks` 是 `ArrayList<Task>`，用到 `add` / `get(i)` / `remove(i)` / `clear` / 增强 for 遍历。
  - 注意 `remove(int)` 是按下标删、返回被删元素；`remove(Object)` 是按 `equals` 删、返回 `boolean`。这俩长一样，是新手最容易踩的坑。
  - `list()` 返回的是新 `ArrayList` 副本，不把内部容器暴露出去。

## 想接着练可以加的东西

1. **持久化**：退出时把任务写成 `tasks.csv`，启动时读回来（`Files.write` / `Files.readAllLines`）。
2. **新任务类型**：加一个 `DeadlineTask implements Task`，带截止日期，看 `TaskManager` 是不是真的不用改。
3. **排序切换**：`list --by-id`，把 `defaultOrder()` 换成另一个 `Comparator`。
4. **单元测试**：给 `TaskManager` 写 JUnit 测试，验证"删不存在的编号不会炸"。
