package liquibase.ext.db2i.sqlgenerator;

import liquibase.database.Database;
import liquibase.ext.db2i.database.DB2iDatabase;
import liquibase.ext.db2i.limits.LengthLimiter.Length;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.SetTableRemarksGenerator;
import liquibase.statement.core.SetTableRemarksStatement;

public class SetTableRemarksGeneratorDB2i extends SetTableRemarksGenerator {

    @Override
    public boolean supports(SetTableRemarksStatement statement, Database database) {
        return database instanceof DB2iDatabase;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public Sql[] generateSql(SetTableRemarksStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {

        StringBuilder sql = new StringBuilder("LABEL ON TABLE ");
        sql.append(
        database.escapeTableName(
                statement.getCatalogName(), 
                statement.getSchemaName(), 
                statement.getTableName()));
        sql.append(" IS '");
    
        // The else should never happen, but you can't be too careful.
        if (database instanceof DB2iDatabase) {
            sql.append(((DB2iDatabase)database).escapeAndTruncate(
                    statement.getRemarks() , Length.TABLE_COMMENT));
        }
        else {
            sql.append(database.escapeStringForDatabase(statement.getRemarks()));
        }
        sql.append("'");

        return new Sql[]{new UnparsedSql(sql.toString(), getAffectedTable(statement))};
    }
}
