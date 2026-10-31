package tasklist;

/**
 * 优先级枚举。
 *
 * 用枚举而不是 int/字符串，好处是编译器帮你挡住非法值；
 * 而且枚举的声明顺序（HIGH -> MEDIUM -> LOW）天然就是"从急到缓"的排序顺序，
 * 后面 TaskManager 排序时可以直接用。
 */
public enum Priority {
    HIGH("高"),
    MEDIUM("中"),
    LOW("低");

    private final String label;

    Priority(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    /** 把命令里的 #h / #m / #l 转成枚举，非法字符回落成 MEDIUM */
    public static Priority fromTag(char tag) {
        switch (Character.toLowerCase(tag)) {
            case 'h':
                return HIGH;
            case 'l':
                return LOW;
            default:
                return MEDIUM;
        }
    }
}
