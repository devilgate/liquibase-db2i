package liquibase.ext.db2i.sqlgenerator;

import liquibase.database.Database;
import liquibase.ext.db2i.database.DB2iDatabase;
import liquibase.ext.db2i.limits.LengthLimiter.Length;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.SetColumnRemarksGenerator;
import liquibase.statement.core.SetColumnRemarksStatement;

public class SetColumnRemarksGeneratorDB2i extends SetColumnRemarksGenerator {
    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(SetColumnRemarksStatement statement, Database database) {
        return database instanceof DB2iDatabase;
    }

    @Override
    public Sql[] generateSql(SetColumnRemarksStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String escapedTableName = database.escapeTableName(
                statement.getCatalogName(), 
                statement.getSchemaName(), 
                statement.getTableName());
        String escapedColumnName = database.escapeColumnName(
                statement.getCatalogName(), 
                statement.getSchemaName(), 
                statement.getTableName(), 
                statement.getColumnName());
        String textComment = null;
        String columnComment = null;
        
        // DB2 for i has length limits; we want to enforce them, rather than fail.
        if (database instanceof DB2iDatabase) {
            textComment = ((DB2iDatabase)database).escapeAndTruncate(statement.getRemarks(), Length.TABLE_COMMENT);
            columnComment = ((DB2iDatabase)database).escapeAndTruncate(statement.getRemarks(), Length.COLUMN_COMMENT);
        }
        else { // Should never happen
            textComment = database.escapeStringForDatabase(statement.getRemarks());
            columnComment = database.escapeStringForDatabase(statement.getRemarks());
        }
        
        return new Sql[] {
                new UnparsedSql("LABEL ON " + escapedTableName + " ("
                        + escapedColumnName
                        + " TEXT IS '" + textComment + "')", getAffectedColumn(statement)),
                new UnparsedSql("LABEL ON COLUMN " + escapedTableName + "."
                        + escapedColumnName
                        + " IS '" + columnComment + "'", getAffectedColumn(statement)) };
    }
}
