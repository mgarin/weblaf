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

package com.alee.demo.api.example;

import com.alee.demo.skin.DemoStyles;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.TableLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.style.StyleId;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Each {@link AbstractTitledPreview} has a custom title.
 *
 * @author Mikle Garin
 */
public abstract class AbstractTitledPreview extends AbstractPreview
{
    /**
     * Preview container.
     */
    protected PreviewPanel previewPanel;

    /**
     * Preview information.
     */
    protected JComponent previewInfo;

    /**
     * Preview content elements.
     */
    protected List<? extends JComponent> previewContent;

    /**
     * Constructs new {@link AbstractTitledPreview}.
     *
     * @param example example this preview belongs to
     * @param id      preview ID
     * @param state   feature state
     */
    public AbstractTitledPreview ( final Example example, final String id, final FeatureState state )
    {
        super ( example, id, state );
    }

    @Override
    protected JComponent createPreview ( final List<Preview> previews, final int index )
    {
        final Preview preview = previews.get ( index );

        final double[] columns = new double[]{ TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL };
        final double[] rows = new double[]{ TableLayout.PREFERRED };
        final TableLayout layout = new TableLayout ( columns, rows, 0, 0 );
        previewPanel = new PreviewPanel ( preview.getFeatureState (), layout );
        previewPanel.add ( createPreviewContent (), "2,0" );
        previewPanel.add ( createSeparator (), "1,0" );
        previewPanel.add ( getPreviewInfo (), "0,0" );
        return previewPanel;
    }

    @Override
    public JComponent getEqualizableWidthComponent ()
    {
        return getPreviewInfo ();
    }

    /**
     * Returns cached preview component information.
     *
     * @return cached preview component information
     */
    protected JComponent getPreviewInfo ()
    {
        if ( previewInfo == null )
        {
            previewInfo = createPreviewInfo ();
        }
        return previewInfo;
    }

    /**
     * Returns preview component information.
     *
     * @return preview component information
     */
    protected JComponent createPreviewInfo ()
    {
        final StyleId styleId = DemoStyles.previewTitleLabel.at ( previewPanel );
        return new WebStyledLabel ( styleId, getTitle () ).setBoldFont ();
    }

    /**
     * Returns newly created information and content separator.
     *
     * @return newly created information and content separator
     */
    protected WebSeparator createSeparator ()
    {
        final StyleId styleId = DemoStyles.previewSeparator.at ( previewPanel );
        return new WebSeparator ( styleId );
    }

    /**
     * Returns newly created previewed elements container.
     *
     * @return newly created previewed elements container
     */
    protected JComponent createPreviewContent ()
    {
        final StyleId styleId = DemoStyles.previewContent.at ( previewPanel );
        final WebPanel contentPanel = new WebPanel ( styleId, createPreviewLayout () );
        contentPanel.add ( getPreviewElements () );
        return contentPanel;
    }

    /**
     * Returns newly created previewed elements container layout.
     *
     * @return newly created previewed elements container layout
     */
    protected LayoutManager createPreviewLayout ()
    {
        return new HorizontalFlowLayout ( 8, false );
    }

    /**
     * Returns cached preview content elements.
     * This can be anything provided by the specific preview.
     *
     * @return cached preview content elements
     */
    protected List<? extends JComponent> getPreviewElements ()
    {
        if ( previewContent == null )
        {
            previewContent = createPreviewElements ();
        }
        return previewContent;
    }

    /**
     * Returns preview content component.
     * This can be anything provided by the example.
     *
     * @return preview content component
     */
    protected abstract List<? extends JComponent> createPreviewElements ();

    @Override
    public void applyEnabled ( final boolean enabled )
    {
        for ( final JComponent component : getPreviewElements () )
        {
            SwingUtils.setEnabledRecursively ( component, enabled );
        }
    }
}