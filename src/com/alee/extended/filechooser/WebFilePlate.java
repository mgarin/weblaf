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

package com.alee.extended.filechooser;

import com.alee.extended.layout.TableLayout;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 24.02.12 Time: 13:42
 */

public class WebFilePlate extends WebPanel
{
    public static final ImageIcon CROSS_ICON = new ImageIcon ( WebFilePlate.class.getResource ( "icons/cross.png" ) );

    private List<ActionListener> closeListeners = new ArrayList<ActionListener> ( 1 );

    private boolean showRemoveButton = true;
    private boolean showFileExtensions = false;
    private boolean animate = StyleConstants.animate;

    private File file;

    private WebTimer animator = null;
    private float opacity = 0f;

    private WebLabel fileName;
    private WebButton remove = null;

    public WebFilePlate ( File file )
    {
        this ( file, true );
    }

    public WebFilePlate ( File file, boolean decorated )
    {
        super ( decorated );

        this.file = file;

        // setDrawFocus ( true );
        setMargin ( 0, 3, 0, 0 );

        TableLayout tableLayout =
                new TableLayout ( new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED }, { TableLayout.PREFERRED } } );
        tableLayout.setHGap ( 0 );
        tableLayout.setVGap ( 0 );
        setLayout ( tableLayout );

        // Displayed file name
        fileName = new WebLabel ();
        fileName.setMargin ( 0, 0, 0, showRemoveButton ? 1 : 0 );
        add ( fileName, "0,0" );

        // Updating current file name
        updateFileName ();

        // Adding remove button if needed
        if ( showRemoveButton )
        {
            add ( getRemoveButton (), "1,0" );
        }

        // Adding appear listener
        addAncestorListener ( new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( AncestorEvent event )
            {
                if ( animator != null && animator.isRunning () )
                {
                    animator.stop ();
                }
                if ( animate )
                {
                    animator = new WebTimer ( "WebFilePlate.fadeInTimer", StyleConstants.animationDelay, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( ActionEvent e )
                        {
                            opacity += 0.1f;
                            if ( opacity < 1f )
                            {
                                WebFilePlate.this.repaint ();
                            }
                            else
                            {
                                opacity = 1f;
                                WebFilePlate.this.repaint ();
                                animator.stop ();
                            }
                        }
                    } );
                    animator.start ();
                }
                else
                {
                    opacity = 1f;
                    WebFilePlate.this.repaint ();
                }
            }
        } );
    }

    private void updateFileName ()
    {
        fileName.setIcon ( getDisplayIcon ( file ) );
        fileName.setText ( getDisplayName ( file ) );
    }

    private WebButton getRemoveButton ()
    {
        if ( remove == null )
        {
            remove = WebButton.createIconWebButton ( CROSS_ICON, StyleConstants.smallRound, 3, 2, true, false );
            remove.setFocusable ( false );
            remove.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent ae )
                {
                    if ( animator != null && animator.isRunning () )
                    {
                        animator.stop ();
                    }
                    if ( animate )
                    {
                        animator = new WebTimer ( "WebFilePlate.fadeOutTimer", StyleConstants.animationDelay, new ActionListener ()
                        {
                            @Override
                            public void actionPerformed ( ActionEvent e )
                            {
                                opacity -= 0.1f;
                                if ( opacity > 0f )
                                {
                                    WebFilePlate.this.repaint ();
                                }
                                else
                                {
                                    // Remove file plate
                                    removeFromParent ();

                                    // Firing final close listeners
                                    fireActionPerformed ( ae );

                                    // Stopping animation
                                    animator.stop ();
                                }
                            }
                        } );
                        animator.start ();
                    }
                    else
                    {
                        // Remove file plate
                        removeFromParent ();
                    }
                }
            } );
        }
        return remove;
    }

    private void removeFromParent ()
    {
        // Change visibility option
        opacity = 0f;

        // Remove file
        Container container = this.getParent ();
        if ( container != null && container instanceof JComponent )
        {
            JComponent parent = ( JComponent ) container;
            parent.remove ( this );
            parent.revalidate ();
            parent.repaint ();
        }
    }

    private ImageIcon getDisplayIcon ( File file )
    {
        return FileUtils.getFileIcon ( file, false );
    }

    private String getDisplayName ( File file )
    {
        String name = FileUtils.getDisplayFileName ( file );
        return showFileExtensions || file.isDirectory () ? name : FileUtils.getFileNamePart ( name );
    }

    public boolean isShowRemoveButton ()
    {
        return showRemoveButton;
    }

    public void setShowRemoveButton ( boolean showRemoveButton )
    {
        if ( this.showRemoveButton != showRemoveButton )
        {
            this.showRemoveButton = showRemoveButton;
            if ( showRemoveButton )
            {
                add ( getRemoveButton (), "1,0" );
            }
            else
            {
                remove ( getRemoveButton () );
            }
            revalidate ();
        }
    }

    public boolean isShowFileExtensions ()
    {
        return showFileExtensions;
    }

    public void setShowFileExtensions ( boolean showFileExtensions )
    {
        this.showFileExtensions = showFileExtensions;
    }

    @Override
    protected void paintComponent ( Graphics g )
    {
        LafUtils.setupAlphaComposite ( ( Graphics2D ) g, opacity, opacity < 1f );
        super.paintComponent ( g );
    }

    public void addCloseListener ( ActionListener actionListener )
    {
        closeListeners.add ( actionListener );
    }

    public void removeCloseListener ( ActionListener actionListener )
    {
        closeListeners.remove ( actionListener );
    }

    private void fireActionPerformed ( ActionEvent e )
    {
        for ( ActionListener listener : CollectionUtils.copy ( closeListeners ) )
        {
            listener.actionPerformed ( e );
        }
    }
}