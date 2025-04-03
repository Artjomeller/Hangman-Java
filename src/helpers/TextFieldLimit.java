package helpers;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * See klass tegeleb sisestuskasti teksti pikkuse piiranduda. Ei luba üle ühe märgi lisada.
 * Samuti tagab, et sisestada saab ainult tähti, mitte numbreid ega erimärke.
 */
public class TextFieldLimit extends PlainDocument {
    /**
     * Maksimaalne pikkus sisestuskastile (meil on see üks)
     */
    private final int limit;

    /**
     * Konstruktor
     */
    public TextFieldLimit(int limit) {
        super(); // TextFieldLimit enda konstruktori kasutamine
        this.limit = limit;
    }

    /**
     * See kontrollib kas lisada märk sisestuskasti või mitte.
     * Lubab ainult tähti, filtreerib välja numbrid ja erimärgid.
     * @param offset the starting offset &gt;= 0
     * @param str the string to insert; does nothing with null/empty strings
     * @param a the attributes for the inserted content
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        if(str == null) return;

        // Kontrolli, kas sisestatud tekst on täht (kas suur või väike) ja pikkus on lubatud piires
        if((getLength() + str.length()) <= limit && str.matches("[a-zA-ZõäöüÕÄÖÜšžŠŽ]")) {
            super.insertString(offset, str, a);
        }
    }
}