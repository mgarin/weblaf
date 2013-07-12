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

import com.alee.laf.label.WebLabel;
import com.alee.managers.language.LanguageMethods;
import com.alee.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This custom component provides a link functionality together with default label options.
 * Link could be an internet address, e-mail or some file. You can also specify a custom link action by passing a runnable.
 * Link can also be styled by default using custom L&F icons and default naming.
 *
 * @author Mikle Garin
 * @since 1.3
 */

public class WebLinkLabel extends WebLabel implements LanguageMethods
{
    /**
     * Internet address link icon.
     */
    public static final ImageIcon LINK_ICON = new ImageIcon ( WebLinkLabel.class.getResource ( "icons/link.png" ) );

    /**
     * E-mail link icon.
     */
    public static final ImageIcon EMAIL_ICON = new ImageIcon ( WebLinkLabel.class.getResource ( "icons/email.png" ) );

    /**
     * Link activation listeners.
     */
    private List<ActionListener> actionListeners = new ArrayList<ActionListener> ();

    // Style settings
    private boolean highlight = WebLinkLabelStyle.highlight;
    private boolean onPressAction = WebLinkLabelStyle.onPressAction;
    private boolean colorVisited = WebLinkLabelStyle.colorVisited;
    private Color foreground = WebLinkLabelStyle.foreground;
    private Color visitedForeground = WebLinkLabelStyle.visitedForeground;

    // Link settings
    private Runnable link = null;
    private String actualText = "";

    // Runtime variables
    private boolean mouseover = false;
    private boolean visitedOnce = false;

    public WebLinkLabel ()
    {
        super ();
        initializeSettings ();
    }

    public WebLinkLabel ( Icon image )
    {
        super ( image );
        initializeSettings ();
    }

    public WebLinkLabel ( Icon image, int horizontalAlignment )
    {
        super ( image, horizontalAlignment );
        initializeSettings ();
    }

    public WebLinkLabel ( String text )
    {
        super ( text );
        setText ( text );
        initializeSettings ();
    }

    public WebLinkLabel ( String text, int horizontalAlignment )
    {
        super ( text, horizontalAlignment );
        setText ( text );
        initializeSettings ();
    }

    public WebLinkLabel ( String text, Icon icon, int horizontalAlignment )
    {
        super ( text, icon, horizontalAlignment );
        setText ( text );
        initializeSettings ();
    }

    /**
     * Link label settings initialization
     */

