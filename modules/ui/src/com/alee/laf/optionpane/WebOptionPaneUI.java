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

import com.alee.laf.button.WebButton;
import com.alee.managers.language.LM;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * @author Mikle Garin
 */

public class WebOptionPaneUI extends BasicOptionPaneUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
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
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebOptionPaneUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebOptionPaneUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebOptionPaneUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( optionPane );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( optionPane );

        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( optionPane );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( optionPane, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( optionPane, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    protected Container createMessageArea ()
    {
        // todo Really bad workaround
        final Container messageArea = super.createMessageArea ();
        SwingUtils.setOpaqueRecursively ( messageArea, false );
        return messageArea;
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
                        aButton = new WebButton ( ( Icon ) button );
                    }
                    else
                    {
                        final String text = button.toString ();
                        aButton = new WebButton ( text );
                        if ( LM.contains ( text ) )
                        {
                            aButton.setLanguage ( text );
                        }
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
        final JOptionPane op = optionPane;
        if ( op != null )
        {
            final Object[] suppliedOptions = op.getOptions ();
            if ( suppliedOptions == null )
            {
                // Initializing buttons
                final WebButton[] defaultOptions;
                final int type = op.getOptionType ();
                if ( type == JOptionPane.YES_NO_OPTION )
                {
                    defaultOptions = new WebButton[ 2 ];
                    defaultOptions[ 0 ] = new WebButton ( StyleId.optionpaneYesButton.at ( op ), "weblaf.optionpane.yes" );
                    defaultOptions[ 1 ] = new WebButton ( StyleId.optionpaneNoButton.at ( op ), "weblaf.optionpane.no" );
                }
                else if ( type == JOptionPane.YES_NO_CANCEL_OPTION )
                {
                    defaultOptions = new WebButton[ 3 ];
                    defaultOptions[ 0 ] = new WebButton ( StyleId.optionpaneYesButton.at ( op ), "weblaf.optionpane.yes" );
                    defaultOptions[ 1 ] = new WebButton ( StyleId.optionpaneNoButton.at ( op ), "weblaf.optionpane.no" );
                    defaultOptions[ 2 ] = new WebButton ( StyleId.optionpaneCancelButton.at ( op ), "weblaf.optionpane.cancel" );
                }
                else if ( type == JOptionPane.OK_CANCEL_OPTION )
                {
                    defaultOptions = new WebButton[ 2 ];
                    defaultOptions[ 0 ] = new WebButton ( StyleId.optionpaneOkButton.at ( op ), "weblaf.optionpane.ok" );
                    defaultOptions[ 1 ] = new WebButton ( StyleId.optionpaneCancelButton.at ( op ), "weblaf.optionpane.cancel" );
                }
                else
                {
                    defaultOptions = new WebButton[ 1 ];
                    defaultOptions[ 0 ] = new WebButton ( StyleId.optionpaneOkButton.at ( op ), "weblaf.optionpane.ok" );
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
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets option pane painter.
     * Pass null to remove option pane painter.
     *
     * @param painter new option pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( optionPane, new DataRunnable<IOptionPanePainter> ()
        {
            @Override
            public void run ( final IOptionPanePainter newPainter )
            {
                WebOptionPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IOptionPanePainter.class, AdaptiveOptionPanePainter.class );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}