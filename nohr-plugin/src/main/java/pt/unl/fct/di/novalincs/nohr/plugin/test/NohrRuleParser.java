package pt.unl.fct.di.novalincs.nohr.plugin.test;

import org.protege.editor.owl.model.classexpression.OWLExpressionParserException;
import pt.unl.fct.di.novalincs.nohr.model.Rule;
import pt.unl.fct.di.novalincs.nohr.plugin.rules.RuleExpressionChecker;

public class NohrRuleParser extends NohrParser {

    RuleExpressionChecker checker;

    public NohrRuleParser() {
        checker = new RuleExpressionChecker(getParser());
    }

    public void checkRule(String str) throws OWLExpressionParserException {
        checker.check(str);
    }

    public Rule createRule(String str) throws OWLExpressionParserException {
        return checker.createObject(str);
    }
}
