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

package com.alee.demo.content.container;

import com.alee.api.jdk.BiConsumer;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.skin.DemoIcons;
import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.element.BreadcrumbElementData;
import com.alee.extended.button.WebSplitButton;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.date.WebDateField;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.link.WebLink;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.checkbox.CheckState;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.combobox.ComboBoxCellParameters;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.combobox.WebComboBoxRenderer;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.text.WebFormattedTextField;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.managers.animation.easing.Quadratic;
import com.alee.managers.animation.transition.*;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.swing.UnselectableButtonGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebBreadcrumbExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "breadcrumb";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "breadcrumb";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new WebLafWikiPage ( "How to Use WebBreadcrumb" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BreadcrumbPreview ( "basic", FeatureState.release, StyleId.breadcrumb ),
                new BreadcrumbPreview ( "undecorated", FeatureState.release, StyleId.breadcrumbUndecorated ),
                new BreadcrumbPreview ( "separated", FeatureState.release, StyleId.breadcrumbSeparated ),
                new BreadcrumbPreview ( "separated-undecorated", FeatureState.release, StyleId.breadcrumbSeparatedUndecorated )
        );
    }

    /**
     * Breadcrumb preview.
     */
    protected class BreadcrumbPreview extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id           preview ID
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public BreadcrumbPreview ( final String id, final FeatureState featureState, final StyleId styleId )
        {
            super ( WebBreadcrumbExample.this, id, featureState, styleId );
        }

        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new VerticalFlowLayout ( VerticalFlowLayout.MIDDLE, 8, false, false );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final String sampleKey = getExampleLanguageKey ( "data.sample" );
            final String breadcrumb = "Breadcrumb";
            final String elementsKey = getExampleLanguageKey ( "data.elements" );

            // todo Panel example

            final WebBreadcrumb labels = new WebBreadcrumb ( getStyleId () );
            labels.add ( new WebLabel ( sampleKey, DemoIcons.fire16, SwingConstants.CENTER ) );
            labels.add ( new WebLabel ( breadcrumb ) );
            labels.add ( new WebLabel ( "JLabel", DemoIcons.fire16, SwingConstants.CENTER ) );
            labels.add ( new WebLabel ( elementsKey ) );

            final WebBreadcrumb styledLabels = new WebBreadcrumb ( getStyleId () );
            styledLabels.add ( new WebStyledLabel ( sampleKey, DemoIcons.fire16 ) );
            styledLabels.add ( new WebStyledLabel ( "{" + breadcrumb + ":u}" ) );
            styledLabels.add ( new WebStyledLabel ( "{WebStyledLabel:c(99,99,99)}", DemoIcons.fire16 ) );
            styledLabels.add ( new WebStyledLabel ( elementsKey ) );

            final WebBreadcrumb links = new WebBreadcrumb ( getStyleId () );
            links.add ( new WebLink ( DemoIcons.fire16, sampleKey ) );
            links.add ( new WebLink ( breadcrumb ) );
            links.add ( new WebLink ( DemoIcons.fire16, "WebLink" ) );
            links.add ( new WebLink ( elementsKey ) );

            final WebBreadcrumb buttons = new WebBreadcrumb ( getStyleId () );
            buttons.add ( new WebButton ( sampleKey, DemoIcons.fire16 ) );
            buttons.add ( new WebButton ( breadcrumb ) );
            buttons.add ( new WebButton ( "JButton", DemoIcons.fire16 ) );
            buttons.add ( new WebButton ( elementsKey ) );

            final WebBreadcrumb toggleButtons = new WebBreadcrumb ( getStyleId () );
            toggleButtons.add ( new WebToggleButton ( sampleKey, DemoIcons.fire16 ) );
            toggleButtons.add ( new WebToggleButton ( breadcrumb ) );
            toggleButtons.add ( new WebToggleButton ( "JToggleButton", DemoIcons.fire16 ) );
            toggleButtons.add ( new WebToggleButton ( elementsKey ) );

            final WebBreadcrumb splitbuttons = new WebBreadcrumb ( getStyleId () );
            splitbuttons.add ( new WebSplitButton ( sampleKey ) );
            splitbuttons.add ( new WebSplitButton ( breadcrumb ) );
            splitbuttons.add ( new WebSplitButton ( "WebSplitButton" ) );
            splitbuttons.add ( new WebSplitButton ( elementsKey ) );
            splitbuttons.forEach ( new BiConsumer<Integer, WebSplitButton> ()
            {
                @Override
                public void accept ( final Integer index, final WebSplitButton splitButton )
                {
                    final WebPopupMenu menu = new WebPopupMenu ();
                    menu.add ( new WebMenuItem ( getExampleLanguageKey ( "data.menu1" ), DemoIcons.fire16 ) );
                    menu.add ( new WebMenuItem ( getExampleLanguageKey ( "data.menu2" ), DemoIcons.brush16 ) );
                    menu.add ( new WebMenuItem ( getExampleLanguageKey ( "data.menu3" ), DemoIcons.menu16 ) );
                    splitButton.setPopupMenu ( menu );
                }
            } );

            final WebBreadcrumb comboboxes = new WebBreadcrumb ( getStyleId () );
            comboboxes.add ( new WebComboBox ( new String[]{ sampleKey } ) );
            comboboxes.add ( new WebComboBox ( new String[]{ breadcrumb } ) );
            comboboxes.add ( new WebComboBox ( new String[]{ "JComboBox" } ) );
            comboboxes.add ( new WebComboBox ( new String[]{ elementsKey } ) );
            comboboxes.forEach ( new BiConsumer<Integer, WebComboBox> ()
            {
                @Override
                public void accept ( final Integer index, final WebComboBox comboBox )
                {
                    comboBox.setRenderer ( new WebComboBoxRenderer<String, JList, ComboBoxCellParameters<String, JList>> ()
                    {
                        @Override
                        protected String textForValue ( final ComboBoxCellParameters<String, JList> parameters )
                        {
                            return LM.get ( parameters.value () );
                        }
                    } );
                }
            } );

            final WebBreadcrumb datefields = new WebBreadcrumb ( getStyleId () );
            datefields.add ( new WebDateField ( new Date () ) );
            datefields.add ( new WebDateField ( new Date () ) );
            datefields.add ( new WebDateField ( new Date () ) );
            datefields.add ( new WebDateField ( new Date () ) );

            final WebBreadcrumb checkboxes = new WebBreadcrumb ( getStyleId () );
            checkboxes.add ( new WebCheckBox ( sampleKey, true ) );
            checkboxes.add ( new WebCheckBox ( breadcrumb, false ) );
            checkboxes.add ( new WebCheckBox ( "JCheckBox", false ) );
            checkboxes.add ( new WebCheckBox ( elementsKey, true ) );

            final WebBreadcrumb tristatecheckboxes = new WebBreadcrumb ( getStyleId () );
            tristatecheckboxes.add ( new WebTristateCheckBox ( sampleKey, CheckState.checked ) );
            tristatecheckboxes.add ( new WebTristateCheckBox ( breadcrumb, CheckState.mixed ) );
            tristatecheckboxes.add ( new WebTristateCheckBox ( "WebTristateCheckBox", CheckState.unchecked ) );
            tristatecheckboxes.add ( new WebTristateCheckBox ( elementsKey ) );

            final WebBreadcrumb radiobuttons = new WebBreadcrumb ( getStyleId () );
            radiobuttons.add ( new WebRadioButton ( sampleKey, true ) );
            radiobuttons.add ( new WebRadioButton ( breadcrumb ) );
            radiobuttons.add ( new WebRadioButton ( "JRadioButton" ) );
            radiobuttons.add ( new WebRadioButton ( elementsKey ) );
            UnselectableButtonGroup.group ( radiobuttons );

            final WebBreadcrumb textfields = new WebBreadcrumb ( getStyleId () );
            textfields.add ( new WebTextField ( 10 ) );
            textfields.add ( new WebTextField ( breadcrumb, 10 ) );
            textfields.add ( new WebTextField ( "JTextField", 10 ) );
            textfields.add ( new WebTextField ( 10 ) );
            textfields.forEach ( new BiConsumer<Integer, WebTextField> ()
            {
                @Override
                public void accept ( final Integer index, final WebTextField textField )
                {
                    if ( index == 0 )
                    {
                        textField.setLanguage ( sampleKey );
                    }
                    if ( index == 3 )
                    {
                        textField.setLanguage ( elementsKey );
                    }
                }
            } );

            final WebBreadcrumb passwordfields = new WebBreadcrumb ( getStyleId () );
            passwordfields.add ( new WebPasswordField ( 10 ) );
            passwordfields.add ( new WebPasswordField ( breadcrumb, 10 ) );
            passwordfields.add ( new WebPasswordField ( "JPasswordField", 10 ) );
            passwordfields.add ( new WebPasswordField ( 10 ) );
            passwordfields.forEach ( new BiConsumer<Integer, WebPasswordField> ()
            {
                @Override
                public void accept ( final Integer index, final WebPasswordField passwordField )
                {
                    if ( index == 0 )
                    {
                        passwordField.setLanguage ( sampleKey );
                    }
                    if ( index == 3 )
                    {
                        passwordField.setLanguage ( elementsKey );
                    }
                }
            } );

            final WebBreadcrumb formattedtextfields = new WebBreadcrumb ( getStyleId () );
            formattedtextfields.add ( new WebFormattedTextField () );
            formattedtextfields.add ( new WebFormattedTextField ( breadcrumb ) );
            formattedtextfields.add ( new WebFormattedTextField ( "JFormattedTextField" ) );
            formattedtextfields.add ( new WebFormattedTextField () );
            formattedtextfields.forEach ( new BiConsumer<Integer, WebFormattedTextField> ()
            {
                @Override
                public void accept ( final Integer index, final WebFormattedTextField formattedTextField )
                {
                    formattedTextField.setColumns ( 10 );
                    if ( index == 0 )
                    {
                        formattedTextField.setLanguage ( sampleKey );
                    }
                    if ( index == 3 )
                    {
                        formattedTextField.setLanguage ( elementsKey );
                    }
                }
            } );

            final WebBreadcrumb panels = new WebBreadcrumb ( getStyleId () );
            panels.add ( new WebPanel ( new BorderLayout () ) );
            panels.add ( new WebPanel ( new BorderLayout () ) );
            panels.add ( new WebPanel ( new BorderLayout () ) );
            panels.add ( new WebPanel ( new BorderLayout () ) );
            panels.forEach ( new BiConsumer<Integer, WebPanel> ()
            {
                @Override
                public void accept ( final Integer index, final WebPanel panel )
                {
                    panel.add ( new WebLabel ( index == 0 ? sampleKey : index == 1 ? breadcrumb : index == 2 ? "JPanel" : elementsKey ) );
                }
            } );

            return CollectionUtils.asList (
                    addProgressControlts ( labels ),
                    addProgressControlts ( styledLabels ),
                    addProgressControlts ( links ),
                    addProgressControlts ( buttons ),
                    addProgressControlts ( toggleButtons ),
                    addProgressControlts ( splitbuttons ),
                    addProgressControlts ( comboboxes ),
                    addProgressControlts ( datefields ),
                    addProgressControlts ( checkboxes ),
                    addProgressControlts ( tristatecheckboxes ),
                    addProgressControlts ( radiobuttons ),
                    addProgressControlts ( textfields ),
                    addProgressControlts ( passwordfields ),
                    addProgressControlts ( formattedtextfields ),
                    addProgressControlts ( panels )
            );
        }

        /**
         * Returns {@link WebBreadcrumb} with added progress state controls.
         *
         * @param breadcrumb {@link WebBreadcrumb}
         * @return {@link WebBreadcrumb} with added progress state controls
         */
        private JComponent addProgressControlts ( final WebBreadcrumb breadcrumb )
        {
            final QueueTransition progressAnimator = new QueueTransition<Integer> ( true );
            progressAnimator.add ( new TimedTransition<Double> ( 0.0d, 1.0d, new Quadratic.Out (), 1500L ) );
            progressAnimator.add ( new TimedTransition<Double> ( 1.0d, 2.0d, new Quadratic.Out (), 1500L ) );
            progressAnimator.add ( new TimedTransition<Double> ( 2.0d, 3.0d, new Quadratic.Out (), 1500L ) );
            progressAnimator.add ( new TimedTransition<Double> ( 3.0d, 4.0d, new Quadratic.Out (), 1500L ) );
            progressAnimator.add ( new IdleTransition<Double> ( 4.0d, 1000L ) );
            progressAnimator.add ( new TimedTransition<Double> ( 4.0d, 3.0d, new Quadratic.Out (), 1500L ) );
            progressAnimator.add ( new TimedTransition<Double> ( 3.0d, 2.0d, new Quadratic.Out (), 1500L ) );
            progressAnimator.add ( new TimedTransition<Double> ( 2.0d, 1.0d, new Quadratic.Out (), 1500L ) );
            progressAnimator.add ( new TimedTransition<Double> ( 1.0d, 0.0d, new Quadratic.Out (), 1500L ) );
            progressAnimator.add ( new IdleTransition<Double> ( 0.0d, 1000L ) );

            final WebToggleButton determinate = new WebToggleButton ( StyleId.togglebuttonUndecorated, DemoIcons.play16, false );
            determinate.addItemListener ( new ItemListener ()
            {
                @Override
                public void itemStateChanged ( final ItemEvent e )
                {
                    if ( determinate.isSelected () )
                    {
                        // Invoking later to allow second button change to sink in
                        CoreSwingUtils.invokeLater ( new Runnable ()
                        {
                            @Override
                            public void run ()
                            {
                                determinate.setIcon ( DemoIcons.pause16 );
                                progressAnimator.play ();
                            }
                        } );
                    }
                    else
                    {
                        determinate.setIcon ( DemoIcons.play16 );
                        progressAnimator.stop ();
                    }
                }
            } );

            final WebToggleButton indeterminate = new WebToggleButton ( StyleId.togglebuttonUndecorated, DemoIcons.indeterminate16, false );
            indeterminate.addItemListener ( new ItemListener ()
            {
                @Override
                public void itemStateChanged ( final ItemEvent e )
                {
                    if ( indeterminate.isSelected () )
                    {
                        // Invoking later to allow second button change to sink in
                        CoreSwingUtils.invokeLater ( new Runnable ()
                        {
                            @Override
                            public void run ()
                            {
                                breadcrumb.forEach ( new BiConsumer<Integer, JComponent> ()
                                {
                                    @Override
                                    public void accept ( final Integer index, final JComponent component )
                                    {
                                        breadcrumb.setProgressType ( component, BreadcrumbElementData.ProgressType.indeterminate );
                                    }
                                } );
                            }
                        } );
                    }
                    else
                    {
                        breadcrumb.forEach ( new BiConsumer<Integer, JComponent> ()
                        {
                            @Override
                            public void accept ( final Integer index, final JComponent component )
                            {
                                breadcrumb.setProgressType ( component, BreadcrumbElementData.ProgressType.none );
                            }
                        } );
                    }
                }
            } );

            UnselectableButtonGroup.group ( determinate, indeterminate );

            progressAnimator.addListener ( new TransitionAdapter<Double> ()
            {
                @Override
                public void started ( final Transition transition, final Double value )
                {
                    breadcrumb.forEach ( new BiConsumer<Integer, JComponent> ()
                    {
                        @Override
                        public void accept ( final Integer index, final JComponent component )
                        {
                            breadcrumb.setProgressType ( component, BreadcrumbElementData.ProgressType.progress );
                        }
                    } );
                    adjusted ( transition, value );
                }

                @Override
                public void adjusted ( final Transition transition, final Double value )
                {
                    // todo This is good enough for demo, but still might improve later on
                    final double floor = Math.floor ( value );
                    final int index = ( int ) Math.round ( floor );
                    if ( index < breadcrumb.getComponentCount () )
                    {
                        final JComponent component = ( JComponent ) breadcrumb.getComponent ( index );
                        breadcrumb.setProgress ( component, value - floor );
                    }
                }

                @Override
                public void finished ( final Transition transition, final Double value )
                {
                    aborted ( transition, value );
                }

                @Override
                public void aborted ( final Transition transition, final Double value )
                {
                    breadcrumb.forEach ( new BiConsumer<Integer, JComponent> ()
                    {
                        @Override
                        public void accept ( final Integer index, final JComponent component )
                        {
                            breadcrumb.setProgressType ( component, BreadcrumbElementData.ProgressType.none );
                            breadcrumb.setProgress ( component, 0.0d );
                        }
                    } );
                }
            } );

            return new WebPanel ( StyleId.panelTransparent, new HorizontalFlowLayout ( 8, false ), determinate, indeterminate, breadcrumb );
        }
    }
}