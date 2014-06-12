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

package com.alee.extended.label;

import com.alee.extended.layout.TableLayout;
import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.managers.settings.SettingsManager;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 16.09.11 Time: 13:45
 */

public class WebTipLabel extends JComponent implements SwingConstants, ActionListener
{
    public static final ImageIcon HIDE_ICON = new ImageIcon ( WebTipLabel.class.getResource ( "icons/hide.png" ) );
    public static final ImageIcon HIDE_DARK_ICON = new ImageIcon ( WebTipLabel.class.getResource ( "icons/hide_dark.png" ) );

    public static final String SETTINGS_GROUP = "WebTipLabel";

    private String id;

    private int opacity = 100;
    private WebTimer timer = null;

    private final WebButton hideButton;
    private final WebLabel helpLabel;

    public WebTipLabel ( final String id )
    {
        this ( id, "" );
    }

    public WebTipLabel ( final String id, final String text )
    {
        this ( id, text, null );
    }

    public WebTipLabel ( final String id, final int horizontalAlignment )
    {
        this ( id, "", horizontalAlignment );
    }

    public WebTipLabel ( final String id, final Icon icon )
    {
        this ( id, "", icon );
    }

    public WebTipLabel ( final String id, final String text, final Icon icon )
    {
        this ( id, text, icon, LEFT );
    }

    public WebTipLabel ( final String id, final String text, final int horizontalAlignment )
    {
        this ( id, text, null, horizontalAlignment );
    }

    public WebTipLabel ( final String id, final String text, final Icon icon, final int horizontalAlignment )
    {
        super ();

        this.id = id;

        SwingUtils.setOrientation ( this );
        setFocusable ( false );
        setOpaque ( false );

        final TableLayout layout = new TableLayout ( new double[][]{
                horizontalAlignment == LEFT ? new double[]{ TableLayout.PREFERRED, TableLayout.PREFERRED } :
                        ( horizontalAlignment == CENTER ?
                                new double[]{ TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL } :
                                new double[]{ TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED } ),
                { TableLayout.PREFERRED } } );
        layout.setHGap ( 4 );
        setLayout ( layout );

        hideButton = new WebButton ( HIDE_ICON );
        hideButton.setRolloverIcon ( HIDE_DARK_ICON );
        hideButton.setUndecorated ( true );
        hideButton.addActionListener ( WebTipLabel.this );

        helpLabel = new WebLabel ();
        helpLabel.setIcon ( icon );
        helpLabel.setText ( text );
        helpLabel.setForeground ( Color.DARK_GRAY );
        SwingUtils.changeFontSize ( helpLabel, -1 );

        if ( horizontalAlignment == LEFT )
        {
            add ( hideButton, "0,0" );
            add ( helpLabel, "1,0" );
        }
        else
        {
            add ( hideButton, "1,0" );
            add ( helpLabel, "2,0" );
        }
    }

    @Override
    public void actionPerformed ( final ActionEvent e )
    {
        hideButton.setEnabled ( false );
        timer = new WebTimer ( "WebTipLabel.animator", StyleConstants.animationDelay, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( opacity > 0 )
                {
                    opacity -= 10;
                    WebTipLabel.this.repaint ();
                }
                else
                {
                    final Container parent = WebTipLabel.this.getParent ();
                    parent.remove ( WebTipLabel.this );

                    if ( parent instanceof JComponent )
                    {
                        ( ( JComponent ) parent ).revalidate ();
                    }
                    else
                    {
                        parent.invalidate ();
                    }
                    parent.repaint ();

                    // Saving closed state
                    SettingsManager.set ( SETTINGS_GROUP, id, false );

                    timer.stop ();
                }
            }
        } );
        timer.start ();
    }

    @Override
    public void paint ( final Graphics g )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, ( float ) opacity / 100, opacity < 100 );

        super.paint ( g );

        GraphicsUtils.restoreComposite ( g2d, oc );
        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    public String getId ()
    {
        return id;
    }

    public void setId ( final String id )
    {
        this.id = id;
    }

    public void setText ( final String text )
    {
        helpLabel.setText ( text );
    }

    public String getText ()
    {
        return helpLabel.getText ();
    }

    public void setIcon ( final Icon icon )
    {
        helpLabel.setIcon ( icon );
    }

    public Icon getIcon ()
    {
        return helpLabel.getIcon ();
    }

    public void setVerticalAlignment ( final int alignment )
    {
        helpLabel.setVerticalAlignment ( alignment );
    }

    public void setHorizontalAlignment ( final int alignment )
    {
        helpLabel.setHorizontalAlignment ( alignment );
    }

    public void setVerticalTextPosition ( final int textPosition )
    {
        helpLabel.setVerticalTextPosition ( textPosition );
    }

    public void setHorizontalTextPosition ( final int textPosition )
    {
        helpLabel.setHorizontalTextPosition ( textPosition );
    }

    public WebLabel getHelpLabel ()
    {
        return helpLabel;
    }

    public WebButton getHideButton ()
    {
        return hideButton;
    }
}
