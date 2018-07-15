/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wt2p.lp100a;

import com.serialpundit.core.SerialComException;
import com.serialpundit.serial.SerialComManager;
import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 *
 * @author cedrickj
 */
public class PowerDisplay extends javax.swing.JFrame {

    private static SerialComManager scm;
    private static long handle;
    
    private static boolean isNetworkEnabled = false;

    /**
     * Creates new form PowerDisplay
     */
    public PowerDisplay() {
        initComponents();
    }

    private static void connectToComPort() throws SerialComException, IOException {
        scm = new SerialComManager();
        handle = scm.openComPort("COM9", true, true, true);
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
            updateStatusField("ERR comm with LP100");
        }
    }
    
    private static void setNetworkEnabled(boolean value) {
        isNetworkEnabled = value;
    }
        
    
    private static void updateStatusField(String value) {
        jtf_StatusField.setText(value);
    }

    private static void parseStringFromLP100A(String data) {
        String[] dataArray = data.substring(1).split(",");
        try {
            PowerDataDto powerDto = new PowerDataDto(dataArray);
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
        } else if (dto.getTransmittingRadio() == 0 && dto.getForwardPower() > 0) {
            jl_Tx1Active.setForeground(Color.WHITE);
            jl_Tx1Active.setBackground(Color.RED);
        }

        if (dto.getTransmittingRadio() == 2 && dto.getForwardPower() == 0) {
            jl_Tx1Active.setForeground(Color.LIGHT_GRAY);
            jl_Tx2Active.setForeground(Color.LIGHT_GRAY);
            jl_Tx1Active.setBackground(Color.BLACK);
            jl_Tx2Active.setBackground(Color.BLACK);
        }
    }
    
    private static void updateForwardPowerBargraph(PowerDataDto dto) {
        if (dto.getForwardPower() >= 0) {
            jpPwr0100.setValue(dto.getForwardPower().intValue());
        }
        
        if (dto.getForwardPower() > 100) {
            jpPwr100500.setValue(dto.getForwardPower().intValue());
        }
        
        if (dto.getForwardPower() > 600) {
            jpPwr6001000.setValue(dto.getForwardPower().intValue());
        }
        
        if (dto.getForwardPower() > 1000 && dto.getForwardPower() <= 1500) {
            jpPwr10001500.setValue(dto.getForwardPower().intValue());
        }
        
        if (dto.getForwardPower() == 0) {
            try {
                Thread.sleep(20);
            } catch (Exception ex) {
                //do nothing
            }
            jpPwr0100.setValue(0);
            jpPwr100500.setValue(0);
            jpPwr6001000.setValue(0);
            jpPwr10001500.setValue(0);
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
        
        if (dto.getSWR() == 1.00) {
            try {
                Thread.sleep(20);
            } catch (Exception ex) {
                //do nothing
            }
            jpSWRGreen.setValue(0);
            jpSWRYellow.setValue(0);
            jpSWROrange.setValue(0);
            jpSWRRed.setValue(0);
        }
    }
    private static void updateSWR(PowerDataDto dto) {
        jl_SWR.setText(Double.toString(dto.getSWR()));

        if (dto.getSWR() == 1.00) {
            // Do nothing, keep the last state
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
        jpPwr0100 = new javax.swing.JProgressBar();
        jl_power_manual = new javax.swing.JLabel();
        jpPwr100500 = new javax.swing.JProgressBar();
        jpPwr10001500 = new javax.swing.JProgressBar();
        jpPwr6001000 = new javax.swing.JProgressBar();
        swrPanel = new javax.swing.JPanel();
        jl_SWR = new javax.swing.JLabel();
        jpSWRGreen = new javax.swing.JProgressBar();
        jpSWRYellow = new javax.swing.JProgressBar();
        jpSWROrange = new javax.swing.JProgressBar();
        jpSWRRed = new javax.swing.JProgressBar();
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

        jl_Power.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jl_Power.setForeground(new java.awt.Color(255, 255, 255));
        jl_Power.setText("PWR");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("SWR");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Radio 1");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Radio 2");

        jl_Tx1Active.setBackground(new java.awt.Color(0, 0, 0));
        jl_Tx1Active.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jl_Tx1Active.setForeground(java.awt.Color.lightGray);
        jl_Tx1Active.setText("TX");
        jl_Tx1Active.setOpaque(true);

        jl_Tx2Active.setBackground(new java.awt.Color(0, 0, 0));
        jl_Tx2Active.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jl_Tx2Active.setForeground(java.awt.Color.lightGray);
        jl_Tx2Active.setText("TX");
        jl_Tx2Active.setOpaque(true);

        jtf_StatusField.setEditable(false);
        jtf_StatusField.setBackground(new java.awt.Color(0, 0, 0));
        jtf_StatusField.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jtf_StatusField.setForeground(new java.awt.Color(204, 204, 204));
        jtf_StatusField.setBorder(null);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpPwr0100.setBackground(new java.awt.Color(0, 0, 0));
        jpPwr0100.setForeground(new java.awt.Color(0, 51, 153));
        jpPwr0100.setBorderPainted(false);
        jpPwr0100.setString("0");
        jPanel1.add(jpPwr0100, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 90, 30));

        jl_power_manual.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jl_power_manual.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jl_power_manual, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 30));

        jpPwr100500.setBackground(new java.awt.Color(0, 0, 0));
        jpPwr100500.setForeground(new java.awt.Color(0, 51, 153));
        jpPwr100500.setMaximum(500);
        jpPwr100500.setMinimum(100);
        jpPwr100500.setBorderPainted(false);
        jPanel1.add(jpPwr100500, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 100, 30));

        jpPwr10001500.setBackground(new java.awt.Color(0, 0, 0));
        jpPwr10001500.setForeground(new java.awt.Color(255, 102, 102));
        jpPwr10001500.setMaximum(1500);
        jpPwr10001500.setMinimum(500);
        jpPwr10001500.setBorderPainted(false);
        jPanel1.add(jpPwr10001500, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 70, 30));

        jpPwr6001000.setBackground(new java.awt.Color(0, 0, 0));
        jpPwr6001000.setForeground(new java.awt.Color(204, 102, 0));
        jpPwr6001000.setMaximum(1000);
        jpPwr6001000.setMinimum(600);
        jpPwr6001000.setBorderPainted(false);
        jPanel1.add(jpPwr6001000, new org.netbeans.lib.awtextra.AbsoluteConstraints(241, 0, 50, 30));

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
        swrPanel.add(jpSWRYellow, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, 60, 30));

        jpSWROrange.setBackground(new java.awt.Color(0, 0, 0));
        jpSWROrange.setForeground(new java.awt.Color(204, 102, 0));
        jpSWROrange.setMaximum(250);
        jpSWROrange.setMinimum(200);
        jpSWROrange.setBorderPainted(false);
        swrPanel.add(jpSWROrange, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 60, 30));

        jpSWRRed.setBackground(new java.awt.Color(0, 0, 0));
        jpSWRRed.setForeground(new java.awt.Color(255, 102, 102));
        jpSWRRed.setMaximum(300);
        jpSWRRed.setMinimum(250);
        jpSWRRed.setBorderPainted(false);
        swrPanel.add(jpSWRRed, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 60, 30));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jl_Tx1Active)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jtf_StatusField)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jl_Tx2Active))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jl_Power, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(swrPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(5, Short.MAX_VALUE))))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jl_Power, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(swrPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jl_Tx1Active))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jl_Tx2Active)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtf_StatusField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 180));

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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jl_Power;
    public static javax.swing.JLabel jl_SWR;
    public static javax.swing.JLabel jl_Tx1Active;
    public static javax.swing.JLabel jl_Tx2Active;
    public static javax.swing.JLabel jl_power_manual;
    private static javax.swing.JProgressBar jpPwr0100;
    private static javax.swing.JProgressBar jpPwr10001500;
    private static javax.swing.JProgressBar jpPwr100500;
    private static javax.swing.JProgressBar jpPwr6001000;
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
