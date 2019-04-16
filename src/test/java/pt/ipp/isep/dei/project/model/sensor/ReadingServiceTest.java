package pt.ipp.isep.dei.project.model.sensor;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ipp.isep.dei.project.controller.ReaderController;
import pt.ipp.isep.dei.project.model.Local;
import pt.ipp.isep.dei.project.repository.AreaSensorRepository;
import pt.ipp.isep.dei.project.repository.ReadingRepository;
import pt.ipp.isep.dei.project.repository.SensorTypeRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static java.lang.Double.NaN;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AreaReadingList tests class.
 */
@ExtendWith(MockitoExtension.class)
class ReadingServiceTest {

    private ReadingService validReadingService;
    private AreaSensorService areaSensorService;
    private AreaSensor firstValidAreaSensor;
    private Date validDate1; // Date 21/11/2018
    private Date validDate2; // Date 03/09/2018
    private Date validDate3; // 31/09/2018 23:59:59
    private Date validDate4; // 07/10/2018 00:00:00
    private Date validDate5; // 08/10/2018 23:26:21
    private Date validDate6; // 09/10/2018 08:21:22
    private Date validDate7; // 10/10/2018 18:14:03
    private Date validDate8; // 23/10/2018 12:14:23
    private Date validDate9; // 13/10/2018 12:12:12
    private Date validDate10; // 30/10/2018 23:59:59
    private Date validDate11; // 01/11/2018 00:00:00
    private Date validDate12; // 02/11/2015
    private Date validDate16; // 13/10/2018 23:59:59
    private Date validDate13;
    private Date validDate14; // 02/10/2018 23:59:00
    private Date validDate15;
    private Date validDate18; // same day and month as 9 ans 16 but different year
    private Date validDate19; // same day and month as 9 ans 16 but different year, different hour
    private static final Logger logger = Logger.getLogger(ReaderController.class.getName());


    @Mock
    ReadingRepository readingRepository;

    @Mock
    AreaSensorRepository areaSensorRepository;

    @Mock
    SensorTypeRepository sensorTypeRepository;

