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

package com.alee.graphics.image.gif;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class GifEncoder - Encodes a GIF file consisting of one or more frames.
 * <p>
 * <pre>
 *  Example:
 *     GifEncoder e = new GifEncoder();
 *     e.start(outputFileName);
 *     e.setDelay(1000);
 *     e.addFrame(image1);
 *     e.addFrame(image2);
 *     e.finish();
 * </pre>
 */

@SuppressWarnings ( "SpellCheckingInspection" )
public class GifEncoder
{
    protected int width; // image size

    protected int height;

    protected Color transparent = null; // transparent color if given

    protected int transIndex; // transparent index in color table

    protected int repeat = -1; // no repeat

    public int delay = 0; // frame delay (hundredths)

    protected boolean started = false; // ready to output frames

    protected OutputStream out;

    protected BufferedImage image; // current frame

    protected byte[] pixels; // BGR byte array from frame

    protected byte[] indexedPixels; // converted frame indexed to palette

    protected int colorDepth; // number of bit planes

    protected byte[] colorTab; // RGB palette

    protected boolean[] usedEntry = new boolean[ 256 ]; // active palette entries

    protected int palSize = 7; // color table size (bits-1)

    protected int dispose = -1; // disposal code (-1 = use default)

    protected boolean closeStream = false; // close stream when finished

    protected boolean firstFrame = true;

    protected boolean sizeSet = false; // if false, get size from first frame

    protected int sample = 10; // default sample interval for quantizer

    /**
     * Sets the delay time between each frame, or changes it for subsequent frames (applies to last
     * frame added).
     *
     * @param ms int delay time in milliseconds
     */
    public void setDelay ( final int ms )
    {
        delay = Math.round ( ms / 10.0f );
    }

    /**
     * Sets the GIF frame disposal code for the last added frame and any subsequent frames. Default
     * is 0 if no transparent color has been set, otherwise 2.
     *
     * @param code int disposal code.
     */
    public void setDispose ( final int code )
    {
        if ( code >= 0 )
        {
            dispose = code;
        }
    }

    /**
     * Sets the number of times the set of GIF frames should be played.
     * Default is 1; 0 means play indefinitely. Must be invoked before the first image is added.
     *
     * @param iter int number of iterations.
     */
    public void setRepeat ( final int iter )
    {
        if ( iter >= 0 )
        {
            repeat = iter;
        }
    }

    /**
     * Sets the transparent color for the last added frame and any subsequent frames. Since all
     * colors are subject to modification in the quantization process, the color in the final
     * palette for each frame closest to the given color becomes the transparent color for that
     * frame. May be set to null to indicate no transparent color.
     *
     * @param c Color to be treated as transparent on display.
     */
    public void setTransparent ( final Color c )
    {
        transparent = c;
    }

    /**
     * Adds next GIF frame. The frame is not written immediately, but is actually deferred until the
     * next frame is received so that timing data can be inserted. Invoking {@code finish()}
     * flushes all frames. If {@code setSize} was not invoked, the size of the first image is
     * used for all subsequent frames.
     *
     * @param im BufferedImage containing frame to write.
     * @return true if successful.
     */
    public boolean addFrame ( final BufferedImage im )
    {
        if ( ( im == null ) || !started )
        {
            return false;
        }
        boolean ok = true;
        try
        {
            if ( !sizeSet )
            {
                // use first frame's size
                setSize ( im.getWidth (), im.getHeight () );
            }
            image = im;
            getImagePixels (); // convert to correct format if necessary
            analyzePixels (); // build color table & map pixels
            if ( firstFrame )
            {
                writeLSD (); // logical screen descriptior
                writePalette (); // global color table
                if ( repeat >= 0 )
                {
                    // use NS app extension to indicate reps
                    writeNetscapeExt ();
                }
            }
            writeGraphicCtrlExt (); // write graphic control extension
            writeImageDesc (); // image descriptor
            if ( !firstFrame )
            {
                writePalette (); // local color table
            }
            writePixels (); // encode and write pixel data
            firstFrame = false;
        }
        catch ( final IOException e )
        {
            ok = false;
        }

        return ok;
    }

    /**
     * Flushes any pending data and closes output file. If writing to an OutputStream, the stream is
     * not closed.
     */
    public boolean finish ()
    {
        if ( !started )
        {
            return false;
        }
        boolean ok = true;
        started = false;
        try
        {
            out.write ( 0x3b ); // gif trailer
            out.flush ();
            if ( closeStream )
            {
                out.close ();
            }
        }
        catch ( final IOException e )
        {
            ok = false;
        }

        // reset for subsequent use
        transIndex = 0;
        out = null;
        image = null;
        pixels = null;
        indexedPixels = null;
        colorTab = null;
        closeStream = false;
        firstFrame = true;

        return ok;
    }

