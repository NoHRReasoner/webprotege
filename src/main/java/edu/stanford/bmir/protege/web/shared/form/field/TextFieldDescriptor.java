package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(TextFieldDescriptor.TYPE)
public class TextFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "Text";

    private String placeholder;

    private StringType stringType;

    private LineMode lineMode;

    private String pattern;

    private String patternViolationErrorMessage;

    private TextFieldDescriptor() {
    }

    public TextFieldDescriptor(@Nonnull String placeholder,
                               @Nonnull StringType stringType,
                               @Nonnull LineMode lineMode,
                               @Nonnull String pattern,
                               @Nonnull String patternViolationErrorMessage) {
        this.placeholder = checkNotNull(placeholder);
        this.stringType = checkNotNull(stringType);
        this.lineMode = checkNotNull(lineMode);
        this.pattern = checkNotNull(pattern);
        this.patternViolationErrorMessage = checkNotNull(patternViolationErrorMessage);
    }

    @Nonnull
    @Override
    @JsonIgnore
    public String getAssociatedType() {
        return TYPE;
    }

    @Nonnull
    public static String getType() {
        return TYPE;
    }

    @Nonnull
    public String getPlaceholder() {
        return placeholder;
    }

    @Nonnull
    public StringType getStringType() {
        return stringType;
    }

    @Nonnull
    public String getPattern() {
        return pattern;
    }

    @Nonnull
    public String getPatternViolationErrorMessage() {
        return patternViolationErrorMessage;
    }

    @Nonnull
    public LineMode getLineMode() {
        return lineMode;
    }
}
