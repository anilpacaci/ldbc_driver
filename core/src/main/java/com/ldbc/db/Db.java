package com.ldbc.db;

import java.util.HashMap;
import java.util.Map;

public abstract class Db
{
    private final Map<Class<? extends Operation<?>>, Class<? extends OperationHandler<?>>> operationHandlers = new HashMap<Class<? extends Operation<?>>, Class<? extends OperationHandler<?>>>();
    private boolean isInitialized = false;
    private boolean isCleanedUp = false;

    public final void init( Map<String, String> properties ) throws DbException
    {
        if ( true == isInitialized )
        {
            throw new DbException( "DB may be initialized only once" );
        }
        isInitialized = true;
        onInit( properties );
    }

    /**
     * Called once to initialize state for DB client
     */
    protected abstract void onInit( Map<String, String> properties ) throws DbException;

    public final void cleanup() throws DbException
    {
        if ( true == isCleanedUp )
        {
            throw new DbException( "DB may be cleaned up only once" );
        }
        isCleanedUp = true;
        onCleanup();
    }

    /**
     * Called once to cleanup state for DB client
     */
    protected abstract void onCleanup() throws DbException;

    protected final <A extends Operation<?>, H extends OperationHandler<A>> void registerOperationHandler(
            Class<A> operationType, Class<H> operationHandlerType ) throws DbException
    {
        if ( null != operationHandlers.get( operationType ) )
        {
            throw new DbException( String.format( "Client already has handler registered for %s",
                    operationType.getClass() ) );
        }
        operationHandlers.put( operationType, operationHandlerType );
    }

    public final OperationHandler<?> getOperationHandler( Operation<?> operation ) throws DbException
    {
        Class<? extends OperationHandler<?>> operationHandlerType = (Class<? extends OperationHandler<?>>) operationHandlers.get( operation.getClass() );
        if ( null == operationHandlerType )
        {
            throw new DbException( String.format( "No handler registered for %s", operation.getClass() ) );
        }
        try
        {
            return operationHandlerType.getConstructor().newInstance();
        }
        catch ( Exception e )
        {
            throw new DbException( String.format( "Unable to instantiate handler %s", operationHandlerType ) );
        }
    }
}
