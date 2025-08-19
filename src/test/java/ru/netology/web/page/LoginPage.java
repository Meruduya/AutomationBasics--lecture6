package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement login = $("[data-test-id='login'] input");
    private final SelenideElement password = $("[data-test-id='password'] input");
    private final SelenideElement button = $("[data-test-id='action-login']");
    private final SelenideElement code = $("[data-test-id='code'] input");
    private final SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public LoginPage() {
        login.shouldBe(Condition.visible);
    }
    public LoginPage validLogin(DataHelper.AutoInfo autoInfo) {
        login.setValue(autoInfo.getLogin());
        password.setValue(autoInfo.getPassword());
        button.click();
        code.shouldBe(Condition.visible);
        return this;
    }
    public DashBoardPage validVerify(DataHelper.VerificationCode verificationCode) {
        code.setValue(verificationCode.getCode());
        verifyButton.click();
        return new DashBoardPage();
    }

}
