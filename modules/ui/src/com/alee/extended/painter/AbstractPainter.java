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

package com.alee.extended.painter;

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.managers.style.MarginSupport;
import com.alee.managers.style.PaddingSupport;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract painter provides a few additional useful features atop of the Painter interface.
 * Usually this class is extended by various painters instead of implementing Painter interface directly.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 * @see Painter
 */

public abstract class AbstractPainter<E extends JComponent, U extends ComponentUI> implements Painter<E, U>, BorderMethods
{
    /**
     * Painter listeners.
     */
    protected transient List<PainterListener> listeners = new ArrayList<PainterListener> ( 1 );

    /**
     * Listeners.
     */
    protected transient PropertyChangeListener propertyChangeListener;

    /**
     * Component reference.
     */
    protected transient E component;

    /**
     * Component UI reference.
     */
    protected transient U ui;

    /**
     * Whether or not painted component has LTR orientation.
     */
    protected transient boolean ltr;

    @Override
    public void install ( final E c, final U ui )
    {
        // Saving references
        this.component = c;
        this.ui = ui;

        // Default settings
        SwingUtils.setOrientation ( c );
        saveOrientation ();

        // Updating border
        updateBorder ();

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                orientationChange ();
            }
        };
        c.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        c.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );

        // Cleaning up references
        this.component = null;
    }

    @Override
    public Boolean isOpaque ()
    {
        return null;
    }

    @Override
    public Insets getBorders ()
    {
        return null;
    }

    /**
     * Performs various updates on orientation change.
     */
    protected void orientationChange ()
    {
        saveOrientation ();
        revalidate ();
        repaint ();
    }

    /**
     * Saves current component orientation state.
     */
    protected void saveOrientation ()
    {
        ltr = component.getComponentOrientation ().isLeftToRight ();
    }

    /**
     * Updates component with complete border.
     * This border takes painter borders and component margin and padding into account.
     */
    @Override
    public void updateBorder ()
    {
        final Insets border = getCompleteBorder ();
        if ( border != null )
        {
            component.setBorder ( LafUtils.createWebBorder ( border ) );
        }
    }

    /**
     * Updates component border according to component's margin and padding and painter's borders.
     *
     * @return complete painter border
     */
    public Insets getCompleteBorder ()
    {
        if ( component != null && ui != null && !SwingUtils.isPreserveBorders ( component ) )
        {
            final Insets border = new Insets ( 0, 0, 0, 0 );

            // Calculating margin borders
            if ( ui instanceof MarginSupport )
            {
                final Insets margin = ( ( MarginSupport ) ui ).getMargin ();
                if ( margin != null )
                {
                    border.top += margin.top;
                    border.left += ltr ? margin.left : margin.right;
                    border.bottom += margin.bottom;
                    border.right += ltr ? margin.right : margin.left;
                }
            }

            // Painter borders
            final Insets borders = getBorders ();
            if ( borders != null )
            {
                border.top += borders.top;
                border.left += ltr ? borders.left : borders.right;
                border.bottom += borders.bottom;
                border.right += ltr ? borders.right : borders.left;
            }

            // Calculating padding borders
            if ( ui instanceof PaddingSupport )
            {
                final Insets padding = ( ( PaddingSupport ) ui ).getPadding ();
                if ( padding != null )
                {
                    border.top += padding.top;
                    border.left += ltr ? padding.left : padding.right;
                    border.bottom += padding.bottom;
                    border.right += ltr ? padding.right : padding.left;
                }
            }

            // Return final border
            return border;
        }
        else
        {
            // Return {@code null} to prevent border updates
            return null;
        }
    }

    @Override
    public void addPainterListener ( final PainterListener listener )
    {
        listeners.add ( listener );
    }

    @Override
    public void removePainterListener ( final PainterListener listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Should be called when painter visual representation changes.
     */
    public void repaint ()
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.repaint ();
        }
    }

    /**
     * Should be called when part of painter visual representation changes.
     *
     * @param bounds part bounds
     */
    public void repaint ( final Rectangle bounds )
    {
        repaint ( bounds.x, bounds.y, bounds.width, bounds.height );
    }

    /**
     * Should be called when part of painter visual representation changes.
     *
     * @param x      part bounds X coordinate
     * @param y      part bounds Y coordinate
     * @param width  part bounds width
     * @param height part bounds height
     */
    public void repaint ( final int x, final int y, final int width, final int height )
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.repaint ( x, y, width, height );
        }
    }

    /**
     * Should be called when painter size or border changes.
     */
    public void revalidate ()
    {
        // Updating border to have correct size
        updateBorder ();

        // Revalidating layout
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.revalidate ();
        }
    }

    /**
     * Should be called when painter opacity changes.
     */
    public void updateOpacity ()
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.updateOpacity ();
        }
    }

    /**
     * Should be called when painter size, border and visual representation changes.
     * Calls both revalidate and update listener methods.
     */
    public void updateAll ()
    {
        updateBorder ();
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.updateOpacity ();
            listener.revalidate ();
            listener.repaint ();
        }
    }

    /**
     * Returns point for the specified coordinates.
     * Might be useful for points generation in various cases
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return point for the specified coordinates
     */
    protected Point p ( final int x, final int y )
    {
        return new Point ( x, y );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Insets borders = getCompleteBorder ();
        return new Dimension ( borders.left + borders.right, borders.top + borders.bottom );
    }
}