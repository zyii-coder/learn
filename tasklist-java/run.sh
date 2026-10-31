#!/usr/bin/env bash
# Git Bash / macOS / Linux 下运行
set -e
cd "$(dirname "$0")"

# 优先用 JAVA_HOME 里的 JDK：这样即使当前终端的 PATH 还没刷新（比如 VSCode 启动早于
# 环境变量生效），也不用重开窗口就能跑。
# JAVA_HOME 也没有时，去 ~/.jdks 下找一个能用的 JDK 兜底。
if [ -z "$JAVA_HOME" ] || [ ! -x "$JAVA_HOME/bin/javac" ]; then
    for candidate in "$HOME"/.jdks/*/; do
        if [ -x "$candidate/bin/javac" ]; then
            JAVA_HOME="${candidate%/}"
            break
        fi
    done
fi

if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/javac" ]; then
    JAVAC="$JAVA_HOME/bin/javac"
    JAVA="$JAVA_HOME/bin/java"
else
    JAVAC="javac"
    JAVA="java"
fi

mkdir -p out
"$JAVAC" -encoding UTF-8 -d out src/tasklist/*.java

# Git Bash 的终端是 UTF-8，但 Windows 报给 JVM 的控制台代码页是 936，
# 所以这里显式声明输入输出都用 UTF-8，否则中文会乱码。
# （-Dstdout.encoding / -Dstdin.encoding 需要 JDK 18+，低版本靠 -Dfile.encoding 兜底）
"$JAVA" -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstdin.encoding=UTF-8 \
        -cp out tasklist.Main "$@"
