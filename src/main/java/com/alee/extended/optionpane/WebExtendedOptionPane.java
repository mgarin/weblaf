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

package com.alee.extended.optionpane;

import com.alee.extended.image.WebImage;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.optionpane.WebOptionPaneUI;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.managers.language.LanguageManager;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * User: mgarin Date: 17.05.12 Time: 13:02
 * <p/>
 * This class extends WebOptionPane functionality with some additional features like special components positioning inside of the message
 * dialog, access to the dialog and more.
 */

public class WebExtendedOptionPane extends WebDialog
{
    public static final ImageIcon INFORMATION_ICON = new ImageIcon ( WebExtendedOptionPane.class.getResource ( "icons/information.png" ) );
    public static final ImageIcon WARNING_ICON = new ImageIcon ( WebExtendedOptionPane.class.getResource ( "icons/warning.png" ) );
    public static final ImageIcon ERROR_ICON = new ImageIcon ( WebExtendedOptionPane.class.getResource ( "icons/error.png" ) );
    public static final ImageIcon QUESTION_ICON = new ImageIcon ( WebExtendedOptionPane.class.getResource ( "icons/question.png" ) );

    public static final int YES_NO_OPTION = WebOptionPane.YES_NO_OPTION;
    public static final int YES_NO_CANCEL_OPTION = WebOptionPane.YES_NO_CANCEL_OPTION;
    public static final int OK_CANCEL_OPTION = WebOptionPane.OK_CANCEL_OPTION;

    public static final int YES_OPTION = WebOptionPane.YES_OPTION;
    public static final int NO_OPTION = WebOptionPane.NO_OPTION;
    public static final int CANCEL_OPTION = WebOptionPane.CANCEL_OPTION;
    public static final int OK_OPTION = WebOptionPane.OK_OPTION;
    public static final int CLOSED_OPTION = WebOptionPane.CLOSED_OPTION;

    public static final int ERROR_MESSAGE = WebOptionPane.ERROR;
    public static final int INFORMATION_MESSAGE = WebOptionPane.INFORMATION_MESSAGE;
    public static final int WARNING_MESSAGE = WebOptionPane.WARNING_MESSAGE;
    public static final int QUESTION_MESSAGE = WebOptionPane.QUESTION_MESSAGE;
    public static final int PLAIN_MESSAGE = WebOptionPane.PLAIN_MESSAGE;

    private Component parentComponent;

    private int result;

    private WebPanel container;
    private WebPanel controls;
    private WebPanel centerer;
    private WebPanel buttons;

    private WebButton yes;
    private WebButton no;
    private WebButton ok;
    private WebButton cancel;

    private Component specialComponent = null;

