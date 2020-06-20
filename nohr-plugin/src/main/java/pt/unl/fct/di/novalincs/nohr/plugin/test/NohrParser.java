package pt.unl.fct.di.novalincs.nohr.plugin.test;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import pt.unl.fct.di.novalincs.nohr.model.vocabulary.DefaultVocabulary;
import pt.unl.fct.di.novalincs.nohr.model.vocabulary.Vocabulary;
import pt.unl.fct.di.novalincs.nohr.parsing.NoHRParser;
import pt.unl.fct.di.novalincs.nohr.parsing.NoHRRecursiveDescentParser;
import pt.unl.fct.di.novalincs.nohr.translation.dl.KoncludeReasonerWrapper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class NohrParser {

    OWLOntologyManager ontologyManager;
    /*final OWLOntologyMerger merger;*/
    OWLOntology ontology;
    Vocabulary vocabulary;
    NoHRParser parser;
    Map<String, String> concreteNames = new HashMap<>();
    private Path data;

    public NohrParser() {
        ontologyManager = OWLManager.createOWLOntologyManager();

        /*merger = new OWLOntologyMerger(ontologyManager);*/
        /*data = Paths.get("C:\\Users\\titox\\Desktop\\wp_ontology_formats\\cargoInspection.owx");*/
        /*data = Paths.get("C:\\Users\\titox\\Downloads\\snomedct_owlf.owl");*/
        data = Paths.get("C:\\Users\\titox\\Downloads\\TestOntologies2013\\TestOntologies2013\\snomedct_owlf.owl");

        {
            try {
                ontologyManager.loadOntologyFromOntologyDocument(data.toFile());
                OWLOntologyMerger merger = new OWLOntologyMerger(ontologyManager);
                ontology = merger.createMergedOntology(ontologyManager, IRI.generateDocumentIRI());
                vocabulary = new DefaultVocabulary(ontology);
                /*ontology = merger.createMergedOntology(ontologyManager, IRI.generateDocumentIRI());*/
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
        }
        isConsistentParser();


        /*for (OWLEntity entity : ontology.getSignature()) {
            concreteNames.put(entity.getIRI().toString(), concreteRepresentations(entity).iterator().next());
        }*/

        vocabulary = new DefaultVocabulary(ontology);
        parser = new NoHRRecursiveDescentParser(vocabulary);

        /*File outFile = new File("C:/ontology/ont.owl");
        IRI outIRI = IRI.create(outFile);
        try {
            ontologyManager.saveOntology(ontology, outIRI);
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }*/
    }

    public boolean isConsistentParser() {
        KoncludeReasonerWrapper konclude = null;
        try {
            konclude = new KoncludeReasonerWrapper("C:\\opt\\Konclude\\Binaries\\Konclude.exe", ontology);
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
        return konclude.consistency();
    }

    public NoHRParser getParser() {
        return parser;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    /*public String getConcreteNames(String iri) {
        if (!concreteNames.containsKey(iri))
            return iri;
        return concreteNames.get(iri);
    }*/

   /* protected Set<String> concreteRepresentations(OWLEntity entity) {
        final Set<String> result = new HashSet<>();
        final String fragment = entity.getIRI().toURI().getFragment();

        if (fragment != null) {
            result.add(fragment);
        }

        final OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
        OWLAnnotationProperty rdfsLabel = dataFactory.getRDFSLabel();

        for (final OWLAnnotation annotation
                : EntitySearcher.getAnnotations(entity, ontology, rdfsLabel)) {

            final OWLAnnotationValue value = annotation.getValue();

            if (value instanceof OWLLiteral) {
                result.add(((OWLLiteral) value).getLiteral());
            }

        }

        return result;
    }*/
}
