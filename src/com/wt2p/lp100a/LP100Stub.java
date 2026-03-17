package com.wt2p.lp100a;

import java.util.Random;

public class LP100Stub {
    
    private static final String FORMAT = "P%s,%s,%s,%s,%s,%d,%s,%s,%s\n";
    
    private String callsign;
    private double basePower;
    private double baseSWR;
    private double baseZ;
    private double basePhase;
    private double baseAlarm;
    private double basePeakHold;
    private double baseDBm;
    private Random random;
    private int tickCount;
    
    public LP100Stub() {
        this.callsign = "TEST10";
        this.basePower = 100.0;
        this.baseSWR = 1.2;
        this.baseZ = 50.0;
        this.basePhase = 0.0;
        this.baseAlarm = 100.0;
        this.basePeakHold = 105.0;
        this.baseDBm = 50.0;
        this.random = new Random();
        this.tickCount = 0;
    }
    
    public String getResponse() {
        tickCount++;
        
        double powerVariation = (random.nextDouble() - 0.5) * 20;
        double power = basePower + powerVariation;
        
        double swrVariation = (random.nextDouble() - 0.5) * 0.1;
        double swr = baseSWR + swrVariation;
        
        double dbmVariation = (random.nextDouble() - 0.5) * 2;
        double dbm = baseDBm + dbmVariation;
        
        double zVariation = (random.nextDouble() - 0.5) * 2;
        double z = baseZ + zVariation;
        
        double peakHold = power + (random.nextDouble() * 5);
        
        if (tickCount % 60 == 0) {
            basePower = 50 + random.nextDouble() * 150;
            baseSWR = 1.0 + random.nextDouble() * 0.5;
        }
        
        return String.format(FORMAT,
            formatNumber(power),
            formatNumber(z),
            formatNumber(basePhase),
            formatNumber(baseAlarm),
            callsign,
            1,
            formatNumber(peakHold),
            formatNumber(dbm),
            formatNumber(swr)
        );
    }
    
    private String formatNumber(double value) {
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            return String.format("%.1f", value);
        }
        return String.format("%.2f", value);
    }
    
    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }
    
    public void setBasePower(double basePower) {
        this.basePower = basePower;
    }
    
    public void setBaseSWR(double baseSWR) {
        this.baseSWR = baseSWR;
    }
}
