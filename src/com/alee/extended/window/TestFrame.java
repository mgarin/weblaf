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
 * This class provides a quick way to open frame with the specified content and some other settings.
 * It suits best for writing UI test applications.
 *
 * @author Mikle Garin
 */

public class TestFrame extends WebFrame
{
    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param component component to display
     * @return displayed test frame
     */
    public static TestFrame show ( Component component )
    {
        return new TestFrame ( component ).display ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param component component to display
     */
    public TestFrame ( Component component )
    {
        this ( new BorderLayout (), component );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param component component to display
     * @param margin    container margin
     * @return displayed test frame
     */
    public static TestFrame show ( Component component, int margin )
    {
        return new TestFrame ( component, margin ).display ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param component component to display
     * @param margin    container margin
     */
    public TestFrame ( Component component, int margin )
    {
        this ( component, margin, margin, margin, margin );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param component component to display
     * @param top       container top margin
     * @param left      container left margin
     * @param bottom    container bottom margin
     * @param right     container right margin
     * @return displayed test frame
     */
    public static TestFrame show ( Component component, int top, int left, int bottom, int right )
    {
        return new TestFrame ( component, top, left, bottom, right ).display ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param component component to display
     * @param top       container top margin
     * @param left      container left margin
     * @param bottom    container bottom margin
     * @param right     container right margin
     */
    public TestFrame ( Component component, int top, int left, int bottom, int right )
    {
        this ( component, new Insets ( top, left, bottom, right ) );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param component component to display
     * @param margin    container margin
     * @return displayed test frame
     */
    public static TestFrame show ( Component component, Insets margin )
    {
        return new TestFrame ( component, margin ).display ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param component component to display
     * @param margin    container margin
     */
    public TestFrame ( Component component, Insets margin )
    {
        this ( new BorderLayout (), component, BorderLayout.CENTER, margin );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @return displayed test frame
     */
    public static TestFrame show ( LayoutManager layout, Component component )
    {
        return new TestFrame ( layout, component ).display ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     */
    public TestFrame ( LayoutManager layout, Component component )
    {
        this ( layout, component, null, null );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param margin    container margin
     * @return displayed test frame
     */
    public static TestFrame show ( LayoutManager layout, Component component, int margin )
    {
        return new TestFrame ( layout, component, margin ).display ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param margin    container margin
     */
    public TestFrame ( LayoutManager layout, Component component, int margin )
    {
        this ( layout, component, new Insets ( margin, margin, margin, margin ) );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param top       container top margin
     * @param left      container left margin
     * @param bottom    container bottom margin
     * @param right     container right margin
     * @return displayed test frame
     */
    public static TestFrame show ( LayoutManager layout, Component component, int top, int left, int bottom, int right )
    {
        return new TestFrame ( layout, component, top, left, bottom, right ).display ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param top       container top margin
     * @param left      container left margin
     * @param bottom    container bottom margin
     * @param right     container right margin
     */
    public TestFrame ( LayoutManager layout, Component component, int top, int left, int bottom, int right )
    {
        this ( layout, component, new Insets ( top, left, bottom, right ) );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param margin    container margin
     * @return displayed test frame
     */
    public static TestFrame show ( LayoutManager layout, Component component, Insets margin )
    {
        return new TestFrame ( layout, component, margin ).display ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param margin    container margin
     */
    public TestFrame ( LayoutManager layout, Component component, Insets margin )
    {
        this ( layout, component, null, margin );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout      container layout
     * @param component   component to display
     * @param constraints component layout constraints
     * @param margin      container margin
     * @return displayed test frame
     */
    public static TestFrame show ( LayoutManager layout, Component component, Object constraints, Insets margin )
    {
        return new TestFrame ( layout, component, constraints, margin ).display ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout      container layout
     * @param component   component to display
     * @param constraints component layout constraints
     * @param margin      container margin
     */
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
    }

    /**
     * Displays and returns test frame.
     *
     * @return test frame
     */
    public TestFrame display ()
    {
        setVisible ( true );
        return this;
    }
}