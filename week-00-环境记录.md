# 环境记录（week-00）

- 记录日期：2026-10-31
- 仓库：`learn`（个人学习仓库，远程 `https://github.com/zyii-coder/learn.git`）
- 标签：`week-00`（本阶段快照）

## 1. 工具版本

| 工具 | 版本 | 位置 |
|---|---|---|
| JDK | openjdk 21.0.13 2026-10-20 LTS（Temurin-21.0.13+9） | `C:\Users\DELL\.jdks\temurin-21` |
| Maven | Apache Maven 3.10.0（c43a36b8d67be7e0805a411bc0898af1a51f5472） | `D:\git(learn)\maven\apache-maven-3.10.0` |
| 操作系统 | Windows 11，amd64 | — |

`mvn -version` 实测输出：

```
Apache Maven 3.10.0 (c43a36b8d67be7e0805a411bc0898af1a51f5472)
Maven home: D:\git(learn)\maven\apache-maven-3.10.0
Java version: 21.0.13, vendor: Eclipse Adoptium, runtime: C:\Users\DELL\.jdks\temurin-21
Default locale: zh_SG, platform encoding: UTF-8, time zone: Asia/Shanghai
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

## 2. 构建验证（maven-lab 可构建的个人仓库）

命令：`mvn -o clean package`（离线，依赖已缓存于 `~/.m2`）

结果：**BUILD SUCCESS**

```
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
Building jar: D:\git(learn)\maven-lab\target\maven-lab.jar
```

产物：`maven-lab/target/maven-lab.jar`

运行验证：`java -jar target/maven-lab.jar "git branch and commit"`

```
文本   : git branch and commit
词数   : 4
字符数 : 21
最长词 : branch
平均词长: 4.50
```

> 注：本机 Bash 终端以 UTF-8 显示，JVM 按控制台代码页输出中文会显示为乱码；在 PowerShell / VS Code 终端（或先 `chcp 65001`）中显示正常，参见 maven-lab README。

## 3. tasklist-java（命令行任务清单，Java 小练习）

- 实现方式：类（`SimpleTask` / `TaskManager` / `Main`）+ 接口（`Task` / `TaskFilter`）+ `ArrayList<Task>`。
- 运行：在仓库根目录 PowerShell 执行 `.\tasklist-java\run.bat`（脚本自动 `javac` 编译后启动 `tasklist.Main`）。
- 本周改动（已提交进 week-00）：
  - `Main.java`：用 `try-with-resources` 包裹 `Scanner`，修复资源泄漏告警。
  - `README.md`：补充 PowerShell 运行说明，以及「不要用管道喂命令」的 GBK/UTF-8 编码警告。

## 4. 任务资源 REST 接口草稿（Markdown）

- 位置：`maven-lab/docs/api/tasks.md`（同时 `learn` 仓库内有 `2026-10-31-18-53-20/任务资源接口设计.md`）。
- 已覆盖四个接口：
  - 新增 `POST /api/v1/tasks` → `201`
  - 查询 `GET /api/v1/tasks` → `200`（分页）
  - 修改 `PUT /api/v1/tasks/{id}` → `200`（含乐观锁 `If-Match`）
  - 删除 `DELETE /api/v1/tasks/{id}` → `204`（幂等 + 软删除策略）
- 另含：统一错误响应、数据模型、权限矩阵、Spring Boot 映射参考、待确认清单。

## 5. Git 提交记录（learn 仓库，最近）

| SHA | 说明 |
|---|---|
| 6514bc8 | docs: 补充运行方式、JDK 自动查找与参数用法 |
| 7f74aad | chore(vscode): 添加默认生成任务，Ctrl+Shift+B 一键运行 |
| b8bc0c3 | docs: 添加任务资源 REST 接口设计草稿 |
| a992c56 | javatest |
| c87478a | 练习 |
| 160ffe6 | 新增水果 |
| 4c03bbe | docs: 添加常用命令速查表 |
| 10bf3ba | docs: 添加撤销回退与变基暂存笔记 |
| e752468 | docs: 添加远程仓库与协作笔记 |
| 8b42329 | docs: 添加基础命令与分支合并笔记 |
| 3ca1235 | docs: 建立笔记目录结构，统一换行符并更新 README 索引 |
| 714f252 | chore: 忽略本地 .workbuddy 工作目录 |
| a4be85c | chore: 初始化仓库，添加 README 与 .gitignore |

> 关联项目 `maven-lab` 是独立 git 仓库，已完成 branch / commit / push 练习（`main` + `feature/*`，tag `v0.1.0`，GitHub Actions CI），见其自身提交历史。

## 6. 本地 Maven 发行包说明

`D:\git(learn)\maven\apache-maven-3.10.0` 是本地 Maven 发行包（二进制，未纳入版本控制，已在 `.gitignore` 忽略 `maven/`）。push 远程时不会带上它；新机器上需自行安装 Maven 或保留此目录。
