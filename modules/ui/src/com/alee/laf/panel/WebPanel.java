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

package com.alee.laf.panel;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PartialDecoration;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleManager;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.EventUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.List;

/**
 * This JPanel extension class provides a direct access to WebPanelUI methods.
 * By default WebPanel uses BorderLayout instead of FlowLayout (unlike JPanel).
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 */

public class WebPanel extends JPanel
        implements Styleable, ShapeProvider, PartialDecoration, EventMethods, ToolTipMethods, SizeMethods<WebPanel>,
        LanguageContainerMethods
{
    /**
     * Constructs new panel.
     */
    public WebPanel ()
    {
        super ( new BorderLayout () );
    }

    /**
     * Constructs new panel which is either decorated or undecorated.
     *
     * @param decorated whether panel should be decorated or not
     */
    public WebPanel ( final boolean decorated )
    {
        super ( new BorderLayout () );
        setUndecorated ( !decorated );
    }

    /**
     * Constructs new panel with the specified layout which is either decorated or undecorated.
     *
     * @param decorated whether panel should be decorated or not
     * @param layout    panel layout
     */
    public WebPanel ( final boolean decorated, final LayoutManager layout )
    {
        super ( layout );
        setUndecorated ( !decorated );
    }

    /**
     * Constructs new panel which is either decorated or undecorated.
     * Also the specified component is automatically added into panel's center area.
     *
     * @param decorated whether panel should be decorated or not
     * @param component component to add into the panel
     */
    public WebPanel ( final boolean decorated, final Component component )
    {
        super ( new BorderLayout () );
        setUndecorated ( !decorated );
        add ( component, BorderLayout.CENTER );
    }

    /**
     * Constructs new panel with the specified component automatically added into panel's center area.
     *
     * @param component component to add into the panel
     */
    public WebPanel ( final Component component )
    {
        super ( new BorderLayout () );
        add ( component, BorderLayout.CENTER );
    }

    /**
     * Constructs new panel with the specified painter.
     *
     * @param painter panel painter
     */
    public WebPanel ( final Painter painter )
    {
        super ( new BorderLayout () );
        setPainter ( painter );
    }

    /**
     * Constructs new panel with the specified layout and painter.
     *
     * @param layout  panel layout
     * @param painter panel painter
     */
    public WebPanel ( final LayoutManager layout, final Painter painter )
    {
        super ( layout );
        setPainter ( painter );
    }

    /**
     * Constructs new panel with the specified painter.
     * Also the specified component is automatically added into panel's center area.
     *
     * @param painter   panel painter
     * @param component component to add into the panel
     */
    public WebPanel ( final Painter painter, final Component component )
    {
        super ( new BorderLayout () );
        setPainter ( painter );
        add ( component, BorderLayout.CENTER );
    }

    /**
     * Constructs new panel with the specified layout and painter.
     * Also the specified components are automatically added into panel's center area.
     *
     * @param layout     panel layout
     * @param painter    panel painter
     * @param components components to add into panel
     */
    public WebPanel ( final LayoutManager layout, final Painter painter, final Component... components )
    {
        super ( layout );
        setPainter ( painter );
        add ( components );
    }

    /**
     * Constructs new panel with the specified layout.
     *
     * @param layout panel layout
     */
    public WebPanel ( final LayoutManager layout )
    {
        super ( layout );
    }

    /**
     * Constructs new panel with the specified layout and double-buffered mark.
     *
     * @param layout           panel layout
     * @param isDoubleBuffered whether panel should be double-buffered or not
     */
    public WebPanel ( final LayoutManager layout, final boolean isDoubleBuffered )
    {
        super ( layout, isDoubleBuffered );
    }

    /**
     * Constructs new panel with the specified layout.
     * Also the specified components are automatically added into panel's center area.
     *
     * @param layout     panel layout
     * @param components components to add into panel
     */
    public WebPanel ( final LayoutManager layout, final Component... components )
    {
        super ( layout );
        add ( components );
    }

    /**
     * Constructs new panel with the specified style ID.
     *
     * @param styleId style ID
     */
    public WebPanel ( final String styleId )
    {
        super ( new BorderLayout () );
        setStyleId ( styleId );
    }

    /**
     * Constructs new panel with the specified style ID.
     *
     * @param styleId style ID
     * @param layout  panel layout
     */
    public WebPanel ( final String styleId, final LayoutManager layout )
    {
        super ( layout );
        setStyleId ( styleId );
    }

    /**
     * Constructs new panel with the specified style ID.
     *
     * @param styleId   style ID
     * @param component component to add into panel
     */
    public WebPanel ( final String styleId, final Component component )
    {
        super ( new BorderLayout () );
        setStyleId ( styleId );
        add ( component, BorderLayout.CENTER );
    }

    /**
     * Returns whether the specified component belongs to this container or not.
     *
     * @param component component to process
     * @return true if the specified component belongs to this container, false otherwise
     */
    public boolean contains ( final Component component )
    {
        return component != null && component.getParent () == this;
    }

    /**
     * Adds all components from the list into the panel under the specified index.
     *
     * @param components components to add into panel
     * @param index      index where components should be placed
     * @return this panel
     */
    public WebPanel add ( final List<? extends Component> components, final int index )
    {
        if ( components != null )
        {
            int skipped = 0;
            for ( int i = 0; i < components.size (); i++ )
            {
                final Component component = components.get ( i );
                if ( component != null )
                {
                    add ( component, index + i - skipped );
                }
                else
                {
                    skipped++;
                }
            }
        }
        return this;
    }

    /**
     * Adds all components from the list into the panel under the specified constraints.
     *
     * @param components  components to add into panel
     * @param constraints constraints for all components
     * @return this panel
     */
    public WebPanel add ( final List<? extends Component> components, final String constraints )
    {
        if ( components != null )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    add ( component, constraints );
                }
            }
        }
        return this;
    }

    /**
     * Adds all components from the list into the panel.
     *
     * @param components components to add into panel
     * @return this panel
     */
    public WebPanel add ( final List<? extends Component> components )
    {
        if ( components != null )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    add ( component );
                }
            }
        }
        return this;
    }

    /**
     * Adds all components into the panel under the specified index.
     *
     * @param index      index where components should be placed
     * @param components components to add into panel
     * @return this panel
     */
    public WebPanel add ( final int index, final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            int skipped = 0;
            for ( int i = 0; i < components.length; i++ )
            {
                final Component component = components[ i ];
                if ( component != null )
                {
                    add ( component, index + i - skipped );
                }
                else
                {
                    skipped++;
                }
            }
        }
        return this;
    }

    /**
     * Adds all components into the panel under the specified constraints.
     * It might be a rare case when you would require to put more than one component under the same constraint, but it is possible.
     *
     * @param constraints constraints for all components
     * @param components  components to add into panel
     * @return this panel
     */
    public WebPanel add ( final String constraints, final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    add ( component, constraints );
                }
            }
        }
        return this;
    }

    /**
     * Adds all specified components into the panel.
     * Useful for layouts like FlowLayout and some others.
     *
     * @param components components to add into panel
     * @return this panel
     */
    public WebPanel add ( final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    add ( component );
                }
            }
        }
        return this;
    }

    /**
     * Removes all components from the list from the panel.
     *
     * @param components components to remove from panel
     * @return this panel
     */
    public WebPanel remove ( final List<? extends Component> components )
    {
        if ( components != null )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    remove ( component );
                }
            }
        }
        return this;
    }

    /**
     * Removes all specified components from the panel.
     *
     * @param components components to remove from panel
     * @return this panel
     */
    public WebPanel remove ( final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( final Component component : components )
            {
                if ( component != null )
                {
                    remove ( component );
                }
            }
        }
        return this;
    }

    /**
     * Removes all children with the specified component class type.
     *
     * @param componentClass class type of child components to be removed
     * @return this panel
     */
    public WebPanel removeAll ( final Class<? extends Component> componentClass )
    {
        if ( componentClass != null )
        {
            for ( int i = 0; i < getComponentCount (); i++ )
            {
                final Component component = getComponent ( i );
                if ( componentClass.isAssignableFrom ( component.getClass () ) )
                {
                    remove ( component );
                }
            }
        }
        return this;
    }

    /**
     * Returns first component contained in this panel.
     *
     * @return first component contained in this panel
     */
    public Component getFirstComponent ()
    {
        if ( getComponentCount () > 0 )
        {
            return getComponent ( 0 );
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns last component contained in this panel.
     *
     * @return last component contained in this panel
     */
    public Component getLastComponent ()
    {
        if ( getComponentCount () > 0 )
        {
            return getComponent ( getComponentCount () - 1 );
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns whether panel is undecorated or not.
     *
     * @return true if panel is undecorated, false otherwise
     */
    public boolean isUndecorated ()
    {
        return getWebUI ().isUndecorated ();
    }

    /**
     * Sets whether panel should be undecorated or not.
     *
     * @param undecorated whether panel should be undecorated or not
     * @return this panel
     */
    public WebPanel setUndecorated ( final boolean undecorated )
    {
        getWebUI ().setUndecorated ( undecorated );
        return this;
    }

    /**
     * Returns whether focus should be painted or not.
     * Panel focus is displayed when either panel or one of its children are focused.
     *
     * @return true if focus should be painted, false otherwise
     */
    public boolean isPaintFocus ()
    {
        return getWebUI ().isPaintFocus ();
    }

    /**
     * Sets whether focus should be painted or not.
     * Panel focus is displayed when either panel or one of its children are focused.
     *
     * @param paint whether focus should be painted or not
     * @return this panel
     */
    public WebPanel setPaintFocus ( final boolean paint )
    {
        getWebUI ().setPaintFocus ( paint );
        return this;
    }

    /**
     * Returns panel margin.
     *
     * @return panel margin
     */
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets panel margin.
     *
     * @param margin new panel margin
     */
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    /**
     * Sets panel margin.
     *
     * @param top    top panel margin
     * @param left   left panel margin
     * @param bottom bottom panel margin
     * @param right  right panel margin
     * @return this panel
     */
    public WebPanel setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    /**
     * Sets panel margin.
     *
     * @param spacing panel margin
     * @return this panel
     */
    public WebPanel setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    /**
     * Returns whether should paint top side or not.
     *
     * @return true if should paint top side, false otherwise
     */
    public boolean isPaintTop ()
    {
        return getWebUI ().isPaintTop ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintTop ( final boolean top )
    {
        getWebUI ().setPaintTop ( top );
    }

    /**
     * Returns whether should paint left side or not.
     *
     * @return true if should paint left side, false otherwise
     */
    public boolean isPaintLeft ()
    {
        return getWebUI ().isPaintLeft ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintLeft ( final boolean left )
    {
        getWebUI ().setPaintLeft ( left );
    }

    /**
     * Returns whether should paint bottom side or not.
     *
     * @return true if should paint bottom side, false otherwise
     */
    public boolean isPaintBottom ()
    {
        return getWebUI ().isPaintBottom ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintBottom ( final boolean bottom )
    {
        getWebUI ().setPaintBottom ( bottom );
    }

    /**
     * Returns whether should paint right side or not.
     *
     * @return true if should paint right side, false otherwise
     */
    public boolean isPaintRight ()
    {
        return getWebUI ().isPaintRight ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintRight ( final boolean right )
    {
        getWebUI ().setPaintRight ( right );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        getWebUI ().setPaintSides ( top, left, bottom, right );
    }

    /**
     * Returns whether should paint top side line or not.
     *
     * @return true if should paint top side line, false otherwise
     */
    public boolean isPaintTopLine ()
    {
        return getWebUI ().isPaintTopLine ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintTopLine ( final boolean top )
    {
        getWebUI ().setPaintTopLine ( top );
    }

    /**
     * Returns whether should paint left side line or not.
     *
     * @return true if should paint left side line, false otherwise
     */
    public boolean isPaintLeftLine ()
    {
        return getWebUI ().isPaintLeftLine ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintLeftLine ( final boolean left )
    {
        getWebUI ().setPaintLeftLine ( left );
    }

    /**
     * Returns whether should paint bottom side line or not.
     *
     * @return true if should paint bottom side line, false otherwise
     */
    public boolean isPaintBottomLine ()
    {
        return getWebUI ().isPaintBottomLine ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintBottomLine ( final boolean bottom )
    {
        getWebUI ().setPaintBottomLine ( bottom );
    }

    /**
     * Returns whether should paint right side line or not.
     *
     * @return true if should paint right side line, false otherwise
     */
    public boolean isPaintRightLine ()
    {
        return getWebUI ().isPaintRightLine ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintRightLine ( final boolean right )
    {
        getWebUI ().setPaintRightLine ( right );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintSideLines ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        getWebUI ().setPaintSideLines ( top, left, bottom, right );
    }

    /**
     * Returns decoration corners rounding.
     *
     * @return decoration corners rounding
     */
    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    /**
     * Sets decoration corners rounding.
     *
     * @param round decoration corners rounding
     * @return this panel
     */
    public WebPanel setRound ( final int round )
    {
        getWebUI ().setRound ( round );
        return this;
    }

    /**
     * Returns decoration shade width.
     *
     * @return decoration shade width
     */
    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    /**
     * Sets decoration shade width.
     *
     * @param shadeWidth decoration shade width
     * @return this panel
     */
    public WebPanel setShadeWidth ( final int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
        return this;
    }

    /**
     * Returns decoration shade transparency.
     *
     * @return decoration shade transparency
     */
    public float getShadeTransparency ()
    {
        return getWebUI ().getShadeTransparency ();
    }

    /**
     * Sets decoration shade transparency.
     *
     * @param transparency new decoration shade transparency
     * @return this panel
     */
    public WebPanel setShadeTransparency ( final float transparency )
    {
        getWebUI ().setShadeTransparency ( transparency );
        return this;
    }

    /**
     * Returns decoration border stroke.
     *
     * @return decoration border stroke
     */
    public Stroke getBorderStroke ()
    {
        return getWebUI ().getBorderStroke ();
    }

    /**
     * Sets decoration border stroke.
     *
     * @param stroke decoration border stroke
     * @return this panel
     */
    public WebPanel setBorderStroke ( final Stroke stroke )
    {
        getWebUI ().setBorderStroke ( stroke );
        return this;
    }

    /**
     * Returns decoration border color.
     *
     * @return decoration border color
     */
    public Color getBorderColor ()
    {
        return getWebUI ().getBorderColor ();
    }

    /**
     * Sets decoration border color.
     *
     * @param color decoration border color
     * @return this panel
     */
    public WebPanel setBorderColor ( final Color color )
    {
        getWebUI ().setBorderColor ( color );
        return this;
    }

    /**
     * Returns decoration disabled border color.
     *
     * @return decoration disabled border color
     */
    public Color getDisabledBorderColor ()
    {
        return getWebUI ().getDisabledBorderColor ();
    }

    /**
     * Sets decoration disabled border color.
     *
     * @param color decoration disabled border color
     * @return this panel
     */
    public WebPanel setDisabledBorderColor ( final Color color )
    {
        getWebUI ().setDisabledBorderColor ( color );
        return this;
    }

    /**
     * Returns whether should paint decoration background or not.
     *
     * @return true if should paint decoration background, false otherwise
     */
    public boolean isPaintBackground ()
    {
        return getWebUI ().isPaintBackground ();
    }

    /**
     * Sets whether should paint decoration background or not.
     *
     * @param paint whether should paint decoration background or not
     * @return this panel
     */
    public WebPanel setPaintBackground ( final boolean paint )
    {
        getWebUI ().setPaintBackground ( paint );
        return this;
    }

    /**
     * Returns whether should paint web-styled background or not.
     *
     * @return true if should paint web-styled background, false otherwise
     */
    public boolean isWebColoredBackground ()
    {
        return getWebUI ().isWebColoredBackground ();
    }

    /**
     * Sets whether should paint web-styled background or not.
     *
     * @param webColored whether should paint web-styled background or not
     * @return this panel
     */
    public WebPanel setWebColoredBackground ( final boolean webColored )
    {
        getWebUI ().setWebColoredBackground ( webColored );
        return this;
    }

    /**
     * Returns panel painter.
     *
     * @return panel painter
     */
    public Painter getPainter ()
    {
        return StyleManager.getPainter ( this );
    }

    /**
     * Sets panel painter.
     * Pass null to remove panel painter.
     *
     * @param painter new panel painter
     * @return this panel
     */
    public WebPanel setPainter ( final Painter painter )
    {
        StyleManager.setCustomPainter ( this, painter );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        getWebUI ().setStyleId ( id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebPanelUI getWebUI ()
    {
        return ( WebPanelUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebPanelUI ) )
        {
            try
            {
                setUI ( ( WebPanelUI ) ReflectUtils.createInstance ( WebLookAndFeel.panelUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebPanelUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, mouseButton, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseEnter ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseExit ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, mouseButton, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, mouseButton, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onDoubleClick ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMenuTrigger ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, hotkey, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, hotkey, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, hotkey, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusGain ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusLoss ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final String tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final String tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToolTip ( final WebCustomTooltip tooltip )
    {
        TooltipManager.removeTooltip ( this, tooltip );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToolTips ()
    {
        TooltipManager.removeTooltips ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToolTips ( final WebCustomTooltip... tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToolTips ( final List<WebCustomTooltip> tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPanel setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPanel setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPanel setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPanel setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPanel setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPanel setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ()
    {
        Dimension ps = SizeUtils.getPreferredSize ( this, super.getPreferredSize () );

        // Fix to take painter preferres size into account
        final Painter painter = getPainter ();
        if ( painter != null )
        {
            ps = SwingUtils.max ( ps, painter.getPreferredSize ( this ) );
        }

        return ps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPanel setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageContainerKey ( final String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }
}