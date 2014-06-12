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

import com.alee.extended.layout.ToolbarLayout;
import com.alee.global.StyleConstants;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.toolbar.WhiteSpace;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 10.10.11 Time: 18:40
 */

public class WebStatusBar extends JComponent implements LanguageContainerMethods
{
    private Color topBgColor = WebStatusBarStyle.topBgColor;
    private Color bottomBgColor = WebStatusBarStyle.bottomBgColor;

    private Insets margin = WebStatusBarStyle.margin;
    private boolean undecorated = WebStatusBarStyle.undecorated;

    public WebStatusBar ()
    {
        super ();
        SwingUtils.setOrientation ( this );
        setLayout ( new ToolbarLayout () );
        updateBorder ();
    }

    public boolean isUndecorated ()
    {
        return undecorated;
    }

    public void setUndecorated ( boolean undecorated )
    {
        this.undecorated = undecorated;
        updateBorder ();
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }

    public void setMargin ( int top, int left, int bottom, int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setMargin ( Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    public void addSeparator ()
    {
        addSeparator ( ToolbarLayout.START );
    }

    public void addSeparatorToEnd ()
    {
        addSeparator ( ToolbarLayout.END );
    }

    public void addSeparator ( String constraints )
    {
        add ( createSeparator (), constraints );
    }

    public void addToMiddle ( Component component )
    {
        add ( component, ToolbarLayout.MIDDLE );
    }

    public void addFill ( Component component )
    {
        add ( component, ToolbarLayout.FILL );
    }

    public void addToEnd ( Component component )
    {
        add ( component, ToolbarLayout.END );
    }

    public void addSpacing ()
    {
        addSpacing ( StyleConstants.contentSpacing );
    }

    public void addSpacing ( int spacing )
    {
        addSpacing ( spacing, ToolbarLayout.START );
    }

    public void addSpacingToEnd ()
    {
        addSpacingToEnd ( StyleConstants.contentSpacing );
    }

    public void addSpacingToEnd ( int spacing )
    {
        addSpacing ( spacing, ToolbarLayout.END );
    }

    public void addSpacing ( int spacing, String constrain )
    {
        add ( new WhiteSpace ( spacing ), constrain );
    }

    private void updateBorder ()
    {
        setBorder ( BorderFactory.createEmptyBorder ( margin.top + ( undecorated ? 0 : 1 ), margin.left, margin.bottom, margin.right ) );
    }

    public ToolbarLayout getActualLayout ()
    {
        return ( ToolbarLayout ) getLayout ();
    }

    @Override
    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        if ( !undecorated )
        {
            Graphics2D g2d = ( Graphics2D ) g;

            g2d.setPaint ( new GradientPaint ( 0, 0, topBgColor, 0, getHeight (), bottomBgColor ) );
            g2d.fillRect ( 0, 0, getWidth (), getHeight () );

            g2d.setPaint ( StyleConstants.darkBorderColor );
            g2d.drawLine ( 0, 0, getWidth () - 1, 0 );
        }
    }

    private static WebSeparator createSeparator ()
    {
        return new WebSeparator ( WebSeparator.VERTICAL );
    }

    /**
     * Language container methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageContainerKey ( String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }
}
