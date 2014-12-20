package com.jlcavanagh.ogamebot.web;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.jlcavanagh.ogamebot.data.Account;
import com.jlcavanagh.ogamebot.data.Planet;
import com.jlcavanagh.ogamebot.persistence.Config;
import com.jlcavanagh.ogamebot.web.construction.ConstructItem;
import com.jlcavanagh.ogamebot.web.construction.Construction;
import com.jlcavanagh.ogamebot.web.navigation.Navigation;

/**
 * Facade for all website interaction.
 * Manages WebDrivers for all Accounts.
 * 
 * @author Phil
 *
 */
public class Web implements Observer {
        private static final Logger logger = Logger.getLogger(Web.class.getSimpleName());
        
        //Driver map
        private Map<Account, WebDriver> webDrivers;
        
        //Instance
        private static Web instance;
        
        //Refresh timer/instance
        private Timer refreshTimer;
        private WebRefresh webRefresh;
        private static final long REFRESH_INTERVAL = 60000;     //One minute
        
        //Singleton
        public static Web getInstance() {
                if(instance == null) {
                        instance = new Web();
                }
                
                return instance;
        }
        
        private Web() {
                webDrivers = new HashMap<Account, WebDriver>();
                webRefresh = new WebRefresh();
                
                //Subscribe to Account events
                Config.getInstance().addObserver(this);
                
                //Timer
                long startTime = Calendar.getInstance().getTimeInMillis() + REFRESH_INTERVAL;
                
                refreshTimer = new Timer();
                refreshTimer.scheduleAtFixedRate(webRefresh, startTime, REFRESH_INTERVAL);
        }
        
        /**
         * Construct something on an account's planet
         * 
         * @param account
         * @param planet
         * @param item
         */
        public void construct(Account account, Planet planet, ConstructItem item, int quantity) {
                //Get driver
                WebDriver driver = getWebDriver(account);
                synchronized(driver) {
                        //Navigate to planet and page
                        Navigation.goToPage(driver, planet, item.getPage());
                        
                        //Build!
                        Construction.construct(driver, planet, item);
                }
        }
        
        /**
         * Initializes a newly created WebDriver
         */
        private void initialize(Account account, WebDriver driver) {
                synchronized(driver) {
                        Navigation.login(driver);
                        List<Planet> planets = Navigation.getPlanets(driver);
                        account.setPlanets(planets);
                }
        }

        /**
         * Return the webdriver for this account, or create it if it does not exist
         * 
         * @param account
         * @return WebDriver
         */
        private WebDriver getWebDriver(Account account) {
                if(webDrivers.containsKey(account)) {
                        return webDrivers.get(account);
                } else {
                        WebDriver driver = new FirefoxDriver();
                        initialize(account, driver);
                        
                        //Put in map
                        webDrivers.put(account, driver);
                        
                        return driver;
                }
        }
        
        private class WebRefresh extends TimerTask {
                @Override
                public void run() {
                        //Refresh planets, attacks, etc for each account
                        for(Entry<Account, WebDriver> entry : webDrivers.entrySet()) {
                                Account account = entry.getKey();
                                WebDriver driver = entry.getValue();
                                
                                synchronized(driver) {
                                        //Do checks for attacks, planets, etc
                                }
                        }
                }
        }

        @Override
        public void update(Observable o, final Object arg) {
                //Run this in a new thread
                Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                                //Update object should always be a list of Accounts
                                try {
                                        List<Account> accounts = (List<Account>)arg;
                                        
                                        synchronized(webDrivers) {
                                                //Diff against existing accounts
                                                if(accounts.size() > webDrivers.size()) {
                                                        //Account added
                                                        for(Account account : accounts) {
                                                                if(!webDrivers.keySet().contains(account)) {
                                                                        //Create and start
                                                                        getWebDriver(account);
                                                                }
                                                        }
                                                } else if (accounts.size() < webDrivers.size()){
                                                        //Account removed
                                                        
                                                        for(Entry<Account, WebDriver> entry : webDrivers.entrySet()) {
                                                                if(!accounts.contains(entry.getKey())) {
                                                                        //This account was removed
                                                                        //Quit webdriver
                                                                        entry.getValue().quit();
                                                                        
                                                                        //Remove from map
                                                                        webDrivers.remove(entry.getKey());
                                                                }
                                                        }
                                                }
                                        }
                                } catch(ClassCastException e) {
                                        logger.error("Error in Web observer handler!", e);
                                }
                        }
                });
                
                t.start();
        }
}