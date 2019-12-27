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

package com.alee.laf.optionpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.*;
import com.alee.painter.PainterSupport;
import com.alee.utils.LafLookup;
import com.alee.utils.ReflectUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * Custom UI for {@link JOptionPane} component.
 *
 * @author Mikle Garin
 */
public class WebOptionPaneUI extends BasicOptionPaneUI
{
    /**
     * Icons.
     */
    public static final ImageIcon INFORMATION_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/information.png" ) );
    public static final ImageIcon WARNING_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/warning.png" ) );
    public static final ImageIcon ERROR_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/error.png" ) );
    public static final ImageIcon QUESTION_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/question.png" ) );

    /**
     * Returns an instance of the {@link WebOptionPaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebOptionPaneUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebOptionPaneUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( optionPane );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( optionPane );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @NotNull
    @Override
    protected Container createMessageArea ()
    {
        final WebPanel messageArea = new WebPanel ( StyleId.optionpaneMessageArea.at ( optionPane ), new BorderLayout () );

        final Icon sideIcon = getIcon ();
        if ( sideIcon != null )
        {
            final WebLabel iconLabel = new WebLabel ( StyleId.optionpaneIconLabel.at ( messageArea ), sideIcon );
            iconLabel.setName ( "OptionPane.iconLabel" );
            iconLabel.setVerticalAlignment ( SwingConstants.TOP );
            messageArea.add ( iconLabel, BorderLayout.BEFORE_LINE_BEGINS );
        }

        final WebPanel realBody = new WebPanel ( StyleId.optionpaneRealBody.at ( messageArea ), new BorderLayout () );
        realBody.setName ( "OptionPane.realBody" );

        if ( sideIcon != null )
        {
            final WebPanel sep = new WebPanel ( StyleId.optionpaneSeparator.at ( realBody ) );
            sep.setName ( "OptionPane.separator" );
            realBody.add ( sep, BorderLayout.BEFORE_LINE_BEGINS );
        }

        final WebPanel body = new WebPanel ( StyleId.optionpaneBody.at ( realBody ), new GridBagLayout () );
        body.setName ( "OptionPane.body" );

        final GridBagConstraints cons = new GridBagConstraints ();
        cons.gridx = cons.gridy = 0;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.gridheight = 1;
        cons.anchor = LafLookup.getInt ( optionPane, this, "OptionPane.messageAnchor", GridBagConstraints.CENTER );
        cons.insets = new Insets ( 0, 0, 3, 0 );
        addMessageComponents ( body, cons, getMessage (), getMaxCharactersPerLineCount (), false );

        realBody.add ( body, BorderLayout.CENTER );

        messageArea.add ( realBody, BorderLayout.CENTER );

        return messageArea;
    }

    @Override
    protected void addIcon ( final Container messageArea )
    {
        // todo Temporary placeholder to pinpoint possible issues
        throw new RuntimeException ( "This method is not supported by WebLookAndFeel" );
    }

    @Override
    protected void addMessageComponents ( @NotNull final Container body, @NotNull final GridBagConstraints cons, @Nullable final Object msg,
                                          final int maxll, final boolean internallyCreated )
    {
        if ( msg != null )
        {
            if ( msg instanceof Component )
            {
                // To workaround problem where Gridbad will set child to its minimum size
                // if its preferred size will not fit within allocated cells
                if ( msg instanceof JScrollPane || msg instanceof JPanel )
                {
                    cons.fill = GridBagConstraints.BOTH;
                    cons.weighty = 1;
                }
                else
                {
                    cons.fill = GridBagConstraints.HORIZONTAL;
                }
                cons.weightx = 1;
                body.add ( ( Component ) msg, cons );
                cons.weightx = 0;
                cons.weighty = 0;
                cons.fill = GridBagConstraints.NONE;
                cons.gridy++;
                if ( !internallyCreated )
                {
                    hasCustomComponents = true;
                }
            }
            else if ( msg instanceof Object[] )
            {
                final Object[] msgs = ( Object[] ) msg;
                for ( final Object smsg : msgs )
                {
                    addMessageComponents ( body, cons, smsg, maxll, false );
                }
            }
            else if ( msg instanceof Icon )
            {
                final JLabel label = new JLabel ( ( Icon ) msg, JLabel.CENTER );
                addMessageComponents ( body, cons, label, maxll, true );
            }
            else
            {
                final String s = msg.toString ();
                final int len = s.length ();
                if ( len > 0 )
                {
                    int nl;
                    int nll = 0;
                    final String newline = TextUtils.getSystemLineSeparator ();
                    if ( ( nl = s.indexOf ( newline ) ) >= 0 )
                    {
                        nll = newline.length ();
                    }
                    else if ( ( nl = s.indexOf ( "\r\n" ) ) >= 0 )
                    {
                        nll = 2;
                    }
                    else if ( ( nl = s.indexOf ( '\n' ) ) >= 0 )
                    {
                        nll = 1;
                    }
                    if ( nl >= 0 )
                    {
                        // break up newlines
                        if ( nl == 0 )
                        {
                            final JPanel breakPanel = new JPanel ()
                            {
                                @NotNull
                                @Override
                                public Dimension getPreferredSize ()
                                {
                                    final Dimension ps;
                                    final Font f = getFont ();
                                    if ( f != null )
                                    {
                                        ps = new Dimension ( 1, f.getSize () + 2 );
                                    }
                                    else
                                    {
                                        ps = new Dimension ( 0, 0 );
                                    }
                                    return ps;
                                }
                            };
                            breakPanel.setName ( "OptionPane.break" );
                            addMessageComponents ( body, cons, breakPanel, maxll, true );
                        }
                        else
                        {
                            addMessageComponents ( body, cons, s.substring ( 0, nl ), maxll, false );
                        }
                        addMessageComponents ( body, cons, s.substring ( nl + nll ), maxll, false );
                    }
                    else if ( len > maxll )
                    {
                        final Container c = Box.createVerticalBox ();
                        c.setName ( "OptionPane.verticalBox" );
                        burstStringInto ( body, c, s, maxll );
                        addMessageComponents ( body, cons, c, maxll, true );
                    }
                    else
                    {
                        final JLabel label = createMessageLabel ( body, s );
                        addMessageComponents ( body, cons, label, maxll, true );
                    }
                }
            }
        }
    }

    @Override
    protected void burstStringInto ( @NotNull final Container c, @Nullable final String msg, final int maxll )
    {
        // todo Temporary placeholder to pinpoint possible issues
        throw new RuntimeException ( "This method is not supported by WebLookAndFeel" );
    }

    /**
     * Recursively creates new JLabel instances to represent {@code d}.
     * Each JLabel instance is added to {@code c}.
     *
     * @param body  message body container
     * @param c     container to put separated labels into
     * @param msg   message text
     * @param maxll maximum characters per line
     */
    protected void burstStringInto ( @NotNull final Container body, @NotNull final Container c, @NotNull final String msg, final int maxll )
    {
        // Primitive line wrapping
        final int len = msg.length ();
        if ( len > 0 )
        {
            if ( len > maxll )
            {
                int p = msg.lastIndexOf ( ' ', maxll );
                if ( p <= 0 )
                {
                    p = msg.indexOf ( ' ', maxll );
                }
                if ( p > 0 && p < len )
                {
                    burstStringInto ( c, msg.substring ( 0, p ), maxll );
                    burstStringInto ( c, msg.substring ( p + 1 ), maxll );
                }
                else
                {
                    c.add ( createMessageLabel ( body, msg ) );
                }
            }
            else
            {
                c.add ( createMessageLabel ( body, msg ) );
            }
        }
    }

