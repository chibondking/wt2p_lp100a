package com.wt2p.lp100a;

import com.serialpundit.core.SerialComException;
import com.serialpundit.serial.SerialComManager;
import java.awt.Color;
import java.io.IOException;

/**
 *
 * @author cedrickj
 */
public class PowerDisplay extends javax.swing.JFrame {

    private static SerialComManager serialComManager;
    private static long comPortHandle;
    private static String comPort;
    private static boolean isUsingLatestLPFirmware;

    /**
     * Creates new form PowerDisplay
     */
    public PowerDisplay() {
        initComponents();
    }

    private static void connectToComPort() throws SerialComException, IOException {
        serialComManager = new SerialComManager();
        comPortHandle = serialComManager.openComPort(comPort, true, true, true);
        serialComManager.configureComPortData(comPortHandle, SerialComManager.DATABITS.DB_8, SerialComManager.STOPBITS.SB_1, SerialComManager.PARITY.P_NONE, SerialComManager.BAUDRATE.B115200, 0);
        serialComManager.configureComPortControl(comPortHandle, SerialComManager.FLOWCONTROL.NONE, 'x', 'x', false, false);
    }

    private static void startReadingFromComPort() throws SerialComException, InterruptedException {
        while (true) {
            Thread.sleep(10);
            serialComManager.writeString(comPortHandle, "P", 2);
            Thread.sleep(30);
            String data = serialComManager.readString(comPortHandle);
            parseStringFromLP100A(data);

        }
    }

    private static void updateStatusField(String value, boolean errorCondition) {
        
        jlStatusField.setVisible(true);
        
        if (errorCondition) {
            jlStatusField.setText(value);
            jlStatusField.setForeground(Color.RED);
        } else {
            jlStatusField.setText(value);
            jlStatusField.setForeground(Color.GREEN);
        }
        
    }

    private static void parseStringFromLP100A(String data) {
        String[] dataArray = data.substring(1).split(",");

        try {
            PowerDataDto powerDto = new PowerDataDto(dataArray, isUsingLatestLPFirmware);
            updateUserInterface(powerDto);
        } catch (Exception ex) {
            //Debugging only. Need to add application logging and set this to DEBUG.
        }
    }

    private static void updateUserInterface(PowerDataDto dto) {
        updateForwardPower(dto);
        updateForwardPowerBargraph(dto);
        updateSWRBargraph(dto);
        updateSWR(dto);
        updateStatusField("Connected", false);
        jl_ConnectedIcon.setVisible(true);
    }

    private static void updateForwardPowerBargraph(PowerDataDto dto) {
        if (dto.getForwardPower() >= 0) {
            jpPwrLow.setValue(dto.getForwardPower().intValue());
        }

        if (dto.getForwardPower() > 100) {
            jpPwrLowMid.setValue(dto.getForwardPower().intValue());
        }
        if (dto.getForwardPower() < 100) {
            jpPwrLowMid.setValue(0);
        }

        if (dto.getForwardPower() >= 500) {
            jpPwrMid.setValue(dto.getForwardPower().intValue());
        } else if (dto.getForwardPower() < 500) {
            jpPwrMid.setValue(0);
        }

        if (dto.getForwardPower() > 1000 && dto.getForwardPower() <= 1500) {
            jpPwrHigh.setValue(dto.getForwardPower().intValue());
        } else if (dto.getForwardPower() < 1000) {
            jpPwrHigh.setValue(0);
        }

        if (dto.getForwardPower() > 1500 && dto.getForwardPower() <= 2000) {
            jpPwrHighHigh.setValue(dto.getForwardPower().intValue());
            jpPwrHigh.setValue(dto.getForwardPower().intValue());
            jlPowerText.setForeground(Color.RED);
        } else if (dto.getForwardPower() <= 1500) {
            jpPwrHighHigh.setValue(0);
            jlPowerText.setForeground(Color.WHITE);
        }

        if (dto.getForwardPower() == 0) {
            jpPwrLow.setValue(0);
            jpPwrLowMid.setValue(0);
            jpPwrMid.setValue(0);
            jpPwrHigh.setValue(0);
        }

    }