    /**
     * Sets frame rate in frames per second. Equivalent to setDelay(1000/fps).
     *
     * @param fps float frame rate (frames per second)
     */
    public void setFrameRate ( final float fps )
    {
        if ( fps != 0f )
        {
            delay = Math.round ( 100f / fps );
        }
    }

    /**
     * Sets quality of color quantization (conversion of images to the maximum 256 colors allowed by
     * the GIF specification). Lower values (minimum = 1) produce better colors, but slow processing
     * significantly. 10 is the default, and produces good color mapping at reasonable speeds.
     * Values greater than 20 do not yield significant improvements in speed.
     *
     * @param quality int greater than 0.
     */
    public void setQuality ( int quality )
    {
        if ( quality < 1 )
        {
            quality = 1;
        }
        sample = quality;
    }

    /**
     * Sets the GIF frame size. The default size is the size of the first frame added if this method
     * is not invoked.
     *
     * @param w int frame width.
     * @param h int frame width.
     */
    public void setSize ( final int w, final int h )
    {
        if ( started && !firstFrame )
        {
            return;
        }
        width = w;
        height = h;
        if ( width < 1 )
        {
            width = 320;
        }
        if ( height < 1 )
        {
            height = 240;
        }
        sizeSet = true;
    }

    /**
     * Initiates GIF file creation on the given stream. The stream is not closed automatically.
     *
     * @param os OutputStream on which GIF images are written.
     * @return false if initial write failed.
     */
    public boolean start ( final OutputStream os )
    {
        if ( os == null )
        {
            return false;
        }
        boolean ok = true;
        closeStream = false;
        out = os;
        try
        {
            writeString ( "GIF89a" ); // header
        }
        catch ( final IOException e )
        {
            ok = false;
        }
        return started = ok;
    }

    /**
     * Initiates writing of a GIF file with the specified name.
     *
     * @param file String containing output file name.
     * @return false if open or initial write failed.
     */
    public boolean start ( final String file )
    {
        boolean ok;
        try
        {
            out = new BufferedOutputStream ( new FileOutputStream ( file ) );
            ok = start ( out );
            closeStream = true;
        }
        catch ( final IOException e )
        {
            ok = false;
        }
        return started = ok;
    }

    /**
     * Analyzes image colors and creates color map.
     */
    protected void analyzePixels ()
    {
        final int len = pixels.length;
        final int nPix = len / 3;
        indexedPixels = new byte[ nPix ];
        final NeuQuant nq = new NeuQuant ( pixels, len, sample );
        // initialize quantizer
        colorTab = nq.process (); // create reduced palette
        // convert map from BGR to RGB
        for ( int i = 0; i < colorTab.length; i += 3 )
        {
            final byte temp = colorTab[ i ];
            colorTab[ i ] = colorTab[ i + 2 ];
            colorTab[ i + 2 ] = temp;
            usedEntry[ i / 3 ] = false;
        }
        // map image pixels to new palette
        int k = 0;
        for ( int i = 0; i < nPix; i++ )
        {
            final int index = nq.map ( pixels[ k++ ] & 0xff, pixels[ k++ ] & 0xff, pixels[ k++ ] & 0xff );
            usedEntry[ index ] = true;
            indexedPixels[ i ] = ( byte ) index;
        }
        pixels = null;
        colorDepth = 8;
        palSize = 7;
        // get closest match to transparent color if specified
        if ( transparent != null )
        {
            transIndex = findClosest ( transparent );
        }
    }

    /**
     * Returns index of palette color closest to c
     */
    protected int findClosest ( final Color c )
    {
        if ( colorTab == null )
        {
            return -1;
        }
        final int r = c.getRed ();
        final int g = c.getGreen ();
        final int b = c.getBlue ();
        int minpos = 0;
        int dmin = 256 * 256 * 256;
        final int len = colorTab.length;
        for ( int i = 0; i < len; )
        {
            final int dr = r - ( colorTab[ i++ ] & 0xff );
            final int dg = g - ( colorTab[ i++ ] & 0xff );
            final int db = b - ( colorTab[ i ] & 0xff );
            final int d = dr * dr + dg * dg + db * db;
            final int index = i / 3;
            if ( usedEntry[ index ] && ( d < dmin ) )
            {
                dmin = d;
                minpos = index;
            }
            i++;
        }
        return minpos;
    }

