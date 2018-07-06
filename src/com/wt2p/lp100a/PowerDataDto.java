package com.wt2p.lp100a;

import java.io.Serializable;
import java.text.DecimalFormat;

public class PowerDataDto extends Object implements Serializable {
    
    private double forwardPower;
    private double zValue;
    private double phaseValue;
    private double alarmSetPoint;
    private String callsign;
    //This is listed as power range but is used to identify the transmitter for now
    private double transmittingRadio;
    private double peakHold;
    private double dBm;
    private double swrValue;
    private DecimalFormat powerFormat = new DecimalFormat("#,##0.0");
    
    public PowerDataDto(String[] dataArray) {
        this.setForwardPower(convertValueToDouble(dataArray[0]));
        this.setZValue(convertValueToDouble(dataArray[1]));
        this.setPhaseValue(convertValueToDouble(dataArray[2]));
        this.setAlarmSetPoint(convertValueToDouble(dataArray[3]));
        this.setCallsign(dataArray[4]);
        this.setTransmittingRadio(convertValueToDouble(dataArray[5]));
        this.setPeakHold(convertValueToDouble(dataArray[6]));
        this.setdBm(convertValueToDouble(dataArray[7]));
        this.setSWR(convertValueToDouble(dataArray[8]));
    }
    
    private double convertValueToDouble(String value) {
        return Double.parseDouble(value);
    }
    
    private void setForwardPower(double value) { this.forwardPower = value; }
    
    public String getFormattedForwardPower() {
        return powerFormat.format(this.forwardPower);
    }
    
    public Double getForwardPower() {
        return this.forwardPower;
    }
    
    private void setZValue(double value) { this.zValue = value; }
    
    public Double getZValue() {
        return this.zValue;
    }
        
    private void setPhaseValue(double value) { this.phaseValue = value; }
    
    public Double getPhaseValue() {
        return this.phaseValue;
    }
    
    private void setAlarmSetPoint(double value) { this.alarmSetPoint = value; }
    
    public Double getAlarmSetPoint() {
        return this.alarmSetPoint;
    }
    
    private void setCallsign(String value) {
        this.callsign = value.trim();
    }
    
    public String getCallsign() {
        return this.callsign;
    }
    
    private void setTransmittingRadio(double value) {
        this.transmittingRadio = value; 
    }
    
    public Double getTransmittingRadio() {
        return this.transmittingRadio;
    }
    
    private void setPeakHold(double value) { this.peakHold = value; }
    
    public Double getPeakHold() {
        return this.peakHold;
    }
    
    private void setdBm(double value) { this.dBm = value; }
    
    public Double get_dBm() {
        return this.dBm;
    }
    
    private void setSWR(double value) { this.swrValue = value; }
    
    public Double getSWR() {
        return this.swrValue;
    }
    
    
    
}
