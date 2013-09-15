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
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.hotkey.HotkeyInfo;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.FontMethods;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * User: mgarin Date: 28.06.11 Time: 0:47
 */

public class WebToggleButton extends JToggleButton
        implements ShapeProvider, LanguageMethods, SettingsMethods, FontMethods<WebToggleButton>, SizeMethods<WebToggleButton>
{
    public WebToggleButton ()
    {
        super ();
    }

    public WebToggleButton ( Icon icon )
    {
        super ( icon );
        setRound ( WebButtonStyle.iconRound );
        setLeftRightSpacing ( WebButtonStyle.iconLeftRightSpacing );
    }

    public WebToggleButton ( Icon icon, boolean selected )
    {
        super ( icon, selected );
    }

    public WebToggleButton ( String text )
    {
        super ( text );
    }

    public WebToggleButton ( String text, boolean selected )
    {
        super ( text, selected );
    }

    public WebToggleButton ( String text, Icon icon )
    {
        super ( text, icon );
    }

    public WebToggleButton ( String text, Icon icon, boolean selected )
    {
        super ( text, icon, selected );
    }

    public WebToggleButton ( ActionListener listener )
    {
        super ();
        addActionListener ( listener );
    }

    public WebToggleButton ( Icon icon, ActionListener listener )
    {
        super ( icon );
        setRound ( WebButtonStyle.iconRound );
        setLeftRightSpacing ( WebButtonStyle.iconLeftRightSpacing );
        addActionListener ( listener );
    }

    public WebToggleButton ( Icon icon, boolean selected, ActionListener listener )
    {
        super ( icon, selected );
        addActionListener ( listener );
    }

    public WebToggleButton ( String text, ActionListener listener )
    {
        super ( text );
        addActionListener ( listener );
    }

    public WebToggleButton ( String text, boolean selected, ActionListener listener )
    {
        super ( text, selected );
        addActionListener ( listener );
    }

    public WebToggleButton ( String text, Icon icon, ActionListener listener )
    {
        super ( text, icon );
        addActionListener ( listener );
    }

    public WebToggleButton ( String text, Icon icon, boolean selected, ActionListener listener )
    {
        super ( text, icon, selected );
        addActionListener ( listener );
    }

    public WebToggleButton ( Action a )
    {
        super ( a );
    }

    /**
     * Proxified kotkey manager methods
     */

    public HotkeyInfo addHotkey ( Integer keyCode )
    {
        return addHotkey ( new HotkeyData ( keyCode ) );
    }

    public HotkeyInfo addHotkey ( boolean isCtrl, boolean isAlt, boolean isShift, Integer keyCode )
    {
        return addHotkey ( new HotkeyData ( isCtrl, isAlt, isShift, keyCode ) );
    }

    public HotkeyInfo addHotkey ( HotkeyData hotkeyData )
    {
        return HotkeyManager.registerHotkey ( this, hotkeyData );
    }

    public HotkeyInfo addHotkey ( HotkeyData hotkeyData, boolean hidden )
    {
        return HotkeyManager.registerHotkey ( this, hotkeyData, hidden );
    }

    public HotkeyInfo addHotkey ( HotkeyData hotkeyData, TooltipWay tooltipWay )
    {
        return HotkeyManager.registerHotkey ( this, hotkeyData, tooltipWay );
    }

    public HotkeyInfo addHotkey ( Component topComponent, HotkeyData hotkeyData )
    {
        return HotkeyManager.registerHotkey ( topComponent, this, hotkeyData );
    }

    public HotkeyInfo addHotkey ( Component topComponent, HotkeyData hotkeyData, boolean hidden )
    {
        return HotkeyManager.registerHotkey ( topComponent, this, hotkeyData, hidden );
    }

    public HotkeyInfo addHotkey ( Component topComponent, HotkeyData hotkeyData, TooltipWay tooltipWay )
    {
        return HotkeyManager.registerHotkey ( topComponent, this, hotkeyData, tooltipWay );
    }

    public List<HotkeyInfo> getHotkeys ()
    {
        return HotkeyManager.getComponentHotkeys ( this );
    }

    public void removeHotkey ( HotkeyInfo hotkeyInfo )
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

    public WebToggleButton setTopBgColor ( Color topBgColor )
    {
        getWebUI ().setTopBgColor ( topBgColor );
        return this;
    }

    public Color getBottomBgColor ()
    {
        return getWebUI ().getBottomBgColor ();
    }

    public WebToggleButton setBottomBgColor ( Color bottomBgColor )
    {
        getWebUI ().setBottomBgColor ( bottomBgColor );
        return this;
    }

    public Color getTopSelectedBgColor ()
    {
        return getWebUI ().getTopSelectedBgColor ();
    }

    public WebToggleButton setTopSelectedBgColor ( Color topSelectedBgColor )
    {
        getWebUI ().setTopSelectedBgColor ( topSelectedBgColor );
        return this;
    }

    public Color getBottomSelectedBgColor ()
    {
        return getWebUI ().getBottomSelectedBgColor ();
    }

    public WebToggleButton setBottomSelectedBgColor ( Color bottomSelectedBgColor )
    {
        getWebUI ().setBottomSelectedBgColor ( bottomSelectedBgColor );
        return this;
    }

    public Color getSelectedForeground ()
    {
        return getWebUI ().getSelectedForeground ();
    }

    public WebToggleButton setSelectedForeground ( Color selectedForeground )
    {
        getWebUI ().setSelectedForeground ( selectedForeground );
        return this;
    }

    public boolean isRolloverDarkBorderOnly ()
    {
        return getWebUI ().isRolloverDarkBorderOnly ();
    }

    public WebToggleButton setRolloverDarkBorderOnly ( boolean rolloverDarkBorderOnly )
    {
        getWebUI ().setRolloverDarkBorderOnly ( rolloverDarkBorderOnly );
        return this;
    }

    public boolean isRolloverShine ()
    {
        return getWebUI ().isRolloverShine ();
    }

    public WebToggleButton setRolloverShine ( boolean rolloverShine )
    {
        getWebUI ().setRolloverShine ( rolloverShine );
        return this;
    }

    public Color getShineColor ()
    {
        return getWebUI ().getShineColor ();
    }

    public WebToggleButton setShineColor ( Color shineColor )
    {
        getWebUI ().setShineColor ( shineColor );
        return this;
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public WebToggleButton setRound ( int round )
    {
        getWebUI ().setRound ( round );
        return this;
    }

    public boolean isRolloverShadeOnly ()
    {
        return getWebUI ().isRolloverShadeOnly ();
    }

    public WebToggleButton setRolloverShadeOnly ( boolean rolloverShadeOnly )
    {
        getWebUI ().setRolloverShadeOnly ( rolloverShadeOnly );
        return this;
    }

    public boolean isShowDisabledShade ()
    {
        return getWebUI ().isShowDisabledShade ();
    }

    public WebToggleButton setShowDisabledShade ( boolean showDisabledShade )
    {
        getWebUI ().setShowDisabledShade ( showDisabledShade );
        return this;
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public WebToggleButton setShadeWidth ( int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
        return this;
    }

    public Color getShadeColor ()
    {
        return getWebUI ().getShadeColor ();
    }

    public WebToggleButton setShadeColor ( Color shadeColor )
    {
        getWebUI ().setShadeColor ( shadeColor );
        return this;
    }

    public int getInnerShadeWidth ()
    {
        return getWebUI ().getInnerShadeWidth ();
    }

    public WebToggleButton setInnerShadeWidth ( int innerShadeWidth )
    {
        getWebUI ().setInnerShadeWidth ( innerShadeWidth );
        return this;
    }

    public Color getInnerShadeColor ()
    {
        return getWebUI ().getInnerShadeColor ();
    }

    public WebToggleButton setInnerShadeColor ( Color innerShadeColor )
    {
        getWebUI ().setInnerShadeColor ( innerShadeColor );
        return this;
    }

    public int getLeftRightSpacing ()
    {
        return getWebUI ().getLeftRightSpacing ();
    }

    public WebToggleButton setLeftRightSpacing ( int leftRightSpacing )
    {
        getWebUI ().setLeftRightSpacing ( leftRightSpacing );
        return this;
    }

    public boolean isRolloverDecoratedOnly ()
    {
        return getWebUI ().isRolloverDecoratedOnly ();
    }

    public WebToggleButton setRolloverDecoratedOnly ( boolean rolloverDecoratedOnly )
    {
        getWebUI ().setRolloverDecoratedOnly ( rolloverDecoratedOnly );
        return this;
    }

    public boolean isUndecorated ()
    {
        return getWebUI ().isUndecorated ();
    }

    public WebToggleButton setUndecorated ( boolean undecorated )
    {
        getWebUI ().setUndecorated ( undecorated );
        return this;
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public WebToggleButton setPainter ( Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    public boolean isMoveIconOnPress ()
    {
        return getWebUI ().isMoveIconOnPress ();
    }

    public WebToggleButton setMoveIconOnPress ( boolean moveIconOnPress )
    {
        getWebUI ().setMoveIconOnPress ( moveIconOnPress );
        return this;
    }

    public boolean isDrawFocus ()
    {
        return getWebUI ().isDrawFocus ();
    }

    public WebToggleButton setDrawFocus ( boolean drawFocus )
    {
        getWebUI ().setDrawFocus ( drawFocus );
        return this;
    }

    public boolean isShadeToggleIcon ()
    {
        return getWebUI ().isShadeToggleIcon ();
    }

    public WebToggleButton setShadeToggleIcon ( boolean shadeToggleIcon )
    {
        getWebUI ().setShadeToggleIcon ( shadeToggleIcon );
        return this;
    }

    public float getShadeToggleIconTransparency ()
    {
        return getWebUI ().getShadeToggleIconTransparency ();
    }

    public WebToggleButton setShadeToggleIconTransparency ( float shadeToggleIconTransparency )
    {
        getWebUI ().setShadeToggleIconTransparency ( shadeToggleIconTransparency );
        return this;
    }

    public boolean isDrawBottom ()
    {
        return getWebUI ().isDrawBottom ();
    }

    public WebToggleButton setDrawBottom ( boolean drawBottom )
    {
        getWebUI ().setDrawBottom ( drawBottom );
        return this;
    }

    public boolean isDrawLeft ()
    {
        return getWebUI ().isDrawLeft ();
    }

    public WebToggleButton setDrawLeft ( boolean drawLeft )
    {
        getWebUI ().setDrawLeft ( drawLeft );
        return this;
    }

    public boolean isDrawRight ()
    {
        return getWebUI ().isDrawRight ();
    }

    public WebToggleButton setDrawRight ( boolean drawRight )
    {
        getWebUI ().setDrawRight ( drawRight );
        return this;
    }

    public boolean isDrawTop ()
    {
        return getWebUI ().isDrawTop ();
    }

    public WebToggleButton setDrawTop ( boolean drawTop )
    {
        getWebUI ().setDrawTop ( drawTop );
        return this;
    }

    public WebToggleButton setDrawSides ( boolean top, boolean left, boolean bottom, boolean right )
    {
        getWebUI ().setDrawSides ( top, left, bottom, right );
        return this;
    }

    public boolean isDrawTopLine ()
    {
        return getWebUI ().isDrawTopLine ();
    }

    public WebToggleButton setDrawTopLine ( boolean drawTopLine )
    {
        getWebUI ().setDrawTopLine ( drawTopLine );
        return this;
    }

    public boolean isDrawLeftLine ()
    {
        return getWebUI ().isDrawLeftLine ();
    }

    public WebToggleButton setDrawLeftLine ( boolean drawLeftLine )
    {
        getWebUI ().setDrawLeftLine ( drawLeftLine );
        return this;
    }

    public boolean isDrawBottomLine ()
    {
        return getWebUI ().isDrawBottomLine ();
    }

    public WebToggleButton setDrawBottomLine ( boolean drawBottomLine )
    {
        getWebUI ().setDrawBottomLine ( drawBottomLine );
        return this;
    }

    public boolean isDrawRightLine ()
    {
        return getWebUI ().isDrawRightLine ();
    }

    public WebToggleButton setDrawRightLine ( boolean drawRightLine )
    {
        getWebUI ().setDrawRightLine ( drawRightLine );
        return this;
    }

    public WebToggleButton setDrawLines ( boolean top, boolean left, boolean bottom, boolean right )
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
    public void setMargin ( Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public WebToggleButton setMargin ( int top, int left, int bottom, int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebToggleButton setMargin ( int spacing )
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
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebToggleButtonUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * Language methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguage ( String key, Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( String key, Object... data )
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
    public void setLanguageUpdater ( LanguageUpdater updater )
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
     * Settings methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String group, String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String group, String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass, boolean loadInitialSettings,
                                                            boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key, Object defaultValue, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass,
                                                            boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String group, String key, Object defaultValue, boolean loadInitialSettings,
                                   boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( SettingsProcessor settingsProcessor )
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
     * Font methods
     */

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
    public WebToggleButton setPlainFont ( boolean apply )
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
    public WebToggleButton setBoldFont ( boolean apply )
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
    public WebToggleButton setItalicFont ( boolean apply )
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
    public WebToggleButton setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton changeFontSize ( int change )
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
    public WebToggleButton setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToggleButton setFontName ( String fontName )
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
     * Size methods.
     */

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
    public WebToggleButton setPreferredWidth ( int preferredWidth )
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
    public WebToggleButton setPreferredHeight ( int preferredHeight )
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
    public WebToggleButton setMinimumWidth ( int minimumWidth )
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
    public WebToggleButton setMinimumHeight ( int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
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
     * Styled toggle button short creation methods
     */

    public static WebToggleButton createIconWebButton ( ImageIcon imageIcon )
    {
        return createIconWebButton ( imageIcon, StyleConstants.smallRound );
    }

    public static WebToggleButton createIconWebButton ( ImageIcon imageIcon, int round )
    {
        return createIconWebButton ( imageIcon, round, StyleConstants.shadeWidth );
    }

    public static WebToggleButton createIconWebButton ( ImageIcon imageIcon, int round, int shadeWidth )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, StyleConstants.innerShadeWidth );
    }

    public static WebToggleButton createIconWebButton ( ImageIcon imageIcon, int round, int shadeWidth, int innerShadeWidth )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, StyleConstants.rolloverDecoratedOnly );
    }

    public static WebToggleButton createIconWebButton ( ImageIcon imageIcon, int round, int shadeWidth, int innerShadeWidth,
                                                        boolean rolloverDecoratedOnly )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, rolloverDecoratedOnly, StyleConstants.undecorated );
    }

    public static WebToggleButton createIconWebButton ( ImageIcon imageIcon, int round, int shadeWidth, int innerShadeWidth,
                                                        boolean rolloverDecoratedOnly, boolean undecorated )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, rolloverDecoratedOnly, undecorated, true );
    }

    public static WebToggleButton createIconWebButton ( ImageIcon imageIcon, int round, int shadeWidth, int innerShadeWidth,
                                                        boolean rolloverDecoratedOnly, boolean undecorated, boolean drawFocus )
    {
        WebToggleButton iconWebButton =
                createWebButton ( round, shadeWidth, innerShadeWidth, 0, rolloverDecoratedOnly, undecorated, drawFocus );
        iconWebButton.setIcon ( imageIcon );
        return iconWebButton;
    }

    public static WebToggleButton createWebButton ( int round, int shadeWidth, int innerShadeWidth, int leftRightSpacing,
                                                    boolean rolloverDecoratedOnly, boolean undecorated, boolean drawFocus )
    {
        WebToggleButton webButton = new WebToggleButton ();
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