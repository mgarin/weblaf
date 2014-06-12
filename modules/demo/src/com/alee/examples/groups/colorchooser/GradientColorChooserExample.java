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

package com.alee.examples.groups.colorchooser;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.examples.content.presentation.OneTimeTooltipStep;
import com.alee.examples.content.presentation.PresentationStep;
import com.alee.extended.colorchooser.GradientColorData;
import com.alee.extended.colorchooser.GradientData;
import com.alee.extended.colorchooser.WebGradientColorChooser;
import com.alee.extended.panel.FlowPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebButtonGroup;
import com.alee.laf.button.WebButton;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.GeometryUtils;
import com.alee.utils.ImageUtils;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 27.11.12 Time: 14:56
 */

public class GradientColorChooserExample extends DefaultExample
{
    private WebGradientColorChooser colorChooser;

    @Override
    public String getTitle ()
    {
        return "Gradient color chooser";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled gradient color chooser";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public boolean isPresentationAvailable ()
    {
        return true;
    }

    @Override
    public List<PresentationStep> getPresentationSteps ()
    {
        final List<PresentationStep> presentationSteps = new ArrayList<PresentationStep> ();

        // Color choose tip
        final WebCustomTooltip tip1 = new WebCustomTooltip ( colorChooser, lmb, "Double click on any color to change it", TooltipWay.down );
        tip1.setDisplayLocation ( gripper1 () );
        presentationSteps.add ( new OneTimeTooltipStep ( "Edit", 5000, tip1, this ) );

        // Color choose tip
        final WebCustomTooltip tip2 =
                new WebCustomTooltip ( colorChooser, lmb, "Click on any free space to add new color", TooltipWay.down );
        tip2.setDisplayLocation ( betweenGrippers () );
        presentationSteps.add ( new OneTimeTooltipStep ( "Add", 5000, tip2, this ) );

        // Color copy tip
        final WebCustomTooltip tip3 = new WebCustomTooltip ( colorChooser, lmb, "Press ALT and drag color to copy it", TooltipWay.down );
        tip3.setDisplayLocation ( gripper2 () );
        presentationSteps.add ( new OneTimeTooltipStep ( "Copy", 5000, tip3, this ) );

        // Color delete tip
        final WebCustomTooltip tip41 = new WebCustomTooltip ( colorChooser, ImageUtils.combineIcons ( 2, rmb, mmb ),
                "Use middle or right mouse button to remove color", TooltipWay.down );
        tip41.setDisplayLocation ( gripper2 () );
        presentationSteps.add ( new OneTimeTooltipStep ( "Remove", 5000, tip41, this ) );

        // Color delete tip 2
        final WebCustomTooltip tip42 =
                new WebCustomTooltip ( colorChooser, cursor, "You can also drag color up to remove it", TooltipWay.down );
        tip42.setDisplayLocation ( gripper2 () );
        presentationSteps.add ( new OneTimeTooltipStep ( "Remove", 5000, tip42, this ) );

        colorChooser.addChangeListener ( new ChangeListener ()
        {
            @Override
            public void stateChanged ( ChangeEvent e )
            {
                tip1.setDisplayLocation ( gripper1 () );
                tip2.setDisplayLocation ( betweenGrippers () );
                tip3.setDisplayLocation ( gripper2 () );
                tip41.setDisplayLocation ( gripper1 () );
                tip42.setDisplayLocation ( gripper1 () );
            }
        } );

        return presentationSteps;
    }

    public Point gripper1 ()
    {
        return GeometryUtils.middle ( colorChooser.getGripperBounds ( 0 ) );
    }

    private Point gripper2 ()
    {
        return GeometryUtils.middle ( colorChooser.getGripperBounds ( 1 ) );
    }

    private Point betweenGrippers ()
    {
        return GeometryUtils.middle ( GeometryUtils.middle ( colorChooser.getGripperBounds ( 0 ) ),
                GeometryUtils.middle ( colorChooser.getGripperBounds ( 1 ) ) );
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Default value
        final GradientData defaultValue = SettingsManager.getDefaultValue ( GradientData.class );

        // Simple color chooser
        colorChooser = new WebGradientColorChooser ();
        colorChooser.setPreferredWidth ( 350 );
        colorChooser.registerSettings ( "GradientColorChooserExample.gradientData", GradientData.class );

        // Reset button
        final WebButton colored = new WebButton ( loadIcon ( "colored.png" ) );
        TooltipManager.setTooltip ( colored, "Various colors" );
        colored.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                // Simply apply default gradient values
                colorChooser.setGradientData ( defaultValue.clone () );
            }
        } );

        // Black & white colors button
        final WebButton blackAndWhite = new WebButton ( loadIcon ( "bw.png" ) );
        TooltipManager.setTooltip ( blackAndWhite, "Black and white colors" );
        blackAndWhite.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                // Apply gradient values through SettingsManager
                // Notice that component will be automatically updated due to external settings changes!
                final GradientData blackAndWhite = new GradientData ();
                blackAndWhite.addGradientColorData ( new GradientColorData ( 0f, Color.BLACK ) );
                blackAndWhite.addGradientColorData ( new GradientColorData ( 1f, Color.WHITE ) );
                SettingsManager.set ( "GradientColorChooserExample.gradientData", blackAndWhite );
            }
        } );

        final WebButtonGroup buttonGroup = new WebButtonGroup ( colored, blackAndWhite );
        buttonGroup.setButtonsFocusable ( false );

        final GroupPanel colorChooserPanel = new GroupPanel ( 20, colorChooser, new FlowPanel ( 5, buttonGroup ) );
        colorChooserPanel.setMargin ( 15 );
        return colorChooserPanel;
    }
}