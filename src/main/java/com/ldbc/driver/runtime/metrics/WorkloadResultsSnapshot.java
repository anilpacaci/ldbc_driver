package com.ldbc.driver.runtime.metrics;

import com.google.common.collect.Lists;
import com.ldbc.driver.runtime.ConcurrentErrorReporter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.util.DefaultPrettyPrinter;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WorkloadResultsSnapshot
{
    @JsonProperty( value = "all_metrics" )
    private List<OperationMetricsSnapshot> metrics;

    @JsonProperty( value = "format_version" )
    private int formatVersion = 3;

    @JsonProperty( value = "unit" )
    private TimeUnit unit;

    @JsonProperty( value = "start_time" )
    private long startTimeAsUnit;

    @JsonProperty( value = "latest_finish_time" )
    private long latestFinishTimeAsUnit;

    @JsonProperty( value = "total_duration" )
    private long totalRunDurationAsUnit;

    @JsonProperty( value = "total_count" )
    private long operationCount;

    @JsonProperty( value = "throughput" )
    private double throughput;

    @JsonProperty( value = "update_count" )
    private long updateCount;

    @JsonProperty( value = "update_throughput" )
    private double updateThroughput;

    @JsonProperty( value = "read_count" )
    private long readCount;

    @JsonProperty( value = "read_throughput" )
    private double readThroughput;

    public static WorkloadResultsSnapshot fromJson( File jsonFile ) throws IOException
    {
        return new ObjectMapper().readValue( jsonFile, WorkloadResultsSnapshot.class );
    }

    public static WorkloadResultsSnapshot fromJson( String jsonString ) throws IOException
    {
        return new ObjectMapper().readValue( jsonString, WorkloadResultsSnapshot.class );
    }

    private WorkloadResultsSnapshot()
    {
    }

    public WorkloadResultsSnapshot(
            Map<String, OperationMetricsSnapshot> metrics,
            long startTimeAsMilli,
            long latestFinishTimeAsMilli,
            long operationCount,
            long updateCount,
            long readCount,
            TimeUnit unit )
    {
        this.metrics = Lists.newArrayList( metrics.values() );
        Collections.sort( this.metrics, new OperationTypeMetricsManager.OperationMetricsNameComparator() );
        this.startTimeAsUnit = unit.convert( startTimeAsMilli, TimeUnit.MILLISECONDS );
        this.latestFinishTimeAsUnit = unit.convert( latestFinishTimeAsMilli, TimeUnit.MILLISECONDS );
        this.totalRunDurationAsUnit = unit.convert( latestFinishTimeAsMilli - startTimeAsMilli, TimeUnit.MILLISECONDS );
        this.throughput = 1000 * (operationCount / (double) unit.toMillis( totalRunDurationAsUnit ));
        this.updateThroughput = 1000 * (updateCount / (double) unit.toMillis( totalRunDurationAsUnit ));
        this.readThroughput = 1000 * (readCount / (double) unit.toMillis( totalRunDurationAsUnit ));
        this.operationCount = operationCount;
        this.unit = unit;
    }

    @JsonProperty( value = "all_metrics" )
    public List<OperationMetricsSnapshot> allMetrics()
    {
        return metrics;
    }

    @JsonProperty( value = "all_metrics" )
    private void setAllMetrics( List<OperationMetricsSnapshot> metrics )
    {
        this.metrics = metrics;
        Collections.sort( metrics, new OperationTypeMetricsManager.OperationMetricsNameComparator() );
    }

    public long startTimeAsMilli()
    {
        return unit.toMillis( startTimeAsUnit );
    }

    public long latestFinishTimeAsMilli()
    {
        return unit.toMillis( latestFinishTimeAsUnit );
    }

    public long totalRunDurationAsNano()
    {
        return unit.toNanos( totalRunDurationAsUnit );
    }

    public long totalOperationCount()
    {
        return operationCount;
    }

    public double throughput()
    {
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

    public String toJson()
    {
        try
        {
            return new ObjectMapper().writer( new DefaultPrettyPrinter() ).writeValueAsString( this );
        }
        catch ( Exception e )
        {
            System.out.println( ConcurrentErrorReporter.stackTraceToString( e ) );
            throw new RuntimeException( "Unable to generate parameter values string", e );
        }
    }

    @Override
    public String toString()
    {
        return "WorkloadResultsSnapshot{" +
               "metrics=" + metrics +
               ", unit=" + unit +
               ", startTimeAsUnit=" + startTimeAsUnit +
               ", latestFinishTimeAsUnit=" + latestFinishTimeAsUnit +
               ", totalRunDurationAsUnit=" + totalRunDurationAsUnit +
               ", operationCount=" + operationCount +
               ", throughput=" + throughput +
               ", updateCount=" + updateCount +
               ", updateThroughput=" + updateThroughput +
               ", readCount=" + readCount +
               ", readThroughput=" + readThroughput +
               '}';
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        { return true; }
        if ( o == null || getClass() != o.getClass() )
        { return false; }

        WorkloadResultsSnapshot that = (WorkloadResultsSnapshot) o;

        if ( latestFinishTimeAsUnit != that.latestFinishTimeAsUnit )
        { return false; }
        if ( operationCount != that.operationCount )
        { return false; }
        if ( updateCount != that.updateCount )
        { return false; }
        if ( readCount != that.readCount )
        { return false; }
        if ( startTimeAsUnit != that.startTimeAsUnit )
        { return false; }
        if ( totalRunDurationAsUnit != that.totalRunDurationAsUnit )
        { return false; }
        if ( metrics != null ? !metrics.equals( that.metrics ) : that.metrics != null )
        { return false; }
        if ( unit != that.unit )
        { return false; }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = metrics != null ? metrics.hashCode() : 0;
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (int) (startTimeAsUnit ^ (startTimeAsUnit >>> 32));
        result = 31 * result + (int) (latestFinishTimeAsUnit ^ (latestFinishTimeAsUnit >>> 32));
        result = 31 * result + (int) (totalRunDurationAsUnit ^ (totalRunDurationAsUnit >>> 32));
        result = 31 * result + (int) (operationCount ^ (operationCount >>> 32));
        result = 31 * result + (int) (updateCount ^ (updateCount >>> 32));
        result = 31 * result + (int) (readCount ^ (readCount >>> 32));
        return result;
    }
}