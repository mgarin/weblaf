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

package com.alee.laf.button;

import com.alee.extended.painter.Painter;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.hotkey.HotkeyInfo;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.EventUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebToggleButton extends JToggleButton
        implements ShapeProvider, EventMethods, ToolTipMethods, LanguageMethods, SettingsMethods, FontMethods<WebToggleButton>,
        SizeMethods<WebToggleButton>
{
    public WebToggleButton ()
    {
        super ();
    }

    public WebToggleButton ( final Icon icon )
    {
        super ( icon );
        setRound ( WebButtonStyle.iconRound );
        setLeftRightSpacing ( WebButtonStyle.iconLeftRightSpacing );
    }

    public WebToggleButton ( final Icon icon, final boolean selected )
    {
        super ( icon, selected );
    }

    public WebToggleButton ( final String text )
    {
        super ( text );
    }

    public WebToggleButton ( final String text, final boolean selected )
    {
        super ( text, selected );
    }

    public WebToggleButton ( final String text, final Icon icon )
    {
        super ( text, icon );
    }

    public WebToggleButton ( final String text, final Icon icon, final boolean selected )
    {
        super ( text, icon, selected );
    }

    public WebToggleButton ( final ActionListener listener )
    {
        super ();
        addActionListener ( listener );
    }

    public WebToggleButton ( final Icon icon, final ActionListener listener )
    {
        super ( icon );
        setRound ( WebButtonStyle.iconRound );
        setLeftRightSpacing ( WebButtonStyle.iconLeftRightSpacing );
        addActionListener ( listener );
    }

    public WebToggleButton ( final Icon icon, final boolean selected, final ActionListener listener )
    {
        super ( icon, selected );
        addActionListener ( listener );
    }

    public WebToggleButton ( final String text, final ActionListener listener )
    {
        super ( text );
        addActionListener ( listener );
    }

    public WebToggleButton ( final String text, final boolean selected, final ActionListener listener )
    {
        super ( text, selected );
        addActionListener ( listener );
    }

    public WebToggleButton ( final String text, final Icon icon, final ActionListener listener )
    {
        super ( text, icon );
        addActionListener ( listener );
    }

    public WebToggleButton ( final String text, final Icon icon, final boolean selected, final ActionListener listener )
    {
        super ( text, icon, selected );
        addActionListener ( listener );
    }

    public WebToggleButton ( final Action a )
    {
        super ( a );
    }

    /**
     * Proxified kotkey manager methods
     */

    public HotkeyInfo addHotkey ( final Integer keyCode )
    {
        return addHotkey ( new HotkeyData ( keyCode ) );
    }

    public HotkeyInfo addHotkey ( final boolean isCtrl, final boolean isAlt, final boolean isShift, final Integer keyCode )
    {
        return addHotkey ( new HotkeyData ( isCtrl, isAlt, isShift, keyCode ) );
    }

    public HotkeyInfo addHotkey ( final HotkeyData hotkeyData )
    {
        return HotkeyManager.registerHotkey ( this, hotkeyData );
    }

    public HotkeyInfo addHotkey ( final HotkeyData hotkeyData, final boolean hidden )
    {
        return HotkeyManager.registerHotkey ( this, hotkeyData, hidden );
    }

    public HotkeyInfo addHotkey ( final HotkeyData hotkeyData, final TooltipWay tooltipWay )
    {
        return HotkeyManager.registerHotkey ( this, hotkeyData, tooltipWay );
    }

    public HotkeyInfo addHotkey ( final Component topComponent, final HotkeyData hotkeyData )
    {
        return HotkeyManager.registerHotkey ( topComponent, this, hotkeyData );
    }

    public HotkeyInfo addHotkey ( final Component topComponent, final HotkeyData hotkeyData, final boolean hidden )
    {
        return HotkeyManager.registerHotkey ( topComponent, this, hotkeyData, hidden );
    }

    public HotkeyInfo addHotkey ( final Component topComponent, final HotkeyData hotkeyData, final TooltipWay tooltipWay )
    {
        return HotkeyManager.registerHotkey ( topComponent, this, hotkeyData, tooltipWay );
    }

    public List<HotkeyInfo> getHotkeys ()
    {
        return HotkeyManager.getComponentHotkeys ( this );
    }

    public void removeHotkey ( final HotkeyInfo hotkeyInfo )
    {
        HotkeyManager.unregisterHotkey ( hotkeyInfo );
    }

    public void removeHotkeys ()
    {
        HotkeyManager.unregisterHotkeys ( this );
    }

    /**
     * UI methods
     */

    public Color getTopBgColor ()
    {
        return getWebUI ().getTopBgColor ();
    }

    public WebToggleButton setTopBgColor ( final Color topBgColor )
    {
        getWebUI ().setTopBgColor ( topBgColor );
        return this;
    }

    public Color getBottomBgColor ()
    {
        return getWebUI ().getBottomBgColor ();
    }

    public WebToggleButton setBottomBgColor ( final Color bottomBgColor )
    {
        getWebUI ().setBottomBgColor ( bottomBgColor );
        return this;
    }

    public Color getTopSelectedBgColor ()
    {
        return getWebUI ().getTopSelectedBgColor ();
    }

    public WebToggleButton setTopSelectedBgColor ( final Color topSelectedBgColor )
    {
        getWebUI ().setTopSelectedBgColor ( topSelectedBgColor );
        return this;
    }

    public Color getBottomSelectedBgColor ()
    {
        return getWebUI ().getBottomSelectedBgColor ();
    }

    public WebToggleButton setBottomSelectedBgColor ( final Color bottomSelectedBgColor )
    {
        getWebUI ().setBottomSelectedBgColor ( bottomSelectedBgColor );
        return this;
    }

    public Color getSelectedForeground ()
    {
        return getWebUI ().getSelectedForeground ();
    }

    public WebToggleButton setSelectedForeground ( final Color selectedForeground )
    {
        getWebUI ().setSelectedForeground ( selectedForeground );
        return this;
    }

    public boolean isRolloverDarkBorderOnly ()
    {
        return getWebUI ().isRolloverDarkBorderOnly ();
    }

    public WebToggleButton setRolloverDarkBorderOnly ( final boolean rolloverDarkBorderOnly )
    {
        getWebUI ().setRolloverDarkBorderOnly ( rolloverDarkBorderOnly );
        return this;
    }

    public boolean isRolloverShine ()
    {
        return getWebUI ().isRolloverShine ();
    }

    public WebToggleButton setRolloverShine ( final boolean rolloverShine )
    {
        getWebUI ().setRolloverShine ( rolloverShine );
        return this;
    }

    public Color getShineColor ()
    {
        return getWebUI ().getShineColor ();
    }

    public WebToggleButton setShineColor ( final Color shineColor )
    {
        getWebUI ().setShineColor ( shineColor );
        return this;
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public WebToggleButton setRound ( final int round )
    {
        getWebUI ().setRound ( round );
        return this;
    }

    public boolean isRolloverShadeOnly ()
    {
        return getWebUI ().isRolloverShadeOnly ();
    }

    public WebToggleButton setRolloverShadeOnly ( final boolean rolloverShadeOnly )
    {
        getWebUI ().setRolloverShadeOnly ( rolloverShadeOnly );
        return this;
    }

    public boolean isShowDisabledShade ()
    {
        return getWebUI ().isShowDisabledShade ();
    }

    public WebToggleButton setShowDisabledShade ( final boolean showDisabledShade )
    {
        getWebUI ().setShowDisabledShade ( showDisabledShade );
        return this;
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public WebToggleButton setShadeWidth ( final int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
        return this;
    }

    public Color getShadeColor ()
    {
        return getWebUI ().getShadeColor ();
    }

    public WebToggleButton setShadeColor ( final Color shadeColor )
    {
        getWebUI ().setShadeColor ( shadeColor );
        return this;
    }

    public int getInnerShadeWidth ()
    {
        return getWebUI ().getInnerShadeWidth ();
    }

    public WebToggleButton setInnerShadeWidth ( final int innerShadeWidth )
    {
        getWebUI ().setInnerShadeWidth ( innerShadeWidth );
        return this;
    }

    public Color getInnerShadeColor ()
    {
        return getWebUI ().getInnerShadeColor ();
    }

    public WebToggleButton setInnerShadeColor ( final Color innerShadeColor )
    {
        getWebUI ().setInnerShadeColor ( innerShadeColor );
        return this;
    }

    public int getLeftRightSpacing ()
    {
        return getWebUI ().getLeftRightSpacing ();
    }

    public WebToggleButton setLeftRightSpacing ( final int leftRightSpacing )
    {
        getWebUI ().setLeftRightSpacing ( leftRightSpacing );
        return this;
    }

    public boolean isRolloverDecoratedOnly ()
    {
        return getWebUI ().isRolloverDecoratedOnly ();
    }

    public WebToggleButton setRolloverDecoratedOnly ( final boolean rolloverDecoratedOnly )
    {
        getWebUI ().setRolloverDecoratedOnly ( rolloverDecoratedOnly );
        return this;
    }

    public boolean isAnimate ()
    {
        return getWebUI ().isAnimate ();
    }

    public WebToggleButton setAnimate ( final boolean animate )
    {
        getWebUI ().setAnimate ( animate );
        return this;
    }

    public boolean isUndecorated ()
    {
        return getWebUI ().isUndecorated ();
    }

    public WebToggleButton setUndecorated ( final boolean undecorated )
    {
        getWebUI ().setUndecorated ( undecorated );
        return this;
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public WebToggleButton setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    public boolean isMoveIconOnPress ()
    {
        return getWebUI ().isMoveIconOnPress ();
    }

    public WebToggleButton setMoveIconOnPress ( final boolean moveIconOnPress )
    {
        getWebUI ().setMoveIconOnPress ( moveIconOnPress );
        return this;
    }

    public boolean isDrawFocus ()
    {
        return getWebUI ().isDrawFocus ();
    }

    public WebToggleButton setDrawFocus ( final boolean drawFocus )
    {
        getWebUI ().setDrawFocus ( drawFocus );
        return this;
    }

    public boolean isShadeToggleIcon ()
    {
        return getWebUI ().isShadeToggleIcon ();
    }

    public WebToggleButton setShadeToggleIcon ( final boolean shadeToggleIcon )
    {
        getWebUI ().setShadeToggleIcon ( shadeToggleIcon );
        return this;
    }

    public float getShadeToggleIconTransparency ()
    {
        return getWebUI ().getShadeToggleIconTransparency ();
    }

    public WebToggleButton setShadeToggleIconTransparency ( final float shadeToggleIconTransparency )
    {
        getWebUI ().setShadeToggleIconTransparency ( shadeToggleIconTransparency );
        return this;
    }

    public boolean isDrawBottom ()
    {
        return getWebUI ().isDrawBottom ();
    }

    public WebToggleButton setDrawBottom ( final boolean drawBottom )
    {
        getWebUI ().setDrawBottom ( drawBottom );
        return this;
    }

    public boolean isDrawLeft ()
    {
        return getWebUI ().isDrawLeft ();
    }

    public WebToggleButton setDrawLeft ( final boolean drawLeft )
    {
        getWebUI ().setDrawLeft ( drawLeft );
        return this;
    }

    public boolean isDrawRight ()
    {
        return getWebUI ().isDrawRight ();
    }

    public WebToggleButton setDrawRight ( final boolean drawRight )
    {
        getWebUI ().setDrawRight ( drawRight );
        return this;
    }

    public boolean isDrawTop ()
    {
        return getWebUI ().isDrawTop ();
    }

    public WebToggleButton setDrawTop ( final boolean drawTop )
    {
        getWebUI ().setDrawTop ( drawTop );
        return this;
    }

    public WebToggleButton setDrawSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        getWebUI ().setDrawSides ( top, left, bottom, right );
        return this;
    }

    public boolean isDrawTopLine ()
    {
        return getWebUI ().isDrawTopLine ();
    }

    public WebToggleButton setDrawTopLine ( final boolean drawTopLine )
    {
        getWebUI ().setDrawTopLine ( drawTopLine );
        return this;
    }

    public boolean isDrawLeftLine ()
    {
        return getWebUI ().isDrawLeftLine ();
    }

    public WebToggleButton setDrawLeftLine ( final boolean drawLeftLine )
    {
        getWebUI ().setDrawLeftLine ( drawLeftLine );
        return this;
    }

    public boolean isDrawBottomLine ()
    {
        return getWebUI ().isDrawBottomLine ();
    }

    public WebToggleButton setDrawBottomLine ( final boolean drawBottomLine )
    {
        getWebUI ().setDrawBottomLine ( drawBottomLine );
        return this;
    }

    public boolean isDrawRightLine ()
    {
        return getWebUI ().isDrawRightLine ();
    }

    public WebToggleButton setDrawRightLine ( final boolean drawRightLine )
    {
        getWebUI ().setDrawRightLine ( drawRightLine );
        return this;
    }

    public WebToggleButton setDrawLines ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        getWebUI ().setDrawLines ( top, left, bottom, right );
        return this;
    }

    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public WebToggleButton setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebToggleButton setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebToggleButtonUI getWebUI ()
    {
        return ( WebToggleButtonUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebToggleButtonUI ) )
        {
            try
            {
                setUI ( ( WebToggleButtonUI ) ReflectUtils.createInstance ( WebLookAndFeel.toggleButtonUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebToggleButtonUI () );
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
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setPlainFont ( final boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setBoldFont ( final boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setItalicFont ( final boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton changeFontSize ( final int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setFontName ( final String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
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
    public WebToggleButton setPreferredWidth ( final int preferredWidth )
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
    public WebToggleButton setPreferredHeight ( final int preferredHeight )
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
    public WebToggleButton setMinimumWidth ( final int minimumWidth )
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
    public WebToggleButton setMinimumHeight ( final int minimumHeight )
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
    public WebToggleButton setMaximumWidth ( final int maximumWidth )
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
    public WebToggleButton setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }

    /**
     * Styled toggle button short creation methods
     */

    public static WebToggleButton createIconWebButton ( final ImageIcon imageIcon )
    {
        return createIconWebButton ( imageIcon, StyleConstants.smallRound );
    }

    public static WebToggleButton createIconWebButton ( final ImageIcon imageIcon, final int round )
    {
        return createIconWebButton ( imageIcon, round, StyleConstants.shadeWidth );
    }

    public static WebToggleButton createIconWebButton ( final ImageIcon imageIcon, final int round, final int shadeWidth )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, StyleConstants.innerShadeWidth );
    }

    public static WebToggleButton createIconWebButton ( final ImageIcon imageIcon, final int round, final int shadeWidth,
                                                        final int innerShadeWidth )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, StyleConstants.rolloverDecoratedOnly );
    }

    public static WebToggleButton createIconWebButton ( final ImageIcon imageIcon, final int round, final int shadeWidth,
                                                        final int innerShadeWidth, final boolean rolloverDecoratedOnly )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, rolloverDecoratedOnly, StyleConstants.undecorated );
    }

    public static WebToggleButton createIconWebButton ( final ImageIcon imageIcon, final int round, final int shadeWidth,
                                                        final int innerShadeWidth, final boolean rolloverDecoratedOnly,
                                                        final boolean undecorated )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, rolloverDecoratedOnly, undecorated, true );
    }

    public static WebToggleButton createIconWebButton ( final ImageIcon imageIcon, final int round, final int shadeWidth,
                                                        final int innerShadeWidth, final boolean rolloverDecoratedOnly,
                                                        final boolean undecorated, final boolean drawFocus )
    {
        final WebToggleButton iconWebButton =
                createWebButton ( round, shadeWidth, innerShadeWidth, 0, rolloverDecoratedOnly, undecorated, drawFocus );
        iconWebButton.setIcon ( imageIcon );
        return iconWebButton;
    }

    public static WebToggleButton createWebButton ( final int round, final int shadeWidth, final int innerShadeWidth,
                                                    final int leftRightSpacing, final boolean rolloverDecoratedOnly,
                                                    final boolean undecorated, final boolean drawFocus )
    {
        final WebToggleButton webButton = new WebToggleButton ();
        webButton.setRound ( round );
        webButton.setShadeWidth ( shadeWidth );
        webButton.setInnerShadeWidth ( innerShadeWidth );
        webButton.setLeftRightSpacing ( leftRightSpacing );
        webButton.setRolloverDecoratedOnly ( rolloverDecoratedOnly );
        webButton.setUndecorated ( undecorated );
        webButton.setDrawFocus ( drawFocus );
        return webButton;
    }
}