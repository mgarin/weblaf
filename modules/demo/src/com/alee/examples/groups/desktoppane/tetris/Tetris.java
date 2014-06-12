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

package com.alee.examples.groups.desktoppane.tetris;

import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.CollectionUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;

/**
 * User: mgarin Date: 02.06.11 Time: 17:09
 */

public class Tetris extends JComponent
{
    public static final int LINES_TO_CHANGE_STAGE = 10;
    public static final int MAX_STAGE = 15;

    public static final int BLOCK_SIDE = 24;

    public static final int BLOCKS_IN_WIDTH = 10;
    public static final int BLOCKS_IN_HEIGHT = 15;

    public static final int TICK_TIME = 850;
    public static final int TICK_TIME_DECREASE = 50;

    public static final Dimension PREFERRED_SIZE =
            new Dimension ( BLOCKS_IN_WIDTH * BLOCK_SIDE + BLOCKS_IN_WIDTH + 1, BLOCKS_IN_HEIGHT * BLOCK_SIDE + BLOCKS_IN_HEIGHT + 1 );

    private static final Color bgTop = Color.WHITE;
    private static final Color bgBottom = new Color ( 223, 223, 223 );

    private static final List<TickListener> tickListeners = new ArrayList<TickListener> ( 1 );
    private static final List<TetrisListener> tetrisListeners = new ArrayList<TetrisListener> ( 1 );

    // Game state variables
    private boolean gameRunning = false;
    private boolean gameOver = false;
    private int stage = 1;
    private int linesDestroyed = 0;
    private int score = 0;

    // Figures queue
    private final List<Figure> figuresQueue = new ArrayList<Figure> ();

    // Fallen figure blocks
    private final Map<Point, Block> terrain = new HashMap<Point, Block> ();

    // Game time counter
    private final WebTimer pusher;

    // Current falling figure
    private Figure currentFigure = null;

    // Keyboard events
    private Action action = null;
    private final WebTimer keyTimer;

    // Next figure field
    private final JComponent nextFigureField;

    // Use internal hotkeys or not
    private boolean useInternalHotkeys = true;

