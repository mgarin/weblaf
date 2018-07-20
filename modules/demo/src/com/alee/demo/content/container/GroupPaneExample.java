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
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.grouping.GroupPane;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.spinner.WebSpinner;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class GroupPaneExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "grouppane";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new ButtonsGrouping ( FeatureState.release, StyleId.grouppane ),
                new MixedGrouping ( FeatureState.release, StyleId.grouppane ),
                new GridGrouping ( FeatureState.release, StyleId.grouppane )
        );
    }

    /**
     * Mixed grouping preview.
     */
    protected class MixedGrouping extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param id           preview style ID
         */
        public MixedGrouping ( final FeatureState featureState, final StyleId id )
        {
            super ( GroupPaneExample.this, "mixed", featureState, id );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebButton e1 = new WebButton ( "First" );
            final WebComboBox e2 = new WebComboBox ( new String[]{ "First", "Second", "Third" }, 1 );
            final WebTextField e3 = new WebTextField ( "Third" );
            final WebButton e4 = new WebButton ( "Last" );
            final GroupPane groupPane = new GroupPane ( getStyleId (), e1, e2, e3, e4 );
            SwingUtils.equalizeComponentsWidth ( groupPane.getComponents () );
            return CollectionUtils.asList ( groupPane );
        }
    }

    /**
     * Grid grouping preview.
     */
    protected class GridGrouping extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param id           preview style ID
         */
        public GridGrouping ( final FeatureState featureState, final StyleId id )
        {
            super ( GroupPaneExample.this, "grid", featureState, id );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            // First row
            final WebComboBox e1 = new WebComboBox ( new String[]{ "First", "Second", "Third" } );
            final WebButton e2 = new WebButton ( "Second" );
            final WebTextField e3 = new WebTextField ( "Third" );

            // Second row
            final WebPanel e4 = new WebPanel ( StyleId.panelDecorated, new WebLabel ( "First", WebLabel.CENTER ) );
            final WebTextField e5 = new WebTextField ( "Second" );
            final WebButton e6 = new WebButton ( "Third" );

            // Third row
            final WebSpinner e7 = new WebSpinner ( new SpinnerNumberModel ( 100, 0, 100, 1 ) );
            final WebButton e8 = new WebButton ( "Second" );
            final WebPasswordField e9 = new WebPasswordField ( "Third" );

            final GroupPane groupPane = new GroupPane ( getStyleId (), 3, 3, e1, e2, e3, e4, e5, e6, e7, e8, e9 );
            SwingUtils.equalizeComponentsWidth ( groupPane.getComponents () );
            return CollectionUtils.asList ( groupPane );
        }
    }

    /**
     * Buttons grouping preview.
     */
    protected class ButtonsGrouping extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param id           preview style ID
         */
        public ButtonsGrouping ( final FeatureState featureState, final StyleId id )
        {
            super ( GroupPaneExample.this, "buttons", featureState, id );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebToggleButton b1 = new WebToggleButton ( "First", true );
            final WebToggleButton b2 = new WebToggleButton ( "Second" );
            final WebToggleButton b3 = new WebToggleButton ( "Third" );
            final WebToggleButton b4 = new WebToggleButton ( "Last" );
            final GroupPane groupPane = new GroupPane ( getStyleId (), b1, b2, b3, b4 );
            SwingUtils.equalizeComponentsWidth ( groupPane.getComponents () );
            return CollectionUtils.asList ( groupPane );
        }
    }
}