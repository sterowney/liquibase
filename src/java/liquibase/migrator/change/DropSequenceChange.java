package liquibase.migrator.change;

import liquibase.database.AbstractDatabase;
import liquibase.database.MSSQLDatabase;
import liquibase.database.MySQLDatabase;
import liquibase.migrator.UnsupportedChangeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Drops an existing sequence.
 */
public class DropSequenceChange extends AbstractChange {

    private String sequenceName;

    public DropSequenceChange() {
        super("dropSequence", "Drop Sequence");
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String[] generateStatements(AbstractDatabase database) throws UnsupportedChangeException {
        if (database instanceof MSSQLDatabase) {
            throw new UnsupportedChangeException("Sequences not supported in MS SQL");
        } else if (database instanceof MySQLDatabase) {
            throw new UnsupportedChangeException("Sequences not supported in MySQL");
        }

        return new String[]{"DROP SEQUENCE " + getSequenceName()};
    }

    public String getConfirmationMessage() {
        return "Sequence " + getSequenceName() + " dropped";
    }

    public Element createNode(Document currentChangeLogFileDOM) {
        Element element = currentChangeLogFileDOM.createElement("dropSequence");
        element.setAttribute("sequenceName", getSequenceName());

        return element;
    }
}
