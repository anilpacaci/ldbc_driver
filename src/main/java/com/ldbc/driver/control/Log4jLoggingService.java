package com.ldbc.driver.control;

import com.ldbc.driver.runtime.metrics.SimpleDetailedWorkloadMetricsFormatter;
import com.ldbc.driver.runtime.metrics.SimpleSummaryWorkloadMetricsFormatter;
import com.ldbc.driver.runtime.metrics.WorkloadMetricsFormatter;
import com.ldbc.driver.runtime.metrics.WorkloadResultsSnapshot;
import com.ldbc.driver.runtime.metrics.WorkloadStatusSnapshot;
import com.ldbc.driver.temporal.TemporalUtil;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Log4jLoggingService implements LoggingService
{
    private static final DecimalFormat OPERATION_COUNT_FORMATTER = new DecimalFormat( "###,###,###,###" );
    private static final DecimalFormat THROUGHPUT_FORMATTER = new DecimalFormat( "###,###,###,##0.00" );

    private final Logger logger;
    private final TemporalUtil temporalUtil;
    private final boolean detailedStatus;
    private final WorkloadMetricsFormatter summaryWorkloadMetricsFormatter;
    private final WorkloadMetricsFormatter detailedWorkloadMetricsFormatter;

    public Log4jLoggingService( String source, TemporalUtil temporalUtil, boolean detailedStatus )
    {
        this.logger = Logger.getLogger( source );
        this.temporalUtil = temporalUtil;
        this.detailedStatus = detailedStatus;
        this.summaryWorkloadMetricsFormatter = new SimpleSummaryWorkloadMetricsFormatter();
        this.detailedWorkloadMetricsFormatter = new SimpleDetailedWorkloadMetricsFormatter();
    }

    @Override
    public void info( String message )
    {
        logger.info( message );
    }

    @Override
    public void status(
            WorkloadStatusSnapshot status,
            RecentThroughputAndDuration recentThroughputAndDuration,
            long globalCompletionTimeAsMilli )
    {
        String statusString;
        statusString = (detailedStatus) ?
                       formatWithGct(
                               status.operationCount(),
                               status.getUpdateCount(),
                               status.getReadCount(),
                               status.runDurationAsMilli(),
                               status.durationSinceLastMeasurementAsMilli(),
                               status.throughput(),
                               status.getUpdateThroughput(),
                               status.getReadThroughput(),
                               recentThroughputAndDuration.throughput(),
                               recentThroughputAndDuration.duration(),
                               recentThroughputAndDuration.updateThroughput(),
                               globalCompletionTimeAsMilli ) :
                       formatWithoutGct(
                               status.operationCount(),
                               status.getUpdateCount(),
                               status.getReadCount(),
                               status.runDurationAsMilli(),
                               status.durationSinceLastMeasurementAsMilli(),
                               status.throughput(),
                               status.getUpdateThroughput(),
                               status.getReadThroughput(),
                               recentThroughputAndDuration.throughput(),
                               recentThroughputAndDuration.duration(),
                               recentThroughputAndDuration.updateThroughput());
        logger.info( statusString );
    }

    @Override
    public void summaryResult( WorkloadResultsSnapshot workloadResultsSnapshot )
    {
        logger.info( "\n" + summaryWorkloadMetricsFormatter.format( workloadResultsSnapshot ) );
    }

    @Override
    public void detailedResult( WorkloadResultsSnapshot workloadResultsSnapshot )
    {
        logger.info( "\n" + detailedWorkloadMetricsFormatter.format( workloadResultsSnapshot ) );
    }

    private String formatWithoutGct( long operationCount, long updateCount, long readCount, long runDurationAsMilli,
            long durationSinceLastMeasurementAsMilli, double throughput, double updateThroughput, double readThroughput, double recentThroughput,
            long recentDurationAsMilli, double recentUpdateThroughput )
    {
        return format( operationCount, updateCount, readCount, runDurationAsMilli, durationSinceLastMeasurementAsMilli, throughput, updateThroughput,
                readThroughput, recentThroughput, recentDurationAsMilli, recentUpdateThroughput,null ).toString();
    }

    private String formatWithGct( long operationCount, long updateCount, long readCount, long runDurationAsMilli,
                                  long durationSinceLastMeasurementAsMilli, double throughput, double updateThroughput, double readThroughput,  double recentThroughput,
                                  long recentDurationAsMilli, double recentUpdateThroughput, long gctAsMilli )
    {
        return format( operationCount, updateCount, readCount, runDurationAsMilli, durationSinceLastMeasurementAsMilli, throughput, updateThroughput,
                readThroughput, recentThroughput, recentDurationAsMilli, recentUpdateThroughput, gctAsMilli ).toString();
    }

    private StringBuffer format( long operationCount, long updateCount, long readCount, long runDurationAsMilli, long durationSinceLastMeasurementAsMilli,
            double throughput, double updateThroughput, double readThroughput, double recentThroughput, long recentDurationAsMilli, double recentUpdateThroughput, Long gctAsMilli )
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "Runtime [" )
                .append( (-1 == runDurationAsMilli) ? "--" : temporalUtil.milliDurationToString( runDurationAsMilli ) )
                .append( "], " );
        sb.append( "Operations [" ).append( OPERATION_COUNT_FORMATTER.format( readCount) ).append(" / ").append(OPERATION_COUNT_FORMATTER.format(updateCount)).append( "], " );
        sb.append( "Last [" ).append( (-1 == durationSinceLastMeasurementAsMilli) ? "--" : temporalUtil
                .milliDurationToString( durationSinceLastMeasurementAsMilli ) ).append( "], " );
        sb.append( "Throughput" );
        sb.append( " (Total) [" ).append( THROUGHPUT_FORMATTER.format( readThroughput ) ).append(" / ").append(THROUGHPUT_FORMATTER.format(updateThroughput)).append( "]" );
        sb.append( " (Last " ).append( TimeUnit.MILLISECONDS.toSeconds( recentDurationAsMilli ) ).append( "s) [" )
                .append( THROUGHPUT_FORMATTER.format( recentThroughput ) ).append(" / ").append(THROUGHPUT_FORMATTER.format(recentUpdateThroughput)).append( "]" );
        if ( null != gctAsMilli )
        {
            sb.append(
                    ", GCT: " + ((-1 == gctAsMilli) ? "--" : temporalUtil.milliTimeToDateTimeString( gctAsMilli )) );
        }
        return sb;
    }
}
