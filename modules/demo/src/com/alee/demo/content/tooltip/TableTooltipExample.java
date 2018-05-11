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

import com.alee.demo.api.example.*;
import com.alee.demo.content.SampleData;
import com.alee.laf.table.TableCellParameters;
import com.alee.laf.table.TableToolTipProvider;
import com.alee.laf.table.WebTable;
import com.alee.laf.table.renderers.WebTableCellRenderer;
import com.alee.managers.style.StyleId;
import com.alee.managers.tooltip.ComponentArea;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class TableTooltipExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "tabletooltip";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "customtooltip";
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
                new SwingTooltips ( FeatureState.release, StyleId.tooltip ),
                new CustomTooltips ( FeatureState.release, StyleId.customtooltip )
        );
    }

    /**
     * Swing table tooltips.
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
            super ( TableTooltipExample.this, "swing", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTable table = new JTable ( SampleData.createShortTableModel ( false ) );
            table.setDefaultRenderer ( String.class, new WebTableCellRenderer ()
            {
                @Override
                protected void updateView ( final TableCellParameters parameters )
                {
                    super.updateView ( parameters );
                    setToolTipText ( textForValue ( parameters ) );
                }
            } );
            return CollectionUtils.asList ( table );
        }
    }

    /**
     * Custom table tooltips.
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
            super ( TableTooltipExample.this, "custom", featureState, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTable table = new WebTable ( SampleData.createShortTableModel ( false ) );
            table.setToolTipProvider ( new TableToolTipProvider ()
            {
                @Override
                protected String getToolTipText ( final JComponent component, final Object value, final ComponentArea area )
                {
                    return String.valueOf ( value );
                }
            } );
            return CollectionUtils.asList ( table );
        }
    }
}