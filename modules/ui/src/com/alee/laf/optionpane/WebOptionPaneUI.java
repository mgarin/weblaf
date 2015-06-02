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

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.laf.Styles;
import com.alee.laf.button.WebButton;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.PaddingSupport;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * User: mgarin Date: 17.08.11 Time: 22:46
 */

public class WebOptionPaneUI extends BasicOptionPaneUI implements Styleable, ShapeProvider, PaddingSupport
{
    public static final ImageIcon INFORMATION_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/information.png" ) );
    public static final ImageIcon WARNING_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/warning.png" ) );
    public static final ImageIcon ERROR_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/error.png" ) );
    public static final ImageIcon QUESTION_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/question.png" ) );

    /**
     * Component painter.
     */
    protected OptionPanePainter painter;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebOptionPaneUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebOptionPaneUI
     */
    @SuppressWarnings ( "UnusedParameters" )
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
        StyleManager.applySkin ( optionPane );
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
        StyleManager.removeSkin ( optionPane );

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return styleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        if ( !CompareUtils.equals ( this.styleId, id ) )
        {
            this.styleId = id;
            StyleManager.applySkin ( optionPane );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( optionPane, painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
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
        PainterSupport.setPainter ( optionPane, new DataRunnable<OptionPanePainter> ()
        {
            @Override
            public void run ( final OptionPanePainter newPainter )
            {
                WebOptionPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, OptionPanePainter.class, AdaptiveOptionPanePainter.class );
    }

    /**
     * Paints option pane.
     *
     * @param g graphic context
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
        }
    }

    @Override
    protected Container createMessageArea ()
    {
        // todo Really bad workaround
        final Container messageArea = super.createMessageArea ();
        SwingUtils.setOpaqueRecursively ( messageArea, false );
        return messageArea;
    }

    /**
     * Modified buttons creation method
     */

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
                    aButton.setMultiClickThreshhold ( UIManager.getInt ( "OptionPane.buttonClickThreshhold" ) );
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
            ( ( ButtonAreaLayout ) container.getLayout () ).setSyncAllWidths ( ( sizeButtonsToSame && createdAll ) );
        }
    }

    /**
     * Modified dialog buttons
     */

    @Override
    protected Object[] getButtons ()
    {
        if ( optionPane != null )
        {
            final Object[] suppliedOptions = optionPane.getOptions ();

            if ( suppliedOptions == null )
            {
                final WebButton[] defaultOptions;
                final int type = optionPane.getOptionType ();
                if ( type == JOptionPane.YES_NO_OPTION )
                {
                    defaultOptions = new WebButton[ 2 ];
                    defaultOptions[ 0 ] = new WebButton ( "weblaf.optionpane.yes" );
                    defaultOptions[ 0 ].setStyleId ( Styles.optionpaneYesButton );
                    defaultOptions[ 1 ] = new WebButton ( "weblaf.optionpane.no" );
                    defaultOptions[ 1 ].setStyleId ( Styles.optionpaneNoButton );
                }
                else if ( type == JOptionPane.YES_NO_CANCEL_OPTION )
                {
                    defaultOptions = new WebButton[ 3 ];
                    defaultOptions[ 0 ] = new WebButton ( "weblaf.optionpane.yes" );
                    defaultOptions[ 0 ].setStyleId ( Styles.optionpaneYesButton );
                    defaultOptions[ 1 ] = new WebButton ( "weblaf.optionpane.no" );
                    defaultOptions[ 1 ].setStyleId ( Styles.optionpaneNoButton );
                    defaultOptions[ 2 ] = new WebButton ( "weblaf.optionpane.cancel" );
                    defaultOptions[ 2 ].setStyleId ( Styles.optionpaneCancelButton );
                }
                else if ( type == JOptionPane.OK_CANCEL_OPTION )
                {
                    defaultOptions = new WebButton[ 2 ];
                    defaultOptions[ 0 ] = new WebButton ( "weblaf.optionpane.ok" );
                    defaultOptions[ 0 ].setStyleId ( Styles.optionpaneOkButton );
                    defaultOptions[ 1 ] = new WebButton ( "weblaf.optionpane.cancel" );
                    defaultOptions[ 1 ].setStyleId ( Styles.optionpaneCancelButton );
                }
                else
                {
                    defaultOptions = new WebButton[ 1 ];
                    defaultOptions[ 0 ] = new WebButton ( "weblaf.optionpane.ok" );
                    defaultOptions[ 0 ].setStyleId ( Styles.optionpaneOkButton );
                }

                int count = 0;
                for ( final WebButton button : defaultOptions )
                {
                    configureButton ( button );
                    button.addActionListener ( createButtonActionListener ( count ) );
                    count++;
                }

                return defaultOptions;

            }
            return suppliedOptions;
        }
        return null;
    }

    /**
     * Default option pane button settings
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

    /**
     * Modified dialogs side icons
     */

    @Override
    protected Icon getIconForType ( final int messageType )
    {
        return getTypeIcon ( messageType );
    }

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
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}
