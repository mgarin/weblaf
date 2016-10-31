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
import com.alee.demo.util.ExampleUtils;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikle Garin
 */

public abstract class AbstractPreviewExample extends AbstractExample
{
    /**
     * Previews cache.
     */
    protected List<Preview> previews;

    /**
     * Preview pane.
     */
    protected WebPanel examplesPane;

    @Override
    public final FeatureState getFeatureState ()
    {
        final List<Preview> previews = getPreviews ();
        final List<FeatureState> states = new ArrayList<FeatureState> ( previews.size () );
        for ( final Preview preview : previews )
        {
            states.add ( preview.getFeatureState () );
        }
        return ExampleUtils.getResultingState ( states );
    }

    @Override
    protected JComponent createContentImpl ()
    {
        return getPreviewContent ();
    }

    /**
     * Returns preview content.
     *
     * @return preview content
     */
    protected JComponent getPreviewContent ()
    {
        if ( examplesPane == null )
        {
            // Initializing examples pane
            examplesPane = new WebPanel ( DemoStyles.previewsPanel, createPreviewLayout () );

            // Creating preview components
            final List<Component> components = new ArrayList<Component> ();
            final List<Preview> previews = getPreviews ();
            for ( int i = 0; i < previews.size (); i++ )
            {
                // Preview
                final Preview preview = previews.get ( i );

                // Preview component
                final JComponent previewComponent = preview.getPreview ( previews, i );
                examplesPane.add ( previewComponent );

                // Colecting components to equalize
                CollectionUtils.addAllNonNull ( components, preview.getEqualizableWidthComponent () );
            }

            // Equalizing preview elements
            SwingUtils.equalizeComponentsWidth ( Arrays.asList ( AbstractButton.TEXT_CHANGED_PROPERTY ), components );
        }
        return examplesPane;
    }

    /**
     * Returns previous layout.
     *
     * @return previous layout
     */
    protected LayoutManager createPreviewLayout ()
    {
        return new VerticalFlowLayout ( 0, -32, true, false );
    }

    /**
     * Returns cached previews.
     *
     * @return cached previews
     */
    protected List<Preview> getPreviews ()
    {
        if ( previews == null )
        {
            previews = createPreviews ();
        }
        return previews;
    }

    /**
     * Returns all example previews.
     *
     * @return all example previews
     */
    protected abstract List<Preview> createPreviews ();
}