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

package com.alee.examples.content;

import com.alee.extended.layout.SlidingLayout;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.painter.NinePatchIconPainter;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltipStyle;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * User: mgarin Date: 14.02.12 Time: 16:01
 */

public class SlidingSearch
{
    public static final ImageIcon searchIcon = new ImageIcon ( SlidingSearch.class.getResource ( "icons/search.png" ) );

    private boolean searchShown = false;

    private final int contentSpacing = 4;
    private final int shadeWidth = 5;
    private final int topRound = 16;
    private final int bottomRound = 6;
    private final boolean connected = true;

    private final WebPanel searchPanel;
    private final SlidingLayout slideLayout;

    private final WebPanel searchPlate;
    private final UpperLine leftLine;
    private final UpperLine rightLine;

    private final WebTextField searchField;

    public SlidingSearch ( final JLayeredPane layeredPane )
    {
        super ();


        final WebPanel topPanel = new WebPanel ();
        topPanel.setLayout ( new TableLayout (
                new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL }, { TableLayout.PREFERRED } } ) );
        topPanel.setOpaque ( false );

        searchPanel = new WebPanel ()
        {
            @Override
            public boolean contains ( final int x, final int y )
            {
                return topPanel.getBounds ().contains ( x, y ) && super.contains ( x, y );
            }
        };
        searchPanel.setOpaque ( false );
        slideLayout = new SlidingLayout ( searchPanel );
        searchPanel.setLayout ( slideLayout );
        searchPanel.add ( topPanel );


        searchPlate = new WebPanel ()
        {
            private final BufferedImage bi = ImageUtils.getBufferedImage ( SlidingSearch.class.getResource ( "icons/searchBg.png" ) );
            private final Paint bg = new TexturePaint ( bi, new Rectangle ( 0, 0, bi.getWidth (), bi.getHeight () ) );

            @Override
            protected void paintComponent ( final Graphics g )
            {
                super.paintComponent ( g );

                final Graphics2D g2d = ( Graphics2D ) g;
                final Object aa = GraphicsUtils.setupAntialias ( g2d );

                final Shape border = createBorderShape ();

                GraphicsUtils.drawShade ( g2d, border, WebCustomTooltipStyle.shadeColor, shadeWidth );

                g2d.setPaint ( bg );
                g2d.fill ( border );
                g2d.setPaint ( Color.WHITE );
                g2d.draw ( border );

                GraphicsUtils.restoreAntialias ( g2d, aa );
            }

            private Shape createBorderShape ()
            {
                final int leftSpace = connected ? 0 : shadeWidth;
                final int rightSpace = connected ? 0 : shadeWidth;
                final int shear = connected ? 0 : -1;
                final GeneralPath gp = new GeneralPath ();
                gp.moveTo ( leftSpace, shear );
                gp.quadTo ( leftSpace + topRound, shear, leftSpace + topRound, topRound );
                gp.lineTo ( leftSpace + topRound, getHeight () - shadeWidth - bottomRound );
                gp.quadTo ( leftSpace + topRound, getHeight () - shadeWidth, leftSpace + topRound + bottomRound,
                        getHeight () - shadeWidth );
                gp.lineTo ( getWidth () - rightSpace - topRound - bottomRound, getHeight () - shadeWidth );
                gp.quadTo ( getWidth () - rightSpace - topRound, getHeight () - shadeWidth, getWidth () - rightSpace - topRound,
                        getHeight () - shadeWidth - bottomRound );
                gp.lineTo ( getWidth () - rightSpace - topRound, topRound );
                gp.quadTo ( getWidth () - rightSpace - topRound, shear, getWidth () - rightSpace, shear );
                return gp;
            }
        };
        searchPlate.setMargin ( contentSpacing - 1, ( connected ? 0 : shadeWidth ) + topRound + contentSpacing + 1,
                shadeWidth + contentSpacing, ( connected ? 0 : shadeWidth ) + topRound + contentSpacing );
        searchPlate.setOpaque ( false );
        searchPlate.setLayout ( new BorderLayout () );
        searchPlate.setVisible ( false );

        leftLine = new UpperLine ();
        leftLine.setVisible ( false );

        rightLine = new UpperLine ();
        rightLine.setVisible ( false );

        topPanel.add ( leftLine, "0,0" );
        topPanel.add ( searchPlate, "1,0" );
        topPanel.add ( rightLine, "2,0" );


        searchField = new WebTextField ( 15 );
        TooltipManager.setTooltip ( searchField, searchIcon, "Quick components search", TooltipWay.down );
        searchField.setPainter ( new NinePatchIconPainter ( SlidingSearch.class.getResource ( "icons/searchField.png" ) ) );
        searchField.setForeground ( Color.WHITE );
        searchField.setCaretColor ( Color.LIGHT_GRAY );
        searchField.setHorizontalAlignment ( WebTextField.CENTER );
        searchField.addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyPressed ( final KeyEvent e )
            {
                if ( Hotkey.ENTER.isTriggered ( e ) )
                {
                    toggleSearch ();
                }
            }
        } );
        searchPlate.add ( searchField );


        HotkeyManager.registerHotkey ( layeredPane, Hotkey.CTRL_F, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                toggleSearch ();
            }
        } );
        HotkeyManager.registerHotkey ( layeredPane, Hotkey.ESCAPE, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                if ( searchField.getText ().length () > 0 )
                {
                    searchField.setText ( "" );
                }
                else if ( searchField.isFocusOwner () )
                {
                    searchField.transferFocus ();
                }
            }
        } );
        searchField.addFocusListener ( new FocusAdapter ()
        {
            @Override
            public void focusLost ( final FocusEvent e )
            {
                hideSearch ();
            }
        } );

        layeredPane.add ( searchPanel, JLayeredPane.DRAG_LAYER );
        layeredPane.addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                searchPanel.setBounds ( 0, 0, layeredPane.getWidth (), layeredPane.getHeight () );
            }
        } );
    }

    public WebTextField getSearchField ()
    {
        return searchField;
    }

    public void toggleSearch ()
    {
        if ( searchShown )
        {
            hideSearch ();
        }
        else
        {
            showSearch ();
        }
    }

    public void showSearch ()
    {
        if ( searchShown || !isSearchEnabled () )
        {
            return;
        }

        searchShown = true;

        leftLine.setVisible ( true );
        searchPlate.setVisible ( true );
        rightLine.setVisible ( true );

        slideLayout.slideIn ();

        searchField.requestFocusInWindow ();
    }

    protected boolean isSearchEnabled ()
    {
        return true;
    }

    public void hideSearch ()
    {
        if ( searchShown )
        {
            searchShown = false;
            searchField.setText ( "" );
            searchField.transferFocus ();
            slideLayout.slideOut ();
        }
    }

    private class UpperLine extends JComponent
    {
        public UpperLine ()
        {
            super ();
            SwingUtils.setOrientation ( this );
        }

        @Override
        protected void paintComponent ( final Graphics g )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final Object aa = GraphicsUtils.setupAntialias ( g2d );

            final Shape border = new Line2D.Double ( 0, 0, getWidth (), 0 );

            GraphicsUtils.drawShade ( g2d, border, WebCustomTooltipStyle.shadeColor, shadeWidth );

            g2d.setPaint ( Color.WHITE );
            g2d.draw ( border );

            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }
}