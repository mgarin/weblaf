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

package com.alee.extended.panel;

import com.alee.extended.layout.ComponentPanelLayout;
import com.alee.extended.painter.DefaultPainter;
import com.alee.laf.StyleConstants;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mgarin Date: 29.02.12 Time: 16:53
 */

public class WebComponentPanel extends WebPanel
{
    public static final int GRIPPER_SIZE = 7;
    public static final int SINGLE_GRIPPER_STEP = 4;

    private List<ComponentReorderListener> listeners = new ArrayList<ComponentReorderListener> ();

    private WebPanel container;
    private Map<Component, WebSelectablePanel> components = new LinkedHashMap<Component, WebSelectablePanel> ();

    private Insets elementMargin = new Insets ( 2, 2, 2, 2 );
    private boolean reorderingAllowed = false;
    private boolean showReorderGrippers = true;
    private boolean upDownHotkeysAllowed = true;
    private boolean leftRightHotkeysAllowed = false;

    public WebComponentPanel ()
    {
        super ();
        initialize ();
    }

    public WebComponentPanel ( boolean decorated )
    {
        super ( decorated );
        initialize ();
    }

    private void initialize ()
    {
        // Default styling
        setWebColored ( false );

        // Elements layout
        container = new WebPanel ();
        container.setLayout ( new ComponentPanelLayout () );
        add ( container, BorderLayout.CENTER );

        // Previous action hotkeys
        HotkeyRunnable prevAction = new HotkeyRunnable ()
        {
            public void run ( KeyEvent e )
            {
                if ( upDownHotkeysAllowed && Hotkey.UP.isTriggered ( e ) || leftRightHotkeysAllowed && Hotkey.LEFT.isTriggered ( e ) )
                {
                    int index = getFocusedElementIndex ();
                    if ( index == -1 )
                    {
                        focusElement ( getElementCount () - 1 );
                    }
                    else
                    {
                        focusElement ( index > 0 ? index - 1 : getElementCount () - 1 );
                    }
                }
            }
        };
        HotkeyManager.registerHotkey ( this, this, Hotkey.UP, prevAction );
        HotkeyManager.registerHotkey ( this, this, Hotkey.LEFT, prevAction );

        // Next action hotkeys
        HotkeyRunnable nextAction = new HotkeyRunnable ()
        {
            public void run ( KeyEvent e )
            {
                if ( upDownHotkeysAllowed && Hotkey.DOWN.isTriggered ( e ) || leftRightHotkeysAllowed && Hotkey.RIGHT.isTriggered ( e ) )
                {
                    int index = getFocusedElementIndex ();
                    if ( index == -1 )
                    {
                        focusElement ( 0 );
                    }
                    else
                    {
                        focusElement ( index < getElementCount () - 1 ? index + 1 : 0 );
                    }
                }
            }
        };
        HotkeyManager.registerHotkey ( this, this, Hotkey.DOWN, nextAction );
        HotkeyManager.registerHotkey ( this, this, Hotkey.RIGHT, nextAction );
    }

    public void applyComponentOrientation ( ComponentOrientation o )
    {
        super.applyComponentOrientation ( o );
        updateAllBorders ();
    }

    public ComponentPanelLayout getContainerLayout ()
    {
        return ( ComponentPanelLayout ) container.getLayout ();
    }

    public boolean isUpDownHotkeysAllowed ()
    {
        return upDownHotkeysAllowed;
    }

    public void setUpDownHotkeysAllowed ( boolean upDownHotkeysAllowed )
    {
        this.upDownHotkeysAllowed = upDownHotkeysAllowed;
    }

    public boolean isLeftRightHotkeysAllowed ()
    {
        return leftRightHotkeysAllowed;
    }

    public void setLeftRightHotkeysAllowed ( boolean leftRightHotkeysAllowed )
    {
        this.leftRightHotkeysAllowed = leftRightHotkeysAllowed;
    }

    public WebSelectablePanel addElement ( Component component )
    {
        // Ignore existing component insert
        if ( components.containsKey ( component ) )
        {
            return components.get ( component );
        }

        // Creating view for component
        WebSelectablePanel element = new WebSelectablePanel ();
        element.add ( component, BorderLayout.CENTER );

        // todo Fix this workaround and check other layouts for that problem
        // String is needed to invoke proper layout method
        container.add ( element, "" );

        // Saving view for component
        components.put ( component, element );

        // Updating existing panels
        updateAllBorders ();

        return element;
    }

