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

package com.alee.extended.tab;

import com.alee.laf.splitpane.WebSplitPane;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.Customizer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Data for single split pane within document pane.
 * It basically contains split pane and links to two other elements contained within split pane.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see com.alee.extended.tab.WebDocumentPane
 */
public final class SplitData<T extends DocumentData> implements StructureData<T>
{
    /**
     * Actual split component.
     */
    protected final WebSplitPane splitPane;

    /**
     * Split orientation.
     */
    protected int orientation;

    /**
     * First split element.
     */
    protected StructureData first;

    /**
     * Last split element.
     */
    protected StructureData last;

    /**
     * Constructs new SplitData.
     *
     * @param documentPane parent WebDocumentPane
     * @param orientation  split orientation
     * @param first        first split element
     * @param last         last split element
     */
    public SplitData ( final WebDocumentPane<T> documentPane, final int orientation, final StructureData first, final StructureData last )
    {
        super ();
        this.orientation = orientation;
        this.first = first;
        this.last = last;

        // Creating split pane
        this.splitPane = createSplit ( orientation, first, last );

        // Customizing split pane
        updateSplitPaneCustomizer ( documentPane );
    }

    /**
     * Returns new split component.
     *
     * @param orientation split orientation
     * @param first       first split element
     * @param last        last split element   @return new split component
     * @return new split component
     */
    protected WebSplitPane createSplit ( final int orientation, final StructureData first, final StructureData last )
    {
        // todo Appropriate split style
        final WebSplitPane splitPane = new WebSplitPane ( orientation, first.getComponent (), last.getComponent () );
        splitPane.putClientProperty ( WebDocumentPane.DATA_KEY, this );
        splitPane.setContinuousLayout ( true );
        // splitPane.setDrawDividerBorder ( true );
        splitPane.setDividerSize ( 8 );
        splitPane.setResizeWeight ( 0.5 );
        splitPane.addPropertyChangeListener ( JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                CoreSwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        getDocumentPane ().fireDividerLocationChanged ( SplitData.this );
                    }
                } );
            }
        } );
        return splitPane;
    }

    /**
     * Updates split customizer.
     *
     * @param documentPane parent WebDocumentPane
     */
    protected void updateSplitPaneCustomizer ( final WebDocumentPane<T> documentPane )
    {
        final Customizer<WebSplitPane> customizer = documentPane.getSplitPaneCustomizer ();
        if ( customizer != null )
        {
            customizer.customize ( splitPane );
        }
    }

    @Override
    public Component getComponent ()
    {
        return getSplitPane ();
    }

    @Override
    public PaneData<T> findClosestPane ()
    {
        return getFirst ().findClosestPane ();
    }

    @Override
    public DocumentPaneState getDocumentPaneState ()
    {
        return new DocumentPaneState ( this );
    }

    /**
     * Returns parent WebDocumentPane.
     *
     * @return parent WebDocumentPane
     */
    public WebDocumentPane getDocumentPane ()
    {
        // todo Replace with saved WebDocumentPane reference
        return SwingUtils.getFirstParent ( splitPane, WebDocumentPane.class );
    }

    /**
     * Returns actual split component.
     *
     * @return actual split component
     */
    public WebSplitPane getSplitPane ()
    {
        return splitPane;
    }

    /**
     * Returns split orientation.
     *
     * @return split orientation
     */
    public int getOrientation ()
    {
        return orientation;
    }

    /**
     * Sets split orientation.
     *
     * @param orientation new split orientation
     */
    public void setOrientation ( final int orientation )
    {
        this.orientation = orientation;
        splitPane.setOrientation ( orientation );

        // Firing orientation change event
        getDocumentPane ().fireOrientationChanged ( this );
    }

    /**
     * Changes split orientation.
     */
    public void changeOrientation ()
    {
        setOrientation ( orientation == WebSplitPane.HORIZONTAL_SPLIT ? WebSplitPane.VERTICAL_SPLIT : WebSplitPane.HORIZONTAL_SPLIT );
    }

    /**
     * Swaps side components.
     */
    public void swapSides ()
    {
        // Remembering components and split location
        final StructureData first = this.last;
        final StructureData last = this.first;
        final double location = getDividerLocation ();

        // Clearing components first to avoid exceptions
        splitPane.setLeftComponent ( null );
        splitPane.setRightComponent ( null );

        // Changing component sides
        this.first = first;
        this.last = last;
        splitPane.setLeftComponent ( first.getComponent () );
        splitPane.setRightComponent ( last.getComponent () );
        splitPane.revalidate ();
        splitPane.repaint ();

        // Restoring opposite divider location
        setDividerLocation ( 1.0 - location );

        // Firing sides swap event
        getDocumentPane ().fireSidesSwapped ( this );
    }

    /**
     * Returns proportional split divider location.
     *
     * @return proportional split divider location
     */
    public double getDividerLocation ()
    {
        return splitPane.getProportionalDividerLocation ();
    }

    /**
     * Sets proportional split divider location.
     *
     * @param location new proportional split divider location
     */
    public void setDividerLocation ( final double location )
    {
        splitPane.setDividerLocation ( Math.max ( 0.0, Math.min ( location, 1.0 ) ) );
    }

    /**
     * Returns first split element.
     *
     * @return first split element
     */
    public StructureData getFirst ()
    {
        return first;
    }

    /**
     * Sets first split element.
     *
     * @param first new first split element
     */
    public void setFirst ( final StructureData first )
    {
        this.first = first;
        splitPane.setLeftComponent ( first.getComponent () );
    }

    /**
     * Returns last split element.
     *
     * @return last split element
     */
    public StructureData getLast ()
    {
        return last;
    }

    /**
     * Sets last split element.
     *
     * @param last new last split element
     */
    public void setLast ( final StructureData last )
    {
        this.last = last;
        splitPane.setRightComponent ( last.getComponent () );
    }

    /**
     * Replaces specified element with new one.
     *
     * @param element     element to replace
     * @param replacement element replacement
     */
    public void replace ( final StructureData element, final StructureData replacement )
    {
        if ( first == element )
        {
            first = replacement;
            splitPane.setLeftComponent ( replacement.getComponent () );
        }
        if ( last == element )
        {
            last = replacement;
            splitPane.setRightComponent ( replacement.getComponent () );
        }
    }
}