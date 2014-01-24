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
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * User: mgarin Date: 16.05.11 Time: 16:39
 */

public class WebTextFieldUI extends BasicTextFieldUI implements ShapeProvider, SwingConstants, BorderMethods
{
    private JTextField textField = null;

    private boolean drawBorder = WebTextFieldStyle.drawBorder;
    private boolean drawFocus = WebTextFieldStyle.drawFocus;
    private int round = WebTextFieldStyle.round;
    private boolean drawShade = WebTextFieldStyle.drawShade;
    private int shadeWidth = WebTextFieldStyle.shadeWidth;
    private boolean drawBackground = WebTextFieldStyle.drawBackground;
    private boolean webColored = WebTextFieldStyle.webColored;
    private Painter painter = WebTextFieldStyle.painter;
    private Insets fieldMargin = WebTextFieldStyle.fieldMargin;
    private String inputPrompt = WebTextFieldStyle.inputPrompt;
    private Font inputPromptFont = WebTextFieldStyle.inputPromptFont;
    private Color inputPromptForeground = WebTextFieldStyle.inputPromptForeground;
    private int inputPromptPosition = WebTextFieldStyle.inputPromptPosition;
    private boolean hideInputPromptOnFocus = WebTextFieldStyle.hideInputPromptOnFocus;

    private JComponent leadingComponent = null;
    private JComponent trailingComponent = null;

    private boolean inputPromptSet = false;

    private FocusListener focusListener;
    private PropertyChangeListener accessibleChangeListener;
    private PropertyChangeListener orientationChangeListener;
    private PropertyChangeListener marginChangeListener;
    private ComponentAdapter componentResizeListener;

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTextFieldUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        this.textField = ( JTextField ) c;

        // Default settings
        SwingUtils.setOrientation ( textField );
        LookAndFeel.installProperty ( textField, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        textField.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );
        textField.setFocusable ( true );
        textField.setMargin ( WebTextFieldStyle.margin );
        textField.setBackground ( Color.WHITE );
        textField.setSelectionColor ( StyleConstants.textSelectionColor );
        textField.setForeground ( Color.BLACK );
        textField.setSelectedTextColor ( Color.BLACK );
        textField.setCaretColor ( Color.GRAY );
        textField.setLayout ( new TextComponentLayout ( textField ) );
        PainterSupport.installPainter ( textField, this.painter );

        // Updating border
        updateBorder ();

        focusListener = new FocusListener ()
        {
            @Override
            public void focusLost ( final FocusEvent e )
            {
                textField.repaint ();
            }

            @Override
            public void focusGained ( final FocusEvent e )
            {
                textField.repaint ();
            }
        };
        textField.addFocusListener ( focusListener );

        accessibleChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateInnerComponents ();
            }
        };
        textField.addPropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, accessibleChangeListener );

        orientationChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        textField.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );

        marginChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        textField.addPropertyChangeListener ( WebLookAndFeel.MARGIN_PROPERTY, marginChangeListener );

        componentResizeListener = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                updateBorder ();
            }
        };
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        PainterSupport.uninstallPainter ( textField, this.painter );

        textField.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, null );

        textField.removeFocusListener ( focusListener );
        textField.removePropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, accessibleChangeListener );
        textField.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );
        textField.removePropertyChangeListener ( WebLookAndFeel.MARGIN_PROPERTY, marginChangeListener );

        cleanupLeadingComponent ();
        cleanupTrailingComponent ();
        textField.setLayout ( null );

        this.textField = null;

        super.uninstallUI ( c );
    }

    @Override
    public Shape provideShape ()
    {
        if ( drawBorder )
        {
            return LafUtils.getWebBorderShape ( textField, shadeWidth, round );
        }
        else
        {
            return SwingUtils.size ( textField );
        }
    }

    private void updateInnerComponents ()
    {
        if ( leadingComponent != null )
        {
            leadingComponent.setEnabled ( textField.isEnabled () );
        }
        if ( trailingComponent != null )
        {
            trailingComponent.setEnabled ( textField.isEnabled () );
        }
    }

    public JComponent getLeadingComponent ()
    {
        return leadingComponent;
    }

    public void setLeadingComponent ( final JComponent leadingComponent )
    {
        if ( this.leadingComponent == leadingComponent )
        {
            return;
        }

        // Removing old leading component
        cleanupLeadingComponent ();

        // New leading component
        if ( leadingComponent != null )
        {
            this.leadingComponent = leadingComponent;

            // Registering resize listener
            this.leadingComponent.addComponentListener ( componentResizeListener );

            // Adding component
            textField.add ( leadingComponent, TextComponentLayout.LEADING );

            // Updating components state
            updateInnerComponents ();
        }

        // Updating layout
        textField.revalidate ();

        // Updating border
        updateBorder ();
    }

    private void cleanupLeadingComponent ()
    {
        if ( this.leadingComponent != null )
        {
            this.leadingComponent.removeComponentListener ( componentResizeListener );
            textField.remove ( this.leadingComponent );
            this.leadingComponent = null;
        }
    }

    public JComponent getTrailingComponent ()
    {
        return trailingComponent;
    }

    public void setTrailingComponent ( final JComponent trailingComponent )
    {
        if ( this.trailingComponent == trailingComponent )
        {
            return;
        }

        // Removing old trailing component
        cleanupTrailingComponent ();

        // New trailing component
        if ( trailingComponent != null )
        {
            this.trailingComponent = trailingComponent;

            // Registering resize listener
            this.trailingComponent.addComponentListener ( componentResizeListener );

            // Adding component
            textField.add ( trailingComponent, TextComponentLayout.TRAILING );

            // Updating components state
            updateInnerComponents ();
        }

        // Updating layout
        textField.revalidate ();

        // Updating border
        updateBorder ();
    }

    private void cleanupTrailingComponent ()
    {
        if ( this.trailingComponent != null )
        {
            this.trailingComponent.removeComponentListener ( componentResizeListener );
            textField.remove ( this.trailingComponent );
            this.trailingComponent = null;
        }
    }

    public void setFieldMargin ( final Insets margin )
    {
        this.fieldMargin = margin;
        updateBorder ();
    }

    public Insets getFieldMargin ()
    {
        return fieldMargin;
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

    public boolean isDrawShade ()
    {
        return drawShade;
    }

    public void setDrawShade ( final boolean drawShade )
    {
        this.drawShade = drawShade;
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    public boolean isDrawBackground ()
    {
        return drawBackground;
    }

    public void setDrawBackground ( final boolean drawBackground )
    {
        this.drawBackground = drawBackground;
        updateView ();
    }

    public boolean isWebColored ()
    {
        return webColored;
    }

    public void setWebColored ( final boolean webColored )
    {
        this.webColored = webColored;
        updateView ();
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
        updateView ();
    }

    public boolean isDrawBorder ()
    {
        return drawBorder;
    }

    public void setDrawBorder ( final boolean drawBorder )
    {
        this.drawBorder = drawBorder;
        updateBorder ();
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( final boolean drawFocus )
    {
        this.drawFocus = drawFocus;
        updateView ();
    }

    public Painter getPainter ()
    {
        return painter;
    }

    public void setPainter ( final Painter painter )
    {
        PainterSupport.uninstallPainter ( textField, this.painter );

        this.painter = painter;
        getComponent ().setOpaque ( painter == null || painter.isOpaque ( textField ) );
        PainterSupport.installPainter ( textField, this.painter );
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
        if ( textField != null )
        {
            textField.repaint ();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( textField != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( textField ) )
            {
                return;
            }

            // Style border
            final Insets m;
            if ( painter != null )
            {
                m = painter.getMargin ( getComponent () );
            }
            else if ( drawBorder )
            {
                m = new Insets ( shadeWidth + 1, shadeWidth + 1, shadeWidth + 1, shadeWidth + 1 );
            }
            else
            {
                m = new Insets ( 0, 0, 0, 0 );
            }

            // Taking margins into account
            final boolean ltr = textField.getComponentOrientation ().isLeftToRight ();
            final Insets margin = textField.getMargin ();
            if ( margin != null )
            {
                m.top += margin.top;
                m.left += ltr ? margin.left : margin.right;
                m.bottom += margin.bottom;
                m.right += ltr ? margin.right : margin.left;
            }
            if ( fieldMargin != null )
            {
                m.top += fieldMargin.top;
                m.left += ltr ? fieldMargin.left : fieldMargin.right;
                m.bottom += fieldMargin.bottom;
                m.right += ltr ? fieldMargin.right : fieldMargin.left;
            }

            // Adding component sizes into border
            final Component lc = ltr ? leadingComponent : trailingComponent;
            final Component tc = ltr ? trailingComponent : leadingComponent;
            if ( lc != null )
            {
                m.left += lc.getPreferredSize ().width;
            }
            if ( tc != null )
            {
                m.right += tc.getPreferredSize ().width;
            }

            // Final border
            textField.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    @Override
    protected void paintSafely ( final Graphics g )
    {
        final JTextComponent c = getComponent ();
        final Graphics2D g2d = ( Graphics2D ) g;

        if ( c.isOpaque () && ( painter == null || !painter.isOpaque ( textField ) ) )
        {
            // Paint default background
            g.setColor ( c.getBackground () );
            g.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
        }

        if ( painter != null || drawBorder )
        {
            final Object aa = LafUtils.setupAntialias ( g2d );
            if ( painter != null )
            {
                // Use background painter instead of default UI graphics
                painter.paint ( g2d, SwingUtils.size ( c ), c );
            }
            else if ( drawBorder )
            {
                // Border, background and shade
                final Color shadeColor;
                if ( drawShade )
                {
                    shadeColor = drawFocus && c.isFocusOwner () ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor;
                }
                else
                {
                    shadeColor = null;
                }
                LafUtils.drawWebStyle ( g2d, c, shadeColor, shadeWidth, round, drawBackground, webColored );
            }
            LafUtils.restoreAntialias ( g2d, aa );
        }

        final Map hints = SwingUtils.setupTextAntialias ( g2d );
        super.paintSafely ( g );
        if ( isInputPromptVisible ( c ) )
        {
            final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
            final Rectangle b = getVisibleEditorRect ();
            final Shape oc = LafUtils.intersectClip ( g2d, b );
            g2d.setFont ( inputPromptFont != null ? inputPromptFont : c.getFont () );
            g2d.setPaint ( inputPromptForeground != null ? inputPromptForeground : c.getForeground () );

            final FontMetrics fm = g2d.getFontMetrics ();
            final int x;
            if ( inputPromptPosition == CENTER )
            {
                x = b.x + b.width / 2 - fm.stringWidth ( inputPrompt ) / 2;
            }
            else if ( ltr && inputPromptPosition == LEADING || !ltr && inputPromptPosition == TRAILING )
            {
                x = b.x;
            }
            else
            {
                x = b.x + b.width - fm.stringWidth ( inputPrompt );
            }
            g2d.drawString ( inputPrompt, x, getBaseline ( c, c.getWidth (), c.getHeight () ) );

            g2d.setClip ( oc );
        }
        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    @Override
    protected void paintBackground ( final Graphics g )
    {
        // Do not paint anything here
    }


    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        Dimension ps = super.getPreferredSize ( c );

        // Fix for Swing bug with pointless scrolling when field's default preferred size is already reached
        ps.width += 1;

        // Height might be changed due to inner components
        if ( leadingComponent != null || trailingComponent != null )
        {
            final Dimension lps = c.getLayout ().preferredLayoutSize ( c );
            ps.height = Math.max ( ps.height, lps.height );
        }

        // Background painter preferred size
        if ( painter != null )
        {
            ps = SwingUtils.max ( ps, painter.getPreferredSize ( c ) );
        }

        return ps;
    }
}