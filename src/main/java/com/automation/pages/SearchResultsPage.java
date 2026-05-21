package com.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class SearchResultsPage extends BasePage {

    @AndroidFindBy(id = "search_result_screen_root")
    private WebElement resultScreen;

    @AndroidFindBy(id = "content_card_hotel_name_0")
    private WebElement firstHotelName;

    @AndroidFindBy(id = "content_card_destination_0")
    private WebElement firstHotelDestination;

    @AndroidFindBy(id = "content_card_rating_0")
    private WebElement firstHotelRating;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='£1,280']")
    private WebElement hotelPrice;

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(resultScreen);
    }

    public String getHotelName() {
        return getText(firstHotelName);
    }

    public String getDestination() {
        return getText(firstHotelDestination);
    }

    public String getRating() {
        return getText(firstHotelRating);
    }

    public String getPrice() {
        return getText(hotelPrice);
    }
}