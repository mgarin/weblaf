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

import com.alee.extended.drag.FileDragAndDropHandler;
import com.alee.extended.layout.WrapFlowLayout;
import com.alee.global.StyleConstants;
import com.alee.managers.style.StyleId;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.utils.*;
import com.alee.utils.filefilter.AbstractFileFilter;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Custom component that acts as files container and allows drag-and-drop them.
 * Separate WebFilePlate component is created for each added file to display it.
 *
 * @author Mikle Garin
 */

public class WebFileDrop extends WebPanel implements LanguageMethods
{
    /**
     * Remove file icon.
     */
    public static final ImageIcon CROSS_ICON = new ImageIcon ( WebFileDrop.class.getResource ( "icons/cross.png" ) );

    protected static final BasicStroke dashStroke =
            new BasicStroke ( 3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{ 8f, 8f }, 0f );

    protected int dashRound = 2;
    protected int dashSideSpacing = 10;

    protected Color dropBackground = new Color ( 242, 242, 242 );
    protected Color dropBorder = new Color ( 192, 192, 192 );

    protected final List<FilesSelectionListener> listeners = new ArrayList<FilesSelectionListener> ( 1 );

    protected boolean showRemoveButton = true;
    protected boolean showFileExtensions = false;

    protected boolean filesDragEnabled = false;
    protected int dragAction = TransferHandler.MOVE;

    protected boolean filesDropEnabled = true;

    protected boolean allowSameFiles = false;
    protected AbstractFileFilter fileFilter = null;

    protected boolean showDropText = true;
    protected float dropTextOpacity = 1f;
    protected String dropText = null;

    protected List<File> selectedFiles = new ArrayList<File> ();

    public WebFileDrop ()
    {
        this ( StyleId.filedrop );
    }

