package com.jlcavanagh.ogamebot.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.jlcavanagh.ogamebot.persistence.Config;

/**
 * An OGame account.
 * Contains all app data needed to use this account
 * 
 * @author Phil
 *
 */
@XmlRootElement(name="account")
public class Account {
        //Credentials
        private String server;                  //Server e.g. ogame.org
        private String username;
        private String password;        
        private String universeUrl;             //Should be URL prefix e.g. "uni113"
        private String universeName;    //Universe display name.  May not be set.
        
        //Data
        private List<Planet> planets;
        
        public Account() {
                this("", "", "");
        }
        
        public Account(String username, String password, String universeUrl) {
                //Set fields
                this.username = username;
                this.password = password;
                this.universeUrl = universeUrl;
                this.planets = new ArrayList<Planet>();
        }
        
        public void addPlanet(Planet p) {
                this.planets.add(p);
                
                Config.getInstance().fireAccountsChanged();
        }
        
        @Override
        public String toString() {
                //Return username + universeName if set, otherwise username + universeUrl
                return username + " - " + (universeName == null || universeName.isEmpty() ? universeUrl : universeName);
        }
        
        public void setServer(String server) {
                this.server = server;
        }
        public String getServer() {
                return server;
        }
        public void setUsername(String username) {
                this.username = username;
        }
        public String getUsername() {
                return username;
        }
        public void setPassword(String password) {
                this.password = password;
        }
        public String getPassword() {
                return password;
        }
        public void setUniverseUrl(String universe) {
                this.universeUrl = universe;
        }
        public String getUniverseUrl() {
                return universeUrl;
        }
        @XmlElementWrapper(name="planetList")
        @XmlElement(name="planet")
        public void setPlanets(List<Planet> planets) {
                this.planets = planets;
                
                Config.getInstance().fireAccountsChanged();
        }
        public List<Planet> getPlanets() {
                return planets;
        }

        @Override
        public boolean equals(Object obj) {
                if(obj instanceof Account) {
                        Account account = (Account)obj;
                        return (server.equals(account.server) &&
                                        username.equals(account.username) &&
                                        password.equals(account.password) && 
                                        universeUrl.equals(account.universeUrl) && 
                                        planets.size() == account.planets.size());
                }
                
                return false;
        }

        @Override
        public int hashCode() {
                return (server + username + password + universeUrl).hashCode();
        }
}