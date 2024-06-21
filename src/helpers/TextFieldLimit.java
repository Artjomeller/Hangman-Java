package helpers;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * See klass tegeleb sisestuskasti teksti pikkuse piiranduda. Ei luba üle ühe märgi lisada.
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
     * See kontrollib kas lisada märk sisestuskasti või mitte
     * @param offset the starting offset &gt;= 0
     * @param str the string to insert; does nothing with null/empty strings
     * @param a the attributes for the inserted content
     */
    public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
        if(str == null) return;
        if((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, a);
        }
    }
}
