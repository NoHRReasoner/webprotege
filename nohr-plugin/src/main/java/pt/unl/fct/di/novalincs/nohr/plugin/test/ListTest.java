package pt.unl.fct.di.novalincs.nohr.plugin.test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ListTest {

    public static void main(String[] args) throws Exception {
        List<NohrRuleTest> list = new LinkedList<>();
        list.add(new NohrRuleTest("rule1"));
        list.add(new NohrRuleTest("rule2"));
        list.add(new NohrRuleTest("rule3"));

        List<NohrRuleTest> delList = new LinkedList<>();
        delList.add(new NohrRuleTest("rule2"));

        list.removeAll(delList);


        /*for (NohrRuleTest deleteRule : delList) {
            for (NohrRuleTest rule : list)
                if (deleteRule.getRule().equals(rule.getRule())) {
                    list.remove(rule);
                    break;
                }
        }
*/
        System.out.println(list);


    }
}
