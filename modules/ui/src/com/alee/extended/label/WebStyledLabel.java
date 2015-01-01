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

package com.alee.extended.label;

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.LanguageUtils;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.SupportedComponent;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.CollectionUtils;
import com.alee.utils.EventUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Custom label component that quickly renders multi-styled text.
 * Its rendering speed is superior to HTML rendering within simple JLabel and its usage is preferred.
 *
 * @author Mikle Garin
 */

public class WebStyledLabel extends JLabel implements EventMethods, ToolTipMethods, Styleable, LanguageMethods, FontMethods<WebStyledLabel>
{
    /**
     * Component properties.
     */
    public static final String PROPERTY_STYLE_RANGE = "styleRange";

    /**
     * StyleRange list.
     */
    protected List<StyleRange> styleRanges;

    /**
     * Whether should wrap
     */
    protected boolean lineWrap;
    protected int rows;
    protected int maximumRows;
    protected int minimumRows;
    protected int preferredWidth;
    protected int rowGap;

    /**
     * Constructs empty label.
     */
    public WebStyledLabel ()
    {
        super ();
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param margin label margin
     */
    public WebStyledLabel ( final Insets margin )
    {
        super ();
        setMargin ( margin );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param icon icon
     */
    public WebStyledLabel ( final Icon icon )
    {
        super ( icon );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param icon   label icon
     * @param margin label margin
     */
    public WebStyledLabel ( final Icon icon, final Insets margin )
    {
        super ( icon );
        setMargin ( margin );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param horizontalAlignment horizontal alignment
     */
    public WebStyledLabel ( final int horizontalAlignment )
    {
        super ();
        setHorizontalAlignment ( horizontalAlignment );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param horizontalAlignment horizontal alignment
     * @param margin              label margin
     */
    public WebStyledLabel ( final int horizontalAlignment, final Insets margin )
    {
        super ();
        setHorizontalAlignment ( horizontalAlignment );
        setMargin ( margin );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param icon                label icon
     * @param horizontalAlignment horizontal alignment
     */
    public WebStyledLabel ( final Icon icon, final int horizontalAlignment )
    {
        super ( icon, horizontalAlignment );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param icon                label icon
     * @param horizontalAlignment horizontal alignment
     * @param margin              label margin
     */
    public WebStyledLabel ( final Icon icon, final int horizontalAlignment, final Insets margin )
    {
        super ( icon, horizontalAlignment );
        setMargin ( margin );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text text or translation key
     * @param data language data, may not be passed
     */
    public WebStyledLabel ( final String text, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ) );
        LanguageUtils.registerInitialLanguage ( this, text, data );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text   text or translation key
     * @param margin label margin
     * @param data   language data, may not be passed
     */
    public WebStyledLabel ( final String text, final Insets margin, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ) );
        LanguageUtils.registerInitialLanguage ( this, text, data );
        setMargin ( margin );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text                text or translation key
     * @param horizontalAlignment horizontal alignment
     * @param data                language data, may not be passed
     */
    public WebStyledLabel ( final String text, final int horizontalAlignment, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ), horizontalAlignment );
        LanguageUtils.registerInitialLanguage ( this, text, data );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text                text or translation key
     * @param horizontalAlignment horizontal alignment
     * @param margin              label margin
     * @param data                language data, may not be passed
     */
    public WebStyledLabel ( final String text, final int horizontalAlignment, final Insets margin, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ), horizontalAlignment );
        LanguageUtils.registerInitialLanguage ( this, text, data );
        setMargin ( margin );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text text or translation key
     * @param icon label icon
     * @param data language data, may not be passed
     */
    public WebStyledLabel ( final String text, final Icon icon, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ), icon, LEADING );
        LanguageUtils.registerInitialLanguage ( this, text, data );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text   text or translation key
     * @param icon   label icon
     * @param margin label margin
     * @param data   language data, may not be passed
     */
    public WebStyledLabel ( final String text, final Icon icon, final Insets margin, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ), icon, LEADING );
        LanguageUtils.registerInitialLanguage ( this, text, data );
        setMargin ( margin );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text                text or translation key
     * @param icon                label icon
     * @param horizontalAlignment horizontal alignment
     * @param data                language data, may not be passed
     */
    public WebStyledLabel ( final String text, final Icon icon, final int horizontalAlignment, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ), icon, horizontalAlignment );
        LanguageUtils.registerInitialLanguage ( this, text, data );
    }

