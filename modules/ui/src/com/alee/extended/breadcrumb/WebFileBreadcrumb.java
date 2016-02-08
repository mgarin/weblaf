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

package com.alee.extended.breadcrumb;

import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.list.WebListCellRenderer;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebWindow;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.utils.FileUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.file.FileComparator;
import com.alee.utils.swing.AncestorAdapter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;

/**
 * @author Mikle Garin
 */

public class WebFileBreadcrumb extends WebBreadcrumb
{
    public static ImageIcon typeIcon = new ImageIcon ( WebFileBreadcrumb.class.getResource ( "icons/file/type.png" ) );
    public static ImageIcon dateIcon = new ImageIcon ( WebFileBreadcrumb.class.getResource ( "icons/file/date.png" ) );
    public static ImageIcon sizeIcon = new ImageIcon ( WebFileBreadcrumb.class.getResource ( "icons/file/size.png" ) );

    private boolean displayFileIcon = WebFileBreadcrumbStyle.displayFileIcon;
    private boolean displayFileName = WebFileBreadcrumbStyle.displayFileName;
    private boolean displayFileTip = WebFileBreadcrumbStyle.displayFileTip;
    private int fileNameLength = WebFileBreadcrumbStyle.fileNameLength;
    private int listFileNameLength = WebFileBreadcrumbStyle.listFileNameLength;
    private boolean showFullNameInTip = WebFileBreadcrumbStyle.showFullNameInTip;
    private int maxVisibleListFiles = WebFileBreadcrumbStyle.maxVisibleListFiles;
    private boolean autoExpandLastElement = WebFileBreadcrumbStyle.autoExpandLastElement;

    private File root;
    private File currentFile;

    public WebFileBreadcrumb ()
    {
        this ( FileUtils.getSystemRoot () );
    }

    public WebFileBreadcrumb ( final String root )
    {
        this ( new File ( root ) );
    }

    public WebFileBreadcrumb ( final File root )
    {
        super ();
        initialize ();
        setRoot ( root );
    }

    private void initialize ()
    {
        setEncloseLastElement ( WebFileBreadcrumbStyle.encloseLastElement );

        //        setClipProvider ( new ShapeProvider ()
        //        {
        //            public Shape provideShape ()
        //            {
        //                if ( !isUndecorated () && !isPaintRight () )
        //                {
        //                    return new Rectangle ( 0, 0,
        //                            getWidth () - getInsets ().right - WebBreadcrumbStyle.shadeWidth -
        //                                    getElementOverlap (), getHeight () ); // ltr support needed
        //                }
        //                else
        //                {
        //                    return null;
        //                }
        //            }
        //        } );
    }

    public File getRoot ()
    {
        return root;
    }

    public void setRoot ( final String root )
    {
        setRoot ( new File ( root ) );
    }

    public void setRoot ( final File root )
    {
        this.root = root.getAbsoluteFile ();
        setCurrentFile ( root );
    }

    public File getCurrentFile ()
    {
        return currentFile;
    }

    public void setCurrentFile ( final String currentFile )
    {
        setCurrentFile ( new File ( currentFile ) );
    }

    public void setCurrentFile ( File currentFile )
    {
        currentFile = currentFile.getAbsoluteFile ();
        if ( !FileUtils.equals ( root, currentFile ) && !FileUtils.isParent ( root, currentFile ) )
        {
            this.root = FileUtils.getTopParent ( currentFile );
        }
        this.currentFile = currentFile;
        updatePath ();
    }

    public boolean isDisplayFileIcon ()
    {
        return displayFileIcon;
    }

    public void setDisplayFileIcon ( final boolean displayFileIcon )
    {
        this.displayFileIcon = displayFileIcon;
        updatePath ();
    }

    public boolean isDisplayFileName ()
    {
        return displayFileName;
    }

    public void setDisplayFileName ( final boolean displayFileName )
    {
        this.displayFileName = displayFileName;
        updatePath ();
    }

