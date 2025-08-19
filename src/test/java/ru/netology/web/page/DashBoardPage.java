package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashBoardPage {
    private final SelenideElement header = $("[data-test-id=dashboard]");

    // к сожалению, разработчики не дали нам удобного селектора, поэтому так
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashBoardPage() {
        header.should(Condition.visible);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    private SelenideElement getCardElement(DataHelper.CardInfo cardInfo) {
        return cards.find(Condition.attribute("data-test-id", cardInfo.getTestId()));
    }
    public int getCardBalance (DataHelper.CardInfo cardInfo) {
        var text = getCardElement(cardInfo).getText();
        return extractBalance(text);
    }

    public TransferPage selectCard(DataHelper.CardInfo cardInfo) {
        getCardElement(cardInfo).$("button").click();
        return new TransferPage();
    }
    //  Метод обновления страницы
    public DashBoardPage refreshPage() {
        $("[data-test-id=action-reload]").click();
        header.should(Condition.visible);
        return this;
    }
    // Доменный метод для проверки баланса конкретной карты
    public DashBoardPage shouldHaveBalance(DataHelper.CardInfo cardInfo, int expectedBalance) {
        var cardElement = getCardElement(cardInfo);
        var expectedText = "баланс: " + expectedBalance + " р.";
        cardElement.should(Condition.text(expectedText));
        return this;
    }

}

