package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.nohrcodes.NohrResponseCodes;
import edu.stanford.bmir.protege.web.shared.nohrrules.NohrRule;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Tito Ferreira<br>
 * Fct NOVA<br>
 * Master degree in computer science<br>
 * Date: 25/09/2019
 */
public class CreateRulesResult extends AbstractNohrResult<NohrRule> {

    private NohrRule rule;

    private NohrResponseCodes code;

    private CreateRulesResult() {
    }

    public CreateRulesResult(@Nonnull ProjectId projectId, @Nonnull EventList<ProjectEvent<?>> eventList, NohrRule rule, NohrResponseCodes code) {
        super(projectId, eventList);
        this.rule = rule;
        this.code = code;
    }

    public NohrRule getRule() {
        return rule;
    }

    public NohrResponseCodes getCode() {
        return code;
    }
}