    public void removeElement ( int index )
    {
        removeElement ( getElement ( index ) );
    }

    public void removeElement ( WebSelectablePanel element )
    {
        for ( Component component : components.keySet () )
        {
            if ( components.get ( component ) == element )
            {
                removeElement ( component );
                break;
            }
        }
    }

    public void removeElement ( Component component )
    {
        // Removing actual element
        WebSelectablePanel element = components.get ( component );
        container.remove ( element );

        // Removing data
        components.remove ( component );

        // Updating existing panels
        updateAllBorders ();
    }

    public int getElementCount ()
    {
        return components.size ();
    }

    public WebSelectablePanel getElement ( int index )
    {
        return ( WebSelectablePanel ) getContainerLayout ().getComponent ( index );
    }

    public WebSelectablePanel getFocusedElement ()
    {
        for ( Component component : getContainerLayout ().getComponents () )
        {
            WebSelectablePanel selectablePanel = ( WebSelectablePanel ) component;
            if ( selectablePanel.isFocused () )
            {
                return selectablePanel;
            }
        }
        return null;
    }

    public int getFocusedElementIndex ()
    {
        return getContainerLayout ().indexOf ( getFocusedElement () );
    }

    public void focusElement ( int index )
    {
        getElement ( index ).transferFocus ();
    }

    public Insets getElementMargin ()
    {
        return elementMargin;
    }

    public void setElementMargin ( int margin )
    {
        setElementMargin ( margin, margin, margin, margin );
    }

    public void setElementMargin ( int top, int left, int bottom, int right )
    {
        setElementMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setElementMargin ( Insets margin )
    {
        this.elementMargin = margin;

        // Updating existing panels
        updateAllBorders ();
    }

    public boolean isReorderingAllowed ()
    {
        return reorderingAllowed;
    }

    public void setReorderingAllowed ( boolean reorderingAllowed )
    {
        this.reorderingAllowed = reorderingAllowed;

        // Updating existing panels
        updateAllBorders ();
    }

    public boolean isShowReorderGrippers ()
    {
        return showReorderGrippers;
    }

    public void setShowReorderGrippers ( boolean showReorderGrippers )
    {
        this.showReorderGrippers = showReorderGrippers;

        // Updating existing panels
        updateAllBorders ();
    }

    private void updateAllBorders ()
    {
        for ( WebSelectablePanel panel : components.values () )
        {
            panel.updateBorder ();
        }
    }

    public class WebSelectablePanel extends WebPanel
    {
        private boolean dragged = false;
        private boolean focused = false;

        public WebSelectablePanel ()
        {
            super ();
            updateBorder ();

            // Selection painter         
            setPainter ( new WebSelectablePanelPainter () );

            // On-press focus transfer & reorder
            MouseAdapter mouseAdapter = new MouseAdapter ()
            {
                private int startY;

                public void mousePressed ( MouseEvent e )
                {
                    if ( !SwingUtils.hasFocusOwner ( WebSelectablePanel.this ) )
                    {
                        WebSelectablePanel.this.transferFocus ();
                    }
                    if ( WebComponentPanel.this.isEnabled () && SwingUtilities.isLeftMouseButton ( e ) )
                    {
                        dragged = true;
                        startY = getY ();
                        container.setComponentZOrder ( WebSelectablePanel.this, 0 );
                    }
                }

                public void mouseDragged ( MouseEvent e )
                {
                    if ( dragged )
                    {
                        getContainerLayout ().setComponentShift ( WebSelectablePanel.this, getY () - startY );
                        WebSelectablePanel.this.revalidate ();
                    }
                }

                public void mouseReleased ( MouseEvent e )
                {
                    WebSelectablePanel wsp = WebSelectablePanel.this;
                    ComponentPanelLayout cpl = getContainerLayout ();
                    if ( SwingUtilities.isLeftMouseButton ( e ) && dragged )
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
                            int oldIndex = cpl.indexOf ( wsp );
                            int middle = getMiddleY ( wsp );

                            // Searching for insert index
                            int insertIndex = 0;
                            for ( Component component : cpl.getComponents () )
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
                                updateAllBorders ();

                                // Informing all listeners
                                fireComponentOrderChanged ( wsp.getComponent (), oldIndex, insertIndex );
                            }
                        }

                        // Update panel positions
                        wsp.revalidate ();
                    }
                }

                private int getMiddleY ( Component component )
                {
                    Rectangle rectangle = component.getBounds ();
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
            FocusManager.registerFocusTracker ( new DefaultFocusTracker ( WebSelectablePanel.this, true, true )
            {
                public void focusChanged ( boolean focused )
                {
                    WebSelectablePanel wsp = WebSelectablePanel.this;
                    wsp.focused = focused;

                    // Cancel panel drag on focus loss
                    if ( !focused && dragged )
                    {
                        dragged = false;
                        getContainerLayout ().setComponentShift ( wsp, null );
                        wsp.revalidate ();
                    }

                    repaint ();
                }
            } );
        }

        public void updateBorder ()
        {
            int index = getIndex ();
            boolean ltr = getComponentOrientation ().isLeftToRight ();

            int top = index == 0 ? elementMargin.top : elementMargin.top + 1;
            int left = elementMargin.left + ( reorderingAllowed && showReorderGrippers && ltr ? GRIPPER_SIZE : 0 );
            int bottom = index == components.size () - 1 ? elementMargin.bottom : elementMargin.bottom + 1;
            int right = elementMargin.right + ( reorderingAllowed && showReorderGrippers && !ltr ? GRIPPER_SIZE : 0 );

            setMargin ( top, left, bottom, right );
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
            return getContainerLayout ().indexOf ( WebSelectablePanel.this );
        }
    }

