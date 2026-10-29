# 04 · 撤销、回退与找回

撤销是 git 里最容易出事的部分。先判断一件事：**这些改动推送到远端了吗？** 推过就不要改写历史。

## 按场景选命令

| 想撤销什么 | 命令 | 影响范围 |
| --- | --- | --- |
| 工作区某个文件的改动 | `git restore <file>` | 只丢工作区修改，不可恢复 |
| 已 `add` 但没提交 | `git restore --staged <file>` | 只移出暂存区，改动还在 |
| 最近一次提交（本地） | `git reset --soft HEAD~1` | 保留改动在暂存区 |
| 最近几次提交（本地） | `git reset --hard HEAD~n` | **改动全部丢弃** |
| 已推送的某个提交 | `git revert <sha>` | 新增反向提交，安全 |
| 误删的分支 / 提交 | `git reflog` + `reset` | 靠 reflog 找回 |

## git restore（推荐，语义清晰）

```bash
git restore <file>                     # 用暂存区内容覆盖工作区 = 丢弃修改
git restore --staged <file>            # 从暂存区撤出（unstage），工作区不变
git restore --source=HEAD~1 <file>     # 把文件恢复成两次提交前的版本
git restore .                          # 丢弃所有工作区修改（谨慎）
```

## git reset 三种模式

```bash
git reset --soft  HEAD~1
git reset --mixed HEAD~1   # 默认
git reset --hard  HEAD~1
```

| 模式 | HEAD 指针 | 暂存区 | 工作区 | 典型场景 |
| --- | --- | --- | --- | --- |
| `--soft` | 移动 | 保留 | 保留 | 提交信息写错，想重写；几次提交合并成一次 |
| `--mixed` | 移动 | 重置 | 保留 | 提交内容包含多余文件，想重新挑 |
| `--hard` | 移动 | 重置 | **重置** | 彻底放弃这些提交（数据丢失） |

> `--hard` 之前先 `git stash` 或建个临时分支兜底，比事后找回轻松得多。

## git revert（公开历史的正确做法）

```bash
git revert <sha>              # 生成一个"反向提交"抵消该提交
git revert HEAD               # 撤销最近一次
git revert --no-commit <sha>  # 只改工作区，之后一起提交
```

revert 不删除历史，只追加历史，所以可以安全地用在已推送的分支上。撤销一次合并提交需要指明主线：

```bash
git revert -m 1 <merge-sha>
```

## reflog：最后的保险

reflog 记录了 HEAD 的每一次移动，默认保留 90 天。

```bash
git reflog                       # 查看所有 HEAD 移动记录
git reset --hard HEAD@{3}        # 回到某个历史状态
git reset --hard <sha>           # 找回被 reset --hard 掉的提交
```

**误删分支也能救回来**：

```bash
git reflog                       # 找到分支最后一个提交的 sha
git branch <name> <sha>          # 重新建分支
```

## 危险操作前的自保清单

1. `git status` 看清当前状态，别在未知状态上操作
2. 改动重要就先 `git stash` 或 `git switch -c tmp-backup`
3. 只影响本地 → 可以用 `reset`；已经推送 → 用 `revert`
4. `--hard`、`push --force`、`clean -fd` 三个命令执行前停三秒
