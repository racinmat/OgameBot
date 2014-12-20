package com.jlcavanagh.ogamebot;

import org.apache.log4j.PropertyConfigurator;

import com.jlcavanagh.ogamebot.persistence.Config;
import com.jlcavanagh.ogamebot.ui.MainWindow;
import com.jlcavanagh.ogamebot.web.Web;

public class Main {
        static {
                PropertyConfigurator.configure("log4j.properties");
        }

        /**
         * @param args
         */
        public static void main(String[] args) {
                //Create singletons
                Config.getInstance();
                Web.getInstance();
                MainWindow.getInstance();
        }
}