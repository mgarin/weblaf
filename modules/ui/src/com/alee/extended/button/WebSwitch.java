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

import com.alee.global.StyleConstants;
import com.alee.laf.Styles;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

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
 */

public class WebSwitch extends WebPanel
{
    /**
     * Switch action listeners.
     */
    protected final List<ActionListener> actionListeners = new ArrayList<ActionListener> ( 1 );

    /**
     * Style settings.
     */
    protected boolean animate = WebSwitchStyle.animate;

    /**
     * Runtime variables.
     */
    protected boolean selected = false;
    protected boolean animating = false;
    protected WebTimer animator;

    /**
     * UI elements.
     */
    protected final WebPanel gripper;
    protected WebLabel leftComponent;
    protected WebLabel rightComponent;

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
    public WebSwitch ( final boolean selected )
    {
        super ( Styles.wswitch, new WebSwitchLayout () );
        putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );

        // Switch gripper
        gripper = new WebPanel ( Styles.wswitchGripper );
        add ( gripper, WebSwitchLayout.GRIPPER );

        // Left switch label
        leftComponent = new WebLabel ( "ON", WebLabel.CENTER );
        leftComponent.setStyleId ( Styles.wswitchOffLabel );
        leftComponent.setBoldFont ();
        add ( leftComponent, WebSwitchLayout.LEFT );

        // Right switch label
        rightComponent = new WebLabel ( "OFF", WebLabel.CENTER );
        leftComponent.setStyleId ( Styles.wswitchOnLabel );
        rightComponent.setBoldFont ();
        add ( rightComponent, WebSwitchLayout.RIGHT );

        // Switch animator
        createAnimator ();

        // Selection switch listener
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
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
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Updating gripper location
                final WebSwitchLayout switchLayout = getSwitchLayout ();
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
    public WebPanel getGripper ()
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
    public void setLeftComponent ( final WebLabel leftComponent )
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
    public void setRightComponent ( final WebLabel rightComponent )
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
    @Override
    public void setEnabled ( final boolean enabled )
    {
        super.setEnabled ( enabled );
        gripper.setEnabled ( enabled );
        leftComponent.setEnabled ( enabled );
        rightComponent.setEnabled ( enabled );
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
    public void setSelected ( final boolean selected )
    {
        setSelected ( selected, animate );
    }

    /**
     * Sets whether switch is selected or not and animates the transition if requested.
     *
     * @param selected whether switch is selected or not
     * @param animate  whether switch should animate the transition or not
     */
    public void setSelected ( final boolean selected, final boolean animate )
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
    public void setAnimate ( final boolean animate )
    {
        this.animate = animate;
    }

    /**
     * Adds new switch action listener.
     *
     * @param actionListener switch action listener
     */
    public void addActionListener ( final ActionListener actionListener )
    {
        actionListeners.add ( actionListener );
    }

    /**
     * Removes switch action listener.
     *
     * @param actionListener switch action listener
     */
    public void removeActionListener ( final ActionListener actionListener )
    {
        actionListeners.remove ( actionListener );
    }

    /**
     * Fires that switch action is performed.
     */
    private void fireActionPerformed ()
    {
        final ActionEvent actionEvent = new ActionEvent ( WebSwitch.this, 0, "Selection changed" );
        for ( final ActionListener actionListener : CollectionUtils.copy ( actionListeners ) )
        {
            actionListener.actionPerformed ( actionEvent );
        }
    }
}