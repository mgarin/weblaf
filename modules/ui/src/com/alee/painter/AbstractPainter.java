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

import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.Bounds;
import com.alee.utils.*;
import com.alee.utils.laf.WebBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This abstract {@link Painter} implementation provides a few basic commonly used features.
 * You might want to extended this class instead of implementing {@link Painter} interface directly.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 * @author Alexandr Zernov
 * @see Painter
 */
public abstract class AbstractPainter<C extends JComponent, U extends ComponentUI> implements Painter<C, U>
{
    /**
     * Listeners.
     */
    protected transient PropertyChangeListener propertyChangeListener;

    /**
     * Whether or not this painter is installed onto some component.
     */
    protected transient boolean installed;

    /**
     * Component reference.
     */
    protected transient C component;

    /**
     * Component UI reference.
     */
    protected transient U ui;

    /**
     * Installed section painters.
     */
    protected transient Map<String, SectionPainter<C, U>> sectionPainters;

    /**
     * Whether or not painted component has LTR orientation.
     */
    protected transient boolean ltr;

    @Override
    public void install ( final C c, final U ui )
    {
        // Event Dispatch Thread checkers
        WebLookAndFeel.installEventDispatchThreadCheckers ( c );

        // Saving references
        this.component = c;
        this.ui = ui;

        // Additional actions before installation
        beforeInstall ();

        // Installing section painters
        // This must always be done first, before we try installing any listeners
        installSectionPainters ();

        // Installing properties and listeners
        installPropertiesAndListeners ();

        // Additional actions after installation
        afterInstall ();
    }

    @Override
    public void uninstall ( final C c, final U ui )
    {
        // Event Dispatch Thread checkers
        WebLookAndFeel.uninstallEventDispatchThreadCheckers ( c );

        // Additional actions before uninstallation
        beforeUninstall ();

        // Uninstalling properties and listeners
        uninstallPropertiesAndListeners ();

        // Uninstalling section painters
        // This must always be done last, after we uninstall all listeners
        uninstallSectionPainters ();

        // Additional actions after uninstallation
        afterUninstall ();

        // Cleaning up references
        this.component = null;
        this.ui = null;
    }

    /**
     * Performs additional actions before installation starts.
     * This is an additional method for override convenience.
     */
    protected void beforeInstall ()
    {
        /**
         * Updating installation mark.
         */
        this.installed = true;
    }

    /**
     * Performs additional actions after installation ends.
     * This is an additional method for override convenience.
     */
    protected void afterInstall ()
    {
        /**
         * Updating initial border.
         */
        updateBorder ();
    }

    /**
     * Performs additional actions before uninstallation starts.
     * This is an additional method for override convenience.
     */
    protected void beforeUninstall ()
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * Performs additional actions after uninstallation ends.
     * This is an additional method for override convenience.
     */
    protected void afterUninstall ()
    {
        /**
         * Updating installation mark.
         */
        this.installed = false;
    }

    @Override
    public boolean isInstalled ()
    {
        return installed;
    }

    @Override
    public Boolean isOpaque ()
    {
        return null;
    }

