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

import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.utils.SwingUtils;

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

public class WebOptionPaneUI extends BasicOptionPaneUI
{
    public static final ImageIcon INFORMATION_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/information.png" ) );
    public static final ImageIcon WARNING_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/warning.png" ) );
    public static final ImageIcon ERROR_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/error.png" ) );
    public static final ImageIcon QUESTION_ICON = new ImageIcon ( WebOptionPaneUI.class.getResource ( "icons/question.png" ) );

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebOptionPaneUI ();
    }

    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( optionPane );
        optionPane.setOpaque ( false );
        optionPane.setBackground ( WebOptionPaneStyle.backgroundColor );

        // Updating border
        optionPane.setBorder ( BorderFactory.createEmptyBorder ( 15, 15, 15, 15 ) );
    }

    protected Container createMessageArea ()
    {
        Container messageArea = super.createMessageArea ();
        SwingUtils.setOpaqueRecursively ( messageArea, false );
        return messageArea;
    }

    /**
     * Modified buttons creation method
     */

    protected void addButtonComponents ( Container container, Object[] buttons, int initialIndex )
    {
        if ( container instanceof JComponent )
        {
            ( ( JComponent ) container ).setOpaque ( false );
        }
        if ( buttons != null && buttons.length > 0 )
        {
            boolean sizeButtonsToSame = getSizeButtonsToSameWidth ();
            boolean createdAll = true;
            int numButtons = buttons.length;
            JButton[] createdButtons = null;
            int maxWidth = 0;

            if ( sizeButtonsToSame )
            {
                createdButtons = new JButton[ numButtons ];
            }

            for ( int counter = 0; counter < numButtons; counter++ )
            {
                Object button = buttons[ counter ];
                Component newComponent;

                if ( button instanceof Component )
                {
                    createdAll = false;
                    newComponent = ( Component ) button;
                    container.add ( newComponent );
                    hasCustomComponents = true;

                }
                else
                {
                    WebButton aButton;
                    if ( button instanceof Icon )
                    {
                        aButton = new WebButton ( ( Icon ) button );
                    }
                    else
                    {
                        aButton = new WebButton ( button.toString () );
                    }

                    aButton.setName ( "OptionPane.button" );
                    aButton.setMultiClickThreshhold ( UIManager.getInt ( "OptionPane.buttonClickThreshhold" ) );
                    configureButton ( aButton );

                    container.add ( aButton );

                    ActionListener buttonListener = createButtonActionListener ( counter );
                    if ( buttonListener != null )
                    {
                        aButton.addActionListener ( buttonListener );
                    }
                    newComponent = aButton;
                }
                if ( sizeButtonsToSame && createdAll &&
                        ( newComponent instanceof JButton ) )
                {
                    createdButtons[ counter ] = ( JButton ) newComponent;
                    maxWidth = Math.max ( maxWidth, newComponent.getMinimumSize ().width );
                }
                if ( counter == initialIndex )
                {
                    initialFocusComponent = newComponent;
                    if ( initialFocusComponent instanceof JButton )
                    {
                        JButton defaultB = ( JButton ) initialFocusComponent;
                        defaultB.addHierarchyListener ( new HierarchyListener ()
                        {
                            public void hierarchyChanged ( HierarchyEvent e )
                            {
                                if ( ( e.getChangeFlags () & HierarchyEvent.PARENT_CHANGED ) != 0 )
                                {
                                    JButton defaultButton = ( JButton ) e.getComponent ();
                                    JRootPane root = SwingUtilities.getRootPane ( defaultButton );
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

    protected Object[] getButtons ()
    {
        if ( optionPane != null )
        {
            Object[] suppliedOptions = optionPane.getOptions ();

            if ( suppliedOptions == null )
            {
                WebButton[] defaultOptions;
                int type = optionPane.getOptionType ();
                if ( type == JOptionPane.YES_NO_OPTION )
                {
                    defaultOptions = new WebButton[ 2 ];

                    defaultOptions[ 0 ] = new WebButton ( "" );
                    defaultOptions[ 0 ].setShineColor ( StyleConstants.greenHighlight );
                    defaultOptions[ 0 ].setLanguage ( "weblaf.optionpane.yes" );

                    defaultOptions[ 1 ] = new WebButton ( "" );
                    defaultOptions[ 1 ].setShineColor ( StyleConstants.redHighlight );
                    defaultOptions[ 1 ].setLanguage ( "weblaf.optionpane.no" );
                }
                else if ( type == JOptionPane.YES_NO_CANCEL_OPTION )
                {
                    defaultOptions = new WebButton[ 3 ];

                    defaultOptions[ 0 ] = new WebButton ( "" );
                    defaultOptions[ 0 ].setShineColor ( StyleConstants.greenHighlight );
                    defaultOptions[ 0 ].setLanguage ( "weblaf.optionpane.yes" );

                    defaultOptions[ 1 ] = new WebButton ( "" );
                    defaultOptions[ 1 ].setShineColor ( StyleConstants.redHighlight );
                    defaultOptions[ 1 ].setLanguage ( "weblaf.optionpane.no" );

                    defaultOptions[ 2 ] = new WebButton ( "" );
                    defaultOptions[ 2 ].setShineColor ( StyleConstants.yellowHighlight );
                    defaultOptions[ 2 ].setLanguage ( "weblaf.optionpane.cancel" );
                }
                else if ( type == JOptionPane.OK_CANCEL_OPTION )
                {
                    defaultOptions = new WebButton[ 2 ];

                    defaultOptions[ 0 ] = new WebButton ( "" );
                    defaultOptions[ 0 ].setShineColor ( StyleConstants.greenHighlight );
                    defaultOptions[ 0 ].setLanguage ( "weblaf.optionpane.ok" );

                    defaultOptions[ 1 ] = new WebButton ( "" );
                    defaultOptions[ 1 ].setShineColor ( StyleConstants.redHighlight );
                    defaultOptions[ 1 ].setLanguage ( "weblaf.optionpane.cancel" );
                }
                else
                {
                    defaultOptions = new WebButton[ 1 ];

                    defaultOptions[ 0 ] = new WebButton ( "" );
                    defaultOptions[ 0 ].setShineColor ( StyleConstants.greenHighlight );
                    defaultOptions[ 0 ].setLanguage ( "weblaf.optionpane.ok" );
                }

                int count = 0;
                for ( WebButton button : defaultOptions )
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

    private void configureButton ( WebButton button )
    {
        button.setLeftRightSpacing ( 10 );
        button.setMinimumSize ( new Dimension ( 70, 0 ) );
        button.setRolloverShine ( WebOptionPaneStyle.highlightControlButtons );

        Font buttonFont = UIManager.getFont ( "OptionPane.buttonFont" );
        if ( buttonFont != null )
        {
            button.setFont ( buttonFont );
        }
    }

    /**
     * Modified dialogs side icons
     */

    protected Icon getIconForType ( int messageType )
    {
        return getTypeIcon ( messageType );
    }

    public static ImageIcon getTypeIcon ( int messageType )
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
}
