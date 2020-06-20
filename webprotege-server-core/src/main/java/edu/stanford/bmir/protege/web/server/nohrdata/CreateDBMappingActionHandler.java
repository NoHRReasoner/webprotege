package edu.stanford.bmir.protege.web.server.nohrdata;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateDBMappingAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateDBMappingResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.nohrcodes.NohrDatabaseSettings;
import edu.stanford.bmir.protege.web.shared.nohrcodes.NohrDatabaseSettingsImpl;
import edu.stanford.bmir.protege.web.shared.nohrcodes.NohrResponseCodes;
import edu.stanford.bmir.protege.web.shared.nohrdbmappings.NohrColumnsTable;
import edu.stanford.bmir.protege.web.shared.nohrdbmappings.NohrDBMapping;
import edu.stanford.bmir.protege.web.shared.nohrdbmappings.NohrDBMappingImpl;
import edu.stanford.bmir.protege.web.shared.nohrdbmappings.NohrTablesTable;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.protege.editor.owl.model.classexpression.OWLExpressionParserException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.unl.fct.di.novalincs.nohr.model.*;
import pt.unl.fct.di.novalincs.nohr.utils.CreatingMappings;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.*;

/**
 * Author: Tito Ferreira<br>
 * Fct NOVA<br>
 * Master degree in computer science<br>
 */
public class CreateDBMappingActionHandler extends AbstractProjectActionHandler<CreateDBMappingAction, CreateDBMappingResult> {

    private static final Logger logger = LoggerFactory.getLogger(CreateDBMappingActionHandler.class);
    @Nonnull
    private final ProjectId projectId;

    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final HasApplyChanges changeApplicator;

    @Nonnull
    private final EntityNodeRenderer renderer;

    private NohrRepository repository;

    /*private pt.unl.fct.di.novalincs.nohr.plugin.test.NohrRuleParser parser;*/

