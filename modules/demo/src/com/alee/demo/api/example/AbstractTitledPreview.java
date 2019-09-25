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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.HorizontalFlowLayout;
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
    @Nullable
    protected PreviewPanel previewPanel;

    /**
     * Preview information.
     */
    @Nullable
    protected JComponent previewInfo;

    /**
     * Preview content elements.
     */
    @Nullable
    protected List<? extends JComponent> previewContent;

    /**
     * Constructs new {@link AbstractTitledPreview}.
     *
     * @param example example this preview belongs to
     * @param id      preview ID
     * @param state   feature state
     */
    public AbstractTitledPreview ( @NotNull final Example example, @NotNull final String id, @NotNull final FeatureState state )
    {
        super ( example, id, state );
    }

    @NotNull
    @Override
    protected JComponent createPreview ( @NotNull final List<Preview> previews, final int index )
    {
        final HorizontalFlowLayout layout = new HorizontalFlowLayout ( 0, true );
        previewPanel = new PreviewPanel ( previews.get ( index ).getFeatureState (), layout );

        previewInfo = createPreviewInfo ();
        previewPanel.add ( previewInfo );

        previewPanel.add ( createSeparator () );

        previewPanel.add ( createPreviewContent () );

        return previewPanel;
    }

    @Nullable
    @Override
    public JComponent getEqualizableWidthComponent ()
    {
        return previewInfo;
    }

    /**
     * Returns preview component information.
     *
     * @return preview component information
     */
    @NotNull
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
    @NotNull
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
    @NotNull
    protected JComponent createPreviewContent ()
    {
        final StyleId styleId = DemoStyles.previewContent.at ( previewPanel );
        final WebPanel contentPanel = new WebPanel ( styleId, createPreviewLayout () );
        contentPanel.add ( getPreviewElements () );
        return contentPanel;
    }

    /**
     * Returns newly created previewed elements container layout.
     * todo Use a non-stretching (vertically) layout
     *
     * @return newly created previewed elements container layout
     */
    @NotNull
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
    @NotNull
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
    @NotNull
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