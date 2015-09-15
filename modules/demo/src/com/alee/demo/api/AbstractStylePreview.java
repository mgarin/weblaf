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

package com.alee.demo.api;

import com.alee.demo.skin.DemoStyles;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Each {@link com.alee.demo.api.AbstractStylePreview} is tied to specific skin style.
 *
 * @author Mikle Garin
 */

public abstract class AbstractStylePreview extends AbstractPreview
{
    /**
     * Icons.
     */
    private static final ImageIcon styleIdIcon = new ImageIcon ( AbstractStylePreview.class.getResource ( "icons/styleid.png" ) );

    /**
     * Preview style ID.
     */
    protected final StyleId id;

    /**
     * Constructs new style preview.
     *
     * @param id preview style ID
     */
    public AbstractStylePreview ( final StyleId id )
    {
        super ();
        this.id = id;
    }

    /**
     * Returns preview style ID.
     *
     * @return preview style ID
     */
    protected StyleId getStyleId ()
    {
        return id;
    }

    @Override
    protected JComponent createPreview ( final List<Preview> previews, final int index )
    {
        final WebPanel preview = new WebPanel ( new BorderLayout ( 0, 0 ) );

        // Creating preview information
        preview.add ( createPreviewInfo (), BorderLayout.WEST );

        // Creating preview
        final StyleId style = index % 2 == 0 ? DemoStyles.previewOdd : DemoStyles.previewEven;
        preview.add ( createPreviewContent ( style ), BorderLayout.CENTER );

        return preview;
    }

    @Override
    public JComponent getEqualizableWidthComponent ( final JComponent preview )
    {
        return ( JComponent ) preview.getComponent ( 0 );
    }

    /**
     * Returns preview component information.
     *
     * @return preview component information
     */
    protected JComponent createPreviewInfo ()
    {
        final WebPanel container = new WebPanel ( DemoStyles.previewInfoPanel );

        // Style ID information
        final WebStyledLabel styleId = new WebStyledLabel ( DemoStyles.styleIdLabel, styleIdIcon );
        styleId.setLanguage ( "demo.content.preview.styleid", getStyleId ().getCompleteId () );
        container.add ( styleId );

        return container;
    }

    /**
     * Returns preview content component.
     * This can be anything provided by the example.
     *
     * @param id style ID
     * @return preview content component
     */
    protected abstract JComponent createPreviewContent ( StyleId id );
}