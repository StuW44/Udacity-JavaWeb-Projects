package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage
{

    @FindBy(id="inputUsername")
    private  WebElement username;
    @FindBy(id="inputPassword")
    private  WebElement password;

    @FindBy(id = "login-button")
    private WebElement loginbutton;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
    public void Login(String userName,String passWord)
    {
        username.sendKeys(userName);
        password.sendKeys(passWord);
        loginbutton.click();
    }
}
