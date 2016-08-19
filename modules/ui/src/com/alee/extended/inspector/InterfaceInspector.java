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

import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.tree.WebTreeFilterField;
import com.alee.extended.window.PopOverDirection;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.window.WebDialog;
import com.alee.laf.window.WebFrame;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mikle Garin
 */

public class InterfaceInspector extends WebPanel
{
    /**
     * UI elements.
     */
    private final InterfaceTree tree;
    private final WebScrollPane scrollPane;

    /**
     * Component inspect behavior.
     */
    private ComponentInspectBehavior inspectBehavior;

    /**
     * Constructs new empty inspector.
     */
    public InterfaceInspector ()
    {
        this ( StyleId.inspector, null );
    }

    /**
     * Constructs new inspector for the specified component and its childrens tree.
     *
     * @param inspected component to inspect
     */
    public InterfaceInspector ( final Component inspected )
    {
        this ( StyleId.inspector, inspected );
    }

    /**
     * Constructs new empty inspector.
     *
     * @param id style ID
     */
    public InterfaceInspector ( final StyleId id )
    {
        this ( id, null );
    }

    /**
     * Constructs new inspector for the specified component and its childrens tree.
     *
     * @param id        style ID
     * @param inspected component to inspect
     */
    public InterfaceInspector ( final StyleId id, final Component inspected )
    {
        super ( id );

        // Tree scroll
        scrollPane = new WebScrollPane ( StyleId.inspectorScroll.at ( InterfaceInspector.this ) );
        scrollPane.setPreferredWidth ( 300 );

        // Interface tree
        tree = new InterfaceTree ( StyleId.inspectorTree.at ( scrollPane ), inspected );
        scrollPane.getViewport ().setView ( tree );

        // Filtering field
        final WebTreeFilterField filter = new WebTreeFilterField ( StyleId.inspectorFilter.at ( InterfaceInspector.this ), tree );

        // Component inspect behavior
        final WebToggleButton inspectToggle = new WebToggleButton ( StyleId.inspectorInspect.at ( filter ), Icons.target );
        inspectToggle.setRolloverIcon ( Icons.targetHover );
        inspectToggle.setSelectedIcon ( Icons.targetSelected );
        inspectToggle.setCursor ( Cursor.getDefaultCursor () );
        inspectToggle.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( inspectToggle.isSelected () )
                {
                    if ( inspectBehavior == null )
                    {
                        inspectBehavior = new ComponentInspectBehavior ();
                    }
                    inspectBehavior.install ( inspected, new InspectionListener ()
                    {
                        @Override
                        public void inspected ( final Component component )
                        {
                            tree.navigate ( component );
                            inspectToggle.setSelected ( false );
                        }

                        @Override
                        public void cancelled ()
                        {
                            inspectToggle.setSelected ( false );
                        }
                    } );
                }
                else
                {
                    if ( inspectBehavior != null )
                    {
                        inspectBehavior.uninstall ();
                    }
                }
            }
        } );
        filter.setTrailingComponent ( inspectToggle );

        // UI composition
        final WebSeparator separator = new WebSeparator ( StyleId.inspectorSeparator.at ( InterfaceInspector.this ) );
        add ( new GroupPanel ( GroupingType.fillLast, 0, false, filter, separator, scrollPane ) );

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
     * Clears highlighted components.
     */
    public void clearHighlights ()
    {
        tree.clearSelection ();
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
        final WebDialog dialog = new WebDialog ( inspected );
        dialog.setIconImages ( WebLookAndFeel.getImages () );
        dialog.add ( new InterfaceInspector ( inspected ) );
        dialog.setModal ( false );
        dialog.pack ();
        dialog.setLocationRelativeTo ( inspected );
        // window.setAttachedTo ( ? );
        dialog.setVisible ( true );
        return dialog;
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
        final WebFrame frame = new WebFrame ();
        frame.setIconImages ( WebLookAndFeel.getImages () );
        frame.add ( new InterfaceInspector ( inspected ) );
        frame.pack ();
        frame.setLocationRelativeTo ( inspected );
        frame.setVisible ( true );
        return frame;
    }

    /**
     * Returns separate inspector popover for the specified component.
     * That popover will be displayed straight away near the inspected component.
     *
     * @param inspected component to inspect
     * @return separate inspector popover for the specified component
     */
    public static WebPopOver showPopOver ( final Component inspected )
    {
        final WebPopOver popOver = new WebPopOver ( inspected );
        popOver.setIconImages ( WebLookAndFeel.getImages () );
        popOver.add ( new InterfaceInspector ( StyleId.inspectorPopover, inspected ) );
        popOver.show ( inspected, PopOverDirection.right );
        return popOver;
    }
}