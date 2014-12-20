package com.jlcavanagh.ogamebot.web.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.jlcavanagh.ogamebot.data.Planet;

public abstract class Navigation {
        private static Logger logger = Logger.getLogger(Navigation.class.getSimpleName());
        
        //Credentials and server URL
        public static final String USERNAME="HERE INSERT YOUR USERNAME";
        public static final String PASSWORD="HERE INSERT YOUR PASSWORD";
        public static final String SERVER = "ogame.cz";
        public static final String UNIVERSE="s119-cz.ogame.gameforge.com";

        //Login page constants
        public static final String LOGIN_URL = "http://" + SERVER;
        public static final String ID_SHOW_LOGIN_MENU = "loginBtn";
        public static final String ID_UNIVERSE_BOX = "serverLogin";
        public static final String X_UNIVERSE_OPTION = "//select[@id='" + ID_UNIVERSE_BOX + "']/option[@value='" + UNIVERSE + "']";
        public static final String ID_USERNAME_BOX = "usernameLogin";
        public static final String ID_PASSWORD_BOX = "passwordLogin";
        public static final String ID_LOGIN_BUTTON = "loginSubmit";
        public static final String X_LOGOUT_BUTTON = "//a[contains(text(), 'Log out')]";
        
        //Resources page constants
        public static final String X_RESOURCES_BUTTON = "//div[@id='links']//a[contains(@href, 'page=resources')]";
        public static final String X_RESOURCES_HEADER = "//div[@id='header_text']//h2[contains(text(), 'Resources')]";
        
        //Research page constants
        public static final String X_RESEARCH_BUTTON = "//div[@id='links']//a[contains(@href, 'page=research')]";
        public static final String X_RESEARCH_HEADER = "//div[@id='header_text']//h2[contains(text(), 'Research')]";
        
        //Facilities page constants
        public static final String X_FACILITIES_BUTTON = "//div[@id='links']//a[contains(@href, 'page=station')]";
        public static final String X_FACILITIES_HEADER = "//div[@id='header_text']//h2[contains(text(), 'Facilities')]";
        
        //Shipyard page constants
        public static final String X_SHIPYARD_BUTTON = "//div[@id='links']//a[contains(@href, 'page=shipyard')]";
        public static final String X_SHIPYARD_HEADER = "//div[@id='header_text']//h2[contains(text(), 'Shipyard')]";
        
        //Defense page constants
        public static final String X_DEFENSE_BUTTON = "//div[@id='links']//a[contains(@href, 'page=defense')]";
        public static final String X_DEFENSE_HEADER = "//div[@id='header_text']//h2[contains(text(), 'Defense')]";
        
        //Fleet page constants
        public static final String X_FLEET_BUTTON = "//div[@id='links']//a[contains(@href, 'page=fleet')]";
        public static final String X_FLEET_HEADER = "//div[@id='header_text']//h2[contains(text(), 'Fleet')]";
        
        //Planets
        public static final String X_PLANET = "//div[contains(@class, 'smallplanet')]";
        public static final String X_PLANET_NAME = "./a/span[@class='planet-name']";
        public static final String X_PLANET_COORDS = "./a/span[@class='planet-koords']";
        
        //Moons
        //TODO: Implement moons
        
        //RNG
        public static Random rnd = new Random();
        
        /**
         * Loads all planets
         * Driver must be logged in. 
         * 
         * @param driver 
         */
        public static List<Planet> getPlanets(WebDriver driver) {
                //Check that we're logged in
                while(!isLoggedIn(driver)) {
                        login(driver);
                }
                
                //Scrape planets
                List<WebElement> planetElements = driver.findElements(By.xpath(X_PLANET));

                return parsePlanets(planetElements);                    
        }
        
        private static List<Planet> parsePlanets(List<WebElement> planetElements) {
                List<Planet> planets = new ArrayList<Planet>();
                
                for(WebElement element : planetElements) {
                        //Get planet name
                        WebElement nameElement = element.findElement(By.xpath(X_PLANET_NAME));
                        WebElement coordsElement = element.findElement(By.xpath(X_PLANET_COORDS));
                        
                        String planetName = nameElement.getText() + " - " + coordsElement.getText();
                        
                        planets.add(new Planet(planetName));
                }
                
                return planets;
        }

