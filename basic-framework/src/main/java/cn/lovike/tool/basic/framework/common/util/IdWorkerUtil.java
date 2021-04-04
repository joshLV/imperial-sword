package cn.lovike.tool.basic.framework.common.util;

/**
 * 雪花算法生成唯一 id
 * <p>
 * tweeter 的 snowflake 移植到 Java:
 * 0 - 00000000 00000000 00000000 00000000 00000000 0 - 00000000 00 - 00000000 0000
 * 1 -                  41                            -     10      -      12
 * 第 1 位始终为 0, 41 位的时间缀 + 10 位工作 id + 12位 sequence 序列号
 * 1.第一位       占用 1  bit，  其值始终是 0，没有实际作用
 * 2.时间戳       占用 41 bit， 精确到毫秒，总共可以容纳约 69 年的时间
 * 3.工作机器 id  占用 10 bit， 其中高位 5bit 是数据中心 ID，低位 5bit 是工作节点 ID，做多可以容纳 1024 个节点
 * 4.序列号       占用 12 bit， 每个节点每毫秒 0 开始不断累加，最多可以累加到 4095，即每毫秒可以产生 4096 个 ID
 *
 * @author lovike
 * @since 2020/11/18
 */
public class IdWorkerUtil {

    /**
     * 起始时间戳作为基准，一般取系统的最近时间
     */
    private final static long START_STAMP = 1480166465631L;

    /**
     * 序列号占用的位数
     */
    private final static long SEQUENCE_BIT    = 12;
    /**
     * 机器标识占用的位数
     */
    private final static long MACHINE_BIT     = 5;
    /**
     * 数据中心占用的位数
     */
    private final static long DATA_CENTER_BIT = 5;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATA_CENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);
    private final static long MAX_MACHINE_NUM     = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE        = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT     = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT   = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    /**
     * 数据中心
     */
    private final long dataCenterId;

    /**
     * 机器标识
     */
    private final long machineId;

    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上一次时间戳
     */
    private long lastStamp = -1L;


    public static IdWorkerUtil getInstance(long dataCenterId, long machineId) {
        return new IdWorkerUtil(1, 1);
    }

    public IdWorkerUtil(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个 ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStamp = getNewStamp();
        if (currStamp < lastStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStamp == lastStamp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为 0
            sequence = 0L;
        }

        lastStamp = currStamp;

        return ((currStamp - START_STAMP) << TIMESTAMP_LEFT) // 时间戳部分
                | (dataCenterId << DATA_CENTER_LEFT)         // 数据中心部分
                | (machineId << MACHINE_LEFT)                // 机器标识部分
                | (sequence);                                // 序列号部分
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println(start);
        for (int i = 0; i < 100; i++) {
            // 机器 ID 建议取 zk id
            System.out.println(IdWorkerUtil.getInstance(1, 1).nextId());
        }

        System.out.println(System.currentTimeMillis() - start);
    }
}
