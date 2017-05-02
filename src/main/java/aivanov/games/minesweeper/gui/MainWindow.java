package aivanov.games.minesweeper.gui;

import aivanov.games.minesweeper.model.GameOptions;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.Container;
import java.awt.Font;


/**
 Class is intended to create a main windows and menu items of the game.
 */

public class MainWindow extends JFrame implements ActionListener {

    private static MainWindow frame;

    private final CustomLevel customDialog;

    public static MainWindow createMainWindow() {

        synchronized (MainWindow.class) {
            if (frame == null) {
                frame = new MainWindow();
            }
        }

        return frame;
    }

    public void actionPerformed(ActionEvent event) {

        switch (event.getActionCommand()) {

            case "New Game":
                initNewGame();
                break;

            case "Beginner":
                GameOptions.beginner();
                initNewGame();
                break;

            case "Intermediate":
                GameOptions.intermediate();
                initNewGame();
                break;

            case "Expert":
                GameOptions.expert();
                initNewGame();
                break;

            case "Custom":
                customDialog.showDialog();
                initNewGame();
                break;

            case "About":        JOptionPane.showMessageDialog(this, "The purpose of this game is to open all\n" +
                            "the cells which do not contain a bomb.\n" +
                            "You lose if you open a bomb cell.\n\n\n" +
                            "P.S. Nice to have the following:\n" +
                            "1. Keep records\n" +
                            "2. Ability to save the game\n" +
                            "3. Automatic solver\n" +
                            "4. Checking for 9 cells\n",
                    "Help", JOptionPane.PLAIN_MESSAGE);
                break;

            case "Opening move":
                GameOptions.safeMove = !GameOptions.safeMove;
                break;

            case "Open remaining":
                GameOptions.openRemaining = !GameOptions.openRemaining;
                break;

            case "Exit":
                System.exit(0);

        }
    }

    private MainWindow() {
        //Toolkit t = Toolkit.getDefaultToolkit();
        //setSize(t.getScreenSize().height/2, t.getScreenSize().width/2);

        customDialog = new CustomLevel(this);

        setTitle("Minesweeper");
        setLocationByPlatform(true);
        setResizable(false);

        setJMenuBar(new MainMenu(this));
        initNewGame();
    }

    private void initNewGame() {
        Container contentPane = getContentPane();

        contentPane.removeAll();
        contentPane.add(new MineGrid(), BorderLayout.SOUTH);
        pack();
    }

    private final class MainMenu extends JMenuBar {

        public final String FONT_NAME = "Comics";
        public final int FONT_STYLE = Font.BOLD;
        public final int FONT_SIZE = 14;
        public final ActionListener listener;

        public MainMenu(ActionListener listener) {

            this.listener = listener;

            Font font = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);

            this.add(createMenuGame(font));
            this.add(createMenuOptions(font));
            this.add(createMenuHelp(font));
        }

        private JMenu createMenuGame(Font font) {

            JMenu mGame = newMenu("Game", KeyEvent.VK_G, font);

            JMenuItem mNewGame = newMenuItem("New Game", KeyEvent.VK_F2, InputEvent.ALT_MASK);
            JMenuItem mExit = newMenuItem("Exit", KeyEvent.VK_F4, InputEvent.ALT_MASK);

            ButtonGroup bGroup = new ButtonGroup();

            mGame.add(mNewGame);
            mGame.addSeparator();
            mGame.add(newRadioButtonItem("Beginner", true, KeyEvent.VK_B, bGroup));
            mGame.add(newRadioButtonItem("Intermediate", false, KeyEvent.VK_I, bGroup));
            mGame.add(newRadioButtonItem("Expert", false, KeyEvent.VK_E, bGroup));
            mGame.add(newRadioButtonItem("Custom", false, KeyEvent.VK_C, bGroup));
            mGame.addSeparator();
            mGame.add(mExit);

            return mGame;
        }

