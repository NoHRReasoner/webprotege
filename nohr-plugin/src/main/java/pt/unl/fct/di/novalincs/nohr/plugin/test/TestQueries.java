package pt.unl.fct.di.novalincs.nohr.plugin.test;

import pt.unl.fct.di.novalincs.nohr.hybridkb.NoHRHybridKBConfiguration;
import pt.unl.fct.di.novalincs.nohr.model.*;
import pt.unl.fct.di.novalincs.nohr.plugin.NoHRInstance;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class TestQueries {
    public static void main(String[] args) throws Exception {

        NohrRuleParser ruleParser = new NohrRuleParser();
        NoHRInstance noHRInstance = NoHRInstance.getInstance();
        Set<Rule> rules = new LinkedHashSet<>();
        /*rules.add(ruleParser.createRule("T(b)"));
        rules.add(ruleParser.createRule("AdmissibleImporter(?X) :- ShpmtImporter(?Y,?X), not RegisteredTransgressor(?X)"));
        rules.add(ruleParser.createRule("ApprovedImporterOf(i02486202,?X) :- EdibleVegetable(?X)"));
        rules.add(ruleParser.createRule("ApprovedImporterOf(i01920703,?X) :- GrapeTomato(?X)"));
        rules.add(ruleParser.createRule("CommodCountry(?X,?Y) :- ShpmtCommod(?Z,?X), ShpmtCountry(?Z,?Y)"));
        rules.add(ruleParser.createRule("CompliantShpmt(?X) :- ShpmtCommod(?X,?Y), HTSCode(?Y,?Z), ShpmtDeclHTSCode(?X,?Z)"));
        rules.add(ruleParser.createRule("ExpeditableImporter(?X,?Y) :- ShpmtCommod(?Z,?X), ShpmtImporter(?Z,?Y), AdmissibleImporter(?Y), ApprovedImporterOf(?Y,?X)"));
        rules.add(ruleParser.createRule("Inspection(?X) :- ShpmtCommod(?X,?Y), not CompliantShpmt(?X)"));
        rules.add(ruleParser.createRule("Inspection(?X) :- ShpmtCommod(?X,?Y), Tomato(?Y), ShpmtCountry(?X,slovakia)"));
        rules.add(ruleParser.createRule("Inspection(?X) :- ShpmtCommod(?X,?Y), not NoInspection(?X)"));
        rules.add(ruleParser.createRule("NoInspection(?X) :- ShpmtCommod(?X,?Y), CommodCountry(?Y,?Z), EUCountry(?Z)"));*/
        Program program = new HashSetProgram(rules);

        /*pt.unl.fct.di.novalincs.nohr.plugin.test.NohrQueryParser queryParser = new pt.unl.fct.di.novalincs.nohr.plugin.test.NohrQueryParser();*/

/*
        NoHRHybridKBConfiguration conf = new NoHRHybridKBConfiguration(new File(System.getenv("XSB_BIN_DIRECTORY")), new File(System.getenv("KONCLUDE_BIN")), true,true,true, DLInferenceEngine.HERMIT);
*/
        NoHRHybridKBConfiguration conf = new NoHRHybridKBConfiguration();
        /*NoHRHybridKB noHRHybridKB = null;
        List<Answer> answers = null;*/
        try {
            DBMappingSet dbMappingsSet = new HashSetDBMappingSet(new HashSet<DBMapping>());
            noHRInstance.start(conf, ruleParser.getOntology(), program, dbMappingsSet,ruleParser.getVocabulary());
            noHRInstance.stop();
            /*noHRHybridKB = noHRInstance.getHybridKB();
            *//*Query query = queryParser.getQuery("T(?X)");*//*
            *//*Query query = queryParser.getQuery("Inspection(?X)");*//*
            Query query = queryParser.getQuery("t");
            answers = noHRHybridKB.allAnswers(query, true, true, true);
            noHRInstance.stop();

            Map<String, String> labelsName = new HashMap<>();
            String str;
            Set<OWLAnnotationAssertionAxiom> annotationsAssertionAxioms = queryParser.getOntology().getAxioms(AxiomType.ANNOTATION_ASSERTION);
            System.out.println(annotationsAssertionAxioms);
            for (OWLAxiom a : annotationsAssertionAxioms) {
                str = a.toString().split("<")[1];
                labelsName.putIfAbsent(str.split(">")[0], str.split("\"")[1]);
            }
            System.out.println("-----------------------");
            for (Answer x : answers) {
                for (int i = 0; i < x.getValues().size(); i++) {
                    if (labelsName.containsKey(x.getValues().get(i).toString()))
                        System.out.println("Resposta: " + x.getValuation().toString().toLowerCase() + " - " + labelsName.get(x.getValues().get(i).toString()));
                    else
                        System.out.println("Resposta: " + x.getValuation().toString().toLowerCase() + " - " + x.getValues().get(i).toString());
                }

            }*/
        } catch (
                /*OWLExpressionParserException |*/ Exception e) {
            e.printStackTrace();
        }
    }
}
