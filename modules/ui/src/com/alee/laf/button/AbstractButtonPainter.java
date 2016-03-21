package com.alee.laf.button;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.IAbstractButtonPainter;
import com.alee.managers.style.Bounds;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public abstract class AbstractButtonPainter<E extends AbstractButton, U extends BasicButtonUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IAbstractButtonPainter<E, U>
{
    /**
     * Listeners.
     */
    protected ChangeListener modelChangeListener;

    /**
     * Style settings.
     */
    protected Color selectedForeground;

    /**
     * Painting variables.
     */
    protected Rectangle viewRect = new Rectangle ();
    protected Rectangle textRect = new Rectangle ();
    protected Rectangle iconRect = new Rectangle ();

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Model change listener to support state changes
        modelChangeListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                updateDecorationState ();
            }
        };
        component.getModel ().addChangeListener ( modelChangeListener );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.getModel ().removeChangeListener ( modelChangeListener );
        modelChangeListener = null;

        super.uninstall ( c, ui );
    }

    @Override
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChange ( property, oldValue, newValue );

        // Switching model change listener to new model
        if ( CompareUtils.equals ( property, WebLookAndFeel.MODEL_PROPERTY ) )
        {
            ( ( ButtonModel ) oldValue ).removeChangeListener ( modelChangeListener );
            ( ( ButtonModel ) newValue ).addChangeListener ( modelChangeListener );
        }
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        final ButtonModel model = component.getModel ();
        if ( model.isPressed () )
        {
            states.add ( DecorationState.pressed );
        }
        if ( model.isSelected () )
        {
            states.add ( DecorationState.selected );
        }
        return states;
    }

    @Override
    protected Boolean isOpaqueUndecorated ()
    {
        return false;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Calculating bounds we will need late
        final FontMetrics fm = c.getFontMetrics ( c.getFont () );
        calculateBounds ( fm );

        // Painting icon
        paintIcon ( g2d );

        // Painting text
        paintText ( g2d );
    }

    /**
     * Calculates view, icon and text bounds for future usage.
     *
     * @param fm font metrics
     */
    protected void calculateBounds ( final FontMetrics fm )
    {
        viewRect = Bounds.padding.of ( component );

        textRect.x = textRect.y = textRect.width = textRect.height = 0;
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

        // Layout the text and icon
        SwingUtilities.layoutCompoundLabel ( component, fm, component.getText (), component.getIcon (), component.getVerticalAlignment (),
                component.getHorizontalAlignment (), component.getVerticalTextPosition (), component.getHorizontalTextPosition (), viewRect,
                iconRect, textRect, component.getText () == null ? 0 : component.getIconTextGap () );
    }

    /**
     * Paints button icon.
     *
     * @param g2d graphics context
     */
    protected void paintIcon ( final Graphics2D g2d )
    {
        if ( component.getIcon () != null )
        {
            Icon icon = component.getIcon ();
            Icon tmpIcon = null;

            if ( icon == null )
            {
                return;
            }

            Icon selectedIcon = null;

            /* the fallback icon should be based on the selected state */
            final ButtonModel model = component.getModel ();
            if ( model.isSelected () )
            {
                selectedIcon = component.getSelectedIcon ();
                if ( selectedIcon != null )
                {
                    icon = selectedIcon;
                }
            }

            if ( !model.isEnabled () )
            {
                if ( model.isSelected () )
                {
                    tmpIcon = component.getDisabledSelectedIcon ();
                    if ( tmpIcon == null )
                    {
                        tmpIcon = selectedIcon;
                    }
                }

                if ( tmpIcon == null )
                {
                    tmpIcon = component.getDisabledIcon ();
                }
            }
            else if ( model.isPressed () && model.isArmed () )
            {
                tmpIcon = component.getPressedIcon ();
            }
            else if ( component.isRolloverEnabled () && model.isRollover () )
            {
                if ( model.isSelected () )
                {
                    tmpIcon = component.getRolloverSelectedIcon ();
                    if ( tmpIcon == null )
                    {
                        tmpIcon = selectedIcon;
                    }
                }

                if ( tmpIcon == null )
                {
                    tmpIcon = component.getRolloverIcon ();
                }
            }

            if ( tmpIcon != null )
            {
                icon = tmpIcon;
            }

            if ( model.isPressed () && model.isArmed () )
            {
                icon.paintIcon ( component, g2d, iconRect.x, iconRect.y );
            }
            else
            {
                icon.paintIcon ( component, g2d, iconRect.x, iconRect.y );
            }
        }
    }

    /**
     * Paints button text.
     *
     * @param g2d graphics context
     */
    protected void paintText ( final Graphics2D g2d )
    {
        final String text = component.getText ();
        if ( text != null && !text.equals ( "" ) )
        {
            final Map map = SwingUtils.setupTextAntialias ( g2d );
            final View v = ( View ) component.getClientProperty ( BasicHTML.propertyKey );
            if ( v != null )
            {
                v.paint ( g2d, textRect );
            }
            else
            {
                final FontMetrics fm = SwingUtils.getFontMetrics ( component, g2d );
                final int mnemonicIndex = component.getDisplayedMnemonicIndex ();

                // Drawing text
                final ButtonModel model = component.getModel ();
                if ( model.isEnabled () )
                {
                    // Drawing normal text
                    g2d.setPaint ( model.isPressed () || model.isSelected () ? selectedForeground : component.getForeground () );
                    SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemonicIndex, textRect.x, textRect.y + fm.getAscent () );
                }
                else
                {
                    // todo Paint single-colored text
                    // Drawing disabled text
                    g2d.setPaint ( component.getBackground ().brighter () );
                    SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemonicIndex, textRect.x + 1, textRect.y + fm.getAscent () + 1 );
                    g2d.setPaint ( component.getBackground ().darker () );
                    SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemonicIndex, textRect.x, textRect.y + fm.getAscent () );
                }
            }
            SwingUtils.restoreTextAntialias ( g2d, map );
        }
    }
}