package edu.stanford.bmir.protege.web.client.nohrrules;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.tag.ProjectTagsStyleManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.nohrrules.NohrRule;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.protege.gwt.graphtree.client.TreeNodeRenderer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Author: Tito Ferreira<br>
 * Fct NOVA<br>
 * Master degree in computer science<br>
 */
public class NohrRuleHtmlRenderer implements TreeNodeRenderer<NohrRule> {

    private static final String NO_DISPLAY_NAME = "_____";

    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final Messages messages;

    private ImmutableList<DictionaryLanguage> primaryLanguages = ImmutableList.of();

    private ImmutableList<DictionaryLanguage> secondaryLanguages = ImmutableList.of();

    private boolean renderTags = true;

    private int highlightStart = -1;

    private int highlightEnd = -1;

    @Inject
    public NohrRuleHtmlRenderer(@Nonnull LoggedInUserProvider loggedInUserProvider,
                                  @Nonnull Messages messages) {
        this.loggedInUserProvider = checkNotNull(loggedInUserProvider);
        this.messages = checkNotNull(messages);
    }

    public void setRenderTags(boolean renderTags) {
        this.renderTags = renderTags;
    }

    public void setDisplayLanguage(@Nonnull DisplayNameSettings displayNameSettings) {
        primaryLanguages = displayNameSettings.getPrimaryDisplayNameLanguages().stream()
                .map(DictionaryLanguageData::getDictionaryLanguage)
                .collect(toImmutableList());
        secondaryLanguages = displayNameSettings.getSecondaryDisplayNameLanguages().stream()
                .map(DictionaryLanguageData::getDictionaryLanguage)
                .collect(toImmutableList());

    }

    public void clearHighlight() {
        highlightStart = -1;
        highlightEnd = -1;
    }

    public void setHighlight(int start, int end) {
        this.highlightStart = start;
        this.highlightEnd = end;
    }

    @Override
    public String getHtmlRendering(NohrRule node) {
        GWT.log("[EntityNodeHtmlRenderer] Rendering node: " + node);
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='wp-entity-node'>");
        renderIcon(node, sb);
        renderDisplayName(node, sb);
        renderCommentsIcon(node, sb);
        renderWatchesIcon(node, sb);
        renderTags(node, sb);
        sb.append("</div>");
        return sb.toString();
    }

    private void renderIcon(NohrRule node, StringBuilder sb) {
        String iconIri;
        DataResource icon = getIcon(node);
        sb.append("<img src='").append(icon.getSafeUri().asString()).append("'/>");
    }

    private void renderDisplayName(NohrRule node, StringBuilder sb) {
        sb.append("<div class='wp-entity-node__display-name'>");
        renderPrimaryDisplayName(node, sb);
        //renderSecondaryDisplayName(node, sb);
        sb.append("</div>");
    }

    private void renderPrimaryDisplayName(NohrRule node, StringBuilder sb) {

            sb.append(highlightText(node.getRule()));

    }

    private String highlightText(@Nonnull String text) {
        if(highlightStart < 0) {
            return text;
        }
        int start = Math.min(highlightStart, text.length() - 1);
        int end = Math.min(highlightEnd, text.length() - 1);
        if(start < end) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div>");
            sb.append(text.substring(0, start));
            sb.append("<span class=\"web-protege-entity-match-substring\">");
            sb.append(text.substring(start, end));
            sb.append("</span>");
            sb.append(text.substring(end));
            sb.append("</div>");
            return sb.toString();
        }
        else {
            return text;
        }
    }

    private void renderSecondaryDisplayName(NohrRule node, StringBuilder sb) {
        String secondaryText = node.getRule();
        if (secondaryText != null) {
            sb.append(" <span class='wp-entity-node__display-name__secondary-language'>");
            sb.append(secondaryText);
            sb.append("</span>");
        }
    }

    private void renderCommentsIcon(NohrRule node, StringBuilder sb) {

        /*if (node.getOpenCommentCount() > 0) {
            sb.append("<img style='padding-left: 6px;' src='").append(BUNDLE.svgCommentSmallFilledIcon().getSafeUri().asString()).append("'/>");
            sb.append("<div style='padding-left: 4px; padding-bottom: 4px; font-size: smaller;'> (").append(node.getOpenCommentCount()).append(")</div>");
        }*/
    }

    private void renderWatchesIcon(NohrRule node, StringBuilder sb) {
        /*node.getWatches().stream()
                .filter(w -> loggedInUserProvider.getCurrentUserId().equals(w.getUserId()))
                .map(Watch::getType)
                .forEach(watchType -> {
                    sb.append("<img style='padding-left: 4px;' src='");
                    if (watchType == WatchType.ENTITY) {
                        sb.append(BUNDLE.svgEyeIcon().getSafeUri().asString());
                    }
                    else {
                        sb.append(BUNDLE.svgEyeIconDown().getSafeUri().asString());
                    }
                    sb.append("'/>");
                });*/
    }

    private void renderTags(NohrRule node, StringBuilder sb) {
        /*if(!renderTags) {
            return;
        }
        Collection<Tag> tags = node.getTags();
        tags.forEach(tag -> renderTag(tag, sb));*/
    }

    private void renderTag(Tag tag, StringBuilder sb) {
        sb.append("<div title='")
                .append(tag.getDescription())
                .append("' class='wp-tag wp-tag--inline-tag ")
                .append(ProjectTagsStyleManager.getTagClassName(tag.getTagId()))
                .append("'")
                .append(">");
        sb.append(tag.getLabel());
        sb.append("</div>");
    }

    private void renderNoDisplayName(NohrRule node, StringBuilder sb) {
        sb.append("<span class='wp-entity-node__display-name__no-display-name' title='")
                .append(messages.displayName_noDisplayName_helpText(node.getRule().toLowerCase(),
                        node.getRule()))
                .append("'>");
        sb.append(NO_DISPLAY_NAME);
        sb.append("</span>");
    }

    @Nonnull
    private DataResource getIcon(@Nonnull NohrRule node) {

            return BUNDLE.svgDatatypeIcon();

    }

    public void setPrimaryDisplayLanguage(@Nonnull DictionaryLanguage language) {
        this.primaryLanguages = ImmutableList.of(checkNotNull(language));
    }
}