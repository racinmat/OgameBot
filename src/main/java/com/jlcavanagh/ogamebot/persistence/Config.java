package com.jlcavanagh.ogamebot.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import com.jlcavanagh.ogamebot.data.Account;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="config")
public class Config extends Observable {
        private static final Logger logger = Logger.getLogger(Config.class.getSimpleName());
        
        //Constants
        public static final String APP_INTERNAL_NAME = "ogamebot";
        public static final String APP_CONFIG_FILE_NAME = APP_INTERNAL_NAME + ".xml";
        public static final String APP_CONFIG_PATH = System.getProperty("user.dir") + File.separator + APP_CONFIG_FILE_NAME;
        
        //Master list of accounts
        @XmlElementWrapper(name="accounts")
        @XmlElement(name="account")
        private List<Account> accounts;
        
        //Singleton instance
        private static Config instance;
        
        public static Config getInstance() {
                if(instance == null) {
                        instance = new Config();
                }
                
                return instance;
        }
        
        private Config() {
                accounts = new ArrayList<Account>();
        }

        public void addAccount(Account acct) {
                if(acct != null) {
                        accounts.add(acct);
                }
                
                fireAccountsChanged();
        }
        
        public void fireAccountsChanged() {
                //Notify observers
                this.setChanged();
                this.notifyObservers(accounts);
                
                //Save new config
                save();
        }

        public void save() {
                try {
                        JAXBContext jaxb = JAXBContext.newInstance(Config.class);
                        Marshaller m = jaxb.createMarshaller();
                        FileWriter writer = new FileWriter(new File(APP_CONFIG_PATH));
                        
                        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                        m.marshal(Config.getInstance(), writer);
                        m.marshal(Config.getInstance(), System.out);
                } catch (JAXBException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch(IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }
        
        public void load() {
                //Clear accounts
                
                //Load config
                
                //Add new accounts
        }
}