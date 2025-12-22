package util;

import javax.swing.text.*;

public class IntegerFilter extends DocumentFilter {

	// The problem number input field has been made so that only 0 and positive integers can be entered.
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (string.matches("\\d+")) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs)
            throws BadLocationException {
        if (string.matches("\\d+")) {
            super.replace(fb, offset, length, string, attrs);
        }
    }
}
