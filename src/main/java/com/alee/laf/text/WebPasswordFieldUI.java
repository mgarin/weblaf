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
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
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
 * User: mgarin Date: 16.05.11 Time: 20:37
 */

public class WebPasswordFieldUI extends BasicPasswordFieldUI implements ShapeProvider, SwingConstants
{
    private JPasswordField passwordField;

    private boolean drawBorder = WebPasswordFieldStyle.drawBorder;
    private boolean drawFocus = WebPasswordFieldStyle.drawFocus;
    private int round = WebPasswordFieldStyle.round;
    private boolean drawShade = WebPasswordFieldStyle.drawShade;
    private int shadeWidth = WebPasswordFieldStyle.shadeWidth;
    private boolean drawBackground = WebPasswordFieldStyle.drawBackground;
    private boolean webColored = WebPasswordFieldStyle.webColored;
    private Painter painter = WebPasswordFieldStyle.painter;
    private Insets fieldMargin = WebPasswordFieldStyle.fieldMargin;
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

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebPasswordFieldUI ( ( JPasswordField ) c );
    }

    public WebPasswordFieldUI ( final JPasswordField passwordField )
    {
        this ( passwordField, true );
    }

    public WebPasswordFieldUI ( final JPasswordField passwordField, boolean drawBorder )
    {
        super ();
        this.drawBorder = drawBorder;
        this.passwordField = passwordField;
    }

    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( passwordField );
        passwordField.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );
        passwordField.setFocusable ( true );
        passwordField.setOpaque ( false );
        passwordField.setMargin ( WebPasswordFieldStyle.margin );
        passwordField.setBackground ( Color.WHITE );
        passwordField.setSelectionColor ( StyleConstants.textSelectionColor );
        passwordField.setForeground ( Color.BLACK );
        passwordField.setSelectedTextColor ( Color.BLACK );
        passwordField.setCaretColor ( Color.GRAY );
        passwordField.setLayout ( new TextComponentLayout ( passwordField ) );

        // Updating border
        updateBorder ();

        focusListener = new FocusListener ()
        {
            public void focusLost ( FocusEvent e )
            {
                passwordField.repaint ();
            }

            public void focusGained ( FocusEvent e )
            {
                passwordField.repaint ();
            }
        };
        passwordField.addFocusListener ( focusListener );

        accessibleChangeListener = new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent evt )
            {
                updateInnerComponents ();
            }
        };
        passwordField.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ENABLED_PROPERTY, accessibleChangeListener );

        orientationChangeListener = new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        passwordField.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, orientationChangeListener );

        marginChangeListener = new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        passwordField.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_MARGIN_PROPERTY, marginChangeListener );

        componentResizeListener = new ComponentAdapter ()
        {
            public void componentResized ( ComponentEvent e )
            {
                updateBorder ();
            }
        };
    }

    public void uninstallUI ( JComponent c )
    {
        passwordField.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, null );

        passwordField.removeFocusListener ( focusListener );
        passwordField.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ENABLED_PROPERTY, accessibleChangeListener );
        passwordField.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, orientationChangeListener );
        passwordField.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_MARGIN_PROPERTY, marginChangeListener );

        cleanupLeadingComponent ();
        cleanupTrailingComponent ();
        passwordField.setLayout ( null );

        super.uninstallUI ( c );
    }

    public Shape provideShape ()
    {
        if ( drawBorder )
        {
            return LafUtils.getWebBorderShape ( passwordField, shadeWidth, round );
        }
        else
        {
            return SwingUtils.size ( passwordField );
        }
    }

    private void updateInnerComponents ()
    {
        if ( leadingComponent != null )
        {
            leadingComponent.setEnabled ( passwordField.isEnabled () );
        }
        if ( trailingComponent != null )
        {
            trailingComponent.setEnabled ( passwordField.isEnabled () );
        }
    }

    public JComponent getLeadingComponent ()
    {
        return leadingComponent;
    }

    public void setLeadingComponent ( JComponent leadingComponent )
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
            passwordField.add ( leadingComponent, TextComponentLayout.LEADING );

            // Updating components state
            updateInnerComponents ();
        }

        // Updating border
        updateBorder ();
    }

    private void cleanupLeadingComponent ()
    {
        if ( this.leadingComponent != null )
        {
            this.leadingComponent.removeComponentListener ( componentResizeListener );
            passwordField.remove ( this.leadingComponent );
            this.leadingComponent = null;
        }
    }

    public JComponent getTrailingComponent ()
    {
        return trailingComponent;
    }

    public void setTrailingComponent ( JComponent trailingComponent )
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
            passwordField.add ( trailingComponent, TextComponentLayout.TRAILING );

            // Updating components state
            updateInnerComponents ();
        }

        // Updating border
        updateBorder ();
    }

    private void cleanupTrailingComponent ()
    {
        if ( this.trailingComponent != null )
        {
            this.trailingComponent.removeComponentListener ( componentResizeListener );
            passwordField.remove ( this.trailingComponent );
            this.trailingComponent = null;
        }
    }

    public void setFieldMargin ( Insets margin )
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

    public void setInputPrompt ( String inputPrompt )
    {
        this.inputPrompt = inputPrompt;
        this.inputPromptSet = inputPrompt != null && !inputPrompt.trim ().equals ( "" );
        updateInputPromptView ();
    }

    public Font getInputPromptFont ()
    {
        return inputPromptFont;
    }

    public void setInputPromptFont ( Font inputPromptFont )
    {
        this.inputPromptFont = inputPromptFont;
        updateInputPromptView ();
    }

    public Color getInputPromptForeground ()
    {
        return inputPromptForeground;
    }

    public void setInputPromptForeground ( Color inputPromptForeground )
    {
        this.inputPromptForeground = inputPromptForeground;
        updateInputPromptView ();
    }

    public int getInputPromptPosition ()
    {
        return inputPromptPosition;
    }

    public void setInputPromptPosition ( int inputPromptPosition )
    {
        this.inputPromptPosition = inputPromptPosition;
        updateInputPromptView ();
    }

    public boolean isHideInputPromptOnFocus ()
    {
        return hideInputPromptOnFocus;
    }

    public void setHideInputPromptOnFocus ( boolean hideInputPromptOnFocus )
    {
        this.hideInputPromptOnFocus = hideInputPromptOnFocus;
        updateInputPromptView ();
    }

    public boolean isDrawShade ()
    {
        return drawShade;
    }

    public void setDrawShade ( boolean drawShade )
    {
        this.drawShade = drawShade;
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    public boolean isDrawBackground ()
    {
        return drawBackground;
    }

    public void setDrawBackground ( boolean drawBackground )
    {
        this.drawBackground = drawBackground;
        updateView ();
    }

    public boolean isWebColored ()
    {
        return webColored;
    }

    public void setWebColored ( boolean webColored )
    {
        this.webColored = webColored;
        updateView ();
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( int round )
    {
        this.round = round;
        updateView ();
    }

    public boolean isDrawBorder ()
    {
        return drawBorder;
    }

    public void setDrawBorder ( boolean drawBorder )
    {
        this.drawBorder = drawBorder;
        updateBorder ();
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( boolean drawFocus )
    {
        this.drawFocus = drawFocus;
        updateView ();
    }

    public Painter getPainter ()
    {
        return painter;
    }

    public void setPainter ( Painter painter )
    {
        this.painter = painter;
        getComponent ().setOpaque ( painter == null || painter.isOpaque ( passwordField ) );
        updateBorder ();
    }

    private void updateInputPromptView ()
    {
        if ( isInputPromptVisible ( getComponent () ) )
        {
            updateView ();
        }
    }

    private boolean isInputPromptVisible ( JTextComponent c )
    {
        return inputPromptSet && c.isEditable () && c.isEnabled () && ( !hideInputPromptOnFocus || !c.isFocusOwner () ) &&
                c.getText ().equals ( "" );
    }

    private void updateView ()
    {
        if ( passwordField != null )
        {
            passwordField.repaint ();
        }
    }

    private void updateBorder ()
    {
        if ( passwordField != null )
        {
            // Style border
            Insets m;
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
            boolean ltr = passwordField.getComponentOrientation ().isLeftToRight ();
            Insets margin = passwordField.getMargin ();
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
            Component lc = ltr ? leadingComponent : trailingComponent;
            Component tc = ltr ? trailingComponent : leadingComponent;
            if ( lc != null )
            {
                m.left += lc.getPreferredSize ().width;
            }
            if ( tc != null )
            {
                m.right += tc.getPreferredSize ().width;
            }

            // Final border
            passwordField.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    protected void paintSafely ( Graphics g )
    {
        JTextComponent c = getComponent ();
        Graphics2D g2d = ( Graphics2D ) g;

        if ( c.isOpaque () && ( painter == null || !painter.isOpaque ( passwordField ) ) )
        {
            // Paint default background
            g.setColor ( c.getBackground () );
            g.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
        }

        if ( painter != null || drawBorder )
        {
            Object aa = LafUtils.setupAntialias ( g2d );
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

        Map hints = SwingUtils.setupTextAntialias ( g2d, passwordField );
        super.paintSafely ( g );
        if ( isInputPromptVisible ( c ) )
        {
            boolean ltr = c.getComponentOrientation ().isLeftToRight ();
            Rectangle b = getVisibleEditorRect ();
            Shape oc = LafUtils.intersectClip ( g2d, b );
            g2d.setFont ( inputPromptFont != null ? inputPromptFont : c.getFont () );
            g2d.setPaint ( inputPromptForeground != null ? inputPromptForeground : c.getForeground () );

            FontMetrics fm = g2d.getFontMetrics ();
            int x;
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
            g2d.drawString ( inputPrompt, x, b.y + b.height / 2 + ( fm.getAscent () - fm.getDescent () ) / 2 );

            g2d.setClip ( oc );
        }
        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    protected void paintBackground ( Graphics g )
    {
        //
    }

    public Dimension getPreferredSize ( JComponent c )
    {
        Dimension ps = super.getPreferredSize ( c );

        // Fix for Swing bug with pointless scrolling when field's default preferred size is already reached
        ps.width += 1;

        // Height might be changed due to inner components
        if ( leadingComponent != null || trailingComponent != null )
        {
            Dimension lps = c.getLayout ().preferredLayoutSize ( c );
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