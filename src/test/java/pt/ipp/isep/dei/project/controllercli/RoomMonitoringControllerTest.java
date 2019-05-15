package pt.ipp.isep.dei.project.controllercli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ipp.isep.dei.project.dto.RoomDTO;
import pt.ipp.isep.dei.project.dto.mappers.RoomMapper;
import pt.ipp.isep.dei.project.model.Local;
import pt.ipp.isep.dei.project.model.Reading;
import pt.ipp.isep.dei.project.model.bridgeservices.GeographicAreaHouseService;
import pt.ipp.isep.dei.project.model.geographicarea.AreaSensor;
import pt.ipp.isep.dei.project.model.geographicarea.GeographicArea;
import pt.ipp.isep.dei.project.model.geographicarea.GeographicAreaRepository;
import pt.ipp.isep.dei.project.model.house.Address;
import pt.ipp.isep.dei.project.model.house.House;
import pt.ipp.isep.dei.project.model.room.Room;
import pt.ipp.isep.dei.project.model.room.RoomRepository;
import pt.ipp.isep.dei.project.model.room.RoomSensor;
import pt.ipp.isep.dei.project.repository.AreaTypeCrudeRepo;
import pt.ipp.isep.dei.project.repository.GeographicAreaCrudeRepo;
import pt.ipp.isep.dei.project.repository.RoomCrudeRepo;
import pt.ipp.isep.dei.project.repository.SensorTypeCrudeRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * RoomMonitoringController tests class.
 */
@ExtendWith(MockitoExtension.class)
class RoomMonitoringControllerTest {

    // Common artifacts for testing in this class.

    private House validHouse;
    private List<String> deviceTypeString;
    private RoomMonitoringController controller = new RoomMonitoringController();
    private SimpleDateFormat validSdf; // SimpleDateFormat dd/MM/yyyy HH:mm:ss
    private SimpleDateFormat validSdf2;
    private RoomRepository roomRepository;
    private GeographicAreaRepository geographicAreaRepository;
    private GeographicAreaHouseService geographicAreaHouseService;
    private Room validRoom1;
    private RoomDTO validRoomDTO;
    private List<Room> rooms;
    private GeographicArea validArea;
    private AreaSensor validAreaSensor;
    private Date validDate1;
    private Date validDate2;
    private Date validDate3;
    private Date validDateSensor;
    private Date validStartDate;
    private Date validEndingDate;
    private Date roomReadingDate1;
    private Date roomReadingDate2;
    private Date roomReadingDate3;
    private Date areaReadingDate1;
    private Date areaReadingDate2;
    private Date areaReadingDate3;
    private RoomSensor firstValidRoomSensor;
    private RoomRepository validRoomRepository;

    @Mock
    SensorTypeCrudeRepo sensorTypeCrudeRepo;

    @Mock
    RoomCrudeRepo roomCrudeRepo;

    @Mock
    GeographicAreaCrudeRepo geographicAreaCrudeRepo;

    @Mock
    AreaTypeCrudeRepo areaTypeCrudeRepo;

    @BeforeEach
    void arrangeArtifacts() {
        validSdf = new SimpleDateFormat("dd/MM/yyyy");
        validSdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        validSdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        validSdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat readingSD = new SimpleDateFormat("yyyy-MM-dd");
        readingSD.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat readingSD2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'");
        readingSD2.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            validDate1 = validSdf.parse("01/02/2018");
            validDate2 = validSdf.parse("10/02/2018");
            validDate3 = validSdf.parse("20/02/2018");
            roomReadingDate1 = validSdf.parse("01/12/2018");
            roomReadingDate2 = validSdf.parse("10/12/2018");
            roomReadingDate3 = validSdf.parse("20/12/2018");
            areaReadingDate1 = readingSD2.parse("2018-12-01T04:00:00+00:00");
            areaReadingDate2 = readingSD2.parse("2018-12-10T04:00:00+00:00");
            areaReadingDate3 = readingSD2.parse("2018-12-20T04:00:00+00:00");
            validDateSensor = validSdf2.parse("21/11/2018 00:00:00");
            validStartDate = readingSD.parse("2017-10-03");
            validEndingDate = readingSD.parse("2019-10-03");
        } catch (ParseException c) {
            c.printStackTrace();
        }
        validAreaSensor = new AreaSensor("sensorID", "SensOne", "temperature", new Local(10, 10, 10), validDate1);
        validAreaSensor.setActive(true);
        Reading areaReading1 = new Reading(20, validDate1, "C", "sensorID");
        Reading areaReading2 = new Reading(20, validDate2, "C", "sensorID");
        Reading areaReading3 = new Reading(20, validDate3, "C", "sensorID");
        Reading areaReading4 = new Reading(0, areaReadingDate1, "C", "sensorID");
        Reading areaReading5 = new Reading(500, areaReadingDate2, "C", "sensorID");
        Reading areaReading6 = new Reading(0, areaReadingDate3, "C", "sensorID");
        validAreaSensor.addReading(areaReading1);
        validAreaSensor.addReading(areaReading2);
        validAreaSensor.addReading(areaReading3);
        validAreaSensor.addReading(areaReading4);
        validAreaSensor.addReading(areaReading5);
        validAreaSensor.addReading(areaReading6);