    public Tetris ()
    {
        super ();
        SwingUtils.setOrientation ( this );
        setOpaque ( false );
        setFocusable ( true );

        figuresQueue.add ( new Figure () );

        keyTimer = new WebTimer ( "Tetris.keyTimer", 60, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( gameRunning )
                {
                    if ( action.equals ( Action.down ) )
                    {
                        performOneTick ();
                        repaint ();
                    }
                }
            }
        } );
        keyTimer.setInitialDelay ( 0 );

        pusher = new WebTimer ( "Tetris.gameTimer", getTickTime (), null )
        {
            @Override
            public WebTimer start ()
            {
                gameRunning = true;
                return super.start ();
            }

            @Override
            public WebTimer stop ()
            {
                gameRunning = false;
                return super.stop ();
            }
        };
        pusher.setInitialDelay ( 0 );
        pusher.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( keyTimer.isRunning () )
                {
                    return;
                }
                performOneTick ();
            }
        } );

        addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyPressed ( final KeyEvent e )
            {
                if ( gameRunning && currentFigure != null && !keyTimer.isRunning () )
                {
                    if ( Hotkey.SPACE.isTriggered ( e ) || Hotkey.UP.isTriggered ( e ) )
                    {
                        currentFigure.rotate ( getTerrainShape () );
                        repaint ();
                    }
                    else if ( Hotkey.LEFT.isTriggered ( e ) )
                    {
                        currentFigure.moveLeft ( getTerrainShape () );
                        repaint ();
                    }
                    else if ( Hotkey.RIGHT.isTriggered ( e ) )
                    {
                        currentFigure.moveRight ( getTerrainShape () );
                        repaint ();
                    }
                    else if ( Hotkey.DOWN.isTriggered ( e ) )
                    {
                        action = Action.down;
                        keyTimer.start ();
                    }
                }
            }

            @Override
            public void keyReleased ( final KeyEvent e )
            {
                if ( useInternalHotkeys && Hotkey.P.isTriggered ( e ) )
                {
                    if ( gameRunning )
                    {
                        pauseGame ();
                    }
                    else
                    {
                        unpauseGame ();
                    }
                }
                else if ( useInternalHotkeys && Hotkey.F2.isTriggered ( e ) )
                {
                    newGame ();
                }
                else if ( keyTimer.isRunning () )
                {
                    if ( Hotkey.DOWN.isTriggered ( e ) && action == Action.down )
                    {
                        keyTimer.stop ();
                    }
                    if ( !keyTimer.isRunning () )
                    {
                        action = null;
                    }
                }
            }
        } );

        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                requestFocusInWindow ();
            }
        } );

        nextFigureField = new JComponent ()
        {
            {
                setPreferredSize ( new Dimension ( 5 * BLOCK_SIDE + 5, 4 * BLOCK_SIDE + 4 ) );
                addTickListener ( new TickListener ()
                {
                    @Override
                    public void tick ()
                    {
                        repaint ();
                    }
                } );
            }

            @Override
            protected void paintComponent ( final Graphics g )
            {
                final Graphics2D g2d = ( Graphics2D ) g;
                final Object aa = GraphicsUtils.setupAntialias ( g2d );

                g2d.setPaint ( Color.WHITE );
                g2d.fillRect ( 0, 0, getWidth (), getHeight () );

                final Stroke old = g2d.getStroke ();
                g2d.setStroke ( borderStroke );
                g2d.setPaint ( Color.LIGHT_GRAY );
                g2d.drawRect ( 0, 0, getWidth () - 1, getHeight () - 1 );
                g2d.setStroke ( old );

                getNextFigure ().paintFigurePreview ( g2d, this );

                GraphicsUtils.restoreAntialias ( g2d, aa );
            }
        };
    }

    public boolean isUseInternalHotkeys ()
    {
        return useInternalHotkeys;
    }

    public void setUseInternalHotkeys ( final boolean useInternalHotkeys )
    {
        this.useInternalHotkeys = useInternalHotkeys;
    }

    public void newGame ()
    {
        gameOver = false;
        stage = 1;
        linesDestroyed = 0;
        score = 0;
        terrain.clear ();
        currentFigure = null;
        repaint ();

        pusher.start ();

        // Informing about new game start
        fireNewGameStarted ();
    }

    public boolean isPaused ()
    {
        return !gameRunning;
    }

    public void pauseGame ()
    {
        if ( !gameOver )
        {
            pusher.stop ();
            repaint ();

            // Informing about pause
            fireGamePaused ();
        }
    }

    public void unpauseGame ()
    {
        if ( !gameOver )
        {
            pusher.start ();
            repaint ();

            // Informing about pause end
            fireGameUnpaused ();
        }
    }

    private enum Action
    {
        rotate,
        left,
        right,
        down
    }

    private int getTickTime ()
    {
        return TICK_TIME - stage * TICK_TIME_DECREASE;
    }

    private void performOneTick ()
    {
        // If there is no current falling figure adding next one
        final boolean cantMoveFuther = currentFigure != null && !currentFigure.canMoveDown ( getTerrainShape () );
        if ( currentFigure == null || cantMoveFuther )
        {
            if ( cantMoveFuther )
            {
                // Disassembling figure
                terrain.putAll ( currentFigure.getBlocks () );

                // Determining blocks amount in each level
                final List<Integer> levels = new ArrayList<Integer> ();
                final Map<Integer, Integer> blocksPerLevel = new HashMap<Integer, Integer> ();
                for ( final Point point : terrain.keySet () )
                {
                    final Block block = terrain.get ( point );

                    final int level = block.getBlockPoint ().y;
                    if ( !levels.contains ( level ) )
                    {
                        levels.add ( level );
                    }

                    blocksPerLevel.put ( level, blocksPerLevel.containsKey ( level ) ? blocksPerLevel.get ( level ) + 1 : 1 );
                }

                // Sorting levels
                Collections.sort ( levels );

                // Checking for filled rows
                for ( final int level : levels )
                {
                    if ( blocksPerLevel.get ( level ) == BLOCKS_IN_WIDTH )
                    {
                        // Destroying filled rows and moving upper rows
                        final List<Point> toRemove = new ArrayList<Point> ();
                        final List<Point> toMove = new ArrayList<Point> ();
                        for ( final Point point : terrain.keySet () )
                        {
                            final Block block = terrain.get ( point );
                            final int lvl = block.getBlockPoint ().y;
                            if ( lvl == level )
                            {
                                // Adding block to delete list
                                toRemove.add ( point );
                            }
                            else if ( lvl < level )
                            {
                                // Adding block to move list
                                toMove.add ( point );
                            }
                        }
                        for ( final Point removed : toRemove )
                        {
                            terrain.remove ( removed );
                        }

                        // Moving upper blocks
                        Collections.sort ( toMove, new Comparator<Point> ()
                        {
                            @Override
                            public int compare ( final Point o1, final Point o2 )
                            {
                                final Integer int1 = o1.y;
                                final Integer int2 = o2.y;
                                return -int1.compareTo ( int2 );
                            }
                        } );
                        final Map<Point, Block> movedBlocks = new HashMap<Point, Block> ();
                        for ( final Point point : toMove )
                        {
                            final Block block = terrain.get ( point );
                            final int lvl = block.getBlockPoint ().y;
                            if ( lvl < level )
                            {
                                // Moving block
                                block.getBlockPoint ().y += 1;
                                terrain.remove ( point );
                                movedBlocks.put ( new Point ( point.x, point.y + BLOCK_SIDE + 1 ), block );
                            }
                        }
                        terrain.putAll ( movedBlocks );

                        // Increasing score
                        linesDestroyed++;
                        score += stage * BLOCKS_IN_WIDTH;

                        // Updating view
                        repaint ();
                    }
                }
            }

            // Changing level
            if ( linesDestroyed >= LINES_TO_CHANGE_STAGE )
            {
                linesDestroyed = 0;
                stage++;
                pusher.setDelay ( getTickTime () );
            }

            currentFigure = pushNextFigure ();

            // todo Other check needed for case when figure lies upper than north border
            // Game over case
            if ( !currentFigure.canMoveDown ( getTerrainShape () ) )
            {
                gameOver = true;
                repaint ();

                // Informing about game over
                fireGameOver ();

                pusher.stop ();
                return;
            }
        }

        // Moving figure expand
        currentFigure.moveDown ();

        // Updating view
        repaint ();

        // Informing about game tick
        fireTick ();
    }

    private Figure pushNextFigure ()
    {
        // Figure from queue
        final Figure figure = figuresQueue.remove ( 0 );

        if ( figuresQueue.size () == 0 )
        {
            figuresQueue.add ( new Figure () );
        }

        return figure;
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return PREFERRED_SIZE;
    }

    private GeneralPath getTerrainShape ()
    {
        final GeneralPath border = getBorderShape ();
        for ( final Point point : terrain.keySet () )
        {
            border.append ( new Rectangle ( point, new Dimension ( BLOCK_SIDE, BLOCK_SIDE ) ), false );
        }
        return border;
    }

    private GeneralPath getBorderShape ()
    {
        final GeneralPath border = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        border.append ( new Rectangle ( -BLOCK_SIDE - 1, -BLOCK_SIDE * 3 - 3, BLOCK_SIDE, PREFERRED_SIZE.height + BLOCK_SIDE * 3 + 3 ),
                false );
        border.append ( new Rectangle ( -BLOCK_SIDE - 1, PREFERRED_SIZE.height + 1, PREFERRED_SIZE.width + 2 + BLOCK_SIDE * 2, BLOCK_SIDE ),
                false );
        border.append (
                new Rectangle ( PREFERRED_SIZE.width + 1, -BLOCK_SIDE * 3 - 3, BLOCK_SIDE, PREFERRED_SIZE.height + BLOCK_SIDE * 3 + 3 ),
                false );
        return border;
    }

    public static String gameOverText1 = "Long way to go";
    public static String gameOverText3 = "Not so fast!";
    public static String gameOverText6 = "Next time...";
    public static String gameOverText9 = "It went so well";
    public static String gameOverText12 = "Close call";
    public static String gameOverText15 = "Its fifteen!";
    public static String gameWinText = "How did you..?";
    public static String gameOverTip = "(press F2 for new game)";

    public static String gamePausedText = "Game Paused";
    public static String gamePausedTip = "(press P to unpause)";

    public static Font largeFont = new Font ( "Comic Sans MS", Font.BOLD, 20 );
    public static Font smallFont = new Font ( "Comic Sans MS", Font.BOLD, 12 );

    public static Stroke borderStroke = new BasicStroke ( 0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1f, new float[]{ 4, 4 }, 0f );

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        g2d.setPaint ( Color.WHITE );
        g2d.fillRect ( 0, 0, getWidth (), getHeight () );

        final Stroke old = g2d.getStroke ();
        g2d.setStroke ( borderStroke );
        g2d.setPaint ( Color.LIGHT_GRAY );
        for ( int i = 0; i <= BLOCKS_IN_WIDTH; i++ )
        {
            g2d.drawLine ( i * ( BLOCK_SIDE + 1 ), 0, i * ( BLOCK_SIDE + 1 ), getHeight () );
        }
        for ( int i = 0; i <= BLOCKS_IN_HEIGHT; i++ )
        {
            g2d.drawLine ( 0, i * ( BLOCK_SIDE + 1 ), getWidth (), i * ( BLOCK_SIDE + 1 ) );
        }
        g2d.setStroke ( old );

        //        g2d.setPaint ( Color.RED );
        //        g2d.draw ( getTerrainShape () );

        for ( final Point point : terrain.keySet () )
        {
            final Block block = terrain.get ( point );
            block.paintBlock ( g2d, new Rectangle ( point, new Dimension ( BLOCK_SIDE, BLOCK_SIDE ) ), block.getRotation () );
        }

        if ( currentFigure != null )
        {
            currentFigure.paintFigure ( g2d );
        }

        if ( gameOver || !gameRunning )
        {
            String gameOverText = "";
            if ( gameOver )
            {
                if ( stage == 15 )
                {
                    gameOverText = gameOverText15;
                }
                else if ( stage >= 12 )
                {
                    gameOverText = gameOverText12;
                }
                else if ( stage >= 9 )
                {
                    gameOverText = gameOverText9;
                }
                else if ( stage >= 6 )
                {
                    gameOverText = gameOverText6;
                }
                else if ( stage >= 3 )
                {
                    gameOverText = gameOverText3;
                }
                else if ( stage >= 1 )
                {
                    gameOverText = gameOverText1;
                }
            }

            g2d.setFont ( largeFont );
            FontMetrics fm = g2d.getFontMetrics ();
            final FontRenderContext frc = fm.getFontRenderContext ();
            final GlyphVector gv = g2d.getFont ().createGlyphVector ( frc, gameOver ? gameOverText : gamePausedText );
            final Rectangle bigTextBounds = gv.getVisualBounds ().getBounds ();

            g2d.setFont ( smallFont );
            fm = g2d.getFontMetrics ();
            final int smallTextWidth = fm.stringWidth ( gameOver ? gameOverTip : gamePausedTip );

            final int width = Math.max ( bigTextBounds.width, smallTextWidth );
            final RoundRectangle2D rr = new RoundRectangle2D.Double ( getWidth () / 2 - width / 2 - 10,
                    getHeight () / 2 - bigTextBounds.height / 2 - smallFont.getSize () / 2 - 10, width + 20,
                    bigTextBounds.height + smallFont.getSize () + 20, 10, 10 );
            final Rectangle bounds = rr.getBounds ();

            g2d.setPaint ( new GradientPaint ( 0, bounds.y, bgTop, 0, bounds.y + bounds.height, bgBottom ) );
            g2d.fill ( rr );

            g2d.setPaint ( Color.GRAY );
            g2d.draw ( rr );

            g2d.setFont ( largeFont );
            g2d.setPaint ( Color.BLACK );
            g2d.drawString ( gameOver ? gameOverText : gamePausedText, getWidth () / 2 - bigTextBounds.width / 2,
                    getHeight () / 2 + bigTextBounds.height / 2 - smallFont.getSize () / 2 - 2 );

            g2d.setFont ( smallFont );
            g2d.setPaint ( Color.GRAY );
            g2d.drawString ( gameOver ? gameOverTip : gamePausedTip, getWidth () / 2 - smallTextWidth / 2,
                    getHeight () / 2 + bigTextBounds.height / 2 + smallFont.getSize () / 2 + 2 );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    public void addTickListener ( final TickListener tickListener )
    {
        tickListeners.add ( tickListener );
    }

    public void removeTickListener ( final TickListener tickListener )
    {
        tickListeners.remove ( tickListener );
    }

    private void fireTick ()
    {
        for ( final TickListener tickListener : CollectionUtils.copy ( tickListeners ) )
        {
            tickListener.tick ();
        }
    }

    public void addTetrisListener ( final TetrisListener tetrisListener )
    {
        tetrisListeners.add ( tetrisListener );
    }

    public void removeTickListener ( final TetrisListener tetrisListener )
    {
        tetrisListeners.remove ( tetrisListener );
    }

    private void fireNewGameStarted ()
    {
        for ( final TetrisListener tetrisListener : CollectionUtils.copy ( tetrisListeners ) )
        {
            tetrisListener.newGameStarted ();
        }
    }

    private void fireGamePaused ()
    {
        for ( final TetrisListener tetrisListener : CollectionUtils.copy ( tetrisListeners ) )
        {
            tetrisListener.gamePaused ();
        }
    }

    private void fireGameUnpaused ()
    {
        for ( final TetrisListener tetrisListener : CollectionUtils.copy ( tetrisListeners ) )
        {
            tetrisListener.gameUnpaused ();
        }
    }

    private void fireGameOver ()
    {
        for ( final TetrisListener tetrisListener : CollectionUtils.copy ( tetrisListeners ) )
        {
            tetrisListener.gameOver ();
        }
    }

    public Figure getNextFigure ()
    {
        return figuresQueue.get ( 0 );
    }

    public int getScore ()
    {
        return score;
    }

    public int getStage ()
    {
        return stage;
    }

    public JComponent getNextFigureField ()
    {
        return nextFigureField;
    }
}
