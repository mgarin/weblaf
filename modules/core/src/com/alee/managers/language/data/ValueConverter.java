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

package com.alee.managers.language.data;

import com.alee.utils.CollectionUtils;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class ValueConverter implements Converter
{
    // Attribute names
    private static final String LANGUAGE = "lang";
    private static final String MNEMONIC = "mnemonic";
    private static final String HOTKEY = "hotkey";
    private static final String STATE = "state";
    private static final String TIP_TYPE = "type";
    private static final String TIP_WAY = "way";
    private static final String TIP_DELAY = "delay";

    // Special key that is used to determine tooltips
    public static final String TOOLTIP_KEY = "tooltip";

    @Override
    public boolean canConvert ( final Class type )
    {
        return Value.class.getCanonicalName ().equals ( type.getCanonicalName () );
    }

    @Override
    public void marshal ( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final Value value = ( Value ) source;

        // Adding language
        if ( value.getLang () != null )
        {
            writer.addAttribute ( LANGUAGE, value.getLang () );
        }

        // Adding mnemonic
        if ( value.getMnemonic () != null )
        {
            writer.addAttribute ( MNEMONIC, value.getMnemonic ().toString () );
        }

        // Adding hotkey
        if ( value.getHotkey () != null )
        {
            writer.addAttribute ( HOTKEY, value.getHotkey () );
        }

        // Adding either single or multiply values
        if ( value.getTexts () != null && value.getTexts ().size () == 1 &&
                ( value.getTooltips () == null || value.getTooltips ().size () == 0 ) )
        {
            final Text text = value.getTextObject ( 0 );

            // Adding state attribute if needed
            if ( text.getState () != null )
            {
                writer.addAttribute ( STATE, text.getState () );
            }

            // Adding value
            writer.setValue ( text.getText () );
        }
        else if ( value.getTooltips () != null && value.getTooltips ().size () == 1 &&
                ( value.getTexts () == null || value.getTexts ().size () == 0 ) )
        {
            final Tooltip tooltip = value.getTooltipObject ( 0 );

            // Adding tooltip way attribute if needed
            if ( TooltipConverter.shouldWriteDelay ( tooltip ) )
            {
                writer.addAttribute ( TIP_DELAY, tooltip.getDelay ().toString () );
            }

            // Adding tooltip way attribute if needed
            if ( TooltipConverter.shouldWriteWay ( tooltip ) )
            {
                writer.addAttribute ( TIP_WAY, tooltip.getWay ().toString () );
            }

            // Adding tooltip type attribute if needed
            if ( TooltipConverter.shouldWriteType ( tooltip ) )
            {
                writer.addAttribute ( TIP_TYPE, tooltip.getType ().toString () );
            }

            // Adding type attribute to define that this will be a tooltip
            writer.addAttribute ( STATE, TOOLTIP_KEY );

            // Adding value
            writer.setValue ( tooltip.getText () );
        }
        else if ( value.getTexts () != null && value.getTexts ().size () > 0 ||
                value.getTooltips () != null && value.getTooltips ().size () > 0 )
        {
            // Adding values
            context.convertAnother ( value.getTexts () );

            // Adding tooltips
            context.convertAnother ( value.getTooltips () );
        }
        else
        {
            // Empty value, generally this should never happen
            writer.setValue ( "" );
        }
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        final Value value = new Value ();

        // Reading language
        value.setLang ( reader.getAttribute ( LANGUAGE ) );

        // Reading mnemonic
        final String mnemonicValue = reader.getAttribute ( MNEMONIC );
        value.setMnemonic ( mnemonicValue != null ? mnemonicValue.charAt ( 0 ) : null );

        // Reading hotkey
        value.setHotkey ( reader.getAttribute ( HOTKEY ) );

        // Reading possible single-value case attributes
        final String state = reader.getAttribute ( STATE );
        final TooltipType tipType = TooltipConverter.parseType ( reader.getAttribute ( TIP_TYPE ) );
        final TooltipWay tipWay = TooltipConverter.parseWay ( reader.getAttribute ( TIP_WAY ) );
        final Integer tipDelay = TooltipConverter.parseDelay ( reader.getAttribute ( TIP_DELAY ) );

        // Reading texts and tooltips
        final String text = reader.getValue ();
        final List<Text> texts = new ArrayList<Text> ();
        final List<Tooltip> tooltips = new ArrayList<Tooltip> ();
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();
            if ( reader.getNodeName ().equals ( "text" ) )
            {
                texts.add ( ( Text ) context.convertAnother ( value, Text.class ) );
            }
            else if ( reader.getNodeName ().equals ( "tooltip" ) )
            {
                tooltips.add ( ( Tooltip ) context.convertAnother ( value, Tooltip.class ) );
            }
            reader.moveUp ();
        }

        // Determining what should we save
        if ( texts.size () == 0 && tooltips.size () == 0 )
        {
            // Saving either single text or tooltip
            if ( state != null && state.equals ( TOOLTIP_KEY ) )
            {
                value.setTooltips ( CollectionUtils.asList ( new Tooltip ( tipType, tipWay, tipDelay, text ) ) );
                value.setTexts ( null );
            }
            else
            {
                value.setTexts ( CollectionUtils.asList ( new Text ( text, state ) ) );
                value.setTooltips ( null );
            }
        }
        else
        {
            // Saving multiply texts and tooltips
            value.setTexts ( texts.size () > 0 ? texts : null );
            value.setTooltips ( tooltips.size () > 0 ? tooltips : null );
        }

        return value;
    }
}