    public class WebSelectablePanelPainter extends DefaultPainter<WebSelectablePanel>
    {
        private float[] fractions = { 0f, 0.25f, 0.75f, 1f };
        private Color[] lightColors = { StyleConstants.transparent, Color.WHITE, Color.WHITE, StyleConstants.transparent };
        private Color[] darkColors = { StyleConstants.transparent, Color.GRAY, Color.GRAY, StyleConstants.transparent };

        public boolean isOpaque ( WebSelectablePanel c )
        {
            return true;
        }

        public void paint ( Graphics2D g2d, Rectangle bounds, WebSelectablePanel panel )
        {
            boolean notFirst = panel.getIndex () > 0;
            boolean notLast = panel.getIndex () < components.size () - 1;

            if ( panel.isFocused () )
            {
                // Background
                g2d.setPaint ( new GradientPaint ( bounds.x, bounds.y, StyleConstants.topBgColor, bounds.x, bounds.y + bounds.height,
                        StyleConstants.bottomBgColor ) );
                g2d.fill ( bounds );

                // Borders
                Integer shift = getContainerLayout ().getComponentShift ( panel );
                boolean moved = panel.isDragged () && shift != null && shift != 0;
                g2d.setPaint ( StyleConstants.darkBorderColor );
                if ( notFirst || moved )
                {
                    g2d.drawLine ( bounds.x, bounds.y, bounds.x + bounds.width - 1, bounds.y );
                }
                if ( notLast || moved )
                {
                    g2d.drawLine ( bounds.x, bounds.y + bounds.height - 1, bounds.x + bounds.width - 1, bounds.y + bounds.height - 1 );
                }
            }

            // Gripper
            if ( reorderingAllowed && showReorderGrippers )
            {
                // Determining coordinates
                boolean ltr = panel.getComponentOrientation ().isLeftToRight ();
                int minY = bounds.y + 2 + ( notFirst ? 1 : 0 );
                int maxY = bounds.x + bounds.height - 2 - ( notLast ? 1 : 0 );
                int x = ltr ? bounds.x + 3 : bounds.x + bounds.width - GRIPPER_SIZE + 2;
                int y = minY + ( ( maxY - minY ) % SINGLE_GRIPPER_STEP ) / 2;

                // Painters
                Paint light = new LinearGradientPaint ( x, minY, x, maxY, fractions, lightColors );
                Paint dark = new LinearGradientPaint ( x, minY, x, maxY, fractions, darkColors );

                // Paint cycle
                while ( y + 3 < maxY )
                {
                    g2d.setPaint ( light );
                    g2d.fillRect ( x + 1, y + 1, 2, 2 );
                    g2d.setPaint ( dark );
                    g2d.fillRect ( x, y, 2, 2 );
                    y += SINGLE_GRIPPER_STEP;
                }
            }
        }
    }

    public void addComponentReorderListener ( ComponentReorderListener listener )
    {
        listeners.add ( listener );
    }

    public void removeComponentReorderListener ( ComponentReorderListener listener )
    {
        listeners.remove ( listener );
    }

    private void fireComponentOrderChanged ( Component component, int oldIndex, int newIndex )
    {
        for ( ComponentReorderListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.componentOrderChanged ( component, oldIndex, newIndex );
        }
    }
}