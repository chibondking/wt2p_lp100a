package com.wt2p.lp100a;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

public class PowerBargraph extends javax.swing.JComponent {

    private static final double MAX_POWER = 2000.0;
    
    private double currentPower = 0;
    private double peakPower = 0;
    private boolean showPeak = false;
    private long peakExpireTime = 0;
    private static final long PEAK_HOLD_MS = 2000;
    
    private Color colorLow = new Color(21, 166, 215);
    private Color colorMid = new Color(249, 136, 60);
    private Color colorHigh = new Color(255, 0, 0);
    private Color colorBackground = new Color(0, 47, 63);
    private Color colorLowBg;
    private Color colorMidBg;
    private Color colorHighBg;
    
    public PowerBargraph() {
        setPreferredSize(new Dimension(390, 30));
        setMinimumSize(new Dimension(390, 30));
        setMaximumSize(new Dimension(390, 30));
        
        colorLowBg = new Color(colorLow.getRed() / 10, colorLow.getGreen() / 10, colorLow.getBlue() / 10);
        colorMidBg = new Color(colorMid.getRed() / 10, colorMid.getGreen() / 10, colorMid.getBlue() / 10);
        colorHighBg = new Color(colorHigh.getRed() / 10, colorHigh.getGreen() / 10, colorHigh.getBlue() / 10);
    }
    
    public void setPower(double watts) {
        this.currentPower = Math.max(0, Math.min(watts, MAX_POWER));
        
        boolean expired = System.currentTimeMillis() > peakExpireTime;
        
        if (expired) {
            showPeak = false;
            peakPower = 0;
        }
        
        if (this.currentPower > 0) {
            if (this.currentPower > peakPower) {
                peakPower = this.currentPower;
                showPeak = true;
                peakExpireTime = System.currentTimeMillis() + PEAK_HOLD_MS;
            }
        }
        
        repaint();
    }
    
    public double getPeak() {
        return peakPower;
    }
    
    public void resetPeak() {
        peakPower = 0;
        showPeak = false;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);
        
        g2d.setColor(colorBackground);
        g2d.fillRect(1, 1, width - 2, height - 2);
        
        int x = 2;
        int segmentWidth1000 = (int) ((1000.0 / MAX_POWER) * (width - 4));
        g2d.setColor(colorLowBg);
        g2d.fillRect(x, 2, segmentWidth1000, height - 4);
        x += segmentWidth1000;
        
        int segmentWidth1500 = (int) ((500.0 / MAX_POWER) * (width - 4));
        g2d.setColor(colorMidBg);
        g2d.fillRect(x, 2, segmentWidth1500, height - 4);
        x += segmentWidth1500;
        
        int remainingWidth = width - 4 - x;
        if (remainingWidth > 0) {
            g2d.setColor(colorHighBg);
            g2d.fillRect(x, 2, remainingWidth, height - 4);
        }
        
        if (currentPower > 0) {
            int barWidth = (int) ((currentPower / MAX_POWER) * (width - 4));
            barWidth = Math.max(0, barWidth);
            
            int barX = 2;
            int barRemaining = barWidth;
            
            if (barRemaining > 0) {
                int seg1000 = (int) ((1000.0 / MAX_POWER) * (width - 4));
                int fill1000 = Math.min(seg1000, barRemaining);
                g2d.setColor(colorLow);
                g2d.fillRect(barX, 2, fill1000, height - 4);
                barX += fill1000;
                barRemaining -= fill1000;
            }
            
            if (barRemaining > 0) {
                int seg1500 = (int) ((500.0 / MAX_POWER) * (width - 4));
                int fill1500 = Math.min(seg1500, barRemaining);
                g2d.setColor(colorMid);
                g2d.fillRect(barX, 2, fill1500, height - 4);
                barX += fill1500;
                barRemaining -= fill1500;
            }
            
            if (barRemaining > 0) {
                g2d.setColor(colorHigh);
                g2d.fillRect(barX, 2, barRemaining, height - 4);
            }
        }
        
        if (showPeak && peakPower > 0) {
            double peakPosition = (peakPower / MAX_POWER);
            int peakX = (int) (peakPosition * (width - 4)) + 2;
            peakX = Math.max(2, Math.min(peakX, width - 5));
            
            Color peakColor;
            if (peakPower <= 1000) {
                peakColor = colorLow;
            } else if (peakPower <= 1500) {
                peakColor = colorMid;
            } else {
                peakColor = colorHigh;
            }
            
            g2d.setColor(peakColor);
            g2d.fillRect(peakX - 2, 2, 4, height - 4);
        }
        
        g2d.dispose();
    }
    
    private Color lerpColor(Color c1, Color c2, float t) {
        t = Math.max(0, Math.min(1, t));
        int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * t);
        int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * t);
        int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * t);
        return new Color(r, g, b);
    }
}
