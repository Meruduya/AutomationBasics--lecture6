package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement code = $("[data-test-id='code'] input");
    private final SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public VerificationPage() {
        code.shouldBe(Condition.visible);
    }

    public DashBoardPage validVerify(DataHelper.VerificationCode verificationCode) {
        code.setValue(verificationCode.getCode());
        verifyButton.click();
        return new DashBoardPage();
    }
}
