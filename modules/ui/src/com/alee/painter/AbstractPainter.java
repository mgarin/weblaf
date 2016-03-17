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

package com.alee.painter;

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.WebBorder;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.border.Border;
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
 * @see com.alee.painter.Painter
 */

public abstract class AbstractPainter<E extends JComponent, U extends ComponentUI> implements Painter<E, U>, BorderMethods
{
    /**
     * Painter listeners.
     */
    protected transient final List<PainterListener> listeners = new ArrayList<PainterListener> ( 1 );

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

        // Updating orientation
        updateOrientation ();
        saveOrientation ();

        // Updating border
        updateBorder ();

        // Installing listeners
        installPropertyChangeListener ();
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Uninstalling listeners
        uninstallPropertyChangeListener ();

        // Cleaning up references
        this.component = null;
        this.ui = null;
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
     * Returns whether or not this painter is allowed to update component settings and visual state.
     * By default it is determined by the painter type, for example any SectionPainter should avoid updating settings.
     *
     * @return true if this painter is allowed to update component settings and visual state, false otherwise
     */
    protected boolean isSettingsUpdateAllowed ()
    {
        return !isSectionPainter ();
    }

    /**
     * Returns whether or not this is a section painter.
     * Some internal behaviors might vary depending on what this method returns.
     *
     * @return true if this is a section painter, false otherwise
     */
    protected boolean isSectionPainter ()
    {
        return this instanceof SectionPainter;
    }

    /**
     * Installs listener that will inform about component property changes.
     */
    protected void installPropertyChangeListener ()
    {
        // Property change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                AbstractPainter.this.propertyChange ( evt.getPropertyName (), evt.getOldValue (), evt.getNewValue () );
            }
        };
        component.addPropertyChangeListener ( propertyChangeListener );
    }

    /**
     * Uninstalls listener that is informing about component property changes.
     */
    protected void uninstallPropertyChangeListener ()
    {
        component.removePropertyChangeListener ( propertyChangeListener );
        propertyChangeListener = null;
    }

    /**
     * Performs various updates on property changes.
     *
     * @param property modified property
     * @param oldValue old property value
     * @param newValue new property value
     */
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Forcing orientation visual updates
        if ( CompareUtils.equals ( property, WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY ) )
        {
            orientationChange ();
        }

        // Tracking component border changes
        if ( CompareUtils.equals ( property, WebLookAndFeel.BORDER_PROPERTY ) )
        {
            borderChange ( ( Border ) newValue );
        }
    }

    /**
     * Performs various updates on orientation change.
     */
    protected void orientationChange ()
    {
        // Saving new orientation
        saveOrientation ();

        // Updating only if allowed
        if ( isSettingsUpdateAllowed () )
        {
            // Updating component view
            // Revalidate includes border update so we don't need to call it separately
            revalidate ();
            repaint ();
        }
    }

    /**
     * Performs various border-related operations.
     *
     * @param border new border
     */
    protected void borderChange ( final Border border )
    {
        // First of all checking that it is not a UI resource
        // If it is not that means new component border was set from outside
        // We might want to keep that border and avoid automated WebLaF border to be set in future until old border is removed
        if ( !SwingUtils.isUIResource ( border ) )
        {
            SwingUtils.setHonorUserBorders ( component, true );
        }
    }

    /**
     * Saves current component orientation state.
     */
    protected void saveOrientation ()
    {
        ltr = component.getComponentOrientation ().isLeftToRight ();
    }

    /**
     * Updates component orientation based on global orientation.
     */
    public void updateOrientation ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            SwingUtils.setOrientation ( component );
        }
    }

    /**
     * Updates component with complete border.
     * This border takes painter borders and component margin and padding into account.
     */
    @Override
    public void updateBorder ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            final Insets border = getCompleteBorder ();
            if ( border != null )
            {
                final Border old = component.getBorder ();
                if ( !( old instanceof WebBorder ) || !CompareUtils.equals ( ( ( WebBorder ) old ).getBorderInsets (), border ) )
                {
                    component.setBorder ( new WebBorder ( border ) );
                }
            }
        }
    }

    /**
     * Updates component border according to component's margin and padding and painter's borders.
     *
     * @return complete painter border
     */
    public Insets getCompleteBorder ()
    {
        if ( component != null && !SwingUtils.isPreserveBorders ( component ) )
        {
            final Insets border = i ( 0, 0, 0, 0 );

            // Calculating margin borders
            if ( !isSectionPainter () )
            {
                final Insets margin = LafUtils.getMargin ( component );
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
            if ( !isSectionPainter () )
            {
                final Insets padding = LafUtils.getPadding ( component );
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

    /**
     * Should be called when painter visual representation changes.
     */
    public void repaint ()
    {
        if ( isSettingsUpdateAllowed () && component != null && component.isShowing () )
        {
            for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
            {
                listener.repaint ();
            }
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
        if ( isSettingsUpdateAllowed () && component.isShowing () )
        {
            for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
            {
                listener.repaint ( x, y, width, height );
            }
        }
    }

    /**
     * Should be called when painter size or border changes.
     */
    public void revalidate ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            // Updating border to have correct size
            updateBorder ();

            // Revalidating layout
            for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
            {
                listener.revalidate ();
            }
        }
    }

    /**
     * Should be called when painter opacity changes.
     */
    public void updateOpacity ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
            {
                listener.updateOpacity ();
            }
        }
    }

    /**
     * Should be called when painter size, border and visual representation changes.
     * Calls both revalidate and update listener methods.
     */
    public void updateAll ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            updateBorder ();
            for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
            {
                listener.updateOpacity ();
                listener.revalidate ();
                if ( component.isShowing () )
                {
                    listener.repaint ();
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Insets b = getCompleteBorder ();
        return b != null ? new Dimension ( b.left + b.right, b.top + b.bottom ) : new Dimension ();
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
     * Returns point for the specified coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return point for the specified coordinates
     */
    protected Point p ( final int x, final int y )
    {
        return new Point ( x, y );
    }

    /**
     * Returns insets with the specified settings.
     *
     * @param top    the inset from the top
     * @param left   the inset from the left
     * @param bottom the inset from the bottom
     * @param right  the inset from the right
     * @return insets with the specified settings
     */
    protected Insets i ( final int top, final int left, final int bottom, final int right )
    {
        return new Insets ( top, left, bottom, right );
    }

    /**
     * Returns combined insets with the specified settings.
     *
     * @param insets base insets
     * @param top    the inset from the top
     * @param left   the inset from the left
     * @param bottom the inset from the bottom
     * @param right  the inset from the right
     * @return combined insets with the specified settings
     */
    protected Insets i ( final Insets insets, final int top, final int left, final int bottom, final int right )
    {
        return insets != null ? new Insets ( insets.top + top, insets.left + left, insets.bottom + bottom, insets.right + right ) :
                new Insets ( top, left, bottom, right );
    }

    /**
     * Returns combined insets.
     *
     * @param i1 first insets
     * @param i2 second insets
     * @return combined insets
     */
    protected Insets i ( final Insets i1, final Insets i2 )
    {
        return i1 != null && i2 != null ? new Insets ( i1.top + i2.top, i1.left + i2.left, i1.bottom + i2.bottom, i1.right + i2.right ) :
                i1 != null ? i1 : i2;
    }

    /**
     * Returns bounds reduced by specified insets.
     *
     * @param bounds bounds to reduce
     * @param limit  limiting insets
     * @return bounds reduced by specified insets
     */
    protected Rectangle b ( final Rectangle bounds, final Insets limit )
    {
        return SwingUtils.shrink ( bounds, limit );
    }
}