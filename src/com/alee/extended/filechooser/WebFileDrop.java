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

import com.alee.extended.drag.FileDropHandler;
import com.alee.extended.filefilter.DefaultFileFilter;
import com.alee.extended.layout.WrapFlowLayout;
import com.alee.laf.StyleConstants;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
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
 * User: mgarin Date: 07.10.11 Time: 14:25
 */

public class WebFileDrop extends WebPanel implements LanguageMethods
{
    public static final ImageIcon CROSS_ICON = new ImageIcon ( WebFileDrop.class.getResource ( "icons/cross.png" ) );

    private static final BasicStroke dashStroke =
            new BasicStroke ( 3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{ 8f, 8f }, 0f );

    public int dashRound = StyleConstants.smallRound;
    public int dashSideSpacing = 10;

    private Color dropBackground = new Color ( 242, 242, 242 );
    private Color dropBorder = new Color ( 192, 192, 192 );

    private List<FilesSelectionListener> listeners = new ArrayList<FilesSelectionListener> ();

    private boolean showRemoveButton = true;
    private boolean showFileExtensions = false;
    private boolean filesDropEnabled = true;

    private boolean allowSameFiles = false;
    private DefaultFileFilter fileFilter = null;

    private boolean showDropText = true;
    private float dropTextOpacity = 1f;
    private String dropText = null;

    private List<File> selectedFiles = new ArrayList<File> ();

