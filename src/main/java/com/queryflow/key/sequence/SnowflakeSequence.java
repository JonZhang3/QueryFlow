package com.queryflow.key.sequence;

import com.queryflow.common.QueryFlowException;

public class SnowflakeSequence implements Sequence<Long> {

    private static final long startTime = 1288834974657L;
    private static final long workerIdBits = 5L;// workerId 的位数
    private static final long datacenterIdBits = 5L;// datacenterId 的位数
    private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);// 5位的 workerId，最大为31
    private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);//5位的 datacenterId，最大为 31
    private static final long sequenceBits = 12L;// 增长序列的位数
    private static final long workerIdShift = sequenceBits;// workerId 的左移位置
    private static final long datacenterIdShift = sequenceBits + workerIdBits;// datacenterId 的左移位置
    private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;// 时间左移序列的位置
    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);//

    private long workerId;// 用于分布式系统中
    private long datacenterId;// 用于分布式系统中
    private long sequence = 0L;// 增长序列初始值
    private long lastTimestamp = -1L;// 上一个时间戳

    public SnowflakeSequence(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    @Override
    public synchronized Long next() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new QueryFlowException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            // 通过位与运算保证计算的结果范围始终是 0-4095
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - startTime) << timestampLeftShift) | (datacenterId << datacenterIdShift)
            | (workerId << workerIdShift) | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}
