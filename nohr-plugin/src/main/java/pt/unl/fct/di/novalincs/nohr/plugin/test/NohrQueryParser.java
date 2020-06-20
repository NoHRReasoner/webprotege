package pt.unl.fct.di.novalincs.nohr.plugin.test;

import org.protege.editor.owl.model.classexpression.OWLExpressionParserException;
import pt.unl.fct.di.novalincs.nohr.model.Query;
import pt.unl.fct.di.novalincs.nohr.plugin.query.QueryExpressionChecker;

public class NohrQueryParser extends NohrParser {

    QueryExpressionChecker checker;

    public NohrQueryParser() {
        checker = new QueryExpressionChecker(getParser());
    }

    public void checkQuery(String str) throws OWLExpressionParserException {
        checker.check(str);
    }

    public Query getQuery(String str) throws OWLExpressionParserException {
        return checker.createObject(str);
    }

    public boolean isConsistent() {return isConsistentParser();}
}
