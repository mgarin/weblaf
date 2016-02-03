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

package com.alee.managers.style.skin.web;

import com.alee.laf.text.IAbstractTextEditorPainter;
import com.alee.managers.language.LM;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;
import com.alee.utils.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Map;

/**
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public abstract class AbstractTextEditorPainter<E extends JTextComponent, U extends BasicTextUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IAbstractTextEditorPainter<E, U>, SwingConstants
{
    /**
     * Style settings.
     */
    protected int inputPromptHorizontalPosition = SwingConstants.CENTER;
    protected int inputPromptVerticalPosition = SwingConstants.CENTER;
    protected Font inputPromptFont = null;
    protected Color inputPromptForeground = new Color ( 160, 160, 160 );
    protected boolean inputPromptOnlyWhenEditable = true;
    protected boolean hideInputPromptOnFocus = true;

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Painting decoration
        super.paint ( g2d, bounds, c, ui );

        // Painting editor
        paintEditor ( g2d );
    }

    /**
     * Paints text editor area.
     *
     * @param g2d graphics context
     */
    protected void paintEditor ( final Graphics2D g2d )
    {
        // Paints text highligher
        final Highlighter highlighter = component.getHighlighter ();
        if ( highlighter != null )
        {
            highlighter.paint ( g2d );
        }

        final Map hints = SwingUtils.setupTextAntialias ( g2d );

        // Paints input prompt
        paintInputPrompt ( g2d );

        // Paints editor view
        final Rectangle alloc = getEditorRect ();
        if ( alloc != null )
        {
            ui.getRootView ( component ).paint ( g2d, alloc );
        }

        SwingUtils.restoreTextAntialias ( g2d, hints );

        // Paints caret
        final Caret caret = component.getCaret ();
        if ( caret != null )
        {
            caret.paint ( g2d );
        }

        // Paints drop caret
        final DefaultCaret dropCaret = ReflectUtils.getFieldValueSafely ( ui, "dropCaret" );
        if ( dropCaret != null )
        {
            dropCaret.paint ( g2d );
        }
    }

    /**
     * Draws input prompt text if it is available and should be visible at the moment.
     *
     * @param g2d graphics context
     */
    protected void paintInputPrompt ( final Graphics2D g2d )
    {
        if ( isInputPromptVisible () )
        {
            final Rectangle b = getEditorRect ();
            final Shape oc = GraphicsUtils.intersectClip ( g2d, b );

            g2d.setFont ( inputPromptFont != null ? inputPromptFont : component.getFont () );
            g2d.setPaint ( inputPromptForeground != null ? inputPromptForeground : component.getForeground () );

            final String text = LM.get ( getInputPrompt () );
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
                y = b.y + b.height / 2 + LafUtils.getTextCenterShiftY ( fm );
            }
            else
            {
                y = ui.getBaseline ( component, component.getWidth (), component.getHeight () );
            }
            g2d.drawString ( text, x, y );

            GraphicsUtils.restoreClip ( g2d, oc );
        }
    }

    /**
     * Returns the bounding box for the root view.
     * The component must have a non-zero positive size for this translation to be computed.
     *
     * @return the bounding box for the root view
     */
    protected Rectangle getEditorRect ()
    {
        final Rectangle alloc = component.getBounds ();
        if ( alloc.width > 0 && alloc.height > 0 )
        {
            final Insets insets = component.getInsets ();
            alloc.x = insets.left;
            alloc.y = insets.top;
            alloc.width -= insets.left + insets.right;
            alloc.height -= insets.top + insets.bottom;
            return alloc;
        }
        return null;
    }

    @Override
    public boolean isInputPromptVisible ()
    {
        final String inputPrompt = LM.get ( getInputPrompt () );
        return inputPrompt != null && !inputPrompt.isEmpty () && TextUtils.isEmpty ( component.getText () ) &&
                ( !inputPromptOnlyWhenEditable || component.isEditable () && component.isEnabled () ) &&
                ( !hideInputPromptOnFocus || !isFocused () );
    }
}