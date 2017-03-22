package com.ldbc.driver.runtime.metrics;

public class WorkloadStatusSnapshot {
    private final long runDurationAsMilli;
    private final long operationCount;
    private final long durationSinceLastMeasurementAsMilli;
    private final double throughput;
    private final long updateCount;
    private final double updateThroughput;
    private final long readCount;
    private final double readThroughput;

    public WorkloadStatusSnapshot( long runDurationAsMilli, long operationCount, long durationSinceLastMeasurementAsMilli, double throughput, long updateCount, double updateThroughput, long readCount, double readThroughput ) {
        this.runDurationAsMilli = runDurationAsMilli;
        this.operationCount = operationCount;
        this.durationSinceLastMeasurementAsMilli = durationSinceLastMeasurementAsMilli;
        this.throughput = throughput;
        this.updateCount = updateCount;
        this.updateThroughput = updateThroughput;
        this.readCount = readCount;
        this.readThroughput = readThroughput;
    }

    public long runDurationAsMilli() {
        return runDurationAsMilli;
    }

    public long operationCount() {
        return operationCount;
    }

    public long durationSinceLastMeasurementAsMilli() {
        return durationSinceLastMeasurementAsMilli;
    }

    public double throughput() {
        return throughput;
    }

    public long getUpdateCount() {
        return updateCount;
    }

    public double getUpdateThroughput() {
        return updateThroughput;
    }

    public long getReadCount() {
        return readCount;
    }

    public double getReadThroughput() {
        return readThroughput;
    }
}
