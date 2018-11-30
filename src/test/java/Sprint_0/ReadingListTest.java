package Sprint_0;


import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadingListTest {

        @Test
        public void ensureThatWeAddAReading1ToAList() {
                //Arrange
                ReadingList readingList = new ReadingList();
                GregorianCalendar calendar = new GregorianCalendar(118, 11, 25);
                Reading reading1 = new Reading(17, calendar.getTime());
                GregorianCalendar calendar2 = new GregorianCalendar(118, 9, 3);
                Reading reading2 = new Reading(29, calendar.getTime());
                readingList.addReading(reading1);
                readingList.addReading(reading2);
                boolean expectedResult = true;

                //Act
                boolean result = readingList.getListOfReadings().contains(reading1);

                //Assert
                assertEquals(expectedResult, result);
        }

        @Test
        public void ensureThatWeAddAReading2ToAList() {
                //Arrange
                ReadingList readingList = new ReadingList();
                GregorianCalendar g1 = new GregorianCalendar(118, 11, 25);
                Reading reading1 = new Reading(15, g1.getTime());
                GregorianCalendar g2 = new GregorianCalendar(118, 9, 3);
                Reading reading2 = new Reading(2, g2.getTime());
                readingList.addReading(reading1);
                readingList.addReading(reading2);
                boolean expectedResult = true;

                //Act
                boolean result = readingList.getListOfReadings().contains(reading2);

                //Assert
                assertEquals(expectedResult, result);
        }

        @Test
        public void ensureThatWeGetAValueFromAReading1InsideAList() {
                //Arrange
                ReadingList readingList = new ReadingList();
                GregorianCalendar g1 = new GregorianCalendar(118, 11, 25);
                Reading reading1 = new Reading(15, g1.getTime());
                GregorianCalendar g2 = new GregorianCalendar(118, 9, 3);
                Reading reading2 = new Reading(29, g2.getTime());
                readingList.addReading(reading1);
                readingList.addReading(reading2);
                double expectedResult = 15;

                //Act
                double result = readingList.getListOfReadings().get(0).getmValue();

                //Assert
                assertEquals(expectedResult, result, 0.1);
        }

        @Test
        public void ensureThatWeGetAValueFromAReading2InsideAList() {
                //Arrange
                ReadingList readingList = new ReadingList();
                GregorianCalendar g1 = new GregorianCalendar(118, 11, 25);
                Reading reading1 = new Reading(15, g1.getTime());
                GregorianCalendar g2 = new GregorianCalendar(118, 9, 3);
                Reading reading2 = new Reading(29,g2.getTime());
                readingList.addReading(reading1);
                readingList.addReading(reading2);
                double expectedResult = 29;

                //Act
                double result = readingList.getListOfReadings().get(1).getmValue();

                //Assert
                assertEquals(expectedResult, result, 0.1);
        }

        @Test
        public void ensureThatAReadingListDoesNotContainAReading() {
                //Arrange
                ReadingList readingList = new ReadingList();
                GregorianCalendar g1 =  new GregorianCalendar(118, 11, 25);
                Reading reading1 = new Reading(15,g1.getTime());
                GregorianCalendar g2 =  new GregorianCalendar(118, 9, 3);
                Reading reading2 = new Reading(29,g2.getTime());
                readingList.addReading(reading1);
                boolean expectedResult = false;

                //Act
                boolean result = readingList.getListOfReadings().contains(reading2);

                //Assert
                assertEquals(expectedResult, result);
        }

        @Test
        public void ensureThatAReadingDoesNotAddBecauseItIsAlreadyContained() {
                //Arrange
                ReadingList readingList = new ReadingList();
                GregorianCalendar g1 =  new GregorianCalendar(118, 11, 25);
                Reading reading1 = new Reading(15,g1.getTime());
                GregorianCalendar g2 =  new GregorianCalendar(118, 9, 3);
                Reading reading2 = new Reading(29,g2.getTime());
                readingList.addReading(reading1);
                readingList.addReading(reading2);
                readingList.addReading(reading2);
                boolean expectedResult = true;

                //Act
                boolean result = readingList.getListOfReadings().contains(reading2);

                //Assert
                assertEquals(expectedResult, result);
        }

        @Test
        public void seeIfGetMostRecentReading() {

                //Arrange
                ReadingList readingList = new ReadingList();
                GregorianCalendar g1 =  new GregorianCalendar(118, 11, 25);
                Reading reading1 = new Reading(15,g1.getTime());
                GregorianCalendar g2 =new GregorianCalendar(118, 9, 3);
                Reading reading2 = new Reading(29, g2.getTime());
                readingList.addReading(reading1);
                readingList.addReading(reading2);
                Reading expectedResult = new Reading(15,g1.getTime());
                Reading actualResult;

                //Act
                actualResult = readingList.getMostRecentReading();

                //Assert
                assertEquals(expectedResult, actualResult);
        }

        @Test
        public void seeIfGetMostRecentReadingSwitchedDates() {

                //Arrange
                ReadingList readingList = new ReadingList();
                GregorianCalendar g1 = new GregorianCalendar(118, 11, 25);
                Reading reading2 = new Reading(16, g1.getTime());
                GregorianCalendar g2 = new GregorianCalendar(118, 9, 3);
                Reading reading1 = new Reading(29, g2.getTime());
                readingList.addReading(reading1);
                readingList.addReading(reading2);
                Reading expectedResult = new Reading(16, g1.getTime());
                Reading actualResult;

                //Act
                actualResult = readingList.getMostRecentReading();

                //Assert
                assertEquals(expectedResult, actualResult);
        }

        @Test
        public void seeIfMeanOfTheDay() {
                ReadingList rl = new ReadingList();
                GregorianCalendar g1 = new GregorianCalendar(2018, 11, 23, 21, 25, 8);
                GregorianCalendar g2 = new GregorianCalendar(2018, 11, 23, 23, 26, 21);
                GregorianCalendar g3 = new GregorianCalendar(2018, 11, 23, 8, 21, 22);
                GregorianCalendar g4 = new GregorianCalendar(2018, 11, 23, 18, 14, 3);
                GregorianCalendar g5 =new GregorianCalendar(2018, 11, 23, 12, 14, 23);
                GregorianCalendar g6 =new GregorianCalendar(2018, 11, 23, 12, 12, 12);
                GregorianCalendar g7 =new GregorianCalendar(2018, 11, 23, 13, 12, 33);
                Reading r1 = new Reading(23, g1.getTime());
                Reading r2 = new Reading(24,g2.getTime());
                Reading r3 = new Reading(25, g3.getTime());
                Reading r4 = new Reading(26, g4.getTime());
                Reading r5 = new Reading(23, g5.getTime());
                Reading r6 = new Reading(22, g6.getTime());
                Reading r7 = new Reading(23, g7.getTime());
                rl.addReading(r1);
                rl.addReading(r2);
                rl.addReading(r3);
                rl.addReading(r4);
                rl.addReading(r5);
                rl.addReading(r6);
                rl.addReading(r7);
                double expectedResult = 23.71;
                double result = rl.meanOftheDay(2018, 11, 23);
                assertEquals(expectedResult, result, 0.1);

        }

        @Test
        public void seeIfMeanOfTheDayDifferentDays() {
                ReadingList rl = new ReadingList();
                GregorianCalendar g1 = new GregorianCalendar(2018, 11, 23, 21, 25, 8);
                GregorianCalendar g2 = new GregorianCalendar(2018, 11, 23, 23, 26, 21);
                GregorianCalendar g3 = new GregorianCalendar(2018, 11, 23, 8, 21, 22);
                GregorianCalendar g4 = new GregorianCalendar(2018, 11, 23, 18, 14, 3);
                GregorianCalendar g5 =new GregorianCalendar(2018, 11, 23, 12, 14, 23);
                GregorianCalendar g6 =new GregorianCalendar(2018, 11, 23, 12, 12, 12);
                GregorianCalendar g7 =new GregorianCalendar(2018, 11, 23, 13, 12, 33);
                Reading r1 = new Reading(23, g1.getTime());
                Reading r2 = new Reading(24,g2.getTime());
                Reading r3 = new Reading(25, g3.getTime());
                Reading r4 = new Reading(26, g4.getTime());
                Reading r5 = new Reading(23, g5.getTime());
                Reading r6 = new Reading(22, g6.getTime());
                Reading r7 = new Reading(23, g7.getTime());
                rl.addReading(r1);
                rl.addReading(r2);
                rl.addReading(r3);
                rl.addReading(r4);
                rl.addReading(r5);
                rl.addReading(r6);
                rl.addReading(r7);
                double expectedResult = 23.8;
                double result = rl.meanOftheDay(2018, 11, 23);
                assertEquals(expectedResult, result, 0.1);
        }

        @Test
        public void seeIfGetDaysOfTheMonthWithReadings() {
                ReadingList rList = new ReadingList();
                GregorianCalendar g0 =new GregorianCalendar(2018, 9, 31, 23, 59, 59);
                GregorianCalendar g1 =new GregorianCalendar(2018, 10, 1, 0, 0, 0);
                GregorianCalendar g2 = new GregorianCalendar(2018, 10, 23, 23, 26, 21);
                GregorianCalendar g3 =new GregorianCalendar(2018, 10, 27, 8, 21, 22);
                GregorianCalendar g4 =new GregorianCalendar(2018, 10, 23, 18, 14, 3);
                GregorianCalendar g5 = new GregorianCalendar(2018, 10, 23, 12, 14, 23);
                GregorianCalendar g6 =new GregorianCalendar(2018, 10, 28, 12, 12, 12);
                GregorianCalendar g7 = new GregorianCalendar(2018, 10, 30, 23, 59, 59);
                GregorianCalendar g8 =new GregorianCalendar(2018, 11, 01, 00, 00, 00);
                Reading r0 = new Reading(23, g0.getTime());
                Reading r1 = new Reading(23, g1.getTime());
                Reading r2 = new Reading(24,g2.getTime());
                Reading r3 = new Reading(25, g3.getTime());
                Reading r4 = new Reading(26, g4.getTime());
                Reading r5 = new Reading(23,g5.getTime());
                Reading r6 = new Reading(22, g6.getTime());
                Reading r7 = new Reading(23,g7.getTime());
                Reading r8 = new Reading(22, g8.getTime());
                rList.addReading(r0);
                rList.addReading(r1);
                rList.addReading(r2);
                rList.addReading(r3);
                rList.addReading(r4);
                rList.addReading(r5);
                rList.addReading(r6);
                rList.addReading(r7);
                rList.addReading(r8);
                ArrayList<Integer> expectedResult = new ArrayList<>();
                expectedResult.add(1);
                expectedResult.add(23);
                expectedResult.add(27);
                expectedResult.add(28);
                expectedResult.add(30);
                ArrayList<Integer> result = rList.getDaysOfMonthWithReadings(2018, 10);
                assertEquals(expectedResult, result);
        }

        @Test
        public void seeGetMeanOfMonth() {
                ReadingList rList = new ReadingList();
                GregorianCalendar g0 =new GregorianCalendar(2018, 9, 31, 23, 59, 59);
                GregorianCalendar g1 =new GregorianCalendar(2018, 10, 1, 0, 0, 0);
                GregorianCalendar g2 = new GregorianCalendar(2018, 10, 23, 23, 26, 21);
                GregorianCalendar g3 =new GregorianCalendar(2018, 10, 27, 8, 21, 22);
                GregorianCalendar g4 =new GregorianCalendar(2018, 10, 23, 18, 14, 3);
                GregorianCalendar g5 = new GregorianCalendar(2018, 10, 23, 12, 14, 23);
                GregorianCalendar g6 =new GregorianCalendar(2018, 10, 28, 12, 12, 12);
                GregorianCalendar g7 = new GregorianCalendar(2018, 10, 30, 23, 59, 59);
                GregorianCalendar g8 =new GregorianCalendar(2018, 11, 01, 00, 00, 00);
                Reading r0 = new Reading(23, g0.getTime());
                Reading r1 = new Reading(23, g1.getTime());
                Reading r2 = new Reading(24,g2.getTime());
                Reading r3 = new Reading(25, g3.getTime());
                Reading r4 = new Reading(26, g4.getTime());
                Reading r5 = new Reading(23,g5.getTime());
                Reading r6 = new Reading(22, g6.getTime());
                Reading r7 = new Reading(23,g7.getTime());
                Reading r8 = new Reading(22, g8.getTime());
                rList.addReading(r0);
                rList.addReading(r1);
                rList.addReading(r2);
                rList.addReading(r3);
                rList.addReading(r4);
                rList.addReading(r5);
                rList.addReading(r6);
                rList.addReading(r7);
                rList.addReading(r8);

                double expectedResult = 23.5;
                double result = rList.meanOfMonth(2018, 10);
                assertEquals(expectedResult, result, 0.1);
        }
}