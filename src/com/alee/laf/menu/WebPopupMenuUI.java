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

package com.alee.laf.menu;

import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.SystemUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for JPopupMenu component.
 *
 * @author Mikle Garin
 */

public class WebPopupMenuUI extends BasicPopupMenuUI implements ShapeProvider
{
    /**
     * Menu listeners.
     */
    private PropertyChangeListener propertyChangeListener;

    /**
     * Returns an instance of the WebPopupMenuUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebPopupMenuUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebPopupMenuUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( popupMenu );
        popupMenu.setOpaque ( false );
        popupMenu.setBackground ( Color.WHITE );

        // Popup-type dependant border
        popupMenu.setBorder ( popupMenu instanceof BasicComboPopup ? BorderFactory.createEmptyBorder ( 1, 1, 1, 1 ) :
                BorderFactory.createEmptyBorder ( 0, 0, 0, 0 ) );

        // Workaround for menu with non-opaque parent window
        if ( SystemUtils.isJava7orAbove () )
        {
            propertyChangeListener = new PropertyChangeListener ()
            {
                @Override
                public void propertyChange ( final PropertyChangeEvent evt )
                {
                    if ( evt.getNewValue () == Boolean.TRUE )
                    {
                        final Window ancestor = SwingUtils.getWindowAncestor ( popupMenu );
                        if ( ancestor != null && ancestor.getClass ().getCanonicalName ().endsWith ( "HeavyWeightWindow" ) )
                        {
                            final Component parent = ancestor.getParent ();
                            if ( parent != null && parent instanceof Window && !ProprietaryUtils.isWindowOpaque ( ( Window ) parent ) )
                            {
                                ProprietaryUtils.setWindowOpaque ( ancestor, false );
                            }
                        }
                    }
                }
            };
            popupMenu.addPropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, propertyChangeListener );
        }
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        if ( SystemUtils.isJava7orAbove () )
        {
            popupMenu.removePropertyChangeListener ( WebLookAndFeel.VISIBLE_PROPERTY, propertyChangeListener );
        }

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return LafUtils.getWebBorderShape ( popupMenu, 0, StyleConstants.smallRound );
    }

    /**
     * Paints popup menu decoration.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        super.paint ( g, c );

        // Border and background
        LafUtils.drawWebStyle ( ( Graphics2D ) g, c, StyleConstants.shadeColor, 0, StyleConstants.smallRound, true, false,
                StyleConstants.averageBorderColor );
    }
}