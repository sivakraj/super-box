package in.bluebytes.superbox;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialPage {

    @FindBy(css="#credential-url")
    private WebElement credURLField;

    @FindBy(css="#credential-username")
    private WebElement credUserNameField;

    @FindBy(css="#credential-password")
    private WebElement credPasswordField;

    @FindBy(css="#credentialModal .modal-footer button:last-child")
    private WebElement credentialSubmitButton;

    public CredentialPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void addCredential(String url, String userName, String password) {
        this.credURLField.sendKeys(url);
        this.credUserNameField.sendKeys(userName);
        this.credPasswordField.sendKeys(password);
        this.credentialSubmitButton.click();
    }

}
