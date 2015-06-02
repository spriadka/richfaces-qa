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
package org.richfaces.tests.metamer.ftest.richDragSource;

import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.0.CR1
 *
 */
public class TestDragSource extends AbstractDragSourceTest {

    @Override
    public String getComponentTestPagePath() {
        return "richDragSource/simple.xhtml";
    }

    @Test
    public void testCustomIndicator() {
        super.testCustomIndicator();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12441")
    public void testDefaultIndicator() {
        super.testDefaultIndicator();
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        super.testRendered();
    }

    @Test
    public void testType() {
        super.testType();
    }
}
