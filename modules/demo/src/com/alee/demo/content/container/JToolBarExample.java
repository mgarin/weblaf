/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.demo.content.container;

import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.content.SampleData;
import com.alee.demo.skin.DemoIcons;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.grouping.GroupPane;
import com.alee.laf.label.WebLabel;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JToolBarExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "jtoolbar";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "toolbar";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Tool Bars", "toolbar" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new ToolBarPreview ( "basic", FeatureState.updated, StyleId.toolbar ),
                new ToolBarPreview ( "undecorated", FeatureState.updated, StyleId.toolbarUndecorated ),
                new ToolBarPreview ( "attached-north", FeatureState.updated, StyleId.toolbarAttachedNorth ),
                new ToolBarPreview ( "attached-south", FeatureState.updated, StyleId.toolbarAttachedSouth )
        );
    }

    /**
     * Toolbar preview.
     */
    protected class ToolBarPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public ToolBarPreview ( final String id, final FeatureState featureState, final StyleId styleId )
        {
            super ( JToolBarExample.this, id, featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JToolBar toolBar = new JToolBar ( SwingConstants.HORIZONTAL );
            toolBar.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            toolBar.add ( new WebButton ( getExampleLanguageKey ( "button1" ) ) );
            toolBar.add ( new WebButton ( getExampleLanguageKey ( "button2" ) ) );
            toolBar.add ( new WebButton ( getExampleLanguageKey ( "button3" ) ) );

            toolBar.addSeparator ( new Dimension ( 10, 10 ) );

            final WebToggleButton toggle1 = new WebToggleButton ( DemoIcons.win16, SystemUtils.isWindows () );
            final WebToggleButton toggle2 = new WebToggleButton ( DemoIcons.mac16, SystemUtils.isMac () );
            final WebToggleButton toggle3 = new WebToggleButton ( DemoIcons.unix16, SystemUtils.isUnix () );
            final WebToggleButton toggle4 = new WebToggleButton ( DemoIcons.solaris16, SystemUtils.isSolaris () );
            toolBar.add ( new GroupPane ( toggle1, toggle2, toggle3, toggle4 ) );

            toolBar.addSeparator ( new Dimension ( 10, 10 ) );

            toolBar.add ( new WebLabel ( getExampleLanguageKey ( "label1" ) ) );
            toolBar.add ( new WebComboBox ( SampleData.createComboBoxModel () ) );

            return CollectionUtils.asList ( toolBar );
        }
    }
}