    /**
     * Returns new label component used for text message.
     *
     * @param body message body container
     * @param msg  message text
     * @return new label component used for text message
     */
    @NotNull
    protected WebLabel createMessageLabel ( @NotNull final Container body, @NotNull final String msg )
    {
        final StyleId styleId = StyleId.optionpaneMessageLabel.at ( ( JComponent ) body );
        final WebLabel label = new WebLabel ( styleId, msg, JLabel.LEADING );
        label.setName ( "OptionPane.label" );
        return label;
    }

    @NotNull
    @Override
    protected Container createButtonArea ()
    {
        final boolean sameSizeButtons = LafLookup.getBoolean ( optionPane, this, "OptionPane.sameSizeButtons", true );
        final int buttonPadding = LafLookup.getInt ( optionPane, this, "OptionPane.buttonPadding", 6 );
        final int buttonOrientation = LafLookup.getInt ( optionPane, this, "OptionPane.buttonOrientation", SwingConstants.CENTER );
        final boolean isYesLast = LafLookup.getBoolean ( optionPane, this, "OptionPane.isYesLast", false );
        final ButtonAreaLayout layout = ReflectUtils.createInstanceSafely ( ButtonAreaLayout.class, sameSizeButtons,
                buttonPadding, buttonOrientation, isYesLast );

        final WebPanel buttonArea = new WebPanel ( StyleId.optionpaneButtonArea.at ( optionPane ), layout );
        buttonArea.setName ( "OptionPane.buttonArea" );

        addButtonComponents ( buttonArea, getButtons ( buttonArea ), getInitialValueIndex () );

        return buttonArea;
    }