    public WebFileDrop ()
    {
        super ();

        setBackground ( Color.WHITE );
        setLayout ( new WrapFlowLayout ( true ) );
        setMargin ( 1, 1, 1, 1 );
        setFocusable ( true );
        setFont ( SwingUtils.getDefaultLabelFont ().deriveFont ( Font.BOLD ).deriveFont ( 20f ) );

        // Empty drop field text
        setShowDefaultDropText ( true );

        // Files TransferHandler
        setTransferHandler ( new FileDropHandler ()
        {
            @Override
            protected boolean isDropEnabled ()
            {
                return filesDropEnabled;
            }

            @Override
            protected boolean filesImported ( List<File> files )
            {
                // Adding dragged files
                boolean anyAdded = false;
                for ( File file : files )
                {
                    if ( fileFilter != null && !fileFilter.accept ( file ) ||
                            !allowSameFiles && FileUtils.containtsFile ( selectedFiles, file ) )
                    {
                        continue;
                    }
                    selectedFiles.add ( file );
                    WebFileDrop.this.add ( createFilePlate ( file ) );
                    anyAdded = true;
                }
                if ( anyAdded )
                {
                    WebFileDrop.this.revalidate ();
                    WebFileDrop.this.repaint ();
                    fireSelectionChanged ( selectedFiles );
                }
                return anyAdded;
            }
        } );

        // Focus "catcher"
        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
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
            public void selectionChanged ( List<File> selectedFiles )
            {
                if ( filesCount == 0 && selectedFiles.size () > 0 )
                {
                    stopAnimator ();
                    filesCount = selectedFiles.size ();
                    animator = new WebTimer ( "WebFileDrop.textFadeOutTimer", StyleConstants.animationDelay, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( ActionEvent e )
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
                    animator = new WebTimer ( "WebFileDrop.textFadeInTimer", StyleConstants.animationDelay, new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( ActionEvent e )
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

    public void setShowDropText ( boolean showDropText )
    {
        this.showDropText = showDropText;
        WebFileDrop.this.repaint ();
    }

    public boolean isFilesDropEnabled ()
    {
        return filesDropEnabled;
    }

    public void setFilesDropEnabled ( boolean filesDropEnabled )
    {
        this.filesDropEnabled = filesDropEnabled;
    }

    public List<File> getSelectedFiles ()
    {
        return selectedFiles;
    }

    public void setSelectedFiles ( List<File> selectedFiles )
    {
        // Filtering only accepted by filter files
        List<File> accepted = new ArrayList<File> ();
        for ( File file : selectedFiles )
        {
            if ( ( fileFilter == null || fileFilter.accept ( file ) ) && ( allowSameFiles || !FileUtils.containtsFile ( accepted, file ) ) )
            {
                accepted.add ( file );
            }
        }

        // Checking if lists are equal
        if ( FileUtils.equals ( accepted, this.selectedFiles ) )
        {
            this.selectedFiles = accepted;
            updateFilesList ();
            fireSelectionChanged ( accepted );
        }
    }

    public boolean isAllowSameFiles ()
    {
        return allowSameFiles;
    }

    public void setAllowSameFiles ( boolean allowSameFiles )
    {
        this.allowSameFiles = allowSameFiles;
        setSelectedFiles ( selectedFiles );
    }

    public DefaultFileFilter getFileFilter ()
    {
        return fileFilter;
    }

    public void setFileFilter ( DefaultFileFilter fileFilter )
    {
        this.fileFilter = fileFilter;
        setSelectedFiles ( selectedFiles );
    }

    public boolean isShowRemoveButton ()
    {
        return showRemoveButton;
    }

    public void setShowRemoveButton ( boolean showRemoveButton )
    {
        this.showRemoveButton = showRemoveButton;
        updateFilesList ();
    }

    public boolean isShowFileExtensions ()
    {
        return showFileExtensions;
    }

    public void setShowFileExtensions ( boolean showFileExtensions )
    {
        this.showFileExtensions = showFileExtensions;
        updateFilesList ();
    }

    public Color getDropBackground ()
    {
        return dropBackground;
    }

    public void setDropBackground ( Color dropBackground )
    {
        this.dropBackground = dropBackground;
        repaint ();
    }

    public Color getDropBorder ()
    {
        return dropBorder;
    }

    public void setDropBorder ( Color dropBorder )
    {
        this.dropBorder = dropBorder;
        repaint ();
    }

    public int getDashRound ()
    {
        return dashRound;
    }

    public void setDashRound ( int dashRound )
    {
        this.dashRound = dashRound;
        repaint ();
    }

    public int getDashSideSpacing ()
    {
        return dashSideSpacing;
    }

    public void setDashSideSpacing ( int dashSideSpacing )
    {
        this.dashSideSpacing = dashSideSpacing;
        repaint ();
    }

    public String getDropText ()
    {
        return dropText;
    }

    public void setDropText ( String dropText )
    {
        this.dropText = dropText;
        repaint ();
    }

    public void setShowDefaultDropText ( boolean defaultDropText )
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

    @Override
    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        if ( isDropTextVisible () )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final Composite old = LafUtils.setupAlphaComposite ( g2d, dropTextOpacity );
            final Object aa = LafUtils.setupAntialias ( g2d );

            final int dashX = dashSideSpacing + Math.round ( dashStroke.getLineWidth () / 2 );
            final int dashY = dashSideSpacing + Math.round ( dashStroke.getLineWidth () / 2 );
            final int dashWidth = getWidth () - dashSideSpacing * 2 - Math.round ( dashStroke.getLineWidth () );
            final int dashHeight = getHeight () - dashSideSpacing * 2 - Math.round ( dashStroke.getLineWidth () );
            g2d.setPaint ( dropBackground );
            g2d.fillRoundRect ( dashX, dashY, dashWidth, dashHeight, dashRound * 2, dashRound * 2 );

            final Stroke os = LafUtils.setupStroke ( g2d, dashStroke );
            g2d.setPaint ( dropBorder );
            g2d.drawRoundRect ( dashSideSpacing, dashSideSpacing, getWidth () - dashSideSpacing * 2 - 1,
                    getHeight () - dashSideSpacing * 2 - 1, dashRound * 2, dashRound * 2 );
            LafUtils.restoreStroke ( g2d, os );

            LafUtils.restoreAntialias ( g2d, aa );

            final FontMetrics fm = g2d.getFontMetrics ();
            if ( dashWidth >= fm.stringWidth ( dropText ) && dashHeight > fm.getHeight () )
            {
                final Map hints = SwingUtils.setupTextAntialias ( g2d, this );
                final Point ts = LafUtils.getTextCenterShear ( fm, dropText );
                g2d.drawString ( dropText, getWidth () / 2 + ts.x, getHeight () / 2 + ts.y );
                SwingUtils.restoreTextAntialias ( g2d, hints );
            }

            LafUtils.restoreComposite ( g2d, old );
        }
    }

    private void updateFilesList ()
    {
        removeAll ();
        for ( File file : selectedFiles )
        {
            add ( createFilePlate ( file ) );
        }
        revalidate ();
        repaint ();
    }

    private WebFilePlate createFilePlate ( final File file )
    {
        WebFilePlate filePlate = new WebFilePlate ( file );

        // To block parent container events
        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
            {
                requestFocusInWindow ();
            }
        } );

        // Data change on removal
        filePlate.addCloseListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                // Removing file from added files list
                selectedFiles.remove ( file );

                // Inform that selected files changed
                fireSelectionChanged ( selectedFiles );
            }
        } );

        return filePlate;
    }

    public void addFileSelectionListener ( FilesSelectionListener listener )
    {
        listeners.add ( listener );
    }

    public void removeFileSelectionListener ( FilesSelectionListener listener )
    {
        listeners.remove ( listener );
    }

    private void fireSelectionChanged ( List<File> selectedFiles )
    {
        for ( FilesSelectionListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.selectionChanged ( selectedFiles );
        }
    }

    /**
     * Language methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguage ( String key, Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( String key, Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageUpdater ( LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }
}