package liquibase.migrator.change;

import liquibase.database.AbstractDatabase;
import liquibase.database.MSSQLDatabase;
import liquibase.database.MySQLDatabase;
import liquibase.migrator.UnsupportedChangeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Renames an existing column.
 */
public class RenameColumnChange extends AbstractChange {
    private String tableName;
    private String oldColumnName;
    private String newColumnName;
    private String columnDataType;

    public RenameColumnChange() {
        super("renameColumn", "Rename Column");
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOldColumnName() {
        return oldColumnName;
    }

    public void setOldColumnName(String oldColumnName) {
        this.oldColumnName = oldColumnName;
    }

    public String getNewColumnName() {
        return newColumnName;
    }

    public void setNewColumnName(String newColumnName) {
        this.newColumnName = newColumnName;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public String[] generateStatements(AbstractDatabase database) throws UnsupportedChangeException {
        if (database instanceof MSSQLDatabase) {
            return new String[]{"exec sp_rename '" + tableName + "." + oldColumnName + "', " + newColumnName};
        } else if (database instanceof MySQLDatabase) {
            if (columnDataType == null) {
                throw new RuntimeException("columnDataType is required to rename columns with MySQL");
            }
            
            return new String[]{"ALTER TABLE " + tableName + " CHANGE " + oldColumnName + " " + newColumnName + " " + columnDataType};
        }

        return new String[]{"ALTER TABLE " + tableName + " RENAME COLUMN " + oldColumnName + " TO " + newColumnName};
    }

    protected AbstractChange[] createInverses() {
        RenameColumnChange inverse = new RenameColumnChange();
        inverse.setTableName(getTableName());
        inverse.setOldColumnName(getNewColumnName());
        inverse.setNewColumnName(getOldColumnName());
        inverse.setColumnDataType(getColumnDataType());

        return new AbstractChange[]{
                inverse
        };
    }

    public String getConfirmationMessage() {
        return "Column with the name " + oldColumnName + " has been renamed to " + newColumnName;
    }

    public Element createNode(Document currentChangeLogFileDOM) {
        Element node = currentChangeLogFileDOM.createElement("renameColumn");
        node.setAttribute("tableName", getTableName());
        node.setAttribute("oldColumnName", getOldColumnName());
        node.setAttribute("newColumnName", getNewColumnName());

        return node;
    }
}
