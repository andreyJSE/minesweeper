package aivanov.games.minesweeper.gui;

import aivanov.games.minesweeper.model.GameOptions;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.Container;
import java.awt.Font;


/**
 Class is intended to create a main windows and menu items of the game.
 */

public class MinesFieldFrame extends JFrame implements ActionListener {

    private GameOptions options = GameOptions.beginner();

    private final CustomLevel customDialog;
    private final Container contentPanel;

    private boolean mOpenedMove = false;
    private boolean mOpenRemaining = false;

    private MinesFieldFrame() {
        //Toolkit t = Toolkit.getDefaultToolkit();
        //setSize(t.getScreenSize().height/2, t.getScreenSize().width/2);

        setTitle("Minesweeper");
        createMainMenu();

        setLocationByPlatform(true);
        setResizable(false);

        customDialog = new CustomLevel(this);

        contentPanel = getContentPane();
        createNewGame();
    }

    public static MinesFieldFrame createMainWindow() {
        MinesFieldFrame mainWindow = new MinesFieldFrame();
        mainWindow.createMainMenu();
        return mainWindow;
    }

    private void createMainMenu() {

        Font font = new Font("Comics", Font.BOLD, 14);

        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createMenuGame(font));
        menuBar.add(createMenuOptions(font));
        menuBar.add(createMenuHelp(font));

        setJMenuBar(menuBar);
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

        mOptions.add(newCheckBoxItem("Opening move", KeyEvent.VK_M,
                "First move always open a useful series of squares."));

        mOptions.add(newCheckBoxItem("Open remaining", KeyEvent.VK_R,
                "When 0 bombs are left unmarked, click the bomb counter 000 to open all remaining"));

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
        mItem.addActionListener(this);
        mItem.setAccelerator(KeyStroke.getKeyStroke(keyEvent, inputEvent));
        return mItem;
    }

    private JRadioButtonMenuItem newRadioButtonItem(String title, Boolean selected, int key,
                                                    ButtonGroup group) {
        JRadioButtonMenuItem rItem = new JRadioButtonMenuItem(title, selected);
        rItem.setActionCommand(title);
        rItem.addActionListener(this);
        rItem.setMnemonic(key);
        group.add(rItem);
        return rItem;
    }

    private JCheckBoxMenuItem newCheckBoxItem(String name, int keyEvent, String description) {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
        item.setActionCommand(name);
        item.addActionListener(this);
        item.setMnemonic(keyEvent);
        item.setToolTipText(description);
        return item;
    }

    private void createNewGame() {
        contentPanel.removeAll();
        FieldPanel minesPanel = new FieldPanel(options,
                mOpenedMove, mOpenRemaining);
        contentPanel.add(minesPanel, BorderLayout.SOUTH);
        pack();
    }

    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {

            case "New Game":     createNewGame();
                break;

            case "Beginner":     options = GameOptions.beginner();
                createNewGame();
                break;

            case "Intermediate": options = GameOptions.intermediate();
                createNewGame();
                break;

            case "Expert":       options = GameOptions.expert();
                createNewGame();
                break;

            case "Custom":       customDialog.showDialog();
                createNewGame();
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
                mOpenedMove = !mOpenedMove;
                break;

            case "Open remaining":
                mOpenRemaining = !mOpenRemaining;
                break;
            case "Exit":         System.exit(0);

        }
    }

    private class CustomLevel extends JDialog {

        private final int DEFAULT_WIDTH = 300;
        private final int DEFAULT_HEIGHT = 200;
        private final int MAX_ROWS = 100;
        private final int MAX_COLS = 100;
        private final int MAX_MINES = MAX_ROWS * MAX_COLS / 3;
        private final JFrame owner;

        private JSpinner rowSpinner;
        private JSpinner colSpinner;
        private JSpinner mineSpinner;

        public CustomLevel(final JFrame owner) {
            super(owner, true);
            this.owner = owner;
            setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.PAGE_AXIS));

            rowSpinner = new JSpinner(new SpinnerNumberModel());
            colSpinner = new JSpinner(new SpinnerNumberModel());
            mineSpinner = new JSpinner(new SpinnerNumberModel());

            dialogPanel.add(rowSpinner);
            dialogPanel.add(colSpinner);
            dialogPanel.add(mineSpinner);

            JPanel dialogButtons = new JPanel();

            JButton ok = new JButton("OK");
            JButton cancel = new JButton("Cancel");

            dialogButtons.add(ok);
            dialogButtons.add(cancel);

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
                        options = GameOptions.custom(_rows, _cols, _mines);
                        setVisible(false);
                    }
                }
            });

            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    setVisible(false);
                }
            });

            add(dialogPanel, BorderLayout.NORTH);
            add(dialogButtons, BorderLayout.SOUTH);
            setTitle("Set custom values");
            pack();

        }

        public void showDialog() {

            rowSpinner.setValue(options.getRowCount());
            colSpinner.setValue(options.getColumnCount());
            mineSpinner.setValue(options.getMineCount());
            setVisible(true);
        }
    }
}