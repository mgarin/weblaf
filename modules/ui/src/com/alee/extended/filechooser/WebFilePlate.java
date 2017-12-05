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
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.drag.transfer.FilesTransferHandler;
import com.alee.managers.drag.transfer.ProxyDropHandler;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a custom panel that represents a single File within any container.
 * It is basically used within WebFileDrop component to render selected files.
 *
 * @author Mikle Garin
 */

public class WebFilePlate extends WebPanel
{
    protected final List<ActionListener> closeListeners = new ArrayList<ActionListener> ( 1 );

    protected boolean showRemoveButton = true;
    protected boolean showFileExtensions = false;
    protected final boolean animate = true;

    protected boolean dragEnabled = false;
    protected int dragAction = TransferHandler.MOVE;

    protected final File file;

    protected WebTimer animator = null;
    protected float opacity = 0f;

    protected final WebLabel fileName;
    protected WebButton remove = null;

    public WebFilePlate ( final WebFileDrop fileDrop, final File file )
    {
        super ( StyleId.filedropPlate.at ( fileDrop ) );

        this.file = file;

        // Layout
        setLayout ( new TableLayout ( new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED }, { TableLayout.PREFERRED } }, 0, 0 ) );

        // Displayed file name
        fileName = new WebLabel ( StyleId.filedropPlateFileLabel.at ( WebFilePlate.this ) );
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
            public void ancestorAdded ( final AncestorEvent event )
            {
                if ( animator != null && animator.isRunning () )
                {
                    animator.stop ();
                }
                if ( animate )
                {
                    animator = new WebTimer ( "WebFilePlate.fadeInTimer", SwingUtils.frameRateDelay ( 24 ), new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
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

        // Custom file drag handler
        final MouseAdapter ma = new MouseAdapter ()
        {
            public boolean doDrag;

            @Override
            public void mousePressed ( final MouseEvent e )
            {
                doDrag = true;
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                if ( doDrag )
                {
                    final TransferHandler transferHandler = getTransferHandler ();
                    transferHandler.exportAsDrag ( WebFilePlate.this, e, transferHandler.getSourceActions ( WebFilePlate.this ) );
                    doDrag = false;
                }
            }
        };
        addMouseListener ( ma );
        addMouseMotionListener ( ma );
        setTransferHandler ( new FilesTransferHandler ( true, true )
        {
            private final ProxyDropHandler dropProxy = new ProxyDropHandler ();

            @Override
            public boolean isDragEnabled ()
            {
                final Container parent = WebFilePlate.this.getParent ();
                if ( parent instanceof WebFileDrop )
                {
                    return ( ( WebFileDrop ) parent ).isFilesDragEnabled ();
                }
                return WebFilePlate.this.isDragEnabled ();
            }

            @Override
            public int getSourceActions ( final JComponent component )
            {
                return getSourceActions ();
            }

            protected int getSourceActions ()
            {
                final Container parent = WebFilePlate.this.getParent ();
                if ( parent instanceof WebFileDrop )
                {
                    return ( ( WebFileDrop ) parent ).getDragAction ();
                }
                return WebFilePlate.this.getDragAction ();
            }

            @Override
            public File getDraggedFile ()
            {
                // Remove this plate from WebFileDrop if it is a move action
                if ( getSourceActions () == MOVE )
                {
                    final Container parent = getParent ();
                    if ( parent instanceof WebFileDrop )
                    {
                        performPlateRemoval ( new ActionEvent ( WebFilePlate.this, 0, "File moved by drag" ), false );
                    }
                }

                // Pass this plate's file
                return file;
            }

            @Override
            public boolean importData ( final TransferSupport info )
            {
                // Special workaround to make this plate drop-transparent
                return dropProxy.importData ( info );
            }
        } );
    }

    public boolean isDragEnabled ()
    {
        return dragEnabled;
    }

    public void setDragEnabled ( final boolean dragEnabled )
    {
        this.dragEnabled = dragEnabled;
    }

    public int getDragAction ()
    {
        return dragAction;
    }

    public void setDragAction ( final int dragAction )
    {
        this.dragAction = dragAction;
    }

    protected void updateFileName ()
    {
        fileName.setIcon ( getDisplayIcon ( file ) );
        fileName.setText ( getDisplayName ( file ) );
    }

    protected WebButton getRemoveButton ()
    {
        if ( remove == null )
        {
            remove = new WebButton ( StyleId.filedropPlateRemoveButton.at ( WebFilePlate.this ), Icons.crossSmall );
            remove.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent ae )
                {
                    performPlateRemoval ( ae, animate );
                }
            } );
        }
        return remove;
    }

    protected void performPlateRemoval ( final ActionEvent ae, final boolean animate )
    {
        if ( animator != null && animator.isRunning () )
        {
            animator.stop ();
        }
        if ( animate )
        {
            animator = new WebTimer ( "WebFilePlate.fadeOutTimer", SwingUtils.frameRateDelay ( 24 ), new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
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

        // Firing close listeners
        fireCloseActionPerformed ( ae );
    }

    protected void removeFromParent ()
    {
        // Change visibility option
        opacity = 0f;

        // Remove file
        final Container container = this.getParent ();
        if ( container != null && container instanceof JComponent )
        {
            final JComponent parent = ( JComponent ) container;
            parent.remove ( this );
            parent.revalidate ();
            parent.repaint ();
        }
    }

    protected ImageIcon getDisplayIcon ( final File file )
    {
        return FileUtils.getFileIcon ( file, false );
    }

    protected String getDisplayName ( final File file )
    {
        final String name = FileUtils.getDisplayFileName ( file );
        return showFileExtensions || file.isDirectory () ? name : FileUtils.getFileNamePart ( name );
    }

    public boolean isShowRemoveButton ()
    {
        return showRemoveButton;
    }

    public void setShowRemoveButton ( final boolean showRemoveButton )
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

    public void setShowFileExtensions ( final boolean showFileExtensions )
    {
        this.showFileExtensions = showFileExtensions;
        updateFileName ();
    }

    public File getFile ()
    {
        return file;
    }

    public void remove ()
    {
        remove.doClick ( 0 );
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        GraphicsUtils.setupAlphaComposite ( ( Graphics2D ) g, opacity, opacity < 1f );
        super.paintComponent ( g );
    }

    public void addCloseListener ( final ActionListener actionListener )
    {
        closeListeners.add ( actionListener );
    }

    public void removeCloseListener ( final ActionListener actionListener )
    {
        closeListeners.remove ( actionListener );
    }

    protected void fireCloseActionPerformed ( final ActionEvent e )
    {
        for ( final ActionListener listener : CollectionUtils.copy ( closeListeners ) )
        {
            listener.actionPerformed ( e );
        }
    }
}