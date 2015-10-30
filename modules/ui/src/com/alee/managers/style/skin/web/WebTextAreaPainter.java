package com.alee.managers.style.skin.web;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.text.TextAreaPainter;
import com.alee.laf.text.WebTextAreaStyle;
import com.alee.laf.text.WebTextAreaUI;
import com.alee.managers.language.LM;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * @author Alexandr Zernov
 */

public class WebTextAreaPainter<E extends JTextArea, U extends WebTextAreaUI> extends WebBasicTextAreaPainter<E, U>
        implements TextAreaPainter<E, U>
{
    /**
     * Style settings.
     */
    protected int inputPromptHorizontalPosition = WebTextAreaStyle.inputPromptHorizontalPosition;
    protected int inputPromptVerticalPosition = WebTextAreaStyle.inputPromptVerticalPosition;
    protected Font inputPromptFont = WebTextAreaStyle.inputPromptFont;
    protected Color inputPromptForeground = WebTextAreaStyle.inputPromptForeground;
    protected boolean hideInputPromptOnFocus = WebTextAreaStyle.hideInputPromptOnFocus;

    /**
     * Listeners.
     */
    protected FocusListener focusListener;
    protected PropertyChangeListener marginChangeListener;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Installing listeners
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
        focusListener = null;
        component.removePropertyChangeListener ( WebLookAndFeel.MARGIN_PROPERTY, marginChangeListener );
        marginChangeListener = null;

        super.uninstall ( c, ui );
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

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Map hints = SwingUtils.setupTextAntialias ( g2d );

        // Painting text area
        super.paint ( g2d, bounds, c, ui );

        if ( isInputPromptVisible () )
        {
            final Rectangle b = getVisibleEditorRect ();
            final Shape oc = GraphicsUtils.intersectClip ( g2d, b );
            g2d.setFont ( inputPromptFont != null ? inputPromptFont : component.getFont () );
            g2d.setPaint ( inputPromptForeground != null ? inputPromptForeground : component.getForeground () );

            final String text = LM.get ( ui.getInputPrompt () );
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
                y = ui.getBaseline ( component, component.getWidth (), component.getHeight () );
            }
            g2d.drawString ( text, x, y );

            GraphicsUtils.restoreClip ( g2d, oc );
        }
        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    /**
     * Returns whether input prompt visible or not.
     *
     * @return whether input prompt visible or not
     */
    protected boolean isInputPromptVisible ()
    {
        final String inputPrompt = ui.getInputPrompt ();
        return inputPrompt != null && !inputPrompt.isEmpty () && component.isEditable () && component.isEnabled () &&
                ( !hideInputPromptOnFocus || !component.isFocusOwner () ) && component.getText ().isEmpty ();
    }

    protected void updateInputPromptView ()
    {
        if ( isInputPromptVisible () )
        {
            component.repaint ();
        }
    }
}