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
package org.richfaces.tests.metamer.ftest.richDragIndicator;

import static org.richfaces.tests.metamer.ftest.richDragIndicator.DragIndicatorAttributes.rendered;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test for rich:contextMenu component at faces/components/richContextMenu/simple.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.0.CR1
 */
public class TestDragIndicator extends AbstractWebDriverTest {

    private static final String ACCEPT_CLASS = "sample-accept-class";
    private static final String DRAGGING_CLASS = "sample-dragging-class";
    private static final String REJECT_CLASS = "sample-reject-class";
    private final Attributes<DragIndicatorAttributes> dragIndicatorAttributes = getAttributes();

    @Page
    private DragIndicatorSimplePage page;

    @Override
    public String getComponentTestPagePath() {
        return "richDragIndicator/simple.xhtml";
    }

    @Test
    @CoversAttributes({ "acceptClass", "draggingClass" })
    public void testAccepting() {
        attsSetter()
            .setAttribute(DragIndicatorAttributes.draggingClass).toValue(DRAGGING_CLASS)
            .setAttribute(DragIndicatorAttributes.acceptClass).toValue(ACCEPT_CLASS)
            .asSingleAction().perform();

        Actions actionQueue = new Actions(driver);

        Action dragging = actionQueue.clickAndHold(page.getDrag1Element()).build();
        dragging.perform();
        Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();

        // just small move to display indicator
        dragging = actionQueue.moveByOffset(1, 1).build();
        dragging.perform();
        assertTrue(page.getIndicatorElement().isDisplayed());
        assertTrue(page.getIndicatorElement().getAttribute("class").contains(DRAGGING_CLASS));

        dragging = actionQueue.moveToElement(page.getDrop1Element()).build();
        dragging.perform();
        assertTrue(page.getIndicatorElement().isDisplayed());
        assertTrue(page.getIndicatorElement().getAttribute("class").contains(ACCEPT_CLASS));

        // then release
        dragging = actionQueue.release().build();
        String drop1Content = page.getDrop1Element().getText();
        dragging.perform();
        Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();
        Graphene.waitModel().until().element(page.getDrop1Element()).text().not().equalTo(drop1Content);
    }

    @Test(groups = "smoke")
    @CoversAttributes({ "acceptClass", "draggingClass" })
    public void testDragging() {
        attsSetter()
            .setAttribute(DragIndicatorAttributes.draggingClass).toValue(DRAGGING_CLASS)
            .setAttribute(DragIndicatorAttributes.acceptClass).toValue(ACCEPT_CLASS)
            .asSingleAction().perform();

        Actions actionQueue = new Actions(driver);

        // before any mouse move, no indicator appears on page
        assertFalse(new WebElementConditionFactory(page.getIndicatorElement()).isPresent().apply(driver));

        // firstly just drag and don't move. Indicator no displayed
        Action dragging = actionQueue.clickAndHold(page.getDrag1Element()).build();
        dragging.perform();
        Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();

        // just small move to display indicator
        dragging = actionQueue.moveByOffset(1, 1).build();
        dragging.perform();
        assertTrue(page.getIndicatorElement().isDisplayed());
        assertTrue(page.getIndicatorElement().getAttribute("class").contains(DRAGGING_CLASS));

        // then move out of drag area (but not over drop area)
        dragging = actionQueue.moveByOffset(550, 10).build();
        dragging.perform();
        assertTrue(page.getIndicatorElement().isDisplayed());
        assertTrue(page.getIndicatorElement().getAttribute("class").contains(DRAGGING_CLASS));

        // this one as well
        dragging = actionQueue.release().build();
        dragging.perform();
        Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();
    }