    private UsersNohrInstances usersNohrInstances;

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Inject
    public CreateDBMappingActionHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull ProjectId projectId, EventManager<ProjectEvent<?>> eventManager,
                                        @Nonnull HasApplyChanges changeApplicator,
                                        @Nonnull EntityNodeRenderer renderer,
                                        @Nonnull RevisionManager revisionManager,
                                        @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                        @Nonnull UsersNohrInstances usersNoHRInstances,
                                        @Nonnull NohrRepository nohrRepository) {
        super(accessManager);
        this.projectId = checkNotNull(projectId);
        this.eventManager = checkNotNull(eventManager);
        this.changeApplicator = checkNotNull(changeApplicator);
        this.renderer = checkNotNull(renderer);
        this.revisionManager = revisionManager;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.repository = checkNotNull(nohrRepository);
        /*this.parser = new pt.unl.fct.di.novalincs.nohr.plugin.test.NohrRuleParser();*/
        this.usersNohrInstances = usersNoHRInstances;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(EDIT_NOHR, CREATE_DBMAPPING);
    }

    @Nonnull
    @Override
    public CreateDBMappingResult execute(@Nonnull CreateDBMappingAction action,
                                         @Nonnull ExecutionContext executionContext) {
        EventTag eventTag = eventManager.getCurrentTag();
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(eventTag);

        DatabaseType databaseType = new DatabaseType(action.getODBC());

        ODBCDriver odbcDriver = null;
        List<NohrDatabaseSettings> databaseSettings = repository.getDatabaseSettings(projectId);
        for (NohrDatabaseSettings dbSettings : databaseSettings) {
            if (dbSettings.getConnectionName().equals(action.getODBC())) {
                odbcDriver = new ODBCDriverImpl(
                        dbSettings.getConnectionName(),
                        dbSettings.getUsername(),
                        dbSettings.getPassword(),
                        dbSettings.getDatabaseName(),
                        databaseType);
            }
        }

        OWLOntologyManager manager = revisionManager.getOntologyManagerForRevision(revisionManager.getCurrentRevision());
        OWLOntology ontology = manager.getOntology(defaultOntologyIdManager.getDefaultOntologyId());
        NohrRuleParser parser;

        if (ontology != null) {
            parser = new NohrRuleParser(ontology);
        } else {
            OWLOntologyMerger merger = new OWLOntologyMerger(manager);
            try {
                ontology = merger.createMergedOntology(manager, IRI.generateDocumentIRI());
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
            parser = new NohrRuleParser(ontology);
        }

        DBMapping dbMapping;


        try {
            //User add a database mapping using the grids
            if (action.getFromTablesList().length > 0) {
                //------------------Tables--------------------------
                List<DBTable> dbTables = new LinkedList<>();
                NohrTablesTable[] uiTables = action.getFromTablesList();
                for (int i = 0; i < uiTables.length; i++) {
                    NohrTablesTable elem = uiTables[i];
                    List<String> newTableCol = new LinkedList<>();
                    newTableCol.add(elem.getColumn());
                    List<String> oldTableCol = new LinkedList<>();
                    oldTableCol.add(elem.getOnColumn());
                    DBTable tmp = new DBTable(elem.getTable(), elem.getJoinTable(), elem.getTableNumber(), elem.getTableJoinNumber(), newTableCol, oldTableCol, uiTables[0].equals(elem));
                    dbTables.add(tmp);
                }

                //------------------Columns--------------------------
                List<String[]> dbColumns = new LinkedList<>();
                NohrColumnsTable[] uiColumns = action.getSelectColumnsList();
                for (int i = 0; i < uiColumns.length; i++) {
                    NohrColumnsTable elem = uiColumns[i];
                    String[] strArray = new String[4];
                    strArray[0] = elem.getTableWithNumber();
                    strArray[1] = elem.getTableNumber();
                    strArray[2] = elem.getColumnCol();
                    strArray[3] = elem.getFloatingCol() ? "true" : "false";
                    dbColumns.add(strArray);
                }

                final List<Term> kbTerms = new LinkedList<>();
                for (int i = 0; i < action.getArity(); i++) {
                    Term kbTerm = Model.var(CreatingMappings.getVar(action.getArity(), i));
                    kbTerms.add(kbTerm);
                }
                Rule tmp = Model.rule(Model.atom(parser.getVocabulary(), action.getPredicate(), kbTerms));
                Predicate predicate = tmp.getHead().getFunctor();

                int aliasNumber = repository.getDBMappings(projectId).size();

                dbMapping = new DBMappingImpl(odbcDriver, dbTables, dbColumns, predicate, aliasNumber + 1);
            }
            //User add a database mapping writing sql
            else {

                final List<Term> kbTerms = new LinkedList<>();
                for (int i = 0; i < action.getArity(); i++) {
                    Term kbTerm = Model.var(CreatingMappings.getVar(action.getArity(), i));
                    kbTerms.add(kbTerm);
                }
                Rule tmp = Model.rule(Model.atom(parser.getVocabulary(), action.getPredicate(), kbTerms));
                Predicate predicate = tmp.getHead().getFunctor();

                dbMapping = new DBMappingImpl(odbcDriver, action.getSql(), action.getArity(), predicate);
            }

        } catch (Exception e) {
            logger.info("Failed to create dbMapping in {}", projectId);
            System.out.println("Failed to create dbMapping in " + projectId);
            return new CreateDBMappingResult(projectId,
                    eventList,
                    null, null, NohrResponseCodes.UNKNOWN_ERROR);
        }

        NohrDBMapping newDBMapping = new NohrDBMappingImpl(dbMapping.getFileSyntax());
        NohrDBMapping newUiDBMapping = new NohrDBMappingImpl(dbMapping.toString());

        try {

            repository.insertDBMapping(projectId, newDBMapping);
            usersNohrInstances.stopProjectNohrInstance(executionContext.getUserId().getUserName(), projectId);
        } catch (Exception e) {
            logger.info("Failed to insert dbMapping in {} in database", projectId);
            System.out.println("Failed to insert dbMapping in " + projectId + " in database");
            return new CreateDBMappingResult(projectId,
                    eventList,
                    null, null, NohrResponseCodes.DATABASE_ERROR);
        }

        return new CreateDBMappingResult(projectId,
                eventList,
                newDBMapping, newUiDBMapping, NohrResponseCodes.OK);
    }

    @Nonnull
    @Override
    public Class<CreateDBMappingAction> getActionClass() {
        return CreateDBMappingAction.class;
    }
}
