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

package com.alee.extended.button;

import com.alee.laf.StyleConstants;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This component that allows switching between two states.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebSwitch extends WebPanel
{
    /**
     * Switch action listeners.
     */
    private List<ActionListener> actionListeners = new ArrayList<ActionListener> ();

    /**
     * Style settings.
     */
    private boolean animate = WebSwitchStyle.animate;

    /**
     * Runtime variables.
     */
    private boolean selected = false;
    private boolean animating = false;
    private WebTimer animator;

    /**
     * UI elements.
     */
    private WebSwitchGripper gripper;
    private WebLabel leftComponent;
    private WebLabel rightComponent;

    /**
     * Constructs a deselected switch.
     */
    public WebSwitch ()
    {
        this ( false );
    }

    /**
     * Cstructs either selected or deselected switch.
     *
     * @param selected whether switch is selected or not
     */
    public WebSwitch ( boolean selected )
    {
        super ( true, new WebSwitchLayout () );

        // Switch syling
        putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );
        setRound ( WebSwitchStyle.round );
        setDrawFocus ( true );
        setFocusable ( true );

        // Switch gripper
        gripper = new WebSwitchGripper ();
        add ( gripper, WebSwitchLayout.GRIPPER );

        // Left switch label
        leftComponent = new WebLabel ( "ON", WebLabel.CENTER );
        leftComponent.setMargin ( 2, 5, 2, 5 );
        leftComponent.setDrawShade ( true );
        leftComponent.setForeground ( Color.DARK_GRAY );
        add ( SwingUtils.setBoldFont ( leftComponent ), WebSwitchLayout.LEFT );

        // Right switch label
        rightComponent = new WebLabel ( "OFF", WebLabel.CENTER );
        rightComponent.setMargin ( 2, 5, 2, 5 );
        rightComponent.setDrawShade ( true );
        rightComponent.setForeground ( Color.DARK_GRAY );
        add ( SwingUtils.setBoldFont ( rightComponent ), WebSwitchLayout.RIGHT );

        // Switch animator
        createAnimator ();

        // Selection switch listener
        MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            public void mousePressed ( MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) && isEnabled () )
                {
                    requestFocusInWindow ();
                    setSelected ( !isSelected () );
                }
            }
        };
        gripper.addMouseListener ( mouseAdapter );
        addMouseListener ( mouseAdapter );

        // Initial selection
        setSelected ( selected, false );
    }

    /**
     * Initializes switch animator.
     */
    private void createAnimator ()
    {
        animator = new WebTimer ( "WebSwitch.animator", StyleConstants.maxAnimationDelay, new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                // Updating gripper location
                WebSwitchLayout switchLayout = getSwitchLayout ();
                switchLayout.setGripperLocation ( switchLayout.getGripperLocation () + ( selected ? 0.1f : -0.1f ) );

                // Checking what to do
                if ( selected && switchLayout.getGripperLocation () >= 1f || !selected && switchLayout.getGripperLocation () <= 0f )
                {
                    // Updating final gripper and view
                    switchLayout.setGripperLocation ( selected ? 1f : 0f );
                    revalidate ();

                    // Finishing animation
                    animating = false;
                    animator.stop ();
                }
                else
                {
                    // Updating view
                    revalidate ();
                }
            }
        } );
    }

    /**
     * Starts animation.
     */
    private void startAnimation ()
    {
        if ( !animating )
        {
            animating = true;
            animator.start ();
        }
    }

    /**
     * Returns actual switch layout.
     *
     * @return actual switch layout
     */
    public WebSwitchLayout getSwitchLayout ()
    {
        return ( WebSwitchLayout ) getLayout ();
    }

    /**
     * Returns switch gripper.
     *
     * @return switch gripper
     */
    public WebSwitchGripper getGripper ()
    {
        return gripper;
    }

    /**
     * Returns left switch component.
     *
     * @return left switch component
     */
    public WebLabel getLeftComponent ()
    {
        return leftComponent;
    }

    /**
     * Sets new left switch component.
     *
     * @param leftComponent new left switch component
     */
    public void setLeftComponent ( WebLabel leftComponent )
    {
        if ( this.leftComponent != null )
        {
            remove ( this.leftComponent );
        }
        this.leftComponent = leftComponent;
        add ( leftComponent, WebSwitchLayout.LEFT );
        revalidate ();
    }

    /**
     * Returns right switch component.
     *
     * @return right switch component
     */
    public WebLabel getRightComponent ()
    {
        return rightComponent;
    }

    /**
     * Sets new right switch component.
     *
     * @param rightComponent new right switch component
     */
    public void setRightComponent ( WebLabel rightComponent )
    {
        if ( this.rightComponent != null )
        {
            remove ( this.rightComponent );
        }
        this.rightComponent = rightComponent;
        add ( rightComponent, WebSwitchLayout.RIGHT );
        revalidate ();
    }

    /**
     * Sets whether switch is enabled or not.
     *
     * @param enabled whether switch is enabled or not
     */
    public void setEnabled ( boolean enabled )
    {
        super.setEnabled ( enabled );
        gripper.setEnabled ( enabled );
        leftComponent.setEnabled ( enabled );
        rightComponent.setEnabled ( enabled );
    }

    /**
     * Sets switch corners rounding.
     *
     * @param round switch corners rounding
     */
    public WebPanel setRound ( int round )
    {
        if ( gripper != null )
        {
            gripper.setRound ( Math.max ( round - 3, 0 ) );
        }
        return super.setRound ( round );
    }

    /**
     * Returns whether switch is selected or not.
     *
     * @return true if switch is selected, false otherwise
     */
    public boolean isSelected ()
    {
        return selected;
    }

    /**
     * Sets whether switch is selected or not.
     *
     * @param selected whether switch is selected or not
     */
    public void setSelected ( boolean selected )
    {
        setSelected ( selected, animate );
    }

    /**
     * Sets whether switch is selected or not and animates the transition if requested.
     *
     * @param selected whether switch is selected or not
     * @param animate  whether switch should animate the transition or not
     */
    public void setSelected ( boolean selected, boolean animate )
    {
        this.selected = selected;
        if ( animate )
        {
            startAnimation ();
        }
        else
        {
            getSwitchLayout ().setGripperLocation ( selected ? 1f : 0f );
            revalidate ();
        }
        fireActionPerformed ();
    }

    /**
     * Returns whether switch should animate all transition by default or not.
     *
     * @return true if switch should animate all transition by default, false otherwise
     */
    public boolean isAnimate ()
    {
        return animate;
    }

    /**
     * Sets whether switch should animate all transition by default or not.
     *
     * @param animate whether switch should animate all transition by default or not
     */
    public void setAnimate ( boolean animate )
    {
        this.animate = animate;
    }

    /**
     * Adds new switch action listener.
     *
     * @param actionListener switch action listener
     */
    public void addActionListener ( ActionListener actionListener )
    {
        actionListeners.add ( actionListener );
    }

    /**
     * Removes switch action listener.
     *
     * @param actionListener switch action listener
     */
    public void removeActionListener ( ActionListener actionListener )
    {
        actionListeners.remove ( actionListener );
    }

    /**
     * Fires that switch action is performed.
     */
    private void fireActionPerformed ()
    {
        ActionEvent actionEvent = new ActionEvent ( WebSwitch.this, 0, "Selection changed" );
        for ( ActionListener actionListener : CollectionUtils.copy ( actionListeners ) )
        {
            actionListener.actionPerformed ( actionEvent );
        }
    }
}