    @Override
    protected void addButtonComponents ( @NotNull final Container container, @Nullable final Object[] buttons, final int initialIndex )
    {
        if ( container instanceof JComponent )
        {
            ( ( JComponent ) container ).setOpaque ( false );
        }
        if ( buttons != null && buttons.length > 0 )
        {
            boolean createdAll = true;
            final int numButtons = buttons.length;
            for ( int counter = 0; counter < numButtons; counter++ )
            {
                final Object button = buttons[ counter ];
                final Component newComponent;
                if ( button instanceof Component )
                {
                    createdAll = false;
                    newComponent = ( Component ) button;
                    container.add ( newComponent );
                    hasCustomComponents = true;
                }
                else
                {
                    final WebButton aButton;
                    if ( button instanceof Icon )
                    {
                        final Icon icon = ( Icon ) button;
                        aButton = new WebButton ( icon );
                    }
                    else
                    {
                        final String text = button.toString ();
                        aButton = new WebButton ( text );
                    }

                    aButton.setName ( "OptionPane.button" );
                    aButton.setMultiClickThreshhold ( UIManager.getInt ( "OptionPane.buttonClickThreshold" ) );
                    configureButton ( aButton );

                    container.add ( aButton );

                    final ActionListener buttonListener = createButtonActionListener ( counter );
                    if ( buttonListener != null )
                    {
                        aButton.addActionListener ( buttonListener );
                    }
                    newComponent = aButton;
                }
                if ( counter == initialIndex )
                {
                    initialFocusComponent = newComponent;
                    if ( initialFocusComponent instanceof JButton )
                    {
                        final JButton defaultB = ( JButton ) initialFocusComponent;
                        defaultB.addHierarchyListener ( new HierarchyListener ()
                        {
                            @Override
                            public void hierarchyChanged ( final HierarchyEvent e )
                            {
                                if ( ( e.getChangeFlags () & HierarchyEvent.PARENT_CHANGED ) != 0 )
                                {
                                    final JButton defaultButton = ( JButton ) e.getComponent ();
                                    final JRootPane root = SwingUtilities.getRootPane ( defaultButton );
                                    if ( root != null )
                                    {
                                        root.setDefaultButton ( defaultButton );
                                    }
                                }
                            }
                        } );
                    }
                }
            }

            // Equalizing button sizes through layout
            final ButtonAreaLayout layout = ( ButtonAreaLayout ) container.getLayout ();
            layout.setSyncAllWidths ( createdAll && getSizeButtonsToSameWidth () );
        }
    }

    @Override
    protected Object[] getButtons ()
    {
        // todo Temporary placeholder to pinpoint possible issues
        throw new RuntimeException ( "This method is not supported by WebLookAndFeel" );
    }

