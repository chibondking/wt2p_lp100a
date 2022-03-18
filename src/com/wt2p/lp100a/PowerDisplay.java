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
    private static boolean isIndBmMode = false;

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
        
        try {
            String[] dataArray = data.substring(1).split(",");
            PowerDataDto powerDto = new PowerDataDto(dataArray);
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
        
        if(isIndBmMode) {
            update_dBmBargraph(dto);
            update_dbmPower(dto);
            update_dBRLText(dto);
        }
        updateCallsignField(dto);
        updateStatusField("Connected", false);
        jl_ConnectedIcon.setVisible(true);
    }

    private static void updateCallsignField(PowerDataDto dto) {
        jl_Callsign.setText(dto.getCallsign());
        jl_Callsign_dbmp.setText(dto.getCallsign());
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

    private static void update_dBmBargraph(PowerDataDto dto) {
        if (dto.get_dBm() >= 0) {
            jp_dBmLow.setValue(dto.get_dBm().intValue());
        }

        if (dto.get_dBm() > 50) {
            jp_dBm_LowMid.setValue(dto.get_dBm().intValue());
        }
        if (dto.get_dBm() < 50) {
            jp_dBm_LowMid.setValue(0);
        }

        if (dto.get_dBm() >= 56.98) {
            jp_dBm_Mid.setValue(dto.get_dBm().intValue());
        } else if (dto.getForwardPower() < 56.98) {
            jp_dBm_Mid.setValue(0);
        }

        if (dto.get_dBm() > 60 && dto.get_dBm() <= 61.76) {
            jp_dBm_High.setValue(dto.get_dBm().intValue());
        } else if (dto.getForwardPower() < 60) {
            jp_dBm_High.setValue(0);
        }

        if (dto.get_dBm() > 61.76 && dto.get_dBm() <= 63.01) {
            jp_dBm_HighHigh.setValue(dto.get_dBm().intValue());
            jp_dBm_High.setValue(dto.get_dBm().intValue());
            jl_dBm_Text.setForeground(Color.RED);
        } else if (dto.get_dBm() <= 61.76) {
            jp_dBm_HighHigh.setValue(0);
            jl_dBm_Text.setForeground(Color.WHITE);
        }

        if (dto.get_dBm() == -2.3) {
            jp_dBmLow.setValue(0);
            jp_dBm_LowMid.setValue(0);            
            jp_dBm_Mid.setValue(0);
            jp_dBm_High.setValue(0);
            jp_dBm_HighHigh.setValue(0);
        }

    }

    private static void update_dbmPower(PowerDataDto dto) {
        jl_dBm_Text.setText(dto.get_dBm().toString());
        jp_dBm_LowMid.setString(dto.getFormattedForwardPower() + " watts");
        jp_dBm_LowMid.setStringPainted(true);
    }

    private static void update_dBRLText(PowerDataDto dto) {
        jl_dBRL_Text.setText(dto.getFormatted_dbRL());
    }

    private static void updateForwardPower(PowerDataDto dto) {
        jlPowerText.setText(dto.getFormattedForwardPower());
    }

    private static void updateSWRBargraph(PowerDataDto dto) {

        if (dto.getSWR() == 1.00) {
            jpSWRLow.setValue(dto.getSWRInteger());
            jlSWRAlarm.setVisible(false);
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
            jlSWRAlarm.setVisible(true);
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
        jl_Callsign = new javax.swing.JLabel();
        dbPanel = new javax.swing.JPanel();
        jl_dBm = new javax.swing.JLabel();
        jl_dBRL = new javax.swing.JLabel();
        jp_dBmBargraphPanel = new javax.swing.JPanel();
        jl_dBm_Text = new javax.swing.JLabel();
        jp_dBmLow = new javax.swing.JProgressBar();
        jp_dBm_LowMid = new javax.swing.JProgressBar();
        jp_dBm_Mid = new javax.swing.JProgressBar();
        jp_dBm_High = new javax.swing.JProgressBar();
        jp_dBm_HighHigh = new javax.swing.JProgressBar();
        jp_DBRL_Panel = new javax.swing.JPanel();
        jl_dBRL_Text = new javax.swing.JLabel();
        jl_dBm_Scale_0 = new javax.swing.JLabel();
        jl_dBm_Scale_50 = new javax.swing.JLabel();
        jl_dBm_Scale_100 = new javax.swing.JLabel();
        jl_dBm_Scale_300 = new javax.swing.JLabel();
        jl_dBm_Scale_500 = new javax.swing.JLabel();
        jl_dBm_Scale_1000 = new javax.swing.JLabel();
        jl_dBm_Scale_1500 = new javax.swing.JLabel();
        jl_dBm_Scale_2000 = new javax.swing.JLabel();
        jl_ConnectedIcon_dbPanel = new javax.swing.JLabel();
        jl_Callsign_dbmp = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        powerMenuItem = new javax.swing.JMenuItem();
        dbMenuItem = new javax.swing.JMenuItem();
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

        jpSWRMedium.setBackground(new java.awt.Color(60, 47, 0));
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

        jlSWR15.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlSWR15.setForeground(new java.awt.Color(204, 204, 0));
        jlSWR15.setText("1.5");
        mainPanel.add(jlSWR15, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, -1, -1));

        jlSWR20.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlSWR20.setForeground(new java.awt.Color(204, 204, 0));
        jlSWR20.setText("2.0");
        mainPanel.add(jlSWR20, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, -1));

        jlSWR25.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jlSWR25.setForeground(new java.awt.Color(204, 204, 0));
        jlSWR25.setText("2.5");
        mainPanel.add(jlSWR25, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, -1, -1));

        jlSWR30.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
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

        jl_Callsign.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jl_Callsign.setForeground(new java.awt.Color(204, 153, 0));
        jl_Callsign.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mainPanel.add(jl_Callsign, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, 80, 30));

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 450, 150));

        dbPanel.setBackground(new java.awt.Color(0, 0, 0));
        dbPanel.setForeground(new java.awt.Color(255, 255, 255));
        dbPanel.setMaximumSize(new java.awt.Dimension(254, 279));
        dbPanel.setMinimumSize(new java.awt.Dimension(254, 279));
        dbPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jl_dBm.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jl_dBm.setForeground(new java.awt.Color(255, 255, 255));
        jl_dBm.setText("dBm");
        dbPanel.add(jl_dBm, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 30));

        jl_dBRL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jl_dBRL.setForeground(new java.awt.Color(255, 255, 255));
        jl_dBRL.setText("dBRL");
        dbPanel.add(jl_dBRL, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, 25));

        jp_dBmBargraphPanel.setBackground(new java.awt.Color(0, 0, 0));
        jp_dBmBargraphPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jl_dBm_Text.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jl_dBm_Text.setForeground(new java.awt.Color(255, 255, 255));
        jl_dBm_Text.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jp_dBmBargraphPanel.add(jl_dBm_Text, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 30));

        jp_dBmLow.setBackground(new java.awt.Color(0, 47, 63));
        jp_dBmLow.setForeground(new java.awt.Color(21, 166, 215));
        jp_dBmLow.setMaximum(50);
        jp_dBmLow.setMinimum(30);
        jp_dBmLow.setBorder(null);
        jp_dBmLow.setBorderPainted(false);
        jp_dBmLow.setString("0");
        jp_dBmBargraphPanel.add(jp_dBmLow, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 0, 115, 30));

        jp_dBm_LowMid.setBackground(new java.awt.Color(0, 47, 63));
        jp_dBm_LowMid.setForeground(new java.awt.Color(21, 166, 215));
        jp_dBm_LowMid.setMaximum(57);
        jp_dBm_LowMid.setMinimum(50);
        jp_dBm_LowMid.setBorder(null);
        jp_dBm_LowMid.setBorderPainted(false);
        jp_dBmBargraphPanel.add(jp_dBm_LowMid, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 90, 30));

        jp_dBm_Mid.setBackground(new java.awt.Color(42, 17, 0));
        jp_dBm_Mid.setForeground(new java.awt.Color(249, 136, 60));
        jp_dBm_Mid.setMaximum(60);
        jp_dBm_Mid.setMinimum(57);
        jp_dBm_Mid.setBorder(null);
        jp_dBm_Mid.setBorderPainted(false);
        jp_dBmBargraphPanel.add(jp_dBm_Mid, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 40, 30));

        jp_dBm_High.setBackground(new java.awt.Color(42, 17, 0));
        jp_dBm_High.setForeground(new java.awt.Color(249, 136, 60));
        jp_dBm_High.setMaximum(62);
        jp_dBm_High.setMinimum(60);
        jp_dBm_High.setBorder(null);
        jp_dBm_High.setBorderPainted(false);
        jp_dBmBargraphPanel.add(jp_dBm_High, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 30, 30));

        jp_dBm_HighHigh.setBackground(new java.awt.Color(49, 0, 0));
        jp_dBm_HighHigh.setForeground(new java.awt.Color(255, 0, 0));
        jp_dBm_HighHigh.setMaximum(64);
        jp_dBm_HighHigh.setMinimum(62);
        jp_dBm_HighHigh.setBorder(null);
        jp_dBm_HighHigh.setBorderPainted(false);
        jp_dBmBargraphPanel.add(jp_dBm_HighHigh, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 30, 30));

        dbPanel.add(jp_dBmBargraphPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 11, 390, 30));

        jp_DBRL_Panel.setBackground(new java.awt.Color(0, 0, 0));
        jp_DBRL_Panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jl_dBRL_Text.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jl_dBRL_Text.setForeground(new java.awt.Color(255, 255, 255));
        jl_dBRL_Text.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_dBRL_Text.setMaximumSize(new java.awt.Dimension(44, 22));
        jl_dBRL_Text.setMinimumSize(new java.awt.Dimension(44, 22));
        jl_dBRL_Text.setPreferredSize(new java.awt.Dimension(44, 22));
        jp_DBRL_Panel.add(jl_dBRL_Text, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 30));

        dbPanel.add(jp_DBRL_Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 65, -1, 34));

        jl_dBm_Scale_0.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jl_dBm_Scale_0.setForeground(new java.awt.Color(204, 204, 0));
        jl_dBm_Scale_0.setText("30");
        dbPanel.add(jl_dBm_Scale_0, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 14, -1));

        jl_dBm_Scale_50.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jl_dBm_Scale_50.setForeground(new java.awt.Color(204, 204, 0));
        jl_dBm_Scale_50.setText("40");
        dbPanel.add(jl_dBm_Scale_50, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, -1, -1));

        jl_dBm_Scale_100.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jl_dBm_Scale_100.setForeground(new java.awt.Color(204, 204, 0));
        jl_dBm_Scale_100.setText("50");
        dbPanel.add(jl_dBm_Scale_100, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, -1, -1));

        jl_dBm_Scale_300.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jl_dBm_Scale_300.setForeground(new java.awt.Color(204, 204, 0));
        jl_dBm_Scale_300.setText("54.7");
        dbPanel.add(jl_dBm_Scale_300, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, -1, -1));

        jl_dBm_Scale_500.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jl_dBm_Scale_500.setForeground(new java.awt.Color(204, 204, 0));
        jl_dBm_Scale_500.setText("56.9");
        dbPanel.add(jl_dBm_Scale_500, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, -1, -1));

        jl_dBm_Scale_1000.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jl_dBm_Scale_1000.setForeground(new java.awt.Color(204, 204, 0));
        jl_dBm_Scale_1000.setText("60");
        dbPanel.add(jl_dBm_Scale_1000, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, -1, -1));

        jl_dBm_Scale_1500.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jl_dBm_Scale_1500.setForeground(new java.awt.Color(204, 204, 0));
        jl_dBm_Scale_1500.setText("61.7");
        dbPanel.add(jl_dBm_Scale_1500, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 30, -1));

        jl_dBm_Scale_2000.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jl_dBm_Scale_2000.setForeground(new java.awt.Color(204, 204, 0));
        jl_dBm_Scale_2000.setText("63");
        dbPanel.add(jl_dBm_Scale_2000, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, -1, -1));

        jl_ConnectedIcon_dbPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/wt2p/lp100a/icons/launch_white.png"))); // NOI18N
        jl_ConnectedIcon_dbPanel.setFocusable(false);
        jl_ConnectedIcon_dbPanel.setRequestFocusEnabled(false);
        jl_ConnectedIcon_dbPanel.setVerifyInputWhenFocusTarget(false);
        dbPanel.add(jl_ConnectedIcon_dbPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jl_Callsign_dbmp.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jl_Callsign_dbmp.setForeground(new java.awt.Color(204, 153, 0));
        jl_Callsign_dbmp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dbPanel.add(jl_Callsign_dbmp, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, 80, 30));

        getContentPane().add(dbPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 450, 150));

        menuBar.setBackground(new java.awt.Color(0, 0, 0));
        menuBar.setForeground(new java.awt.Color(102, 102, 102));
        menuBar.setBorderPainted(false);

        fileMenu.setBackground(new java.awt.Color(0, 0, 0));
        fileMenu.setForeground(new java.awt.Color(102, 102, 102));
        fileMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/wt2p/lp100a/icons/switch_profile_white.png"))); // NOI18N
        fileMenu.setMnemonic('f');

        powerMenuItem.setBackground(new java.awt.Color(0, 0, 0));
        powerMenuItem.setForeground(new java.awt.Color(255, 255, 0));
        powerMenuItem.setText("Meter: Power/SWR");
        powerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                powerMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(powerMenuItem);

        dbMenuItem.setBackground(new java.awt.Color(0, 0, 0));
        dbMenuItem.setForeground(new java.awt.Color(255, 255, 0));
        dbMenuItem.setText("Meter: dBm/dBRL");
        dbMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dbMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(dbMenuItem);

        exitMenuItem.setBackground(new java.awt.Color(0, 0, 0));
        exitMenuItem.setForeground(new java.awt.Color(255, 0, 0));
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

    private void dbMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dbMenuItemActionPerformed
        mainPanel.setVisible(false);
        isIndBmMode = true;
        dbPanel.setVisible(true);
    }//GEN-LAST:event_dbMenuItemActionPerformed

    private void powerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_powerMenuItemActionPerformed
        dbPanel.setVisible(false);
        isIndBmMode = false;
        mainPanel.setVisible(true);
    }//GEN-LAST:event_powerMenuItemActionPerformed

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
    private javax.swing.JMenuItem dbMenuItem;
    private static javax.swing.JPanel dbPanel;
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
    private static javax.swing.JLabel jl_Callsign;
    private static javax.swing.JLabel jl_Callsign_dbmp;
    private static javax.swing.JLabel jl_ConnectedIcon;
    private static javax.swing.JLabel jl_ConnectedIcon_dbPanel;
    private javax.swing.JLabel jl_Power;
    public static javax.swing.JLabel jl_SWR;
    private javax.swing.JLabel jl_dBRL;
    public static javax.swing.JLabel jl_dBRL_Text;
    private javax.swing.JLabel jl_dBm;
    private javax.swing.JLabel jl_dBm_Scale_0;
    private javax.swing.JLabel jl_dBm_Scale_100;
    private javax.swing.JLabel jl_dBm_Scale_1000;
    private javax.swing.JLabel jl_dBm_Scale_1500;
    private javax.swing.JLabel jl_dBm_Scale_2000;
    private javax.swing.JLabel jl_dBm_Scale_300;
    private javax.swing.JLabel jl_dBm_Scale_50;
    private javax.swing.JLabel jl_dBm_Scale_500;
    public static javax.swing.JLabel jl_dBm_Text;
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
    private javax.swing.JPanel jp_DBRL_Panel;
    private javax.swing.JPanel jp_dBmBargraphPanel;
    private static javax.swing.JProgressBar jp_dBmLow;
    private static javax.swing.JProgressBar jp_dBm_High;
    private static javax.swing.JProgressBar jp_dBm_HighHigh;
    private static javax.swing.JProgressBar jp_dBm_LowMid;
    private static javax.swing.JProgressBar jp_dBm_Mid;
    private static javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem powerMenuItem;
    // End of variables declaration//GEN-END:variables

}
