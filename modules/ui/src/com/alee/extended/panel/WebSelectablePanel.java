package com.alee.extended.panel;

import com.alee.extended.layout.ComponentPanelLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.style.StyleId;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Mikle Garin
 */

public class WebSelectablePanel extends WebPanel
{
    private final WebComponentPane componentPane;
    private final DefaultFocusTracker focusTracker;

    private boolean dragged = false;
    private boolean focused = false;

    public WebSelectablePanel ( final WebComponentPane componentPane )
    {
        super ( StyleId.componentpanePanel.at ( componentPane ) );
        this.componentPane = componentPane;

        // On-press focus transfer & reorder
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            private int startY;

            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( !SwingUtils.hasFocusOwner ( WebSelectablePanel.this ) )
                {
                    WebSelectablePanel.this.transferFocus ();
                }
                if ( getComponentPane ().isEnabled () && componentPane.isReorderingAllowed () && SwingUtilities.isLeftMouseButton ( e ) )
                {
                    dragged = true;
                    startY = getY ();
                    getComponentPane ().getContainer ().setComponentZOrder ( WebSelectablePanel.this, 0 );
                }
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                if ( componentPane.isReorderingAllowed () && dragged )
                {
                    getComponentPane ().getContainerLayout ().setComponentShift ( WebSelectablePanel.this, getY () - startY );
                    WebSelectablePanel.this.revalidate ();
                }
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                final WebSelectablePanel wsp = WebSelectablePanel.this;
                final ComponentPanelLayout cpl = getComponentPane ().getContainerLayout ();
                if ( componentPane.isReorderingAllowed () && SwingUtilities.isLeftMouseButton ( e ) && dragged )
                {
                    // Stop drag
                    dragged = false;

                    // Update if needed
                    if ( getY () - startY == 0 || cpl.getComponents ().size () <= 1 )
                    {
                        // Ignore drag if there is only 1 component or change is zero
                        cpl.setComponentShift ( wsp, null );
                    }
                    else
                    {
                        // Dragged panel index and middle
                        final int oldIndex = cpl.indexOf ( wsp );
                        final int middle = getMiddleY ( wsp );

                        // Searching for insert index
                        int insertIndex = 0;
                        for ( final Component component : cpl.getComponents () )
                        {
                            if ( component != wsp && middle > getMiddleY ( component ) )
                            {
                                insertIndex = cpl.indexOf ( component ) + 1;
                            }
                        }

                        // Fix index
                        if ( insertIndex > oldIndex )
                        {
                            insertIndex--;
                        }

                        // Resetting shift
                        cpl.setComponentShift ( wsp, null );

                        // Changing panel location if it has actually changed
                        if ( oldIndex != insertIndex )
                        {
                            // Updating panel indices
                            cpl.removeLayoutComponent ( wsp );
                            cpl.insertLayoutComponent ( insertIndex, wsp );

                            // Informing all listeners
                            getComponentPane ().fireComponentOrderChanged ( wsp.getComponent (), oldIndex, insertIndex );
                        }
                    }

                    // Update panel positions
                    wsp.revalidate ();
                }
            }

            private int getMiddleY ( final Component component )
            {
                final Rectangle rectangle = component.getBounds ();
                return rectangle.y + rectangle.height / 2;
            }

            private int getY ()
            {
                return MouseInfo.getPointerInfo ().getLocation ().y;
            }
        };
        addMouseListener ( mouseAdapter );
        addMouseMotionListener ( mouseAdapter );

        // Panel focus tracking
        focusTracker = new DefaultFocusTracker ( true )
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                final WebSelectablePanel wsp = WebSelectablePanel.this;
                wsp.focused = focused;

                // Cancel panel drag on focus loss
                if ( !focused && dragged )
                {
                    dragged = false;
                    getComponentPane ().getContainerLayout ().setComponentShift ( wsp, null );
                    wsp.revalidate ();
                }

                repaint ();
            }
        };
        com.alee.managers.focus.FocusManager.addFocusTracker ( WebSelectablePanel.this, focusTracker );
    }

    public boolean isFocused ()
    {
        return focused;
    }

    public boolean isDragged ()
    {
        return dragged;
    }

    public Component getComponent ()
    {
        return getComponent ( 0 );
    }

    public int getIndex ()
    {
        final WebComponentPane pane = getComponentPane ();
        return pane != null ? pane.getContainerLayout ().indexOf ( WebSelectablePanel.this ) : -1;
    }

    public int getTotal ()
    {
        final WebComponentPane pane = getComponentPane ();
        return pane != null ? pane.getContainer ().getComponentCount () : 0;
    }

    public WebComponentPane getComponentPane ()
    {
        return componentPane;
    }
}