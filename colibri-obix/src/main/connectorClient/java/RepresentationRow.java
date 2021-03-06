import model.ObixObject;

import javax.swing.*;

public class RepresentationRow {

    private JLabel uriLabel;
    private JCheckBox observedCheckBox;
    private JTextField valueTextField;
    private ObixObject obixObject;
    private JCheckBox writableCheckbox;
    private JButton getButton;
    private JCheckBox chooseCheckbox;
    private String objectType;

    public RepresentationRow(JLabel uriLabel, JCheckBox observedCheckBox, JTextField valueTextField, ObixObject obixObject, JCheckBox writableCheckbox, JButton getButton) {
        this.uriLabel = uriLabel;
        this.observedCheckBox = observedCheckBox;
        this.valueTextField = valueTextField;
        this.obixObject = obixObject;
        this.writableCheckbox = writableCheckbox;
        this.getButton = getButton;
    }

    public RepresentationRow(ObixObject obixObject, JCheckBox chooseCheckbox, String objectType) {
        this.obixObject = obixObject;
        this.chooseCheckbox = chooseCheckbox;
        this.objectType = objectType;
    }

    public JLabel getUriLabel() {
        return uriLabel;
    }

    public void setUriLabel(JLabel uriLabel) {
        this.uriLabel = uriLabel;
    }

    public JCheckBox getObservedCheckBox() {
        return observedCheckBox;
    }

    public void setObservedCheckBox(JCheckBox observedCheckBox) {
        this.observedCheckBox = observedCheckBox;
    }

    public JTextField getValueTextField() {
        return valueTextField;
    }

    public void setValueTextField(JTextField valueTextField) {
        this.valueTextField = valueTextField;
    }

    public ObixObject getObixObject() {
        return obixObject;
    }

    public void setObixObject(ObixObject obixObject) {
        this.obixObject = obixObject;
    }

    public RepresentationRow get(JCheckBox checkBox) {
        return this;
    }

    public JCheckBox getWritableCheckbox() {
        return writableCheckbox;
    }

    public void setWritableCheckbox(JCheckBox writableCheckbox) {
        this.writableCheckbox = writableCheckbox;
    }

    public JButton getGetButton() {
        return getButton;
    }

    public void setGetButton(JButton getButton) {
        this.getButton = getButton;
    }

    public JCheckBox getChooseCheckbox() {
        return chooseCheckbox;
    }

    public void setChooseCheckbox(JCheckBox chooseCheckbox) {
        this.chooseCheckbox = chooseCheckbox;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
