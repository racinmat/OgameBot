package com.jlcavanagh.ogamebot.web.construction;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.jlcavanagh.ogamebot.data.Planet;
import com.jlcavanagh.ogamebot.web.navigation.Navigation;

public class Construction {
        private static Logger logger = Logger.getLogger(Construction.class.getSimpleName());
        
        //Construction constants
        public static final String X_BUILD_BUTTON = "//div[@id='detail']//a[@class='build-it']";
        public static final String X_BUILD_QUANTITY = "//input[@id='number']";
        
        public static void construct(WebDriver driver, Planet planet, ConstructItem toConstruct) {
                construct(driver, planet, toConstruct, 1);
        }
        
        public static void construct(WebDriver driver, Planet planet, ConstructItem toConstruct, int quantity) {
                //Navigate to appropriate page
                Navigation.goToPage(driver, planet, toConstruct.getPage());

                //Build!
                build(driver, toConstruct, quantity);
        }
        
        private static void build(WebDriver driver, ConstructItem toConstruct, int quantity) {
                //Attempt to click button
                String xpath = toConstruct.getXpath();
                WebElement element;
                try {
                        //Click portrait
                        element = driver.findElement(By.xpath(xpath));
                        element.click();
                        
                        //Wait for build button
                        WebDriverWait wait = new WebDriverWait(driver, 10);
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(X_BUILD_BUTTON)));
                        
                        //Enter quantity if available
                        try {
                                element = driver.findElement(By.xpath(X_BUILD_QUANTITY));
                        } catch (NoSuchElementException e) {
                                //Ignore
                        }
                        
                        //Click build button
                        element = driver.findElement(By.xpath(X_BUILD_BUTTON));
                        element.click();
                        
                        logger.info("Started building " + toConstruct);
                } catch(Exception e) {
                        //Get error message from UI
                        try {
                                //Find description element
                                xpath = toConstruct.getErrorXpath(); 
                                element = driver.findElement(By.xpath(xpath));
                                
                                //Pull message string from title attr
                                String message = element.getAttribute("title");
                                
                                //Find <br> tag which prefixes method, then add the length of <br> to exclude that tag
                                //Not present if an error occurs when something could be built according to the game
                                String errorStart = "<br>";
                                if(message.contains(errorStart)) {
                                        message = message.substring(message.indexOf(errorStart) + errorStart.length());
                                }
                                
                                logger.warn("Failed to construct " + toConstruct + ": " + message);
                        } catch(Exception e2) {
                                logger.error("Unknown exception: " + e2);
                        }
                }
        }
}