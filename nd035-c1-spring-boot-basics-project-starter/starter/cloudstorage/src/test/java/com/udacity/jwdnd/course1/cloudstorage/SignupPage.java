package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage
{
    @FindBy(id="inputFirstName")
    private WebElement firstname;
    @FindBy(id="inputLastName")
    private  WebElement lastName;
    @FindBy(id="inputUsername")
    private  WebElement username;
    @FindBy(id="inputPassword")
    private  WebElement password;

    @FindBy(id = "buttonSignUp")
    private WebElement submitbutton;


    public SignupPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
    public void Signup(String fn,String ln,String userName,String passWord)
    {
        firstname.sendKeys(fn);
        lastName.sendKeys(ln);
        username.sendKeys(userName);
        password.sendKeys(passWord);
        submitbutton.click();

    }
}
