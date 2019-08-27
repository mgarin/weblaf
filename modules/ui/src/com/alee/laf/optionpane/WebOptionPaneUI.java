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
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.LafLookup;
import com.alee.utils.ReflectUtils;
import com.alee.utils.TextUtils;
import com.alee.api.jdk.Consumer;

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
public class WebOptionPaneUI extends BasicOptionPaneUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Icons.
     */
    public static final ImageIcon INFORMATION_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/information.png" ) );
    public static final ImageIcon WARNING_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/warning.png" ) );
    public static final ImageIcon ERROR_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/error.png" ) );
    public static final ImageIcon QUESTION_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/question.png" ) );

    /**
     * Component painter.
     */
    @DefaultPainter ( OptionPanePainter.class )
    protected IOptionPanePainter painter;

    /**
     * Returns an instance of the {@link WebOptionPaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebOptionPaneUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebOptionPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( optionPane );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( optionPane );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( optionPane, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( optionPane, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( optionPane, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( optionPane );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( optionPane, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( optionPane );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( optionPane, padding );
    }

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
    protected void addMessageComponents ( final Container body, final GridBagConstraints cons, final Object msg, final int maxll,
                                          final boolean internallyCreated )
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
                if ( len <= 0 )
                {
                    return;
                }

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
                            @Override
                            public Dimension getPreferredSize ()
                            {
                                final Font f = getFont ();

                                if ( f != null )
                                {
                                    return new Dimension ( 1, f.getSize () + 2 );
                                }
                                return new Dimension ( 0, 0 );
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

    @Override
    protected void burstStringInto ( final Container c, final String msg, final int maxll )
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
    protected void burstStringInto ( final Container body, final Container c, final String msg, final int maxll )
    {
        // Primitive line wrapping
        final int len = msg.length ();
        if ( len <= 0 )
        {
            return;
        }
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
                return;
            }
        }
        c.add ( createMessageLabel ( body, msg ) );
    }

    /**
     * Returns new label component used for text message.
     *
     * @param body message body container
     * @param msg  message text
     * @return new label component used for text message
     */
    protected WebLabel createMessageLabel ( final Container body, final String msg )
    {
        final StyleId styleId = StyleId.optionpaneMessageLabel.at ( ( JComponent ) body );
        final WebLabel label = new WebLabel ( styleId, msg, JLabel.LEADING );
        label.setName ( "OptionPane.label" );
        return label;
    }

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
    protected void addButtonComponents ( final Container container, final Object[] buttons, final int initialIndex )
    {
        if ( container instanceof JComponent )
        {
            ( ( JComponent ) container ).setOpaque ( false );
        }
        if ( buttons != null && buttons.length > 0 )
        {
            final boolean sizeButtonsToSame = getSizeButtonsToSameWidth ();
            boolean createdAll = true;
            final int numButtons = buttons.length;
            JButton[] createdButtons = null;
            int maxWidth = 0;

            if ( sizeButtonsToSame )
            {
                createdButtons = new JButton[ numButtons ];
            }

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
                if ( sizeButtonsToSame && createdAll && newComponent instanceof JButton )
                {
                    createdButtons[ counter ] = ( JButton ) newComponent;
                    maxWidth = Math.max ( maxWidth, newComponent.getMinimumSize ().width );
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
            ( ( ButtonAreaLayout ) container.getLayout () ).setSyncAllWidths ( sizeButtonsToSame && createdAll );
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
    protected Object[] getButtons ( final WebPanel buttonArea )
    {
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

                return defaultOptions;
            }
            return suppliedOptions;
        }
        return null;
    }

    /**
     * Applies default option pane button settings.
     *
     * @param button button to configure
     */
    private void configureButton ( final WebButton button )
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
    public static ImageIcon getTypeIcon ( final int messageType )
    {
        if ( messageType < 0 || messageType > 3 )
        {
            return null;
        }
        switch ( messageType )
        {
            case 0:
                return ERROR_ICON;
            case 1:
                return INFORMATION_ICON;
            case 2:
                return WARNING_ICON;
            case 3:
                return QUESTION_ICON;
            default:
                return null;
        }
    }

    /**
     * Returns option pane painter.
     *
     * @return option pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets option pane painter.
     * Pass null to remove option pane painter.
     *
     * @param painter new option pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( optionPane, this, new Consumer<IOptionPanePainter> ()
        {
            @Override
            public void accept ( final IOptionPanePainter newPainter )
            {
                WebOptionPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IOptionPanePainter.class, AdaptiveOptionPanePainter.class );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}