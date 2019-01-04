package pt.ipp.isep.dei.project.model;

import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.Assert.*;

public class PowerSourceListTest {

    @Test
    public void seeIfContainsPowerSource() {
    }

    @Test
    public void seeIfGetPowerSources() {

    }

    @Test
    public void seeIfGetPowerSourceList() {
    }

    @Test
    public void seeHashCodeDummyTest() {
        PowerSource pS1 = new PowerSource("Energia", 50, 50);
        PowerSourceList pSList1 = new PowerSourceList();
        pSList1.addPowerSource(pS1);
        int expectedResult = 1;
        int actualResult = pSList1.hashCode();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void seeIfEqualsPowerSourceWithDifferentObject() {
        PowerSource pS1 = new PowerSource("Energia", 50, 50);
        int teste = 3;
        PowerSourceList pSList1 = new PowerSourceList();
        pSList1.addPowerSource(pS1);
        boolean actualResult = pSList1.equals(teste);
        boolean expectedResult = false;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void seeIfEqualsPowerSourceWithDifferentContent() {
        PowerSource pS1 = new PowerSource("Energia", 50, 50);
        PowerSource pS2 = new PowerSource("Muita Energia", 50, 50);
        PowerSourceList pSList1 = new PowerSourceList();
        PowerSourceList pSList2 = new PowerSourceList();
        pSList1.addPowerSource(pS1);
        pSList2.addPowerSource(pS2);
        boolean actualResult = pSList1.equals(pSList2);
        boolean expectedResult = false;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void seeIfEqualsPowerSourceWithSameContent() {
        PowerSource pS1 = new PowerSource("Energia", 50, 50);
        PowerSource pS2 = new PowerSource("Energia", 50, 50);
        PowerSourceList pSList1 = new PowerSourceList();
        PowerSourceList pSList2 = new PowerSourceList();
        pSList1.addPowerSource(pS1);
        pSList2.addPowerSource(pS2);
        boolean actualResult = pSList1.equals(pSList2);
        boolean expectedResult = true;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void seeIfequalsSameObject() {
        PowerSource pS1 = new PowerSource("Energia", 50, 50);
        PowerSourceList pSList1 = new PowerSourceList();
        pSList1.addPowerSource(pS1);
        boolean actualResult = pSList1.equals(pSList1);
        boolean expectedResult = true;
        assertEquals(expectedResult, actualResult);
    }
}