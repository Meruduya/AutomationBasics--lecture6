package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountInput = $("[data-test-id=amount] input");
    private final SelenideElement fromInput = $("[data-test-id=from] input");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public TransferPage() {
        // Проверяем, что страница перевода загрузилась
        amountInput.shouldBe(Condition.visible);
    }

    // Основной метод для выполнения перевода
    public DashBoardPage makeValidTransfer(String amount, DataHelper.CardInfo fromCard) {
        amountInput.setValue(amount);
        fromInput.setValue(fromCard.getCardNumber());
        transferButton.click();
        return new DashBoardPage();
    }

    // Метод для проверки ошибки (для негативного теста)
    public void findErrorMessage(String expectedText) {
        errorNotification.shouldBe(Condition.visible)   // проверяем, что сообщение отображается
                .shouldHave(Condition.text(expectedText)); // и что текст совпадает
    }
}