        validArea = new GeographicArea("Europe", "Continent", 3500, 3000,
                new Local(20, 12, 33));
        validArea.setId(111L);
        validArea.addSensor(validAreaSensor);
        deviceTypeString = new ArrayList<>();
        this.validHouse = new House("ISEP", new Address("Rua Dr. António Bernardino de Almeida", "431",
                "4455-125", "Porto", "Portugal"),
                new Local(20, 20, 20), 60,
                180, deviceTypeString);
        this.validHouse.setMotherAreaID(validArea.getId());
        this.roomRepository = new RoomRepository(roomCrudeRepo);
        this.geographicAreaRepository = new GeographicAreaRepository(geographicAreaCrudeRepo, areaTypeCrudeRepo);
        this.geographicAreaHouseService = new GeographicAreaHouseService(geographicAreaCrudeRepo, areaTypeCrudeRepo, sensorTypeCrudeRepo);
        validRoom1 = new Room("Bedroom", "Double Bedroom", 2, 15, 15, 10, "Room1");
        validRoomRepository = new RoomRepository(roomCrudeRepo);
        this.rooms = new ArrayList<>();
        rooms.add(validRoom1);
        roomRepository = new RoomRepository(roomCrudeRepo);
        validRoomDTO = RoomMapper.objectToDTO(validRoom1);
        validRoomDTO.setHouseId(validHouse.getId());
        firstValidRoomSensor = new RoomSensor("T32875", "SensorOne", "temperature", validDate1);
        firstValidRoomSensor.setActive(true);
    }

    @Test
    void seeIfGetDayMaxTemperatureWorks() {
        // Arrange

        Date validDate1 = new Date();
        Date validDate2 = new Date();
        Date validDate3 = new Date();

        SimpleDateFormat validSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'", Locale.ENGLISH);
        try {
            validDate1 = validSdf.parse("2018-07-03T10:50:00+00:00");
            validDate2 = validSdf.parse("2018-07-03T13:15:00+00:00");
            validDate3 = validSdf.parse("2018-07-03T05:35:00+00:00");

        } catch (ParseException c) {
            c.printStackTrace();
        }

        Room testRoom = new Room("Kitchen", "Where we cook", 0, 15, 15, 15,
                "ISEP");
        RoomSensor testSensor = new RoomSensor("S001", "TempOne", "temperature", validDate1);
        Reading testReading = new Reading(11, validDate1,
                "C", "S001");
        Reading secondTestReading = new Reading(17, validDate2,
                "C", "S001");
        Reading thirdTestReading = new Reading(11, validDate3,
                "C", "S001");
        List<Room> mockRepositoryRooms = new ArrayList<>();
        mockRepositoryRooms.add(testRoom);
        List<Reading> testReadingList = new ArrayList<>();
        testReadingList.add(testReading);
        testReadingList.add(secondTestReading);
        testReadingList.add(thirdTestReading);
        testSensor.setReadings(testReadingList);
        testRoom.addSensor(testSensor);
        double expectedResult = 17.0;

        RoomDTO testDTO = RoomMapper.objectToDTO(testRoom);
        Mockito.when(roomRepository.getAllRooms()).thenReturn(mockRepositoryRooms);

        // Act

        double actualResult = controller.getDayMaxTemperature(testDTO, validDate1, roomRepository);

        // Assert

        assertEquals(expectedResult, actualResult, 0.01);

    }


    @Test
    void seeIfGetCurrentRoomTemperatureThrowsException() {
        // Arrange

        List<Room> mockList = new ArrayList<>();
        mockList.add(validRoom1);

        // Act


        Mockito.when(roomCrudeRepo.findAll()).thenReturn(mockList);
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> controller.getCurrentRoomTemperature(validRoomDTO, roomRepository));

        // Assert

        assertEquals("There aren't any temperature readings available.", exception.getMessage());

    }

    @Test
    void seeIfGetCurrentRoomTemperatureWorks() {
        //Arrange

        List<Room> mockList = new ArrayList<>();
        mockList.add(validRoom1);
        RoomSensor roomSensor = new RoomSensor("S1", "Room Temperature Sensor", "temperature", new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime());
        validRoom1.addSensor(roomSensor);
        Reading reading = new Reading(21, Calendar.getInstance().getTime(), "C", roomSensor.getId());
        List<Reading> list = new ArrayList<>();
        list.add(reading);
        roomSensor.setReadings(list);
        Mockito.when(roomCrudeRepo.findAll()).thenReturn(mockList);

        //Act

        double actualResult = controller.getCurrentRoomTemperature(RoomMapper.objectToDTO(validRoom1), roomRepository);

        //Assert

        assertEquals(21.0, actualResult, 0.1);
    }


    @Test
    void getRoomName() {
        // Arrange

        String expectedResult = "Bedroom";
        List<Room> rooms = new ArrayList<>();
        rooms.add(validRoom1);

        // Act

        Mockito.when(roomCrudeRepo.findAll()).thenReturn(rooms);
        String actualResult = controller.getRoomName(validRoomDTO, roomRepository);

        // Assert

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void seeIfBuildReadingsOutputWorks() {
        // Arrange

        List<Reading> list = new ArrayList<>();

        Reading reading1 = new Reading(11, validDate1,
                "C", "S001");
        Reading reading2 = new Reading(17, validDate2,
                "C", "S001");
        Reading reading3 = new Reading(11, validDate3,
                "C", "S001");


        list.add(reading1);
        list.add(reading2);
        list.add(reading3);

        String expectedResult = "Instants in which the readings are above comfort temperature:\n" +
                "0) Instant: 2018-02-01 12.00.00   Temperature value: 11.0\n" +
                "1) Instant: 2018-02-10 12.00.00   Temperature value: 17.0\n" +
                "2) Instant: 2018-02-20 12.00.00   Temperature value: 11.0\n" +
                "--------------------------------------\n";

        // Act

        String actualResult = controller.buildReadingsOutput(list, "Instants in which the readings are above comfort temperature:\n");

        // Assert

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void seeIfBuildReadingsOutputEmptyListWorks() {
        // Arrange

        List<Reading> list = new ArrayList<>();

        String expectedResult = "Instants in which the readings are above comfort temperature:\n" +
                "--------------------------------------\n";

        // Act

        String actualResult = controller.buildReadingsOutput(list, "Instants in which the readings are above comfort temperature:\n");

        // Assert

        assertEquals(expectedResult, actualResult);

    }


    @Test
    void seeIfGetInstantsAboveComfortIntervalCategoryI_II_IIIWithoutOutOfIntervalReadingsWorks() {
        // Arrange

        String expectedResult = "For the given category, in the given interval, there were no temperature readings above the max comfort temperature.";

        int category1 = 0;
        int category2 = 1;
        int category3 = 2;

        Reading reading1 = new Reading(15, validDate1, "C", "Test");
        Reading reading2 = new Reading(20, validDate2, "C", "Test1");
        Reading reading3 = new Reading(17, validDate3, "C", "Test");

        List<Reading> readings = new ArrayList<>();

        readings.add(reading1);
        readings.add(reading2);
        readings.add(reading3);

        firstValidRoomSensor.setReadings(readings);

        List<RoomSensor> roomSensors = new ArrayList<>();
        roomSensors.add(firstValidRoomSensor);
        validRoom1.setRoomSensors(roomSensors);

        reading1.setSensorID(firstValidRoomSensor.getId());
        reading2.setSensorID(firstValidRoomSensor.getId());
        reading3.setSensorID(firstValidRoomSensor.getId());

        RoomDTO roomDTO = RoomMapper.objectToDTO(validRoom1);

        // Act
        Mockito.when(geographicAreaCrudeRepo.findById(validArea.getId())).thenReturn(Optional.of(validArea));
        String actualResult1 = controller.getInstantsAboveComfortInterval(validHouse, category1, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);
        String actualResult2 = controller.getInstantsAboveComfortInterval(validHouse, category2, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);
        String actualResult3 = controller.getInstantsAboveComfortInterval(validHouse, category3, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);

        // Assert

        assertEquals(expectedResult, actualResult1);
        assertEquals(expectedResult, actualResult2);
        assertEquals(expectedResult, actualResult3);

    }

    @Test
    void seeIfGetInstantsAboveComfortIntervalCategoryIWorks() {
        // Arrange

        String expectedResult = "Instants in which the readings are above comfort temperature:\n" +
                "0) Instant: 2018-02-01 12.00.00   Temperature value: 31.0\n" +
                "1) Instant: 2018-02-10 12.00.00   Temperature value: 80.0\n" +
                "2) Instant: 2018-02-20 12.00.00   Temperature value: 31.0\n" +
                "--------------------------------------\n";

        int category = 0;

        Reading reading1 = new Reading(31, validDate1, "C", "Test");
        Reading reading2 = new Reading(80, validDate2, "C", "Test1");
        Reading reading3 = new Reading(31, validDate3, "C", "Test");

        List<Reading> readings = new ArrayList<>();

        readings.add(reading1);
        readings.add(reading2);
        readings.add(reading3);

        firstValidRoomSensor.setReadings(readings);
        List<RoomSensor> roomSensors = new ArrayList<>();
        roomSensors.add(firstValidRoomSensor);
        validRoom1.setRoomSensors(roomSensors);

        reading1.setSensorID(firstValidRoomSensor.getId());
        reading2.setSensorID(firstValidRoomSensor.getId());
        reading3.setSensorID(firstValidRoomSensor.getId());

        RoomDTO roomDTO = RoomMapper.objectToDTO(validRoom1);

        // Act
        Mockito.when(geographicAreaCrudeRepo.findById(validArea.getId())).thenReturn(Optional.of(validArea));
        String actualResult = controller.getInstantsAboveComfortInterval(validHouse, category, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);

        // Assert

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void seeIfGetInstantsAboveComfortIntervalCategoryIIWorks() {
        // Arrange

        String expectedResult = "Instants in which the readings are above comfort temperature:\n" +
                "0) Instant: 2018-02-10 12.00.00   Temperature value: 80.0\n" +
                "1) Instant: 2018-02-20 12.00.00   Temperature value: 30.0\n" +
                "--------------------------------------\n";

        int category = 1;

        Reading reading1 = new Reading(15, validDate1, "C", "Test");
        Reading reading2 = new Reading(80, validDate2, "C", "Test1");
        Reading reading3 = new Reading(30, validDate3, "C", "Test");

        List<Reading> readings = new ArrayList<>();

        readings.add(reading1);
        readings.add(reading2);
        readings.add(reading3);

        firstValidRoomSensor.setReadings(readings);

        List<RoomSensor> roomSensors = new ArrayList<>();
        roomSensors.add(firstValidRoomSensor);
        validRoom1.setRoomSensors(roomSensors);

        reading1.setSensorID(firstValidRoomSensor.getId());
        reading2.setSensorID(firstValidRoomSensor.getId());
        reading3.setSensorID(firstValidRoomSensor.getId());

        RoomDTO roomDTO = RoomMapper.objectToDTO(validRoom1);

        // Act
        Mockito.when(geographicAreaCrudeRepo.findById(validArea.getId())).thenReturn(Optional.of(validArea));
        String actualResult = controller.getInstantsAboveComfortInterval(validHouse, category, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);

        // Assert

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void seeIfGetInstantsAboveComfortIntervalCategoryIIIWorks() {
        // Arrange

        String expectedResult = "Instants in which the readings are above comfort temperature:\n" +
                "0) Instant: 2018-02-10 12.00.00   Temperature value: 80.0\n" +
                "--------------------------------------\n";

        int category = 2;

        Reading reading1 = new Reading(15, validDate1, "C", "Test");
        Reading reading2 = new Reading(80, validDate2, "C", "Test1");
        Reading reading3 = new Reading(28, validDate3, "C", "Test");

        List<Reading> readings = new ArrayList<>();

        readings.add(reading1);
        readings.add(reading2);
        readings.add(reading3);

        firstValidRoomSensor.setReadings(readings);

        List<RoomSensor> roomSensors = new ArrayList<>();
        roomSensors.add(firstValidRoomSensor);
        validRoom1.setRoomSensors(roomSensors);

        reading1.setSensorID(firstValidRoomSensor.getId());
        reading2.setSensorID(firstValidRoomSensor.getId());
        reading3.setSensorID(firstValidRoomSensor.getId());

        RoomDTO roomDTO = RoomMapper.objectToDTO(validRoom1);

        // Act
        Mockito.when(geographicAreaCrudeRepo.findById(validArea.getId())).thenReturn(Optional.of(validArea));
        String actualResult = controller.getInstantsAboveComfortInterval(validHouse, category, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);

        // Assert

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void seeIfGetInstantsBelowComfortIntervalCategoryIWorksNoReadings() {
        // Arrange

        String expectedResult = "For the given category, in the given interval, there were no temperature readings below the min comfort temperature.";

        int category = 0;

        List<Reading> readings = new ArrayList<>();
        firstValidRoomSensor.setReadings(readings);

        List<RoomSensor> roomSensors = new ArrayList<>();
        roomSensors.add(firstValidRoomSensor);
        validRoom1.setRoomSensors(roomSensors);

        RoomDTO roomDTO = RoomMapper.objectToDTO(validRoom1);

        // Act

        String actualResult = controller.getInstantsBelowComfortInterval(validHouse, category, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);

        // Assert

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void seeIfGetInstantsBelowComfortIntervalCategoryIIWorks() {
        // Arrange

        String expectedResult = "Instants in which the readings are below the comfort temperature:\n" +
                "0) Instant: 2018-02-01 12.00.00   Temperature value: 0.0\n" +
                "1) Instant: 2018-02-10 12.00.00   Temperature value: 0.0\n" +
                "2) Instant: 2018-02-20 12.00.00   Temperature value: 0.0\n" +
                "--------------------------------------\n";

        int category = 1;

        Reading reading1 = new Reading(0, validDate1, "C", "Test");
        Reading reading2 = new Reading(0, validDate2, "C", "Test1");
        Reading reading3 = new Reading(0, validDate3, "C", "Test");

        List<Reading> readings = new ArrayList<>();

        readings.add(reading1);
        readings.add(reading2);
        readings.add(reading3);

        firstValidRoomSensor.setReadings(readings);

        List<RoomSensor> roomSensors = new ArrayList<>();
        roomSensors.add(firstValidRoomSensor);
        validRoom1.setRoomSensors(roomSensors);

        reading1.setSensorID(firstValidRoomSensor.getId());
        reading2.setSensorID(firstValidRoomSensor.getId());
        reading3.setSensorID(firstValidRoomSensor.getId());

        RoomDTO roomDTO = RoomMapper.objectToDTO(validRoom1);

        // Act
        Mockito.when(geographicAreaCrudeRepo.findById(validArea.getId())).thenReturn(Optional.of(validArea));
        String actualResult = controller.getInstantsBelowComfortInterval(validHouse, category, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);

        // Assert

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void seeIfGetInstantsBelowComfortIntervalCategoryIIIWorks() {
        // Arrange

        String expectedResult = "Instants in which the readings are below the comfort temperature:\n" +
                "0) Instant: 2018-02-01 12.00.00   Temperature value: 0.0\n" +
                "1) Instant: 2018-02-10 12.00.00   Temperature value: 0.0\n" +
                "2) Instant: 2018-02-20 12.00.00   Temperature value: 0.0\n" +
                "--------------------------------------\n";

        int category = 2;

        Reading reading1 = new Reading(0, validDate1, "C", "Test");
        Reading reading2 = new Reading(0, validDate2, "C", "Test1");
        Reading reading3 = new Reading(0, validDate3, "C", "Test");

        List<Reading> readings = new ArrayList<>();

        readings.add(reading1);
        readings.add(reading2);
        readings.add(reading3);

        firstValidRoomSensor.setReadings(readings);

        List<RoomSensor> roomSensors = new ArrayList<>();
        roomSensors.add(firstValidRoomSensor);
        validRoom1.setRoomSensors(roomSensors);

        reading1.setSensorID(firstValidRoomSensor.getId());
        reading2.setSensorID(firstValidRoomSensor.getId());
        reading3.setSensorID(firstValidRoomSensor.getId());

        RoomDTO roomDTO = RoomMapper.objectToDTO(validRoom1);

        // Act
        Mockito.when(geographicAreaCrudeRepo.findById(validArea.getId())).thenReturn(Optional.of(validArea));
        String actualResult = controller.getInstantsBelowComfortInterval(validHouse, category, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);

        // Assert

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void seeIfGetInstantsBelowComfortIntervalCategoryIWorks() {
        // Arrange

        String expectedResult = "Instants in which the readings are below the comfort temperature:\n" +
                "0) Instant: 2018-12-01 12.00.00   Temperature value: 0.0\n" +
                "1) Instant: 2018-12-10 12.00.00   Temperature value: 0.0\n" +
                "2) Instant: 2018-12-20 12.00.00   Temperature value: 0.0\n" +
                "--------------------------------------\n";

        int category = 0;

        Reading reading1 = new Reading(0, roomReadingDate1, "C", "T32875");
        Reading reading2 = new Reading(0, roomReadingDate2, "C", "T32875");
        Reading reading3 = new Reading(0, roomReadingDate3, "C", "T32875");

        List<Reading> readings = new ArrayList<>();
        readings.add(reading1);
        readings.add(reading2);
        readings.add(reading3);

        firstValidRoomSensor.setReadings(readings);

        List<RoomSensor> roomSensors = new ArrayList<>();
        roomSensors.add(firstValidRoomSensor);
        validRoom1.setRoomSensors(roomSensors);

        RoomDTO roomDTO = RoomMapper.objectToDTO(validRoom1);

        // Act
        Mockito.when(geographicAreaCrudeRepo.findById(validArea.getId())).thenReturn(Optional.of(validArea));
        String actualResult = controller.getInstantsBelowComfortInterval(validHouse, category, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);

        // Assert

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void seeIfGetInstantsBelowComfortIntervalCategoryIIWorksNoReadings() {
        // Arrange

        String expectedResult = "For the given category, in the given interval, there were no temperature readings below the min comfort temperature.";

        int category = 1;

        List<Reading> readings = new ArrayList<>();
        firstValidRoomSensor.setReadings(readings);

        List<RoomSensor> roomSensors = new ArrayList<>();
        roomSensors.add(firstValidRoomSensor);
        validRoom1.setRoomSensors(roomSensors);

        RoomDTO roomDTO = RoomMapper.objectToDTO(validRoom1);

        // Act

        String actualResult = controller.getInstantsBelowComfortInterval(validHouse, category, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);

        // Assert

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void seeIfGetInstantsBelowComfortIntervalCategoryIIIWorksNoReadings() {
        // Arrange

        String expectedResult = "For the given category, in the given interval, there were no temperature readings below the min comfort temperature.";

        int category = 2;

        List<Reading> readings = new ArrayList<>();
        firstValidRoomSensor.setReadings(readings);

        List<RoomSensor> roomSensors = new ArrayList<>();
        roomSensors.add(firstValidRoomSensor);
        validRoom1.setRoomSensors(roomSensors);

        RoomDTO roomDTO = RoomMapper.objectToDTO(validRoom1);

        // Act

        String actualResult = controller.getInstantsBelowComfortInterval(validHouse, category, roomDTO, validStartDate, validEndingDate, roomRepository, geographicAreaHouseService);

        // Assert

        assertEquals(expectedResult, actualResult);

    }
}