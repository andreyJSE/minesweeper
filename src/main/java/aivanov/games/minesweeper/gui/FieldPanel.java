package aivanov.games.minesweeper.gui;

import aivanov.games.minesweeper.model.GameOptions;
import aivanov.games.minesweeper.model.MinesModel;
import aivanov.games.minesweeper.resources.Glyph;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 This class intended to create GUI for minefield and processing button events.
 */

class FieldPanel extends JPanel implements MouseListener{

    private final int rowNumber;
    private final int colNumber;
    private final int minesNumber;

    // Buttons below main menu and above minefield
    private JButton leftButton;
    private JButton rightButton;
    private JButton centralButton;

    private final Timer timer;

    // Contains pointers to "mine" buttons
    private MineTile[][] tiles;

    private MinesModel minesLayout;

    private int numClosedTiles;
    private int minesLeft;
    private int timeElapsed = 0;

    // Size of left and right buttons
    private final int BUTTONWIDTH = 45;
    private final int BUTTONHEIGHT = 30;

    // Auxiliary indexes to select icon for central button
    private final int SMILED = 0;
    private final int NEUTRAL = 1;
    private final int SAD = 2;

    private final int CBUTTONWIDTH = 30;
    private final int CBUTTONHEIGHT = 30;

    private boolean isGameEnded = false;
    private boolean gameStarted = false;

    // Variables intended for keeping menus state
    private boolean openMove;
    private boolean openRemaining;


