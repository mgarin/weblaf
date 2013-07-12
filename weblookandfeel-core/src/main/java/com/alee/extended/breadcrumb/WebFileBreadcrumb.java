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

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.list.WebListCellRenderer;
import com.alee.laf.list.WebListElement;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebWindow;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.hotkey.Hotkey;
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
 * User: mgarin Date: 22.06.12 Time: 15:00
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

    public WebFileBreadcrumb ( String root )
    {
        this ( new File ( root ) );
    }

    public WebFileBreadcrumb ( File root )
    {
        super ();
        initialize ();
        setRoot ( root );
    }

    public WebFileBreadcrumb ( boolean decorated )
    {
        super ( decorated );
        initialize ();
        setRoot ( FileUtils.getSystemRoot () );
    }

    public WebFileBreadcrumb ( String root, boolean decorated )
    {
        this ( new File ( root ), decorated );
    }

    public WebFileBreadcrumb ( File root, boolean decorated )
    {
        super ( decorated );
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
        //                if ( !isUndecorated () && !isDrawRight () )
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

    public void setRoot ( String root )
    {
        setRoot ( new File ( root ) );
    }

    public void setRoot ( File root )
    {
        this.root = root.getAbsoluteFile ();
        setCurrentFile ( root );
    }

    public File getCurrentFile ()
    {
        return currentFile;
    }

    public void setCurrentFile ( String currentFile )
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

    public void setDisplayFileIcon ( boolean displayFileIcon )
    {
        this.displayFileIcon = displayFileIcon;
        updatePath ();
    }

    public boolean isDisplayFileName ()
    {
        return displayFileName;
    }

    public void setDisplayFileName ( boolean displayFileName )
    {
        this.displayFileName = displayFileName;
        updatePath ();
    }

    public boolean isDisplayFileTip ()
    {
        return displayFileTip;
    }

    public void setDisplayFileTip ( boolean displayFileTip )
    {
        this.displayFileTip = displayFileTip;
        updatePath ();
    }

    public int getFileNameLength ()
    {
        return fileNameLength;
    }

    public void setFileNameLength ( int fileNameLength )
    {
        this.fileNameLength = fileNameLength;
        updatePath ();
    }

    public int getListFileNameLength ()
    {
        return listFileNameLength;
    }

    public void setListFileNameLength ( int listFileNameLength )
    {
        this.listFileNameLength = listFileNameLength;
    }

    public boolean isShowFullNameInTip ()
    {
        return showFullNameInTip;
    }

    public void setShowFullNameInTip ( boolean showFullNameInTip )
    {
        this.showFullNameInTip = showFullNameInTip;
        updatePath ();
    }

    public int getMaxVisibleListFiles ()
    {
        return maxVisibleListFiles;
    }

    public void setMaxVisibleListFiles ( int maxVisibleListFiles )
    {
        this.maxVisibleListFiles = maxVisibleListFiles;
    }

    public boolean isAutoExpandLastElement ()
    {
        return autoExpandLastElement;
    }

    public void setAutoExpandLastElement ( boolean autoExpandLastElement )
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

        // Performing single update
        updateElements ();
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
                String fileName = FileUtils.getDisplayFileName ( file );
                String shortName = FileUtils.getShortFileName ( fileName, fileNameLength );
                showFullName = showFullName && shortName.length () != fileName.length ();
                fileButton.setText ( shortName );
            }
            if ( displayFileTip )
            {
                installTip ( file, fileButton, showFullName );
            }
            fileButton.addActionListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    File[] files = file.listFiles ();
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
                String fileName = FileUtils.getDisplayFileName ( file );
                String shortName = FileUtils.getShortFileName ( fileName, fileNameLength );
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

    private void showFilesPopup ( File[] files, final WebBreadcrumbButton fileButton )
    {
        final WebWindow window = new WebWindow ( SwingUtils.getWindowAncestor ( fileButton ) );
        window.setCloseOnFocusLoss ( true );
        window.setAlwaysOnTop ( true );

        final WebList list = new WebList ( files );
        list.setRolloverSelectionEnabled ( true );
        list.setSelectedIndex ( 0 );
        list.setVisibleRowCount ( Math.min ( maxVisibleListFiles, files.length ) );
        list.setCellRenderer ( new WebListCellRenderer ()
        {
            public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
            {
                WebListElement element =
                        ( WebListElement ) super.getListCellRendererComponent ( list, value, index, isSelected, cellHasFocus );

                File child = ( File ) value;

                element.setIcon ( FileUtils.getFileIcon ( child ) );

                String fileName = FileUtils.getDisplayFileName ( child );
                element.setText ( FileUtils.getShortFileName ( fileName, listFileNameLength ) );

                return element;
            }
        } );

        MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            public void mousePressed ( MouseEvent e )
            {
                if ( list.getSelectedIndex () != -1 )
                {
                    setCurrentFile ( ( File ) list.getSelectedValue () );

                    Component lc = getLastComponent ();
                    lc.requestFocus ();
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
            public void keyReleased ( KeyEvent e )
            {
                if ( Hotkey.ESCAPE.isTriggered ( e ) )
                {
                    fileButton.requestFocusInWindow ();
                }
            }
        } );

        WebScrollPane listScroll = new WebScrollPane ( list );
        listScroll.setShadeWidth ( 0 );
        listScroll.setDrawFocus ( false );
        window.add ( listScroll );

        window.applyComponentOrientation ( getComponentOrientation () );
        window.pack ();

        Point los = fileButton.getLocationOnScreen ();
        Insets bi = list.getWebListCellRenderer ().getBorder ().getBorderInsets ( list );
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

    private void installTip ( File file, final JComponent component, boolean showFullName )
    {
        WebPanel panel = new WebPanel ( new VerticalFlowLayout ( 4, 4 ) );
        panel.setOpaque ( false );

        if ( showFullName )
        {
            // Full file name
            panel.add ( new WebLabel ( FileUtils.getDisplayFileName ( file ), FileUtils.getFileIcon ( file ) ) );
            panel.add ( new WebSeparator ( false, WebSeparator.HORIZONTAL, true ) );
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
            public void ancestorRemoved ( AncestorEvent event )
            {
                TooltipManager.removeTooltips ( component );
            }
        } );
    }
}