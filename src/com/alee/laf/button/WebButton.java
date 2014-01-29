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
 * User: mgarin Date: 28.06.11 Time: 0:43
 */

public class WebButton extends JButton implements ShapeProvider, LanguageMethods, FontMethods<WebButton>, SizeMethods<WebButton>
{
    public WebButton ()
    {
        super ();
    }

    public WebButton ( final Icon icon )
    {
        super ( icon );
        setRound ( WebButtonStyle.iconRound );
        setLeftRightSpacing ( WebButtonStyle.iconLeftRightSpacing );
    }

    public WebButton ( final String text )
    {
        super ( text );
    }

    public WebButton ( final String text, final Icon icon )
    {
        super ( text, icon );
    }

    public WebButton ( final ActionListener listener )
    {
        super ();
        addActionListener ( listener );
    }

    public WebButton ( final Icon icon, final ActionListener listener )
    {
        super ( icon );
        setRound ( WebButtonStyle.iconRound );
        setLeftRightSpacing ( WebButtonStyle.iconLeftRightSpacing );
        addActionListener ( listener );
    }

    public WebButton ( final String text, final ActionListener listener )
    {
        super ( text );
        addActionListener ( listener );
    }

    public WebButton ( final String text, final Icon icon, final ActionListener listener )
    {
        super ( text, icon );
        addActionListener ( listener );
    }

    public WebButton ( final Action a )
    {
        super ( a );
    }

