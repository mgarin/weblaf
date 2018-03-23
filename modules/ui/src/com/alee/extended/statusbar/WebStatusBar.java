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
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.toolbar.WhiteSpace;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;

import java.awt.*;

/**
 * Implementation of status bar panel.
 * It is a container that is usually used at the bottom side of the application UI and contains some status information.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see StatusBarDescriptor
 * @see WStatusBarUI
 * @see WebStatusBarUI
 * @see IStatusBarPainter
 * @see StatusBarPainter
 * @see WebContainer
 */

public class WebStatusBar extends WebContainer<WebStatusBar, WStatusBarUI>
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
        setLayout ( new StatusBarLayout () );
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
        add ( component, StatusBarLayout.MIDDLE );
    }

    /**
     * Adds component to the fill status bar area.
     *
     * @param component component to add
     */
    public void addFill ( final Component component )
    {
        add ( component, StatusBarLayout.FILL );
    }

    /**
     * Adds component to the end of the status bar.
     *
     * @param component component to add
     */
    public void addToEnd ( final Component component )
    {
        add ( component, StatusBarLayout.END );
    }

    /**
     * Adds separator to the start of the status bar.
     */
    public void addSeparator ()
    {
        addSeparator ( StatusBarLayout.START );
    }

    /**
     * Adds separator to the end of the status bar.
     */
    public void addSeparatorToEnd ()
    {
        addSeparator ( StatusBarLayout.END );
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
     * Adds spacing between components.
     */
    public void addSpacing ()
    {
        addSpacing ( 2 );
    }

    /**
     * Adds spacing between components.
     *
     * @param spacing spacing size
     */
    public void addSpacing ( final int spacing )
    {
        addSpacing ( spacing, StatusBarLayout.START );
    }

    /**
     * Adds spacing between components at the end.
     */
    public void addSpacingToEnd ()
    {
        addSpacingToEnd ( 2 );
    }

    /**
     * Adds spacing between components at the end.
     *
     * @param spacing spacing size
     */
    public void addSpacingToEnd ( final int spacing )
    {
        addSpacing ( spacing, StatusBarLayout.END );
    }

    /**
     * Adds spacing between components at the specified constraints.
     *
     * @param spacing     spacing size
     * @param constraints layout constraints
     */
    public void addSpacing ( final int spacing, final String constraints )
    {
        // todo Add layout implementation instead of wasted component
        add ( new WhiteSpace ( spacing ), constraints );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the StatusBarUI object that renders this component
     */
    public WStatusBarUI getUI ()
    {
        return ( WStatusBarUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WStatusBarUI}
     */
    public void setUI ( final WStatusBarUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}