package com.wt2p.lp100a;

import org.junit.Test;
import static org.junit.Assert.*;

public class PowerDataDtoTest {

    private String[] createTestDataArray() {
        return new String[]{
            "100.5",
            "50.0",
            "45.0",
            "75.0",
            "W1AW",
            "1",
            "105.2",
            "50.0",
            "1.5"
        };
    }

    @Test
    public void testParseForwardPower() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals(100.5, dto.getForwardPower(), 0.01);
    }

    @Test
    public void testParseZValue() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals(50.0, dto.getZValue(), 0.01);
    }

    @Test
    public void testParsePhaseValue() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals(45.0, dto.getPhaseValue(), 0.01);
    }

    @Test
    public void testParseAlarmSetPoint() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals(75.0, dto.getAlarmSetPoint(), 0.01);
    }

    @Test
    public void testParseCallsign() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals("W1AW", dto.getCallsign());
    }

    @Test
    public void testParsePeakHold() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals(105.2, dto.getPeakHold(), 0.01);
    }

    @Test
    public void testParseDBm() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals(50.0, dto.get_dBm(), 0.01);
    }

    @Test
    public void testParseSWR() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals(1.5, dto.getSWR(), 0.01);
    }

    @Test
    public void testFormattedForwardPower() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals("100.50", dto.getFormattedForwardPower());
    }

    @Test
    public void testGetSWRInteger() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals(150, dto.getSWRInteger());
    }

    @Test
    public void testGet_dbRL_Integer() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals(13, dto.get_dbRL_Integer());
    }

    @Test
    public void testGetTransmittingRadio() {
        String[] data = createTestDataArray();
        PowerDataDto dto = new PowerDataDto(data);
        assertEquals(0, dto.getTransmittingRadio());
    }
}