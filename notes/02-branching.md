# 02 · 分支与合并

## 分支的本质

分支只是一个指向某个提交的**可变指针**，所以创建和切换分支几乎不产生开销——这是 git 鼓励频繁建分支的原因。

## 增删查切

```bash
git branch                    # 列出本地分支
git branch -a                 # 含远程分支
git branch -vv                # 显示各分支跟踪的远程分支
git branch <name>             # 创建分支（不切换）
git switch -c <name>          # 创建并切换（推荐，语义清晰）
git checkout -b <name>        # 老写法，等价
git switch <name>             # 切换
git branch -d <name>          # 删除已合并分支
git branch -D <name>          # 强制删除（未合并也删）
git branch -m <old> <new>     # 重命名
```

> Git 2.23 起 `switch` 专管切换分支、`restore` 专管恢复文件，替代了 `checkout` 的多重语义。老命令仍可用。

## 合并

把 `feature` 合进当前分支：

```bash
git switch main
git merge feature
```

两种结果：

- **Fast-forward**：`main` 没有新提交，指针直接前移，不产生合并提交
- **三方合并**：两边都有新提交，生成一个合并提交（merge commit）

```bash
git merge --no-ff feature    # 强制生成合并提交，保留分支拓扑（团队常用）
git merge --squash feature   # 把 feature 所有改动压成一个待提交的变更
git merge --abort            # 冲突太多，放弃本次合并，回到合并前
```

## 解决冲突

冲突时文件里会出现标记：

```
<<<<<<< HEAD
当前分支的内容
=======
要合进来的分支内容
>>>>>>> feature
```

处理步骤：

1. 手工编辑，保留想要的内容，**删掉三行标记**
2. `git add <file>` 标记为已解决
3. `git commit` 完成合并（merge 场景）

```bash
git status               # 看哪些文件 unmerged
git diff --name-only --diff-filter=U   # 只列出冲突文件
git merge --abort        # 想反悔
```

## 实用技巧

```bash
git log --oneline --graph --all            # 看清分支结构
git branch --merged main                   # 哪些分支已合入 main，可安全删除
git branch --no-merged main                # 哪些还没合入
git merge-base main feature                # 两个分支的共同祖先
```

## 什么时候用分支

- 一个分支只做一件事，合并后删除
- 长期分支（`main`）保持随时可发布
- 命名建议：`feat/login`、`fix/upload-timeout`、`docs/readme`
