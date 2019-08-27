package com.alee.laf.combobox;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageListener;
import com.alee.managers.language.LanguageSensitive;
import com.alee.managers.language.UILanguageManager;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.swing.EditabilityListener;
import com.alee.utils.swing.VisibilityListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Basic painter for {@link JComboBox} component.
 * It is used as {@link WebComboBoxUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class ComboBoxPainter<C extends JComboBox, U extends WComboBoxUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements IComboBoxPainter<C, U>, EditabilityListener, VisibilityListener
{
    /**
     * Listeners.
     */
    protected transient LanguageListener languageSensitive;

    /**
     * Painting variables.
     */
    protected transient CellRendererPane currentValuePane = null;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installComboBoxListeners ();
        installLanguageListeners ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallLanguageListeners ();
        uninstallComboBoxListeners ();
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating combobox popup list state
        // This is a workaround to allow box renderer properly inherit enabled state
        if ( Objects.equals ( property, WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            ui.getListBox ().setEnabled ( component.isEnabled () );
        }
    }

    /**
     * Installs combobox listeners that update decoration states.
     */
    protected void installComboBoxListeners ()
    {
        ui.addEditabilityListener ( this );
        ui.addPopupVisibilityListener ( this );
    }

    @Override
    public void editabilityChanged ( final boolean editable )
    {
        updateDecorationState ();
    }

    @Override
    public void visibilityChanged ( final boolean visible )
    {
        updateDecorationState ();
    }

    /**
     * Uninstalls combobox listeners that update decoration states.
     */
    protected void uninstallComboBoxListeners ()
    {
        ui.removePopupVisibilityListener ( this );
        ui.removeEditabilityListener ( this );
    }

    /**
     * Installs language listeners.
     */
    protected void installLanguageListeners ()
    {
        languageSensitive = new LanguageListener ()
        {
            @Override
            public void languageChanged ( @NotNull final Language oldLanguage, @NotNull final Language newLanguage )
            {
                if ( isLanguageSensitive () )
                {
                    // Updating sizes according to renderer changes
                    ui.updateRendererSize ();
                }
            }
        };
        UILanguageManager.addLanguageListener ( component, languageSensitive );
    }

    /**
     * Returns whether or not table is language-sensitive.
     *
     * @return {@code true} if table is language-sensitive, {@code false} otherwise
     */
    protected boolean isLanguageSensitive ()
    {
        boolean sensitive = false;
        if ( component instanceof LanguageSensitive ||
                component.getRenderer () instanceof LanguageSensitive )
        {
            // Either table header or its default renderer is language-sensitive
            sensitive = true;
        }
        else
        {
            // Checking existing combobox items for being language-sensitive
            final ListModel model = component.getModel ();
            for ( int i = 0; i < model.getSize (); i++ )
            {
                if ( model.getElementAt ( i ) instanceof LanguageSensitive )
                {
                    sensitive = true;
                    break;
                }
            }
        }
        return sensitive;
    }

    /**
     * Uninstalls language listeners.
     */
    protected void uninstallLanguageListeners ()
    {
        UILanguageManager.removeLanguageListener ( component, languageSensitive );
        languageSensitive = null;
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.isEditable () )
        {
            states.add ( DecorationState.editable );
        }
        states.add ( ui.isPopupVisible ( component ) ? DecorationState.expanded : DecorationState.collapsed );
        return states;
    }

    @Override
    public void prepareToPaint ( @NotNull final CellRendererPane currentValuePane )
    {
        this.currentValuePane = currentValuePane;
    }

    @Override
    protected void paintContent ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Rectangle bounds )
    {
        // Selected non-editable value
        paintCurrentValue ( g2d, ui.getValueBounds () );

        // Cleaning up paint variables
        cleanupAfterPaint ();
    }

    /**
     * Method called when single paint operation is completed.
     */
    protected void cleanupAfterPaint ()
    {
        this.currentValuePane = null;
    }

    /**
     * Paints the currently selected item.
     *
     * @param g2d    graphics context
     * @param bounds bounds
     */
    protected void paintCurrentValue ( @NotNull final Graphics2D g2d, @NotNull final Rectangle bounds )
    {
        if ( !component.isEditable () )
        {
            // Retrieving configured renderer
            final JList list = ui.getListBox ();
            final Object value = component.getSelectedItem ();
            final boolean selected = !component.isPopupVisible ();
            final boolean isFocused = isFocused ();
            final ListCellRenderer renderer = component.getRenderer ();
            final Component c = renderer.getListCellRendererComponent ( list, value, -1, selected, isFocused );

            // Updating combobox-related renderer settings
            c.setFont ( component.getFont () );

            // Painting current value
            final int x = bounds.x;
            final int y = bounds.y;
            final int w = bounds.width;
            final int h = bounds.height;
            final boolean shouldValidate = c instanceof JPanel;
            currentValuePane.paintComponent ( g2d, c, component, x, y, w, h, shouldValidate );
        }
    }
}