    /**
     * Returns whether or not this painter is allowed to update component settings and visual state.
     * By default it is determined by the painter type, for example any SectionPainter should avoid updating settings.
     *
     * @return {@code true} if this painter is allowed to update component settings and visual state, {@code false} otherwise
     */
    protected boolean isSettingsUpdateAllowed ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Actual updateability check
        return isInstalled () && !isSectionPainter ();
    }

    /**
     * Returns whether or not this is a section painter.
     * Some internal behaviors might vary depending on what this method returns.
     *
     * @return {@code true} if this is a section painter, {@code false} otherwise
     */
    protected boolean isSectionPainter ()
    {
        return this instanceof SectionPainter;
    }

    /**
     * Installs {@link SectionPainter}s used by this {@link Painter}.
     */
    protected final void installSectionPainters ()
    {
        final List<SectionPainter<C, U>> sectionPainters = getSectionPainters ();
        if ( CollectionUtils.notEmpty ( sectionPainters ) )
        {
            for ( final SectionPainter<C, U> sectionPainter : sectionPainters )
            {
                installSectionPainter ( sectionPainter );
            }
        }
    }

    /**
     * Uninstalls {@link SectionPainter}s used by this {@link Painter}.
     */
    protected final void uninstallSectionPainters ()
    {
        final List<SectionPainter<C, U>> sectionPainters = getInstalledSectionPainters ();
        if ( CollectionUtils.notEmpty ( sectionPainters ) )
        {
            for ( final SectionPainter<C, U> sectionPainter : sectionPainters )
            {
                uninstallSectionPainter ( sectionPainter );
            }
        }
    }

    /**
     * Returns {@link SectionPainter}s used by this painter or {@code null} if none are used.
     * Do not return any {@code null} {@link SectionPainter}s here, it will cause an exception.
     * You can use {@link #asList(SectionPainter[])} method to conveniently form a list filtering out {@link null}s.
     *
     * @return {@link SectionPainter}s used by this painter or {@code null} if none are used
     */
    protected List<SectionPainter<C, U>> getSectionPainters ()
    {
        return null;
    }

    /**
     * Returns section painters list in a most optimal way.
     * Utility method for usage inside of classed extending this one.
     *
     * @param sections section painters, some or all of them can be {@code null}
     * @return section painters list in a most optimal way
     */
    protected final List<SectionPainter<C, U>> asList ( final SectionPainter<C, U>... sections )
    {
        ArrayList<SectionPainter<C, U>> list = null;
        if ( sections != null )
        {
            for ( final SectionPainter<C, U> section : sections )
            {
                if ( section != null )
                {
                    if ( list == null )
                    {
                        list = new ArrayList<SectionPainter<C, U>> ( sections.length );
                    }
                    list.add ( section );
                }
            }
        }
        return list;
    }

    /**
     * Returns {@link SectionPainter}s installed within this painter or {@code null} if none are installed.
     * This method is used for various internal update mechanisms involving {@link SectionPainter}s.
     *
     * @return {@link SectionPainter}s installed within this painter or {@code null} if none are installed
     */
    protected final List<SectionPainter<C, U>> getInstalledSectionPainters ()
    {
        return MapUtils.notEmpty ( sectionPainters ) ? new ArrayList<SectionPainter<C, U>> ( sectionPainters.values () ) : null;
    }

    /**
     * Installs {@link SectionPainter} onto this {@link Painter}.
     *
     * @param painter {@link SectionPainter} to install
     */
    protected final void installSectionPainter ( final SectionPainter<C, U> painter )
    {
        // Ensure painter exists
        if ( painter == null )
        {
            throw new PainterException ( "Installed Painter cannot be null" );
        }

        // Initializing cache map
        if ( sectionPainters == null )
        {
            sectionPainters = new HashMap<String, SectionPainter<C, U>> ( 3 );
        }

        // Section identifier
        final String sectionId = painter.getSectionId ();

        // Uninstalling previous section painter under same section identifier
        final SectionPainter<C, U> old = sectionPainters.get ( sectionId );
        if ( old != null )
        {
            old.uninstall ( component, ui, AbstractPainter.this );
        }

        // Installing new section painter
        painter.install ( component, ui, AbstractPainter.this );

        // Caching new section painter
        sectionPainters.put ( sectionId, painter );
    }

    /**
     * Uninstalls {@link SectionPainter} from this {@link Painter}.
     *
     * @param painter {@link SectionPainter} to uninstall
     */
    protected final void uninstallSectionPainter ( final SectionPainter<C, U> painter )
    {
        // Ensure painter exists
        if ( painter == null )
        {
            throw new PainterException ( "Uninstalled Painter cannot be null" );
        }

        // Section identifier
        final String sectionId = painter.getSectionId ();

        // Removing section painter cache
        sectionPainters.remove ( sectionId );

        // Uninstalling section painter
        painter.uninstall ( component, ui, AbstractPainter.this );
    }

    /**
     * Paints {@link com.alee.painter.SectionPainter} at the specified bounds.
     * This method was introduced as one of the measures to fix #401 issue appearing on Linux systems.
     *
     * @param painter {@link com.alee.painter.SectionPainter}
     * @param g2d     graphics context
     * @param bounds  section bounds relative to component coordinates system
     */
    protected void paintSection ( final SectionPainter painter, final Graphics2D g2d, final Rectangle bounds )
    {
        if ( SystemUtils.isUnix () )
        {
            /**
             * This part of code is only here until #401 issue fix for Unix systems.
             * The problem with this workaround is that it provides bounds which are only relevant within paint run.
             */

            // Translating to section coordinates
            g2d.translate ( bounds.x, bounds.y );

            // Clipping area
            final Rectangle section = new Rectangle ( 0, 0, bounds.width, bounds.height );
            final Shape oc = GraphicsUtils.intersectClip ( g2d, section );

            // Creating appropriate bounds for painter
            final Bounds componentBounds = new Bounds ( component, -bounds.x, -bounds.y );
            final Bounds sectionBounds = new Bounds ( componentBounds, section );

            // Painting section
            painter.paint ( g2d, component, ui, sectionBounds );

            // Restoring old clip
            GraphicsUtils.restoreClip ( g2d, oc );

            // Translating back
            g2d.translate ( -bounds.x, -bounds.y );
        }
        else
        {
            // Clipping area
            final Shape oc = GraphicsUtils.intersectClip ( g2d, bounds );

            // Creating appropriate bounds for painter
            final Bounds componentBounds = new Bounds ( component );
            final Bounds sectionBounds = new Bounds ( componentBounds, bounds );

            // Painting section
            painter.paint ( g2d, component, ui, sectionBounds );

            // Restoring old clip
            GraphicsUtils.restoreClip ( g2d, oc );
        }
    }

    /**
     * Installs properties and listeners used by this {@link Painter} implementation.
     * Override this method instead of {@link #install(JComponent, ComponentUI)} to install additional properties and listeners.
     */
    protected void installPropertiesAndListeners ()
    {
        // Updating orientation
        updateOrientation ();
        saveOrientation ();

        // Install property change listener
        installPropertyChangeListener ();
    }

    /**
     * Uninstalls properties and listeners used by this {@link Painter} implementation.
     * Override this method instead of {@link #uninstall(JComponent, ComponentUI)} to uninstall additional properties and listeners.
     */
    protected void uninstallPropertiesAndListeners ()
    {
        // Uninstall property change listener
        uninstallPropertyChangeListener ();
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
                // Event Dispatch Thread check
                WebLookAndFeel.checkEventDispatchThread ();

                // Ensure component is still available
                // This might happen if painter is replaced from another PropertyChangeListener
                if ( component != null )
                {
                    // Inform about property change event
                    AbstractPainter.this.propertyChanged ( evt.getPropertyName (), evt.getOldValue (), evt.getNewValue () );
                }
            }
        };
        component.addPropertyChangeListener ( propertyChangeListener );
    }

    /**
     * Informs about {@link #component} property change.
     *
     * @param property modified property
     * @param oldValue old property value
     * @param newValue new property value
     */
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Forcing orientation visual updates
        if ( Objects.equals ( property, WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY ) )
        {
            orientationChange ();
        }

        // Tracking component border changes
        if ( Objects.equals ( property, WebLookAndFeel.BORDER_PROPERTY ) )
        {
            borderChange ( ( Border ) newValue );
        }

        // Tracking component margin and padding changes
        if ( Objects.equals ( property, WebLookAndFeel.LAF_MARGIN_PROPERTY, WebLookAndFeel.LAF_PADDING_PROPERTY ) )
        {
            updateBorder ();
        }
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
     * Performs various updates on orientation change.
     */
    protected void orientationChange ()
    {
        // Saving new orientation
        saveOrientation ();

        // Updating component view
        // Revalidate includes border update so we don't need to call it separately
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
     * Updates component orientation based on global orientation.
     */
    protected void updateOrientation ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            SwingUtils.setOrientation ( component );
        }
    }

    /**
     * Performs various border-related operations.
     *
     * @param border new border
     */
    protected void borderChange ( final Border border )
    {
        /**
         * Managing {@link JComponent}'s client property to preserve customized component {@link Border}s.
         * Any {@link Border} that is not {@code null} and not a {@link javax.swing.plaf.UIResource} is considered to be custom.
         * This is necessary to prevent WebLaF from overwriting those custom borders.
         */
        final boolean oldHonor = SwingUtils.getHonorUserBorders ( component );
        final boolean newHonor = !SwingUtils.isUIResource ( border );
        if ( oldHonor != newHonor )
        {
            // Updating client property
            SwingUtils.setHonorUserBorders ( component, newHonor );
        }
        if ( !newHonor && !( border instanceof WebBorder ) )
        {
            // Restoring WebLaF's border
            updateBorder ();
        }
    }

    /**
     * Returns {@link Painter} border according to component's margin, padding and {@link Painter}'s borders.
     * It is used to update component's border within {@link #updateBorder()} and to calculated default preferred size.
     *
     * @return {@link Painter} border according to component's margin, padding and {@link Painter}'s borders
     */
    protected Insets getCompleteBorder ()
    {
        final Insets border;
        if ( component != null && !SwingUtils.isPreserveBorders ( component ) )
        {
            // Initializing empty border
            border = new Insets ( 0, 0, 0, 0 );

            // Adding margin size
            if ( !isSectionPainter () )
            {
                final Insets margin = PainterSupport.getMargin ( component, true );
                SwingUtils.increase ( border, margin );
            }

            // Adding painter border size
            final Insets borders = getBorder ();
            if ( borders != null )
            {
                border.top += borders.top;
                border.left += ltr ? borders.left : borders.right;
                border.bottom += borders.bottom;
                border.right += ltr ? borders.right : borders.left;
            }

            // Adding padding size
            if ( !isSectionPainter () )
            {
                final Insets padding = PainterSupport.getPadding ( component, true );
                SwingUtils.increase ( border, padding );
            }
        }
        else
        {
            // Null border to prevent updates
            border = null;
        }
        return border;
    }

    /**
     * Returns border required for the view provided by this {@link Painter} or {@code null} in case it is not needed.     *
     * This border should not include possible component margin and padding, but only border provided by painter.
     * This border is added to component's margin and padding in {@link #getCompleteBorder()} calculations.
     * This border should not take component orientation into account, painter will take care of it later.
     *
     * @return border required for the view provided by this {@link Painter} or {@code null} in case it is not needed
     */
    protected Insets getBorder ()
    {
        return null;
    }

    /**
     * Custom check is written to avoid {@link Component#inside(int, int)} deprecated method usage.
     * Default implementation simply checks that point is within component bounds.
     */
    @Override
    public boolean contains ( final C c, final U ui, final Bounds bounds, final int x, final int y )
    {
        return bounds.get ().contains ( x, y );
    }

    @Override
    public int getBaseline ( final C c, final U ui, final Bounds bounds )
    {
        return -1;
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final C c, final U ui )
    {
        return Component.BaselineResizeBehavior.OTHER;
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SwingUtils.increase ( new Dimension ( 0, 0 ), getCompleteBorder () );
    }

    /**
     * Updates component with complete border.
     * This border takes painter borders and component margin and padding into account.
     */
    protected void updateBorder ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            final Insets border = getCompleteBorder ();
            if ( border != null )
            {
                final Border old = component.getBorder ();
                if ( !( old instanceof WebBorder ) || Objects.notEquals ( ( ( WebBorder ) old ).getBorderInsets (), border ) )
                {
                    component.setBorder ( new WebBorder ( border ) );
                }
            }
        }
    }

    /**
     * Should be called when whole painter visual representation changes.
     */
    protected void repaint ()
    {
        repaint ( 0, 0, component.getWidth (), component.getHeight () );
    }

    /**
     * Should be called when part of painter visual representation changes.
     *
     * @param bounds part bounds
     */
    protected void repaint ( final Rectangle bounds )
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
    protected void repaint ( final int x, final int y, final int width, final int height )
    {
        if ( isSettingsUpdateAllowed () && component.isShowing () )
        {
            component.repaint ( x, y, width, height );
        }
    }

    /**
     * Should be called when painter size or border changes.
     */
    protected void revalidate ()
    {
        updateBorder ();
        if ( isSettingsUpdateAllowed () )
        {
            component.revalidate ();
        }
    }

    /**
     * Should be called when painter opacity changes.
     * todo Use this instead of the outer border updates?
     */
    protected void updateOpacity ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            final Boolean opaque = isOpaque ();
            if ( opaque != null )
            {
                LookAndFeel.installProperty ( component, WebLookAndFeel.OPAQUE_PROPERTY, opaque ? Boolean.TRUE : Boolean.FALSE );
            }
        }
    }

    /**
     * Should be called when painter size, border and visual representation changes.
     * Makes sure that everything in the component view is up to date.
     */
    protected void updateAll ()
    {
        updateOpacity ();
        revalidate ();
        repaint ();
    }
}