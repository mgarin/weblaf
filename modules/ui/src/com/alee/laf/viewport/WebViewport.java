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

package com.alee.laf.viewport;

import com.alee.api.annotations.NotNull;
import com.alee.laf.scroll.layout.ScrollBarSettings;
import com.alee.laf.scroll.layout.ScrollPaneLayout;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * {@link JViewport} extension class.
 * It contains various useful methods to simplify core component usage.
 * <p>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see JViewport
 * @see WebViewportUI
 * @see ViewportPainter
 */
public class WebViewport extends JViewport implements Styleable, Paintable
{
    /**
     * Constructs new viewport component.
     */
    public WebViewport ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new viewport component.
     *
     * @param id style ID
     */
    public WebViewport ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.viewport;
    }

    @NotNull
    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( this );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( this, id );
    }

    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( this );
    }

    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Override
    public Skin setSkin ( final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Override
    public Skin setSkin ( final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Override
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( this );
    }

    @Override
    public void addStyleListener ( final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public boolean resetCustomPainter ()
    {
        return StyleManager.resetCustomPainter ( this );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WViewportUI} object that renders this component
     */
    @Override
    public WViewportUI getUI ()
    {
        return ( WViewportUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WViewportUI}
     */
    public void setUI ( final WViewportUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }

    @Override
    public void scrollRectToVisible ( final Rectangle newRect )
    {
        final Container scroll = getParent ();
        if ( scroll instanceof JScrollPane )
        {
            final JScrollPane scrollPane = ( JScrollPane ) scroll;
            if ( this == scrollPane.getViewport () )
            {
                final LayoutManager layout = scrollPane.getLayout ();
                if ( layout instanceof ScrollPaneLayout )
                {
                    final Rectangle curRect = getVisibleRect ();

                    final ScrollBarSettings vpos = ( ( ScrollPaneLayout ) layout ).getVerticalScrollBarPosition ();
                    if ( vpos.isHovering () && vpos.isExtending () )
                    {
                        final JScrollBar vsb = scrollPane.getVerticalScrollBar ();
                        if ( vsb != null && vsb.isShowing () )
                        {
                            newRect.x += calculateAdjustment ( curRect.x, curRect.width, newRect.x, newRect.width, vsb.getWidth () );
                        }
                    }

                    final ScrollBarSettings hpos = ( ( ScrollPaneLayout ) layout ).getHorizontalScrollBarPosition ();
                    if ( hpos.isHovering () && hpos.isExtending () )
                    {
                        final JScrollBar hsb = scrollPane.getHorizontalScrollBar ();
                        if ( hsb != null && hsb.isShowing () )
                        {
                            newRect.y += calculateAdjustment ( curRect.y, curRect.height, newRect.y, newRect.height, hsb.getHeight () );
                        }
                    }
                }
            }
        }

        super.scrollRectToVisible ( newRect );
    }

    /**
     * Calculates new position adjustment.
     *
     * @param curPos currents position
     * @param curLen current length
     * @param newPos new position
     * @param newLen new length
     * @param barLen scrollbar length
     * @return new position adjustment
     */
    private int calculateAdjustment ( final int curPos, final int curLen, final int newPos, final int newLen, final int barLen )
    {
        final int len = Math.min ( curLen, newLen );
        if ( curPos + curLen < newPos + len + barLen )
        {
            return barLen;
        }

        return 0;
    }
}