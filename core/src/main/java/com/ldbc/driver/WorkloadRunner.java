package com.ldbc.driver;

import org.apache.log4j.Logger;

import com.ldbc.driver.generator.Generator;
import com.ldbc.driver.generator.GeneratorBuilder;
import com.ldbc.driver.measurements.Measurements;
import com.ldbc.driver.workloads.Workload;
import com.ldbc.driver.workloads.WorkloadException;

class WorkloadRunner
{
    private static Logger logger = Logger.getLogger( WorkloadRunner.class );

    private final int STATUS_INTERVAL_SECONDS = 1;

    private final Db db;
    private final BenchmarkPhase benchmarkPhase;
    private final Workload workload;
    private final int operationCount;
    private final GeneratorBuilder generatorBuilder;
    private final boolean showStatus;
    private final int threadCount;
    private final Measurements measurements;

    int operationsDone;

    public WorkloadRunner( Db db, BenchmarkPhase benchmarkPhase, Workload workload, int operationCount,
            GeneratorBuilder generatorBuilder, boolean showStatus, int threadCount, Measurements measurements )
    {
        this.db = db;
        this.benchmarkPhase = benchmarkPhase;
        this.workload = workload;
        this.operationCount = operationCount;
        this.operationsDone = 0;
        this.generatorBuilder = generatorBuilder;
        this.showStatus = showStatus;
        this.threadCount = threadCount;
        this.measurements = measurements;
    }

    public void run() throws ClientException
    {
        OperationHandlerExecutor operationHandlerExecutor = new OperationHandlerExecutor( threadCount );
        OperationResultLoggingThread operationResultLoggingThread = new OperationResultLoggingThread(
                operationHandlerExecutor, measurements );
        operationResultLoggingThread.start();
        Generator<Operation<?>> operationGenerator = getOperationGenerator( benchmarkPhase );
        WorkloadProgressStatus workloadProgressStatus = new WorkloadProgressStatus( System.nanoTime() );
        while ( operationCount == 0 || operationsDone < operationCount )
        {
            Operation<?> operation = operationGenerator.next();
            try
            {
                OperationHandler<?> operationHandler = db.getOperationHandler( operation );
                operationHandlerExecutor.execute( operationHandler );
                operationsDone++;
                // TODO YCSB legacy shit, convert to Generator(Wrapper) solution
                // doThrottleOperations( startTime );
                // TODO pause/wait/spinner here according to scheduled delay

                if ( showStatus && workloadProgressStatus.secondsSinceLastUpdate() >= STATUS_INTERVAL_SECONDS )
                {
                    String statusString = workloadProgressStatus.update( operationsDone );
                    logger.info( statusString );
                }
            }
            catch ( Exception e )
            {
                throw new ClientException( String.format(
                        "Error encountered trying to execute %s after %s of %s operations", operation, operationsDone,
                        operationCount ), e.getCause() );
            }
        }

        try
        {
            operationResultLoggingThread.finishLoggingRemainingResults();
            operationResultLoggingThread.join();
            operationHandlerExecutor.shutdown();
        }
        catch ( InterruptedException e )
        {
            logger.error( "Error encountered while waiting for logging thread to finish", e );
        }
    }

    private Generator<Operation<?>> getOperationGenerator( BenchmarkPhase benchmarkPhase ) throws ClientException
    {
        Generator<Operation<?>> operationGenerator = null;
        try
        {
            switch ( benchmarkPhase )
            {
            case LOAD_PHASE:
                operationGenerator = workload.getLoadOperations( generatorBuilder );
                break;
            case TRANSACTION_PHASE:
                operationGenerator = workload.getTransactionalOperations( generatorBuilder );
                break;
            }
        }
        catch ( WorkloadException e )
        {
            throw new ClientException( "Error encounterd trying to get operation generator", e.getCause() );
        }
        return operationGenerator;
    }

    // TODO remove/replace with more configurable (Generator-based) strategy
    // private void doThrottleOperations( long startTime )
    // {
    // /*
    // * more accurate than other strategies tried, like sleeping for
    // (1/target)-operation_latency.
    // * this way smoothes timing inaccuracies, (sleep() takes int, current time
    // in millis) over many operations
    // */
    // if ( targetPerformancePerMs > 0 )
    // {
    // while ( System.currentTimeMillis() - startTime < ( (double)
    // operationsDone ) / targetPerformancePerMs )
    // {
    // try
    // {
    // Thread.sleep( 1 );
    // }
    // catch ( InterruptedException e )
    // {
    // // do nothing.
    // }
    // }
    // }
    // }
}