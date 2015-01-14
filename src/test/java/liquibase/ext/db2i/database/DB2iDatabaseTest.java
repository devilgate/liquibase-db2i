package liquibase.ext.db2i.database;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class DB2iDatabaseTest {

    @Test
    public void getShortName() {
        assertEquals("db2i", new DB2iDatabase().getShortName());
    }
    
    @Test
    public void doesnNotSupportTablepaces() {
    	assertFalse(new DB2iDatabase().supportsTablespaces());
    }
}
