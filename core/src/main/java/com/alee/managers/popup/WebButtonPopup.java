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

package com.alee.managers.popup;

import com.alee.extended.painter.DefaultPainter;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

/**
 * User: mgarin Date: 26.03.12 Time: 17:32
 */

public class WebButtonPopup extends WebPopup
{
    private static final List<String> BUTTON_PROPERTIES =
            Arrays.asList ( AbstractButton.ICON_CHANGED_PROPERTY, AbstractButton.TEXT_CHANGED_PROPERTY,
                    AbstractButton.HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY, AbstractButton.VERTICAL_ALIGNMENT_CHANGED_PROPERTY,
                    AbstractButton.HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY, AbstractButton.VERTICAL_TEXT_POSITION_CHANGED_PROPERTY,
                    "iconTextGap", "border" );

    private PopupWay popupWay;

    private WebButton button;
    private WebButton copiedButton;
    private WebPanel container;

    public WebButtonPopup ( final WebButton button, PopupWay popupWay )
    {
        super ();

        this.popupWay = popupWay;
        this.button = button;

        // Initial popup settings
        setCloseOnFocusLoss ( true );
        setPainter ( new WebButtonPopupPainter () );

        // Button copy for popup
        copiedButton = copy ( button );
        copiedButton.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                hidePopup ( true );
            }
        } );

        // Button listeners
        button.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                // Displaying popup when button is pressed
                PopupManager.showPopup ( button, WebButtonPopup.this, false );
            }
        } );
        button.addPropertyChangeListener ( new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent evt )
            {
                // Updating button copy on property changes
                if ( BUTTON_PROPERTIES.contains ( evt.getPropertyName () ) )
                {
                    copySettings ( button, copiedButton );
                }
            }
        } );
        button.addHierarchyListener ( new HierarchyListener ()
        {
            public void hierarchyChanged ( HierarchyEvent e )
            {
                // Hiding popup properly when popup button parent has changed
                if ( e.getID () == HierarchyEvent.PARENT_CHANGED )
                {
                    hidePopup ( false );
                }
            }
        } );
        button.addAncestorListener ( new AncestorAdapter ()
        {
            public void ancestorRemoved ( AncestorEvent event )
            {
                // Hiding popup properly when button is removed from visible container somehow
                hidePopup ( false );
            }

            public void ancestorMoved ( AncestorEvent event )
            {
                // Placing popup properly when button has moved
                updateBounds ();
            }
        } );
        button.addComponentListener ( new ComponentAdapter ()
        {
            public void componentHidden ( ComponentEvent e )
            {
                // Hiding popup properly when button is hidden
                hidePopup ( false );
            }

            public void componentResized ( ComponentEvent e )
            {
                updateBounds ();
            }

            public void componentMoved ( ComponentEvent e )
            {
                updateBounds ();
            }
        } );

        // Popup content panel
        container = new WebPanel ();
        container.setOpaque ( false );

        // Initial components composition
        updateContent ();

        // Bounds update listeners
        addAncestorListener ( new AncestorAdapter ()
        {
            public void ancestorAdded ( AncestorEvent event )
            {
                updateBounds ();
            }

            public void ancestorMoved ( AncestorEvent event )
            {
                updateBounds ();
            }
        } );
    }

    private void updateContent ()
    {
        removeAll ();

        // Bottom popup
        if ( popupWay.equals ( PopupWay.downLeft ) )
        {
            setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED }, { TableLayout.PREFERRED, TableLayout.PREFERRED } } ) );
            add ( copiedButton, "1,0" );
            add ( container, "0,1,1,1" );
        }
        else if ( popupWay.equals ( PopupWay.downCenter ) )
        {
            setLayout ( new TableLayout ( new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED } } ) );
            add ( copiedButton, "1,0" );
            add ( container, "0,1,2,1" );
        }
        else if ( popupWay.equals ( PopupWay.downRight ) )
        {
            setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.PREFERRED, TableLayout.FILL }, { TableLayout.PREFERRED, TableLayout.PREFERRED } } ) );
            add ( copiedButton, "0,0" );
            add ( container, "0,1,1,1" );
        }
        // Top popup   
        if ( popupWay.equals ( PopupWay.upLeft ) )
        {
            setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED }, { TableLayout.PREFERRED, TableLayout.PREFERRED } } ) );
            add ( copiedButton, "1,1" );
            add ( container, "0,0,1,0" );
        }
        else if ( popupWay.equals ( PopupWay.upCenter ) )
        {
            setLayout ( new TableLayout ( new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED } } ) );
            add ( copiedButton, "1,1" );
            add ( container, "0,0,2,0" );
        }
        else if ( popupWay.equals ( PopupWay.upRight ) )
        {
            setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.PREFERRED, TableLayout.FILL }, { TableLayout.PREFERRED, TableLayout.PREFERRED } } ) );
            add ( copiedButton, "0,1" );
            add ( container, "0,0,1,0" );
        }
        // Left popup    
        if ( popupWay.equals ( PopupWay.leftUp ) )
        {
            setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.PREFERRED, TableLayout.PREFERRED }, { TableLayout.FILL, TableLayout.PREFERRED } } ) );
            add ( copiedButton, "1,1" );
            add ( container, "0,0,0,1" );
        }
        else if ( popupWay.equals ( PopupWay.leftCenter ) )
        {
            setLayout ( new TableLayout ( new double[][]{ { TableLayout.PREFERRED, TableLayout.PREFERRED },
                    { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL } } ) );
            add ( copiedButton, "1,1" );
            add ( container, "0,0,0,2" );
        }
        else if ( popupWay.equals ( PopupWay.leftDown ) )
        {
            setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.PREFERRED, TableLayout.PREFERRED }, { TableLayout.PREFERRED, TableLayout.FILL } } ) );
            add ( copiedButton, "1,0" );
            add ( container, "0,0,0,1" );
        }
        // Right popup       
        if ( popupWay.equals ( PopupWay.rightUp ) )
        {
            setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.PREFERRED, TableLayout.PREFERRED }, { TableLayout.FILL, TableLayout.PREFERRED } } ) );
            add ( copiedButton, "0,1" );
            add ( container, "1,0,1,1" );
        }
        else if ( popupWay.equals ( PopupWay.rightCenter ) )
        {
            setLayout ( new TableLayout ( new double[][]{ { TableLayout.PREFERRED, TableLayout.PREFERRED },
                    { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL } } ) );
            add ( copiedButton, "0,1" );
            add ( container, "1,0,1,2" );
        }
        else if ( popupWay.equals ( PopupWay.rightDown ) )
        {
            setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.PREFERRED, TableLayout.PREFERRED }, { TableLayout.PREFERRED, TableLayout.FILL } } ) );
            add ( copiedButton, "0,0" );
            add ( container, "1,0,1,1" );
        }

        int margin = button.getShadeWidth () + 1;
        if ( isDown () )
        {
            container.setMargin ( new Insets ( 0, margin, margin, margin ) );
        }
        else if ( isUp () )
        {
            container.setMargin ( new Insets ( margin, margin, 0, margin ) );
        }
        else if ( isLeft () )
        {
            container.setMargin ( new Insets ( margin, margin, margin, 0 ) );
        }
        else if ( isRight () )
        {
            container.setMargin ( new Insets ( margin, 0, margin, margin ) );
        }

        revalidate ();
    }

    public void focusChanged ( boolean focused )
    {
        super.focusChanged ( focused );
        WebButtonPopup.this.repaint ();
    }

    public void setContent ( Component component )
    {
        container.removeAll ();
        container.add ( component );
        container.revalidate ();
    }

    public PopupWay getPopupWay ()
    {
        return popupWay;
    }

    public void setPopupWay ( PopupWay popupWay )
    {
        this.popupWay = popupWay;
        updateContent ();
        updateBounds ();
    }

    private WebButton copy ( final WebButton button )
    {
        WebButton copy = new WebButton ()
        {
            public Dimension getPreferredSize ()
            {
                return button.getSize ();
            }
        };

        copySettings ( button, copy );

        copy.setFocusable ( true );
        copy.setUndecorated ( true );
        copy.setCursor ( button.getCursor () );

        return copy;
    }

    private void copySettings ( WebButton button, WebButton copy )
    {
        copy.setIcon ( button.getIcon () );
        copy.setText ( button.getText () );
        copy.setIconTextGap ( button.getIconTextGap () );
        copy.setHorizontalAlignment ( button.getHorizontalAlignment () );
        copy.setVerticalAlignment ( button.getVerticalAlignment () );
        copy.setHorizontalTextPosition ( button.getHorizontalTextPosition () );
        copy.setVerticalTextPosition ( button.getVerticalTextPosition () );
        copy.setBorder ( button.getBorder () );
    }

    private void updateBounds ()
    {
        if ( isShowing () && button.isShowing () )
        {
            Point rl = SwingUtils.getRelativeLocation ( button, getParent () );
            Dimension ps = getPreferredSize ();

            // Bottom popup
            if ( popupWay.equals ( PopupWay.downLeft ) )
            {
                setBounds ( new Rectangle ( new Point ( rl.x + button.getWidth () - ps.width, rl.y ), ps ) );
            }
            else if ( popupWay.equals ( PopupWay.downCenter ) )
            {
                setBounds ( new Rectangle ( new Point ( rl.x + button.getWidth () / 2 - ps.width / 2, rl.y ), ps ) );
            }
            else if ( popupWay.equals ( PopupWay.downRight ) )
            {
                setBounds ( new Rectangle ( rl, ps ) );
            }
            // Top popup   
            else if ( popupWay.equals ( PopupWay.upLeft ) )
            {
                setBounds (
                        new Rectangle ( new Point ( rl.x + button.getWidth () - ps.width, rl.y + button.getHeight () - ps.height ), ps ) );
            }
            else if ( popupWay.equals ( PopupWay.upCenter ) )
            {
                setBounds (
                        new Rectangle ( new Point ( rl.x + button.getWidth () / 2 - ps.width / 2, rl.y + button.getHeight () - ps.height ),
                                ps ) );
            }
            else if ( popupWay.equals ( PopupWay.upRight ) )
            {
                setBounds ( new Rectangle ( new Point ( rl.x, rl.y + button.getHeight () - ps.height ), ps ) );
            }
            // Left popup   
            else if ( popupWay.equals ( PopupWay.leftUp ) )
            {
                setBounds (
                        new Rectangle ( new Point ( rl.x + button.getWidth () - ps.width, rl.y + button.getHeight () - ps.height ), ps ) );
            }
            else if ( popupWay.equals ( PopupWay.leftCenter ) )
            {
                setBounds (
                        new Rectangle ( new Point ( rl.x + button.getWidth () - ps.width, rl.y + button.getHeight () / 2 - ps.height / 2 ),
                                ps ) );
            }
            else if ( popupWay.equals ( PopupWay.leftDown ) )
            {
                setBounds ( new Rectangle ( new Point ( rl.x + button.getWidth () - ps.width, rl.y ), ps ) );
            }
            // Right popup   
            else if ( popupWay.equals ( PopupWay.rightUp ) )
            {
                setBounds ( new Rectangle ( new Point ( rl.x, rl.y + button.getHeight () - ps.height ), ps ) );
            }
            else if ( popupWay.equals ( PopupWay.rightCenter ) )
            {
                setBounds ( new Rectangle ( new Point ( rl.x, rl.y + button.getHeight () / 2 - ps.height / 2 ), ps ) );
            }
            else if ( popupWay.equals ( PopupWay.rightDown ) )
            {
                setBounds ( new Rectangle ( new Point ( rl.x, rl.y ), ps ) );
            }

            revalidate ();
        }
    }

    public boolean contains ( Point p )
    {
        return getPopupShape ( this ).contains ( p );
    }

    public Shape getPopupShape ( WebButtonPopup c )
    {
        int shadeWidth = button.getShadeWidth ();
        int round = button.getRound ();

        int bh = button.getHeight () - 1;
        int bw = button.getWidth () - 1;
        int cw = c.getWidth () - 1;
        int ch = c.getHeight () - 1;

        Shape shape = null;

        // Simplified shape
        if ( isUpDown () && cw == bw || isLeftRight () && ch == bh )
        {
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ) );
        }
        // Bottom popup
        else if ( popupWay.equals ( PopupWay.downLeft ) )
        {
            shape = LafUtils.createRoundedShape ( round, p ( cw - bw + shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ), p ( shadeWidth, bh ),
                    p ( cw - bw + shadeWidth, bh ) );
        }
        else if ( popupWay.equals ( PopupWay.downCenter ) )
        {
            int shear = bw % 2;
            shape = LafUtils.createRoundedShape ( round, p ( cw / 2 - bw / 2 - shear + shadeWidth, shadeWidth ),
                    p ( cw / 2 + bw / 2 - shadeWidth, shadeWidth ), p ( cw / 2 + bw / 2 - shadeWidth, bh ), p ( cw - shadeWidth, bh ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ), p ( shadeWidth, bh ),
                    p ( cw / 2 - bw / 2 - shear + shadeWidth, bh ) );
        }
        else if ( popupWay.equals ( PopupWay.downRight ) )
        {
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( bw - shadeWidth, shadeWidth ),
                    p ( bw - shadeWidth, bh ), p ( cw - shadeWidth, bh ), p ( cw - shadeWidth, ch - shadeWidth ),
                    p ( shadeWidth, ch - shadeWidth ) );
        }
        // Top popup
        else if ( popupWay.equals ( PopupWay.upLeft ) )
        {
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( cw - bw + shadeWidth, ch - shadeWidth ),
                    p ( cw - bw + shadeWidth, ch - bh ), p ( shadeWidth, ch - bh ) );
        }
        else if ( popupWay.equals ( PopupWay.upCenter ) )
        {
            int shear = bw % 2;
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - bh ), p ( cw / 2 + bw / 2 - shadeWidth, ch - bh ),
                    p ( cw / 2 + bw / 2 - shadeWidth, ch - shadeWidth ), p ( cw / 2 - bw / 2 - shear + shadeWidth, ch - shadeWidth ),
                    p ( cw / 2 - bw / 2 - shear + shadeWidth, ch - bh ), p ( shadeWidth, ch - bh ) );
        }
        else if ( popupWay.equals ( PopupWay.upRight ) )
        {
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - bh ), p ( bw - shadeWidth, ch - bh ), p ( bw - shadeWidth, ch - shadeWidth ),
                    p ( shadeWidth, ch - shadeWidth ) );
        }
        // Left popup
        else if ( popupWay.equals ( PopupWay.leftUp ) )
        {
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - bw, shadeWidth ),
                    p ( cw - bw, ch - bh + shadeWidth ), p ( cw - shadeWidth, ch - bh + shadeWidth ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ) );
        }
        else if ( popupWay.equals ( PopupWay.leftCenter ) )
        {
            int shear = bh % 2;
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - bw, shadeWidth ),
                    p ( cw - bw, ch / 2 - bh / 2 + shadeWidth ), p ( cw - shadeWidth, ch / 2 - bh / 2 + shadeWidth ),
                    p ( cw - shadeWidth, ch / 2 + bh / 2 + shear - shadeWidth ), p ( cw - bw, ch / 2 + bh / 2 + shear - shadeWidth ),
                    p ( cw - bw, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ) );
        }
        else if ( popupWay.equals ( PopupWay.leftDown ) )
        {
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, bh - shadeWidth ), p ( cw - bw, bh - shadeWidth ), p ( cw - bw, ch - shadeWidth ),
                    p ( shadeWidth, ch - shadeWidth ) );
        }
        // Right popup
        else if ( popupWay.equals ( PopupWay.rightUp ) )
        {
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, ch - bh + shadeWidth ), p ( bw, ch - bh + shadeWidth ),
                    p ( bw, shadeWidth ), p ( cw - shadeWidth, shadeWidth ), p ( cw - shadeWidth, ch - shadeWidth ),
                    p ( shadeWidth, ch - shadeWidth ) );
        }
        else if ( popupWay.equals ( PopupWay.rightCenter ) )
        {
            int shear = bh % 2;
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, ch / 2 - bh / 2 + shadeWidth ),
                    p ( bw, ch / 2 - bh / 2 + shadeWidth ), p ( bw, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( bw, ch - shadeWidth ), p ( bw, ch / 2 + bh / 2 + shear - shadeWidth ),
                    p ( shadeWidth, ch / 2 + bh / 2 + shear - shadeWidth ) );
        }
        else if ( popupWay.equals ( PopupWay.rightDown ) )
        {
            shape = LafUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( bw, ch - shadeWidth ), p ( bw, bh - shadeWidth ),
                    p ( shadeWidth, bh - shadeWidth ) );
        }

        return shape;
    }

    private boolean isUpDown ()
    {
        return isUp () || isDown ();
    }

    private boolean isDown ()
    {
        return popupWay.equals ( PopupWay.downLeft ) || popupWay.equals ( PopupWay.downCenter ) ||
                popupWay.equals ( PopupWay.downRight );
    }

    private boolean isUp ()
    {
        return popupWay.equals ( PopupWay.upLeft ) || popupWay.equals ( PopupWay.upCenter ) ||
                popupWay.equals ( PopupWay.upRight );
    }

    private boolean isLeftRight ()
    {
        return isLeft () || isRight ();
    }

    private boolean isRight ()
    {
        return popupWay.equals ( PopupWay.rightUp ) || popupWay.equals ( PopupWay.rightCenter ) ||
                popupWay.equals ( PopupWay.rightDown );
    }

    private boolean isLeft ()
    {
        return popupWay.equals ( PopupWay.leftUp ) || popupWay.equals ( PopupWay.leftCenter ) ||
                popupWay.equals ( PopupWay.leftDown );
    }

    private Point p ( int x, int y )
    {
        return new Point ( x, y );
    }

    public Shape provideShape ()
    {
        return getPopupShape ( this );
    }

    public void hidePopup ()
    {
        hidePopup ( false );
    }

    public void hidePopup ( boolean requestFocus )
    {
        if ( isCloseOnFocusLoss () && button.isFocusable () && requestFocus )
        {
            button.requestFocusInWindow ();
            return;
        }

        super.hidePopup ();

        // Transfering focus back to component
        if ( button.isFocusable () && requestFocus )
        {
            button.requestFocusInWindow ();
        }
    }

    private class WebButtonPopupPainter extends DefaultPainter<WebButtonPopup>
    {
        public void paint ( Graphics2D g2d, Rectangle bounds, WebButtonPopup c )
        {
            LafUtils.drawCustomWebBorder ( g2d, c, getPopupShape ( c ),
                    button.isFocusable () && button.isDrawFocus () && focused ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor,
                    button.getShadeWidth (), true, isWebColored () );
        }
    }
}