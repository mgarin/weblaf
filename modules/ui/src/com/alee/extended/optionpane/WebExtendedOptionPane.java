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
import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.optionpane.WebOptionPaneUI;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.window.WebDialog;
import com.alee.managers.language.LM;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.collection.ImmutableList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * This class is an improved version of WebOptionPane. It has some additional features like special components positioning inside of the
 * message dialog, it also provides access to the dialog itself and adds some more options to play with.
 *
 * @author Mikle Garin
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

    private final Component parentComponent;

    private int result;

    private final WebPanel container;
    private final WebPanel controls;
    private final WebPanel centered;
    private final WebPanel buttons;

    private WebButton yes;
    private WebButton no;
    private WebButton ok;
    private WebButton cancel;

    private Component specialComponent = null;

    public WebExtendedOptionPane ( final Component parentComponent, final Object message, final Object special, final String title,
                                   final int optionType, final int messageType )
    {
        super ( CoreSwingUtils.getWindowAncestor ( parentComponent ), title );
        setIconImages ( getDialogIcons ( messageType ) );
        setLayout ( new BorderLayout () );

        this.parentComponent = parentComponent;

        container = new WebPanel ( new BorderLayout ( 15, 15 ) );
        container.setMargin ( 15, 15, 8, 15 );
        add ( container, BorderLayout.CENTER );

        // Icon
        final ImageIcon typeIcon = getLargeIcon ( messageType );
        if ( typeIcon != null )
        {
            final WebImage image = new WebImage ( typeIcon );
            image.setVerticalAlignment ( WebImage.TOP );
            container.add ( new CenterPanel ( image, false, true ), BorderLayout.WEST );
        }

        // Message
        setContent ( message, false );

        // South panel
        controls = new WebPanel ( new BorderLayout ( 15, 15 ) );
        container.add ( controls, BorderLayout.SOUTH );

        // Buttons
        centered = new WebPanel ();
        controls.add ( centered, BorderLayout.CENTER );
        buttons = new WebPanel ( new HorizontalFlowLayout ( 5, false ) );

        // Special content
        setSpecialComponent ( special );

        // Yes
        if ( optionType == YES_NO_OPTION || optionType == YES_NO_CANCEL_OPTION )
        {
            yes = createControlButton ( "weblaf.optionpane.yes" );
            yes.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
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
                @Override
                public void actionPerformed ( final ActionEvent e )
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
                @Override
                public void actionPerformed ( final ActionEvent e )
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
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    closeDialog ( CANCEL_OPTION );
                }
            } );
            buttons.add ( cancel );
        }

        // Equalize button widths
        final List<String> properties = new ImmutableList<String> ( AbstractButton.TEXT_CHANGED_PROPERTY );
        SwingUtils.equalizeComponentsWidth ( properties, buttons.getComponents ()  );

        // Dialog settings
        setModal ( true );
        setResizable ( false );
        setDefaultCloseOperation ( JDialog.DISPOSE_ON_CLOSE );
        pack ();
        setLocationRelativeTo ( CoreSwingUtils.getWindowAncestor ( parentComponent ) );
    }

    protected ImageIcon getLargeIcon ( final int messageType )
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

    public void setContent ( final Object message )
    {
        setContent ( message, true );
    }

    public void setSpecialComponent ( final Object special )
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
        updateCentered ();
    }

    private void updateCentered ()
    {
        centered.removeAll ();
        if ( specialComponent == null )
        {
            centered.setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL }, { TableLayout.PREFERRED } } ) );
        }
        else
        {
            centered.setLayout (
                    new TableLayout ( new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED }, { TableLayout.PREFERRED } } ) );
        }
        centered.add ( buttons, "1,0" );
    }

    private void setContent ( final Object message, final boolean updateWindow )
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
            setLocationRelativeTo ( CoreSwingUtils.getWindowAncestor ( parentComponent ) );
        }
    }

    private List<Image> getDialogIcons ( final int messageType )
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
        final ImageIcon bi = WebOptionPaneUI.getTypeIcon ( messageType );
        final Image big = bi != null ? bi.getImage () : null;

        return CollectionUtils.asList ( small, big );
    }

    private void closeDialog ( final int result )
    {
        this.result = result;
        dispose ();
    }

    private WebButton createControlButton ( final String key )
    {
        final WebButton cancel = new WebButton ( key );
        cancel.setMinimumSize ( new Dimension ( 70, 0 ) );
        return cancel;
    }

    public int getResult ()
    {
        return result;
    }

    @Override
    public void setVisible ( final boolean b )
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

    public static WebExtendedOptionPane showConfirmDialog ( final Component parentComponent, final Object message )
    {
        return showConfirmDialog ( parentComponent, message, null );
    }

    public static WebExtendedOptionPane showConfirmDialog ( final Component parentComponent, final Object message, final Object special )
    {
        return showConfirmDialog ( parentComponent, message, special, LM.get ( "weblaf.optionpane.title" ) );
    }

    public static WebExtendedOptionPane showConfirmDialog ( final Component parentComponent, final Object message, final Object special,
                                                            final String title )
    {
        return showConfirmDialog ( parentComponent, message, special, title, YES_NO_OPTION );
    }

    public static WebExtendedOptionPane showConfirmDialog ( final Component parentComponent, final Object message, final Object special,
                                                            final String title, final int optionType )
    {
        return showConfirmDialog ( parentComponent, message, special, title, optionType, QUESTION_MESSAGE );
    }

    public static WebExtendedOptionPane showConfirmDialog ( final Component parentComponent, final Object message, final Object special,
                                                            final String title, final int optionType, final int messageType )
    {
        final WebExtendedOptionPane confirmDialog =
                new WebExtendedOptionPane ( parentComponent, message, special, title, optionType, messageType );
        confirmDialog.setVisible ( true );
        return confirmDialog;
    }
}