package com.jlcavanagh.ogamebot.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

import com.jlcavanagh.ogamebot.data.Account;
import com.jlcavanagh.ogamebot.data.Planet;
import com.jlcavanagh.ogamebot.persistence.Config;
import com.jlcavanagh.ogamebot.web.construction.ConstructItem;

/**
 * Main window for OgameBot
 * Generated dynamically based on configuration.
 * Adding, removing or loading an Account will cause the UI to refresh itself completely.
 * When a Planet is added or removed, the planets UI for that account will be regenerated. 
 * 
 * @author Phil
 *
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Observer {
        //Singleton instance
        private static MainWindow instance;
        
        //Main window
        JPanel mainPanel;
        
        //Card panel constants
        private static final String CARD_DEFAULT = "Default";
        private static final String CARD_ACCOUNTS = "Accounts";
        
        //Account tabs
        private JTabbedPane accountTabs;
        
        //Logging
        Logger logger = Logger.getLogger(MainWindow.class.getSimpleName());
        
        public static MainWindow getInstance() {
                if(instance == null) {
                        instance = new MainWindow();
                }
                
                return instance;
        }
        
        private MainWindow() {
                //Subscribe to config events
                Config.getInstance().addObserver(this);
                
                // Build UI
                JFrame frame = new JFrame("OgameBot");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(800, 600));
                
                //Main panel
                mainPanel = new JPanel(new CardLayout());
                
                //Account tabs
                accountTabs = new JTabbedPane(JTabbedPane.LEFT);
                
                //Default panel
                JPanel defaultPanel = new JPanel();
                defaultPanel.setLayout(new BorderLayout());
                JLabel defaultLabel = new JLabel("Please add an account from the Tools menu.");
                defaultLabel.setHorizontalAlignment(JLabel.CENTER);
                
                //Add filler JPanels so label is centered
                defaultPanel.add(new JPanel(), BorderLayout.EAST);
                defaultPanel.add(new JPanel(), BorderLayout.WEST);
                
                //Add default to frame
                mainPanel.add(defaultLabel, CARD_DEFAULT);
                mainPanel.add(accountTabs, CARD_ACCOUNTS);
                
                // Add main frame components
                frame.setJMenuBar(createMenuBar());
                frame.add(mainPanel);
                frame.pack();

                // Show frame
                frame.setVisible(true);
        }
        
        private void regenerateUI(List<Account> accounts) {
                //Remove and recreate account tabs
                accountTabs.removeAll();
                for(Account acct : accounts) {
                        JPanel panel = createAccountTab(acct);
                        accountTabs.addTab(acct.toString(), panel);
                }
        }
        
        private JMenuBar createMenuBar() {
                JMenuBar menuBar = new JMenuBar();
                
                //Tools
                JMenu toolsMenu = new JMenu("Tools");
                JMenuItem addAccountItem = new JMenuItem("Add Account...");
                addAccountItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                                //Create dialog fields
                                JTextField usernameBox = new JTextField();
                                JPasswordField passwordBox = new JPasswordField();
                                JTextField universeUrlBox = new JTextField();
                                
                                final JComponent[] inputs = new JComponent[] {
                                        new JLabel("Username: "),
                                        usernameBox,
                                        new JLabel("Password: "),
                                        passwordBox,
                                        new JLabel("Universe URL Prefix: "),
                                        universeUrlBox
                                };
                                
                                //Show dialog
                                JOptionPane.showMessageDialog(null, inputs, "New Account", JOptionPane.PLAIN_MESSAGE);
                                
                                //Get values
                                String username = usernameBox.getText();
                                String password = new String(passwordBox.getPassword());
                                String universeUrl = universeUrlBox.getText();
                                
                                //Create account and store
                                Account acct = new Account(username, password, universeUrl);
                                Config.getInstance().addAccount(acct);
                        }
                });
                
                //Add tools menu items
                toolsMenu.add(addAccountItem);
                
                //Add menus to bar
                menuBar.add(toolsMenu);
                
                return menuBar;
        }
        
        /**
         * Creates the construction queue panel
         * 
         * @return Construction queue panel
         */
        private JPanel createQueuePanel() {
                // Container
                JPanel queuePanel = new JPanel();
                queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.Y_AXIS));

                // List of ConstructItems
                final JList itemList = new JList();
                itemList.setModel(new DefaultListModel());

                // Combo box of all construction items
                final JComboBox box = new JComboBox(ConstructItem.values());

                // Add construct item button
                JButton addButton = new JButton("Add");
                addButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                // Get ConstructItem
                                ConstructItem item = (ConstructItem) box.getSelectedItem();

                                // Add to list
                                DefaultListModel model = (DefaultListModel) itemList.getModel();
                                model.addElement(item);
                        }
                });

                // Button/combobox panel above list
                JPanel addPanel = new JPanel();
                addPanel.add(box);
                addPanel.add(addButton);

                // List control buttons
                JPanel listControlPanel = new JPanel();
                listControlPanel.setLayout(new BoxLayout(listControlPanel,
                                BoxLayout.Y_AXIS));

                // Up button
                final JButton upButton = new JButton("\u2191");
                upButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                // Move selected list item up
                                int moveMe = itemList.getSelectedIndex();
                                
                                if (moveMe != 0) {
                                        // Not already at top
                                        DefaultListModel model = (DefaultListModel) itemList.getModel();
                                        swap(model, moveMe, moveMe - 1);
                                        itemList.setSelectedIndex(moveMe - 1);
                                        itemList.ensureIndexIsVisible(moveMe - 1);
                                }
                        }
                });

                // Down button
                final JButton downButton = new JButton("\u2193");
                downButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                // Move selected list item down
                                int moveMe = itemList.getSelectedIndex();
                                DefaultListModel model = (DefaultListModel) itemList.getModel();
                                
                                if (moveMe != model.getSize() - 1) {
                                        // Not already at bottom
                                        swap(model, moveMe, moveMe + 1);
                                        itemList.setSelectedIndex(moveMe + 1);
                                        itemList.ensureIndexIsVisible(moveMe + 1);
                                }
                        }
                });

                // Remove button
                final JButton removeButton = new JButton("X");
                removeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                // Remove selected list item
                                ListSelectionModel lsm = itemList.getSelectionModel();
                                int firstSelected = lsm.getMinSelectionIndex();
                                int lastSelected = lsm.getMaxSelectionIndex();

                                DefaultListModel model = (DefaultListModel) itemList.getModel();
                                model.removeRange(firstSelected, lastSelected);

                                int size = model.size();

                                if (size == 0) {
                                        // List is empty: disable delete, up, and down buttons.
                                        removeButton.setEnabled(false);
                                        upButton.setEnabled(false);
                                        downButton.setEnabled(false);

                                } else {
                                        // Adjust the selection.
                                        if (firstSelected == model.getSize()) {
                                                // Removed item in last position.
                                                firstSelected--;
                                        }
                                        itemList.setSelectedIndex(firstSelected);
                                }
                        }
                });

                listControlPanel.add(upButton);
                listControlPanel.add(downButton);
                listControlPanel.add(removeButton);

                // List scrollpane
                JScrollPane scrollPane = new JScrollPane(itemList);

                // List + Scrollpane + list buttons
                JPanel listPanel = new JPanel();
                listPanel.add(scrollPane);
                listPanel.add(listControlPanel);

                queuePanel.add(addPanel);
                queuePanel.add(listPanel);

                return queuePanel;
        }
        
        private JPanel createAccountTab(Account acct) {
                JPanel acctTab = new JPanel();
                acctTab.setLayout(new BorderLayout());
                
                JTabbedPane planetTabs = new JTabbedPane(JTabbedPane.TOP);
                
                for(Planet planet : acct.getPlanets()) {
                        planetTabs.addTab(planet.getPlanetName(), createPlanetTab(planet));
                }
                
                //Add tabs to account panel
                acctTab.add(planetTabs);
                
                return acctTab;
        }
        
        private JPanel createPlanetTab(Planet planet) {
                JPanel tab = new JPanel();
                tab.setLayout(new BorderLayout());
                tab.add(createQueuePanel(), BorderLayout.CENTER);
                
                return tab;
        }
        
        // Swap two elements in the list.
        private void swap(DefaultListModel listModel, int a, int b) {
                Object aObject = listModel.getElementAt(a);
                Object bObject = listModel.getElementAt(b);
                listModel.set(a, bObject);
                listModel.set(b, aObject);
        }

        @Override
        public void update(Observable config, Object updateObj) {
                //Regenerate UI based on new data
                System.out.println("Got update!");
                System.out.println(config);
                System.out.println(updateObj);
                
                try {
                        List<Account> accounts = (List<Account>)updateObj;
                        
                        //Create new tabs if we have accounts, otherwise show the default panel
                        if(accounts.size() > 0) {
                                regenerateUI(accounts);
                                
                                //Show accounts
                                ((CardLayout)mainPanel.getLayout()).show(mainPanel, CARD_ACCOUNTS);
                        } else {
                                //Reset frame to default
                                ((CardLayout)mainPanel.getLayout()).show(mainPanel, CARD_DEFAULT);
                        }
                        
                        //Render UI
                        mainPanel.validate();
                } catch(ClassCastException e) {
                        logger.error(e);
                }
        }
}