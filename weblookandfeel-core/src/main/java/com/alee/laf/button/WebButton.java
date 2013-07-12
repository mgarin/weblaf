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
import com.alee.managers.tooltip.TooltipWay;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * User: mgarin Date: 28.06.11 Time: 0:43
 */

public class WebButton extends JButton implements ShapeProvider, LanguageMethods, FontMethods<WebButton>
{
    public WebButton ()
    {
        super ();
    }

    public WebButton ( Icon icon )
    {
        super ( icon );
        setRound ( WebButtonStyle.iconRound );
        setLeftRightSpacing ( WebButtonStyle.iconLeftRightSpacing );
    }

    public WebButton ( String text )
    {
        super ( text );
    }

    public WebButton ( Action a )
    {
        super ( a );
    }

    public WebButton ( String text, Icon icon )
    {
        super ( text, icon );
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

    public WebButton setTopBgColor ( Color topBgColor )
    {
        getWebUI ().setTopBgColor ( topBgColor );
        return this;
    }

    public Color getBottomBgColor ()
    {
        return getWebUI ().getBottomBgColor ();
    }

    public WebButton setBottomBgColor ( Color bottomBgColor )
    {
        getWebUI ().setBottomBgColor ( bottomBgColor );
        return this;
    }

    public Color getTopSelectedBgColor ()
    {
        return getWebUI ().getTopSelectedBgColor ();
    }

    public WebButton setTopSelectedBgColor ( Color topSelectedBgColor )
    {
        getWebUI ().setTopSelectedBgColor ( topSelectedBgColor );
        return this;
    }

    public Color getBottomSelectedBgColor ()
    {
        return getWebUI ().getBottomSelectedBgColor ();
    }

    public WebButton setBottomSelectedBgColor ( Color bottomSelectedBgColor )
    {
        getWebUI ().setBottomSelectedBgColor ( bottomSelectedBgColor );
        return this;
    }

    public Color getSelectedForeground ()
    {
        return getWebUI ().getSelectedForeground ();
    }

    public WebButton setSelectedForeground ( Color selectedForeground )
    {
        getWebUI ().setSelectedForeground ( selectedForeground );
        return this;
    }

    public boolean isRolloverDarkBorderOnly ()
    {
        return getWebUI ().isRolloverDarkBorderOnly ();
    }

    public WebButton setRolloverDarkBorderOnly ( boolean rolloverDarkBorderOnly )
    {
        getWebUI ().setRolloverDarkBorderOnly ( rolloverDarkBorderOnly );
        return this;
    }

    public boolean isRolloverShine ()
    {
        return getWebUI ().isRolloverShine ();
    }

    public WebButton setRolloverShine ( boolean rolloverShine )
    {
        getWebUI ().setRolloverShine ( rolloverShine );
        return this;
    }

    public Color getShineColor ()
    {
        return getWebUI ().getShineColor ();
    }

    public WebButton setShineColor ( Color shineColor )
    {
        getWebUI ().setShineColor ( shineColor );
        return this;
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public WebButton setRound ( int round )
    {
        getWebUI ().setRound ( round );
        return this;
    }

    public boolean isDrawShade ()
    {
        return getWebUI ().isDrawShade ();
    }

    public WebButton setDrawShade ( boolean drawShade )
    {
        getWebUI ().setDrawShade ( drawShade );
        return this;
    }

    public boolean isRolloverShadeOnly ()
    {
        return getWebUI ().isRolloverShadeOnly ();
    }

    public WebButton setRolloverShadeOnly ( boolean rolloverShadeOnly )
    {
        getWebUI ().setRolloverShadeOnly ( rolloverShadeOnly );
        return this;
    }

    public boolean isShowDisabledShade ()
    {
        return getWebUI ().isShowDisabledShade ();
    }

    public WebButton setShowDisabledShade ( boolean showDisabledShade )
    {
        getWebUI ().setShowDisabledShade ( showDisabledShade );
        return this;
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public WebButton setShadeWidth ( int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
        return this;
    }

    public Color getShadeColor ()
    {
        return getWebUI ().getShadeColor ();
    }

    public WebButton setShadeColor ( Color shadeColor )
    {
        getWebUI ().setShadeColor ( shadeColor );
        return this;
    }

    public int getInnerShadeWidth ()
    {
        return getWebUI ().getInnerShadeWidth ();
    }

    public WebButton setInnerShadeWidth ( int innerShadeWidth )
    {
        getWebUI ().setInnerShadeWidth ( innerShadeWidth );
        return this;
    }

    public Color getInnerShadeColor ()
    {
        return getWebUI ().getInnerShadeColor ();
    }

    public WebButton setInnerShadeColor ( Color innerShadeColor )
    {
        getWebUI ().setInnerShadeColor ( innerShadeColor );
        return this;
    }

    public int getLeftRightSpacing ()
    {
        return getWebUI ().getLeftRightSpacing ();
    }

    public WebButton setLeftRightSpacing ( int leftRightSpacing )
    {
        getWebUI ().setLeftRightSpacing ( leftRightSpacing );
        return this;
    }

    public boolean isRolloverDecoratedOnly ()
    {
        return getWebUI ().isRolloverDecoratedOnly ();
    }

    public WebButton setRolloverDecoratedOnly ( boolean rolloverDecoratedOnly )
    {
        getWebUI ().setRolloverDecoratedOnly ( rolloverDecoratedOnly );
        return this;
    }

    public boolean isUndecorated ()
    {
        return getWebUI ().isUndecorated ();
    }

    public WebButton setUndecorated ( boolean undecorated )
    {
        getWebUI ().setUndecorated ( undecorated );
        return this;
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public WebButton setPainter ( Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    public boolean isMoveIconOnPress ()
    {
        return getWebUI ().isMoveIconOnPress ();
    }

    public WebButton setMoveIconOnPress ( boolean moveIconOnPress )
    {
        getWebUI ().setMoveIconOnPress ( moveIconOnPress );
        return this;
    }

    public boolean isDrawFocus ()
    {
        return getWebUI ().isDrawFocus ();
    }

    public WebButton setDrawFocus ( boolean drawFocus )
    {
        getWebUI ().setDrawFocus ( drawFocus );
        return this;
    }

    public boolean isDrawBottom ()
    {
        return getWebUI ().isDrawBottom ();
    }

    public WebButton setDrawBottom ( boolean drawBottom )
    {
        getWebUI ().setDrawBottom ( drawBottom );
        return this;
    }

    public boolean isDrawLeft ()
    {
        return getWebUI ().isDrawLeft ();
    }

    public WebButton setDrawLeft ( boolean drawLeft )
    {
        getWebUI ().setDrawLeft ( drawLeft );
        return this;
    }

    public boolean isDrawRight ()
    {
        return getWebUI ().isDrawRight ();
    }

    public WebButton setDrawRight ( boolean drawRight )
    {
        getWebUI ().setDrawRight ( drawRight );
        return this;
    }

    public boolean isDrawTop ()
    {
        return getWebUI ().isDrawTop ();
    }

    public WebButton setDrawTop ( boolean drawTop )
    {
        getWebUI ().setDrawTop ( drawTop );
        return this;
    }

    public WebButton setDrawSides ( boolean top, boolean left, boolean bottom, boolean right )
    {
        getWebUI ().setDrawSides ( top, left, bottom, right );
        return this;
    }

    public boolean isDrawTopLine ()
    {
        return getWebUI ().isDrawTopLine ();
    }

    public WebButton setDrawTopLine ( boolean drawTopLine )
    {
        getWebUI ().setDrawTopLine ( drawTopLine );
        return this;
    }

    public boolean isDrawLeftLine ()
    {
        return getWebUI ().isDrawLeftLine ();
    }

    public WebButton setDrawLeftLine ( boolean drawLeftLine )
    {
        getWebUI ().setDrawLeftLine ( drawLeftLine );
        return this;
    }

    public boolean isDrawBottomLine ()
    {
        return getWebUI ().isDrawBottomLine ();
    }

    public WebButton setDrawBottomLine ( boolean drawBottomLine )
    {
        getWebUI ().setDrawBottomLine ( drawBottomLine );
        return this;
    }

    public boolean isDrawRightLine ()
    {
        return getWebUI ().isDrawRightLine ();
    }

    public WebButton setDrawRightLine ( boolean drawRightLine )
    {
        getWebUI ().setDrawRightLine ( drawRightLine );
        return this;
    }

    public WebButton setDrawLines ( boolean top, boolean left, boolean bottom, boolean right )
    {
        getWebUI ().setDrawLines ( top, left, bottom, right );
        return this;
    }

    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    public void setMargin ( Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public WebButton setMargin ( int top, int left, int bottom, int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebButton setMargin ( int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebButtonUI getWebUI ()
    {
        return ( WebButtonUI ) getUI ();
    }

    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebButtonUI ) )
        {
            try
            {
                setUI ( ( WebButtonUI ) ReflectUtils.createInstance ( WebLookAndFeel.buttonUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebButtonUI () );
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
    public void setLanguage ( String key, Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    public void updateLanguage ( Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public void updateLanguage ( String key, Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    public void setLanguageUpdater ( LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    /**
     * {@inheritDoc}
     */
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }

    /**
     * Font methods
     */

    /**
     * {@inheritDoc}
     */
    public WebButton setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebButton setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebButton setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebButton setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebButton setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebButton setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    public WebButton changeFontSize ( int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    /**
     * {@inheritDoc}
     */
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebButton setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebButton setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebButton setFontName ( String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    /**
     * {@inheritDoc}
     */
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }

    /**
     * Styled button short creation methods
     */

    public static WebButton createIconWebButton ( ImageIcon imageIcon )
    {
        return createIconWebButton ( imageIcon, StyleConstants.smallRound );
    }

    public static WebButton createIconWebButton ( ImageIcon imageIcon, int round )
    {
        return createIconWebButton ( imageIcon, round, StyleConstants.shadeWidth );
    }

    public static WebButton createIconWebButton ( ImageIcon imageIcon, int round, int shadeWidth )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, StyleConstants.innerShadeWidth );
    }

    public static WebButton createIconWebButton ( ImageIcon imageIcon, int round, int shadeWidth, int innerShadeWidth )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, StyleConstants.rolloverDecoratedOnly );
    }

    public static WebButton createIconWebButton ( ImageIcon imageIcon, boolean rolloverDecoratedOnly )
    {
        return createIconWebButton ( imageIcon, StyleConstants.smallRound, rolloverDecoratedOnly );
    }

    public static WebButton createIconWebButton ( ImageIcon imageIcon, int round, boolean rolloverDecoratedOnly )
    {
        return createIconWebButton ( imageIcon, round, StyleConstants.shadeWidth, StyleConstants.innerShadeWidth, rolloverDecoratedOnly,
                StyleConstants.undecorated );
    }

    public static WebButton createIconWebButton ( ImageIcon imageIcon, int round, int shadeWidth, int innerShadeWidth,
                                                  boolean rolloverDecoratedOnly )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, rolloverDecoratedOnly, StyleConstants.undecorated );
    }

    public static WebButton createIconWebButton ( ImageIcon imageIcon, int round, int shadeWidth, int innerShadeWidth,
                                                  boolean rolloverDecoratedOnly, boolean undecorated )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, rolloverDecoratedOnly, undecorated, true );
    }

    public static WebButton createIconWebButton ( ImageIcon imageIcon, int round, int shadeWidth, int innerShadeWidth,
                                                  boolean rolloverDecoratedOnly, boolean undecorated, boolean drawFocus )
    {
        WebButton iconWebButton = createWebButton ( round, shadeWidth, innerShadeWidth, 0, rolloverDecoratedOnly, undecorated, drawFocus );
        iconWebButton.setIcon ( imageIcon );
        return iconWebButton;
    }

    public static WebButton createWebButton ( int round, int shadeWidth, int innerShadeWidth, int leftRightSpacing,
                                              boolean rolloverDecoratedOnly, boolean undecorated, boolean drawFocus )
    {
        WebButton webButton = new WebButton ();
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