    public boolean isDisplayFileTip ()
    {
        return displayFileTip;
    }

    public void setDisplayFileTip ( final boolean displayFileTip )
    {
        this.displayFileTip = displayFileTip;
        updatePath ();
    }

    public int getFileNameLength ()
    {
        return fileNameLength;
    }

    public void setFileNameLength ( final int fileNameLength )
    {
        this.fileNameLength = fileNameLength;
        updatePath ();
    }

    public int getListFileNameLength ()
    {
        return listFileNameLength;
    }

    public void setListFileNameLength ( final int listFileNameLength )
    {
        this.listFileNameLength = listFileNameLength;
    }

    public boolean isShowFullNameInTip ()
    {
        return showFullNameInTip;
    }

    public void setShowFullNameInTip ( final boolean showFullNameInTip )
    {
        this.showFullNameInTip = showFullNameInTip;
        updatePath ();
    }

    public int getMaxVisibleListFiles ()
    {
        return maxVisibleListFiles;
    }

    public void setMaxVisibleListFiles ( final int maxVisibleListFiles )
    {
        this.maxVisibleListFiles = maxVisibleListFiles;
    }

    public boolean isAutoExpandLastElement ()
    {
        return autoExpandLastElement;
    }

    public void setAutoExpandLastElement ( final boolean autoExpandLastElement )
    {
        this.autoExpandLastElement = autoExpandLastElement;
    }

    private void updatePath ()
    {
        // Disabling auto-update for the update time
        setAutoUpdate ( false );

        // Clearing current breadcrumb content
        removeAll ();

        // Creating file path content
        File current = currentFile;
        while ( !FileUtils.equals ( current, root ) )
        {
            addFile ( current );
            current = current.getParentFile ();
        }
        addFile ( root );

        // Restoring auto-update
        setAutoUpdate ( true );
    }

