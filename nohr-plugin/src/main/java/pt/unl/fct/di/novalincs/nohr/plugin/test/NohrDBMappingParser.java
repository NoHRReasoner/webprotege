package pt.unl.fct.di.novalincs.nohr.plugin.test;

import org.protege.editor.owl.model.classexpression.OWLExpressionParserException;
import pt.unl.fct.di.novalincs.nohr.model.DBMapping;
import pt.unl.fct.di.novalincs.nohr.plugin.dbmapping.DBMappingExpressionChecker;

/**
 * Author: Tito Ferreira<br>
 * Fct NOVA<br>
 * Master degree in computer science<br>
 */
public class NohrDBMappingParser extends NohrParser {

    DBMappingExpressionChecker checker;

    public NohrDBMappingParser() {
        super();
        checker = new DBMappingExpressionChecker(getParser());
    }

    public void checkDBMapping(String str) throws OWLExpressionParserException {
        checker.check(str);
    }
    public DBMapping createDBMapping(String str) throws OWLExpressionParserException {
        return checker.createObject(str);
    }
}
