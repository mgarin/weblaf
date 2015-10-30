package com.alee.managers.style.skin.web;

import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.text.AbstractTextAreaPainter;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;

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
 */

public class WebBasicTextAreaPainter<E extends JTextComponent, U extends BasicTextUI> extends AbstractPainter<E, U>
        implements AbstractTextAreaPainter<E, U>, SwingConstants
{
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Map hints = SwingUtils.setupTextAntialias ( g2d );

        final Highlighter highlighter = component.getHighlighter ();
        final Caret caret = component.getCaret ();

        // paint the background
        if ( component.isOpaque () )
        {
            g2d.setColor ( component.getBackground () );
            g2d.fill ( bounds );
        }

        // paint the highlights
        if ( highlighter != null )
        {
            highlighter.paint ( g2d );
        }

        // paint the view hierarchy
        final Rectangle alloc = getVisibleEditorRect ();
        if ( alloc != null )
        {
            ui.getRootView ( component ).paint ( g2d, alloc );
        }

        // paint the caret
        if ( caret != null )
        {
            caret.paint ( g2d );
        }

        final DefaultCaret dropCaret = ReflectUtils.getFieldValueSafely ( ui, "dropCaret" );
        if ( dropCaret != null )
        {
            dropCaret.paint ( g2d );
        }

        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    /**
     * Gets the allocation to give the root View.  Due to an unfortunate set of historical events this method is inappropriately named.
     * The Rectangle returned has nothing to do with visibility. The component must have a non-zero positive size for this translation
     * to be computed.
     *
     * @return the bounding box for the root view
     */
    protected Rectangle getVisibleEditorRect ()
    {
        final Rectangle alloc = component.getBounds ();
        if ( ( alloc.width > 0 ) && ( alloc.height > 0 ) )
        {
            alloc.x = alloc.y = 0;
            final Insets insets = component.getInsets ();
            alloc.x += insets.left;
            alloc.y += insets.top;
            alloc.width -= insets.left + insets.right;
            alloc.height -= insets.top + insets.bottom;
            return alloc;
        }
        return null;
    }
}