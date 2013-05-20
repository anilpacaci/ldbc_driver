package com.ldbc.db;

public class OperationException extends Exception
{
    private static final long serialVersionUID = 6646883591588721475L;

    public OperationException( String message )
    {
        super( message );
    }

    public OperationException()
    {
        super();
    }

    public OperationException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public OperationException( Throwable cause )
    {
        super( cause );
    }
}
