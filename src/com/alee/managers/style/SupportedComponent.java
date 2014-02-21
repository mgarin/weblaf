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

package com.alee.managers.style;

import com.alee.extended.button.WebSplitButton;
import com.alee.extended.button.WebSplitButtonUI;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.checkbox.WebTristateCheckBoxUI;
import com.alee.extended.label.WebMultiLineLabel;
import com.alee.extended.label.WebMultiLineLabelUI;
import com.alee.extended.label.WebVerticalLabel;
import com.alee.extended.label.WebVerticalLabelUI;
import com.alee.laf.button.WebButtonUI;
import com.alee.laf.button.WebToggleButtonUI;
import com.alee.laf.checkbox.WebCheckBoxUI;
import com.alee.laf.colorchooser.WebColorChooserUI;
import com.alee.laf.combobox.WebComboBoxUI;
import com.alee.laf.desktoppane.WebDesktopIconUI;
import com.alee.laf.desktoppane.WebDesktopPaneUI;
import com.alee.laf.desktoppane.WebInternalFrameUI;
import com.alee.laf.filechooser.WebFileChooserUI;
import com.alee.laf.label.WebLabelUI;
import com.alee.laf.list.WebListUI;
import com.alee.laf.menu.*;
import com.alee.laf.optionpane.WebOptionPaneUI;
import com.alee.laf.panel.WebPanelUI;
import com.alee.laf.progressbar.WebProgressBarUI;
import com.alee.laf.radiobutton.WebRadioButtonUI;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.laf.scroll.WebScrollBarUI;
import com.alee.laf.scroll.WebScrollPaneUI;
import com.alee.laf.separator.WebSeparatorUI;
import com.alee.laf.slider.WebSliderUI;
import com.alee.laf.spinner.WebSpinnerUI;
import com.alee.laf.splitpane.WebSplitPaneUI;
import com.alee.laf.tabbedpane.WebTabbedPaneUI;
import com.alee.laf.table.WebTableHeaderUI;
import com.alee.laf.table.WebTableUI;
import com.alee.laf.text.*;
import com.alee.laf.toolbar.WebToolBarSeparatorUI;
import com.alee.laf.toolbar.WebToolBarUI;
import com.alee.laf.tooltip.WebToolTipUI;
import com.alee.laf.tree.WebTreeUI;
import com.alee.laf.viewport.WebViewportUI;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.Styleable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.table.JTableHeader;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * This enumeration represents list of supported components.
 * It also contains some default references and useful settings.
 *
 * @author Mikle Garin
 * @see com.alee.laf.WebLookAndFeel
 */

