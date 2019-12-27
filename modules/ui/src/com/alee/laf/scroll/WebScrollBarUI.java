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

package com.alee.laf.scroll;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Custom UI for {@link JScrollBar} component.
 *
 * @author Mikle Garin
 */
public class WebScrollBarUI extends WScrollBarUI
{
    /**
     * Whether or not scroll bar buttons should be displayed.
     */
    protected boolean displayButtons;

    /**
     * Whether or not scroll bar track should be displayed.
     */
    protected boolean displayTrack;

    /**
     * Miinimum thumb size.
     */
    protected Dimension minimumThumbSize;

    /**
     * Listeners.
     */
    private transient PropertyChangeListener buttonsStateUpdater;

    /**
     * Returns an instance of the {@link WebScrollBarUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebScrollBarUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebScrollBarUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Enabled handling mark
        SwingUtils.setHandlesEnableStateMark ( scrollbar );

        // Applying skin
        StyleManager.installSkin ( scrollbar );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( scrollbar );

        // Removing enabled handling mark
        SwingUtils.removeHandlesEnableStateMark ( scrollbar );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public boolean isDisplayButtons ()
    {
        return displayButtons;
    }

    @Override
    public void setDisplayButtons ( final boolean displayButtons )
    {
        this.displayButtons = displayButtons;
        scrollbar.revalidate ();
        scrollbar.repaint ();
    }

    @Override
    public boolean isDisplayTrack ()
    {
        return displayTrack;
    }

    @Override
    public void setDisplayTrack ( final boolean displayTrack )
    {
        this.displayTrack = displayTrack;
        scrollbar.revalidate ();
        scrollbar.repaint ();
    }

    /**
     * Installs additional scroll bar components.
     */
    @Override
    protected void installComponents ()
    {
        // Decrease button
        decrButton = new ScrollBarButton ( StyleId.scrollbarDecreaseButton.at ( scrollbar ) );
        scrollbar.add ( decrButton );

        // Increase button
        incrButton = new ScrollBarButton ( StyleId.scrollbarIncreaseButton.at ( scrollbar ) );
        scrollbar.add ( incrButton );

        // Proper decoration states update
        buttonsStateUpdater = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                DecorationUtils.fireStatesChanged ( decrButton );
                DecorationUtils.fireStatesChanged ( incrButton );
            }
        };
        scrollbar.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, buttonsStateUpdater );
    }

    @Override
    protected void uninstallComponents ()
    {
        scrollbar.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, buttonsStateUpdater );
        scrollbar.remove ( incrButton );
        scrollbar.remove ( decrButton );
    }

    @Override
    protected Dimension getMinimumThumbSize ()
    {
        return minimumThumbSize;
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, width, height );
    }

    @NotNull
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this, new ScrollBarPaintParameters ( isDragging, trackRect, thumbRect ) );
    }

    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        // Scroll bar preferred size
        final boolean ver = scrollbar.getOrientation () == Adjustable.VERTICAL;
        final Painter painter = PainterSupport.getPainter ( c );
        final Dimension ps = painter != null ? painter.getPreferredSize () : new Dimension ( ver ? 0 : 48, ver ? 48 : 0 );

        // Arrow button preferred sizes
        if ( painter != null && displayButtons && decrButton != null && incrButton != null )
        {
            final Dimension dps = decrButton.getPreferredSize ();
            final Dimension ips = incrButton.getPreferredSize ();
            if ( ver )
            {
                ps.width = Math.max ( ps.width, Math.max ( dps.width, ips.width ) );
                ps.height += dps.height + ips.height;
            }
            else
            {
                ps.width += dps.width + ips.width;
                ps.height = Math.max ( ps.height, Math.max ( dps.height, ips.height ) );
            }
        }

        return ps;
    }

    /**
     * Customized button class.
     */
    public class ScrollBarButton extends WebButton implements Stateful
    {
        /**
         * Constructs new scroll bar button wit the specified style.
         *
         * @param id {@link StyleId}
         */
        public ScrollBarButton ( @NotNull final StyleId id )
        {
            super ( id );
            setFocusable ( false );
            setEnabled ( scrollbar != null && scrollbar.isEnabled () );
        }

        @Nullable
        @Override
        public List<String> getStates ()
        {
            // Additional states useful for the decoration
            List<String> states = null;
            if ( scrollbar != null )
            {
                final boolean ver = scrollbar.getOrientation () == Adjustable.VERTICAL;
                states = CollectionUtils.asList ( ver ? DecorationState.vertical : DecorationState.horizontal );
            }
            return states;
        }

        @Override
        public void setFocusable ( final boolean focusable )
        {
            // Workaround to completely disable focusability of this button
            super.setFocusable ( false );
        }

        @NotNull
        @Override
        public Dimension getPreferredSize ()
        {
            // The best way (so far) to hide buttons without causing a serious mess in the code
            return displayButtons ? super.getPreferredSize () : new Dimension ( 0, 0 );
        }
    }
}