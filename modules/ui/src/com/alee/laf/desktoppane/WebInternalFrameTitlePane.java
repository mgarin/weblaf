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

import com.alee.laf.WebFonts;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.managers.style.StyleId;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;

/**
 * @author Mikle Garin
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
        setBorder ( BorderFactory.createEmptyBorder ( 2, 2, 2, 2 ) );

        if ( !isFrameTitle () )
        {
            setBackground ( new Color ( 90, 90, 90, 220 ) );
        }
    }

    /**
     * Returns whether or not this title is used for frame.
     * todo This probably should be eliminated completely
     *
     * @return true if this title is used for frame, false otherwise
     */
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
            LafUtils.drawWebStyle ( ( Graphics2D ) g, WebInternalFrameTitlePane.this, new Color ( 210, 210, 210 ), 2, 4, true, false );
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
        final Icon titleIcon = new Icon ()
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
        };

        final WebLabel titleLabel = new WebLabel ( StyleId.internalframeTitleLabel.at ( frame ), titleIcon, WebLabel.LEFT )
        {
            @Override
            public String getText ()
            {
                return frame.getTitle ();
            }
        };
        titleLabel.setForeground ( Color.WHITE );
        titleLabel.setFont ( WebFonts.getSystemTitleFont () );
        titleLabel.setMargin ( isFrameTitle () ? 3 : 1, 3, 0, 3 );
        add ( titleLabel, BorderLayout.CENTER );

        final int buttons = ( frame.isIconifiable () ? 1 : 0 ) + ( frame.isMaximizable () ? 1 : 0 ) + ( frame.isClosable () ? 1 : 0 );
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
        add ( buttonsPanel, BorderLayout.EAST );
    }

    @Override
    protected void createButtons ()
    {
        iconButton = new WebButton ( StyleId.internalframeMinimizeButton.at ( frame ) );
        iconButton.setEnabled ( frame.isIconifiable () );
        iconButton.addActionListener ( iconifyAction );

        maxButton = new WebButton ( StyleId.internalframeMaximizeButton.at ( frame ) );
        maxButton.setEnabled ( frame.isMaximizable () );
        maxButton.addActionListener ( maximizeAction );

        closeButton = new WebButton ( StyleId.internalframeCloseButton.at ( frame ) );
        closeButton.setEnabled ( frame.isClosable () );
        closeButton.addActionListener ( closeAction );

        setButtonIcons ();
    }

    @Override
    protected void setButtonIcons ()
    {
        iconButton.setIcon ( frame.isIcon () ? restoreIcon : iconifyIcon );
        maxButton.setIcon ( frame.isIcon () ? maximizeIcon : frame.isMaximum () ? restoreIcon : maximizeIcon );
        closeButton.setIcon ( closeIcon );
    }
}