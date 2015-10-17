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

package com.alee.extended.inspector;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public class InterfaceInspector extends WebPanel
{
    /**
     * Inspected components tree.
     */
    private final InterfaceTree tree;

    /**
     * Constructs new empty inspector.
     */
    public InterfaceInspector ()
    {
        this ( null );
    }

    /**
     * Constructs new inspector for the specified component and its childrens tree.
     *
     * @param inspected component to inspect
     */
    public InterfaceInspector ( final Component inspected )
    {
        super ();

        // Component inspection tree
        tree = new InterfaceTree ( inspected );
        add ( new WebScrollPane ( StyleId.scrollpaneUndecorated, tree ) );

        // Expanding tree
        tree.expandAll ();
    }

    /**
     * Sets inspected component.
     *
     * @param inspected component to inspect
     */
    public void setInspected ( final Component inspected )
    {
        tree.setRootComponent ( inspected );
    }

    /**
     * Returns separate inspector frame for the specified component.
     * That frame will be displayed straight away on the screen.
     *
     * @param inspected component to inspect
     * @return separate inspector frame for the specified component
     */
    public static WebDialog showDialog ( final Component inspected )
    {
        final WebDialog window = new WebDialog ( inspected );
        window.setIconImages ( WebLookAndFeel.getImages () );
        window.add ( new InterfaceInspector ( inspected ) );
        window.setModal ( false );
        window.pack ();
        window.setLocationRelativeTo ( inspected );
        // window.setAttachedTo ( ? );
        window.setVisible ( true );
        return window;
    }

    /**
     * Returns separate inspector frame for the specified component.
     * That frame will be displayed straight away on the screen.
     *
     * @param inspected component to inspect
     * @return separate inspector frame for the specified component
     */
    public static WebFrame showFrame ( final Component inspected )
    {
        final WebFrame window = new WebFrame ();
        window.setIconImages ( WebLookAndFeel.getImages () );
        window.add ( new InterfaceInspector ( inspected ) );
        window.pack ();
        window.setLocationRelativeTo ( inspected );
        window.setVisible ( true );
        return window;
    }
}