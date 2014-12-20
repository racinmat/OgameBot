package com.jlcavanagh.ogamebot.web.construction;

import static com.jlcavanagh.ogamebot.web.navigation.Page.FACILITIES;
import static com.jlcavanagh.ogamebot.web.navigation.Page.RESEARCH;
import static com.jlcavanagh.ogamebot.web.navigation.Page.RESOURCES;

import com.jlcavanagh.ogamebot.web.navigation.Page;

/**
 * Enumerates and creates xpath for constructing all buildings, research, fleet, and defense
 * 
 * @author Phil
 *
 */
public enum ConstructItem {
        //Resources
        METAL_MINE(RESOURCES, "supply1", "Metal Mine"),
        CRYSTAL_MINE(RESOURCES, "supply2", "Crystal Mine"),
        DEUT_MINE(RESOURCES, "supply3", "Deuterium Synthesizer"),
        SOLAR_PLANT(RESOURCES, "supply4", "Solar Plant"),
        FUSION_PLANT(RESOURCES, "supply12", "Fusion Plant"),
        SOLAR_SAT(RESOURCES, "supply212", "Solar Satellite"),
        METAL_STORAGE(RESOURCES, "supply22", "Metal Storage"),
        CRYSTAL_STORAGE(RESOURCES, "supply23", "Crystal Storage"),
        DEUT_STORAGE(RESOURCES, "supply24", "Deuterium Tank"),
        
        //Facilities
        ROBOTICS_FACTORY(FACILITIES, "station14", "Robotics Factory"),
        SHIPYARD(FACILITIES, "station21", "Shipyard"),
        RESEARCH_LAB(FACILITIES, "station31", "Research Lab"),
        ALLIANCE_DEPOT(FACILITIES, "station34", "Alliance Depot"),
        MISSILE_SILO(FACILITIES, "station44", "Missile Silo"),
        NANITE_FACTORY(FACILITIES, "station15", "Nanite Factory"),
        TERRAFORMER(FACILITIES, "station33", "Terraformer"),
        
        //Research
        LASER_TECH(RESEARCH, "research120", "Laser Technology"),
        ION_TECH(RESEARCH, "research121", "Ion Technology"),
        HYPERSPACE_TECH(RESEARCH, "research114", "Hyperspace Technology"),
        PLASMA_TECH(RESEARCH, "research122", "Plasma Technology"),
        COMBUSTION_DRIVE(RESEARCH, "research115", "Combustion Drive"),
        IMPULSE_DRIVE(RESEARCH, "research117", "Impulse Drive"),
        HYPERSPACE_DRIVE(RESEARCH, "research118", "Hyperspace Drive"),
        ESPIONAGE_TECH(RESEARCH, "research106", "Espionage Technology"),
        COMPUTER_TECH(RESEARCH, "research108", "Computer Technology"),
        ASTROPHYSICS(RESEARCH, "research124", "Astrophysics"),
        INTERGAL_RESEARCH(RESEARCH, "research123", "Intergalactic Research Network"),
        GRAVITON_TECH(RESEARCH, "research199", "Graviton Technology"),
        WEAPONS_TECH(RESEARCH, "research109", "Weapons Technology"),
        SHIELDING_TECH(RESEARCH, "research110", "Shielding Technology"),
        ARMOR_TECH(RESEARCH, "research111", "Armor Technology");
        
        private Page page;
        private String xpath;
        private String buttonXpath = "//a[contains(@class,'fastBuild')]/img";   //Appends xpath for actual button to click on page
        private String errorXpath = "//a[contains(@id, 'details')]";    //Appends xpath for construct failure error message
        private String displayName = "";

        private ConstructItem(Page page, String classId, String displayName) {
                this.setPage(page);
                this.setXpath("//div[@class='" + classId + "']");
                this.setButtonXpath(this.getXpath() + buttonXpath);
                this.setErrorXpath(this.getXpath() + errorXpath);
                this.setDisplayName(displayName);
        }

        public void setPage(Page page) {
                this.page = page;
        }

        public String getButtonXpath() {
                return buttonXpath;
        }

        public void setButtonXpath(String constructButtonXpath) {
                this.buttonXpath = constructButtonXpath;
        }

        public String getErrorXpath() {
                return errorXpath;
        }

        public void setErrorXpath(String constructErrorXpath) {
                this.errorXpath = constructErrorXpath;
        }

        public Page getPage() {
                return page;
        }

        public void setXpath(String constructXpath) {
                this.xpath = constructXpath;
        }

        public String getXpath() {
                return xpath;
        }
        
        public String getDisplayName() {
                return displayName;
        }

        public void setDisplayName(String displayName) {
                this.displayName = displayName;
        }
        
        @Override
        public String toString() {
                return displayName; 
        }
}