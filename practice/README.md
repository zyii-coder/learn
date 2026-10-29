# 练习区

用来动手试验各种 git 操作，弄坏了直接删掉重来，不用担心污染笔记区。

## 练习前先隔离

在这个目录里做实验不会影响仓库其他部分，但 `reset` / `rebase` 之类的命令会影响整个仓库历史。想彻底放开手脚，建议复制一份出来单独玩：

```bash
cp -r practice ~/tmp/git-sandbox
cd ~/tmp/git-sandbox
git init -b main
```

## 建议练习顺序

1. **三区模型** — 改文件、`git add`、`git diff` / `git diff --staged` 反复切换，观察差异输出的变化
2. **提交拆分** — 一次改多个文件，用 `git add -p` 拆成三个提交
3. **分支合并** — 建两个分支各改同一行，制造冲突并手工解决
4. **fast-forward vs no-ff** — 同样的场景分别用 `merge` 和 `merge --no-ff`，对比 `git log --graph`
5. **reset 三模式** — 连续做 3 个提交，分别用 `--soft` / `--mixed` / `--hard` 回退，对比工作区和暂存区状态
6. **reflog 救援** — `reset --hard` 之后用 `git reflog` 把提交找回来
7. **rebase** — 两个分支分叉后用 `rebase` 变直线，再试 `rebase -i` 把 3 个提交压成 1 个
8. **stash** — 改到一半切分支，用 `stash` 保存再取回

## 记录

每个练习的结论写进 `notes/` 对应文档，或在本目录下新建 `练习-主题.md`。
