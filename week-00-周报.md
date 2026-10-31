# 周报 · Week 00

- **周期**：2026-10-25 ~ 2026-10-31（本周收尾于 10-31）
- **仓库**：`learn`（`https://github.com/zyii-coder/learn`）｜标签 `week-00`
- **一句话总结**：搭好 Java 开发环境，完成一个**可运行、可打包**的 Maven 小项目，练熟 git 工作流，并产出任务资源 REST 接口草稿。

## 一、本周交付物

| # | 交付物 | 位置 | 状态 |
|---|---|---|---|
| 1 | 命令行任务清单（类 / 接口 / ArrayList）| `tasklist-java/` | ✅ 可编译运行 |
| 2 | 可构建 Maven 项目 | `maven-lab/` | ✅ `mvn clean package` 通过 |
| 3 | 任务资源 REST 接口草稿 | `maven-lab/docs/api/tasks.md` | ✅ CRUD 四接口 |
| 4 | 环境记录 | `week-00-环境记录.md` | ✅ |
| — | 本周周报 | 本文件 | ✅ |

## 二、可运行、可打包的 Java Maven 小项目（核心交付）

**项目 `maven-lab`**：一个最小但「真能构建」的 Maven 工程，用来练 `branch` / `commit` / `push`。

- **技术栈**：Java 21（Temurin 21.0.13）+ Apache Maven 3.10.0 + JUnit 5.14.4 + surefire 3.6.1 + GitHub Actions
- **构建**：`mvn clean package` → **BUILD SUCCESS**，产出 `target/maven-lab.jar`（约 4.9 KB）
- **测试**：10 个 JUnit 单测全过（`Tests run: 10, Failures: 0, Errors: 0`）
- **运行**：`java -jar target/maven-lab.jar "git branch and commit"`
  → 词数 4 / 字符数 21 / 最长词 `branch` / 平均词长 4.50
- **目录结构**：

  ```
  maven-lab/
  ├── pom.xml
  ├── .github/workflows/maven.yml   # CI：推送时自动 mvn verify
  ├── docs/api/tasks.md              # REST 接口草稿（见第四节）
  └── src/
      ├── main/java/com/zyii/mavenlab/{App,TextStats}.java
      └── test/java/com/zyii/mavenlab/TextStatsTest.java
  ```
- **功能**：`TextStats` 对一段文本做词数 / 字符数 / 最长词 / 平均词长统计；`App` 以命令行参数为输入（无参数时用默认文字）；`null` 与纯空白输入一律按空文本处理，统计结果为 0，不抛异常。

## 三、Git 提交记录（≥ 3 次）

`maven-lab` 共 **10 次提交**，全程 Conventional Commits，并练习了 `feature/*` 分支开发 + `--no-ff` 合并 + 打标签：

| 日期 | SHA | 说明 |
|---|---|---|
| 2026-10-31 | 52ed835 | chore: 初始化 Maven 项目与 CI 构建 |
| 2026-10-31 | e07cd78 | feat: 添加可运行的程序入口 |
| 2026-10-31 | afa4c27 | chore: 统一换行符为 LF，避免跨平台差异 |
| 2026-10-31 | f9548b7 | feat: 添加 TextStats 文本统计逻辑 |
| 2026-10-31 | 70d34f2 | test: 为 TextStats 补充 10 个单元测试 |
| 2026-10-31 | b6c6d30 | feat: 入口输出统计结果并补充 README |
| 2026-10-31 | 49aa6e2 | merge: 合并 feature/text-stats 文本统计功能 |
| 2026-10-31 | 7cc1e47 | docs: 添加构建状态徽章与练习流程说明 |
| 2026-10-31 | d197f7a | docs: 新增任务资源 REST 接口设计草稿 |
| 2026-10-31 | 1a0b829 | merge: 合并 feature/docs-api 接口设计文档 |

`learn` 仓库本周追加 2 个提交并打 `week-00` 标签：`8ce01b1 fix: tasklist-java 用 try-with-resources…`、`a36041a docs: 添加 week-00 环境记录，忽略本地 Maven 发行包`，均已推送 GitHub。

## 四、REST 接口草稿

位置：`maven-lab/docs/api/tasks.md`（草稿 v0.1，待评审）。覆盖任务资源的**增 / 删 / 改 / 查**四个接口：

| 操作 | 方法 & 路径 | 成功码 | 说明 |
|---|---|---|---|
| 新增 | `POST /api/v1/tasks` | 201 | 创建任务，响应头带 `Location`，响应体为完整资源 |
| 查询 | `GET /api/v1/tasks` | 200 | 分页查询（支持 `status` / `priority` / `keyword` / 排序等过滤） |
| 修改 | `PUT /api/v1/tasks/{id}` | 200 | 全量更新，含 `If-Match` 乐观锁防并发覆盖 |
| 删除 | `DELETE /api/v1/tasks/{id}` | 204 | 幂等删除 + 软删除策略（保留 7 天可恢复） |

另含：统一错误响应结构、Task 数据模型、权限矩阵、Spring Boot 注解映射参考，以及 10 条待确认清单（软删除、分页策略、`PATCH` 部分更新、认证方案等）。

## 五、环境

| 工具 | 版本 |
|---|---|
| JDK | openjdk 21.0.13 2026-10-20 LTS（Temurin-21.0.13+9） |
| Maven | Apache Maven 3.10.0 |
| OS | Windows 11，amd64 |

> 注：本机 Maven 发行包放在 `maven/`（已在 `.gitignore` 忽略，不进版本库）；CI 与本地均依赖 JDK 21。完整记录见 `week-00-环境记录.md`。

## 六、下周计划

- [ ] 给 `tasklist-java` 加 CSV 持久化（退出保存、启动读取）
- [ ] 用 Spring Boot 落地 REST 接口草稿，先实现「新增 + 查询」
- [ ] 给 `TaskManager` 补 JUnit 测试（验证「删不存在的编号不炸」）
- [ ] 练习 `rebase` / `cherry-pick` 等进阶 git 操作
