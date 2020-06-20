package pt.unl.fct.di.novalincs.nohr.model;

/*
 * #%L
 * nohr-reasoner
 * %%
 * Copyright (C) 2014 - 2015 NOVA Laboratory of Computer Science and Informatics (NOVA LINCS)
 * %%
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * #L%
 */
import pt.unl.fct.di.novalincs.nohr.model.vocabulary.ModelVisitor;

/**
 * Represents variable.
 *
 * @author Nuno Costa
 */
public interface Variable extends Term, Comparable<Variable> {

    @Override
    Variable accept(ModelVisitor visitor);
}
