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

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.scroll.WebScrollBar;
import com.alee.laf.scroll.WebScrollPaneBar;
import com.alee.laf.scroll.WebScrollPaneUI;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.managers.style.Skin;
import com.alee.managers.style.Skinnable;
import com.alee.managers.style.StyleListener;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.swing.SizeMethods;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;
import java.util.Map;

/**
 * Special scrollpane exclusively for {@link com.alee.extended.syntax.WebSyntaxArea} component.
 *
 * @author Mikle Garin
 */

public class WebSyntaxScrollPane extends RTextScrollPane
        implements Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, SizeMethods<WebSyntaxScrollPane>,
        LanguageContainerMethods
{
    /**
     * Constructs new empty syntax scrollpane.
     */
    public WebSyntaxScrollPane ()
    {
        super ();
        initialize ( StyleId.syntaxareaScroll );
    }

    /**
     * Constructs new syntax scrollpane with the specified syntax area inside.
     *
     * @param syntaxArea {@link com.alee.extended.syntax.WebSyntaxArea}
     */
    public WebSyntaxScrollPane ( final WebSyntaxArea syntaxArea )
    {
        super ( syntaxArea );
        initialize ( StyleId.syntaxareaScroll );
    }

    /**
     * Constructs new syntax scrollpane with the specified syntax area inside.
     *
     * @param syntaxArea  {@link com.alee.extended.syntax.WebSyntaxArea}
     * @param lineNumbers whether or not should display line numbers
     */
    public WebSyntaxScrollPane ( final WebSyntaxArea syntaxArea, final boolean lineNumbers )
    {
        super ( syntaxArea, lineNumbers );
        initialize ( StyleId.syntaxareaScroll );
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
        super ( syntaxArea, lineNumbers, lineNumberColor );
        initialize ( StyleId.syntaxareaScroll );
    }

    /**
     * Constructs new empty syntax scrollpane.
     *
     * @param id style ID
     */
    public WebSyntaxScrollPane ( final StyleId id )
    {
        super ();
        initialize ( id );
    }

    /**
     * Constructs new syntax scrollpane with the specified syntax area inside.
     *
     * @param id         style ID
     * @param syntaxArea {@link com.alee.extended.syntax.WebSyntaxArea}
     */
    public WebSyntaxScrollPane ( final StyleId id, final WebSyntaxArea syntaxArea )
    {
        super ( syntaxArea );
        initialize ( id );
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
        super ( syntaxArea, lineNumbers );
        initialize ( id );
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
        initialize ( id );
    }

    /**
     * Initializes default scrollpane styling.
     *
     * @param id style ID
     */
    protected void initialize ( final StyleId id )
    {
        setStyleId ( id );
        setGutterStyleId ( StyleId.syntaxareaScrollGutter.at ( this ) );
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
        return getWebUI ().getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return getWebUI ().setStyleId ( id );
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
    public Skin restoreSkin ()
    {
        return StyleManager.restoreSkin ( this );
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
    public Map<String, Painter> getCustomPainters ()
    {
        return StyleManager.getCustomPainters ( this );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter getCustomPainter ( final String id )
    {
        return StyleManager.getCustomPainter ( this, id );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public Painter setCustomPainter ( final String id, final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, id, painter );
    }

    @Override
    public boolean restoreDefaultPainters ()
    {
        return StyleManager.restoreDefaultPainters ( this );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets new margin.
     *
     * @param margin new margin
     */
    public void setMargin ( final int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }

    /**
     * Sets new margin.
     *
     * @param top    new top margin
     * @param left   new left margin
     * @param bottom new bottom margin
     * @param right  new right margin
     */
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    @Override
    public Insets getPadding ()
    {
        return getWebUI ().getPadding ();
    }

    /**
     * Sets new padding.
     *
     * @param padding new padding
     */
    public void setPadding ( final int padding )
    {
        setPadding ( padding, padding, padding, padding );
    }

    /**
     * Sets new padding.
     *
     * @param top    new top padding
     * @param left   new left padding
     * @param bottom new bottom padding
     * @param right  new right padding
     */
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        setPadding ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        getWebUI ().setPadding ( padding );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebScrollPaneUI getWebUI ()
    {
        return ( WebScrollPaneUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebScrollPaneUI ) )
        {
            try
            {
                setUI ( ( WebScrollPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.scrollPaneUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebScrollPaneUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    @Override
    public WebSyntaxScrollPane setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    @Override
    public WebSyntaxScrollPane setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    @Override
    public WebSyntaxScrollPane setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    @Override
    public WebSyntaxScrollPane setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    @Override
    public WebSyntaxScrollPane setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    @Override
    public WebSyntaxScrollPane setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebSyntaxScrollPane setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }

    @Override
    public void setLanguageContainerKey ( final String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    @Override
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    @Override
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }
}