    /**
     * Returns the buttons to display from the JOptionPane the receiver is providing the look and feel for.
     * If the JOptionPane has options set, they will be provided, otherwise if the optionType is YES_NO_OPTION, yesNoOptions is returned,
     * if the type is YES_NO_CANCEL_OPTION yesNoCancelOptions is returned, otherwise defaultButtons are returned.
     *
     * @param buttonArea buttons container
     * @return the buttons to display from the JOptionPane the receiver is providing the look and feel for
     */
    @Nullable
    protected Object[] getButtons ( @NotNull final WebPanel buttonArea )
    {
        Object[] buttons = null;
        if ( optionPane != null )
        {
            final Object[] suppliedOptions = optionPane.getOptions ();
            if ( suppliedOptions == null )
            {
                // Initializing buttons
                final WebButton[] defaultOptions;
                final int type = optionPane.getOptionType ();
                if ( type == JOptionPane.YES_NO_OPTION )
                {
                    defaultOptions = new WebButton[ 2 ];
                    defaultOptions[ 0 ] = new WebButton ( StyleId.optionpaneYesButton.at ( buttonArea ), "weblaf.optionpane.yes" );
                    defaultOptions[ 1 ] = new WebButton ( StyleId.optionpaneNoButton.at ( buttonArea ), "weblaf.optionpane.no" );
                }
                else if ( type == JOptionPane.YES_NO_CANCEL_OPTION )
                {
                    defaultOptions = new WebButton[ 3 ];
                    defaultOptions[ 0 ] = new WebButton ( StyleId.optionpaneYesButton.at ( buttonArea ), "weblaf.optionpane.yes" );
                    defaultOptions[ 1 ] = new WebButton ( StyleId.optionpaneNoButton.at ( buttonArea ), "weblaf.optionpane.no" );
                    defaultOptions[ 2 ] = new WebButton ( StyleId.optionpaneCancelButton.at ( buttonArea ), "weblaf.optionpane.cancel" );
                }
                else if ( type == JOptionPane.OK_CANCEL_OPTION )
                {
                    defaultOptions = new WebButton[ 2 ];
                    defaultOptions[ 0 ] = new WebButton ( StyleId.optionpaneOkButton.at ( buttonArea ), "weblaf.optionpane.ok" );
                    defaultOptions[ 1 ] = new WebButton ( StyleId.optionpaneCancelButton.at ( buttonArea ), "weblaf.optionpane.cancel" );
                }
                else
                {
                    defaultOptions = new WebButton[ 1 ];
                    defaultOptions[ 0 ] = new WebButton ( StyleId.optionpaneOkButton.at ( buttonArea ), "weblaf.optionpane.ok" );
                }

                // Configuring created buttons
                for ( int i = 0; i < defaultOptions.length; i++ )
                {
                    configureButton ( defaultOptions[ i ] );
                    defaultOptions[ i ].addActionListener ( createButtonActionListener ( i ) );
                }

                buttons = defaultOptions;
            }
            else
            {
                buttons = suppliedOptions;
            }
        }
        return buttons;
    }

    /**
     * Applies default option pane button settings.
     *
     * @param button button to configure
     */
    private void configureButton ( @NotNull final WebButton button )
    {
        // Minimum size
        button.setMinimumSize ( new Dimension ( 70, 0 ) );

        // Proper font
        final Font buttonFont = UIManager.getFont ( "OptionPane.buttonFont" );
        if ( buttonFont != null )
        {
            button.setFont ( buttonFont );
        }
    }

    @Override
    protected Icon getIconForType ( final int messageType )
    {
        return getTypeIcon ( messageType );
    }

    /**
     * Returns icon for specified option pane message type.
     *
     * @param messageType option pane message type
     * @return icon for specified option pane message type
     */
    @Nullable
    public static ImageIcon getTypeIcon ( final int messageType )
    {
        ImageIcon icon = null;
        if ( messageType >= 0 && messageType <= 3 )
        {
            switch ( messageType )
            {
                case 0:
                    icon = ERROR_ICON;
                    break;

                case 1:
                    icon = INFORMATION_ICON;
                    break;

                case 2:
                    icon = WARNING_ICON;
                    break;

                case 3:
                    icon = QUESTION_ICON;
                    break;
            }
        }
        return icon;
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, width, height );
    }

    @NotNull
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ) );
    }
}