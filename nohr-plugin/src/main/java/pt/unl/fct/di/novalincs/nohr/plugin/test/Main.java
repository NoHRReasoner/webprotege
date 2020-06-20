package pt.unl.fct.di.novalincs.nohr.plugin.test;

import com.google.common.math.IntMath;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import pt.unl.fct.di.novalincs.nohr.model.Rule;
import pt.unl.fct.di.novalincs.nohr.model.vocabulary.DefaultVocabulary;
import pt.unl.fct.di.novalincs.nohr.model.vocabulary.Vocabulary;
import pt.unl.fct.di.novalincs.nohr.parsing.NoHRParser;
import pt.unl.fct.di.novalincs.nohr.parsing.NoHRRecursiveDescentParser;
import pt.unl.fct.di.novalincs.nohr.plugin.rules.RuleExpressionChecker;

import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner in = new Scanner(System.in);
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        final OWLOntologyMerger merger = new OWLOntologyMerger(ontologyManager);
        OWLOntology ontology = null;

        {
            try {
                ontology = merger.createMergedOntology(ontologyManager, IRI.generateDocumentIRI());
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
        }

        Vocabulary vocabulary = new DefaultVocabulary(ontology);
        NoHRParser parser = new NoHRRecursiveDescentParser(vocabulary);
        RuleExpressionChecker checker = new RuleExpressionChecker(parser);

        String input = in.nextLine();
        Rule rule = null;
        try {
            rule = checker.createObject(input);
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("head: " + rule.getHead());
        System.out.println("body: " + rule.getBody());
        System.out.println("negative body: " + rule.getNegativeBody());

    }
}
