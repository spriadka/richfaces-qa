/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.testng.annotations.Test;

/**
 * Selenium tests for page faces/components/richOrderingList/withColumn.xhtml.
 *
 * It checks whether the moving is OK.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestOrderingList extends AbstractOrderingListTest {

    @Override
    public String getComponentTestPagePath() {
        return "richOrderingList/withColumn.xhtml";
    }

    @Test
    public void testInit() {
        assertVisible(orderingList.advanced().getRootElement(), "The ordering list should be visible.");
        assertButtonDisabled(orderingList.advanced().getBottomButtonElement(), "The button [bottom] should be disabled.");
        assertButtonDisabled(orderingList.advanced().getDownButtonElement(), "The button [down] should be disabled.");
        assertButtonDisabled(orderingList.advanced().getTopButtonElement(), "The button [top] should be disabled.");
        assertButtonDisabled(orderingList.advanced().getUpButtonElement(), "The button [up] should be disabled.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14261")
    public void testOrderingListWontShiftPageWhenBodyHasDirection() {
        final int delta = 10;
        final Long originalWidth = (Long) executeJS("return jQuery(document).width();");
        for (String direction : new String[] { "rtl", "ltr" }) {
            // set body direction
            executeJS("jQuery('body').attr('dir','" + direction + "');");
            // interact with pickList
            orderingList.select(0).putItAfter(5);
            // check page did not shift
            Long actualWidth = (Long) executeJS("return jQuery(document).width();");
            assertEquals(actualWidth, originalWidth, delta);
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-13558")
    public void testScrollingWithKeyboard() {
        final Action workaround = new Action() {
            @Override
            public void perform() {
                orderingList.select(1);
                // workaround for webdriver issue https://code.google.com/p/selenium/issues/detail?id=7937
                // the initial focus of keyboard is in browser's url bar instead on the actual clicked item
                // clicking any button on the page should workaround this problem
                orderingList.advanced().getUpButtonElement().click();
                orderingList.advanced().getDownButtonElement().click();
            }
        };
        checkScrollingWithKeyboard(orderingList.advanced().getRootElement(), orderingList.advanced().getItemsElements(), workaround);
    }

    @Test(groups = "smoke")
    public void testSelectFirst() {
        orderingList.select(0);
        checkButtonsStateTop();
    }

    @Test
    public void testSelectLast() {
        orderingList.select(ChoicePickerHelper.byIndex().last());
        checkButtonsStateBottom();
    }

    @Test
    public void testSelectMiddle() {
        orderingList.select(2);
        checkButtonsStateMiddle();
    }

    @Test(groups = "smoke")
    @CoversAttributes({ "itemLabel", "itemValue" })
    public void testSubmit() {
        String firstBefore = orderingList.advanced().getItemsElements().get(0).getText();
        String thirdBefore = orderingList.advanced().getItemsElements().get(2).getText();
        orderingList.select(2).putItBefore(0);
        submitAndCheckElementsOrderPersists();
        String firstAfter = orderingList.advanced().getItemsElements().get(0).getText();
        String secondAfter = orderingList.advanced().getItemsElements().get(1).getText();
        assertEquals(firstAfter, thirdBefore, "After submitting the ordering list doesn't preserve the chosen order.");
        assertEquals(firstBefore, secondAfter, "After submitting the ordering list doesn't preserve the chosen order.");
        submitAndCheckElementsOrderPersists();
        firstAfter = orderingList.advanced().getItemsElements().get(0).getText();
        secondAfter = orderingList.advanced().getItemsElements().get(1).getText();
        assertEquals(firstAfter, thirdBefore, "After submitting the ordering list doesn't preserve the chosen order.");
        assertEquals(firstBefore, secondAfter, "After submitting the ordering list doesn't preserve the chosen order.");
    }
}