    @BeforeEach
    void arrangeArtifacts() {
        validReadingService = new ReadingService(readingRepository);
        areaSensorService = new AreaSensorService(areaSensorRepository, sensorTypeRepository);
        SimpleDateFormat validSdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat validSdfDay = new SimpleDateFormat("dd/MM/yyyy");
        try {
            validDate12 = validSdf.parse("02/11/2015 20:00:00");
            validDate2 = validSdf.parse("03/09/2018 00:00:00");
            validDate3 = validSdf.parse("31/09/2018 23:59:59");
            validDate13 = validSdfDay.parse("03/10/2018");
            validDate14 = validSdf.parse("02/10/2018 23:59:00");
            validDate15 = validSdf.parse("03/10/2018 00:00:00");
            validDate4 = validSdf.parse("07/10/2018 00:00:00");
            validDate5 = validSdf.parse("08/10/2018 23:26:21");
            validDate6 = validSdf.parse("09/10/2018 08:21:22");
            validDate7 = validSdf.parse("10/10/2018 18:14:03");
            validDate9 = validSdf.parse("13/10/2018 12:12:12");
            validDate16 = validSdf.parse("13/10/2018 23:59:59");
            validDate8 = validSdf.parse("23/10/2018 12:14:23");
            validDate10 = validSdf.parse("30/10/2018 23:59:59");
            validDate1 = validSdf.parse("21/11/2018 00:00:00");
            validDate11 = validSdf.parse("01/11/2018 00:00:00");
            validDate18 = validSdf.parse("13/10/2019 12:12:12");
            validDate19 = validSdf.parse("13/10/2019 23:59:59");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        firstValidAreaSensor = new AreaSensor("SensorOne", "SensorOne", new SensorType("Temperature", "Celsius"), new Local(
                31, 1, 2), validDate1, 6008L);
        firstValidAreaSensor.setActive(true);
    }

    @Test
    void seeAddReadingIfListIsEmpty() {
        // Arrange

        Reading reading1 = new Reading(17, validDate1, "C", "TEST");

        // Act

        boolean actualResult = validReadingService.addReading(reading1);

        // Assert

        assertTrue(actualResult);
    }

    @Test
    void seeIfGetTotalFromList() {
        // Arrange

        List<Double> list = new ArrayList<>();
        list.add(1.0);
        list.add(2.0);

        // Act

        double actualResult = validReadingService.getListSum(list);

        // Assert

        assertEquals(3.0, actualResult);
    }

    @Test
    void seeTotalFromEmptyList() {
        // Arrange

        List<Double> list = new ArrayList<>();

        // Act

        double actualResult = validReadingService.getListSum(list);

        // Assert

        assertEquals(0.0, actualResult);
    }

    @Test
    void seeIfAddsDifferentReadings() {
        // Arrange

        Reading reading1 = new Reading(17, validDate1, "C", "TEST");
        Reading reading2 = new Reading(29, validDate2, "C", "TEST");
        validReadingService.addReading(reading1);

        // Act

        boolean actualResult = validReadingService.addReading(reading2);

        // Assert

        assertTrue(actualResult);
    }

    @Test
    void seeIfAddsSameReading() {
        // Arrange

        Reading reading1 = new Reading(17, validDate1, "C", "TEST");
        Reading reading2 = new Reading(17, validDate1, "C", "TEST");
        validReadingService.addReading(reading1);

        // Act

        boolean actualResult = validReadingService.addReading(reading2);

        // Assert

        assertFalse(actualResult);
    }

    @Test
    void seeIfGetsValueFromList() {
        // Arrange

        Reading reading1 = new Reading(15, validDate1, "C", "TEST");
        Reading reading2 = new Reading(29, validDate2, "C", "TEST");
        validReadingService.addReading(reading1);
        validReadingService.addReading(reading2);
        double expectedResult = 15;

        // Act

        double actualResult = validReadingService.get(0).getValue();

        // Assert

        assertEquals(expectedResult, actualResult, 0.1);
    }

    @Test
    void seeIfGetsSingularValue() {
        // Arrange

        Reading reading1 = new Reading(15, validDate1, "C", "TEST");
        Reading reading2 = new Reading(29, validDate2, "C", "TEST");
        validReadingService.addReading(reading1);
        validReadingService.addReading(reading2);
        double expectedResult = 29;

        // Act

        double actualResult = validReadingService.get(1).getValue();

        // Assert

        assertEquals(expectedResult, actualResult, 0.1);
    }

    @Test
    void seeIfGetDatesWithReadingsBetweenTwoGivenDates() {
        // Arrange

        Reading r0 = new Reading(23, validDate3, "C", "TEST"); //  01 Oct 23:59:59 (2018)
        Reading r1 = new Reading(23, validDate4, "C", "TEST"); //  07 Oct 00:00:00 "
        Reading r2 = new Reading(24, validDate5, "C", "TEST"); //  08 Oct 23:26:21 "
        Reading r3 = new Reading(25, validDate6, "C", "TEST"); //  09 Oct 08:21:22 "
        Reading r4 = new Reading(26, validDate7, "C", "TEST"); //  10 Oct 18:14:03 "
        Reading r5 = new Reading(23, validDate8, "C", "TEST"); //  23 Oct 12:14:23 "
        Reading r6 = new Reading(22, validDate9, "C", "TEST"); //  13 Oct 12:12:12 "
        Reading r7 = new Reading(23, validDate10, "C", "TEST"); // 30 Oct 23:59:59 "
        Reading r8 = new Reading(22, validDate11, "C", "TEST"); // 01 Nov 01:00:00 "
        Reading r9 = new Reading(23, validDate18, "C", "TEST"); // 13 Oct 12:12:12 (2019)
        Reading r10 = new Reading(22, validDate19, "C", "TEST"); // 13 Oct 23:59:59 "
        validReadingService.addReading(r0);
        validReadingService.addReading(r1);
        validReadingService.addReading(r2);
        validReadingService.addReading(r3);
        validReadingService.addReading(r4);
        validReadingService.addReading(r5);
        validReadingService.addReading(r6);
        validReadingService.addReading(r7);
        validReadingService.addReading(r8);
        validReadingService.addReading(r9);
        validReadingService.addReading(r10);
        List<Date> expectedResult = new ArrayList<>();
        expectedResult.add(validDate4);
        expectedResult.add(validDate5);
        expectedResult.add(validDate6);
        expectedResult.add(validDate7);
        expectedResult.add(validDate8);
        expectedResult.add(validDate9);
        expectedResult.add(validDate10);
        expectedResult.add(validDate11);
        expectedResult.add(validDate18);

        // Act

        List<Date> actualResult = validReadingService.getDaysWithReadingsBetweenDates(validDate4, validDate19);

        // Assert

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfGetDatesWithReadingsBetweenTwoGivenDatesIfMostRecentReadingsAreIntroducedFirst() {
        // Arrange

        Reading r0 = new Reading(23, validDate3, "C", "TEST"); //  01 Oct 23:59:59 (2018)
        Reading r1 = new Reading(23, validDate4, "C", "TEST"); //  07 Oct 00:00:00 "
        Reading r2 = new Reading(24, validDate5, "C", "TEST"); //  08 Oct 23:26:21 "
        Reading r3 = new Reading(25, validDate6, "C", "TEST"); //  09 Oct 08:21:22 "
        Reading r4 = new Reading(26, validDate7, "C", "TEST"); //  10 Oct 18:14:03 "
        Reading r5 = new Reading(23, validDate8, "C", "TEST"); //  23 Oct 12:14:23 "
        Reading r6 = new Reading(22, validDate9, "C", "TEST"); //  13 Oct 12:12:12 "
        Reading r7 = new Reading(23, validDate10, "C", "TEST"); // 30 Oct 23:59:59 "
        Reading r8 = new Reading(22, validDate11, "C", "TEST"); // 01 Nov 01:00:00 "
        Reading r9 = new Reading(23, validDate18, "C", "TEST"); // 13 Oct 12:12:12 (2019)
        Reading r10 = new Reading(22, validDate19, "C", "TEST"); // 13 Oct 23:59:59 "
        validReadingService.addReading(r10);
        validReadingService.addReading(r9);
        validReadingService.addReading(r8);
        validReadingService.addReading(r7);
        validReadingService.addReading(r6);
        validReadingService.addReading(r5);
        validReadingService.addReading(r4);
        validReadingService.addReading(r3);
        validReadingService.addReading(r2);
        validReadingService.addReading(r1);
        validReadingService.addReading(r0);
        List<Date> expectedResult = new ArrayList<>();
        expectedResult.add(validDate19);
        expectedResult.add(validDate11);
        expectedResult.add(validDate10);
        expectedResult.add(validDate9);
        expectedResult.add(validDate8);
        expectedResult.add(validDate7);
        expectedResult.add(validDate6);
        expectedResult.add(validDate5);
        expectedResult.add(validDate4);

        // Act

        List<Date> actualResult = validReadingService.getDaysWithReadingsBetweenDates(validDate4, validDate19);

        // Assert

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfGetsAverage() {
        // Arrange

        List<Double> doubleList = new ArrayList<>();
        double expectedResult = 22.40;
        doubleList.add(1D);
        doubleList.add(23D);
        doubleList.add(43.2D);

        // Act

        double actualResult = validReadingService.getAvgFromList(doubleList);

        // Assert

        assertEquals(expectedResult, actualResult, 0.001);
    }

    @Test
    void seeIfAverageIsZeroInEmptyList() {
        // Arrange

        List<Double> doubleList = new ArrayList<>();

        // Act

        double actualResult = validReadingService.getAvgFromList(doubleList);

        // Assert

        assertEquals(0, actualResult, 0.001);
    }

    @Test
    void seeIfGetsMostRecentValueWorks() {
        // Arrange

        Date testDate = new GregorianCalendar(2018, Calendar.NOVEMBER, 3).getTime();
        Reading earlierReading = new Reading(15, validDate12, "C", "TEST");
        Reading laterReading = new Reading(30, testDate, "C", "TEST");
        List<Reading>readings = new ArrayList<>();
        readings.add(earlierReading);
        readings.add(laterReading);
        double expectedResult = 30.0;

        // Act

        double result = validReadingService.getMostRecentValue(readings);

        // Assert

        assertEquals(expectedResult, result, 0.01);

    }

    @Test
    void seeIfGetMostRecentValueWorksInSameDay() {
        // Arrange

        Date testDate = new GregorianCalendar(2015, Calendar.NOVEMBER, 2, 5, 0,
                0).getTime();
        Reading earlierReading = new Reading(15, validDate12, "C", "TEST");
        Reading laterReading = new Reading(30, testDate, "C", "TEST");
        List<Reading>readings = new ArrayList<>();
        readings.add(earlierReading);
        readings.add(laterReading);
        double expectedResult = 15.0;

        // Act

        double result = validReadingService.getMostRecentValue(readings);

        // Assert

        assertEquals(expectedResult, result, 0.01);

    }

    @Test
    void seeIfReadingListIsEmpty() {

        // Act

        boolean actualResult = validReadingService.isEmpty();

        // Assert

        assertTrue(actualResult);
    }

    @Test
    void seeIfReadingListIsNotEmpty() {
        // Arrange

        Reading testReading = new Reading(31, validDate3, "C", "TEST");
        validReadingService.addReading(testReading);

        // Act

        boolean actualResult = validReadingService.isEmpty();

        // Assert

        assertFalse(actualResult);
    }

    @Test
    void seeIfGetsTotalReadings() {
        // Arrange

        List<Reading> readings = new ArrayList<>();
        Reading reading = new Reading(20, validDate15, "C", "TEST");
        Reading reading2 = new Reading(20, validDate3, "C", "TEST");
        Reading reading3 = new Reading(20, validDate7, "C", "TEST");
        Reading reading4 = new Reading(20, validDate14, "C", "TEST");
        readings.add(reading);
        readings.add(reading2);
        readings.add(reading3);
        readings.add(reading4);
        double expectedResult = 20;

        // Act

        double actualResult = validReadingService.getValueReadingsInDay(validDate13, readings);

        // Assert

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfGetsTotalReadingsInDayWorksNoReadings() {

        // Act
        List<Reading> readings = new ArrayList<>();
        Throwable exception = assertThrows(IllegalStateException.class,
                () -> validReadingService.getValueReadingsInDay(validDate13, readings));

        // Assert

        assertEquals("Warning: Total value was not calculated - No readings were available.", exception.getMessage());
    }

    @Test
    void seeIfGetsMostRecentReading() {
        // This test is particularly complex, but it tests several failure cases. The particular failure scenarios
        // are expanded on next to each assert.

        // Arrange

        ReadingService readingService2 = new ReadingService();
        ReadingService readingService3 = new ReadingService();
        ReadingService readingService4 = new ReadingService();
        ReadingService readingService5 = new ReadingService();
        Reading secondMostRecentReading = new Reading(22, validDate14, "C", "TEST");
        Reading mostRecentReading = new Reading(25, validDate15, "C", "TEST");
        Reading oldestReading = new Reading(27, validDate3, "C", "TEST");
        validReadingService.addReading(oldestReading);
        validReadingService.addReading(secondMostRecentReading);
        validReadingService.addReading(mostRecentReading);
        readingService2.addReading(mostRecentReading);
        readingService2.addReading(oldestReading);
        readingService2.addReading(secondMostRecentReading);
        readingService3.addReading(mostRecentReading);
        readingService3.addReading(secondMostRecentReading);
        readingService3.addReading(oldestReading);
        readingService4.addReading(oldestReading);
        readingService4.addReading(secondMostRecentReading);
        readingService4.addReading(oldestReading);
        Reading error = new Reading(NaN, new GregorianCalendar(1900, Calendar.JANUARY, 1).getTime(), "C", "TEST");

        // Act

        Reading actualResult1 = validReadingService.getMostRecentReading();
        Reading actualResult2 = readingService2.getMostRecentReading();
        Reading actualResult3 = readingService3.getMostRecentReading();
        Reading actualResult4 = readingService4.getMostRecentReading();
        Reading actualResult5 = readingService5.getMostRecentReading();

        // Assert

        assertEquals(mostRecentReading, actualResult1); // Tests if method works when most recent reading is the first on the list.
        assertEquals(mostRecentReading, actualResult2); // Tests if method works when most recent reading is in the middle of the list.
        assertEquals(mostRecentReading, actualResult3); // Tests if method works when most recent reading is the last on the list.
        assertEquals(secondMostRecentReading, actualResult4); // Tests if method works when most recent reading happens more than once.
        assertEquals(error, actualResult5); // Tests if method works when there are no readings.
    }

    @Test
    void seeAllEqualsConditions() {
        // Arrange

        ReadingService readingService2 = new ReadingService();
        ReadingService readingService3 = new ReadingService();
        Reading reading1 = new Reading(22, validDate14, "C", "TEST");
        Reading reading2 = new Reading(25, validDate15, "C", "TEST");
        validReadingService.addReading(reading1);
        validReadingService.addReading(reading2);
        readingService2.addReading(reading1);
        readingService2.addReading(reading2);
        readingService3.addReading(reading2);
        readingService3.addReading(reading1);

        // Act

        boolean actualResult1 = validReadingService.equals(readingService2);
        boolean actualResult2 = validReadingService.equals(validReadingService); //Necessary for Sonarqube testing purposes.
        boolean actualResult3 = validReadingService.equals(readingService3);
        boolean actualResult4 = validReadingService.equals(2D); //Necessary for Sonarqube testing purposes.

        // Assert

        assertTrue(actualResult1);
        assertTrue(actualResult2);
        assertFalse(actualResult3);
        assertFalse(actualResult4);
    }

    @Test
    void seeIfGetsFirstSecondOfDay() {
        // Arrange

        Date expectedResult = new GregorianCalendar(2018, Calendar.OCTOBER, 2).getTime();

        // Assert

        assertEquals(expectedResult, validReadingService.getFirstSecondOfDay(validDate14));
    }


    @Test
    void seeIfGetsLastSecondOfDay() {
        // Arrange

        Date expectedResult = new GregorianCalendar(2018, Calendar.OCTOBER, 2, 23, 59, 59).getTime();

        // Assert

        assertEquals(expectedResult, validReadingService.getLastSecondOfDay(validDate14));
    }

    @Test
    void seeIfReadingDateWithinTwoDates() {
        //Arrange

        Reading testReading = new Reading(22, validDate14, "C", "TEST");
        validReadingService.addReading(testReading);

        // Act

        boolean actualResult = validReadingService.isReadingDateBetweenTwoDates(testReading.getDate(), validDate12,
                validDate16);

        //Assert

        assertTrue(actualResult);
    }

    @Test
    void seeIfReadingDateWithinTwoDatesFalse() {
        //Arrange

        Reading testReading = new Reading(22, validDate14, "C", "TEST");
        validReadingService.addReading(testReading);

        // Act

        boolean actualResult = validReadingService.isReadingDateBetweenTwoDates(testReading.getDate(), validDate13,
                validDate15);

        //Assert

        assertFalse(actualResult);
    }

    @Test
    void getByIndexEmptyReadingList() {
        //Arrange

        ReadingService emptyList = new ReadingService();

        //Act

        Throwable exception = assertThrows(IndexOutOfBoundsException.class, () -> emptyList.get(0));

        //Assert

        assertEquals("The reading list is empty.", exception.getMessage());
    }

    @Test
    void getElementsAsArray() {
        //Arrange

        Reading[] expectedResult1 = new Reading[0];
        Reading[] expectedResult2 = new Reading[1];
        Reading[] expectedResult3 = new Reading[2];

        ReadingService emptyList = new ReadingService();
        validReadingService.addReading(new Reading(20, validDate1, "C", "TEST"));
        ReadingService validReadingService2 = new ReadingService();
        validReadingService2.addReading(new Reading(20, validDate1, "C", "TEST"));
        validReadingService2.addReading(new Reading(25, validDate2, "C", "TEST"));

        expectedResult2[0] = new Reading(20, validDate1, "C", "TEST");
        expectedResult3[0] = new Reading(20, validDate1, "C", "TEST");
        expectedResult3[1] = new Reading(25, validDate2, "C", "TEST");

        //Act

        Reading[] actualResult1 = emptyList.getElementsAsArray();
        Reading[] actualResult2 = validReadingService.getElementsAsArray();
        Reading[] actualResult3 = validReadingService2.getElementsAsArray();

        //Assert

        assertArrayEquals(expectedResult1, actualResult1);
        assertArrayEquals(expectedResult2, actualResult2);
        assertArrayEquals(expectedResult3, actualResult3);
    }

    @Test
    void appendListNoDuplicates() {
        //Arrange

        ReadingService expectedResult1 = new ReadingService();
        ReadingService expectedResult2 = new ReadingService();

        Reading reading1 = new Reading(20, validDate1, "C", "TEST");
        Reading reading2 = new Reading(22, validDate2, "C", "TEST");

        ReadingService emptyList = new ReadingService();
        validReadingService.addReading(reading1);
        ReadingService validReadingService2 = new ReadingService();
        validReadingService2.addReading(reading1);
        validReadingService2.addReading(reading2);

        expectedResult1.addReading(reading1);
        expectedResult2.addReading(reading1);
        expectedResult2.addReading(reading2);

        //Act

        ReadingService actualResult1 = validReadingService.appendListNoDuplicates(emptyList);

        //Assert

        assertEquals(expectedResult1, actualResult1);

        //Act

        ReadingService actualResult2 = emptyList.appendListNoDuplicates(validReadingService);
        ReadingService actualResult3 = validReadingService.appendListNoDuplicates(validReadingService2);

        //Assert

        assertEquals(expectedResult1, actualResult2);
        assertEquals(expectedResult2, actualResult3);
    }

    @Test
    void hashCodeDummyTest() {
        //Arrange

        Reading reading1 = new Reading(22, validDate14, "C", "TEST");
        validReadingService.addReading(reading1);

        // Act

        int actualResult = validReadingService.hashCode();

        // Assert

        assertEquals(actualResult, 1);
    }

    @Test
    void seeIfWeGetMaxValueOfTheDayWorks() {
        //Arrange
        List<Reading> readingService = new ArrayList<>();
        List<Reading> readingService2 = new ArrayList<>();
        List<Reading> readingService3 = new ArrayList<>();
        Reading reading1 = new Reading(22, new GregorianCalendar(2018, Calendar.OCTOBER, 8, 10, 0).getTime(), "C", "TEST");
        Reading reading2 = new Reading(22, new GregorianCalendar(2018, Calendar.OCTOBER, 8, 9, 0).getTime(), "C", "TEST");
        Reading reading3 = new Reading(25, new GregorianCalendar(2018, Calendar.OCTOBER, 8, 11, 0).getTime(), "C", "TEST");
        Reading reading4 = new Reading(19, new GregorianCalendar(2018, Calendar.OCTOBER, 8, 21, 30).getTime(), "C", "TEST");
        readingService.add(reading1);
        readingService.add(reading2);
        readingService2.add(reading2);
        readingService2.add(reading3);
        readingService3.add(reading3);
        readingService3.add(reading4);
        //Act
        Reading actualResult = validReadingService.getMaxValueOfTheDayDb(readingService, (new GregorianCalendar(2018, Calendar.OCTOBER, 8).getTime()));
        Reading actualResult2 = validReadingService.getMaxValueOfTheDayDb(readingService2, new GregorianCalendar(2018, Calendar.OCTOBER, 8).getTime());
        Reading actualResult3 = validReadingService.getMaxValueOfTheDayDb(readingService3, new GregorianCalendar(2018, Calendar.OCTOBER, 8).getTime());

        //Assert
        assertEquals(reading1, actualResult);
        assertEquals(reading3, actualResult2);
        assertEquals(reading3, actualResult3);
    }

    @Test
    void seeIfWeGetReadingListWithSpecificValueWorks() {
        //Arrange
        List<Reading> readingService = new ArrayList<>();
        List<Reading> expectedResult = new ArrayList<>();
        Reading r1 = new Reading(22, validDate2, "C", "TEST");
        Reading r2 = new Reading(24, validDate14, "C", "TEST");
        Reading r3 = new Reading(22, validDate2, "C", "TEST");
        Reading r4 = new Reading(21, validDate15, "C", "TEST");
        Reading r5 = new Reading(22, validDate12, "C", "TEST");
        Reading r6 = new Reading(29, validDate2, "C", "TEST");
        readingService.add(r1);
        readingService.add(r2);
        readingService.add(r3);
        readingService.add(r4);
        readingService.add(r5);
        readingService.add(r6);
        expectedResult.add(r1);
        expectedResult.add(r3);
        expectedResult.add(r5);
        //Act
        List<Reading> actualResult = validReadingService.getReadingListOfReadingsWithSpecificValueDb(readingService, 22.0);
        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfWeGetMinValueInReadingListWorks() {
        //Arrange
        ReadingService readingService = new ReadingService();
        ReadingService readingService2 = new ReadingService();
        ReadingService readingService3 = new ReadingService();
        Reading reading1 = new Reading(20, new GregorianCalendar(2018, Calendar.OCTOBER, 8, 2, 30).getTime(), "C", "TEST");
        Reading reading2 = new Reading(20, new GregorianCalendar(2018, Calendar.OCTOBER, 8, 11, 30).getTime(), "C", "TEST");
        Reading reading3 = new Reading(19, new GregorianCalendar(2018, Calendar.OCTOBER, 8, 21, 30).getTime(), "C", "TEST");
        Reading reading4 = new Reading(21, new GregorianCalendar(2018, Calendar.OCTOBER, 8, 3, 30).getTime(), "C", "TEST");
        readingService.addReading(reading1);
        readingService.addReading(reading4);
        readingService.addReading(reading2);
        readingService2.addReading(reading2);
        readingService2.addReading(reading3);
        readingService3.addReading(reading3);
        readingService3.addReading(reading4);
        double expectedResult1 = 20;
        double expectedResult2 = 19;
        double expectedResult3 = 19;
        //Act
        double actualResult = readingService.getMinValueInReadingList();
        double actualResult2 = readingService2.getMinValueInReadingList();
        double actualResult3 = readingService3.getMinValueInReadingList();
        //Assert
        assertNotSame(expectedResult1, actualResult);
        assertEquals(expectedResult2, actualResult2);
        assertEquals(expectedResult3, actualResult3);
    }

    @Test
    void seeIfWeGetReadingWithSpecificDateWorks() {
        //Arrange
        List<Reading> readingService = new ArrayList<>();
        Reading r1 = new Reading(22, validDate5, "C", "TEST");
        Reading r2 = new Reading(24, validDate14, "C", "TEST");
        Reading r3 = new Reading(22, validDate2, "C", "TEST");
        Reading r4 = new Reading(21, validDate15, "C", "TEST");
        Reading r5 = new Reading(22, validDate12, "C", "TEST");
        Reading r6 = new Reading(29, validDate2, "C", "TEST");
        readingService.add(r1);
        readingService.add(r2);
        readingService.add(r3);
        readingService.add(r4);
        readingService.add(r5);
        readingService.add(r6);
        //Act
        Reading actualResult2 = validReadingService.getAReadingWithSpecificDayDb(readingService, validDate7);
        Reading actualResult = validReadingService.getAReadingWithSpecificDayDb(readingService, validDate2);
        //Assert
        assertNull(actualResult2);
        assertEquals(r3, actualResult);
    }

    @Test
    void seeIfWeGetListOfMaxValuesForEachDayWorks() {
        //Arrange
        List<Reading> readingService = new ArrayList<>();
        Reading r1 = new Reading(22, validDate5, "C", "TEST");
        Reading r2 = new Reading(24, validDate14, "C", "TEST");
        Reading r3 = new Reading(22, validDate2, "C", "TEST");
        Reading r4 = new Reading(21, validDate15, "C", "TEST");
        Reading r5 = new Reading(22, validDate12, "C", "TEST");
        Reading r6 = new Reading(23, new GregorianCalendar(2018, Calendar.OCTOBER, 8, 21, 0).getTime(), "C", "TEST");
        Reading r7 = new Reading(26, new GregorianCalendar(2018, Calendar.OCTOBER, 2, 10, 0).getTime(), "C", "TEST");
        Reading r8 = new Reading(20, new GregorianCalendar(2018, Calendar.SEPTEMBER, 3, 23, 30).getTime(), "C", "TEST");
        Reading r10 = new Reading(20, validDate12, "C", "TEST");
        readingService.add(r1);
        readingService.add(r2);
        readingService.add(r3);
        readingService.add(r4);
        readingService.add(r5);
        readingService.add(r6);
        readingService.add(r7);
        readingService.add(r8);
        readingService.add(r10);
        List<Reading> expectedResult = new ArrayList<>();
        expectedResult.add(r6);
        expectedResult.add(r7);
        expectedResult.add(r3);
        expectedResult.add(r4);
        expectedResult.add(r10);
        //Act
        List<Reading> actualResult = validReadingService.getListOfMaxValuesForEachDayDb(readingService);
        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfGetValueReadingThrowsException() {
        //Arrange

        ReadingService emptyList = new ReadingService();

        //Act

        Throwable exception = assertThrows(IndexOutOfBoundsException.class, () -> emptyList.getValueReading(0));

        //Assert

        assertEquals("The reading list is empty.", exception.getMessage());
    }

    @Test
    void seeIfGetDateReadingThrowsException() {
        //Arrange

        ReadingService emptyList = new ReadingService();

        //Act

        Throwable exception = assertThrows(IndexOutOfBoundsException.class, () -> emptyList.getValueDate(0));

        //Assert

        assertEquals("The reading list is empty.", exception.getMessage());
    }

    @Test
    void seeIfContainsWorks() {
        //Arrange

        Reading validReading1 = new Reading(20.2, validDate1, "C", "TEST");
        Reading validReading2 = new Reading(20.2, validDate1, "C", "TEST");

        //Act

        boolean actualResult1 = validReadingService.contains(validReading1);

        //Arrange

        validReadingService.addReading(validReading1);

        //Act

        boolean actualResult2 = validReadingService.contains(validReading2);

        //Assert

        assertFalse(actualResult1);
        assertTrue(actualResult2);
    }

    @Test
    void seeIfAddReadingWorks() {
        //Arrange

        Reading validReading1 = new Reading(20.2, validDate1, "C", "TEST");
        Reading validReading2 = new Reading(20.2, validDate1, "C", "TEST");

        //Act

        boolean actualResult1 = validReadingService.addReading(validReading1);
        boolean actualResult2 = validReadingService.addReading(validReading2);

        //Assert

        assertTrue(actualResult1);
        assertFalse(actualResult2);
    }

    @Test
    void seeIfGetHottestDayInGivenPeriodWorksNoReadings() {
        // Arrange

        Reading outOfBoundsReading = new Reading(1, validDate1, "C", "SensorOne");
        validReadingService.addReading(outOfBoundsReading);

        // Assert

        assertThrows(IllegalArgumentException.class, () -> validReadingService.getFirstHottestDayInGivenPeriod(firstValidAreaSensor, validDate12, validDate2));
    }

    @Test
    void seeReadingExistsInRepositoryWorks() {
        // Arrange

        String sensorId = "TT12";
        Reading reading = new Reading(2D, validDate1, "C", sensorId);
        Mockito.when(readingRepository.findReadingByDateEqualsAndSensorId(validDate1, sensorId)).thenReturn((reading));

        // Act

        boolean actualResult = validReadingService.readingExistsInRepository(sensorId, validDate1);

        // Assert

        assertTrue(actualResult);
    }

    @Test
    void seeReadingExistsInRepositoryWorksWhenReadingIsNotPresent() {
        // Arrange

        String sensorId = "TT12";
        Mockito.when(readingRepository.findReadingByDateEqualsAndSensorId(validDate1, sensorId)).thenReturn((null));

        // Act

        boolean actualResult = validReadingService.readingExistsInRepository(sensorId, validDate1);

        // Assert

        assertFalse(actualResult);
    }

    @Test
    void seeIfAddAreaReadingToRepositoryWorksWhenSensorDoesNotExist() {
        // Arrange

        String sensorId = "TT12";
        Mockito.when(areaSensorRepository.findById(sensorId)).thenReturn((Optional.empty()));

        // Act

        boolean actualResult = validReadingService.addAreaReadingToRepository(sensorId, 20D, validDate1, "C", logger, areaSensorService);

        // Assert

        assertFalse(actualResult);
    }

    @Test
    void seeIfAddAreaReadingToRepositoryWorksWhenSensorWasNotActiveDuringRead() {
        // Arrange

        String sensorId = "TT12";
        Mockito.when(areaSensorRepository.findById(sensorId)).thenReturn(Optional.of(firstValidAreaSensor));

        // Act

        boolean actualResult = validReadingService.addAreaReadingToRepository(sensorId, 20D, validDate2, "C", logger, areaSensorService);

        // Assert

        assertFalse(actualResult);
    }

    @Test
    void seeIfAddAreaReadingToRepositoryWorksWhenReadingAlreadyExists() {
        // Arrange

        String sensorId = "TT12";
        Reading reading = new Reading(2D, validDate1, "C", sensorId);
        Mockito.when(areaSensorRepository.findById(sensorId)).thenReturn(Optional.of(firstValidAreaSensor));
        Mockito.when(readingRepository.findReadingByDateEqualsAndSensorId(validDate1, sensorId)).thenReturn((reading));

        // Act

        boolean actualResult = validReadingService.addAreaReadingToRepository(sensorId, 2D, validDate1, "C", logger, areaSensorService);

        // Assert

        assertFalse(actualResult);
    }

    @Test
    void seeIfAddAreaReadingToRepositoryWorks() {
        // Arrange

        String sensorId = "TT12";
        Mockito.when(areaSensorRepository.findById(sensorId)).thenReturn(Optional.of(firstValidAreaSensor));
        Mockito.when(readingRepository.findReadingByDateEqualsAndSensorId(validDate1, sensorId)).thenReturn((null));

        // Act

        boolean actualResult = validReadingService.addAreaReadingToRepository(sensorId, 2D, validDate1, "C", logger, areaSensorService);

        // Assert

        assertTrue(actualResult);
    }
}