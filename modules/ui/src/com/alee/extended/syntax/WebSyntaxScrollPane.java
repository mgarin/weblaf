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

package com.alee.extended.syntax;

import com.alee.laf.scroll.WebScrollBar;
import com.alee.laf.scroll.WebScrollPaneBar;
import com.alee.laf.scroll.WebScrollPaneUI;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.swing.extensions.SizeMethods;
import com.alee.utils.swing.extensions.SizeMethodsImpl;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;

/**
 * {@link RTextScrollPane} extension class.
 * It contains various useful methods to simplify core component usage.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see RTextScrollPane
 * @see WebScrollPaneUI
 * @see com.alee.laf.scroll.ScrollPanePainter
 */

public class WebSyntaxScrollPane extends RTextScrollPane
        implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods, SizeMethods<WebSyntaxScrollPane>
{
    /**
     * Constructs new empty syntax scrollpane.
     */
    public WebSyntaxScrollPane ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new syntax scrollpane with the specified syntax area inside.
     *
     * @param syntaxArea {@link com.alee.extended.syntax.WebSyntaxArea}
     */
    public WebSyntaxScrollPane ( final WebSyntaxArea syntaxArea )
    {
        this ( StyleId.auto, syntaxArea );
    }

    /**
     * Constructs new syntax scrollpane with the specified syntax area inside.
     *
     * @param syntaxArea  {@link com.alee.extended.syntax.WebSyntaxArea}
     * @param lineNumbers whether or not should display line numbers
     */
    public WebSyntaxScrollPane ( final WebSyntaxArea syntaxArea, final boolean lineNumbers )
    {
        this ( StyleId.auto, syntaxArea, lineNumbers );
    }

    /**
     * Constructs new syntax scrollpane with the specified syntax area inside.
     *
     * @param syntaxArea      {@link com.alee.extended.syntax.WebSyntaxArea}
     * @param lineNumbers     whether or not should display line numbers
     * @param lineNumberColor line numbers foreground
     */
    public WebSyntaxScrollPane ( final WebSyntaxArea syntaxArea, final boolean lineNumbers, final Color lineNumberColor )
    {
        this ( StyleId.auto, syntaxArea, lineNumbers, lineNumberColor );
    }

    /**
     * Constructs new empty syntax scrollpane.
     *
     * @param id style ID
     */
    public WebSyntaxScrollPane ( final StyleId id )
    {
        this ( id, null );
    }

    /**
     * Constructs new syntax scrollpane with the specified syntax area inside.
     *
     * @param id         style ID
     * @param syntaxArea {@link com.alee.extended.syntax.WebSyntaxArea}
     */
    public WebSyntaxScrollPane ( final StyleId id, final WebSyntaxArea syntaxArea )
    {
        this ( id, syntaxArea, true );
    }

    /**
     * Constructs new syntax scrollpane with the specified syntax area inside.
     *
     * @param id          style ID
     * @param syntaxArea  {@link com.alee.extended.syntax.WebSyntaxArea}
     * @param lineNumbers whether or not should display line numbers
     */
    public WebSyntaxScrollPane ( final StyleId id, final WebSyntaxArea syntaxArea, final boolean lineNumbers )
    {
        this ( id, syntaxArea, lineNumbers, Color.GRAY );
    }

    /**
     * Constructs new syntax scrollpane with the specified syntax area inside.
     *
     * @param id              style ID
     * @param syntaxArea      {@link com.alee.extended.syntax.WebSyntaxArea}
     * @param lineNumbers     whether or not should display line numbers
     * @param lineNumberColor line numbers foreground
     */
    public WebSyntaxScrollPane ( final StyleId id, final WebSyntaxArea syntaxArea, final boolean lineNumbers, final Color lineNumberColor )
    {
        super ( syntaxArea, lineNumbers, lineNumberColor );
        setStyleId ( id );
        setGutterStyleId ( StyleId.syntaxareaScrollGutter.at ( this ) );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.syntaxareaScroll;
    }

    @Override
    public WebScrollPaneBar createHorizontalScrollBar ()
    {
        return new WebScrollPaneBar ( this, WebScrollBar.HORIZONTAL );
    }

    @Override
    public WebScrollPaneBar getHorizontalScrollBar ()
    {
        return ( WebScrollPaneBar ) super.getHorizontalScrollBar ();
    }

    @Override
    public WebScrollPaneBar createVerticalScrollBar ()
    {
        return new WebScrollPaneBar ( this, WebScrollBar.VERTICAL );
    }

    @Override
    public WebScrollPaneBar getVerticalScrollBar ()
    {
        return ( WebScrollPaneBar ) super.getVerticalScrollBar ();
    }

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

    /**
     * Sets gutter panel style ID.
     *
     * @param id gutter panel style ID
     */
    public void setGutterStyleId ( final StyleId id )
    {
        id.set ( getGutter () );
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

    @Override
    public Shape getShape ()
    {
        return ShapeMethodsImpl.getShape ( this );
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

    @Override
    public Insets getMargin ()
    {
        return MarginMethodsImpl.getMargin ( this );
    }

    @Override
    public void setMargin ( final int margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        MarginMethodsImpl.setMargin ( this, top, left, bottom, right );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PaddingMethodsImpl.getPadding ( this );
    }

    @Override
    public void setPadding ( final int padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    @Override
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        PaddingMethodsImpl.setPadding ( this, top, left, bottom, right );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WebScrollPaneUI} object that renders this component
     */
    @Override
    public WebScrollPaneUI getUI ()
    {
        return ( WebScrollPaneUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WebScrollPaneUI}
     */
    public void setUI ( final WebScrollPaneUI ui )
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
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @Override
    public WebSyntaxScrollPane setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public WebSyntaxScrollPane setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public WebSyntaxScrollPane setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public WebSyntaxScrollPane setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public WebSyntaxScrollPane setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public WebSyntaxScrollPane setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebSyntaxScrollPane setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }
}