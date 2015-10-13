/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Custom validator for capital names.
 */
@FacesValidator("org.richfaces.CaliforniaFirstValidator")
public class CaliforniaFirstValidator implements Validator {

    public static final String VALIDATOR_ERROR_MSG = "California has to be the first state.";
    private static final FacesMessage MESSAGE = new FacesMessage(FacesMessage.SEVERITY_ERROR, VALIDATOR_ERROR_MSG,
        VALIDATOR_ERROR_MSG);

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null || !(value instanceof String)) {
            throw new ValidatorException(MESSAGE);
        }
        String val = (String) value;
        if (!"California".equals(val)) {
            throw new ValidatorException(MESSAGE);
        }
    }
}
