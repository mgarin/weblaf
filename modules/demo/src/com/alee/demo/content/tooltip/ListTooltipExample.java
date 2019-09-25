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
import com.alee.demo.content.SampleData;
import com.alee.laf.list.*;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class ListTooltipExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "listtooltip";
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
                new SwingTooltips ( FeatureState.release, StyleId.tooltip ),
                new CustomTooltips ( FeatureState.release, StyleId.customtooltip )
        );
    }

    /**
     * Swing list tooltips.
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
            super ( ListTooltipExample.this, "swing", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JList list = new JList ( SampleData.createListModel () );
            list.setVisibleRowCount ( list.getModel ().getSize () );
            list.setCellRenderer ( new WebListCellRenderer ()
            {
                @Override
                protected void updateView ( final ListCellParameters parameters )
                {
                    super.updateView ( parameters );
                    setToolTipText ( textForValue ( parameters ) );
                }
            } );
            return CollectionUtils.asList ( new JScrollPane ( list ) );
        }
    }

    /**
     * Custom list tooltips.
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
            super ( ListTooltipExample.this, "custom", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JList list = new JList ( SampleData.createListModel () );
            list.setVisibleRowCount ( list.getModel ().getSize () );
            list.putClientProperty ( WebList.TOOLTIP_PROVIDER_PROPERTY, new ListToolTipProvider<SampleData.ListItem> ()
            {
                @Nullable
                @Override
                protected String getToolTipText ( @NotNull final JList list,
                                                  @NotNull final ListCellArea<SampleData.ListItem, JList> area )
                {
                    final SampleData.ListItem value = area.getValue ( list );
                    return LM.get (
                            getPreviewLanguageKey ( "cell" ),
                            area.index (),
                            value != null ? value.getText ( new ListCellParameters<SampleData.ListItem, JList> ( list, area ) ) : null
                    );
                }
            } );
            return CollectionUtils.asList ( new JScrollPane ( list ) );
        }
    }
}