    public FieldPanel() {

        rowNumber = GameOptions.rowCount;
        colNumber = GameOptions.columnCount;
        minesNumber = GameOptions.mineCount;
        openMove = GameOptions.safeMove;
        openRemaining = GameOptions.openRemaining;

        minesLeft = minesNumber;
        numClosedTiles = rowNumber * colNumber;

        minesLayout = new MinesModel(rowNumber, colNumber, minesNumber);

        setLayout(new BorderLayout());

        add(new ToolBar(), BorderLayout.NORTH);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getBackground(),5),
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));

        add(new TilesPanel(), BorderLayout.SOUTH);

        timer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (++timeElapsed > 999) {
                    timer.stop();
                    isGameEnded = true;
                    setButtonIcon(SAD, true, centralButton);
                    openAllTiles();
                } else {
                    setButtonIcon(timeElapsed, false, rightButton);
                }
            }
        });
    }


    /**
     Open all tiles that are next to the given tile.
     Assumes that the given tile don't have any mines next to it.
     @param rowIndex Row index of given tile
     @param colIndex Column index of given tile
     */

    private void openAdjacentTiles(int rowIndex, int colIndex) {

        for (int i = rowIndex - 1; i <= rowIndex + 1 && i < rowNumber; i++) {
            for (int j = colIndex - 1; j <= colIndex + 1 && j < colNumber; j++) {
                if (i >= 0 && j >= 0 && tiles[i][j].getIcon() == null) {
                    MouseEvent me = new MouseEvent(tiles[i][j], MouseEvent.MOUSE_RELEASED,
                            System.nanoTime(), MouseEvent.BUTTON1_MASK,
                            0, 0, 1, false);
                    tiles[i][j].dispatchEvent(me);
                }
            }
        }

    }

    /**
     Create event for all non-selected tile.
     To cut a long story short it opens all closed tiles.
     */

    private void openAllTiles() {
        for (int i = rowNumber-1; i >= 0; i--) {
            for (int j = colNumber-1; j >= 0; j--) {
                if (!tiles[i][j].isSelected()) {
                    MouseEvent me = new MouseEvent(tiles[i][j], MouseEvent.MOUSE_RELEASED,
                            System.nanoTime(), MouseEvent.BUTTON1_MASK,
                            0, 0, 1, false);
                    tiles[i][j].dispatchEvent(me);
                }
            }
        }
    }

    /**
     Check if flagged icons don't contain mines.
     The method is used only with the "Open remaining" menu item.
     */

    private void checkClosedTiles() {
        for (int i = rowNumber-1; i >= 0; i--) {
            for (int j = colNumber-1; j >= 0; j--) {
                if (!tiles[i][j].isSelected() && tiles[i][j].getIcon() == null) {
                    MouseEvent me = new MouseEvent(tiles[i][j], MouseEvent.MOUSE_RELEASED,
                            System.nanoTime(), MouseEvent.BUTTON1_MASK,
                            0, 0, 1, false);
                    tiles[i][j].dispatchEvent(me);
                }
            }
        }
    }

    public void mouseClicked(MouseEvent me) {}

    public void mousePressed(MouseEvent me) {}

    public void mouseReleased(MouseEvent event) {

        MineTile button = (MineTile) event.getComponent();

        //    Start timer and create a new game model in case if the "Open move" menu item selected.

        if (!gameStarted) {
            gameStarted = true;
            timer.start();
            if (openMove) {
                minesLayout = new MinesModel(rowNumber, colNumber, minesNumber,
                        button.getRow(), button.getCol());
            }
        }

        // Check if the right mouse button has been pressed to flag/unflag tile
        if (event.getButton() == event.BUTTON3 && !button.isSelected()) {
            if (button.getIcon() != null) {
                button.setIcon(null);
                minesLeft++;
            } else if (minesLeft > 0) {
                button.setIcon(Glyph.FLAG.getGlyph());
                minesLeft--;
            }
            setButtonIcon(minesLeft, false, leftButton);
            // Check if the left mouse button has been pressed
        } else if (event.getButton() == event.BUTTON1 && !button.isSelected()) {
            button.setSelected(true);
            int value = minesLayout.getValueAt(button.getRow(), button.getCol());

            if (!isGameEnded) {
                if (value == -1) {
                    button.setIcon(Glyph.MINERED.getGlyph());
                    isGameEnded = true;
                    timer.stop();
                    setButtonIcon(SAD, true, centralButton);
                    openAllTiles();
                } else {

                    button.setIcon(Glyph.getByValue(value));
                    setButtonIcon(minesLeft, false, leftButton);
                    --numClosedTiles;

                    if (value == 0) {
                        openAdjacentTiles(button.getRow(), button.getCol());
                    }

                    if (numClosedTiles <= minesNumber) {
                        isGameEnded = true;
                        timer.stop();
                        setButtonIcon(SMILED, true, centralButton);
                        openAllTiles();
                    }
                }
            } else {
                button.setIcon(Glyph.getByValue(value));
            }
        }
    }

    public void mouseEntered(MouseEvent me) {}

    public void mouseExited(MouseEvent me) {}

    /**
     Auxiliary method to draw picture on buttons below menu.
     @param number The index of subimage
     @param isExpression If "true" then it draw picture on the central else left/right button
     @param button Pointer to the button instance
     */

    private void setButtonIcon(int number, boolean isExpression, JButton button) {

        int bWidth = button.getPreferredSize().width;
        int bHeight = button.getPreferredSize().height;


        if (isExpression) {
            BufferedImage source = (BufferedImage) Glyph.EXPRESSIONS.getGlyph().getImage();
            int imageWidth = source.getWidth();
            int imageHeight = source.getHeight();
            BufferedImage result = new BufferedImage(imageWidth * 1/3, imageHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = result.createGraphics();
            g2.drawImage(source.getSubimage((imageWidth / 3) * number, 0,
                    imageWidth / 3, imageHeight), null, 0, 0);

            button.setIcon(new ImageIcon(result.getScaledInstance(bWidth,
                    bHeight, java.awt.Image.SCALE_SMOOTH)));
        } else {

            BufferedImage source = (BufferedImage) Glyph.NUMBERS.getGlyph().getImage();
            int imageWidth = source.getWidth();
            int imageHeight = source.getHeight();

            int first = number / 100;
            int second = (number % 100) / 10;
            int third = (number % 10);

            BufferedImage result = new BufferedImage(imageWidth * 3/10, imageHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = result.createGraphics();

            g2.drawImage(source.getSubimage((imageWidth / 10) * first, 0,
                    imageWidth / 10, imageHeight), null, 0, 0);
            g2.drawImage(source.getSubimage((imageWidth / 10) * second, 0,imageWidth / 10,
                    imageHeight), null, imageWidth / 10, 0);
            g2.drawImage(source.getSubimage((imageWidth / 10) * third, 0, imageWidth / 10,
                    imageHeight), null, imageWidth * 2/ 10, 0);

            button.setIcon(new ImageIcon(result.getScaledInstance(bWidth, bHeight,
                    java.awt.Image.SCALE_SMOOTH)));
        }

    }

    private class ToolBar extends JPanel {

        public ToolBar() {

            GridBagLayout gbl = new GridBagLayout();
            setLayout(gbl);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = GridBagConstraints.RELATIVE;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.weightx = 1;

            leftButton = new JButton();
            leftButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
            gbl.setConstraints(leftButton, gbc);
            leftButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    if (openRemaining && minesLeft == 0) {
                        checkClosedTiles();
                    }
                }
            });


            centralButton = new JButton();
            centralButton.setPreferredSize(new Dimension(CBUTTONWIDTH, CBUTTONHEIGHT));
            centralButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            centralButton.setActionCommand("New Game");
//        centralButton.addActionListener(this);

            gbc.anchor = GridBagConstraints.CENTER;
            gbl.setConstraints(centralButton, gbc);

            rightButton = new JButton();
            rightButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
            gbc.anchor = GridBagConstraints.EAST;
            gbl.setConstraints(rightButton, gbc);


            add(leftButton);
            add(centralButton);
            add(rightButton);

            setButtonIcon(minesLeft, false, leftButton);
            setButtonIcon(NEUTRAL, true, centralButton);
            setButtonIcon(timeElapsed, false, rightButton);

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(getBackground(),5),
                    BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));

        }
    }

