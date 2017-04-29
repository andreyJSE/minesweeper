package aivanov.games.minesweeper.resources;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 Resource class for game images.
 */

public enum Glyph {
    ZERO("zero.png"), ONE("one.png"), TWO("two.png"), THREE("three.png"), FOUR("four.png"), FIVE("five.png"), SIX("six.png"), SEVEN("seven.png"), EIGHT("eight.png"), MINE("mine.png"), FLAG("flag.png"), MINERED("minered.png"), UNOPENED("unopened.png"), SAD("sad.png"), SMILE("smile.png"), TICK("tick.png"), NUMBERS("numbers.png"), EXPRESSIONS("expressions.png");

    private ImageIcon icon = null;

    private Glyph(String fileName) {
        try {
            Image image = ImageIO.read(getClass().getResource("/images/" + fileName));
            icon = new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageIcon getGlyph() {
        return icon;
    }

    public static ImageIcon getByValue(int num) {

        if (num < -1 || num > 9) {
            throw new IndexOutOfBoundsException("No such icon mapped to " + num + "value.");
        }

        ImageIcon imageIcon = null;

        switch (num) {
            case -1:    imageIcon = MINE.getGlyph();
                break;
            case 0: imageIcon = ZERO.getGlyph();
                break;
            case 1: imageIcon = ONE.getGlyph();
                break;
            case 2: imageIcon = TWO.getGlyph();
                break;
            case 3: imageIcon = THREE.getGlyph();
                break;
            case 4: imageIcon = FOUR.getGlyph();
                break;
            case 5: imageIcon = FIVE.getGlyph();
                break;
            case 6: imageIcon = SIX.getGlyph();
                break;
            case 7: imageIcon = SEVEN.getGlyph();
                break;
            case 8: imageIcon = EIGHT.getGlyph();
                break;
            case 9: imageIcon = FLAG.getGlyph();
                break;
            default: break;
        }

        return imageIcon;
    }
}