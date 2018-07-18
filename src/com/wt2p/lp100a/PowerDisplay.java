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

    private static SerialComManager scm;
    private static long handle;
    private static String comPort;
    private static boolean isNetworkEnabled;
    private static boolean isUsingLatestLPFirmware;

    /**
     * Creates new form PowerDisplay
     */
    public PowerDisplay() {
        initComponents();
    }

    private static void connectToComPort() throws SerialComException, IOException {
        scm = new SerialComManager();
        handle = scm.openComPort(comPort, true, true, true);
        scm.configureComPortData(handle, SerialComManager.DATABITS.DB_8, SerialComManager.STOPBITS.SB_1, SerialComManager.PARITY.P_NONE, SerialComManager.BAUDRATE.B115200, 0);
        scm.configureComPortControl(handle, SerialComManager.FLOWCONTROL.NONE, 'x', 'x', false, false);
    }

    private static void startReadingFromComPort() {
        try {
            while (true) {
                scm.writeString(handle, "P", 2);
                Thread.sleep(50);
                String data = scm.readString(handle);
                parseStringFromLP100A(data);
                Thread.sleep(50);
            }

        } catch (Exception e) {
            updateStatusField("ERR comm with LP100. Restart.");
        }
    }
    
    private static void setNetworkEnabled(boolean value) {
        isNetworkEnabled = value;
    }
    
    private static void setIsUsingLatestLPFirmware(boolean value) {
        isUsingLatestLPFirmware = value;
    }
        
    
    private static void updateStatusField(String value) {
        jtf_StatusField.setText(value);
    }

    private static void parseStringFromLP100A(String data) {
        String[] dataArray = data.substring(1).split(",");
        try {
            PowerDataDto powerDto = new PowerDataDto(dataArray, isUsingLatestLPFirmware);
            updateUserInterface(powerDto);
            
            
        } catch (Exception ex) {
            updateStatusField("parse error from lp100");
        }
    }
    
    private static void updateUserInterface(PowerDataDto dto) {
        updateForwardPower(dto);
        updateForwardPowerBargraph(dto);
        updateSWRBargraph(dto);
        updateTransmittingRadio(dto);
        updateSWR(dto);
    }


    private static void updateTransmittingRadio(PowerDataDto dto) {      
        if (dto.getTransmittingRadio() == 2 && dto.getForwardPower() > 0) {
            jl_Tx2Active.setForeground(Color.WHITE);
            jl_Tx2Active.setBackground(Color.RED);
        } else if (dto.getTransmittingRadio() == 1 && dto.getForwardPower() > 0) {
            jl_Tx1Active.setForeground(Color.WHITE);
            jl_Tx1Active.setBackground(Color.RED);
        }

        if (dto.getForwardPower() == 0) {
            jl_Tx1Active.setForeground(Color.LIGHT_GRAY);
            jl_Tx2Active.setForeground(Color.LIGHT_GRAY);
            jl_Tx1Active.setBackground(Color.BLACK);
            jl_Tx2Active.setBackground(Color.BLACK);
        }
    }
    
    private static void updateForwardPowerBargraph(PowerDataDto dto) {
        if (dto.getForwardPower() >= 0) {
            jpPwrLow.setValue(dto.getForwardPower().intValue());
        }
        
        if (dto.getForwardPower() > 100) {
            jpPwrLowMid.setValue(dto.getForwardPower().intValue());
        } if (dto.getForwardPower() < 100) {
            jpPwrLowMid.setValue(0);
        }
        
        if (dto.getForwardPower() > 500) {
            jpPwrMid.setValue(dto.getForwardPower().intValue());
        } else if (dto.getForwardPower() < 500) {
            jpPwrMid.setValue(0);
        }
        
        if (dto.getForwardPower() > 1000 && dto.getForwardPower() <= 1500) {
            jpPwrHigh.setValue(dto.getForwardPower().intValue());
        } else if(dto.getForwardPower() < 1000) {
            jpPwrHigh.setValue(0);
        }
        
        if (dto.getForwardPower() > 1500 && dto.getForwardPower() <= 2000) {
            jpPwrHighHigh.setValue(dto.getForwardPower().intValue());
        } else if(dto.getForwardPower() < 1500) {
            jpPwrHighHigh.setValue(0);
        }
        
        if (dto.getForwardPower() == 0) {
            jpPwrLow.setValue(0);
            jpPwrLowMid.setValue(0);
            jpPwrMid.setValue(0);
            jpPwrHigh.setValue(0);
        }
        
    }

    private static void updateForwardPower(PowerDataDto dto) {
        jl_power_manual.setText(dto.getFormattedForwardPower());
    }

    private static void updateSWRBargraph(PowerDataDto dto) {
        if (dto.getSWR() >= 1.01) {
            jpSWRGreen.setValue(dto.getSWRInteger());
        }
        
        if (dto.getSWR() >= 1.51) {
            jpSWRYellow.setValue(dto.getSWRInteger());
        }
        
        if (dto.getSWR() >= 2.00) {
            jpSWROrange.setValue(dto.getSWRInteger());
        }
        
        if (dto.getSWR() >= 2.50) {
            jpSWRRed.setValue(dto.getSWRInteger());
        }
        
        if (dto.getSWR() > 3.0) {
            jlSWRAlarm.setForeground(Color.RED);
        } 
        
        if (dto.getSWR() == 1.00) {
            jpSWRGreen.setValue(0);
            jpSWRYellow.setValue(0);
            jpSWROrange.setValue(0);
            jpSWRRed.setValue(0);
            jlSWRAlarm.setForeground(Color.BLACK);
        }
    }
    private static void updateSWR(PowerDataDto dto) {
        
        if (dto.getSWR() == 1.00) {
            // Do nothing, keep the last state
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
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jl_Tx1Active = new javax.swing.JLabel();
        jl_Tx2Active = new javax.swing.JLabel();
        jtf_StatusField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jpPwrLow = new javax.swing.JProgressBar();
        jl_power_manual = new javax.swing.JLabel();
        jpPwrLowMid = new javax.swing.JProgressBar();
        jpPwrHigh = new javax.swing.JProgressBar();
        jpPwrMid = new javax.swing.JProgressBar();
        jpPwrHighHigh = new javax.swing.JProgressBar();
        swrPanel = new javax.swing.JPanel();
        jl_SWR = new javax.swing.JLabel();
        jpSWRGreen = new javax.swing.JProgressBar();
        jpSWRYellow = new javax.swing.JProgressBar();
        jpSWROrange = new javax.swing.JProgressBar();
        jpSWRRed = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jlSWRAlarm = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WT2P LP-100A");
        setBackground(new java.awt.Color(0, 0, 0));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setBackground(new java.awt.Color(0, 0, 0));
        mainPanel.setForeground(new java.awt.Color(255, 255, 255));
        mainPanel.setMaximumSize(new java.awt.Dimension(254, 279));
        mainPanel.setMinimumSize(new java.awt.Dimension(254, 279));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jl_Power.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jl_Power.setForeground(new java.awt.Color(255, 255, 255));
        jl_Power.setText("PWR");
        mainPanel.add(jl_Power, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 46, 25));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("SWR");
        mainPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 65, -1, 25));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Radio 1");
        mainPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Radio 2");
        mainPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 143, -1, -1));

        jl_Tx1Active.setBackground(new java.awt.Color(0, 0, 0));
        jl_Tx1Active.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jl_Tx1Active.setForeground(java.awt.Color.lightGray);
        jl_Tx1Active.setText("TX");
        jl_Tx1Active.setOpaque(true);
        mainPanel.add(jl_Tx1Active, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, -1, -1));

        jl_Tx2Active.setBackground(new java.awt.Color(0, 0, 0));
        jl_Tx2Active.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jl_Tx2Active.setForeground(java.awt.Color.lightGray);
        jl_Tx2Active.setText("TX");
        jl_Tx2Active.setOpaque(true);
        mainPanel.add(jl_Tx2Active, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, -1));

        jtf_StatusField.setEditable(false);
        jtf_StatusField.setBackground(new java.awt.Color(0, 0, 0));
        jtf_StatusField.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jtf_StatusField.setForeground(new java.awt.Color(204, 204, 204));
        jtf_StatusField.setBorder(null);
        mainPanel.add(jtf_StatusField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 163, 415, -1));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpPwrLow.setBackground(new java.awt.Color(0, 0, 0));
        jpPwrLow.setForeground(new java.awt.Color(0, 51, 153));
        jpPwrLow.setBorderPainted(false);
        jpPwrLow.setString("0");
        jPanel1.add(jpPwrLow, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 90, 30));

        jl_power_manual.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jl_power_manual.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jl_power_manual, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 30));

        jpPwrLowMid.setBackground(new java.awt.Color(0, 0, 0));
        jpPwrLowMid.setForeground(new java.awt.Color(0, 51, 153));
        jpPwrLowMid.setMaximum(500);
        jpPwrLowMid.setMinimum(100);
        jpPwrLowMid.setBorderPainted(false);
        jPanel1.add(jpPwrLowMid, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 90, 30));

        jpPwrHigh.setBackground(new java.awt.Color(0, 0, 0));
        jpPwrHigh.setForeground(new java.awt.Color(204, 102, 0));
        jpPwrHigh.setMaximum(1500);
        jpPwrHigh.setMinimum(1000);
        jpPwrHigh.setBorderPainted(false);
        jPanel1.add(jpPwrHigh, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 30, 30));

        jpPwrMid.setBackground(new java.awt.Color(0, 0, 0));
        jpPwrMid.setForeground(new java.awt.Color(153, 153, 0));
        jpPwrMid.setMaximum(1000);
        jpPwrMid.setMinimum(500);
        jpPwrMid.setBorderPainted(false);
        jPanel1.add(jpPwrMid, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 40, 30));

        jpPwrHighHigh.setBackground(new java.awt.Color(0, 0, 0));
        jpPwrHighHigh.setForeground(new java.awt.Color(255, 0, 0));
        jpPwrHighHigh.setMaximum(2000);
        jpPwrHighHigh.setMinimum(1500);
        jpPwrHighHigh.setBorderPainted(false);
        jPanel1.add(jpPwrHighHigh, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 0, 30, 30));

        mainPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 11, 355, 30));

        swrPanel.setBackground(new java.awt.Color(0, 0, 0));
        swrPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jl_SWR.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jl_SWR.setForeground(new java.awt.Color(255, 255, 255));
        jl_SWR.setMaximumSize(new java.awt.Dimension(44, 22));
        jl_SWR.setMinimumSize(new java.awt.Dimension(44, 22));
        jl_SWR.setPreferredSize(new java.awt.Dimension(44, 22));
        swrPanel.add(jl_SWR, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 30));

        jpSWRGreen.setBackground(new java.awt.Color(0, 0, 0));
        jpSWRGreen.setForeground(new java.awt.Color(0, 51, 153));
        jpSWRGreen.setMaximum(150);
        jpSWRGreen.setMinimum(100);
        jpSWRGreen.setBorderPainted(false);
        jpSWRGreen.setOpaque(true);
        swrPanel.add(jpSWRGreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 70, 30));

        jpSWRYellow.setBackground(new java.awt.Color(0, 0, 0));
        jpSWRYellow.setForeground(new java.awt.Color(153, 153, 0));
        jpSWRYellow.setMaximum(200);
        jpSWRYellow.setMinimum(150);
        jpSWRYellow.setBorderPainted(false);
        swrPanel.add(jpSWRYellow, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 50, 30));

        jpSWROrange.setBackground(new java.awt.Color(0, 0, 0));
        jpSWROrange.setForeground(new java.awt.Color(204, 102, 0));
        jpSWROrange.setMaximum(250);
        jpSWROrange.setMinimum(200);
        jpSWROrange.setBorderPainted(false);
        swrPanel.add(jpSWROrange, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 50, 30));

        jpSWRRed.setBackground(new java.awt.Color(0, 0, 0));
        jpSWRRed.setForeground(new java.awt.Color(255, 0, 0));
        jpSWRRed.setMaximum(300);
        jpSWRRed.setMinimum(250);
        jpSWRRed.setBorderPainted(false);
        swrPanel.add(jpSWRRed, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 50, 30));

        mainPanel.add(swrPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 65, -1, 34));

        jLabel1.setForeground(new java.awt.Color(204, 204, 0));
        jLabel1.setText("0");
        mainPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 14, -1));

        jLabel2.setForeground(new java.awt.Color(204, 204, 0));
        jLabel2.setText("100");
        mainPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, -1, -1));

        jLabel4.setForeground(new java.awt.Color(204, 204, 0));
        jLabel4.setText("50");
        mainPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, -1, -1));

        jLabel7.setForeground(new java.awt.Color(204, 204, 0));
        jLabel7.setText("300");
        mainPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, -1, -1));

        jLabel8.setForeground(new java.awt.Color(204, 204, 0));
        jLabel8.setText("500");
        mainPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, -1, -1));

        jLabel9.setForeground(new java.awt.Color(204, 204, 0));
        jLabel9.setText("1k");
        mainPanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, -1, -1));

        jLabel10.setForeground(new java.awt.Color(204, 204, 0));
        jLabel10.setText("2k");
        mainPanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, -1, -1));

        jLabel11.setForeground(new java.awt.Color(204, 204, 0));
        jLabel11.setText("1");
        mainPanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, -1, -1));

        jLabel12.setForeground(new java.awt.Color(204, 204, 0));
        jLabel12.setText("1.5");
        mainPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, -1, -1));

        jLabel13.setForeground(new java.awt.Color(204, 204, 0));
        jLabel13.setText("2.0");
        mainPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, -1));

        jLabel14.setForeground(new java.awt.Color(204, 204, 0));
        jLabel14.setText("2.5");
        mainPanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, -1, -1));

        jLabel15.setForeground(new java.awt.Color(204, 204, 0));
        jLabel15.setText("3.0");
        mainPanel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, -1, -1));

        jlSWRAlarm.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlSWRAlarm.setText("HI SWR");
        mainPanel.add(jlSWRAlarm, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, -1, -1));

        jLabel16.setForeground(new java.awt.Color(204, 204, 0));
        jLabel16.setText("1.5k");
        mainPanel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 30, -1));

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, 180));

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

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
            scm.closeComPort(handle);
        } catch (Exception ex) {

        }
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    public static void main(String args[]) {
        
        if (args.length > 0) {
            comPort = args[0];
            setNetworkEnabled(Boolean.valueOf(args[1]));
            setIsUsingLatestLPFirmware(Boolean.valueOf(args[2]));
            
        } else {
            comPort = "COM9";
            setNetworkEnabled(false);
            setIsUsingLatestLPFirmware(false);           
        } 

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PowerDisplay().setVisible(true);
            }
        });
        try {
            connectToComPort();
            startReadingFromComPort();
        } catch (Exception ex) {

        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private static javax.swing.JLabel jlSWRAlarm;
    private javax.swing.JLabel jl_Power;
    public static javax.swing.JLabel jl_SWR;
    public static javax.swing.JLabel jl_Tx1Active;
    public static javax.swing.JLabel jl_Tx2Active;
    public static javax.swing.JLabel jl_power_manual;
    private static javax.swing.JProgressBar jpPwrHigh;
    private static javax.swing.JProgressBar jpPwrHighHigh;
    private static javax.swing.JProgressBar jpPwrLow;
    private static javax.swing.JProgressBar jpPwrLowMid;
    private static javax.swing.JProgressBar jpPwrMid;
    private static javax.swing.JProgressBar jpSWRGreen;
    private static javax.swing.JProgressBar jpSWROrange;
    private static javax.swing.JProgressBar jpSWRRed;
    private static javax.swing.JProgressBar jpSWRYellow;
    private static javax.swing.JTextField jtf_StatusField;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel swrPanel;
    // End of variables declaration//GEN-END:variables

}
