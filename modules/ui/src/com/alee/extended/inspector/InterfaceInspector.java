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
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;
import com.alee.utils.ProprietaryUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Complex component that provides Swing components structure overview and allows inspecting specific components.
 * It also has a few methods to display as a separate window on top of any UI element.
 *
 * @author Mikle Garin
 * @see InterfaceTree
 * @see #showFrame(Component)
 * @see #showDialog(Component, Component)
 * @see #showPopOver(Component, Component)
 * @see #showPopOver(Component, Component, PopOverDirection)
 */
public class InterfaceInspector extends WebPanel
{
    /**
     * {@link InterfaceTree}.
     */
    private final InterfaceTree tree;

    /**
     * {@link Component} inspect behavior.
     */
    private ComponentInspectBehavior inspectBehavior;

    /**
     * Constructs new empty {@link InterfaceInspector}.
     */
    public InterfaceInspector ()
    {
        this ( StyleId.inspector, null );
    }

    /**
     * Constructs new {@link InterfaceInspector} for the specified {@link Component} and its childrens tree.
     *
     * @param inspected {@link Component} to inspect
     */
    public InterfaceInspector ( final Component inspected )
    {
        this ( StyleId.inspector, inspected );
    }

    /**
     * Constructs new empty {@link InterfaceInspector}.
     *
     * @param id style ID
     */
    public InterfaceInspector ( final StyleId id )
    {
        this ( id, null );
    }

    /**
     * Constructs new {@link InterfaceInspector} for the specified {@link Component} and its childrens tree.
     *
     * @param id        style ID
     * @param inspected {@link Component} to inspect
     */
    public InterfaceInspector ( final StyleId id, final Component inspected )
    {
        super ( id );

        // Tree scroll
        final WebScrollPane scrollPane = new WebScrollPane ( StyleId.inspectorScroll.at ( InterfaceInspector.this ) );
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
        inspectToggle.addHotkey ( Hotkey.CTRL_I );
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

        // Expanding tree root by default
        tree.expandRoot ();
    }

    /**
     * Sets inspected {@link Component}.
     *
     * @param inspected {@link Component} to inspect
     */
    public void setInspected ( final Component inspected )
    {
        tree.setRootComponent ( inspected );
    }

    /**
     * Clears highlighted {@link Component}s.
     */
    public void clearHighlights ()
    {
        tree.clearSelection ();
    }

    /**
     * Returns separate {@link WebFrame} with inspector for all visible {@link Component}s.
     * That {@link WebFrame} will be displayed straight away on the screen.
     *
     * @return separate {@link WebFrame} with inspector for all visible {@link Component}s
     */
    public static WebFrame showFrame ()
    {
        return showFrame ( null );
    }

    /**
     * Returns separate {@link WebFrame} with inspector for the specified {@link Component}.
     * That {@link WebFrame} will be displayed straight away on the screen.
     *
     * @param inspected {@link Component} to inspect
     * @return separate {@link WebFrame} with inspector for the specified {@link Component}
     */
    public static WebFrame showFrame ( final Component inspected )
    {
        final WebFrame frame = new WebFrame ();
        frame.setIconImages ( WebLookAndFeel.getImages () );
        frame.add ( new InterfaceInspector ( inspected ) );
        ProprietaryUtils.setUtilityWindowType ( frame );
        frame.setModalExclusionType ( Dialog.ModalExclusionType.APPLICATION_EXCLUDE );
        frame.pack ();
        frame.setLocationRelativeTo ( inspected );
        frame.setVisible ( true );
        return frame;
    }

    /**
     * Returns separate {@link WebDialog} with inspector for all visible {@link Component}s.
     * That {@link WebDialog} will be displayed straight away on the screen.
     *
     * @param parent parent {@link Component} for {@link WebDialog}
     * @return separate {@link WebDialog} with inspector for all visible {@link Component}s
     */
    public static WebDialog showDialog ( final Component parent )
    {
        return showDialog ( parent, null );
    }

    /**
     * Returns separate {@link WebDialog} with inspector for the specified {@link Component}.
     * That {@link WebDialog} will be displayed straight away on the screen.
     *
     * @param parent    parent {@link Component} for {@link WebDialog}
     * @param inspected {@link Component} to inspect
     * @return separate {@link WebDialog} with inspector for the specified {@link Component}
     */
    public static WebDialog showDialog ( final Component parent, final Component inspected )
    {
        final WebDialog dialog = new WebDialog ( parent );
        dialog.setIconImages ( WebLookAndFeel.getImages () );
        dialog.add ( new InterfaceInspector ( inspected ) );
        ProprietaryUtils.setUtilityWindowType ( dialog );
        dialog.setModalExclusionType ( Dialog.ModalExclusionType.APPLICATION_EXCLUDE );
        dialog.setModal ( false );
        dialog.pack ();
        dialog.setLocationRelativeTo ( inspected );
        // dialog.setAttachedTo ( ? );
        dialog.setVisible ( true );
        return dialog;
    }

    /**
     * Returns separate {@link WebPopOver} with inspector for all visible {@link Component}s.
     * That {@link WebPopOver} will be displayed straight away near the parent {@link Component}.
     *
     * @param parent parent {@link Component} for {@link WebPopOver}
     * @return separate {@link WebPopOver} with inspector for all visible {@link Component}s
     */
    public static WebPopOver showPopOver ( final Component parent )
    {
        return showPopOver ( parent, null, PopOverDirection.right );
    }

    /**
     * Returns separate {@link WebPopOver} with inspector for all visible {@link Component}s.
     * That {@link WebPopOver} will be displayed straight away near the parent {@link Component}.
     *
     * @param parent    parent {@link Component} for {@link WebPopOver}
     * @param direction {@link PopOverDirection}
     * @return separate {@link WebPopOver} with inspector for all visible {@link Component}s
     */
    public static WebPopOver showPopOver ( final Component parent, final PopOverDirection direction )
    {
        return showPopOver ( parent, null, direction );
    }

    /**
     * Returns separate {@link WebPopOver} with inspector for the specified {@link Component}.
     * That {@link WebPopOver} will be displayed straight away near the parent {@link Component}.
     *
     * @param parent    parent {@link Component} for {@link WebPopOver}
     * @param inspected {@link Component} to inspect
     * @return separate {@link WebPopOver} with inspector for the specified {@link Component}
     */
    public static WebPopOver showPopOver ( final Component parent, final Component inspected )
    {
        return showPopOver ( parent, inspected, PopOverDirection.right );
    }

    /**
     * Returns separate {@link WebPopOver} with inspector for the specified {@link Component}.
     * That {@link WebPopOver} will be displayed straight away near the parent {@link Component}.
     *
     * @param parent    parent {@link Component} for {@link WebPopOver}
     * @param inspected {@link Component} to inspect
     * @param direction {@link PopOverDirection}
     * @return separate {@link WebPopOver} with inspector for the specified {@link Component}
     */
    public static WebPopOver showPopOver ( final Component parent, final Component inspected, final PopOverDirection direction )
    {
        final WebPopOver popOver = new WebPopOver ( parent );
        popOver.setIconImages ( WebLookAndFeel.getImages () );
        popOver.add ( new InterfaceInspector ( StyleId.inspectorPopover, inspected ) );
        popOver.setModalExclusionType ( Dialog.ModalExclusionType.APPLICATION_EXCLUDE );
        popOver.show ( parent, direction );
        return popOver;
    }
}