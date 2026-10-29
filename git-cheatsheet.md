# Git 常用命令速查表

## 配置

```bash
git config --global user.name "名字"
git config --global user.email "邮箱"
git config --list --show-origin
```

## 创建与克隆

```bash
git init -b main
git clone <url>
```

## 状态与差异

```bash
git status -sb
git diff              # 工作区 vs 暂存区
git diff --staged     # 暂存区 vs HEAD
git diff HEAD         # 工作区 vs HEAD
```

## 提交

```bash
git add <file>
git add -A
git add -p
git commit -m "feat: xxx"
git commit --amend
```

## 历史

```bash
git log --oneline --graph --decorate --all
git log -n 5 --stat
git log -S "string"
git show <sha>
git reflog
```

## 分支

```bash
git branch
git branch -vv
git switch -c <name>
git switch <name>
git branch -d <name>
git branch -m <new>
```

## 合并与变基

```bash
git merge <branch>
git merge --no-ff <branch>
git merge --abort
git rebase <branch>
git rebase -i HEAD~3
git rebase --continue / --abort
git cherry-pick <sha>
```

## 撤销

```bash
git restore <file>                  # 丢弃工作区修改
git restore --staged <file>         # 取消暂存
git reset --soft HEAD~1             # 撤提交，改动留暂存区
git reset --mixed HEAD~1            # 撤提交和暂存，改动留工作区
git reset --hard HEAD~1             # 全撤（危险）
git revert <sha>                    # 反向提交（安全，可对已推送）
```

## 暂存改动

```bash
git stash push -m "msg"
git stash push -u
git stash list
git stash pop
git stash apply stash@{n}
git stash drop stash@{n}
```

## 远程

```bash
git remote -v
git remote add origin <url>
git fetch --prune
git pull --rebase
git push -u origin main
git push
git push origin --delete <branch>
```

## 危险命令清单（执行前停三秒）

```bash
git reset --hard
git push --force            # 用 --force-with-lease 更安全
git clean -fdx
git branch -D
```

## 排查

```bash
git check-ignore -v <file>      # 为什么被忽略
git branch --merged main        # 哪些分支已合并
git log HEAD..origin/main       # 远端比本地多的提交
git rev-parse --show-toplevel   # 仓库根目录
```
