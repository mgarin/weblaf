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

package com.alee.extended.window;

import com.alee.extended.panel.BorderPanel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 01.11.11 Time: 15:16
 */

public class TestFrame extends WebFrame
{
    public TestFrame ( Component component )
    {
        this ( new BorderLayout (), component );
    }

    public TestFrame ( Component component, int margin )
    {
        this ( component, margin, margin, margin, margin );
    }

    public TestFrame ( Component component, int top, int left, int bottom, int right )
    {
        this ( component, new Insets ( top, left, bottom, right ) );
    }

    public TestFrame ( Component component, Insets margin )
    {
        this ( new BorderLayout (), component, BorderLayout.CENTER, margin );
    }

    public TestFrame ( LayoutManager layout, Component component )
    {
        this ( layout, component, null, null );
    }

    public TestFrame ( LayoutManager layout, Component component, int margin )
    {
        this ( layout, component, new Insets ( margin, margin, margin, margin ) );
    }

    public TestFrame ( LayoutManager layout, Component component, int top, int left, int bottom, int right )
    {
        this ( layout, component, new Insets ( top, left, bottom, right ) );
    }

    public TestFrame ( LayoutManager layout, Component component, Insets margin )
    {
        this ( layout, component, null, margin );
    }

    public TestFrame ( LayoutManager layout, Component component, Object constraints, Insets margin )
    {
        super ( ReflectUtils.getClassName ( component.getClass () ) + " (" + SystemUtils.getOsName () + " " + SystemUtils.getOsArch () +
                ", JRE " + SystemUtils.getJavaVersionString () + " " + SystemUtils.getJreArch () + "-bit)" );

        setIconImages ( WebLookAndFeel.getImages () );

        getContentPane ().setLayout ( layout );

        if ( margin != null )
        {
            if ( constraints != null )
            {
                getContentPane ().add ( new BorderPanel ( component, margin ), constraints );
            }
            else
            {
                getContentPane ().add ( new BorderPanel ( component, margin ) );
            }
        }
        else
        {
            if ( constraints != null )
            {
                getContentPane ().add ( component, constraints );
            }
            else
            {
                getContentPane ().add ( component );
            }
        }

        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        setResizable ( true );
        pack ();
        center ();
        setVisible ( true );
    }
}
