package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    private DashBoardPage dashboardPage;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;
    private int initialBalance1;
    private int initialBalance2;

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://127.0.0.1:9999");

        var authInfo = DataHelper.getAutoInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var loginPage = new LoginPage();
        loginPage.validLogin(authInfo);
        dashboardPage = loginPage.validVerify(verificationCode);

        firstCard = DataHelper.getFirstCardInfo();
        secondCard = DataHelper.getSecondCardInfo();
        initialBalance1 = dashboardPage.getCardBalance(firstCard);
        initialBalance2 = dashboardPage.getCardBalance(secondCard);
    }

    @AfterEach
    void tearDown() {
        // если надо — можно будет сбрасывать состояние
    }

    @Test
    //  Перевод денег с первой карты на вторую
    void shouldTransferMoneyFromFirstCardToSecond() {
        int amount = 1000;

        var transferPage = dashboardPage.selectCard(secondCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard);

        assertEquals(initialBalance1 - amount, dashboardPage.getCardBalance(firstCard));
        assertEquals(initialBalance2 + amount, dashboardPage.getCardBalance(secondCard));
    }

    @Test
    // Перевод денег со второй карты на первую
    void shouldTransferMoneyFromSecondCardToFirst() {
        int amount = 1000;

        var transferPage = dashboardPage.selectCard(firstCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCard);

        assertEquals(initialBalance1 + amount, dashboardPage.getCardBalance(firstCard));
        assertEquals(initialBalance2 - amount, dashboardPage.getCardBalance(secondCard));
    }

    @Test
    //  Перевод суммы больше остатка
    void shouldGetErrorWhenTransferringMoreThanAvailable() {
        int amount = initialBalance1 + 1;

        var transferPage = dashboardPage.selectCard(secondCard);
        transferPage.makeValidTransfer(String.valueOf(amount), firstCard);
        transferPage.findErrorMessage("Выполнена попытка перевода суммы, превышающей остаток на карте списания");
    }

    @Test
    // Перевод отклоняется из-за превышения лимита средств, а баланс остаётся тем же
    void shouldKeepBalanceUnchangedAfterFailedTransfer() {
        int amount = initialBalance1 + 1000;

        var transferPage = dashboardPage.selectCard(secondCard);
        transferPage.makeValidTransfer(String.valueOf(amount), firstCard);

        dashboardPage = dashboardPage.refreshPage();

        assertEquals(initialBalance1, dashboardPage.getCardBalance(firstCard));
        assertEquals(initialBalance2, dashboardPage.getCardBalance(secondCard));
    }

    @Test
    // Перевод 0 рублей
    void shouldTransferZeroAmount() {
        var transferPage = dashboardPage.selectCard(secondCard);
        dashboardPage = transferPage.makeValidTransfer("0", firstCard);

        assertEquals(initialBalance1, dashboardPage.getCardBalance(firstCard));
        assertEquals(initialBalance2, dashboardPage.getCardBalance(secondCard));
    }

    @Test
    // Проверка баланса через доменные методы
    void shouldUseDomainMethodToVerifyBalance() {
        int amount = 1500;

        var transferPage = dashboardPage.selectCard(secondCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard);

        dashboardPage
                .shouldHaveBalance(firstCard, initialBalance1 - amount)
                .shouldHaveBalance(secondCard, initialBalance2 + amount);
    }
}