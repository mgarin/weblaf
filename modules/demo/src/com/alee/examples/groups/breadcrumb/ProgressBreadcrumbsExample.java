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

package com.alee.examples.groups.breadcrumb;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.breadcrumb.BreadcrumbElement;
import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbToggleButton;
import com.alee.extended.panel.GroupPanel;
import com.alee.global.StyleConstants;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.ComponentUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 27.06.12 Time: 17:35
 */

public class ProgressBreadcrumbsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Progress breadcrumbs";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled progress breadcrumbs";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Decorated breadcrumb
        final WebBreadcrumb breadcrumb1 = new WebBreadcrumb ( true );
        fillBreadcrumb ( breadcrumb1 );
        installFakeProgressTimer ( breadcrumb1 );

        // Undecorated breadcrumb
        final WebBreadcrumb breadcrumb2 = new WebBreadcrumb ( false );
        fillBreadcrumb ( breadcrumb2 );
        installFakeProgressTimer ( breadcrumb2 );

        return new GroupPanel ( 4, false, breadcrumb1, breadcrumb2 );
    }

    private void fillBreadcrumb ( final WebBreadcrumb b )
    {
        // Sample breadcrumb data
        final ImageIcon icon = loadIcon ( "icon.png" );
        b.add ( new WebBreadcrumbToggleButton ( "img1.png", icon ) );
        b.add ( new WebBreadcrumbToggleButton ( "img2.png", icon ) );
        b.add ( new WebBreadcrumbToggleButton ( "img3.png", icon ) );
        b.add ( new WebBreadcrumbToggleButton ( "img4.png", icon ) );
        SwingUtils.groupButtons ( b );
    }

    private void installFakeProgressTimer ( final WebBreadcrumb breadcrumb )
    {
        // Progress updater
        ComponentUpdater.install ( breadcrumb, "ProgressBreadcrumbsExample.fakeProgressTimer", StyleConstants.avgAnimationDelay,
                new ActionListener ()
                {
                    private int run = 0;
                    private int element = 0;

                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        // Currently updated element
                        final BreadcrumbElement be = getElement ( element );

                        // Calculating progress changes
                        final float progress = be.getProgress ();
                        if ( progress >= 1f )
                        {
                            if ( run % 2 != 0 )
                            {
                                // Clearing previous element progress on odd runs
                                be.setProgress ( 0f );
                                be.setShowProgress ( false );
                            }
                            else
                            {
                                // Saving progress on even runs
                                be.setProgress ( 1f );
                                be.setShowProgress ( true );
                            }

                            if ( element < 3 )
                            {
                                // Still the same run
                                element = element + 1;
                            }
                            else
                            {
                                // New run starts
                                element = 0;
                                run++;

                                // Clearing progress on odd run
                                if ( run % 2 != 0 )
                                {
                                    for ( int i = 0; i <= 3; i++ )
                                    {
                                        final BreadcrumbElement el = getElement ( i );
                                        el.setProgress ( 0f );
                                        el.setShowProgress ( false );
                                    }
                                }
                            }
                        }
                        else
                        {
                            // Updating progress
                            if ( !be.isShowProgress () )
                            {
                                be.setShowProgress ( true );
                            }
                            be.setProgress ( progress + 0.01f );
                        }
                    }

                    private BreadcrumbElement getElement ( final int element )
                    {
                        return ( BreadcrumbElement ) breadcrumb.getComponent ( element );
                    }
                }
        );
    }
}