    private void initializeSettings ()
    {
        setCursor ( Cursor.getPredefinedCursor ( Cursor.HAND_CURSOR ) );
        updateForeground ();

        MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            public void mousePressed ( MouseEvent e )
            {
                if ( onPressAction )
                {
                    performAction ( e );
                }
            }

            public void mouseReleased ( MouseEvent e )
            {
                if ( !onPressAction && SwingUtils.size ( WebLinkLabel.this ).contains ( e.getPoint () ) )
                {
                    performAction ( e );
                }
            }

            private void performAction ( MouseEvent e )
            {
                if ( isEnabled () && SwingUtilities.isLeftMouseButton ( e ) )
                {
                    visitedOnce = true;
                    updateForeground ();

                    if ( link != null )
                    {
                        new Thread ( new Runnable ()
                        {
                            public void run ()
                            {
                                link.run ();
                            }
                        } ).start ();
                    }

                    fireActionPerformed ();
                }
            }

            public void mouseEntered ( MouseEvent e )
            {
                if ( isEnabled () )
                {
                    mouseover = true;
                    updateText ();
                }
            }

            public void mouseExited ( MouseEvent e )
            {
                if ( highlight )
                {
                    mouseover = false;
                    updateText ();
                }
            }

            public void mouseDragged ( MouseEvent e )
            {
                if ( highlight )
                {
                    mouseover = false;
                    updateText ();
                }
            }
        };
        addMouseListener ( mouseAdapter );
        addMouseMotionListener ( mouseAdapter );
    }

    /**
     * Link foreground settings
     */

    public Color getUnvisitedForeground ()
    {
        return foreground;
    }

    public void setUnvisitedForeground ( Color foreground )
    {
        setForeground ( foreground );
    }

    public void setForeground ( Color foreground )
    {
        this.foreground = foreground;
        updateForeground ();
    }

    public Color getVisitedForeground ()
    {
        return visitedForeground;
    }

    public void setVisitedForeground ( Color visitedForeground )
    {
        this.visitedForeground = visitedForeground;
        updateForeground ();
    }

    private void updateForeground ()
    {
        WebLinkLabel.super.setForeground ( colorVisited && visitedOnce ? visitedForeground : foreground );
    }

    /**
     * Link text settings
     */

    public String getActualText ()
    {
        return actualText;
    }

    public void setText ( String text )
    {
        this.actualText = text;
        updateText ();
    }

    private void updateText ()
    {
        if ( mouseover && highlight )
        {
            if ( !actualText.trim ().equals ( "" ) )
            {
                final String text;
                if ( HtmlUtils.hasHtml ( actualText ) )
                {
                    text = HtmlUtils.getContent ( actualText );
                }
                else
                {
                    text = actualText.replaceAll ( "<", "&lt;" ).replaceAll ( ">", "&gt;" );
                }
                WebLinkLabel.super.setText ( "<html><u>" + text + "</u></html>" );
            }
            else
            {
                WebLinkLabel.super.setText ( "" );
            }
        }
        else
        {
            WebLinkLabel.super.setText ( actualText );
        }
    }

    /**
     * Link settings
     */

    public boolean isVisitedOnce ()
    {
        return visitedOnce;
    }

    public Runnable getLink ()
    {
        return link;
    }

    public void setLink ( Runnable link )
    {
        this.link = link;
    }

    public void setLink ( String text, Runnable link )
    {
        this.link = link;
    }

    public void setLink ( String address )
    {
        setLink ( address, true );
    }

    public void setLink ( String address, boolean setupView )
    {
        setLink ( address, address, setupView );
    }

    public void setLink ( String text, String address )
    {
        setLink ( text, address, true );
    }

    public void setLink ( String text, String address, boolean setupView )
    {
        setLink ( text, createAddressLink ( address ), setupView );
    }

    public void setLink ( String text, Runnable link, boolean setupView )
    {
        if ( setupView )
        {
            setIcon ( LINK_ICON );
            setText ( text );
        }
        this.link = link;
    }

    public void setEmailLink ( String email )
    {
        setEmailLink ( email, true );
    }

    public void setEmailLink ( String email, boolean setupView )
    {
        setEmailLink ( email, email, setupView );
    }

    public void setEmailLink ( String text, String email )
    {
        setEmailLink ( text, email, true );
    }

    public void setEmailLink ( String text, String email, boolean setupView )
    {
        if ( setupView )
        {
            setIcon ( EMAIL_ICON );
            setText ( email );
        }
        this.link = createEmailLink ( email );
    }

    public void setFileLink ( File file )
    {
        setFileLink ( file, true );
    }

    public void setFileLink ( File file, boolean setupView )
    {
        setFileLink ( FileUtils.getDisplayFileName ( file ), file, setupView );
    }

    public void setFileLink ( String text, File file )
    {
        setFileLink ( text, file, true );
    }

    public void setFileLink ( String text, File file, boolean setupView )
    {
        if ( setupView )
        {
            setIcon ( FileUtils.getFileIcon ( file ) );
            setText ( text );
        }
        this.link = createFileLink ( file );
    }

    public boolean isHighlight ()
    {
        return highlight;
    }

    public void setHighlight ( boolean highlight )
    {
        this.highlight = highlight;
        updateText ();
    }

    public boolean isOnPressAction ()
    {
        return onPressAction;
    }

    public void setOnPressAction ( boolean onPressAction )
    {
        this.onPressAction = onPressAction;
    }

    /**
     * Link action listeners
     */

    public void addActionListener ( ActionListener actionListener )
    {
        actionListeners.add ( actionListener );
    }

    public void removeActionListener ( ActionListener actionListener )
    {
        actionListeners.remove ( actionListener );
    }

    private void fireActionPerformed ()
    {
        ActionEvent event = new ActionEvent ( this, 0, "Link opened" );
        for ( ActionListener actionListener : CollectionUtils.copy ( actionListeners ) )
        {
            actionListener.actionPerformed ( event );
        }
    }

    private static Runnable createAddressLink ( final String address )
    {
        if ( address != null )
        {
            return new Runnable ()
            {
                public void run ()
                {
                    try
                    {
                        WebUtils.browseSite ( address );
                    }
                    catch ( Throwable e )
                    {
                        //
                    }
                }
            };
        }
        else
        {
            return null;
        }
    }

    private static Runnable createEmailLink ( final String email )
    {
        if ( email != null )
        {
            return new Runnable ()
            {
                public void run ()
                {
                    try
                    {
                        WebUtils.writeEmail ( email );
                    }
                    catch ( Throwable e )
                    {
                        //
                    }
                }
            };
        }
        else
        {
            return null;
        }
    }

    private static Runnable createFileLink ( final File file )
    {
        if ( file != null )
        {
            return new Runnable ()
            {
                public void run ()
                {
                    try
                    {
                        WebUtils.openFile ( file );
                    }
                    catch ( Throwable e )
                    {
                        //
                    }
                }
            };
        }
        else
        {
            return null;
        }
    }
}