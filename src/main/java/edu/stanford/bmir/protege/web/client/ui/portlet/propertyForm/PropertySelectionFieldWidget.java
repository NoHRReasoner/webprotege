package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.ontology.properties.PropertiesTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;

public class PropertySelectionFieldWidget extends AbstractSelectionFieldWidget {

	public PropertySelectionFieldWidget(Project project, DispatchServiceManager dispatchServiceManager) {
		super(project, dispatchServiceManager);
	}

	@Override
	public Selectable createSelectable() {
		throw new RuntimeException("Implementation broken");
//		PropertiesTreePortlet propertiesTreePortlet = new PropertiesTreePortlet(getProject());
		// no need for this because we want to 
		// let parent containers use FitLayout() 
		// or AnchorLayoutData("100% 100%")
		//propertiesTreePortlet.setHeight(250);
		//propertiesTreePortlet.setWidth(200);
//		return propertiesTreePortlet;
	}

}