        /**
         * Logs in from the login page
         * @param driver
         */
        public static void login(WebDriver driver) {
                //Setup
                WebElement element;
                WebDriverWait wait = new WebDriverWait(driver, 10);
                
                //Go to login page
                driver.get(LOGIN_URL);
                
                //Validate page
                try {
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(ID_SHOW_LOGIN_MENU)));
                } catch(Exception e) {
                        logger.error("Could not validate login page!", e);
                        return;
                }
                
                //Show login menu
                element = driver.findElement(By.id(ID_SHOW_LOGIN_MENU));
                element.click();
                sleep();
                
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_UNIVERSE_BOX)));
                
                //Select uni
                element = driver.findElement(By.xpath(X_UNIVERSE_OPTION));
                element.click();
                sleep();
                
                //Enter username
                element = driver.findElement(By.id(ID_USERNAME_BOX));
                element.sendKeys(USERNAME);
                sleep();
                
                //Enter password
                element = driver.findElement(By.id(ID_PASSWORD_BOX));
                element.sendKeys(PASSWORD);
                sleep();
                
                //Click login button
                element = driver.findElement(By.id(ID_LOGIN_BUTTON));
                element.click();
                sleep();
        }
        
        public static void goToPage(WebDriver driver, Planet planet, Page page) {
                //Go to planet
                goToPlanet(driver, planet);
                
                //Switch page
                switch(page) {
                case RESOURCES:
                        clickSideLink(driver, X_RESOURCES_BUTTON, X_RESOURCES_HEADER);
                        break;
                case FACILITIES:
                        clickSideLink(driver, X_FACILITIES_BUTTON, X_FACILITIES_HEADER);
                        break;
                case RESEARCH:
                        clickSideLink(driver, X_RESEARCH_BUTTON, X_RESEARCH_HEADER);
                        break;
                case SHIPYARD:
                        clickSideLink(driver, X_SHIPYARD_BUTTON, X_SHIPYARD_HEADER);
                        break;
                case DEFENSE:
                        clickSideLink(driver, X_DEFENSE_BUTTON, X_DEFENSE_HEADER);
                        break;
                case FLEET:
                        clickSideLink(driver, X_FLEET_BUTTON, X_FLEET_HEADER);
                        break;
                }
        }
        
        /**
         * Navigates to a particular planet.
         * 
         * @param driver
         * @param planet
         */
        public static void goToPlanet(WebDriver driver, Planet planet) {
                //Setup
                WebElement element;
                WebDriverWait wait = new WebDriverWait(driver, 10);
                
                //Check that we're logged in
                while(!isLoggedIn(driver)) {
                        login(driver);
                }
                
                //Scrape planets
                List<WebElement> planetElements = driver.findElements(By.xpath(X_PLANET));
                
                //Find proper planet
                for(WebElement planetElement : planetElements) {
                        try {
                                element = planetElement.findElement(By.xpath("//a[contains(@href, \"" + planet.getPlanetName() + "\")]"));
                                
                                //If we get here, the planet was found.
                                element.click();
                                
                                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(X_LOGOUT_BUTTON)));
                        } catch(Exception e) {
                                //findElement throws an exception if not found.  Ignore.  
                        }
                }
        }
        
        /**
         * Clicks a side link to access OGame pages
         * @param driver WebDriver
         * @param link XPath of link to click
         * @param verify XPath of element to verify page load
         */
        private static void clickSideLink(WebDriver driver, String link, String verify) {
                //Setup
                WebElement element;
                WebDriverWait wait = new WebDriverWait(driver, 10);
                
                //Check that we're logged in
                while(!isLoggedIn(driver)) {
                        login(driver);
                }
                
                //Click research button
                element = driver.findElement(By.xpath(link));
                element.click();
                
                //Validate page
                try {
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(verify)));
                } catch(Exception e) {
                        logger.error("Could not validate page: " + link, e);
                        return;
                }
        }

        /**
         * Tests to see if we are logged in
         * @param driver
         * @return
         */
        public static boolean isLoggedIn(WebDriver driver) {
                try {
                        WebDriverWait wait = new WebDriverWait(driver, 15);
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(X_LOGOUT_BUTTON)));
                } catch(Exception e) {
                        logger.error("Failed to confirm login!", e);
                        return false;
                }
                
                return true;
        }

        /**
         * Sleeps for a random amount of time to more accurately simulate a human
         */
        public static void sleep() {
                try {
                        Thread.sleep(getClickDelay());
                } catch(InterruptedException e) {
                        e.printStackTrace();
                }
        }
        
        /**
         * @return A random click delay in ms
         */
        private static int getClickDelay() {
                return rnd.nextInt(2000) + 500; //Delay 500-2000 ms
        }
}