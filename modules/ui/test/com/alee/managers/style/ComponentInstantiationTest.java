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

package com.alee.managers.style;

import com.alee.extended.canvas.WebCanvas;
import com.alee.extended.image.WebImage;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.data.Dictionary;
import com.alee.painter.decoration.DecorationState;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.SystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

/**
 * Set of JUnit tests for various components instantiation.
 *
 * @author Mikle Garin
 */
@FixMethodOrder ( MethodSorters.JVM )
public final class ComponentInstantiationTest
{
    /**
     * todo 1. Add tests for all components covered in {@link StyleManager#initializeDescriptors()}
     */

    /**
     * Initializes {@link WebLookAndFeel}.
     */
    @BeforeClass
    public static void initialize ()
    {
        if ( !SystemUtils.isHeadlessEnvironment () )
        {
            CoreSwingUtils.invokeAndWait ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    WebLookAndFeel.setForceSingleEventsThread ( true );
                    WebLookAndFeel.install ();
                    LanguageManager.addDictionary ( new Dictionary ( ComponentInstantiationTest.class, "resources/language.xml" ) );
                }
            } );
        }
    }

    /**
     * Tests {@link WebCanvas} instantiation.
     */
    @Test
    public void canvas ()
    {
        if ( !SystemUtils.isHeadlessEnvironment () )
        {
            CoreSwingUtils.invokeAndWait ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    new WebCanvas ();
                    new WebCanvas ( DecorationState.hover );
                    new WebCanvas ( StyleId.canvasGripperC );
                    new WebCanvas ( StyleId.canvasGripperC, DecorationState.hover );
                }
            } );
        }
    }

    /**
     * Tests {@link WebImage} instantiation.
     */
    @Test
    public void image ()
    {
        if ( !SystemUtils.isHeadlessEnvironment () )
        {
            CoreSwingUtils.invokeAndWait ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    new WebImage ();
                    new WebImage ( ComponentInstantiationTest.class, "resources/image.png" );
                    new WebImage ( ComponentInstantiationTest.class.getResource ( "resources/image.png" ) );
                    new WebImage ( icon16 () );
                    new WebImage ( imageIcon16 () );
                    new WebImage ( image16 () );
                    new WebImage ( renderedImage16 () );
                    new WebImage ( bufferedImage16 () );
                    new WebImage ( StyleId.imagePortrait );
                    new WebImage ( StyleId.imagePortrait, ComponentInstantiationTest.class, "resources/image.png" );
                    new WebImage ( StyleId.imagePortrait, ComponentInstantiationTest.class.getResource ( "resources/image.png" ) );
                    new WebImage ( StyleId.imagePortrait, icon16 () );
                    new WebImage ( StyleId.imagePortrait, imageIcon16 () );
                    new WebImage ( StyleId.imagePortrait, image16 () );
                    new WebImage ( StyleId.imagePortrait, renderedImage16 () );
                    new WebImage ( StyleId.imagePortrait, bufferedImage16 () );
                }
            } );
        }
    }

    /**
     * Tests {@link WebLabel} instantiation.
     */
    @Test
    public void label ()
    {
        if ( !SystemUtils.isHeadlessEnvironment () )
        {
            CoreSwingUtils.invokeAndWait ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    new WebLabel ();
                    new WebLabel ( WebLabel.CENTER );
                    new WebLabel ( icon16 () );
                    new WebLabel ( icon16 (), WebLabel.CENTER );
                    new WebLabel ( "Sample text" );
                    new WebLabel ( "weblaf.test.sample" );
                    new WebLabel ( "weblaf.test.sample", WebLabel.CENTER );
                    new WebLabel ( "weblaf.test.sample", icon16 () );
                    new WebLabel ( "weblaf.test.sample", icon16 (), WebLabel.CENTER );
                    new WebLabel ( StyleId.labelShadow );
                    new WebLabel ( StyleId.labelShadow, WebLabel.CENTER );
                    new WebLabel ( StyleId.labelShadow, icon16 () );
                    new WebLabel ( StyleId.labelShadow, icon16 (), WebLabel.CENTER );
                    new WebLabel ( StyleId.labelShadow, "Sample text" );
                    new WebLabel ( StyleId.labelShadow, "weblaf.test.sample" );
                    new WebLabel ( StyleId.labelShadow, "weblaf.test.sample", WebLabel.CENTER );
                    new WebLabel ( StyleId.labelShadow, "weblaf.test.sample", icon16 () );
                    new WebLabel ( StyleId.labelShadow, "weblaf.test.sample", icon16 (), WebLabel.CENTER );
                }
            } );
        }
    }

    /**
     * Returns {@link Icon} that can be used for tests.
     *
     * @return {@link Icon} that can be used for tests
     */
    private static Icon icon16 ()
    {
        return WebLookAndFeel.getIcon ( 16 );
    }

    /**
     * Returns {@link ImageIcon} that can be used for tests.
     *
     * @return {@link ImageIcon} that can be used for tests
     */
    private static ImageIcon imageIcon16 ()
    {
        return WebLookAndFeel.getIcon ( 16 );
    }

    /**
     * Returns {@link Image} that can be used for tests.
     *
     * @return {@link Image} that can be used for tests
     */
    private static Image image16 ()
    {
        return WebLookAndFeel.getIcon ( 16 ).getImage ();
    }

    /**
     * Returns {@link RenderedImage} that can be used for tests.
     *
     * @return {@link RenderedImage} that can be used for tests
     */
    private static RenderedImage renderedImage16 ()
    {
        return ImageUtils.getBufferedImage ( WebLookAndFeel.getIcon ( 16 ) );
    }

    /**
     * Returns {@link BufferedImage} that can be used for tests.
     *
     * @return {@link BufferedImage} that can be used for tests
     */
    private static BufferedImage bufferedImage16 ()
    {
        return ImageUtils.getBufferedImage ( WebLookAndFeel.getIcon ( 16 ) );
    }

    /**
     * Destroys {@link WebLookAndFeel}.
     */
    @AfterClass
    public static void destroy ()
    {
        if ( !SystemUtils.isHeadlessEnvironment () )
        {
            CoreSwingUtils.invokeAndWait ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    WebLookAndFeel.uninstall ();
                }
            } );
        }
    }
}