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

package com.alee.extended.statusbar;

import com.alee.extended.WebContainer;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleableComponent;

import javax.swing.*;
import java.awt.*;

/**
 * Implementation of status bar panel.
 * It is a container that is usually used at the bottom side of the application UI and contains some status information.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see WebContainer
 * @see WebStatusBarUI
 * @see StatusBarPainter
 */

public class WebStatusBar extends WebContainer<WebStatusBarUI,WebStatusBar>
{
    /**
     * Constructs new status bar.
     */
    public WebStatusBar ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new status bar.
     *
     * @param id style ID
     */
    public WebStatusBar ( final StyleId id )
    {
        super ();
        setLayout ( new ToolbarLayout () );
        updateUI ();
        setStyleId ( id );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.statusbar;
    }

    /**
     * Adds component to the middle status bar area.
     *
     * @param component component to add
     */
    public void addToMiddle ( final Component component )
    {
        add ( component, ToolbarLayout.MIDDLE );
    }

    /**
     * Adds component to the fill status bar area.
     *
     * @param component component to add
     */
    public void addFill ( final Component component )
    {
        add ( component, ToolbarLayout.FILL );
    }

    /**
     * Adds component to the end of the status bar.
     *
     * @param component component to add
     */
    public void addToEnd ( final Component component )
    {
        add ( component, ToolbarLayout.END );
    }

    /**
     * Adds separator to the start of the status bar.
     */
    public void addSeparator ()
    {
        addSeparator ( ToolbarLayout.START );
    }

    /**
     * Adds separator to the end of the status bar.
     */
    public void addSeparatorToEnd ()
    {
        addSeparator ( ToolbarLayout.END );
    }

    /**
     * Adds separator under the specified constaints.
     *
     * @param constraints layout constraints
     */
    public void addSeparator ( final String constraints )
    {
        add ( createSeparator (), constraints );
    }

    /**
     * Returns new status bar separator.
     *
     * @return new status bar separator
     */
    protected WebSeparator createSeparator ()
    {
        return new WebSeparator ( WebSeparator.VERTICAL );
    }

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the StatusBarUI object that renders this component
     */
    public StatusBarUI getUI ()
    {
        return ( StatusBarUI ) ui;
    }

    /**
     * Sets the L&amp;F object that renders this component.
     *
     * @param ui {@link com.alee.extended.statusbar.StatusBarUI}
     */
    public void setUI ( final StatusBarUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public WebStatusBarUI getWebUI ()
    {
        return ( WebStatusBarUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebStatusBarUI ) )
        {
            try
            {
                setUI ( ( WebStatusBarUI ) UIManager.getUI ( this ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebStatusBarUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public String getUIClassID ()
    {
        return StyleableComponent.statusbar.getUIClassID ();
    }
}