    private static void updateForwardPower(PowerDataDto dto) {
        jlPowerText.setText(dto.getFormattedForwardPower());
    }

    private static void updateSWRBargraph(PowerDataDto dto) {

        if (dto.getSWR() >= 1.01) {
            jpSWRLow.setValue(dto.getSWRInteger());
        }

        if (dto.getSWR() >= 1.50) {
            jpSWRMedium.setValue(dto.getSWRInteger());
        }

        if (dto.getSWR() >= 2.00) {
            jpSWRWarning.setValue(dto.getSWRInteger());
        }

        if (dto.getSWR() >= 2.50) {
            jpSWRHigh.setValue(dto.getSWRInteger());
            jl_SWR.setForeground(new java.awt.Color(204, 102, 0));
        }

        if (dto.getSWR() > 3.0) {
            jlSWRAlarm.setForeground(Color.RED);
            jl_SWR.setForeground(Color.RED);
        }

        try {
            //Try to make it so the SWR bargraph doesn't flicker??
            Thread.sleep(100);
        } catch (Exception ex) {
            //Ignore
        }

        if (dto.getSWR() == 1.00) {
            jpSWRLow.setValue(1);
            jpSWRMedium.setValue(0);
            jpSWRWarning.setValue(0);
            jpSWRHigh.setValue(0);
            jlSWRAlarm.setForeground(Color.BLACK);
        }
    }

    private static void updateSWR(PowerDataDto dto) {

       if (dto.getSWR() == 1.00) {
            // Do nothing, keep the last number displayed
            jl_SWR.setForeground(Color.WHITE);
        } else {
            jl_SWR.setText(Double.toString(dto.getSWR()));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jl_Power = new javax.swing.JLabel();
        jlSWR = new javax.swing.JLabel();
        jpPwrBargraphPanel = new javax.swing.JPanel();
        jpPwrLow = new javax.swing.JProgressBar();
        jlPowerText = new javax.swing.JLabel();
        jpPwrLowMid = new javax.swing.JProgressBar();
        jpPwrHigh = new javax.swing.JProgressBar();
        jpPwrMid = new javax.swing.JProgressBar();
        jpPwrHighHigh = new javax.swing.JProgressBar();
        jpSWRPanel = new javax.swing.JPanel();
        jl_SWR = new javax.swing.JLabel();
        jpSWRLow = new javax.swing.JProgressBar();
        jpSWRMedium = new javax.swing.JProgressBar();
        jpSWRWarning = new javax.swing.JProgressBar();
        jpSWRHigh = new javax.swing.JProgressBar();
        jlPwr0 = new javax.swing.JLabel();
        jlPwr100 = new javax.swing.JLabel();
        jlPwr50 = new javax.swing.JLabel();
        jlPwr300 = new javax.swing.JLabel();
        jlPwr500 = new javax.swing.JLabel();
        jlPwr1000 = new javax.swing.JLabel();
        jlPwr2000 = new javax.swing.JLabel();
        jlSWR1 = new javax.swing.JLabel();
        jlSWR15 = new javax.swing.JLabel();
        jlSWR20 = new javax.swing.JLabel();
        jlSWR25 = new javax.swing.JLabel();
        jlSWR30 = new javax.swing.JLabel();
        jlSWRAlarm = new javax.swing.JLabel();
        jlPwr1500 = new javax.swing.JLabel();
        jlStatusField = new javax.swing.JLabel();
        jl_ConnectedIcon = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WT2P LP-100A Utility");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(0, 0, 0));
        setLocationByPlatform(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setBackground(new java.awt.Color(0, 0, 0));
        mainPanel.setForeground(new java.awt.Color(255, 255, 255));
        mainPanel.setMaximumSize(new java.awt.Dimension(254, 279));
        mainPanel.setMinimumSize(new java.awt.Dimension(254, 279));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jl_Power.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jl_Power.setForeground(new java.awt.Color(255, 255, 255));
        jl_Power.setText("PWR");
        mainPanel.add(jl_Power, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 30));

        jlSWR.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jlSWR.setForeground(new java.awt.Color(255, 255, 255));
        jlSWR.setText("SWR");
        mainPanel.add(jlSWR, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, 25));

        jpPwrBargraphPanel.setBackground(new java.awt.Color(0, 0, 0));
        jpPwrBargraphPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpPwrLow.setBackground(new java.awt.Color(0, 47, 63));
        jpPwrLow.setForeground(new java.awt.Color(21, 166, 215));
        jpPwrLow.setBorder(null);
        jpPwrLow.setBorderPainted(false);
        jpPwrLow.setString("0");
        jpPwrBargraphPanel.add(jpPwrLow, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 0, 115, 30));

