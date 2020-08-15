package in.bluebytes.superbox;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotePage {

    @FindBy(css="#note-id")
    private WebElement noteIdField;

    @FindBy(css="#note-title")
    private WebElement noteTitleField;

    @FindBy(css="#note-description")
    private WebElement noteDescriptionField;

    @FindBy(css="#noteModal .modal-footer button:last-child")
    private WebElement noteSubmitButton;

    public NotePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void addNote(String noteTitle, String noteDescription) {
        this.noteTitleField.sendKeys(noteTitle);
        this.noteDescriptionField.sendKeys(noteDescription);
        this.noteSubmitButton.click();
    }

}
