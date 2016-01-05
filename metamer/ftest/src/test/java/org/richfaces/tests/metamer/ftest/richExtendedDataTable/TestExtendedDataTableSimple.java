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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSimpleTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SimpleEDT;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SimpleEDTRow;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.model.Capital;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class TestExtendedDataTableSimple extends DataTableSimpleTest {

    private final Attributes<ExtendedDataTableAttributes> attributes = getAttributes();
    final Action selectFirstRowAction = new Action() {
        @Override
        public void perform() {
            table.getFirstRow().getRootElement().click();
        }
    };

    @FindBy(css = "div.rf-edt[id$=richEDT]")
    private SimpleEDT table;
    @FindBy(css = "div.rf-edt[id$=richEDT]")
    private WebElement tableRoot;

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/simple.xhtml";
    }

    @Override
    protected SimpleEDT getTable() {
        return table;
    }

    @Test
    @Templates(exclude = "uirepeat")
    @CoversAttributes("clientRows")
    public void testClientRows() {
        // setup
        attributes.set(ExtendedDataTableAttributes.rows, 30);
        List<Capital> capitals = CAPITALS.subList(0, 30);
        final int clientRows = 18;
        attributes.set(ExtendedDataTableAttributes.clientRows, clientRows);
        By loadedRows = ByJQuery.selector("tbody[id$='richEDT:tbn']>tr");
        // check number of loaded rows equals to number of @clientRows
        assertEquals(driver.findElements(loadedRows).size(), clientRows);

        // check the last loaded row
        SimpleEDTRow row = table.getLastRow();
        String text = row.getStateColumn().getText();
        assertEquals(text, capitals.get(17).getState());

        // browse to last loaded row, this will load 18 last rows
        Graphene.guardAjax(row.getRootElement()).click();
        // check the last loaded row
        row = table.getLastRow();
        text = row.getStateColumn().getText();
        assertEquals(text, capitals.get(29).getState());
        // check number of loaded rows equals to number of @clientRows
        assertEquals(driver.findElements(loadedRows).size(), clientRows);

        // check the first loaded row
        row = table.getFirstRow();
        text = row.getStateColumn().getText();
        int firstLoadedRowIndex = 30 - clientRows;
        assertEquals(text, capitals.get(firstLoadedRowIndex).getState());
        // check number of loaded rows equals to number of @clientRows
        assertEquals(driver.findElements(loadedRows).size(), clientRows);

        // repeatedly scroll to the first loaded element, wait for new elements to load and check
        while ((firstLoadedRowIndex -= 2) >= 0) {
            // move to first loaded row, this will load few more items before the previous first item
            Graphene.guardAjax(row.getRootElement()).click();
            // check the first loaded row
            row = table.getFirstRow();
            text = row.getStateColumn().getText();
            assertEquals(text, capitals.get(firstLoadedRowIndex).getState());
            // check number of loaded rows equals to number of @clientRows
            assertEquals(driver.findElements(loadedRows).size(), clientRows);
        }
    }

    @Skip
    @Test
    @Templates("uirepeat")
    @IssueTracking("https://issues.jboss.org/browse/RF-14216")
    @CoversAttributes("clientRows")
    public void testClientRowsInUIRepeat() {
        testClientRows();
    }

    @Test
    @CoversAttributes("first")
    @UseWithField(field = "first", valuesFrom = FROM_FIELD, value = "COUNTS")
    public void testFirst() {
        super.testFirst();
    }

    @Test
    @CoversAttributes("noDataLabel")
    @Templates("plain")
    public void testNoDataLabel() {
        super.testNoDataLabel();
    }

    @Test
    @CoversAttributes("onbeforeselectionchange")
    @Templates("plain")
    public void testOnbeforeselectionchange() {
        testFireEvent("onbeforeselectionchange", selectFirstRowAction);
    }

    @Test
    @CoversAttributes("onrowclick")
    @Templates("plain")
    public void testOnrowclick() {
        super.testOnrowclick();
    }

    @Test
    @CoversAttributes("onrowdblclick")
    @Templates("plain")
    public void testOnrowdblclick() {
        super.testOnrowdblclick();
    }

    @Test
    @CoversAttributes("onrowkeydown")
    @Templates("plain")
    public void testOnrowkeydown() {
        super.testOnrowkeydown();
    }

    @Test
    @CoversAttributes("onrowkeypress")
    @Templates("plain")
    public void testOnrowkeypress() {
        super.testOnrowkeypress();
    }

    @Test
    @CoversAttributes("onrowkeyup")
    @Templates("plain")
    public void testOnrowkeyup() {
        super.testOnrowkeyup();
    }

    @Test
    @CoversAttributes("onrowmousedown")
    @Templates("plain")
    public void testOnrowmousedown() {
        super.testOnrowmousedown();
    }

    @Test
    @CoversAttributes("onrowmousemove")
    @Templates("plain")
    public void testOnrowmousemove() {
        super.testOnrowmousemove();
    }

    @Test
    @CoversAttributes("onrowmouseout")
    @Templates("plain")
    public void testOnrowmouseout() {
        super.testOnrowmouseout();
    }

    @Test
    @CoversAttributes("onrowmouseover")
    @Templates("plain")
    public void testOnrowmouseover() {
        super.testOnrowmouseover();
    }

    @Test
    @CoversAttributes("onrowmouseup")
    @Templates("plain")
    public void testOnrowmouseup() {
        super.testOnrowmouseup();
    }

    @Test
    @CoversAttributes("onselectionchange")
    @Templates("plain")
    public void testOnselectionchange() {
        testFireEvent("onselectionchange", selectFirstRowAction);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates("plain")
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @CoversAttributes("rowClass")
    @Templates("plain")
    public void testRowClass() {
        super.testRowClass();
    }

    @Test
    @CoversAttributes("rows")
    @UseWithField(field = "rows", valuesFrom = FROM_FIELD, value = "COUNTS")
    public void testRows() {
        super.testRows();
    }

    @Test
    @CoversAttributes("style")
    @Templates("plain")
    public void testStyle() {
        testStyle(tableRoot);
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(tableRoot);
    }
}