//    private JPanel createToolBar() {
//
//        JPanel toolBar = new JPanel();
//
//        GridBagLayout gbl = new GridBagLayout();
//        toolBar.setLayout(gbl);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = GridBagConstraints.RELATIVE;
//        gbc.fill = GridBagConstraints.NONE;
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.weightx = 1;
//
//        leftButton = new JButton();
//        leftButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
//        gbl.setConstraints(leftButton, gbc);
//        leftButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent event) {
//                if (openRemaining && minesLeft == 0) {
//                    checkClosedTiles();
//                }
//            }
//        });
//
//
//        centralButton = new JButton();
//        centralButton.setPreferredSize(new Dimension(CBUTTONWIDTH, CBUTTONHEIGHT));
//        centralButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
//        centralButton.setActionCommand("New Game");
////        centralButton.addActionListener(this);
//
//        gbc.anchor = GridBagConstraints.CENTER;
//        gbl.setConstraints(centralButton, gbc);
//
//        rightButton = new JButton();
//        rightButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
//        gbc.anchor = GridBagConstraints.EAST;
//        gbl.setConstraints(rightButton, gbc);
//
//
//        toolBar.add(leftButton);
//        toolBar.add(centralButton);
//        toolBar.add(rightButton);
//
//        setButtonIcon(minesLeft, false, leftButton);
//        setButtonIcon(NEUTRAL, true, centralButton);
//        setButtonIcon(timeElapsed, false, rightButton);
//
//        toolBar.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(getBackground(),5),
//                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));
//
//    }

    /**
     Class intended for creating tile button
     */
    private class MineTile extends JButton {

        private final int rowIndex;
        private final int colIndex;

        public MineTile(int row, int col, MouseListener mListener) {

            super();

            rowIndex = row;
            colIndex = col;

            setPreferredSize(new Dimension(15, 15));
            addMouseListener(mListener);
        }

        public int getRow() {
            return rowIndex;
        }

        public int getCol() {
            return colIndex;
        }

    }

    private class TilesPanel extends JPanel {

        public TilesPanel() {
            setLayout(new GridLayout(rowNumber, colNumber));

            tiles = new MineTile[rowNumber][colNumber];

            for (int i = 0; i < rowNumber; i++) {
                for (int j = 0; j < colNumber; j++) {
                    tiles[i][j] = new MineTile(i, j, FieldPanel.this);
                    add(tiles[i][j]);
                }
            }

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(getBackground(),5),
                    BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));

        }
    }

}