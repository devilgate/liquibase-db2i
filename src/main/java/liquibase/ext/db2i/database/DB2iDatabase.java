package liquibase.ext.db2i.database;

import liquibase.database.DatabaseConnection;
import liquibase.database.core.DB2Database;
import liquibase.exception.DatabaseException;
import liquibase.ext.db2i.limits.LengthLimiter;
import liquibase.ext.db2i.limits.LengthLimiter.Length;

public class DB2iDatabase extends DB2Database {

    @Override
    public int getPriority() {
        return super.getPriority()+5;
    }

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return conn.getDatabaseProductName().startsWith("DB2 UDB for AS/400");
    }

    @Override
    public String getDefaultDriver(String url) {
        if (url.startsWith("jdbc:as400")) {
            return "com.ibm.as400.access.AS400JDBCDriver";
        }
        return null;
    }

    @Override
    protected String getDefaultDatabaseProductName() {
        return "DB2i";
    }

    @Override
    public String getShortName() {
        return "db2i";
    }

    @Override
    public boolean supportsSchemas() {
        return true;
    }
    
    @Override
    /** 
     * This is true for DB2 LUW, but not DB2 for the iSeries.
     */
    public boolean supportsTablespaces() {
    	return false;
    }
    
    /**
     * Extends the {@link escapeStringForDatabase} method to also truncate the
     * String to a System i-specific limit.
     * 
     * @param input
     * @param length
     * @return
     */
    public String escapeAndTruncate(String input, Length length) {
    	LengthLimiter limiter = new LengthLimiter();
    	return escapeStringForDatabase(limiter.truncate(input, length));
    }
    
    @Override
    /* No it doesn't, even if DB2 LUW does. But something has changed on 
     * the i, because we're getting an error that this should fix.
     */
    public boolean jdbcCallsCatalogsSchemas() {
        return false;
    }

    
}
