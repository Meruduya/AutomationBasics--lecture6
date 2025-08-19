package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement login = $("[data-test-id='login'] input");
    private final SelenideElement password = $("[data-test-id='password'] input");
    private final SelenideElement button = $("[data-test-id='action-login']");

    public LoginPage() {
        login.shouldBe(Condition.visible);
    }

    public VerificationPage validLogin(DataHelper.AutoInfo autoInfo) {
        login.setValue(autoInfo.getLogin());
        password.setValue(autoInfo.getPassword());
        button.click();
        return new VerificationPage(); // возвращаем новую страницу
    }
}