    public WebFileDrop ( final StyleId id )
    {
        super ( id, new WrapFlowLayout ( true ) );

        // Default visual settings
        setFont ( SwingUtils.getDefaultLabelFont ().deriveFont ( Font.BOLD ).deriveFont ( 20f ) );

        // Empty drop field text
        setShowDefaultDropText ( true );

        // Files TransferHandler
        setTransferHandler ( new FileDragAndDropHandler ()
        {
            @Override
            public boolean isDropEnabled ()
            {
                return filesDropEnabled;
            }

            @Override
            public boolean filesDropped ( final List<File> files )
            {
                // Adding dragged files
                addSelectedFiles ( files );
                return true;
            }
        } );

        // Focus "catcher"
        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                WebFileDrop.this.requestFocusInWindow ();
            }
        } );

        // Background animation listener
        addFileSelectionListener ( new FilesSelectionListener ()
        {
            private int filesCount = 0;
            private WebTimer animator = null;

            @Override
            public void selectionChanged ( final List<File> selectedFiles )
            {
                if ( filesCount == 0 && selectedFiles.size () > 0 )
                {
                    stopAnimator ();
                    filesCount = selectedFiles.size ();
                    animator = new WebTimer ( "WebFileDrop.textFadeOutTimer", StyleConstants.fps24, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            if ( dropTextOpacity > 0f )
                            {
                                dropTextOpacity -= 0.1f;
                            }
                            dropTextOpacity = Math.max ( dropTextOpacity, 0f );
                            WebFileDrop.this.repaint ();
                            if ( dropTextOpacity <= 0f )
                            {
                                animator.stop ();
                            }
                        }
                    } );
                    animator.start ();
                }
                else if ( filesCount > 0 && selectedFiles.size () == 0 )
                {
                    stopAnimator ();
                    filesCount = selectedFiles.size ();
                    animator = new WebTimer ( "WebFileDrop.textFadeInTimer", StyleConstants.fps24, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            if ( dropTextOpacity < 1f )
                            {
                                dropTextOpacity += 0.1f;
                            }
                            dropTextOpacity = Math.min ( dropTextOpacity, 1f );
                            WebFileDrop.this.repaint ();
                            if ( dropTextOpacity >= 1f )
                            {
                                animator.stop ();
                            }
                        }
                    } );
                    animator.start ();
                }
            }

            private void stopAnimator ()
            {
                if ( animator != null && animator.isRunning () )
                {
                    animator.stop ();
                }
            }
        } );
    }

    public boolean isShowDropText ()
    {
        return showDropText;
    }

    public void setShowDropText ( final boolean showDropText )
    {
        this.showDropText = showDropText;
        WebFileDrop.this.repaint ();
    }

    public boolean isFilesDragEnabled ()
    {
        return filesDragEnabled;
    }

    public void setFilesDragEnabled ( final boolean filesDragEnabled )
    {
        this.filesDragEnabled = filesDragEnabled;
    }

    public int getDragAction ()
    {
        return dragAction;
    }

    public void setDragAction ( final int dragAction )
    {
        this.dragAction = dragAction;
    }

    public boolean isFilesDropEnabled ()
    {
        return filesDropEnabled;
    }

    public void setFilesDropEnabled ( final boolean filesDropEnabled )
    {
        this.filesDropEnabled = filesDropEnabled;
    }

    public List<File> getSelectedFiles ()
    {
        return CollectionUtils.copy ( selectedFiles );
    }

    public void setSelectedFiles ( final List<File> files )
    {
        removeAllSelectedFiles ();
        addSelectedFiles ( files );
    }

    public void addSelectedFiles ( final List<File> files )
    {
        boolean changed = false;
        for ( final File file : files )
        {
            changed = addSelectedFileImpl ( file ) || changed;
        }
        if ( changed )
        {
            revalidate ();
            fireSelectionChanged ();
        }
    }

    public void addSelectedFiles ( final File... files )
    {
        boolean changed = false;
        for ( final File file : files )
        {
            changed = addSelectedFileImpl ( file ) || changed;
        }
        if ( changed )
        {
            revalidate ();
            fireSelectionChanged ();
        }
    }

    public void addSelectedFile ( final File file )
    {
        if ( addSelectedFileImpl ( file ) )
        {
            revalidate ();
            fireSelectionChanged ();
        }
    }

    protected boolean addSelectedFileImpl ( final File file )
    {
        if ( ( fileFilter == null || fileFilter.accept ( file ) ) && ( allowSameFiles || !FileUtils.containsFile ( selectedFiles, file ) ) )
        {
            add ( createFilePlate ( file ) );
            selectedFiles.add ( file );
            return true;
        }
        else
        {
            return false;
        }
    }

    public void removeAllSelectedFiles ()
    {
        boolean changed = false;
        for ( final File file : CollectionUtils.copy ( selectedFiles ) )
        {
            changed = removeSelectedFileImpl ( file, false ) || changed;
        }
        if ( changed )
        {
            revalidate ();
            fireSelectionChanged ();
        }
    }

    public void removeSelectedFiles ( final List<File> files )
    {
        boolean changed = false;
        for ( final File file : files )
        {
            changed = removeSelectedFileImpl ( file, true ) || changed;
        }
        if ( changed )
        {
            revalidate ();
            fireSelectionChanged ();
        }
    }

    public void removeSelectedFiles ( final File... files )
    {
        boolean changed = false;
        for ( final File file : files )
        {
            changed = removeSelectedFileImpl ( file, true ) || changed;
        }
        if ( changed )
        {
            revalidate ();
            fireSelectionChanged ();
        }
    }

    public void removeSelectedFile ( final File file )
    {
        if ( removeSelectedFileImpl ( file, true ) )
        {
            revalidate ();
            fireSelectionChanged ();
        }
    }

    protected boolean removeSelectedFileImpl ( final File file, final boolean animate )
    {
        if ( FileUtils.containsFile ( selectedFiles, file ) )
        {
            for ( final WebFilePlate filePlate : getFilePlates ( file ) )
            {
                if ( animate )
                {
                    filePlate.remove ();
                }
                else
                {
                    remove ( filePlate );
                    selectedFiles.remove ( file );
                }
            }
            return !animate;
        }
        else
        {
            return false;
        }
    }

    public List<WebFilePlate> getFilePlates ( final File file )
    {
        final List<WebFilePlate> plates = new ArrayList<WebFilePlate> ( 1 );
        for ( int i = 0; i < getComponentCount (); i++ )
        {
            final Component component = getComponent ( i );
            if ( component instanceof WebFilePlate )
            {
                final WebFilePlate filePlate = ( WebFilePlate ) component;
                if ( file.getAbsolutePath ().equals ( filePlate.getFile ().getAbsolutePath () ) )
                {
                    plates.add ( filePlate );
                }
            }
        }
        return plates;
    }

    public boolean isAllowSameFiles ()
    {
        return allowSameFiles;
    }

    public void setAllowSameFiles ( final boolean allowSameFiles )
    {
        this.allowSameFiles = allowSameFiles;
        setSelectedFiles ( selectedFiles );
    }

    public AbstractFileFilter getFileFilter ()
    {
        return fileFilter;
    }

    public void setFileFilter ( final AbstractFileFilter fileFilter )
    {
        this.fileFilter = fileFilter;
        setSelectedFiles ( selectedFiles );
    }

    public boolean isShowRemoveButton ()
    {
        return showRemoveButton;
    }

    public void setShowRemoveButton ( final boolean showRemoveButton )
    {
        this.showRemoveButton = showRemoveButton;
        for ( int i = 0; i < getComponentCount (); i++ )
        {
            ( ( WebFilePlate ) getComponent ( i ) ).setShowRemoveButton ( showRemoveButton );
        }
    }

    public boolean isShowFileExtensions ()
    {
        return showFileExtensions;
    }

    public void setShowFileExtensions ( final boolean showFileExtensions )
    {
        this.showFileExtensions = showFileExtensions;
        for ( int i = 0; i < getComponentCount (); i++ )
        {
            ( ( WebFilePlate ) getComponent ( i ) ).setShowFileExtensions ( showFileExtensions );
        }
    }

    public Color getDropBackground ()
    {
        return dropBackground;
    }

    public void setDropBackground ( final Color dropBackground )
    {
        this.dropBackground = dropBackground;
        repaint ();
    }

    public Color getDropBorder ()
    {
        return dropBorder;
    }

    public void setDropBorder ( final Color dropBorder )
    {
        this.dropBorder = dropBorder;
        repaint ();
    }

    public int getDashRound ()
    {
        return dashRound;
    }

    public void setDashRound ( final int dashRound )
    {
        this.dashRound = dashRound;
        repaint ();
    }

    public int getDashSideSpacing ()
    {
        return dashSideSpacing;
    }

    public void setDashSideSpacing ( final int dashSideSpacing )
    {
        this.dashSideSpacing = dashSideSpacing;
        repaint ();
    }

    public String getDropText ()
    {
        return dropText;
    }

    public void setDropText ( final String dropText )
    {
        this.dropText = dropText;
        repaint ();
    }

    public void setShowDefaultDropText ( final boolean defaultDropText )
    {
        if ( defaultDropText )
        {
            setLanguage ( "weblaf.ex.filedrop.drop" );
        }
        else
        {
            removeLanguage ();
        }
        repaint ();
    }

    public boolean isDropTextVisible ()
    {
        return dropText != null && showDropText && dropTextOpacity > 0f;
    }

    protected WebFilePlate createFilePlate ( final File file )
    {
        final WebFilePlate filePlate = new WebFilePlate ( this, file );
        filePlate.setShowFileExtensions ( showFileExtensions );

        // To block parent container events
        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                requestFocusInWindow ();
            }
        } );

        // Data change on removal
        filePlate.addCloseListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Removing file from added files list
                selectedFiles.remove ( file );

                // Inform that selected files changed
                fireSelectionChanged ();
            }
        } );

        return filePlate;
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        if ( isDropTextVisible () )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final Composite old = GraphicsUtils.setupAlphaComposite ( g2d, dropTextOpacity );
            final Object aa = GraphicsUtils.setupAntialias ( g2d );

            final Insets bi = getInsets ();
            final int hd = Math.round ( dashStroke.getLineWidth () / 2 );
            final int hd2 = Math.round ( dashStroke.getLineWidth () );
            final int dashX = dashSideSpacing + bi.left;
            final int dashY = dashSideSpacing + bi.top;
            final int dashWidth = getWidth () - dashSideSpacing * 2 - bi.left - bi.right;
            final int dashHeight = getHeight () - dashSideSpacing * 2 - bi.top - bi.bottom;
            g2d.setPaint ( dropBackground );
            g2d.fillRoundRect ( dashX + hd, dashY + hd, dashWidth - hd2, dashHeight - hd2, dashRound * 2, dashRound * 2 );

            final Stroke os = GraphicsUtils.setupStroke ( g2d, dashStroke );
            g2d.setPaint ( dropBorder );
            g2d.drawRoundRect ( dashX, dashY, dashWidth, dashHeight, dashRound * 2, dashRound * 2 );
            GraphicsUtils.restoreStroke ( g2d, os );

            GraphicsUtils.restoreAntialias ( g2d, aa );

            final FontMetrics fm = g2d.getFontMetrics ();
            if ( dashWidth >= fm.stringWidth ( dropText ) && dashHeight > fm.getHeight () )
            {
                final Map hints = SwingUtils.setupTextAntialias ( g2d );
                final Point ts = LafUtils.getTextCenterShift ( fm, dropText );
                g2d.drawString ( dropText, dashX + dashWidth / 2 + ts.x, dashY + dashHeight / 2 + ts.y );
                SwingUtils.restoreTextAntialias ( g2d, hints );
            }

            GraphicsUtils.restoreComposite ( g2d, old );
        }
    }

    public void addFileSelectionListener ( final FilesSelectionListener listener )
    {
        listeners.add ( listener );
    }

    public void removeFileSelectionListener ( final FilesSelectionListener listener )
    {
        listeners.remove ( listener );
    }

    protected void fireSelectionChanged ()
    {
        for ( final FilesSelectionListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.selectionChanged ( CollectionUtils.copy ( selectedFiles ) );
        }
    }

    /**
     * Language methods
     */

    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }
}