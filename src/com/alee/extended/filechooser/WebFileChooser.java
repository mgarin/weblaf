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

import com.alee.extended.filefilter.DefaultFileFilter;
import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;
import com.alee.laf.rootpane.WebDialog;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyCondition;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 05.07.11 Time: 12:27
 */

@Deprecated
public class WebFileChooser extends WebDialog
{
    private static final ImageIcon ICON = new ImageIcon ( WebFileChooser.class.getResource ( "icons/file_icon.png" ) );

    private ActionListener okListener;
    private WebFileChooserPanel fileChooserPanel;

    private int result = StyleConstants.NONE_OPTION;

    public WebFileChooser ( Component parent )
    {
        this ( parent, null );
    }

    public WebFileChooser ( Component parent, String title )
    {
        super ( SwingUtils.getWindowAncestor ( parent ), title != null ? title : "" );
        setIconImage ( ICON.getImage () );
        if ( title == null )
        {
            setLanguage ( "weblaf.filechooser.title" );
        }

        // Hotkeys preview action
        HotkeyManager.installShowAllHotkeysAction ( this, Hotkey.F1 );

        // Default container settings
        getContentPane ().setBackground ( Color.WHITE );
        getContentPane ().setLayout ( new BorderLayout () );

        // File chooser itself
        fileChooserPanel = new WebFileChooserPanel ( true );
        fileChooserPanel.setOkListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                result = StyleConstants.OK_OPTION;
                WebFileChooser.this.dispose ();
                if ( okListener != null )
                {
                    okListener.actionPerformed ( e );
                }
            }
        } );
        fileChooserPanel.setCancelListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                result = StyleConstants.CANCEL_OPTION;
                WebFileChooser.this.dispose ();
            }
        } );
        getContentPane ().add ( fileChooserPanel, BorderLayout.CENTER );

        // Hotkeys condition
        HotkeyManager.addContainerHotkeyCondition ( this, new HotkeyCondition ()
        {
            public boolean checkCondition ( Component component )
            {
                return fileChooserPanel.allowHotkeys ();
            }
        } );

        // Result saver
        addWindowListener ( new WindowAdapter ()
        {
            public void windowClosed ( WindowEvent e )
            {
                result = StyleConstants.CLOSE_OPTION;
            }
        } );

        setModal ( true );
        pack ();
        setDefaultCloseOperation ( JDialog.DISPOSE_ON_CLOSE );
    }

    public int getResult ()
    {
        return result;
    }

    public int showDialog ()
    {
        setVisible ( true );
        return getResult ();
    }

    public static List<File> showDialog ( Component parent )
    {
        return showDialog ( parent, null );
    }

    public static List<File> showDialog ( Component parent, String title )
    {
        WebFileChooser wdc = new WebFileChooser ( parent, title );
        wdc.setVisible ( true );
        if ( wdc.getResult () == StyleConstants.OK_OPTION )
        {
            return wdc.getSelectedFiles ();
        }
        else
        {
            return null;
        }
    }

    public WebFileChooserPanel getFileChooserPanel ()
    {
        return fileChooserPanel;
    }

    public void addFileChooserListener ( FileChooserListener listener )
    {
        fileChooserPanel.addFileChooserListener ( listener );
    }

    public void removeFileChooserListener ( FileChooserListener listener )
    {
        fileChooserPanel.removeFileChooserListener ( listener );
    }

    public Painter getPainter ()
    {
        return fileChooserPanel.getPainter ();
    }

    public void setPainter ( Painter painter )
    {
        fileChooserPanel.setPainter ( painter );
    }

    public void setCurrentDirectory ( String dir )
    {
        fileChooserPanel.setCurrentDirectory ( dir );
    }

    public void setCurrentDirectory ( File dir )
    {
        fileChooserPanel.setCurrentDirectory ( dir );
    }

    public File getCurrentDirectory ()
    {
        return fileChooserPanel.getCurrentDirectory ();
    }

    public SelectionMode getSelectionMode ()
    {
        return fileChooserPanel.getSelectionMode ();
    }

    public void setSelectionMode ( SelectionMode selectionMode )
    {
        fileChooserPanel.setSelectionMode ( selectionMode );
    }

    public FilesToChoose getFilesToChoose ()
    {
        return fileChooserPanel.getFilesToChoose ();
    }

    public void setFilesToChoose ( FilesToChoose filesToChoose )
    {
        fileChooserPanel.setFilesToChoose ( filesToChoose );
    }

    public List<DefaultFileFilter> getAvailableFilters ()
    {
        return fileChooserPanel.getAvailableFilters ();
    }

    public void setAvailableFilter ( DefaultFileFilter availableFilter )
    {
        ArrayList<DefaultFileFilter> filters = new ArrayList<DefaultFileFilter> ();
        filters.add ( availableFilter );
        setAvailableFilters ( filters );
    }

    public void setAvailableFilters ( List<DefaultFileFilter> availableFilters )
    {
        if ( availableFilters.size () == 1 )
        {
            setIconImage ( availableFilters.get ( 0 ).getIcon ().getImage () );
        }
        fileChooserPanel.setAvailableFilters ( availableFilters );
    }

    public ActionListener getOkListener ()
    {
        return okListener;
    }

    public void setOkListener ( ActionListener okListener )
    {
        this.okListener = okListener;
    }

    public DefaultFileFilter getPreviewFilter ()
    {
        return fileChooserPanel.getPreviewFilter ();
    }

    public void setPreviewFilter ( DefaultFileFilter fileFilter )
    {
        fileChooserPanel.setPreviewFilter ( fileFilter );
    }

    public DefaultFileFilter getChooseFilter ()
    {
        return fileChooserPanel.getChooseFilter ();
    }

    public void setChooseFilter ( DefaultFileFilter fileChooseFilter )
    {
        fileChooserPanel.setChooseFilter ( fileChooseFilter );
    }

    public List<File> getSelectedFiles ()
    {
        return fileChooserPanel.getSelectedFiles ();
    }

    public File getSelectedFile ()
    {
        List<File> files = getSelectedFiles ();
        return files != null && files.size () > 0 ? files.get ( 0 ) : null;
    }

    public void setVisible ( boolean b )
    {
        if ( b )
        {
            result = StyleConstants.NONE_OPTION;
            setLocationRelativeTo ( getOwner () );
        }
        super.setVisible ( b );
    }
}