    /**
     * Extracts image pixels into byte array "pixels"
     */
    protected void getImagePixels ()
    {
        final int w = image.getWidth ();
        final int h = image.getHeight ();
        final int type = image.getType ();
        if ( ( w != width ) || ( h != height ) || ( type != BufferedImage.TYPE_3BYTE_BGR ) )
        {
            // create new image with right size/format
            final BufferedImage temp = new BufferedImage ( width, height, BufferedImage.TYPE_3BYTE_BGR );
            final Graphics2D g = temp.createGraphics ();
            g.drawImage ( image, 0, 0, null );
            g.dispose ();

            //            // clear "transparent" pixels
            //            for ( int i = 0; i < image.getWidth (); i++ )
            //            {
            //                for ( int j = 0; j < image.getHeight (); j++ )
            //                {
            //                    Color color = new Color ( image.getRGB ( i, j ), true );
            //                    if ( color.getAlpha () == 0 )
            //                    {
            //                        temp.getRaster ().setDataElements ( i, j, new byte[]{ 0,0,0 } );
            //                    }
            //                }
            //            }

            image = temp;
        }
        pixels = ( ( DataBufferByte ) image.getRaster ().getDataBuffer () ).getData ();
    }

    /**
     * Writes Graphic Control Extension
     */
    protected void writeGraphicCtrlExt () throws IOException
    {
        out.write ( 0x21 ); // extension introducer
        out.write ( 0xf9 ); // GCE label
        out.write ( 4 ); // data block size
        final int transp;
        int disp;
        if ( transparent == null )
        {
            transp = 0;
            disp = 0; // dispose = no action
        }
        else
        {
            transp = 1;
            disp = 2; // force clear if using transparent color
        }
        if ( dispose >= 0 )
        {
            disp = dispose & 7; // user override
        }
        disp <<= 2;

        // packed fields
        out.write ( disp | transp ); // 8 transparency flag

        writeShort ( delay ); // delay x 1/100 sec
        out.write ( transIndex ); // transparent color index
        out.write ( 0 ); // block terminator
    }

    /**
     * Writes Image Descriptor
     */
    protected void writeImageDesc () throws IOException
    {
        out.write ( 0x2c ); // image separator
        writeShort ( 0 ); // image position x,y = 0,0
        writeShort ( 0 );
        writeShort ( width ); // image size
        writeShort ( height );
        // packed fields
        if ( firstFrame )
        {
            // no LCT - GCT is used for first (or only) frame
            out.write ( 0 );
        }
        else
        {
            // specify normal LCT
            out.write ( 0x80 | palSize ); // 6-8 size of color table
        }
    }

    /**
     * Writes Logical Screen Descriptor
     */
    protected void writeLSD () throws IOException
    {
        // logical screen size
        writeShort ( width );
        writeShort ( height );
        // packed fields
        out.write ( 0x80 | 0x70 | palSize ); // 6-8 : gct size

        out.write ( 0 ); // background color index
        out.write ( 0 ); // pixel aspect ratio - assume 1:1
    }

    /**
     * Writes Netscape application extension to define repeat count.
     */
    protected void writeNetscapeExt () throws IOException
    {
        out.write ( 0x21 ); // extension introducer
        out.write ( 0xff ); // app extension label
        out.write ( 11 ); // block size
        writeString ( "NETSCAPE" + "2.0" ); // app id + auth code
        out.write ( 3 ); // sub-block size
        out.write ( 1 ); // loop sub-block id
        writeShort ( repeat ); // loop count (extra iterations, 0=repeat forever)
        out.write ( 0 ); // block terminator
    }

    /**
     * Writes color table
     */
    protected void writePalette () throws IOException
    {
        out.write ( colorTab, 0, colorTab.length );
        final int n = ( 3 * 256 ) - colorTab.length;
        for ( int i = 0; i < n; i++ )
        {
            out.write ( 0 );
        }
    }

    /**
     * Encodes and writes pixel data
     */
    protected void writePixels () throws IOException
    {
        final LZWEncoder encoder = new LZWEncoder ( width, height, indexedPixels, colorDepth );
        encoder.encode ( out );
    }

    /**
     * Write 16-bit value to output stream, LSB first
     */
    protected void writeShort ( final int value ) throws IOException
    {
        out.write ( value & 0xff );
        out.write ( ( value >> 8 ) & 0xff );
    }

    /**
     * Writes string to output stream
     */
    protected void writeString ( final String s ) throws IOException
    {
        for ( int i = 0; i < s.length (); i++ )
        {
            out.write ( ( byte ) s.charAt ( i ) );
        }
    }
}