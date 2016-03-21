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
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.style.Bounds;
import com.alee.managers.style.StyleId;
import com.alee.utils.*;
import com.alee.utils.swing.AncestorAdapter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This custom component provides a link functionality together with default label options.
 * Link could be an internet address, e-mail or some file. You can also specify a custom link action by passing a runnable.
 * Link can also be styled by default using custom L&amp;F icons and default naming.
 *
 * @author Mikle Garin
 */

public class WebLinkLabel extends WebLabel
{
    /**
     * todo 1. Remodel WebLinkLabel to extend WebStyledLabel
     * todo 2. Replace HTML with proper styling
     */

    /**
     * Used icons.
     */
    public static final ImageIcon LINK_ICON = new ImageIcon ( WebLinkLabel.class.getResource ( "icons/link.png" ) );
    public static final ImageIcon EMAIL_ICON = new ImageIcon ( WebLinkLabel.class.getResource ( "icons/email.png" ) );

    /**
     * ExecutorService to limit simultaneously running threads.
     */
    protected ExecutorService executorService = Executors.newSingleThreadExecutor ();

    /**
     * Link activation listeners.
     */
    protected List<ActionListener> actionListeners = new ArrayList<ActionListener> ( 1 );

    /**
     * Style settings.
     */
    protected boolean highlight = WebLinkLabelStyle.highlight;
    protected boolean onPressAction = WebLinkLabelStyle.onPressAction;
    protected boolean highlightVisited = WebLinkLabelStyle.highlightVisited;
    protected Color foreground = WebLinkLabelStyle.foreground;
    protected Color visitedForeground = WebLinkLabelStyle.visitedForeground;

    /**
     * Link settings.
     */
    protected Runnable link;
    protected String actualText;

    /**
     * Runtime variables.
     */
    protected boolean mouseover = false;
    protected boolean visitedOnce = false;

    public WebLinkLabel ()
    {
        super ();
        initializeSettings ();
    }

    public WebLinkLabel ( final Icon image )
    {
        super ( image );
        initializeSettings ();
    }

    public WebLinkLabel ( final Icon image, final int horizontalAlignment )
    {
        super ( image, horizontalAlignment );
        initializeSettings ();
    }

    public WebLinkLabel ( final String text )
    {
        super ( text );
        initializeSettings ();
    }

    public WebLinkLabel ( final String text, final int horizontalAlignment )
    {
        super ( text, horizontalAlignment );
        initializeSettings ();
    }

    public WebLinkLabel ( final String text, final Icon icon )
    {
        super ( text, icon );
        initializeSettings ();
    }

    public WebLinkLabel ( final String text, final Icon icon, final int horizontalAlignment )
    {
        super ( text, icon, horizontalAlignment );
        initializeSettings ();
    }

    public WebLinkLabel ( final StyleId id )
    {
        super ( id );
        initializeSettings ();
    }

    public WebLinkLabel ( final StyleId id, final Icon image )
    {
        super ( id, image );
        initializeSettings ();
    }

    public WebLinkLabel ( final StyleId id, final Icon image, final int horizontalAlignment )
    {
        super ( id, image, horizontalAlignment );
        initializeSettings ();
    }

    public WebLinkLabel ( final StyleId id, final String text )
    {
        super ( id, text );
        initializeSettings ();
    }

    public WebLinkLabel ( final StyleId id, final String text, final int horizontalAlignment )
    {
        super ( id, text, horizontalAlignment );
        initializeSettings ();
    }

    public WebLinkLabel ( final StyleId id, final String text, final Icon icon )
    {
        super ( id, text, icon );
        initializeSettings ();
    }

    public WebLinkLabel ( final StyleId id, final String text, final Icon icon, final int horizontalAlignment )
    {
        super ( id, text, icon, horizontalAlignment );
        initializeSettings ();
    }

    /**
     * Link label settings initialization
     */

    protected void initializeSettings ()
    {
        setCursor ( Cursor.getPredefinedCursor ( Cursor.HAND_CURSOR ) );
        updateForeground ();

        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( onPressAction )
                {
                    performAction ( e );
                }
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                if ( !onPressAction && Bounds.padding.of ( WebLinkLabel.this ).contains ( e.getPoint () ) )
                {
                    performAction ( e );
                }
            }