    private void addFile ( final File file )
    {
        if ( file.isDirectory () )
        {
            boolean showFullName = showFullNameInTip;
            final WebBreadcrumbButton fileButton = new WebBreadcrumbButton ();
            if ( displayFileIcon )
            {
                fileButton.setIcon ( FileUtils.getFileIcon ( file ) );
            }
            if ( displayFileName )
            {
                final String fileName = FileUtils.getDisplayFileName ( file );
                final String shortName = FileUtils.getShortFileName ( fileName, fileNameLength );
                showFullName = showFullName && shortName.length () != fileName.length ();
                fileButton.setText ( shortName );
            }
            if ( displayFileTip )
            {
                installTip ( file, fileButton, showFullName );
            }
            fileButton.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    final File[] files = file.listFiles ();
                    if ( files != null && files.length > 0 )
                    {
                        Arrays.sort ( files, new FileComparator () );
                        showFilesPopup ( files, fileButton );
                    }
                    else
                    {
                        TooltipManager.showOneTimeTooltip ( fileButton, null, "There are no files inside" );
                    }
                }
            } );
            add ( fileButton, 0 );
        }
        else
        {
            boolean showFullName = showFullNameInTip;
            final WebBreadcrumbLabel fileButton = new WebBreadcrumbLabel ();
            if ( displayFileIcon )
            {
                fileButton.setIcon ( FileUtils.getFileIcon ( file ) );
            }
            if ( displayFileName )
            {
                final String fileName = FileUtils.getDisplayFileName ( file );
                final String shortName = FileUtils.getShortFileName ( fileName, fileNameLength );
                showFullName = showFullName && shortName.length () != fileName.length ();
                fileButton.setText ( shortName );
            }
            if ( displayFileTip )
            {
                installTip ( file, fileButton, showFullName );
            }
            add ( fileButton, 0 );
        }
    }

    private void showFilesPopup ( final File[] files, final WebBreadcrumbButton fileButton )
    {
        final WebWindow window = new WebWindow ( SwingUtils.getWindowAncestor ( fileButton ) );
        window.setCloseOnFocusLoss ( true );
        window.setAlwaysOnTop ( true );

        final WebList list = new WebList ( files );
        list.setSelectOnHover ( true );
        list.setSelectedIndex ( 0 );
        list.setVisibleRowCount ( Math.min ( maxVisibleListFiles, files.length ) );
        list.setCellRenderer ( new WebListCellRenderer ()
        {
            @Override
            public Component getListCellRendererComponent ( final JList list, final Object value, final int index, final boolean isSelected,
                                                            final boolean cellHasFocus )
            {
                final File child = ( File ) value;
                final String fileName = FileUtils.getDisplayFileName ( child );
                final String shortFileName = FileUtils.getShortFileName ( fileName, listFileNameLength );

                final WebStyledLabel element =
                        ( WebStyledLabel ) super.getListCellRendererComponent ( list, shortFileName, index, isSelected, cellHasFocus );

                element.setIcon ( FileUtils.getFileIcon ( child ) );

                return element;
            }
        } );

        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( list.getSelectedIndex () != -1 )
                {
                    setCurrentFile ( ( File ) list.getSelectedValue () );

                    final Component lc = getLastComponent ();
                    lc.requestFocusInWindow ();

                    if ( autoExpandLastElement && lc instanceof AbstractButton )
                    {
                        ( ( AbstractButton ) lc ).doClick ();
                    }
                }
            }
        };
        list.addMouseListener ( mouseAdapter );

        list.addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyReleased ( final KeyEvent e )
            {
                if ( Hotkey.ESCAPE.isTriggered ( e ) )
                {
                    fileButton.requestFocusInWindow ();
                }
            }
        } );

        final WebScrollPane listScroll = new WebScrollPane ( StyleId.scrollpaneUndecorated, list );
        window.add ( listScroll );

        window.applyComponentOrientation ( getComponentOrientation () );
        window.pack ();

        final Point los = fileButton.getLocationOnScreen ();
        final Insets bi = list.getWebListCellRenderer ().getBorder ().getBorderInsets ( list );
        if ( getComponentOrientation ().isLeftToRight () )
        {
            window.setLocation ( los.x + fileButton.getInsets ().left - listScroll.getInsets ().left -
                    bi.left, los.y + fileButton.getHeight () + 2 );
        }
        else
        {
            window.setLocation ( los.x + fileButton.getWidth () - fileButton.getInsets ().right -
                    listScroll.getWidth () + listScroll.getInsets ().right + bi.right, los.y + fileButton.getHeight () + 2 );
        }

        window.setVisible ( true );

        list.requestFocusInWindow ();
    }

    private void installTip ( final File file, final JComponent component, final boolean showFullName )
    {
        final WebPanel panel = new WebPanel ( new VerticalFlowLayout ( 4, 4 ) );
        panel.setOpaque ( false );

        if ( showFullName )
        {
            // Full file name
            panel.add ( new WebLabel ( FileUtils.getDisplayFileName ( file ), FileUtils.getFileIcon ( file ) ) );
            panel.add ( new WebSeparator ( WebSeparator.HORIZONTAL ) );
        }

        // File description
        panel.add ( new WebLabel ( FileUtils.getFileTypeDescription ( file ), typeIcon ) );

        if ( FileUtils.isFile ( file ) )
        {
            // File modification date
            panel.add ( new WebLabel ( FileUtils.getDisplayFileModificationDate ( file ), dateIcon ) );

            // File size
            panel.add ( new WebLabel ( FileUtils.getDisplayFileSize ( file ), sizeIcon ) );
        }

        // Registering tooltip
        TooltipManager.setTooltip ( component, panel );

        // Removing tooltip when component is removed
        component.addAncestorListener ( new AncestorAdapter ()
        {
            @Override
            public void ancestorRemoved ( final AncestorEvent event )
            {
                TooltipManager.removeTooltips ( component );
            }
        } );
    }
}