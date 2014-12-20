package com.jlcavanagh.ogamebot.web.navigation;

public enum Page {
        RESOURCES("Resources"), FACILITIES("Facilities"), RESEARCH("Research"), SHIPYARD("Shipyard"), DEFENSE("Defense"), FLEET("Fleet");
        
        private String displayName;
        
        private Page(String d) {
                this.setDisplayName(d);
        }
        
        public void setDisplayName(String displayName) {
                this.displayName = displayName;
        }

        public String getDisplayName() {
                return displayName;
        }
}