        private JMenu createMenuOptions(Font font) {

            JMenu mOptions = newMenu("Options", KeyEvent.VK_O, font);

            JCheckBoxMenuItem safeMove = newCheckBoxItem("Opening move", KeyEvent.VK_M,
                    "First move always open a useful series of squares.");

            JCheckBoxMenuItem openRemaining = newCheckBoxItem("Open remaining", KeyEvent.VK_R,
                    "When 0 bombs are left unmarked, click the bomb counter 000 to open all remaining");

            GameOptions.safeMove = safeMove.isSelected();
            GameOptions.openRemaining = openRemaining.isSelected();

            mOptions.add(safeMove);
            mOptions.add(openRemaining);

            return mOptions;
        }

        private JMenu createMenuHelp(Font font) {
            JMenu mHelp = newMenu("Help", KeyEvent.VK_H, font);

            JMenuItem mAbout = newMenuItem("About", KeyEvent.VK_F1, 0);
            mAbout.setMnemonic(KeyEvent.VK_A);

            mHelp.add(mAbout);

            return mHelp;
        }

        private JMenu newMenu(String name, int keyEvent, Font font) {
            JMenu menu = new JMenu(name);
            menu.setMnemonic(keyEvent);
            menu.setFont(font);
            return menu;
        }

        private JMenuItem newMenuItem(String title, int keyEvent, int inputEvent) {
            JMenuItem mItem = new JMenuItem(title);
            mItem.setActionCommand(title);
            mItem.addActionListener(listener);
            mItem.setAccelerator(KeyStroke.getKeyStroke(keyEvent, inputEvent));
            return mItem;
        }

        private JRadioButtonMenuItem newRadioButtonItem(String title, Boolean selected, int key,
                                                        ButtonGroup group) {
            JRadioButtonMenuItem rItem = new JRadioButtonMenuItem(title, selected);
            rItem.setActionCommand(title);
            rItem.addActionListener(listener);
            rItem.setMnemonic(key);
            group.add(rItem);
            return rItem;
        }

        private JCheckBoxMenuItem newCheckBoxItem(String name, int keyEvent, String description) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
            item.setActionCommand(name);
            item.addActionListener(listener);
            item.setMnemonic(keyEvent);
            item.setToolTipText(description);
            return item;
        }
    }

    private final class CustomLevel extends JDialog {

        private final int DEFAULT_WIDTH = 300;
        private final int DEFAULT_HEIGHT = 200;
        private final int MAX_ROWS = 100;
        private final int MAX_COLS = 100;
        private final int MAX_MINES = MAX_ROWS * MAX_COLS / 3;

        private JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel());
        private JSpinner colSpinner = new JSpinner(new SpinnerNumberModel());
        private JSpinner mineSpinner = new JSpinner(new SpinnerNumberModel());

        public CustomLevel(final JFrame owner) {
            super(MainWindow.this, true);

            setTitle("Set custom values");
            setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.PAGE_AXIS));

            dialogPanel.add(rowSpinner);
            dialogPanel.add(colSpinner);
            dialogPanel.add(mineSpinner);

            JPanel dialogButtons = new JPanel();

            JButton ok = new JButton("OK");
            ok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    int _rows = (Integer) rowSpinner.getValue();
                    int _cols = (Integer) colSpinner.getValue();
                    int _mines = (Integer) mineSpinner.getValue();

                    if (_rows > MAX_ROWS || _cols > MAX_COLS || _mines > MAX_MINES) {
                        JOptionPane.showMessageDialog(owner, "Please select values from the following ranges:\nrows 1 - " + MAX_ROWS +
                                "\ncolumns 1 - " + MAX_COLS + "\nmines 1 - " + MAX_MINES);
                    } else if (_mines > (_rows * _cols/3)) {
                        JOptionPane.showMessageDialog(owner, "Max value of mines should be less then 1/3 of row*columns");
                    } else {
                        GameOptions.custom(_rows, _cols, _mines);
                        setVisible(false);
                    }
                }
            });

            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    setVisible(false);
                }
            });

            dialogButtons.add(ok);
            dialogButtons.add(cancel);

            add(dialogPanel, BorderLayout.NORTH);
            add(dialogButtons, BorderLayout.SOUTH);

            pack();

        }

        public void showDialog() {

            rowSpinner.setValue(GameOptions.rowCount);
            colSpinner.setValue(GameOptions.columnCount);
            mineSpinner.setValue(GameOptions.mineCount);
            setVisible(true);
        }
    }
}