package com.ps.Page;

public class OnceText {
    private String textBox;

    private boolean checkBox;

    public OnceText(String textBox, boolean checkBox) {
        this.textBox = textBox;
        this.checkBox = checkBox;
    }

    public String getTextBox() {
        return textBox;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }
}
