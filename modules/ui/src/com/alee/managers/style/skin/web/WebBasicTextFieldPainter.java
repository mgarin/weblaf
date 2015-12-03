package com.alee.managers.style.skin.web;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.text.AbstractTextFieldPainter;
import com.alee.laf.text.WebTextFieldStyle;
import com.alee.managers.language.LM;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * @author Alexandr Zernov
 */

public class WebBasicTextFieldPainter<E extends JTextComponent, U extends BasicTextUI> extends AbstractDecorationPainter<E, U>
        implements AbstractTextFieldPainter<E, U>, SwingConstants
{
    /**
     * Style settings.
     */
    protected int inputPromptPosition = WebTextFieldStyle.inputPromptPosition;
    protected Font inputPromptFont = WebTextFieldStyle.inputPromptFont;
    protected Color inputPromptForeground = WebTextFieldStyle.inputPromptForeground;
    protected boolean hideInputPromptOnFocus = WebTextFieldStyle.hideInputPromptOnFocus;

    /**
     * Listeners.
     */
    protected FocusListener focusListener;
    protected PropertyChangeListener accessibleChangeListener;
    protected PropertyChangeListener orientationChangeListener;
    protected PropertyChangeListener marginChangeListener;

    /**
     * Painting variables
     */
    protected View rootView = null;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        focusListener = new FocusListener ()
        {
            @Override
            public void focusLost ( final FocusEvent e )
            {
                component.repaint ();
            }

            @Override
            public void focusGained ( final FocusEvent e )
            {
                component.repaint ();
            }
        };
        component.addFocusListener ( focusListener );

        accessibleChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateInnerComponents ();
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, accessibleChangeListener );

        orientationChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );

        marginChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.MARGIN_PROPERTY, marginChangeListener );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removeFocusListener ( focusListener );
        component.removePropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, accessibleChangeListener );
        component.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );
        component.removePropertyChangeListener ( WebLookAndFeel.MARGIN_PROPERTY, marginChangeListener );

        super.uninstall ( c, ui );
    }

    @Override
    public Insets getBorders ()
    {
        final Component lc = ltr ? getLeadingComponent () : getTrailingComponent ();
        final Component tc = ltr ? getTrailingComponent () : getLeadingComponent ();
        if ( lc != null || tc != null )
        {
            return i ( 0, lc != null ? lc.getPreferredSize ().width : 0, 0, tc != null ? tc.getPreferredSize ().width : 0 );
        }
        else
        {
            return null;
        }
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

    public int getInputPromptPosition ()
    {
        return inputPromptPosition;
    }

    public void setInputPromptPosition ( final int inputPromptPosition )
    {
        this.inputPromptPosition = inputPromptPosition;
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

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        rootView = ui.getRootView ( component );

        // Painting decoration
        super.paint ( g2d, bounds, c, ui );

        // Painting editor
        paintEditor ( g2d );

        rootView = null;
    }

    protected void paintEditor ( final Graphics2D g2d )
    {
        final Map hints = SwingUtils.setupTextAntialias ( g2d );

        final Highlighter highlighter = component.getHighlighter ();
        final Caret caret = component.getCaret ();

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

        if ( isInputPromptVisible () )
        {
            final Rectangle b = getVisibleEditorRect ();
            final Shape oc = GraphicsUtils.intersectClip ( g2d, b );
            g2d.setFont ( inputPromptFont != null ? inputPromptFont : component.getFont () );
            g2d.setPaint ( inputPromptForeground != null ? inputPromptForeground : component.getForeground () );

            final String text = LM.get ( getInputPrompt () );
            final FontMetrics fm = g2d.getFontMetrics ();
            final int x;
            if ( inputPromptPosition == CENTER )
            {
                x = b.x + b.width / 2 - fm.stringWidth ( text ) / 2;
            }
            else if ( ltr && inputPromptPosition == LEADING || !ltr && inputPromptPosition == TRAILING || inputPromptPosition == LEFT )
            {
                x = b.x;
            }
            else
            {
                x = b.x + b.width - fm.stringWidth ( text );
            }
            g2d.drawString ( text, x, ui.getBaseline ( component, component.getWidth (), component.getHeight () ) );

            g2d.setClip ( oc );
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

    /**
     * Returns whether input prompt visible or not.
     *
     * @return whether input prompt visible or not
     */
    protected boolean isInputPromptVisible ()
    {
        final String inputPrompt = LM.get ( getInputPrompt () );
        return inputPrompt != null && !inputPrompt.isEmpty () && component.isEditable () && component.isEnabled () &&
                ( !hideInputPromptOnFocus || !focused ) && component.getText ().isEmpty ();
    }

    /**
     * Returns input prompt text.
     *
     * @return input prompt text
     */
    protected String getInputPrompt ()
    {
        return null;
    }

    protected Component getTrailingComponent ()
    {
        return null;
    }

    protected Component getLeadingComponent ()
    {
        return null;
    }

    protected void updateInnerComponents ()
    {
    }

    protected void updateInputPromptView ()
    {
        if ( isInputPromptVisible () )
        {
            component.repaint ();
        }
    }
}