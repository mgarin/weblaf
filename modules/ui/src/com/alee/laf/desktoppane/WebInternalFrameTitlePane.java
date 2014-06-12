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

package com.alee.laf.desktoppane;

import com.alee.extended.panel.BorderPanel;
import com.alee.global.StyleConstants;
import com.alee.laf.WebFonts;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;

/**
 * User: mgarin Date: 24.08.11 Time: 20:30
 */

public class WebInternalFrameTitlePane extends BasicInternalFrameTitlePane
{
    public static final ImageIcon iconifyIcon = new ImageIcon ( WebInternalFrameTitlePane.class.getResource ( "icons/minimize.png" ) );
    public static final ImageIcon maximizeIcon = new ImageIcon ( WebInternalFrameTitlePane.class.getResource ( "icons/maximize.png" ) );
    public static final ImageIcon restoreIcon = new ImageIcon ( WebInternalFrameTitlePane.class.getResource ( "icons/restore.png" ) );
    public static final ImageIcon closeIcon = new ImageIcon ( WebInternalFrameTitlePane.class.getResource ( "icons/close.png" ) );

    public WebInternalFrameTitlePane ( final JInternalFrame f )
    {
        super ( f );

        setOpaque ( false );
        setBorder ( BorderFactory.createEmptyBorder ( StyleConstants.shadeWidth, StyleConstants.shadeWidth, StyleConstants.shadeWidth,
                StyleConstants.shadeWidth ) );

        if ( !isFrameTitle () )
        {
            setBackground ( new Color ( 90, 90, 90, 220 ) );
        }
    }

    protected boolean isFrameTitle ()
    {
        return true;
    }

    @Override
    public void paintComponent ( final Graphics g )
    {
        // super.paintComponent ( g );
        if ( !isFrameTitle () )
        {
            LafUtils.drawWebStyle ( ( Graphics2D ) g, WebInternalFrameTitlePane.this, StyleConstants.shadeColor, StyleConstants.shadeWidth,
                    StyleConstants.bigRound, true, false );
        }
    }

    @Override
    protected LayoutManager createLayout ()
    {
        return new BorderLayout ();
    }

    @Override
    protected void addSubComponents ()
    {
        add ( new BorderPanel ( new WebLabel ( frame.getTitle (), new Icon ()
        {
            @Override
            public void paintIcon ( final Component c, final Graphics g, final int x, final int y )
            {
                if ( frame.getFrameIcon () != null )
                {
                    frame.getFrameIcon ().paintIcon ( c, g, x, y );
                }
            }

            @Override
            public int getIconWidth ()
            {
                return frame.getFrameIcon () != null ? frame.getFrameIcon ().getIconWidth () : 16;
            }

            @Override
            public int getIconHeight ()
            {
                return frame.getFrameIcon () != null ? frame.getFrameIcon ().getIconHeight () : 16;
            }
        }, WebLabel.LEFT )
        {
            {
                setOpaque ( false );
                setForeground ( Color.WHITE );
                setFont ( WebFonts.getSystemTitleFont () );
            }
        }, isFrameTitle () ? 3 : 1, 3, 0, 3 ), BorderLayout.CENTER );

        final int buttons = ( frame.isIconifiable () ? 1 : 0 ) + ( frame.isMaximizable () ? 1 : 0 ) +
                ( frame.isClosable () ? 1 : 0 );
        final JPanel buttonsPanel = new JPanel ( new GridLayout ( 1, buttons ) );
        buttonsPanel.setOpaque ( false );
        if ( frame.isIconifiable () )
        {
            buttonsPanel.add ( iconButton );
        }
        if ( frame.isMaximizable () )
        {
            buttonsPanel.add ( maxButton );
        }
        if ( frame.isClosable () )
        {
            buttonsPanel.add ( closeButton );
        }
        add ( new BorderPanel ( buttonsPanel, 0, 0, 0, 0 ), BorderLayout.EAST );
    }

    @Override
    protected void createButtons ()
    {
        iconButton = new WebButton ()
        {
            {
                setEnabled ( frame.isIconifiable () );
                setRolloverDarkBorderOnly ( false );
                setShadeWidth ( 0 );
                setRound ( StyleConstants.bigRound );
                setInnerShadeWidth ( 2 );
                setFocusable ( false );
                if ( isFrameTitle () )
                {
                    setDrawRight ( false );
                    setDrawRightLine ( true );
                    setDrawTop ( false );
                    setDrawTopLine ( true );
                }
                else
                {
                    setDrawLeft ( false );
                    setDrawLeftLine ( true );
                    setDrawRight ( false );
                    setDrawRightLine ( true );
                }
                setBorder ( BorderFactory.createEmptyBorder ( 4, 7, 4, 6 ) );
            }
        };
        iconButton.addActionListener ( iconifyAction );

        maxButton = new WebButton ()
        {
            {
                setEnabled ( frame.isMaximizable () );
                setRolloverDarkBorderOnly ( false );
                setShadeWidth ( 0 );
                setRound ( StyleConstants.bigRound );
                setInnerShadeWidth ( 2 );
                setFocusable ( false );
                setDrawLeft ( false );
                setDrawLeftLine ( false );
                setDrawRight ( false );
                setDrawRightLine ( true );
                setBorder ( BorderFactory.createEmptyBorder ( 4, 6, 4, 6 ) );
            }
        };
        maxButton.addActionListener ( maximizeAction );

        closeButton = new WebButton ()
        {
            {
                setEnabled ( frame.isClosable () );
                setRolloverDarkBorderOnly ( false );
                setShadeWidth ( 0 );
                setRound ( StyleConstants.bigRound );
                setInnerShadeWidth ( 2 );
                setFocusable ( false );
                if ( isFrameTitle () )
                {
                    setDrawLeft ( false );
                    setDrawLeftLine ( false );
                    setDrawBottom ( false );
                    setDrawBottomLine ( true );
                }
                else
                {
                    setDrawLeft ( false );
                    setDrawLeftLine ( false );
                }
                setBorder ( BorderFactory.createEmptyBorder ( 4, 6, 4, 7 ) );
            }
        };
        closeButton.addActionListener ( closeAction );

        setButtonIcons ();
    }

    @Override
    protected void setButtonIcons ()
    {
        iconButton.setIcon ( frame.isIcon () ? restoreIcon : iconifyIcon );
        maxButton.setIcon ( frame.isIcon () ? maximizeIcon : ( frame.isMaximum () ? restoreIcon : maximizeIcon ) );
        closeButton.setIcon ( closeIcon );
    }
}
