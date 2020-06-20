package pt.unl.fct.di.novalincs.nohr.plugin.test;

public class NohrRuleTest {
    private String rule;

    public NohrRuleTest(String rule) {
        this.rule = rule;
    }

    public NohrRuleTest() {
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String toString() { return rule;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NohrRuleTest)) return false;

        NohrRuleTest that = (NohrRuleTest) o;

        return rule != null ? rule.equals(that.rule) : that.rule == null;
    }

    @Override
    public int hashCode() {
        return rule != null ? rule.hashCode() : 0;
    }
}
