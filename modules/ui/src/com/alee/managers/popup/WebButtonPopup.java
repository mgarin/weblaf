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

import com.alee.api.annotations.NotNull;
import com.alee.extended.layout.TableLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.PanelPainter;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.panel.WebPanelUI;
import com.alee.managers.style.Bounds;
import com.alee.managers.style.ShapeMethodsImpl;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ShapeUtils;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.swing.AncestorAdapter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Mikle Garin
 */
@Deprecated
public class WebButtonPopup extends WebInnerPopup
{
    private static final ImmutableList<String> BUTTON_PROPERTIES = new ImmutableList<String> (
            AbstractButton.ICON_CHANGED_PROPERTY,
            AbstractButton.TEXT_CHANGED_PROPERTY,
            AbstractButton.HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY,
            AbstractButton.VERTICAL_ALIGNMENT_CHANGED_PROPERTY,
            AbstractButton.HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY,
            AbstractButton.VERTICAL_TEXT_POSITION_CHANGED_PROPERTY,
            WebLookAndFeel.ICON_TEXT_GAP_PROPERTY,
            WebLookAndFeel.BORDER_PROPERTY
    );

    private PopupWay popupWay;

    private final WebButton button;
    private final WebButton copiedButton;
    private final WebPanel container;

    public WebButtonPopup ( final WebButton button, final PopupWay popupWay )
    {
        super ();

        this.popupWay = popupWay;
        this.button = button;

        // Initial popup settings
        setCloseOnFocusLoss ( true );
        setFocusCycleRoot ( false );
        // todo setPainter ( new ButtonPopupPainter () );

        // Button copy for popup
        copiedButton = copy ( button );
        copiedButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                hidePopup ( true );
            }
        } );

        // Button listeners
        button.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Displaying popup when button is pressed
                showPopup ();
            }
        } );
        button.addPropertyChangeListener ( new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
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
            @Override
            public void hierarchyChanged ( final HierarchyEvent e )
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
            @Override
            public void ancestorRemoved ( final AncestorEvent event )
            {
                // Hiding popup properly when button is removed from visible container somehow
                hidePopup ( false );
            }

            @Override
            public void ancestorMoved ( final AncestorEvent event )
            {
                // Placing popup properly when button has moved
                updateBounds ();
            }
        } );
        button.addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentHidden ( final ComponentEvent e )
            {
                // Hiding popup properly when button is hidden
                hidePopup ( false );
            }

            @Override
            public void componentResized ( final ComponentEvent e )
            {
                updateBounds ();
            }

            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                updateBounds ();
            }
        } );

        // Popup content panel
        container = new WebPanel ();
        container.setOpaque ( false );
        container.setFocusCycleRoot ( true );

        // Initial components composition
        updateContent ();

        // Bounds update listeners
        addAncestorListener ( new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                updateBounds ();
            }

            @Override
            public void ancestorMoved ( final AncestorEvent event )
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

        // todo FIX
        //        final int margin = button.getShadeWidth () + 1;
        //        if ( isDown () )
        //        {
        //            container.setMargin ( new Insets ( 0, margin, margin, margin ) );
        //        }
        //        else if ( isUp () )
        //        {
        //            container.setMargin ( new Insets ( margin, margin, 0, margin ) );
        //        }
        //        else if ( isLeft () )
        //        {
        //            container.setMargin ( new Insets ( margin, margin, margin, 0 ) );
        //        }
        //        else if ( isRight () )
        //        {
        //            container.setMargin ( new Insets ( margin, 0, margin, margin ) );
        //        }

        revalidate ();
    }

    @Override
    public void focusChanged ( final boolean focused )
    {
        super.focusChanged ( focused );
        WebButtonPopup.this.repaint ();
    }

    public void setContent ( final Component component )
    {
        container.removeAll ();
        container.add ( component );
        container.revalidate ();
    }

    public PopupWay getPopupWay ()
    {
        return popupWay;
    }

    public void setPopupWay ( final PopupWay popupWay )
    {
        this.popupWay = popupWay;
        updateContent ();
        updateBounds ();
    }

    private WebButton copy ( final WebButton button )
    {
        final WebButton copy = new WebButton ()
        {
            @NotNull
            @Override
            public Dimension getPreferredSize ()
            {
                return button.getSize ();
            }
        };

        copySettings ( button, copy );

        copy.setFocusable ( true );
        // todo FIX
        //        copy.setUndecorated ( true );
        copy.setCursor ( button.getCursor () );

        return copy;
    }

    private void copySettings ( final WebButton button, final WebButton copy )
    {
        copy.setFont ( button.getFont () );
        copy.setIcon ( button.getIcon () );
        copy.setText ( button.getText () );
        copy.setIconTextGap ( button.getIconTextGap () );
        copy.setHorizontalAlignment ( button.getHorizontalAlignment () );
        copy.setVerticalAlignment ( button.getVerticalAlignment () );
        copy.setHorizontalTextPosition ( button.getHorizontalTextPosition () );
        copy.setVerticalTextPosition ( button.getVerticalTextPosition () );
        copy.setBorder ( button.getBorder () );
    }

    @Override
    public void updateBounds ()
    {
        if ( isShowing () && button.isShowing () )
        {
            final Point rl = CoreSwingUtils.getRelativeLocation ( button, getParent () );
            final Dimension ps = getPreferredSize ();

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

    @Override
    public boolean contains ( final int x, final int y )
    {
        return getPopupShape ( this ).contains ( x, y );
    }

    public Shape getPopupShape ( final WebButtonPopup c )
    {
        // todo FIX
        final int shadeWidth = 2;//button.getShadeWidth ();
        final int round = 3;//button.getRound ();

        final int bh = button.getHeight () - 1;
        final int bw = button.getWidth () - 1;
        final int cw = c.getWidth () - 1;
        final int ch = c.getHeight () - 1;

        Shape shape = null;

        // Simplified shape
        if ( isUpDown () && cw == bw || isLeftRight () && ch == bh )
        {
            shape = ShapeUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ) );
        }
        // Bottom popup
        else if ( popupWay.equals ( PopupWay.downLeft ) )
        {
            shape = ShapeUtils.createRoundedShape ( round, p ( cw - bw + shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ), p ( shadeWidth, bh ),
                    p ( cw - bw + shadeWidth, bh ) );
        }
        else if ( popupWay.equals ( PopupWay.downCenter ) )
        {
            final int shear = bw % 2;
            shape = ShapeUtils.createRoundedShape ( round, p ( cw / 2 - bw / 2 - shear + shadeWidth, shadeWidth ),
                    p ( cw / 2 + bw / 2 - shadeWidth, shadeWidth ), p ( cw / 2 + bw / 2 - shadeWidth, bh ), p ( cw - shadeWidth, bh ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ), p ( shadeWidth, bh ),
                    p ( cw / 2 - bw / 2 - shear + shadeWidth, bh ) );
        }
        else if ( popupWay.equals ( PopupWay.downRight ) )
        {
            shape = ShapeUtils
                    .createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( bw - shadeWidth, shadeWidth ), p ( bw - shadeWidth, bh ),
                            p ( cw - shadeWidth, bh ), p ( cw - shadeWidth, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ) );
        }
        // Top popup
        else if ( popupWay.equals ( PopupWay.upLeft ) )
        {
            shape = ShapeUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( cw - bw + shadeWidth, ch - shadeWidth ),
                    p ( cw - bw + shadeWidth, ch - bh ), p ( shadeWidth, ch - bh ) );
        }
        else if ( popupWay.equals ( PopupWay.upCenter ) )
        {
            final int shear = bw % 2;
            shape = ShapeUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - bh ), p ( cw / 2 + bw / 2 - shadeWidth, ch - bh ),
                    p ( cw / 2 + bw / 2 - shadeWidth, ch - shadeWidth ), p ( cw / 2 - bw / 2 - shear + shadeWidth, ch - shadeWidth ),
                    p ( cw / 2 - bw / 2 - shear + shadeWidth, ch - bh ), p ( shadeWidth, ch - bh ) );
        }
        else if ( popupWay.equals ( PopupWay.upRight ) )
        {
            shape = ShapeUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, ch - bh ), p ( bw - shadeWidth, ch - bh ), p ( bw - shadeWidth, ch - shadeWidth ),
                    p ( shadeWidth, ch - shadeWidth ) );
        }
        // Left popup
        else if ( popupWay.equals ( PopupWay.leftUp ) )
        {
            shape = ShapeUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - bw, shadeWidth ),
                    p ( cw - bw, ch - bh + shadeWidth ), p ( cw - shadeWidth, ch - bh + shadeWidth ),
                    p ( cw - shadeWidth, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ) );
        }
        else if ( popupWay.equals ( PopupWay.leftCenter ) )
        {
            final int shear = bh % 2;
            shape = ShapeUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - bw, shadeWidth ),
                    p ( cw - bw, ch / 2 - bh / 2 + shadeWidth ), p ( cw - shadeWidth, ch / 2 - bh / 2 + shadeWidth ),
                    p ( cw - shadeWidth, ch / 2 + bh / 2 + shear - shadeWidth ), p ( cw - bw, ch / 2 + bh / 2 + shear - shadeWidth ),
                    p ( cw - bw, ch - shadeWidth ), p ( shadeWidth, ch - shadeWidth ) );
        }
        else if ( popupWay.equals ( PopupWay.leftDown ) )
        {
            shape = ShapeUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
                    p ( cw - shadeWidth, bh - shadeWidth ), p ( cw - bw, bh - shadeWidth ), p ( cw - bw, ch - shadeWidth ),
                    p ( shadeWidth, ch - shadeWidth ) );
        }
        // Right popup
        else if ( popupWay.equals ( PopupWay.rightUp ) )
        {
            shape = ShapeUtils.createRoundedShape ( round, p ( shadeWidth, ch - bh + shadeWidth ), p ( bw, ch - bh + shadeWidth ),
                    p ( bw, shadeWidth ), p ( cw - shadeWidth, shadeWidth ), p ( cw - shadeWidth, ch - shadeWidth ),
                    p ( shadeWidth, ch - shadeWidth ) );
        }
        else if ( popupWay.equals ( PopupWay.rightCenter ) )
        {
            final int shear = bh % 2;
            shape = ShapeUtils
                    .createRoundedShape ( round, p ( shadeWidth, ch / 2 - bh / 2 + shadeWidth ), p ( bw, ch / 2 - bh / 2 + shadeWidth ),
                            p ( bw, shadeWidth ), p ( cw - shadeWidth, shadeWidth ), p ( cw - shadeWidth, ch - shadeWidth ),
                            p ( bw, ch - shadeWidth ), p ( bw, ch / 2 + bh / 2 + shear - shadeWidth ),
                            p ( shadeWidth, ch / 2 + bh / 2 + shear - shadeWidth ) );
        }
        else if ( popupWay.equals ( PopupWay.rightDown ) )
        {
            shape = ShapeUtils.createRoundedShape ( round, p ( shadeWidth, shadeWidth ), p ( cw - shadeWidth, shadeWidth ),
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

    private Point p ( final int x, final int y )
    {
        return new Point ( x, y );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return getPopupShape ( this );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return ShapeMethodsImpl.isShapeDetectionEnabled ( this );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        ShapeMethodsImpl.setShapeDetectionEnabled ( this, enabled );
    }

    public void showPopup ()
    {
        PopupManager.showPopup ( button, this, false );

        // todo Some problems with focus transfer
        //        if ( SwingUtils.hasFocusableComponent ( container ) )
        //        {
        //            container.transferFocus ();
        //        }
    }

    @Override
    public void hidePopup ()
    {
        hidePopup ( false );
    }

    public void hidePopup ( final boolean requestFocus )
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

    /**
     * Custom button popup painter.
     */
    protected class ButtonPopupPainter<D extends IDecoration<WebButtonPopup, D>>
            extends PanelPainter<WebButtonPopup, WebPanelUI<WebButtonPopup>, D>
    {
        @Override
        public void paint ( @NotNull final Graphics2D g2d, @NotNull final WebButtonPopup c, @NotNull final WebPanelUI ui, @NotNull final Bounds bounds )
        {
            // todo FIX
            //            LafUtils.drawCustomWebBorder ( g2d, c, getPopupShape ( c ),
            //                    button.isFocusable () && button.isDrawFocus () && focused ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor,
            //                    button.getShadeWidth (), true, isWebColoredBackground () );
        }
    }
}