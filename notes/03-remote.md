# 03 · 远程仓库与协作

## 管理远端

```bash
git remote -v                                  # 查看远端及地址
git remote add origin https://github.com/user/repo.git
git remote set-url origin <new-url>            # 改地址
git remote remove origin                       # 删除
git remote show origin                         # 查看远端详情（含分支跟踪关系）
```

## fetch / pull / push

```bash
git fetch origin            # 只下载远端更新，不动本地分支（安全，推荐先看再合）
git fetch --prune           # 同时清理远端已删除的分支引用
git pull                    # = fetch + merge
git pull --rebase           # = fetch + rebase（历史更线性）

git push -u origin main     # 首次推送并建立跟踪关系
git push                    # 之后直接推送
git push origin <branch>
git push origin --delete <branch>   # 删除远端分支
```

想看远端到底改了什么，再决定怎么合：

```bash
git fetch origin
git log --oneline HEAD..origin/main     # 远端比本地多的提交
git diff HEAD origin/main               # 具体差异
```

## 跟踪分支

本地分支可以"跟踪"一个远程分支，之后 `git push` / `git pull` 不用带参数。

```bash
git branch -vv                          # 查看跟踪关系
git branch -u origin/main               # 为当前分支设置上游
git push -u origin main                 # 推送同时设置
```

`git status -sb` 输出 `## main...origin/main [ahead 2, behind 1]`：本地领先 2 个提交、落后 1 个。

## 认证方式

| 方式 | 说明 |
| --- | --- |
| HTTPS + 凭据管理器 | 最省事，Windows 下由 Git Credential Manager 保存 token |
| HTTPS + PAT | GitHub 已不支持密码，需 Personal Access Token（勾 `repo` 权限） |
| SSH | 生成密钥后把公钥加到 GitHub，长期免密 |

```bash
# SSH 方式
ssh-keygen -t ed25519 -C "邮箱"
cat ~/.ssh/id_ed25519.pub          # 粘贴到 GitHub → Settings → SSH keys
ssh -T git@github.com              # 测试
```

## 常见问题

**推送被拒 `non-fast-forward`**

```bash
git pull --rebase origin main   # 先把远端提交接到本地之后
git push
```

**网络连不上 github.com（国内常见）**

```bash
git config --global http.proxy http://127.0.0.1:7890
git config --global https.proxy http://127.0.0.1:7890
# 取消：git config --global --unset http.proxy
```

**`src refspec main does not match any`**

本地还没有提交，或分支名不是 `main`。先 `git log` 确认有提交，再 `git branch -M main` 统一分支名。

**`fatal: refusing to merge unrelated histories`**

两个仓库各自从零开始、历史无关（如本地 init 后又 clone 了带 README 的仓库）：

```bash
git pull origin main --allow-unrelated-histories
```
