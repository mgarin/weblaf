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

package com.alee.examples.groups.transition;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.transition.TransitionListener;
import com.alee.extended.transition.effects.Direction;
import com.alee.extended.transition.effects.curtain.CurtainTransitionEffect;
import com.alee.extended.transition.effects.curtain.CurtainType;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 30.10.12 Time: 20:22
 */

public class CurtainTransitionExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Curtain transition";
    }

    @Override
    public String getDescription ()
    {
        return "Curtain transition effect";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Transition panel
        final ComponentTransition componentTransition = new ComponentTransition ();

        // Transition effect
        final CurtainTransitionEffect effect = new CurtainTransitionEffect ();
        effect.setType ( CurtainType.fade );
        effect.setSize ( 10 );
        effect.setSpeed ( 2 );
        componentTransition.setTransitionEffect ( effect );

        // Toolbar #1
        final WebToolBar toolBar1 = new WebToolBar ( WebToolBar.HORIZONTAL );
        toolBar1.setFloatable ( false );
        toolBar1.add ( WebButton.createIconWebButton ( loadIcon ( "toolbar/1.png" ), true ) );
        toolBar1.addSeparator ();
        toolBar1.add ( WebButton.createIconWebButton ( loadIcon ( "toolbar/2.png" ), true ) );
        toolBar1.addSeparator ();
        toolBar1.add ( WebButton.createIconWebButton ( loadIcon ( "toolbar/3.png" ), true ) );
        toolBar1.addToEnd ( WebButton.createIconWebButton ( loadIcon ( "toolbar/4.png" ), true ) );

        // Toolbar #2
        final WebToolBar toolBar2 = new WebToolBar ( WebToolBar.HORIZONTAL );
        toolBar2.setFloatable ( false );
        toolBar2.addSpacing ();
        toolBar2.add ( new WebLabel ( "Curtain transition example" ) );
        toolBar2.addSpacing ();
        componentTransition.setContent ( toolBar2 );

        // Initial transition panel state
        componentTransition.setContent ( toolBar1 );
        componentTransition.setPreferredSize ( SwingUtils.max ( toolBar1, toolBar2 ) );

        // States switch button
        final WebButton switchView = new WebButton ( loadIcon ( "switch.png" ) );
        switchView.setDrawFocus ( false );
        switchView.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( componentTransition.getContent () == toolBar1 )
                {
                    effect.setDirection ( Direction.right );
                    componentTransition.performTransition ( toolBar2 );
                }
                else
                {
                    effect.setDirection ( Direction.left );
                    componentTransition.performTransition ( toolBar1 );
                }
            }
        } );
        componentTransition.addTransitionListener ( new TransitionListener ()
        {
            @Override
            public void transitionStarted ()
            {
                switchView.setEnabled ( false );
            }

            @Override
            public void transitionFinished ()
            {
                switchView.setEnabled ( true );
            }
        } );

        return new GroupPanel ( 10, componentTransition, new CenterPanel ( switchView ) );
    }
}