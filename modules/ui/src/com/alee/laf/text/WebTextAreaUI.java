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

package com.alee.laf.text;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LM;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * User: mgarin Date: 17.08.11 Time: 23:01
 */

public class WebTextAreaUI extends BasicTextAreaUI implements BorderMethods, SwingConstants
{
    private String inputPrompt = WebTextAreaStyle.inputPrompt;
    private Font inputPromptFont = WebTextAreaStyle.inputPromptFont;
    private Color inputPromptForeground = WebTextAreaStyle.inputPromptForeground;
    private int inputPromptHorizontalPosition = WebTextAreaStyle.inputPromptHorizontalPosition;
    private int inputPromptVerticalPosition = WebTextAreaStyle.inputPromptVerticalPosition;
    private boolean hideInputPromptOnFocus = WebTextAreaStyle.hideInputPromptOnFocus;
    private Painter painter = WebTextAreaStyle.painter;

    private boolean inputPromptSet = false;

    private FocusListener focusListener;
    private PropertyChangeListener marginChangeListener;

    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTextAreaUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        final JTextComponent textComponent = getComponent ();

        // Default settings
        SwingUtils.setOrientation ( textComponent );
        LookAndFeel.installProperty ( textComponent, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.TRUE );
        textComponent.setBackground ( WebTextAreaStyle.backgroundColor );
        textComponent.setSelectionColor ( StyleConstants.textSelectionColor );
        textComponent.setForeground ( WebTextAreaStyle.foregroundColor );
        textComponent.setSelectedTextColor ( WebTextAreaStyle.selectedTextColor );
        textComponent.setCaretColor ( WebTextAreaStyle.caretColor );
        textComponent.setMargin ( WebTextAreaStyle.margin );
        PainterSupport.installPainter ( textComponent, this.painter );

        // Updating border
        updateBorder ();

        focusListener = new FocusListener ()
        {
            @Override
            public void focusLost ( final FocusEvent e )
            {
                textComponent.repaint ();
            }

            @Override
            public void focusGained ( final FocusEvent e )
            {
                textComponent.repaint ();
            }
        };
        textComponent.addFocusListener ( focusListener );

        marginChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        textComponent.addPropertyChangeListener ( WebLookAndFeel.MARGIN_PROPERTY, marginChangeListener );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        final JTextComponent component = getComponent ();

        PainterSupport.uninstallPainter ( component, this.painter );

        component.removeFocusListener ( focusListener );
        component.removePropertyChangeListener ( WebLookAndFeel.MARGIN_PROPERTY, marginChangeListener );

