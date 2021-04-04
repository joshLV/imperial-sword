package cn.lovike.tool.basic.framework.common.enums;

import java.util.Arrays;

/**
 * 定时任务状态
 *
 * @author kuang.wang
 * @since 2020/12/31
 */
public enum TaskStatusEnum {
    DOWN(0, "已下线"),
    UP(1, "已上线"),
    WAIT(2, "待发布"),
    ;

    private final int    value;
    private final String desc;

    TaskStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static boolean isExist(int value) {
        return Arrays.stream(TaskStatusEnum.values()).anyMatch(x -> x.value == value);
    }

    public static boolean isNotExist(int value) {
        return !isExist(value);
    }

    public static TaskStatusEnum getEnumByValue(int value) {
        return Arrays.stream(TaskStatusEnum.values()).filter(x -> x.value == value).findFirst().orElse(null);
    }

    public static String getRoutedDesc(int value) {
        TaskStatusEnum em = Arrays.stream(TaskStatusEnum.values()).filter(x -> x.value == value).findFirst().orElse(null);
        return em == null ? null : em.desc;
    }
}