    @Test
    @CoversAttributes({ "acceptClass", "draggingClass", "rejectClass" })
    public void testMovingOverDifferentStates() {
        attsSetter()
            .setAttribute(DragIndicatorAttributes.draggingClass).toValue(DRAGGING_CLASS)
            .setAttribute(DragIndicatorAttributes.acceptClass).toValue(ACCEPT_CLASS)
            .setAttribute(DragIndicatorAttributes.rejectClass).toValue(REJECT_CLASS)
            .asSingleAction().perform();

        for (int i = 0; i <= 20; ++i) {
            Actions actionQueue = new Actions(driver);

            Action dragging = actionQueue.clickAndHold(page.getDrag1Element()).build();
            dragging.perform();
            Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();

            // just small move to display indicator
            dragging = actionQueue.moveByOffset(1, 1).build();
            dragging.perform();
            assertTrue(page.getIndicatorElement().isDisplayed());
            assertTrue(page.getIndicatorElement().getAttribute("class").contains(DRAGGING_CLASS));

            // then move out of drag area (but not over drop area)
            dragging = actionQueue.moveByOffset(550, 10).build();
            dragging.perform();
            assertTrue(page.getIndicatorElement().isDisplayed());
            assertTrue(page.getIndicatorElement().getAttribute("class").contains(DRAGGING_CLASS));

            dragging = actionQueue.moveToElement(page.getDrop1Element()).build();
            dragging.perform();
            assertTrue(page.getIndicatorElement().isDisplayed());
            assertTrue(page.getIndicatorElement().getAttribute("class").contains(ACCEPT_CLASS));

            dragging = actionQueue.moveToElement(page.getDrop2Element()).build();
            dragging.perform();
            assertTrue(page.getIndicatorElement().isDisplayed());
            assertTrue(page.getIndicatorElement().getAttribute("class").contains(REJECT_CLASS));

            // then release
            dragging = actionQueue.release().build();
            dragging.perform();
            Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();
        }
    }

    @Test
    @CoversAttributes({ "draggingClass", "rejectClass" })
    public void testRejecting() {
        attsSetter()
            .setAttribute(DragIndicatorAttributes.draggingClass).toValue(DRAGGING_CLASS)
            .setAttribute(DragIndicatorAttributes.rejectClass).toValue(REJECT_CLASS)
            .asSingleAction().perform();

        Actions actionQueue = new Actions(driver);

        Action dragging = actionQueue.clickAndHold(page.getDrag1Element()).build();
        dragging.perform();
        Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();

        // just small move to display indicator
        dragging = actionQueue.moveByOffset(1, 1).build();
        dragging.perform();
        assertTrue(page.getIndicatorElement().isDisplayed());
        assertTrue(page.getIndicatorElement().getAttribute("class").contains(DRAGGING_CLASS));

        dragging = actionQueue.moveToElement(page.getDrop2Element()).build();
        dragging.perform();
        assertTrue(page.getIndicatorElement().isDisplayed());
        assertTrue(page.getIndicatorElement().getAttribute("class").contains(REJECT_CLASS));

        // then release
        dragging = actionQueue.release().build();
        dragging.perform();
        Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();
    }

    @Test(groups = "smoke")
    @CoversAttributes({ "rendered", "draggingClass" })
    public void testRendered() throws InterruptedException {
        attsSetter()
            .setAttribute(DragIndicatorAttributes.draggingClass).toValue(DRAGGING_CLASS)
            .setAttribute(DragIndicatorAttributes.rendered).toValue(true)
            .asSingleAction().perform();

        // before any mouse move, no indicator appears on page
        assertFalse(new WebElementConditionFactory(page.getIndicatorElement()).isPresent().apply(driver));

        Actions actionQueue = new Actions(driver);

        // firstly just drag and don't move. Indicator no displayed
        Action dragging = actionQueue.clickAndHold(page.getDrag1Element()).build();
        dragging.perform();
        Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();

        // just small move to display indicator
        dragging = actionQueue.moveByOffset(1, 1).build();
        dragging.perform();
        assertTrue(page.getIndicatorElement().isDisplayed());
        assertTrue(page.getIndicatorElement().getAttribute("class").contains(DRAGGING_CLASS));

        // simulate drop
        dragging = actionQueue.release().build();
        Graphene.waitModel().until().element(page.getIndicatorElement()).is().present();
        dragging.perform();
        Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();

        // and now the same with indicator not rendered
        dragIndicatorAttributes.set(rendered, false);
        assertFalse(new WebElementConditionFactory(page.getIndicatorElement()).isPresent().apply(driver));

        // just small move to attempt display indicator
        dragging = actionQueue.clickAndHold(page.getDrag1Element()).moveByOffset(1, 1).build();
        dragging.perform();
        Graphene.waitModel().until().element(page.getIndicatorElement()).is().not().present();
        actionQueue.release().perform();
    }
}