    /**
     * Constructs label with the specified preferences.
     *
     * @param text                text or translation key
     * @param icon                label icon
     * @param horizontalAlignment horizontal alignment
     * @param margin              label margin
     * @param data                language data, may not be passed
     */
    public WebStyledLabel ( final String text, final Icon icon, final int horizontalAlignment, final Insets margin, final Object... data )
    {
        super ( LanguageUtils.getInitialText ( text, data ), icon, horizontalAlignment );
        LanguageUtils.registerInitialLanguage ( this, text, data );
        setMargin ( margin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText ( final String text )
    {
        // Parse styles
        final ArrayList<StyleRange> styles = new ArrayList<StyleRange> ();
        final String plainText = StyledLabelUtils.getPlainText ( text, styles );

        // Update text
        super.setText ( plainText );

        // Set styles only if they are actually found in text
        if ( styles.size () > 0 )
        {
            setStyleRanges ( styles );
        }
    }

    /**
     * Returns label margin.
     *
     * @return label margin
     */
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets label margin.
     *
     * @param margin new label margin
     * @return this label
     */
    public WebStyledLabel setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
        return this;
    }

    /**
     * Sets label margin.
     *
     * @param top    top label margin
     * @param left   left label margin
     * @param bottom bottom label margin
     * @param right  right label margin
     * @return this label
     */
    public WebStyledLabel setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    /**
     * Sets label margin.
     *
     * @param spacing label margin
     * @return this label
     */
    public WebStyledLabel setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    /**
     * Returns label painter.
     *
     * @return label painter
     */
    public Painter getPainter ()
    {
        return StyleManager.getPainter ( this );
    }

    /**
     * Sets label painter.
     * Pass null to remove label painter.
     *
     * @param painter new label painter
     * @return this label
     */
    public WebStyledLabel setPainter ( final Painter painter )
    {
        StyleManager.setCustomPainter ( this, painter );
        return this;
    }

    /**
     * Returns added style ranges.
     *
     * @return added style ranges
     */
    public List<StyleRange> getStyleRanges ()
    {
        return CollectionUtils.copy ( getStyleRangesImpl () );
    }

    /**
     * Adds style range into this label.
     *
     * @param styleRange new style range
     */
    public void addStyleRange ( final StyleRange styleRange )
    {
        final StyleRange removed = addStyleRangeImpl ( styleRange );
        firePropertyChange ( PROPERTY_STYLE_RANGE, removed, styleRange );
    }

    /**
     * Adds style ranges into this label.
     *
     * @param styleRanges new style ranges list
     */
    public void addStyleRanges ( final List<StyleRange> styleRanges )
    {
        addStyleRangesImpl ( styleRanges );
        firePropertyChange ( PROPERTY_STYLE_RANGE, null, styleRanges );
    }

    /**
     * Removes style range from this label.
     *
     * @param styleRange style range to remove
     */
    public void removeStyleRange ( final StyleRange styleRange )
    {
        removeStyleRangeImpl ( styleRange );
        firePropertyChange ( PROPERTY_STYLE_RANGE, styleRange, null );
    }

    /**
     * Removes style ranges from this label.
     *
     * @param styleRanges style ranges to remove
     */
    public void removeStyleRanges ( final List<StyleRange> styleRanges )
    {
        removeStyleRangesImpl ( styleRanges );
        firePropertyChange ( PROPERTY_STYLE_RANGE, styleRanges, null );
    }

    /**
     * Clears all style ranges and adds new ones.
     *
     * @param styleRanges new style ranges
     */
    public void setStyleRanges ( final List<StyleRange> styleRanges )
    {
        clearStyleRangesImpl ();
        addStyleRangesImpl ( styleRanges );
        firePropertyChange ( PROPERTY_STYLE_RANGE, null, styleRanges );
    }

    /**
     * Clears all style ranges.
     */
    public void clearStyleRanges ()
    {
        clearStyleRangesImpl ();
        firePropertyChange ( PROPERTY_STYLE_RANGE, null, null );
    }

    /**
     * Returns added style ranges.
     *
     * @return added style ranges
     */
    protected List<StyleRange> getStyleRangesImpl ()
    {
        if ( styleRanges == null )
        {
            styleRanges = new ArrayList<StyleRange> ( 3 );
        }
        return styleRanges;
    }

    /**
     * Adds style range into this label.
     *
     * @param styleRange new style range
     * @return removed style range
     */
    protected StyleRange addStyleRangeImpl ( final StyleRange styleRange )
    {
        final StyleRange removed = clearSimilarRangeImpl ( styleRange.getStartIndex (), styleRange.getLength () );
        getStyleRangesImpl ().add ( styleRange );
        return removed;
    }

    /**
     * Adds style ranges into this label.
     *
     * @param styleRanges new style ranges list
     */
    protected void addStyleRangesImpl ( final List<StyleRange> styleRanges )
    {
        if ( styleRanges != null )
        {
            for ( final StyleRange styleRange : styleRanges )
            {
                addStyleRangeImpl ( styleRange );
            }
        }
    }

    /**
     * Removes style range from this label.
     *
     * @param styleRange style range to remove
     */
    protected void removeStyleRangeImpl ( final StyleRange styleRange )
    {
        if ( !getStyleRangesImpl ().remove ( styleRange ) )
        {
            final Iterator<StyleRange> iterator = getStyleRangesImpl ().iterator ();
            while ( iterator.hasNext () )
            {
                final StyleRange range = iterator.next ();
                if ( range.getStartIndex () == styleRange.getStartIndex () && range.getLength () == styleRange.getLength () )
                {
                    iterator.remove ();
                    return;
                }
            }
        }
    }

    /**
     * Removes style ranges from this label.
     *
     * @param styleRanges style ranges to remove
     */
    protected void removeStyleRangesImpl ( final List<StyleRange> styleRanges )
    {
        if ( styleRanges != null )
        {
            for ( final StyleRange styleRange : styleRanges )
            {
                removeStyleRangeImpl ( styleRange );
            }
        }
    }

    /**
     * Clears all style ranges.
     */
    protected void clearStyleRangesImpl ()
    {
        getStyleRangesImpl ().clear ();
    }

    /**
     * Removes any style range found in the same range as the specified one.
     *
     * @param start  range start
     * @param length range length
     * @return removed style range
     */
    protected StyleRange clearSimilarRangeImpl ( final int start, final int length )
    {
        final Iterator<StyleRange> iterator = getStyleRangesImpl ().iterator ();
        while ( iterator.hasNext () )
        {
            final StyleRange range = iterator.next ();
            if ( range.getStartIndex () == start && range.getLength () == length )
            {
                iterator.remove ();
                return range;
            }
        }
        return null;
    }

    /**
     * Returns whether text lines should be wrapped or not.
     *
     * @return true if text lines should be wrapped, false otherwise
     */
    public boolean isLineWrap ()
    {
        return lineWrap;
    }

    /**
     * Sets whether text lines should be wrapped or not.
     *
     * @param wrap whether text lines should be wrapped or not
     */
    public void setLineWrap ( final boolean wrap )
    {
        this.lineWrap = wrap;
    }

    /**
     * Returns row count used to wrap label text.
     *
     * @return row count used to wrap label text
     */
    public int getRows ()
    {
        return rows;
    }

    /**
     * Sets row count used to wrap label text.
     * By default it is set to zero.
     * <p/>
     * Note that it has lower priority than preferred width.
     * If preferred width is set this value is ignored.
     *
     * @param rows the row count
     */
    public void setRows ( final int rows )
    {
        this.rows = rows;
    }

    /**
     * Returns gap between text rows in pixels.
     *
     * @return gap between text rows in pixels
     */
    public int getRowGap ()
    {
        return rowGap;
    }

    /**
     * Sets gap between text rows in pixels.
     *
     * @param gap gap between text rows in pixels
     */
    public void setRowGap ( final int gap )
    {
        this.rowGap = gap;
    }

    /**
     * Returns maximum rows amount visible after wrapping.
     *
     * @return maximum rows amount visible after wrapping
     */
    public int getMaximumRows ()
    {
        return maximumRows;
    }

    /**
     * Sets maximum rows amount visible after wrapping.
     * By default it is set to zero.
     *
     * @param maximumRows maximum rows amount visible after wrapping
     */
    public void setMaximumRows ( final int maximumRows )
    {
        this.maximumRows = maximumRows;
    }

    /**
     * Returns minimum rows amount visible after wrapping.
     *
     * @return minimum rows amount visible after wrapping
     */
    public int getMinimumRows ()
    {
        return minimumRows;
    }

    /**
     * Sets minimum rows amount visible after wrapping.
     * By default it is set to zero.
     *
     * @param minimumRows minimum rows amount visible after wrapping
     */
    public void setMinimumRows ( final int minimumRows )
    {
        this.minimumRows = minimumRows;
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
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebStyledLabelUI getWebUI ()
    {
        return ( WebStyledLabelUI ) getUI ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebStyledLabelUI ) )
        {
            try
            {
                setUI ( ( WebStyledLabelUI ) ReflectUtils.createInstance ( WebLookAndFeel.styledLabelUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebStyledLabelUI () );
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
    public String getUIClassID ()
    {
        return SupportedComponent.styledLabel.getUIClassID ();
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
    public Dimension getMinimumSize ()
    {
        return isLineWrap () ? new Dimension ( 1, 1 ) : super.getMinimumSize ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getMaximumSize ()
    {
        return isLineWrap () ? new Dimension ( Integer.MAX_VALUE, Integer.MAX_VALUE ) : super.getMaximumSize ();
    }

    /**
     * Sets preferred width of the label.
     *
     * @param width new preferred width of the label
     */
    public void setPreferredWidth ( final int width )
    {
        this.preferredWidth = width;
    }

    /**
     * Gets the preferred width of the styled label.
     *
     * @return the preferred width
     */
    public int getPreferredWidth ()
    {
        return preferredWidth;
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
    public WebStyledLabel setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStyledLabel setPlainFont ( final boolean apply )
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
    public WebStyledLabel setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStyledLabel setBoldFont ( final boolean apply )
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
    public WebStyledLabel setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStyledLabel setItalicFont ( final boolean apply )
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
    public WebStyledLabel setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStyledLabel setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStyledLabel setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStyledLabel changeFontSize ( final int change )
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
    public WebStyledLabel setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStyledLabel setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebStyledLabel setFontName ( final String fontName )
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
}