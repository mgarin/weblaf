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

import com.alee.api.annotations.NotNull;
import com.alee.extended.layout.ComponentPanelLayout;
import com.alee.managers.style.StyleId;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.utils.CollectionUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mikle Garin
 */
public class WebComponentPane extends WebPanel
{
    private final List<ComponentReorderListener> listeners = new ArrayList<ComponentReorderListener> ( 1 );

    protected final WebPanel container;
    protected final Map<Component, WebSelectablePanel> components = new LinkedHashMap<Component, WebSelectablePanel> ();

    protected boolean reorderingAllowed = false;
    protected boolean showReorderGrippers = true;
    protected boolean upDownHotkeysAllowed = true;
    protected boolean leftRightHotkeysAllowed = false;

    public WebComponentPane ()
    {
        super ( StyleId.componentpane );

        // Elements layout
        container = new WebPanel ();
        container.setLayout ( new ComponentPanelLayout () );
        add ( container, BorderLayout.CENTER );

        // Previous action hotkeys
        final HotkeyRunnable prevAction = new HotkeyRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
            {
                if ( upDownHotkeysAllowed && Hotkey.UP.isTriggered ( e ) || leftRightHotkeysAllowed && Hotkey.LEFT.isTriggered ( e ) )
                {
                    final int index = getFocusedElementIndex ();
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
        final HotkeyRunnable nextAction = new HotkeyRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
            {
                if ( upDownHotkeysAllowed && Hotkey.DOWN.isTriggered ( e ) || leftRightHotkeysAllowed && Hotkey.RIGHT.isTriggered ( e ) )
                {
                    final int index = getFocusedElementIndex ();
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

    public WebPanel getContainer ()
    {
        return container;
    }

    public ComponentPanelLayout getContainerLayout ()
    {
        return ( ComponentPanelLayout ) container.getLayout ();
    }

    public boolean isUpDownHotkeysAllowed ()
    {
        return upDownHotkeysAllowed;
    }

    public void setUpDownHotkeysAllowed ( final boolean upDownHotkeysAllowed )
    {
        this.upDownHotkeysAllowed = upDownHotkeysAllowed;
    }

    public boolean isLeftRightHotkeysAllowed ()
    {
        return leftRightHotkeysAllowed;
    }

    public void setLeftRightHotkeysAllowed ( final boolean leftRightHotkeysAllowed )
    {
        this.leftRightHotkeysAllowed = leftRightHotkeysAllowed;
    }

    public WebSelectablePanel addElement ( final Component component )
    {
        // Ignore existing component insert
        if ( components.containsKey ( component ) )
        {
            return components.get ( component );
        }

        // Creating view for component
        final WebSelectablePanel element = new WebSelectablePanel ( this );
        element.add ( component, BorderLayout.CENTER );

        // todo Fix this workaround and check other layouts for that problem
        // String is needed to invoke proper layout method
        container.add ( element, "" );

        // Saving view for component
        components.put ( component, element );

        return element;
    }

    public void removeElement ( final int index )
    {
        removeElement ( getElement ( index ) );
    }

    public void removeElement ( final WebSelectablePanel element )
    {
        for ( final Component component : components.keySet () )
        {
            if ( components.get ( component ) == element )
            {
                removeElement ( component );
                break;
            }
        }
    }

    public void removeElement ( final Component component )
    {
        // Removing actual element
        final WebSelectablePanel element = components.get ( component );
        container.remove ( element );

        // Removing data
        components.remove ( component );
    }

    public int getElementCount ()
    {
        return components.size ();
    }

    public WebSelectablePanel getElement ( final int index )
    {
        return ( WebSelectablePanel ) getContainerLayout ().getComponent ( index );
    }

    public WebSelectablePanel getFocusedElement ()
    {
        for ( final Component component : getContainerLayout ().getComponents () )
        {
            final WebSelectablePanel selectablePanel = ( WebSelectablePanel ) component;
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

    public void focusElement ( final int index )
    {
        getElement ( index ).transferFocus ();
    }

    public boolean isReorderingAllowed ()
    {
        return reorderingAllowed;
    }

    public void setReorderingAllowed ( final boolean reorderingAllowed )
    {
        this.reorderingAllowed = reorderingAllowed;
    }

    public boolean isShowReorderGrippers ()
    {
        return showReorderGrippers;
    }

    public void setShowReorderGrippers ( final boolean showReorderGrippers )
    {
        this.showReorderGrippers = showReorderGrippers;
    }

    public void addComponentReorderListener ( final ComponentReorderListener listener )
    {
        listeners.add ( listener );
    }

    public void removeComponentReorderListener ( final ComponentReorderListener listener )
    {
        listeners.remove ( listener );
    }

    public void fireComponentOrderChanged ( final Component component, final int oldIndex, final int newIndex )
    {
        for ( final ComponentReorderListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.componentOrderChanged ( component, oldIndex, newIndex );
        }
    }
}