    public WebButton ( final Painter painter )
    {
        super ();
        setPainter ( painter );
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

    public WebButton setTopBgColor ( final Color topBgColor )
    {
        getWebUI ().setTopBgColor ( topBgColor );
        return this;
    }

    public Color getBottomBgColor ()
    {
        return getWebUI ().getBottomBgColor ();
    }

    public WebButton setBottomBgColor ( final Color bottomBgColor )
    {
        getWebUI ().setBottomBgColor ( bottomBgColor );
        return this;
    }

    public Color getTopSelectedBgColor ()
    {
        return getWebUI ().getTopSelectedBgColor ();
    }

    public WebButton setTopSelectedBgColor ( final Color topSelectedBgColor )
    {
        getWebUI ().setTopSelectedBgColor ( topSelectedBgColor );
        return this;
    }

    public Color getBottomSelectedBgColor ()
    {
        return getWebUI ().getBottomSelectedBgColor ();
    }

    public WebButton setBottomSelectedBgColor ( final Color bottomSelectedBgColor )
    {
        getWebUI ().setBottomSelectedBgColor ( bottomSelectedBgColor );
        return this;
    }

    public Color getSelectedForeground ()
    {
        return getWebUI ().getSelectedForeground ();
    }

    public WebButton setSelectedForeground ( final Color selectedForeground )
    {
        getWebUI ().setSelectedForeground ( selectedForeground );
        return this;
    }

    public boolean isRolloverDarkBorderOnly ()
    {
        return getWebUI ().isRolloverDarkBorderOnly ();
    }

    public WebButton setRolloverDarkBorderOnly ( final boolean rolloverDarkBorderOnly )
    {
        getWebUI ().setRolloverDarkBorderOnly ( rolloverDarkBorderOnly );
        return this;
    }

    public boolean isRolloverShine ()
    {
        return getWebUI ().isRolloverShine ();
    }

    public WebButton setRolloverShine ( final boolean rolloverShine )
    {
        getWebUI ().setRolloverShine ( rolloverShine );
        return this;
    }

    public Color getShineColor ()
    {
        return getWebUI ().getShineColor ();
    }

    public WebButton setShineColor ( final Color shineColor )
    {
        getWebUI ().setShineColor ( shineColor );
        return this;
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public WebButton setRound ( final int round )
    {
        getWebUI ().setRound ( round );
        return this;
    }

    public boolean isDrawShade ()
    {
        return getWebUI ().isDrawShade ();
    }

    public WebButton setDrawShade ( final boolean drawShade )
    {
        getWebUI ().setDrawShade ( drawShade );
        return this;
    }

    public boolean isRolloverShadeOnly ()
    {
        return getWebUI ().isRolloverShadeOnly ();
    }

    public WebButton setRolloverShadeOnly ( final boolean rolloverShadeOnly )
    {
        getWebUI ().setRolloverShadeOnly ( rolloverShadeOnly );
        return this;
    }

    public boolean isShowDisabledShade ()
    {
        return getWebUI ().isShowDisabledShade ();
    }

    public WebButton setShowDisabledShade ( final boolean showDisabledShade )
    {
        getWebUI ().setShowDisabledShade ( showDisabledShade );
        return this;
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public WebButton setShadeWidth ( final int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
        return this;
    }

    public Color getShadeColor ()
    {
        return getWebUI ().getShadeColor ();
    }

    public WebButton setShadeColor ( final Color shadeColor )
    {
        getWebUI ().setShadeColor ( shadeColor );
        return this;
    }

    public int getInnerShadeWidth ()
    {
        return getWebUI ().getInnerShadeWidth ();
    }

    public WebButton setInnerShadeWidth ( final int innerShadeWidth )
    {
        getWebUI ().setInnerShadeWidth ( innerShadeWidth );
        return this;
    }

    public Color getInnerShadeColor ()
    {
        return getWebUI ().getInnerShadeColor ();
    }

    public WebButton setInnerShadeColor ( final Color innerShadeColor )
    {
        getWebUI ().setInnerShadeColor ( innerShadeColor );
        return this;
    }

    public int getLeftRightSpacing ()
    {
        return getWebUI ().getLeftRightSpacing ();
    }

    public WebButton setLeftRightSpacing ( final int leftRightSpacing )
    {
        getWebUI ().setLeftRightSpacing ( leftRightSpacing );
        return this;
    }

    public boolean isRolloverDecoratedOnly ()
    {
        return getWebUI ().isRolloverDecoratedOnly ();
    }

    public WebButton setRolloverDecoratedOnly ( final boolean rolloverDecoratedOnly )
    {
        getWebUI ().setRolloverDecoratedOnly ( rolloverDecoratedOnly );
        return this;
    }

    public boolean isAnimate ()
    {
        return getWebUI ().isAnimate ();
    }

    public WebButton setAnimate ( final boolean animate )
    {
        getWebUI ().setAnimate ( animate );
        return this;
    }

    public boolean isUndecorated ()
    {
        return getWebUI ().isUndecorated ();
    }

    public WebButton setUndecorated ( final boolean undecorated )
    {
        getWebUI ().setUndecorated ( undecorated );
        return this;
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public WebButton setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    public boolean isMoveIconOnPress ()
    {
        return getWebUI ().isMoveIconOnPress ();
    }

    public WebButton setMoveIconOnPress ( final boolean moveIconOnPress )
    {
        getWebUI ().setMoveIconOnPress ( moveIconOnPress );
        return this;
    }

    public boolean isDrawFocus ()
    {
        return getWebUI ().isDrawFocus ();
    }

    public WebButton setDrawFocus ( final boolean drawFocus )
    {
        getWebUI ().setDrawFocus ( drawFocus );
        return this;
    }

    public boolean isDrawBottom ()
    {
        return getWebUI ().isDrawBottom ();
    }

    public WebButton setDrawBottom ( final boolean drawBottom )
    {
        getWebUI ().setDrawBottom ( drawBottom );
        return this;
    }

    public boolean isDrawLeft ()
    {
        return getWebUI ().isDrawLeft ();
    }

    public WebButton setDrawLeft ( final boolean drawLeft )
    {
        getWebUI ().setDrawLeft ( drawLeft );
        return this;
    }

    public boolean isDrawRight ()
    {
        return getWebUI ().isDrawRight ();
    }

    public WebButton setDrawRight ( final boolean drawRight )
    {
        getWebUI ().setDrawRight ( drawRight );
        return this;
    }

    public boolean isDrawTop ()
    {
        return getWebUI ().isDrawTop ();
    }

    public WebButton setDrawTop ( final boolean drawTop )
    {
        getWebUI ().setDrawTop ( drawTop );
        return this;
    }

    public WebButton setDrawSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        getWebUI ().setDrawSides ( top, left, bottom, right );
        return this;
    }

    public boolean isDrawTopLine ()
    {
        return getWebUI ().isDrawTopLine ();
    }

    public WebButton setDrawTopLine ( final boolean drawTopLine )
    {
        getWebUI ().setDrawTopLine ( drawTopLine );
        return this;
    }

    public boolean isDrawLeftLine ()
    {
        return getWebUI ().isDrawLeftLine ();
    }

    public WebButton setDrawLeftLine ( final boolean drawLeftLine )
    {
        getWebUI ().setDrawLeftLine ( drawLeftLine );
        return this;
    }

    public boolean isDrawBottomLine ()
    {
        return getWebUI ().isDrawBottomLine ();
    }

    public WebButton setDrawBottomLine ( final boolean drawBottomLine )
    {
        getWebUI ().setDrawBottomLine ( drawBottomLine );
        return this;
    }

    public boolean isDrawRightLine ()
    {
        return getWebUI ().isDrawRightLine ();
    }

    public WebButton setDrawRightLine ( final boolean drawRightLine )
    {
        getWebUI ().setDrawRightLine ( drawRightLine );
        return this;
    }

    public WebButton setDrawLines ( final boolean top, final boolean left, final boolean bottom, final boolean right )
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

    public WebButton setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebButton setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebButtonUI getWebUI ()
    {
        return ( WebButtonUI ) getUI ();
    }

    @Override
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
     * Font methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public WebButton setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebButton setPlainFont ( final boolean apply )
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
    public WebButton setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebButton setBoldFont ( final boolean apply )
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
    public WebButton setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebButton setItalicFont ( final boolean apply )
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
    public WebButton setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebButton setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebButton setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebButton changeFontSize ( final int change )
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
    public WebButton setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebButton setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebButton setFontName ( final String fontName )
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
    public WebButton setPreferredWidth ( final int preferredWidth )
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
    public WebButton setPreferredHeight ( final int preferredHeight )
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
    public WebButton setMinimumWidth ( final int minimumWidth )
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
    public WebButton setMinimumHeight ( final int minimumHeight )
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
     * Styled button short creation methods
     */

    public static WebButton createIconWebButton ( final ImageIcon imageIcon )
    {
        return createIconWebButton ( imageIcon, StyleConstants.smallRound );
    }

    public static WebButton createIconWebButton ( final ImageIcon imageIcon, final int round )
    {
        return createIconWebButton ( imageIcon, round, StyleConstants.shadeWidth );
    }

    public static WebButton createIconWebButton ( final ImageIcon imageIcon, final int round, final int shadeWidth )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, StyleConstants.innerShadeWidth );
    }

    public static WebButton createIconWebButton ( final ImageIcon imageIcon, final int round, final int shadeWidth,
                                                  final int innerShadeWidth )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, StyleConstants.rolloverDecoratedOnly );
    }

    public static WebButton createIconWebButton ( final ImageIcon imageIcon, final boolean rolloverDecoratedOnly )
    {
        return createIconWebButton ( imageIcon, StyleConstants.smallRound, rolloverDecoratedOnly );
    }

    public static WebButton createIconWebButton ( final ImageIcon imageIcon, final int round, final boolean rolloverDecoratedOnly )
    {
        return createIconWebButton ( imageIcon, round, StyleConstants.shadeWidth, StyleConstants.innerShadeWidth, rolloverDecoratedOnly,
                StyleConstants.undecorated );
    }

    public static WebButton createIconWebButton ( final ImageIcon imageIcon, final int round, final int shadeWidth,
                                                  final int innerShadeWidth, final boolean rolloverDecoratedOnly )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, rolloverDecoratedOnly, StyleConstants.undecorated );
    }

    public static WebButton createIconWebButton ( final ImageIcon imageIcon, final int round, final int shadeWidth,
                                                  final int innerShadeWidth, final boolean rolloverDecoratedOnly,
                                                  final boolean undecorated )
    {
        return createIconWebButton ( imageIcon, round, shadeWidth, innerShadeWidth, rolloverDecoratedOnly, undecorated, true );
    }

    public static WebButton createIconWebButton ( final ImageIcon imageIcon, final int round, final int shadeWidth,
                                                  final int innerShadeWidth, final boolean rolloverDecoratedOnly, final boolean undecorated,
                                                  final boolean drawFocus )
    {
        final WebButton iconWebButton =
                createWebButton ( round, shadeWidth, innerShadeWidth, 0, rolloverDecoratedOnly, undecorated, drawFocus );
        iconWebButton.setIcon ( imageIcon );
        return iconWebButton;
    }

    public static WebButton createWebButton ( final int round, final int shadeWidth, final int innerShadeWidth, final int leftRightSpacing,
                                              final boolean rolloverDecoratedOnly, final boolean undecorated, final boolean drawFocus )
    {
        final WebButton webButton = new WebButton ();
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