            private void performAction ( final MouseEvent e )
            {
                if ( isEnabled () && SwingUtilities.isLeftMouseButton ( e ) )
                {
                    doClick ();
                }
            }

            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                setMouseover ( true );
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                setMouseover ( false );
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                setMouseover ( false );
            }
        };
        addMouseListener ( mouseAdapter );
        addMouseMotionListener ( mouseAdapter );

        addAncestorListener ( new AncestorAdapter ()
        {
            @Override
            public void ancestorRemoved ( final AncestorEvent event )
            {
                setMouseover ( false );
            }

            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                setMouseover ( false );
            }

            @Override
            public void ancestorMoved ( final AncestorEvent event )
            {
                setMouseover ( false );
            }
        } );

        addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyReleased ( final KeyEvent e )
            {
                if ( Hotkey.ENTER.isTriggered ( e ) || Hotkey.SPACE.isTriggered ( e ) )
                {
                    doClick ();
                }
            }
        } );
    }

    protected void setMouseover ( final boolean mouseover )
    {
        if ( mouseover )
        {
            if ( isEnabled () && highlight && !this.mouseover )
            {
                this.mouseover = true;
                updateText ();
            }
        }
        else
        {
            if ( this.mouseover )
            {
                this.mouseover = false;
                updateText ();
            }
        }
    }

    /**
     * Link foreground settings
     */

    public Color getUnvisitedForeground ()
    {
        return foreground;
    }

    public void setUnvisitedForeground ( final Color foreground )
    {
        setForeground ( foreground );
    }

    @Override
    public void setForeground ( final Color foreground )
    {
        this.foreground = foreground;
        updateForeground ();
    }

    public Color getVisitedForeground ()
    {
        return visitedForeground;
    }

    public void setVisitedForeground ( final Color visitedForeground )
    {
        this.visitedForeground = visitedForeground;
        updateForeground ();
    }

    protected void updateForeground ()
    {
        super.setForeground ( highlightVisited && visitedOnce ? visitedForeground : foreground );
    }

    /**
     * Link text settings
     */

    public String getActualText ()
    {
        return actualText;
    }

    @Override
    public void setText ( final String text )
    {
        this.actualText = LanguageManager.get ( text );
        updateText ();
    }

    protected void updateText ()
    {
        if ( mouseover && highlight )
        {
            if ( !TextUtils.isEmpty ( actualText ) )
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
                super.setText ( "<html><u>" + text + "</u></html>" );
            }
            else
            {
                super.setText ( "" );
            }
        }
        else
        {
            super.setText ( actualText );
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

    public void setLink ( final Runnable link )
    {
        this.link = link;
    }

    public void setLink ( final String text, final Runnable link )
    {
        this.link = link;
        setText ( text );
    }

    public void setLink ( final String address )
    {
        setLink ( address, true );
    }

    public void setLink ( final String address, final boolean setupView )
    {
        setLink ( address, address, setupView );
    }

    public void setLink ( final String text, final String address )
    {
        setLink ( text, address, true );
    }

    public void setLink ( final String text, final String address, final boolean setupView )
    {
        setLink ( text, createAddressLink ( address ), setupView );
    }

    public void setLink ( final String text, final Runnable link, final boolean setupView )
    {
        if ( setupView )
        {
            setIcon ( LINK_ICON );
            setText ( text );
        }
        this.link = link;
    }

    public void setEmailLink ( final String email )
    {
        setEmailLink ( email, true );
    }

    public void setEmailLink ( final String email, final boolean setupView )
    {
        setEmailLink ( email, email, setupView );
    }

    public void setEmailLink ( final String text, final String email )
    {
        setEmailLink ( text, email, true );
    }

    public void setEmailLink ( final String text, final String email, final boolean setupView )
    {
        if ( setupView )
        {
            setIcon ( EMAIL_ICON );
            setText ( text );
        }
        this.link = createEmailLink ( email );
    }

    public void setFileLink ( final File file )
    {
        setFileLink ( file, true );
    }

    public void setFileLink ( final File file, final boolean setupView )
    {
        setFileLink ( FileUtils.getDisplayFileName ( file ), file, setupView );
    }

    public void setFileLink ( final String text, final File file )
    {
        setFileLink ( text, file, true );
    }

    public void setFileLink ( final String text, final File file, final boolean setupView )
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

    public void setHighlight ( final boolean highlight )
    {
        this.highlight = highlight;
        updateText ();
    }

    public boolean isHighlightVisited ()
    {
        return highlightVisited;
    }

    public void setHighlightVisited ( final boolean highlightVisited )
    {
        this.highlightVisited = highlightVisited;
        updateForeground ();
    }

    public boolean isOnPressAction ()
    {
        return onPressAction;
    }

    public void setOnPressAction ( final boolean onPressAction )
    {
        this.onPressAction = onPressAction;
    }

    /**
     * Link action listeners
     */

    public void addActionListener ( final ActionListener actionListener )
    {
        actionListeners.add ( actionListener );
    }

    public void removeActionListener ( final ActionListener actionListener )
    {
        actionListeners.remove ( actionListener );
    }

    public void doClick ()
    {
        visitedOnce = true;
        updateForeground ();

        if ( link != null )
        {
            link.run ();
        }

        fireActionPerformed ();
    }

    protected void fireActionPerformed ()
    {
        final ActionEvent event = new ActionEvent ( this, 0, "Link opened" );
        for ( final ActionListener actionListener : CollectionUtils.copy ( actionListeners ) )
        {
            actionListener.actionPerformed ( event );
        }
    }

    protected Runnable createAddressLink ( final String address )
    {
        if ( address != null )
        {
            return new Runnable ()
            {
                @Override
                public void run ()
                {
                    executorService.execute ( new Runnable ()
                    {
                        @Override
                        public void run ()
                        {
                            WebUtils.browseSiteSafely ( address );
                        }
                    } );
                }
            };
        }
        else
        {
            return null;
        }
    }

    protected Runnable createEmailLink ( final String email )
    {
        if ( email != null )
        {
            return new Runnable ()
            {
                @Override
                public void run ()
                {
                    executorService.execute ( new Runnable ()
                    {
                        @Override
                        public void run ()
                        {
                            WebUtils.writeEmailSafely ( email );
                        }
                    } );
                }
            };
        }
        else
        {
            return null;
        }
    }

    protected Runnable createFileLink ( final File file )
    {
        if ( file != null )
        {
            return new Runnable ()
            {
                @Override
                public void run ()
                {
                    executorService.execute ( new Runnable ()
                    {
                        @Override
                        public void run ()
                        {
                            WebUtils.openFileSafely ( file );
                        }
                    } );
                }
            };
        }
        else
        {
            return null;
        }
    }
}
