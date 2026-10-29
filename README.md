# git-learn

Git 学习与实践仓库。记录常用命令、工作流和踩坑经验。

## 目录

| 文档 | 内容 |
| --- | --- |
| [git-cheatsheet.md](git-cheatsheet.md) | 常用命令速查表 |
| [notes/01-basics.md](notes/01-basics.md) | 仓库、暂存区与提交 |
| [notes/02-branching.md](notes/02-branching.md) | 分支与合并 |
| [notes/03-remote.md](notes/03-remote.md) | 远程仓库与协作 |
| [notes/04-undo.md](notes/04-undo.md) | 撤销、回退与找回 |
| [notes/05-rebase-and-stash.md](notes/05-rebase-and-stash.md) | 变基与暂存 |
| [practice/README.md](practice/README.md) | 练习区说明 |

## 核心模型：三个区域

```
工作区            暂存区(index)      本地仓库            远程仓库
Working Tree  ->  Staging Area  ->  Local Repository -> Remote
                git add            git commit          git push
```

理解三个区域的转换关系后，大部分 git 命令就不再是死记硬背：

- `git add`：工作区 → 暂存区
- `git commit`：暂存区 → 本地仓库（生成一次快照）
- `git push`：本地仓库 → 远程仓库
- `git restore` / `git reset`：反向操作，把改动「退」回去

## 环境备注

- 本机访问 `github.com` 需走代理，`http.proxy` / `https.proxy` 已配置在本仓库的 local config 中
- 默认分支：`main`
- 提交身份（本仓库）：`zyii-coder <zyii-coder@users.noreply.github.com>`
