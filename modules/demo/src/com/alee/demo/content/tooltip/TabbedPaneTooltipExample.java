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

package com.alee.demo.content.tooltip;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.demo.api.example.*;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.tabbedpane.TabbedPaneTabArea;
import com.alee.laf.tabbedpane.TabbedPaneToolTipProvider;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.icon.Icons;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.ArrayUtils;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class TabbedPaneTooltipExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "tabbedpanetooltip";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "customtooltip";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new SwingTooltips ( FeatureState.updated, StyleId.tooltip ),
                new CustomTooltips ( FeatureState.release, StyleId.customtooltip )
        );
    }

    /**
     * Swing tabbed pane tooltips.
     */
    protected class SwingTooltips extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public SwingTooltips ( final FeatureState featureState, final StyleId styleId )
        {
            super ( TabbedPaneTooltipExample.this, "swing", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTabbedPane tabbedPane = createTabbedPane ( JTabbedPane.SCROLL_TAB_LAYOUT, JTabbedPane.TOP );
            for ( int tab = 0; tab < tabbedPane.getTabCount (); tab++ )
            {
                tabbedPane.setToolTipTextAt ( tab, LM.get (
                        getExampleLanguageKey ( "data.tooltip" ),
                        tab,
                        LM.get ( tabbedPane.getTitleAt ( tab ) )
                ) );
            }
            return CollectionUtils.asList ( tabbedPane );
        }
    }

    /**
     * Custom tabbed pane tooltips.
     */
    protected class CustomTooltips extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public CustomTooltips ( final FeatureState featureState, final StyleId styleId )
        {
            super ( TabbedPaneTooltipExample.this, "custom", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTabbedPane tabbedPane = createTabbedPane ( JTabbedPane.SCROLL_TAB_LAYOUT, JTabbedPane.TOP );
            tabbedPane.putClientProperty ( WebTabbedPane.TOOLTIP_PROVIDER_PROPERTY, new TabbedPaneToolTipProvider<Component> ()
            {
                @Nullable
                @Override
                protected String getToolTipText ( @NotNull final JTabbedPane tabbedPane,
                                                  @NotNull final TabbedPaneTabArea<Component, JTabbedPane> area )
                {
                    return LM.get (
                            getExampleLanguageKey ( "data.tooltip" ),
                            area.tab (),
                            LM.get ( tabbedPane.getTitleAt ( area.tab () ) )
                    );
                }
            } );
            return CollectionUtils.asList ( tabbedPane );
        }
    }


    /**
     * Returns single {@link JTabbedPane} instance with specified layout policy.
     *
     * @param layoutPolicy layout policy
     * @param tabPlacement tab placement
     * @return single {@link JTabbedPane} instance with specified layout policy
     */
    @NotNull
    protected JTabbedPane createTabbedPane ( final int layoutPolicy, final int tabPlacement )
    {
        final JTabbedPane tabbedPane = new JTabbedPane ( tabPlacement, layoutPolicy );
        for ( int i = 0; i < 8; i++ )
        {
            final String key = getExampleLanguageKey ( "data.tab" + i );
            final Icon icon = ArrayUtils.roundRobin ( i, Icons.leaf, Icons.magnifier, Icons.computer, Icons.globe );
            tabbedPane.addTab ( key, icon, createTabContent () );
        }
        tabbedPane.setPreferredSize ( new Dimension (
                350,
                tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM ? 250 : 150
        ) );
        return tabbedPane;
    }

    /**
     * Returns single tab content {@link JComponent}.
     *
     * @return single tab content {@link JComponent}
     */
    @NotNull
    protected JComponent createTabContent ()
    {
        final WebStyledLabel content = new WebStyledLabel (
                DemoStyles.placeholderLabel,
                getExampleLanguageKey ( "data.content" ),
                WebLabel.CENTER
        );
        content.setFocusable ( true );
        content.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                content.requestFocusInWindow ();
            }
        } );
        return content;
    }
}