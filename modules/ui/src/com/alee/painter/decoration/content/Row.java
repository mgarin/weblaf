package com.alee.painter.decoration.content;

import com.alee.extended.label.StyleRange;
import com.alee.extended.label.TextRange;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Styled text row data.
 *
 * @author Alexandr Zernov
 */
public final class Row
{
    /**
     * List of styled text fragments.
     */
    public List<TextRange> fragments = new ArrayList<TextRange> ();

    /**
     * Row width.
     */
    public int width = 0;

    /**
     * Row height.
     */
    public int height = 0;

    /**
     * Mnemonic index.
     */
    public int mnemonic = -1;

    /**
     * Constructs row with the specified height.
     *
     * @param height row height
     */
    public Row ( final int height )
    {
        this.height = height;
    }

    /**
     * Appends the row.
     *
     * @param s             text fragment
     * @param style         fragment style
     * @param fm            font metrics
     * @param charOffset    fragment total offset in chars
     * @param mnemonicIndex mnemonic index
     * @return width of appended fragment
     */
    public int append ( final String s, final StyleRange style, final FontMetrics fm, final int charOffset, final int mnemonicIndex )
    {
        int sWidth = 0;
        if ( !s.isEmpty () )
        {
            if ( mnemonicIndex >= 0 && s.length () > mnemonicIndex - charOffset )
            {
                mnemonic = mnemonicIndex - charOffset;
                for ( final TextRange fragment : fragments )
                {
                    mnemonic += fragment.text.length ();
                }
            }

            if ( fragments.isEmpty () )
            {
                // Trimming left first fragment
                final int fw = AbstractStyledTextContent.findFirstWordFromIndex ( s, 0 );
                if ( fw >= 0 )
                {
                    final String trimmed = s.substring ( fw, s.length () );
                    fragments.add ( new TextRange ( trimmed, style ) );
                    if ( mnemonic > 0 )
                    {
                        mnemonic -= fw;
                    }

                    sWidth += fm.stringWidth ( trimmed );
                }
            }
            else
            {
                fragments.add ( new TextRange ( s, style ) );
                sWidth += fm.stringWidth ( s );
            }
        }

        width += sWidth;

        return sWidth;
    }

    /**
     * Returns whether the row is empty or not.
     *
     * @return true if  the row is empty, false otherwise
     */
    public boolean isEmpty ()
    {
        return fragments.isEmpty ();
    }
}