public enum SupportedComponent
{
    /**
     * Label-related components.
     */
    label
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JLabel.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "LabelUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLabelUI.class.getCanonicalName ();
                }
            },
    verticalLabel
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return WebVerticalLabel.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "VerticalLabelUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebVerticalLabelUI.class.getCanonicalName ();
                }
            },
    multiLineLabel
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return WebMultiLineLabel.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "MultiLineLabelUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebMultiLineLabelUI.class.getCanonicalName ();
                }
            },
    toolTip
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JToolTip.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ToolTipUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebToolTipUI.class.getCanonicalName ();
                }
            },

    /**
     * Button-related components.
     */
    button
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JButton.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ButtonUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebButtonUI.class.getCanonicalName ();
                }
            },
    splitButton
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return WebSplitButton.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "SplitButtonUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebSplitButtonUI.class.getCanonicalName ();
                }
            },
    toggleButton
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JToggleButton.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ToggleButtonUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebToggleButtonUI.class.getCanonicalName ();
                }
            },
    checkBox
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JCheckBox.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "CheckBoxUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebCheckBoxUI.class.getCanonicalName ();
                }
            },
    tristateCheckBox
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return WebTristateCheckBox.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TristateCheckBoxUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebTristateCheckBoxUI.class.getCanonicalName ();
                }
            },
    radioButton
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JRadioButton.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "RadioButtonUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebRadioButtonUI.class.getCanonicalName ();
                }
            },

    /**
     * Menu-related components.
     */
    menuBar
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JMenuBar.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "MenuBarUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebMenuBarUI.class.getCanonicalName ();
                }
            },
    menu
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JMenu.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "MenuUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebMenuUI.class.getCanonicalName ();
                }
            },
    popupMenu
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JPopupMenu.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "PopupMenuUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebPopupMenuUI.class.getCanonicalName ();
                }
            },
    menuItem
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JMenuItem.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "MenuItemUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebMenuItemUI.class.getCanonicalName ();
                }
            },
    checkBoxMenuItem
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JCheckBoxMenuItem.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "CheckBoxMenuItemUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebCheckBoxMenuItemUI.class.getCanonicalName ();
                }
            },
    radioButtonMenuItem
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JRadioButtonMenuItem.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "RadioButtonMenuItemUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebRadioButtonMenuItemUI.class.getCanonicalName ();
                }
            },
    popupMenuSeparator
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JPopupMenu.Separator.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "PopupMenuSeparatorUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebPopupMenuSeparatorUI.class.getCanonicalName ();
                }
            },

    /**
     * Separator component.
     */
    separator
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JSeparator.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "SeparatorUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebSeparatorUI.class.getCanonicalName ();
                }
            },

    /**
     * Scroll-related components.
     */
    scrollBar
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JScrollBar.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ScrollBarUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebScrollBarUI.class.getCanonicalName ();
                }
            },
    scrollPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JScrollPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ScrollPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebScrollPaneUI.class.getCanonicalName ();
                }
            },
    viewport
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JViewport.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ViewportUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebViewportUI.class.getCanonicalName ();
                }
            },

    /**
     * Text-related components.
     */
    textField
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTextField.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TextFieldUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebTextFieldUI.class.getCanonicalName ();
                }
            },
    passwordField
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JPasswordField.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "PasswordFieldUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebPasswordFieldUI.class.getCanonicalName ();
                }
            },
    formattedTextField
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JFormattedTextField.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "FormattedTextFieldUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebFormattedTextFieldUI.class.getCanonicalName ();
                }
            },
    textArea
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTextArea.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TextAreaUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebTextAreaUI.class.getCanonicalName ();
                }
            },
    editorPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JEditorPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "EditorPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebEditorPaneUI.class.getCanonicalName ();
                }
            },
    textPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTextPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TextPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebTextPaneUI.class.getCanonicalName ();
                }
            },

    /**
     * Toolbar-related components.
     */
    toolBar
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JToolBar.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ToolBarUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebToolBarUI.class.getCanonicalName ();
                }
            },
    toolBarSeparator
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JToolBar.Separator.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ToolBarSeparatorUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebToolBarSeparatorUI.class.getCanonicalName ();
                }
            },

    /**
     * Table-related components.
     */
    table
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTable.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TableUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebTableUI.class.getCanonicalName ();
                }
            },
    tableHeader
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTableHeader.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TableHeaderUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebTableHeaderUI.class.getCanonicalName ();
                }
            },

    /**
     * Chooser components.
     */
    colorChooser
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JColorChooser.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ColorChooserUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebColorChooserUI.class.getCanonicalName ();
                }
            },
    fileChooser
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JFileChooser.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "FileChooserUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebFileChooserUI.class.getCanonicalName ();
                }
            },

    /**
     * Container-related components.
     */
    panel
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JPanel.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "PanelUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebPanelUI.class.getCanonicalName ();
                }
            },
    rootPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JRootPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "RootPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebRootPaneUI.class.getCanonicalName ();
                }
            },
    tabbedPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTabbedPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TabbedPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebTabbedPaneUI.class.getCanonicalName ();
                }
            },
    splitPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JSplitPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "SplitPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebSplitPaneUI.class.getCanonicalName ();
                }
            },

    /**
     * Other data-related components.
     */
    progressBar
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JProgressBar.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ProgressBarUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebProgressBarUI.class.getCanonicalName ();
                }
            },
    slider
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JSlider.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "SliderUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebSliderUI.class.getCanonicalName ();
                }
            },
    spinner
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JSpinner.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "SpinnerUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebSpinnerUI.class.getCanonicalName ();
                }
            },
    tree
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTree.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TreeUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebTreeUI.class.getCanonicalName ();
                }
            },
    list
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JList.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ListUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebListUI.class.getCanonicalName ();
                }
            },
    comboBox
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JComboBox.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ComboBoxUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebComboBoxUI.class.getCanonicalName ();
                }
            },

    /**
     * Desktop-pane-related components.
     */
    desktopPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JDesktopPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "DesktopPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebDesktopPaneUI.class.getCanonicalName ();
                }
            },
    desktopIcon
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JInternalFrame.JDesktopIcon.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "DesktopIconUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebDesktopIconUI.class.getCanonicalName ();
                }
            },
    internalFrame
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JInternalFrame.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "InternalFrameUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebInternalFrameUI.class.getCanonicalName ();
                }
            },

    /**
     * Option pane component.
     */
    optionPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JOptionPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "OptionPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebOptionPaneUI.class.getCanonicalName ();
                }
            };

    /**
     * Returns component class for this component type.
     *
     * @return component class for this component type
     */
    public abstract Class<? extends JComponent> getComponentClass ();

    /**
     * Returns UI class ID used by LookAndFeel to store various settings.
     *
     * @return UI class ID
     */
    public abstract String getUIClassID ();

    /**
     * Returns default UI class canonical name.
     * This value is used in WebLookAndFeel to provide default UI classes.
     * However they can be reassigned before WebLookAndFeel installation.
     *
     * @return default UI class canonical name
     */
    public abstract String getDefaultUIClass ();

    /**
     * Returns style identifier for the specified component.
     * This identifier might be customized in component to force StyleManager provide another style for that specific component.
     *
     * @return component identifier used within style in skin descriptor
     */
    public String getComponentStyleId ( final JComponent component )
    {
        final Styleable styleable = LafUtils.getStyleable ( component );
        return styleable != null ? styleable.getStyleId () : null;
    }

    /**
     * Lazily initialized component UI class map by their types.
     */
    private static final Map<SupportedComponent, Class> componentUIClassByUIClassID =
            new EnumMap<SupportedComponent, Class> ( SupportedComponent.class );

    /**
     * Returns UI class for this component type.
     *
     * @return UI class for this component type
     */
    public Class<? extends ComponentUI> getUIClass ()
    {
        Class type = componentUIClassByUIClassID.get ( this );
        if ( type == null )
        {
            type = ReflectUtils.getClassSafely ( UIManager.getString ( getUIClassID () ) );
            componentUIClassByUIClassID.put ( this, type );
        }
        return type;
    }

    /**
     * Component type icons cache.
     */
    private static final Map<SupportedComponent, ImageIcon> componentIcons =
            new EnumMap<SupportedComponent, ImageIcon> ( SupportedComponent.class );

    /**
     * Returns component type icon.
     *
     * @return component type icon
     */
    public ImageIcon getIcon ()
    {
        ImageIcon icon = componentIcons.get ( this );
        if ( icon == null )
        {
            icon = new ImageIcon ( SupportedComponent.class.getResource ( "icons/component/" + this + ".png" ) );
            componentIcons.put ( this, icon );
        }
        return icon;
    }

    /**
     * Lazily initialized component types map by their UI class IDs.
     */
    private static final Map<String, SupportedComponent> componentByUIClassID =
            new HashMap<String, SupportedComponent> ( values ().length );

    /**
     * Returns supported component type by UI class ID.
     *
     * @param uiClassID UI class ID
     * @return supported component type by UI class ID
     */
    public static SupportedComponent getComponentTypeByUIClassID ( final String uiClassID )
    {
        if ( componentByUIClassID.size () == 0 )
        {
            for ( final SupportedComponent supportedComponent : values () )
            {
                componentByUIClassID.put ( supportedComponent.getUIClassID (), supportedComponent );
            }
        }
        return componentByUIClassID.get ( uiClassID );
    }
}