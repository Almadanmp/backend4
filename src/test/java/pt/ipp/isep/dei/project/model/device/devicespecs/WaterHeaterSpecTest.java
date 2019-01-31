package pt.ipp.isep.dei.project.model.device.devicespecs;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.project.TestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WaterHeaterSpec tests class.
 */

class WaterHeaterSpecTest {

    @Test
    void getTypeTest() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        String expectedResult = "WaterHeater";
        String result = waterHeaterSpec.getType();
        assertEquals(expectedResult, result);
    }

    //getConsumption Tests

    @Test
    void getConsumptionTest() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 0.6D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 30D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 0.9D);
        Double coldT = 12.0;
        Double waterV = 300.0;
        Double hotT = 25.0;
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, hotT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_COLD_WATER_TEMP, coldT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER_HEAT, waterV);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, waterV);
        Double expectedResult = 0.17008875;
        Double result = waterHeaterSpec.getConsumption();
        assertEquals(expectedResult, result);
    }

    @Test
    void getConsumptionWithSetsFailsTest() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        Double coldT = 30.0;
        Double waterV = 200.0;
        Double hotT = 25.0;
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, hotT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_COLD_WATER_TEMP, coldT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER_HEAT, waterV);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, waterV);
        double expectedResult = -1;
        double result = waterHeaterSpec.getConsumption();
        assertEquals(expectedResult, result);
    }

    @Test
    void getConsumptionTestFails() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 0.6D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 30D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 0.9D);
        Double coldT = 200.0;
        Double waterV = 300.0;
        waterHeaterSpec.setAttributeValue(TestUtils.WH_COLD_WATER_TEMP, coldT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER_HEAT, waterV);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, waterV);
        double expectedResult = -1;
        double result = waterHeaterSpec.getConsumption();
        assertEquals(expectedResult, result);
    }

    @Test
    void getConsumptionTestColdWaterEqualsHotWater() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        Double coldT = 25.0;
        Double waterV = 100.0;
        Double hotT = 25.0;
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, hotT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_COLD_WATER_TEMP, coldT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER_HEAT, waterV);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, waterV);
        double expectedResult = -1;
        double result = waterHeaterSpec.getConsumption();
        assertEquals(expectedResult, result);
    }

    @Test
    void getConsumptionTestColdWaterEqualsHotWaterDifString() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 0.6D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 30D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 0.9D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 0.9D);
        Double coldT = 25.0;
        Double waterV = 100.0;
        Double hotT = 25.0;
        waterHeaterSpec.setAttributeValue("dgfhfjg", coldT);
        waterHeaterSpec.setAttributeValue("Volume Of Water To Heat", waterV);
        waterHeaterSpec.setAttributeValue("adsdfgh", hotT);
        double expectedResult = 0.1308375;
        double result = waterHeaterSpec.getConsumption();
        assertEquals(expectedResult, result);
    }

    @Test
    void getConsumptionTestColdWaterMinorHotWater() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 0.6D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 30D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 0.9D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 0.9D);
        Double coldT = 2.0;
        Double waterV = 100.0;
        Double hotT = 25.0;
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, hotT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_COLD_WATER_TEMP, coldT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER_HEAT, waterV);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, waterV);
        double expectedResult = 0.10030875;
        double result = waterHeaterSpec.getConsumption();
        assertEquals(expectedResult, result);
    }

    @Test
    void getConsumptionTestColdWaterMinorHotWaterDifferentString() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 0.6D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 30D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 0.9D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 0.9D);
        double expectedResult = 0.0;
        double result = waterHeaterSpec.getConsumption();
        assertEquals(expectedResult, result);
    }

    @Test
    void getConsumptionTestColdWaterMinorHotWater2() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        Double coldT = 30.0;
        Double waterV = 800.0;
        Double hotT = 25.0;
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, hotT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_COLD_WATER_TEMP, coldT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER_HEAT, waterV);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, waterV);
        double expectedResult = -1;
        double result = waterHeaterSpec.getConsumption();
        assertEquals(expectedResult, result);
    }

    @Test
    void getConsumptionTestColdWaterMinorHotWater3() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        Double coldT = 25.0;
        Double waterV = 800.0;
        Double hotT = 25.0;
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, hotT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_COLD_WATER_TEMP, coldT);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER_HEAT, waterV);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, waterV);
        double expectedResult = -1;
        double result = waterHeaterSpec.getConsumption();
        assertEquals(expectedResult, result);
    }


    //Test Attributes

    @Test
    void seeIfGetAndSetAttributeValue() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 0.6D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 30D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 0.9D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 0.9D);
        Double volumeOfWater = 0.6;
        String attribute = "Volume Of Water";
        Double expectedResult = 0.6;
        boolean setResult = waterHeaterSpec.setAttributeValue(attribute, volumeOfWater);
        Object getResult = waterHeaterSpec.getAttributeValue(attribute);
        assertEquals(expectedResult, getResult);
        assertTrue(setResult);
    }


    @Test
    void seeIfSetAttributeValueInvalid() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 12D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 40D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 234D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 234D);
        Double value = 0.6;
        String attribute = "invalid";
        boolean result = waterHeaterSpec.setAttributeValue(attribute, value);
        assertFalse(result);
    }

    @Test
    void seeIfGetAttributeNames() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(WaterHeaterSpec.ATTRIBUTE_VOLUME_OF_WATER, 12D);
        waterHeaterSpec.setAttributeValue(WaterHeaterSpec.ATTRIBUTE_HOT_WATER_TEMP, 40D);
        waterHeaterSpec.setAttributeValue(WaterHeaterSpec.ATTRIBUTE_PERFORMANCE_RATIO, 234D);
        waterHeaterSpec.setAttributeValue(WaterHeaterSpec.NOMINAL_POWER, 23D);
        List<String> result = waterHeaterSpec.getAttributeNames();
        assertTrue(result.contains(WaterHeaterSpec.ATTRIBUTE_VOLUME_OF_WATER));
        assertTrue(result.contains(WaterHeaterSpec.ATTRIBUTE_HOT_WATER_TEMP));
        assertTrue(result.contains(WaterHeaterSpec.ATTRIBUTE_PERFORMANCE_RATIO));
        assertTrue(result.contains(WaterHeaterSpec.NOMINAL_POWER));
        assertEquals(result.size(), 4);
    }

    @Test
    void seeIfGetAttributeValueDefaultTest() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        String attribute = "Lisboa";
        Double expectedResult = 0.0;
        Object getResult = waterHeaterSpec.getAttributeValue(attribute);
        assertEquals(expectedResult, getResult);
    }


    @Test
    void seeIfGetAndSetAttributeValues() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 12D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 40D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 234D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 234D);
        String attribute = "Volume Of Water";
        Double expectedResult = 2.0;
        Double attributeValue = 2.0;
        boolean setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        Object getResult = waterHeaterSpec.getAttributeValue(attribute);
        assertEquals(expectedResult, getResult);
        assertTrue(setResult);

        attribute = "Hot Water Temperature";
        attributeValue = 3.0;
        expectedResult = 3.0;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        getResult = waterHeaterSpec.getAttributeValue(attribute);
        assertEquals(expectedResult, getResult);
        assertTrue(setResult);

        attribute = "Cold Water Temperature";
        attributeValue = 4.0;
        expectedResult = 4.0;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        getResult = waterHeaterSpec.getAttributeValue(attribute);
        assertEquals(expectedResult, getResult);
        assertTrue(setResult);

        attribute = "Performance Ratio";
        expectedResult = 5.0;
        attributeValue = 5.0;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        getResult = waterHeaterSpec.getAttributeValue(attribute);
        assertEquals(expectedResult, getResult);
        assertTrue(setResult);

        attribute = "Volume Of Water To Heat";
        expectedResult = 10.0;
        attributeValue = 10.0;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        getResult = waterHeaterSpec.getAttributeValue(attribute);
        assertEquals(expectedResult, getResult);
        assertTrue(setResult);

        attribute = "nominal power";
        expectedResult = 10.0;
        attributeValue = 10.0;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        getResult = waterHeaterSpec.getAttributeValue(attribute);
        assertEquals(expectedResult, getResult);
        assertTrue(setResult);
    }

    @Test
    void seeIFSetAttributeValuesFails() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 12D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 40D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 234D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 234D);
        String attribute = "volumeOfWater";
        int attributeValue = 2;
        boolean setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);

        attribute = "hotWaterTemperature";
        attributeValue = 3;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);

        attribute = "coldWaterTemperature";
        attributeValue = 4;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);

        attribute = "performanceRatio";
        attributeValue = 5;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);

        attribute = "volumeOfWaterToHeat";
        attributeValue = 10;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);

        attribute = "nominal power";
        attributeValue = 10;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);
    }

    @Test
    void seeIFSetAttributeValuesFails2() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 12D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 40D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 234D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 234D);

        String attribute = "njfdjkndfk";
        int attributeValue = 2;
        boolean setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);

        attribute = "htfcf";
        attributeValue = 3;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);

        attribute = "fhj";
        attributeValue = 4;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);

        attribute = "fhjg";
        attributeValue = 5;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);

        attribute = "gfdjcktuyvuh";
        attributeValue = 10;
        setResult = waterHeaterSpec.setAttributeValue(attribute, attributeValue);
        assertFalse(setResult);
    }

    @Test
    void testSetAttributeCoveringAllCasesFalse() {
        //Arrange
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        Double attribute = 6.0;
        waterHeaterSpec.setAttributeValue(TestUtils.F_FREEZER_CAPACITY, null);
        waterHeaterSpec.setAttributeValue(TestUtils.F_REFRIGERATOR_CAPACITY, null);
        waterHeaterSpec.setAttributeValue(TestUtils.F_ANNUAL_CONSUMPTION, null);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, null);

        // original strings:
        assertTrue(waterHeaterSpec.setAttributeValue("Volume Of Water", attribute));
        assertTrue(waterHeaterSpec.setAttributeValue("Cold Water Temperature", attribute));
        assertTrue(waterHeaterSpec.setAttributeValue("Hot Water Temperature", attribute));
        assertTrue(waterHeaterSpec.setAttributeValue("Performance Ratio", attribute));
        assertTrue(waterHeaterSpec.setAttributeValue("Volume Of Water To Heat", attribute));
        assertTrue(waterHeaterSpec.setAttributeValue("nominal power", attribute));

        // same hash codes, but different strings:
        assertFalse(waterHeaterSpec.setAttributeValue("\0Volume Of Water", attribute));
        assertFalse(waterHeaterSpec.setAttributeValue("\0Cold Water Temperature", attribute));
        assertFalse(waterHeaterSpec.setAttributeValue("\0Hot Water Temperature", attribute));
        assertFalse(waterHeaterSpec.setAttributeValue("\0Performance Ratio", attribute));
        assertFalse(waterHeaterSpec.setAttributeValue("\0Volume Of Water To Heat", attribute));
        assertFalse(waterHeaterSpec.setAttributeValue("\0nominal power", attribute));

        // distinct hash code to cover default cases of switches
        assertFalse(waterHeaterSpec.setAttributeValue("", attribute));
    }


    @Test
    void seeIfGetAndSetAttributeValues2() {
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 12D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 40D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 234D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 234D);
        Double attributeValue = 3.0;
        boolean setResult = waterHeaterSpec.setAttributeValue(null, attributeValue);
        assertFalse(setResult);
    }


    @Test
    void testGetAttributeCoveringAllCases() {
        //Arrange
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 5D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 5D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 5D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 5D);

        // original strings:
        assertEquals(5.0, waterHeaterSpec.getAttributeValue("Volume Of Water"));
        assertEquals(5.0, waterHeaterSpec.getAttributeValue("Hot Water Temperature"));
        assertEquals(0.0, waterHeaterSpec.getAttributeValue("Cold Water Temperature"));
        assertEquals(5.0, waterHeaterSpec.getAttributeValue("Performance Ratio"));
        assertEquals(0.0, waterHeaterSpec.getAttributeValue("Volume Of Water To Heat"));
        assertEquals(5.0, waterHeaterSpec.getAttributeValue("nominal power"));

        // same hash codes, but different strings:
        assertEquals(0.0, waterHeaterSpec.getAttributeValue("\0Volume Of Water"));
        assertEquals(0.0, waterHeaterSpec.getAttributeValue("\0Hot Water Temperature"));
        assertEquals(0.0, waterHeaterSpec.getAttributeValue("\0Cold Water Temperature"));
        assertEquals(0.0, waterHeaterSpec.getAttributeValue("\0Performance Ratio"));
        assertEquals(0.0, waterHeaterSpec.getAttributeValue("\0Volume Of Water To Heat"));
        assertEquals(0.0, waterHeaterSpec.getAttributeValue("\0nominal power"));

        // distinct hash code to cover default cases of switches
        assertEquals(0.0, waterHeaterSpec.getAttributeValue(""));
    }

    @Test
    void testGetAttributeUnitCoveringAllCases() {
        //Arrange
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        waterHeaterSpec.setAttributeValue(TestUtils.WH_VOLUME_OF_WATER, 5D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_HOT_WATER_TEMP, 5D);
        waterHeaterSpec.setAttributeValue(TestUtils.WH_PERFORMANCE_RATIO, 5D);
        waterHeaterSpec.setAttributeValue(TestUtils.NOMINAL_POWER, 5D);

        // original strings:
        assertEquals("L", waterHeaterSpec.getAttributeUnit("Volume Of Water"));
        assertEquals("ºC", waterHeaterSpec.getAttributeUnit("Hot Water Temperature"));
        assertEquals("ºC", waterHeaterSpec.getAttributeUnit("Cold Water Temperature"));
        assertEquals("", waterHeaterSpec.getAttributeUnit("Performance Ratio"));
        assertEquals("L", waterHeaterSpec.getAttributeUnit("Volume Of Water To Heat"));
        assertEquals("kW", waterHeaterSpec.getAttributeUnit("nominal power"));

        // same hash codes, but different strings:
        assertEquals(false, waterHeaterSpec.getAttributeUnit("\0VolumeOfWater"));
        assertEquals(false, waterHeaterSpec.getAttributeUnit("\0HotWaterTemperature"));
        assertEquals(false, waterHeaterSpec.getAttributeUnit("\0ColdWaterTemperature"));
        assertEquals(false, waterHeaterSpec.getAttributeUnit("\0PerformanceRatio"));
        assertEquals(false, waterHeaterSpec.getAttributeUnit("\0VolumeOfWaterToHeat"));
        assertEquals(false, waterHeaterSpec.getAttributeUnit("\0nominal power"));

        // distinct hash code to cover default cases of switches
        assertEquals(false, waterHeaterSpec.getAttributeUnit(""));
    }

    @Test
    void testSetAttributeCoveringAllCases() {
        //Arrange
        WaterHeaterSpec waterHeaterSpec = new WaterHeaterSpec();
        Double attribute = 6.0;

        // original strings:
        assertTrue(waterHeaterSpec.setAttributeValue("Volume Of Water", attribute));
        assertTrue(waterHeaterSpec.setAttributeValue("Hot Water Temperature", attribute));
        assertTrue(waterHeaterSpec.setAttributeValue("Cold Water Temperature", attribute));
        assertTrue(waterHeaterSpec.setAttributeValue("Performance Ratio", attribute));
        assertTrue(waterHeaterSpec.setAttributeValue("Volume Of Water To Heat", attribute));
        assertTrue(waterHeaterSpec.setAttributeValue("nominal power", attribute));

        // same hash codes, but different strings:
        assertFalse(waterHeaterSpec.setAttributeValue("\0Volume Of Water", attribute));
        assertFalse(waterHeaterSpec.setAttributeValue("\0Hot Water Temperature", attribute));
        assertFalse(waterHeaterSpec.setAttributeValue("\0Cold Water Temperature", attribute));
        assertFalse(waterHeaterSpec.setAttributeValue("\0Performance Ratio", attribute));
        assertFalse(waterHeaterSpec.setAttributeValue("\0Volume Of Water To Heat", attribute));
        assertFalse(waterHeaterSpec.setAttributeValue("\0nominal power", attribute));

        // distinct hash code to cover default cases of switches
        assertFalse(waterHeaterSpec.setAttributeValue("", attribute));
    }
}