package OLD_com.ldbc;

public class ClientException extends Exception
{
    private static final long serialVersionUID = 7166804842129940500L;

    public ClientException( String message )
    {
        super( message );
    }

    public ClientException()
    {
        super();
    }

    public ClientException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public ClientException( Throwable cause )
    {
        super( cause );
    }
}