        super.uninstallUI ( c );
    }

    public String getInputPrompt ()
    {
        return inputPrompt;
    }

    public void setInputPrompt ( final String inputPrompt )
    {
        this.inputPrompt = inputPrompt;
        this.inputPromptSet = inputPrompt != null && !inputPrompt.trim ().equals ( "" );
        updateInputPromptView ();
    }

    public Font getInputPromptFont ()
    {
        return inputPromptFont;
    }

    public void setInputPromptFont ( final Font inputPromptFont )
    {
        this.inputPromptFont = inputPromptFont;
        updateInputPromptView ();
    }

    public Color getInputPromptForeground ()
    {
        return inputPromptForeground;
    }

    public void setInputPromptForeground ( final Color inputPromptForeground )
    {
        this.inputPromptForeground = inputPromptForeground;
        updateInputPromptView ();
    }

    public int getInputPromptHorizontalPosition ()
    {
        return inputPromptHorizontalPosition;
    }

    public void setInputPromptHorizontalPosition ( final int inputPromptHorizontalPosition )
    {
        this.inputPromptHorizontalPosition = inputPromptHorizontalPosition;
        updateInputPromptView ();
    }

    public int getInputPromptVerticalPosition ()
    {
        return inputPromptVerticalPosition;
    }

    public void setInputPromptVerticalPosition ( final int inputPromptVerticalPosition )
    {
        this.inputPromptVerticalPosition = inputPromptVerticalPosition;
        updateInputPromptView ();
    }

    public boolean isHideInputPromptOnFocus ()
    {
        return hideInputPromptOnFocus;
    }

    public void setHideInputPromptOnFocus ( final boolean hideInputPromptOnFocus )
    {
        this.hideInputPromptOnFocus = hideInputPromptOnFocus;
        updateInputPromptView ();
    }

    public Painter getPainter ()
    {
        return painter;
    }

    public void setPainter ( final Painter painter )
    {
        final JTextComponent textComponent = getComponent ();
        PainterSupport.uninstallPainter ( textComponent, this.painter );

        this.painter = painter;
        textComponent.setOpaque ( painter == null || painter.isOpaque ( textComponent ) );
        PainterSupport.installPainter ( textComponent, this.painter );
        updateBorder ();
    }

    private void updateInputPromptView ()
    {
        if ( isInputPromptVisible ( getComponent () ) )
        {
            updateView ();
        }
    }

    private boolean isInputPromptVisible ( final JTextComponent c )
    {
        return inputPromptSet && c.isEditable () && c.isEnabled () && ( !hideInputPromptOnFocus || !c.isFocusOwner () ) &&
                c.getText ().equals ( "" );
    }

    private void updateView ()
    {
        if ( getComponent () != null )
        {
            getComponent ().repaint ();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        final JTextComponent component = getComponent ();
        if ( component != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( component ) )
            {
                return;
            }

            // Actual margin
            final Insets margin = component.getMargin ();
            final boolean ltr = component.getComponentOrientation ().isLeftToRight ();
            final Insets m = margin == null ? new Insets ( 0, 0, 0, 0 ) :
                    new Insets ( margin.top, ( ltr ? margin.left : margin.right ), margin.bottom, ( ltr ? margin.right : margin.left ) );

            // Applying border
            if ( painter != null )
            {
                // Painter borders
                final Insets pi = painter.getMargin ( component );
                m.top += pi.top;
                m.bottom += pi.bottom;
                m.left += ltr ? pi.left : pi.right;
                m.right += ltr ? pi.right : pi.left;
            }

            // Installing border
            component.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    @Override
    protected void paintBackground ( final Graphics g )
    {
        //
    }

    @Override
    protected void paintSafely ( final Graphics g )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        final JTextComponent c = getComponent ();

        if ( c.isOpaque () && ( painter == null || !painter.isOpaque ( c ) ) )
        {
            // Paint default background
            g2d.setPaint ( c.getBackground () );
            g2d.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
        }

        if ( painter != null )
        {
            // Use background painter instead of default UI graphics
            painter.paint ( g2d, SwingUtils.size ( c ), c );
        }

        final Map hints = SwingUtils.setupTextAntialias ( g2d );
        super.paintSafely ( g );
        if ( isInputPromptVisible ( c ) )
        {
            final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
            final Rectangle b = getVisibleEditorRect ();
            final Shape oc = GraphicsUtils.intersectClip ( g2d, b );
            g2d.setFont ( inputPromptFont != null ? inputPromptFont : c.getFont () );
            g2d.setPaint ( inputPromptForeground != null ? inputPromptForeground : c.getForeground () );

            final String text = LM.get ( inputPrompt );
            final FontMetrics fm = g2d.getFontMetrics ();
            final int x;
            if ( inputPromptHorizontalPosition == CENTER )
            {
                x = b.x + b.width / 2 - fm.stringWidth ( text ) / 2;
            }
            else if ( ltr && inputPromptHorizontalPosition == LEADING || !ltr && inputPromptHorizontalPosition == TRAILING ||
                    inputPromptHorizontalPosition == LEFT )
            {
                x = b.x;
            }
            else
            {
                x = b.x + b.width - fm.stringWidth ( text );
            }
            final int y;
            if ( inputPromptVerticalPosition == CENTER )
            {
                y = b.y + b.height / 2 + LafUtils.getTextCenterShearY ( fm );
            }
            else
            {
                y = getBaseline ( c, c.getWidth (), c.getHeight () );
            }
            g2d.drawString ( text, x, y );

            GraphicsUtils.restoreClip ( g2d, oc );
        }
        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        Dimension ps = super.getPreferredSize ( c );
        if ( painter != null )
        {
            if ( c.getLayout () != null )
            {
                ps = SwingUtils.max ( ps, c.getLayout ().preferredLayoutSize ( c ) );
            }
            ps = SwingUtils.max ( ps, painter.getPreferredSize ( c ) );
        }
        return ps;
    }
}