        jlPowerText.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jlPowerText.setForeground(new java.awt.Color(255, 255, 255));
        jlPowerText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jpPwrBargraphPanel.add(jlPowerText, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 30));

        jpPwrLowMid.setBackground(new java.awt.Color(0, 47, 63));
        jpPwrLowMid.setForeground(new java.awt.Color(21, 166, 215));
        jpPwrLowMid.setMaximum(500);
        jpPwrLowMid.setMinimum(100);
        jpPwrLowMid.setValue(0);
        jpPwrLowMid.setBorder(null);
        jpPwrLowMid.setBorderPainted(false);
        jpPwrBargraphPanel.add(jpPwrLowMid, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 90, 30));

        jpPwrHigh.setBackground(new java.awt.Color(42, 17, 0));
        jpPwrHigh.setForeground(new java.awt.Color(249, 136, 60));
        jpPwrHigh.setMaximum(1500);
        jpPwrHigh.setMinimum(1000);
        jpPwrHigh.setValue(0);
        jpPwrHigh.setBorder(null);
        jpPwrHigh.setBorderPainted(false);
        jpPwrBargraphPanel.add(jpPwrHigh, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 30, 30));

        jpPwrMid.setBackground(new java.awt.Color(42, 17, 0));
        jpPwrMid.setForeground(new java.awt.Color(249, 136, 60));
        jpPwrMid.setMaximum(1000);
        jpPwrMid.setMinimum(500);
        jpPwrMid.setValue(0);
        jpPwrMid.setBorder(null);
        jpPwrMid.setBorderPainted(false);
        jpPwrBargraphPanel.add(jpPwrMid, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 40, 30));

        jpPwrHighHigh.setBackground(new java.awt.Color(49, 0, 0));
        jpPwrHighHigh.setForeground(new java.awt.Color(255, 0, 0));
        jpPwrHighHigh.setMaximum(2000);
        jpPwrHighHigh.setMinimum(1500);
        jpPwrHighHigh.setValue(0);
        jpPwrHighHigh.setBorder(null);
        jpPwrHighHigh.setBorderPainted(false);
        jpPwrBargraphPanel.add(jpPwrHighHigh, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 30, 30));

        mainPanel.add(jpPwrBargraphPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 11, 390, 30));

        jpSWRPanel.setBackground(new java.awt.Color(0, 0, 0));
        jpSWRPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jl_SWR.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jl_SWR.setForeground(new java.awt.Color(255, 255, 255));
        jl_SWR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_SWR.setMaximumSize(new java.awt.Dimension(44, 22));
        jl_SWR.setMinimumSize(new java.awt.Dimension(44, 22));
        jl_SWR.setPreferredSize(new java.awt.Dimension(44, 22));
        jpSWRPanel.add(jl_SWR, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 30));

        jpSWRLow.setBackground(new java.awt.Color(0, 47, 63));
        jpSWRLow.setForeground(new java.awt.Color(21, 166, 215));
        jpSWRLow.setMaximum(150);
        jpSWRLow.setMinimum(100);
        jpSWRLow.setValue(0);
        jpSWRLow.setBorder(null);
        jpSWRLow.setBorderPainted(false);
        jpSWRLow.setOpaque(true);
        jpSWRPanel.add(jpSWRLow, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 70, 30));

        jpSWRMedium.setBackground(new java.awt.Color(0, 0, 0));
        jpSWRMedium.setForeground(new java.awt.Color(204, 204, 0));
        jpSWRMedium.setMaximum(200);
        jpSWRMedium.setMinimum(150);
        jpSWRMedium.setValue(0);
        jpSWRMedium.setBorder(null);
        jpSWRMedium.setBorderPainted(false);
        jpSWRPanel.add(jpSWRMedium, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 50, 30));

        jpSWRWarning.setBackground(new java.awt.Color(42, 17, 0));
        jpSWRWarning.setForeground(new java.awt.Color(204, 102, 0));
        jpSWRWarning.setMaximum(250);
        jpSWRWarning.setMinimum(200);
        jpSWRWarning.setValue(0);
        jpSWRWarning.setBorder(null);
        jpSWRWarning.setBorderPainted(false);
        jpSWRPanel.add(jpSWRWarning, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 50, 30));

        jpSWRHigh.setBackground(new java.awt.Color(49, 0, 0));
        jpSWRHigh.setForeground(new java.awt.Color(255, 0, 0));
        jpSWRHigh.setMaximum(300);
        jpSWRHigh.setMinimum(250);
        jpSWRHigh.setValue(0);
        jpSWRHigh.setBorder(null);
        jpSWRHigh.setBorderPainted(false);
        jpSWRPanel.add(jpSWRHigh, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 50, 30));

        mainPanel.add(jpSWRPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 65, -1, 34));

        jlPwr0.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlPwr0.setForeground(new java.awt.Color(204, 204, 0));
        jlPwr0.setText("0");
        mainPanel.add(jlPwr0, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 14, -1));

        jlPwr100.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlPwr100.setForeground(new java.awt.Color(204, 204, 0));
        jlPwr100.setText("100");
        mainPanel.add(jlPwr100, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, -1, -1));

        jlPwr50.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlPwr50.setForeground(new java.awt.Color(204, 204, 0));
        jlPwr50.setText("50");
        mainPanel.add(jlPwr50, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, -1, -1));

        jlPwr300.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlPwr300.setForeground(new java.awt.Color(204, 204, 0));
        jlPwr300.setText("300");
        mainPanel.add(jlPwr300, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, -1, -1));

        jlPwr500.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlPwr500.setForeground(new java.awt.Color(204, 204, 0));
        jlPwr500.setText("500");
        mainPanel.add(jlPwr500, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, -1, -1));

        jlPwr1000.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlPwr1000.setForeground(new java.awt.Color(204, 204, 0));
        jlPwr1000.setText("1k");
        mainPanel.add(jlPwr1000, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, -1, -1));

        jlPwr2000.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlPwr2000.setForeground(new java.awt.Color(204, 204, 0));
        jlPwr2000.setText("2k");
        mainPanel.add(jlPwr2000, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, -1, -1));

        jlSWR1.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlSWR1.setForeground(new java.awt.Color(204, 204, 0));
        jlSWR1.setText("1");
        mainPanel.add(jlSWR1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, -1, -1));

        jlSWR15.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jlSWR15.setForeground(new java.awt.Color(204, 204, 0));
        jlSWR15.setText("1.5");
        mainPanel.add(jlSWR15, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, -1, -1));

        jlSWR20.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jlSWR20.setForeground(new java.awt.Color(204, 204, 0));
        jlSWR20.setText("2.0");
        mainPanel.add(jlSWR20, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, -1));

        jlSWR25.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jlSWR25.setForeground(new java.awt.Color(204, 204, 0));
        jlSWR25.setText("2.5");
        mainPanel.add(jlSWR25, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, -1, -1));

        jlSWR30.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jlSWR30.setForeground(new java.awt.Color(204, 204, 0));
        jlSWR30.setText("3.0");
        mainPanel.add(jlSWR30, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, -1, -1));

        jlSWRAlarm.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jlSWRAlarm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlSWRAlarm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/wt2p/lp100a/icons/cancel_icon_small.png"))); // NOI18N
        jlSWRAlarm.setText("SWR");
        mainPanel.add(jlSWRAlarm, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 90, 40));

        jlPwr1500.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlPwr1500.setForeground(new java.awt.Color(204, 204, 0));
        jlPwr1500.setText("1.5k");
        mainPanel.add(jlPwr1500, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 30, -1));

        jlStatusField.setBackground(new java.awt.Color(0, 0, 0));
        jlStatusField.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jlStatusField.setForeground(new java.awt.Color(153, 153, 153));
        jlStatusField.setMinimumSize(new java.awt.Dimension(230, 20));
        jlStatusField.setOpaque(true);
        jlStatusField.setPreferredSize(new java.awt.Dimension(230, 20));
        mainPanel.add(jlStatusField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 210, 20));

        jl_ConnectedIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/wt2p/lp100a/icons/launch_white.png"))); // NOI18N
        jl_ConnectedIcon.setFocusable(false);
        jl_ConnectedIcon.setRequestFocusEnabled(false);
        jl_ConnectedIcon.setVerifyInputWhenFocusTarget(false);
        mainPanel.add(jl_ConnectedIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 450, 150));

        menuBar.setBackground(new java.awt.Color(0, 0, 0));
        menuBar.setForeground(new java.awt.Color(102, 102, 102));
        menuBar.setBorderPainted(false);

        fileMenu.setBackground(new java.awt.Color(0, 0, 0));
        fileMenu.setForeground(new java.awt.Color(102, 102, 102));
        fileMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/wt2p/lp100a/icons/switch_profile_white.png"))); // NOI18N
        fileMenu.setMnemonic('f');

        exitMenuItem.setBackground(new java.awt.Color(0, 0, 0));
        exitMenuItem.setForeground(new java.awt.Color(255, 255, 51));
        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        try {
            serialComManager.closeComPort(comPortHandle);
        } catch (Exception ex) {
            // Do nothing
        }
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    public static void main(String args[]) {
        //TODO: Use a argument parser here to make things simpler
        if (args.length > 0) {
            comPort = args[0];
        } else {
            comPort = "COM1";
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            PowerDisplay pd = new PowerDisplay();
            pd.setVisible(true);
        });

        try {
            // Hackish way to prevent NPE's when you cannot connect to te LP-100A
            // We sleep for 300ms to allow for all of the UI elements to be
            // initialized before attempting to connect to the LP-100.
            Thread.sleep(400);
            connectToComPort();
            startReadingFromComPort();
            updateStatusField("Connected", false);
        } catch (IOException | InterruptedException ex) {
            updateStatusField("Connection error to LP-100A! Restart.", true);
            jl_ConnectedIcon.setVisible(false);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    public static javax.swing.JLabel jlPowerText;
    private javax.swing.JLabel jlPwr0;
    private javax.swing.JLabel jlPwr100;
    private javax.swing.JLabel jlPwr1000;
    private javax.swing.JLabel jlPwr1500;
    private javax.swing.JLabel jlPwr2000;
    private javax.swing.JLabel jlPwr300;
    private javax.swing.JLabel jlPwr50;
    private javax.swing.JLabel jlPwr500;
    private javax.swing.JLabel jlSWR;
    private javax.swing.JLabel jlSWR1;
    private javax.swing.JLabel jlSWR15;
    private javax.swing.JLabel jlSWR20;
    private javax.swing.JLabel jlSWR25;
    private javax.swing.JLabel jlSWR30;
    private static javax.swing.JLabel jlSWRAlarm;
    private static javax.swing.JLabel jlStatusField;
    private static javax.swing.JLabel jl_ConnectedIcon;
    private javax.swing.JLabel jl_Power;
    public static javax.swing.JLabel jl_SWR;
    private javax.swing.JPanel jpPwrBargraphPanel;
    private static javax.swing.JProgressBar jpPwrHigh;
    private static javax.swing.JProgressBar jpPwrHighHigh;
    private static javax.swing.JProgressBar jpPwrLow;
    private static javax.swing.JProgressBar jpPwrLowMid;
    private static javax.swing.JProgressBar jpPwrMid;
    private static javax.swing.JProgressBar jpSWRHigh;
    private static javax.swing.JProgressBar jpSWRLow;
    private static javax.swing.JProgressBar jpSWRMedium;
    private javax.swing.JPanel jpSWRPanel;
    private static javax.swing.JProgressBar jpSWRWarning;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

}