    public WebExtendedOptionPane ( DialogType dialogType, Component parentComponent, Object message, Object special, String title,
                                   int optionType, int messageType )
    {
        super ( SwingUtils.getWindowAncestor ( parentComponent ), title );
        setIconImages ( getDialogIcons ( messageType ) );
        setLayout ( new BorderLayout () );

        this.parentComponent = parentComponent;

        container = new WebPanel ( new BorderLayout ( 15, 15 ) );
        container.setMargin ( 15, 15, 8, 15 );
        add ( container, BorderLayout.CENTER );

        // Icon
        ImageIcon typeIcon = getLargeIcon ( messageType );
        if ( typeIcon != null )
        {
            WebImage image = new WebImage ( typeIcon );
            image.setVerticalAlignment ( WebImage.TOP );
            container.add ( new CenterPanel ( image, false, true ), BorderLayout.WEST );
        }

        // Message
        setContent ( message, false );

        // South panel
        controls = new WebPanel ( new BorderLayout ( 15, 15 ) );
        container.add ( controls, BorderLayout.SOUTH );

        // Buttons
        centerer = new WebPanel ();
        controls.add ( centerer, BorderLayout.CENTER );
        buttons = new WebPanel ( new HorizontalFlowLayout ( 5, false ) );

        // Special content
        setSpecialComponent ( special );

        // Yes
        if ( optionType == YES_NO_OPTION || optionType == YES_NO_CANCEL_OPTION )
        {
            yes = createControlButton ( "weblaf.optionpane.yes" );
            yes.addActionListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    closeDialog ( YES_OPTION );
                }
            } );
            buttons.add ( yes );
        }

        // No    
        if ( optionType == YES_NO_OPTION || optionType == YES_NO_CANCEL_OPTION )
        {
            no = createControlButton ( "weblaf.optionpane.no" );
            no.addActionListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    closeDialog ( NO_OPTION );
                }
            } );
            buttons.add ( no );
        }

        // Ok   
        if ( optionType == OK_CANCEL_OPTION )
        {
            ok = createControlButton ( "weblaf.optionpane.ok" );
            ok.addActionListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    closeDialog ( OK_OPTION );
                }
            } );
            buttons.add ( ok );
        }

        // Cancel     
        if ( optionType == YES_NO_CANCEL_OPTION || optionType == OK_CANCEL_OPTION )
        {
            cancel = createControlButton ( "weblaf.optionpane.cancel" );
            cancel.addActionListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    closeDialog ( CANCEL_OPTION );
                }
            } );
            buttons.add ( cancel );
        }

        // Equalize button widths
        SwingUtils.equalizeComponentsWidths ( buttons.getComponents () );

        // Dialog settings
        setModal ( true );
        setResizable ( false );
        setDefaultCloseOperation ( JDialog.DISPOSE_ON_CLOSE );
        pack ();
        setLocationRelativeTo ( SwingUtils.getWindowAncestor ( parentComponent ) );
    }

    protected ImageIcon getLargeIcon ( int messageType )
    {
        return WebOptionPaneUI.getTypeIcon ( messageType );
    }

    public void clickYes ()
    {
        yes.doClick ();
    }

    public void clickNo ()
    {
        no.doClick ();
    }

    public void clickOk ()
    {
        ok.doClick ();
    }

    public void clickCancel ()
    {
        cancel.doClick ();
    }

    public void setContent ( Object message )
    {
        setContent ( message, true );
    }

    public void setSpecialComponent ( Object special )
    {
        if ( this.specialComponent != null )
        {
            controls.remove ( specialComponent );
            this.specialComponent = null;
        }
        if ( special != null )
        {
            if ( special instanceof Component )
            {
                specialComponent = ( Component ) special;
            }
            else
            {
                specialComponent = new WebLabel ( special.toString () );
            }
            controls.add ( specialComponent, BorderLayout.WEST );
        }
        updateCenterer ();
    }

    private void updateCenterer ()
    {
        centerer.removeAll ();
        if ( specialComponent == null )
        {
            centerer.setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL }, { TableLayout.PREFERRED } } ) );
        }
        else
        {
            centerer.setLayout (
                    new TableLayout ( new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED }, { TableLayout.PREFERRED } } ) );
        }
        centerer.add ( buttons, "1,0" );
    }

    private void setContent ( Object message, boolean updateWindow )
    {
        // Update content
        if ( message != null )
        {
            if ( message instanceof Component )
            {
                container.add ( ( Component ) message, BorderLayout.CENTER );
            }
            else
            {
                container.add ( new WebLabel ( message.toString () ), BorderLayout.CENTER );
            }
        }

        // Update window
        if ( updateWindow )
        {
            pack ();
            setLocationRelativeTo ( SwingUtils.getWindowAncestor ( parentComponent ) );
        }
    }

    private List<Image> getDialogIcons ( int messageType )
    {
        // Small icon
        Image small = null;
        switch ( messageType )
        {
            case ERROR_MESSAGE:
            {
                small = ERROR_ICON.getImage ();
                break;
            }
            case INFORMATION_MESSAGE:
            {
                small = INFORMATION_ICON.getImage ();
                break;
            }
            case WARNING_MESSAGE:
            {
                small = WARNING_ICON.getImage ();
                break;
            }
            case QUESTION_MESSAGE:
            {
                small = QUESTION_ICON.getImage ();
                break;
            }
        }

        // Big icon
        ImageIcon bi = WebOptionPaneUI.getTypeIcon ( messageType );
        Image big = bi != null ? bi.getImage () : null;

        return CollectionUtils.copy ( small, big );
    }

    private void closeDialog ( int result )
    {
        this.result = result;
        dispose ();
    }

    private WebButton createControlButton ( String key )
    {
        WebButton cancel = new WebButton ();
        cancel.setLanguage ( key );
        cancel.setLeftRightSpacing ( 10 );
        cancel.setMinimumSize ( new Dimension ( 70, 0 ) );
        return cancel;
    }

    public int getResult ()
    {
        return result;
    }

    public void setVisible ( boolean b )
    {
        if ( b )
        {
            // Default result
            result = WebOptionPane.CLOSED_OPTION;

            // Default focus
            buttons.getComponents ()[ 0 ].requestFocusInWindow ();
        }
        super.setVisible ( b );
    }

    public static WebExtendedOptionPane showConfirmDialog ( Component parentComponent, Object message )
    {
        return showConfirmDialog ( parentComponent, message, null );
    }

    public static WebExtendedOptionPane showConfirmDialog ( Component parentComponent, Object message, Object special )
    {
        return showConfirmDialog ( parentComponent, message, special, LanguageManager.get ( "weblaf.optionpane.title" ) );
    }

    public static WebExtendedOptionPane showConfirmDialog ( Component parentComponent, Object message, Object special, String title )
    {
        return showConfirmDialog ( parentComponent, message, special, title, YES_NO_OPTION );
    }

    public static WebExtendedOptionPane showConfirmDialog ( Component parentComponent, Object message, Object special, String title,
                                                            int optionType )
    {
        return showConfirmDialog ( parentComponent, message, special, title, optionType, QUESTION_MESSAGE );
    }

    public static WebExtendedOptionPane showConfirmDialog ( Component parentComponent, Object message, Object special, String title,
                                                            int optionType, int messageType )
    {
        WebExtendedOptionPane confirmDialog =
                new WebExtendedOptionPane ( DialogType.confirm, parentComponent, message, special, title, optionType, messageType );
        confirmDialog.setVisible ( true );
        return confirmDialog;
    }
}