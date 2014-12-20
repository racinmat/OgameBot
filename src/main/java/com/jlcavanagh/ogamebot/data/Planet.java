package com.jlcavanagh.ogamebot.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.jlcavanagh.ogamebot.web.construction.ConstructItem;

/**
 * Encapsulates a planet owned by the player
 * 
 * @author Phil
 *
 */
@XmlRootElement(name="planet")
public class Planet {
        private String planetName;
        
        private List<ConstructItem> constructItems;
        
        public Planet() {
                this("");
        }
        
        public Planet(String name) {
                this.setPlanetName(name);
        }
        
        public void setPlanetName(String planetName) {
                this.planetName = planetName;
        }
        public String getPlanetName() {
                return planetName;
        }
        @XmlElementWrapper(name="constructItems")
        @XmlElement(name="constructItem")
        public List<ConstructItem> getConstructItems() {
                return constructItems;
        }
}       