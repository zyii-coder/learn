# 05 · 变基与暂存

## rebase 做了什么

`merge` 保留分叉历史，`rebase` 则把当前分支的提交**逐个重放**到目标分支的最新提交之后，得到一条直线。

```bash
git switch feature
git rebase main          # 把 feature 的提交"搬"到 main 最新提交之后
```

结果差异：

```
merge:                        rebase:
      A---B---C feature              A'--B'--C' feature
     /                         /
D---E---F main            D---E---F---A'--B'--C'
```

提交内容一样，但 **sha 变了**（因为是新建的提交对象）。

## 黄金法则

> **不要对已经推送到公共分支的提交做 rebase。**

改写历史会导致他人本地历史与远端冲突。只在自己的、未共享的分支上 rebase。

## 冲突处理

```bash
git rebase main
# 冲突 -> 编辑文件 -> git add <file>
git rebase --continue
git rebase --skip       # 跳过这个提交
git rebase --abort      # 放弃，回到 rebase 前
```

## 交互式 rebase：整理提交

```bash
git rebase -i HEAD~3      # 整理最近 3 个提交
```

编辑器里每行一个提交，改前面的动词：

| 动词 | 作用 |
| --- | --- |
| `pick` | 保留 |
| `reword` | 保留但改提交信息 |
| `edit` | 停下让你修改该提交内容 |
| `squash` | 合并到上一个提交，并编辑合并后的信息 |
| `fixup` | 合并到上一个提交，丢弃本条信息 |
| `drop` | 删除该提交 |

顺序也可以调整（交换行的位置），用来整理提交逻辑顺序。

## stash：临时收起改动

切分支时手头有半成品，不想提交又不想丢：

```bash
git stash push -m "登录页样式未完成"    # 收进储藏室
git stash push -u                       # 连未跟踪文件一起（-u = --include-untracked）
git stash list                          # 查看列表 stash@{0} 是最新的
git stash pop                           # 取出最新一条并删除记录
git stash apply stash@{2}               # 取出指定一条，保留记录
git stash drop stash@{0}                # 删除某条
git stash clear                         # 全部清空（不可恢复）
git stash show -p stash@{0}             # 查看内容
```

## cherry-pick：只挑一个提交

把别的分支上的某一个提交摘过来，不合并整个分支：

```bash
git cherry-pick <sha>
git cherry-pick <sha1>..<sha2>     # 一段范围（不含 sha1）
git cherry-pick --abort
```

适用场景：修复提交误提到别的分支、只需要某个功能的一小块。

## 三种整合方式对比

| 方式 | 历史形态 | 适用场景 |
| --- | --- | --- |
| `merge` | 保留分叉 + 合并提交 | 公共分支合并，保留真实协作轨迹 |
| `rebase` | 直线 | 个人分支同步主干、提交前整理 |
| `cherry-pick` | 复制单个提交 | 精准搬运一处改动 |

## pull 的两种姿势

```bash
git config --global pull.rebase true   # pull 默认用 rebase 而非 merge
git pull --rebase
```

团队规定不一致时容易因此产生额外合并提交，建议在仓库级统一约定。
