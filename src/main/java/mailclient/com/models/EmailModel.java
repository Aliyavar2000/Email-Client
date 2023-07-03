package mailclient.com.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.RadioButton;

public class EmailModel {
    private String from;
    private String to;
    private String subject;
    private String content;
    private String dateSent;
    private String dateReceived;
    // private RadioButton isSelected;
    // private BooleanProperty selected = new SimpleBooleanProperty(false);
    // private RadioButton radioButton;

    public EmailModel(String from, String to, String subject, String content, String dateSent, String dateReceived) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.dateSent = dateSent;
        this.dateReceived = dateReceived;
        // this.isSelected = new RadioButton();
        // isSelected.getStylesheets()
        // .add(getClass().getResource("/mailclient/com/styles/styles.css").toExternalForm());
        // this.isSelected.getStyleClass().add("radio-button");
        // System.out.println(this.isSelected.getStyleClass());
        // System.out.println(this.isSelected.getStylesheets());
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    // public boolean isSelected() {
    // return selected.get();
    // }

    // public void setSelected(boolean isSelected) {
    // this.selected.set(isSelected);
    // }

    // public BooleanProperty selectedProperty() {
    // return selected;
    // }
}
