# 01 · 仓库、暂存区与提交

## 初始化与配置

```bash
git init -b main                 # 初始化仓库，指定初始分支为 main
git clone <url>                  # 克隆已有仓库
```

配置分三级，优先级从高到低：`--local`（当前仓库）> `--global`（当前用户）> `--system`。

```bash
git config --global user.name  "名字"
git config --global user.email "邮箱"
git config --list --show-origin  # 查看所有配置及来源文件
```

## 查看状态

```bash
git status            # 完整状态
git status -sb        # 简洁模式：分支 + 文件列表（-s 简短，-b 显示分支）
```

`git status -sb` 里的字母含义：

| 标记 | 含义 |
| --- | --- |
| `??` | 未跟踪文件 |
| `A` | 新增并已暂存 |
| `M` | 已修改（左列=暂存区，右列=工作区） |
| `D` | 已删除 |

## 暂存与提交

```bash
git add <file>          # 暂存指定文件
git add -A              # 暂存全部（新增 + 修改 + 删除）
git add -p              # 交互式逐块选择，提交前拆分改动很有用

git commit -m "feat: 新增登录接口"
git commit -am "fix: 修正拼写"   # 跳过 add，但只对已跟踪文件生效
git commit --amend              # 修改上一条提交（信息或内容）
```

> `--amend` 会改写提交历史，**已推送的提交不要 amend**，否则需要强推并影响他人。

## 提交信息规范

推荐 Conventional Commits 前缀，便于生成 changelog：

```
feat:     新功能
fix:      修 bug
docs:     文档
refactor: 重构（不改行为）
test:     测试
chore:    构建/工具/杂项
```

## 查看历史与差异

```bash
git log --oneline --graph --decorate --all   # 图形化全部分支历史
git log -n 5 --stat                          # 最近 5 条，附改动文件统计
git log -S "关键字"                           # 搜索引入/删除该字符串的提交
git log --author="名字" --since="2 weeks ago"

git diff                # 工作区 vs 暂存区
git diff --staged       # 暂存区 vs 最新提交（= --cached）
git diff HEAD           # 工作区 vs 最新提交（所有未提交改动）
git diff main..feature  # 两个分支的差异
```

## .gitignore

```gitignore
node_modules/
*.log
.env
!keep.env       # ! 表示例外，不忽略
```

注意：

- `.gitignore` **只对未跟踪文件生效**，已被跟踪的文件加进去也不会消失
- 需要让已跟踪文件停止跟踪：`git rm --cached <file>`，再提交
- 查看某文件为何被忽略：`git check-ignore -v <file>`
