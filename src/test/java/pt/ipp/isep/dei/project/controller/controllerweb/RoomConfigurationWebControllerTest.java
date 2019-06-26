package pt.ipp.isep.dei.project.controller.controllerweb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.ipp.isep.dei.project.dto.*;
import pt.ipp.isep.dei.project.dto.mappers.*;
import pt.ipp.isep.dei.project.model.bridgeservices.HouseRoomService;
import pt.ipp.isep.dei.project.model.room.Room;
import pt.ipp.isep.dei.project.model.room.RoomRepository;
import pt.ipp.isep.dei.project.model.room.RoomSensor;
import pt.ipp.isep.dei.project.model.sensortype.SensorType;
import pt.ipp.isep.dei.project.model.sensortype.SensorTypeRepository;
import pt.ipp.isep.dei.project.model.user.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@ContextConfiguration(classes = HibernateJpaAutoConfiguration.class)
class RoomConfigurationWebControllerTest {


    @Mock
    HouseRoomService houseRoomService;

    @Mock
    RoomRepository roomRepository;

    @Mock
    UserService userService;

    @Mock
    SensorTypeRepository sensorTypeRepository;

    @InjectMocks
    RoomsWebController roomConfigurationWebController;

    @Autowired
    private MockMvc mockMvc;

    private RoomDTO roomDTO;
    private RoomDTOMinimal roomDTOMinimal;
    private RoomSensor validRoomSensor;
    private Room validRoom;
    private List<SensorTypeDTO> validTypeList;
    private SensorType validSensorType;
    private List<RoomDTOMinimal> validRoomMinimalDTOlist;
    private AddressLocalGeographicAreaIdDTO addressAndLocalDTO;
    private Date date1;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        SimpleDateFormat validSdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        date1 = new Date();

        try {
            date1 = validSdf.parse("11/01/2018 10:00:00");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        roomDTO = new RoomDTO();
        roomDTO.setName("Name");
        roomDTO.setWidth(2D);
        roomDTO.setLength(4D);
        roomDTO.setHeight(1D);
        roomDTO.setFloor(1);

        roomDTOMinimal = new RoomDTOMinimal();
        roomDTOMinimal.setName("Name2");
        roomDTOMinimal.setWidth(2D);
        roomDTOMinimal.setLength(4D);
        roomDTOMinimal.setHeight(1D);
        roomDTOMinimal.setFloor(1);

        addressAndLocalDTO = new AddressLocalGeographicAreaIdDTO();

        addressAndLocalDTO.setNumber("431");
        addressAndLocalDTO.setCountry("Portugal");
        addressAndLocalDTO.setZip("4200-072");
        addressAndLocalDTO.setTown("Porto");
        addressAndLocalDTO.setStreet("rua carlos peixoto");

        addressAndLocalDTO.setAltitude(20);
        addressAndLocalDTO.setLongitude(20);
        addressAndLocalDTO.setLatitude(20);

        addressAndLocalDTO.setGeographicAreaId(2L);

        this.mockMvc = MockMvcBuilders.standaloneSetup(roomConfigurationWebController).build();

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(roomConfigurationWebController).build();
        validRoomSensor = new RoomSensor("RF12345", "Meteo station ISEP - rainfall", "rainfall", new Date());
        validRoom = new Room("Bedroom", "Cosy", 3, 2, 1, 5, "7");
        validTypeList = new ArrayList<>();
        validSensorType = new SensorType("temperature", "C");
        validTypeList.add(SensorTypeMapper.objectToDTO(validSensorType));
        validRoomMinimalDTOlist = new ArrayList<>();
        validRoomMinimalDTOlist.add(RoomMinimalMapper.objectToDtoWeb(validRoom));
    }

    @Test
    void seeIfDeleteRoomWorks() {

        // Arrange

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(roomDTO, HttpStatus.OK);

        Mockito.when(roomRepository.getRoomDTOByName("Name")).thenReturn(roomDTO);
        Mockito.when(roomRepository.deleteRoom(roomDTO)).thenReturn(true);

        // Act

        ResponseEntity<Object> actualResult = roomConfigurationWebController.deleteRoom("Name");

        //Assert

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfGetSensorsWorks() {

        // Arrange

        RoomSensor roomSensor1 = new RoomSensor("RoomSensorId1", "RoomSensor1", "SensorType", date1);
        RoomSensor roomSensor2 = new RoomSensor("RoomSensorId2", "RoomSensor2", "SensorType", date1);
        RoomSensorDTO roomSensorDTO1 = RoomSensorMapper.objectToDTO(roomSensor1);
        RoomSensorDTO roomSensorDTO2 = RoomSensorMapper.objectToDTO(roomSensor2);

        RoomSensorDTOMinimal roomSensorDTOMinimal1 = RoomSensorMinimalMapper.objectToDTO(roomSensor1);
        RoomSensorDTOMinimal roomSensorDTOMinimal2 = RoomSensorMinimalMapper.objectToDTO(roomSensor2);
        List<RoomSensorDTOMinimal> roomSensorDTOMinimals = new ArrayList<>();
        roomSensorDTOMinimals.add(roomSensorDTOMinimal1);
        roomSensorDTOMinimals.add(roomSensorDTOMinimal2);

        ResponseEntity<List<RoomSensorDTOMinimal>> expectedResult = new ResponseEntity<>(roomSensorDTOMinimals, HttpStatus.OK);

        roomDTO.addSensor(roomSensorDTO1);
        roomDTO.addSensor(roomSensorDTO2);

        Mockito.when(roomRepository.getRoomDTOByName("Name")).thenReturn(roomDTO);
        Mockito.when(userService.getUsernameFromToken()).thenReturn("admin");

        // Act

        ResponseEntity<List<RoomSensorDTOMinimal>> actualResult = roomConfigurationWebController.getSensors("Name");

        //Assert

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfGetSensorsWorksWhenUserNameIsNotAdmin() {

        // Arrange

        RoomSensor roomSensor1 = new RoomSensor("RoomSensorId1", "RoomSensor1", "SensorType", date1);
        RoomSensor roomSensor2 = new RoomSensor("RoomSensorId2", "RoomSensor2", "SensorType", date1);
        RoomSensorDTO roomSensorDTO1 = RoomSensorMapper.objectToDTO(roomSensor1);
        RoomSensorDTO roomSensorDTO2 = RoomSensorMapper.objectToDTO(roomSensor2);

        RoomSensorDTOMinimal roomSensorDTOMinimal1 = RoomSensorMinimalMapper.objectToDTO(roomSensor1);
        RoomSensorDTOMinimal roomSensorDTOMinimal2 = RoomSensorMinimalMapper.objectToDTO(roomSensor2);
        List<RoomSensorDTOMinimal> roomSensorDTOMinimals = new ArrayList<>();
        roomSensorDTOMinimals.add(roomSensorDTOMinimal1);
        roomSensorDTOMinimals.add(roomSensorDTOMinimal2);

        ResponseEntity<List<RoomSensorDTOMinimal>> expectedResult = new ResponseEntity<>(roomSensorDTOMinimals, HttpStatus.OK);

        roomDTO.addSensor(roomSensorDTO1);
        roomDTO.addSensor(roomSensorDTO2);

        Mockito.when(roomRepository.getRoomDTOByName("Name")).thenReturn(roomDTO);
        Mockito.when(userService.getUsernameFromToken()).thenReturn("otherUser");

        // Act

        ResponseEntity<List<RoomSensorDTOMinimal>> actualResult = roomConfigurationWebController.getSensors("Name");

        //Assert

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfRemoveRoomSensorWorksWhenSensorIsNotFound() {

        // Arrange

        Mockito.when(roomRepository.removeSensorDTO(roomDTO, "SensorId")).thenReturn(false);

        ResponseEntity<String> expectedResult = new ResponseEntity<>("Sensor doesn't exist or wasn't found.", HttpStatus.NOT_FOUND);

        // Act

        ResponseEntity<String> actualResult = roomConfigurationWebController.removeRoomSensor("Name", "SensorId");

        //Assert

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfRetrieveSensorTypesWorks() throws Exception {

        // Arrange

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);

        // Perform

        this.mockMvc.perform(get("/rooms/types"))
                .andExpect(status().isOk());
    }

    @Test
    void seeIfRetrieveSensorTypesWorksEmptyList() throws Exception {

        // Arrange

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(new ArrayList<>());

        // Perform

        this.mockMvc.perform(get("/rooms/types"))
                .andExpect(status().isOk());
    }

    @Test
    void seeIfRetrieveSensorTypesWorksNullList() throws Exception {

        // Arrange

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(null);

        // Perform

        this.mockMvc.perform(get("/rooms/types"))
                .andExpect(status().isOk());
    }

    @Test
    void seeIfRetrieveAllRoomsWorks() throws Exception {

        // Arrange

        Mockito.when(roomRepository.getAllRoomsAsMinimalDTOs()).thenReturn(validRoomMinimalDTOlist);

        // Perform

        this.mockMvc.perform(get("/rooms/"))
                .andExpect(status().isOk());
    }

    @Test
    void seeIfRetrieveAllRoomsWorksEmptyList() throws Exception {

        // Arrange

        Mockito.when(roomRepository.getAllRoomsAsMinimalDTOs()).thenReturn(new ArrayList<>());

        // Perform

        this.mockMvc.perform(get("/rooms/"))
                .andExpect(status().isOk());
    }

    @Test
    void seeIfRetrieveAllRoomsWorksNullList() throws Exception {

        // Arrange

        Mockito.when(roomRepository.getAllRoomsAsMinimalDTOs()).thenReturn(null);

        // Perform

        this.mockMvc.perform(get("/rooms/"))
                .andExpect(status().isOk());
    }

    @Test
    void seeIfRetrieveAllRoomSensorsWorks() throws Exception {

        // Arrange

        Mockito.when(roomRepository.getRoomDTOByName(validRoom.getId())).thenReturn(RoomMapper.objectToDTO(validRoom));

        // Perform

        this.mockMvc.perform(get("/rooms/Bedroom/sensors"))
                .andExpect(status().isOk());
    }

    @Test
    void seeIfRetrieveAllRoomSensorsWorksEmptyRoom() throws Exception {

        // Arrange

        Mockito.when(roomRepository.getRoomDTOByName(validRoom.getId())).thenReturn(RoomMapper.objectToDTO(new Room("test", "test", 0, 0, 0, 0, "test")));

        // Perform

        this.mockMvc.perform(get("/rooms/Bedroom/sensors"))
                .andExpect(status().isOk());
    }

    @Test
    void seeIfRemoveRoomSensorWorks() throws Exception {

        // Arrange

        validRoom.addSensor(validRoomSensor);
        String id2 = "RF12345";

        Mockito.when(roomRepository.getRoomDTOByName("Bedroom")).thenReturn(RoomMapper.objectToDTO(validRoom));
        Mockito.when((roomRepository).removeSensorDTO(RoomMapper.objectToDTO(validRoom), id2)).thenReturn(true);


        // Perform

        this.mockMvc.perform(delete("/rooms/Bedroom/sensors/RF12345"))
                .andExpect(status().isOk());
    }

    @Test
    void seeIfRemoveRoomSensorFails() throws Exception {

        // Arrange

        validRoom.addSensor(validRoomSensor);
        String id2 = "RF12345";

        Mockito.when(roomRepository.getRoomDTOByName("Bedroom")).thenReturn(RoomMapper.objectToDTO(validRoom));
        Mockito.when((roomRepository).removeSensorDTO(RoomMapper.objectToDTO(validRoom), id2)).thenReturn(true);


        // Perform

        this.mockMvc.perform(delete("/roomConfiguration/rooms/Bedroom/sensors/RF666"))
                .andExpect(status().isNotFound());
    }

    @Test
    void seeIfCreateAreaSensorWorks() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "temperature", new Date());
        List<RoomSensor> sensors = new ArrayList<>();
        sensors.add(validRoomSensor);
        validRoom.setRoomSensors(sensors);
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);

        List<String> names = new ArrayList<>();
        names.add("temperature");

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);
        Mockito.when(sensorTypeRepository.getTypeNames(validTypeList)).thenReturn(names);

        Mockito.doReturn(RoomMapper.objectToDTO(validRoom)).when(this.roomRepository).getRoomDTOByName("Bedroom");


        Mockito.doReturn(true).when(this.roomRepository).roomSensorDTOIsValid(roomSensorDTO);
        Mockito.doReturn(true).when(this.roomRepository).addSensorDTO(RoomMapper.objectToDTO(validRoom), roomSensorDTO);


        // Mockito.when(((RoomMapper.objectToDTO(validRoom)).addSensor(roomSensorDTO))).thenReturn(true);

        // Perform

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "Bedroom");

        // Assert

        assertEquals(HttpStatus.CREATED, actualResult.getStatusCode());
    }

    @Test
    void seeIfCreateAreaSensorWorksWhenSensorAlreadyExists() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "temperature", new Date());
        List<RoomSensor> sensors = new ArrayList<>();
        sensors.add(validRoomSensor);
        validRoom.setRoomSensors(sensors);
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);

        List<String> names = new ArrayList<>();
        names.add("temperature");

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);
        Mockito.when(sensorTypeRepository.getTypeNames(validTypeList)).thenReturn(names);

        Mockito.doReturn(RoomMapper.objectToDTO(validRoom)).when(this.roomRepository).getRoomDTOByName("Bedroom");


        Mockito.doReturn(true).when(this.roomRepository).roomSensorDTOIsValid(roomSensorDTO);
        Mockito.doReturn(true).when(this.roomRepository).sensorExists(roomSensorDTO.getSensorId());


        // Act

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "Bedroom");

        // Assert

        assertEquals(HttpStatus.CONFLICT, actualResult.getStatusCode());
    }

    @Test
    void seeIfCreateAreaSensorWorksWhenDateIsInvalid() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "temperature", new Date());
        List<RoomSensor> sensors = new ArrayList<>();
        sensors.add(validRoomSensor);
        validRoom.setRoomSensors(sensors);
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);
        roomSensorDTO.setDateStartedFunctioning("");

        List<String> names = new ArrayList<>();
        names.add("temperature");

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);
        Mockito.when(sensorTypeRepository.getTypeNames(validTypeList)).thenReturn(names);

        Mockito.doReturn(RoomMapper.objectToDTO(validRoom)).when(this.roomRepository).getRoomDTOByName("Bedroom");


        Mockito.doReturn(true).when(this.roomRepository).roomSensorDTOIsValid(roomSensorDTO);


        // Act

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "Bedroom");

        // Assert

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, actualResult.getStatusCode());
    }

    @Test
    void seeIfCreateAreaSensorFailsEmptyName() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "temperature", new Date());
        List<RoomSensor> sensors = new ArrayList<>();
        sensors.add(validRoomSensor);
        validRoom.setRoomSensors(sensors);
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);
        roomSensorDTO.setName("");

        List<String> names = new ArrayList<>();
        names.add("temperature");

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);
        Mockito.when(sensorTypeRepository.getTypeNames(validTypeList)).thenReturn(names);

        Mockito.doReturn(RoomMapper.objectToDTO(validRoom)).when(this.roomRepository).getRoomDTOByName("Bedroom");


        Mockito.doReturn(true).when(this.roomRepository).roomSensorDTOIsValid(roomSensorDTO);
        Mockito.doReturn(true).when(this.roomRepository).addSensorDTO(RoomMapper.objectToDTO(validRoom), roomSensorDTO);


        // Perform

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "Bedroom");

        // Assert

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, actualResult.getStatusCode());
    }

    @Test
    void seeIfCreateAreaSensorFailsNotFoundSensorType() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "wrong", new Date());
        List<RoomSensor> sensors = new ArrayList<>();
        sensors.add(validRoomSensor);
        validRoom.setRoomSensors(sensors);
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);

        List<String> names = new ArrayList<>();
        names.add("temperature");

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);
        Mockito.when(sensorTypeRepository.getTypeNames(validTypeList)).thenReturn(names);

        Mockito.doReturn(RoomMapper.objectToDTO(validRoom)).when(this.roomRepository).getRoomDTOByName("Bedroom");


        Mockito.doReturn(true).when(this.roomRepository).roomSensorDTOIsValid(roomSensorDTO);
        Mockito.doReturn(true).when(this.roomRepository).addSensorDTO(RoomMapper.objectToDTO(validRoom), roomSensorDTO);


        // Perform

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "Bedroom");

        // Assert

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, actualResult.getStatusCode());
    }

    @Test
    void seeIfCreateAreaSensorFailsSameSensor() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "temperature", new Date());
        List<RoomSensor> sensors = new ArrayList<>();
        sensors.add(validRoomSensor);
        validRoom.setRoomSensors(sensors);
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);

        List<String> names = new ArrayList<>();
        names.add("temperature");

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);
        Mockito.when(sensorTypeRepository.getTypeNames(validTypeList)).thenReturn(names);

        Mockito.doReturn(RoomMapper.objectToDTO(validRoom)).when(this.roomRepository).getRoomDTOByName("Bedroom");


        Mockito.doReturn(true).when(this.roomRepository).roomSensorDTOIsValid(roomSensorDTO);
        Mockito.doReturn(false).when(this.roomRepository).addSensorDTO(RoomMapper.objectToDTO(validRoom), roomSensorDTO);


        // Perform

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "Bedroom");

        // Assert

        assertEquals(HttpStatus.CONFLICT, actualResult.getStatusCode());
    }

    @Test
    void seeIfCreateAreaSensorFailsNullName() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "temperature", new Date());
        List<RoomSensor> sensors = new ArrayList<>();
        sensors.add(validRoomSensor);
        validRoom.setRoomSensors(sensors);
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);
        roomSensorDTO.setName(null);

        List<String> names = new ArrayList<>();
        names.add("temperature");

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);
        Mockito.when(sensorTypeRepository.getTypeNames(validTypeList)).thenReturn(names);

        Mockito.doReturn(RoomMapper.objectToDTO(validRoom)).when(this.roomRepository).getRoomDTOByName("Bedroom");


        //Mockito.doReturn(true).when(this.roomRepository).roomSensorDTOIsValid(roomSensorDTO);
        Mockito.doReturn(true).when(this.roomRepository).addSensorDTO(RoomMapper.objectToDTO(validRoom), roomSensorDTO);


        // Perform

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "Bedroom");

        // Assert

        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
    }

    @Test
    void seeIfCreateAreaSensorFailsNullDate() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "temperature", new Date());
        List<RoomSensor> sensors = new ArrayList<>();
        sensors.add(validRoomSensor);
        validRoom.setRoomSensors(sensors);
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);
        roomSensorDTO.setDateStartedFunctioning(null);

        List<String> names = new ArrayList<>();
        names.add("temperature");

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);
        Mockito.when(sensorTypeRepository.getTypeNames(validTypeList)).thenReturn(names);

        Mockito.doReturn(RoomMapper.objectToDTO(validRoom)).when(this.roomRepository).getRoomDTOByName("Bedroom");


        //Mockito.doReturn(true).when(this.roomRepository).roomSensorDTOIsValid(roomSensorDTO);
        Mockito.doReturn(true).when(this.roomRepository).addSensorDTO(RoomMapper.objectToDTO(validRoom), roomSensorDTO);


        // Perform

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "Bedroom");

        // Assert

        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
    }

    @Test
    void seeIfCreateAreaSensorFailsNullType() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "temperature", new Date());
        List<RoomSensor> sensors = new ArrayList<>();
        sensors.add(validRoomSensor);
        validRoom.setRoomSensors(sensors);
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);
        roomSensorDTO.setTypeSensor(null);

        List<String> names = new ArrayList<>();
        names.add("temperature");

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);
        Mockito.when(sensorTypeRepository.getTypeNames(validTypeList)).thenReturn(names);

        Mockito.doReturn(RoomMapper.objectToDTO(validRoom)).when(this.roomRepository).getRoomDTOByName("Bedroom");


        //Mockito.doReturn(true).when(this.roomRepository).roomSensorDTOIsValid(roomSensorDTO);
        Mockito.doReturn(true).when(this.roomRepository).addSensorDTO(RoomMapper.objectToDTO(validRoom), roomSensorDTO);


        // Perform

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "Bedroom");

        // Assert

        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
    }

    @Test
    void seeIfCreateAreaSensorFailsNullSensorId() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "temperature", new Date());
        List<RoomSensor> sensors = new ArrayList<>();
        sensors.add(validRoomSensor);
        validRoom.setRoomSensors(sensors);
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);
        roomSensorDTO.setSensorId(null);

        List<String> names = new ArrayList<>();
        names.add("temperature");

        Mockito.when(sensorTypeRepository.getAllSensorTypeDTO()).thenReturn(validTypeList);
        Mockito.when(sensorTypeRepository.getTypeNames(validTypeList)).thenReturn(names);

        Mockito.doReturn(RoomMapper.objectToDTO(validRoom)).when(this.roomRepository).getRoomDTOByName("Bedroom");


        //Mockito.doReturn(true).when(this.roomRepository).roomSensorDTOIsValid(roomSensorDTO);
        Mockito.doReturn(true).when(this.roomRepository).addSensorDTO(RoomMapper.objectToDTO(validRoom), roomSensorDTO);


        // Perform

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "Bedroom");

        // Assert

        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
    }

    @Test
    void seeIfCreateRoomSensorFailsWrongRoom() {

        // Arrange

        RoomSensor sensor2 = new RoomSensor("test", "test", "temperature", new Date());
        RoomSensorDTO roomSensorDTO = RoomSensorMapper.objectToDTO(sensor2);

        Mockito.doThrow(IllegalArgumentException.class).when(this.roomRepository).getRoomDTOByName("name");

        // Perform

        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoomSensor(roomSensorDTO, "name");

        // Assert

        assertEquals(HttpStatus.NOT_FOUND, actualResult.getStatusCode());
    }

    @Test
    public void seeIfCreateRoomWorks() {
        //Arrange
        Mockito.doReturn(true).when(this.houseRoomService).addMinimalRoomDTOToHouse(roomDTOMinimal);

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(roomDTOMinimal, HttpStatus.CREATED);

        //Act
        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoom(roomDTOMinimal);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void seeIfCreateRoomWorksIfRoomAlreadyExists() {
        //Arrange
        Mockito.doReturn(false).when(this.houseRoomService).addMinimalRoomDTOToHouse(roomDTOMinimal);

        ResponseEntity<Object> expectedResult = new ResponseEntity<>("The room you are trying to create already exists.", HttpStatus.CONFLICT);

        //Act
        ResponseEntity<Object> actualResult = roomConfigurationWebController.createRoom(roomDTOMinimal);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void seeIfConfigureRoomWorks() {
        //Arrange
        Mockito.doReturn(true).when(this.roomRepository).configureRoom(roomDTOMinimal, "Kitchen");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>(roomDTOMinimal, HttpStatus.OK);

        //Act
        ResponseEntity<Object> actualResult = roomConfigurationWebController.configureRoom("Kitchen", roomDTOMinimal);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void seeIfConfigureRoomWorksWhenItDoestNotExist() {
        //Arrange
        Mockito.doReturn(false).when(this.roomRepository).configureRoom(roomDTOMinimal, "21");

        ResponseEntity<Object> expectedResult = new ResponseEntity<>("The room you are trying to edit does not exist in the database.", HttpStatus.NOT_FOUND);

        //Act
        ResponseEntity<Object> actualResult = roomConfigurationWebController.configureRoom("21", roomDTOMinimal);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void seeIfConfigureRoomWorksWhenDimensionAreInvalid() {
        //Arrange

        roomDTOMinimal.setLength(0D);

        ResponseEntity<Object> expectedResult = new ResponseEntity<>("The room you entered has invalid parameters.", HttpStatus.UNPROCESSABLE_ENTITY);

        //Act
        ResponseEntity<Object> actualResult = roomConfigurationWebController.configureRoom("21", roomDTOMinimal);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

//    @Test
//    void seeIfDeleteRoomWorks() {
//        //Arrange
//        Mockito.when(this.roomRepository.deleteRoom(roomDTO)).thenReturn(true);
//
//
//        ResponseEntity<Object> expectedResult = new ResponseEntity<>(roomDTO, HttpStatus.OK);
//
//        //Act
//        ResponseEntity<Object> actualResult = roomConfigurationWebController.deleteRoom(roomDTO.getName());
//
//        //Assert
//        assertEquals(expectedResult, actualResult);
//    }

    @Test
    void seeIfDeleteRoomWorksIfRoomAlreadyExists() {
        //Arrange
        Mockito.when(this.roomRepository.deleteRoom(roomDTO)).thenReturn(false);

        ResponseEntity<Object> expectedResult = new ResponseEntity<>("The room you are trying to delete does not exist in the database.", HttpStatus.NOT_FOUND);

        //Act
        ResponseEntity<Object> actualResult = roomConfigurationWebController.deleteRoom(roomDTO.getName());

        //Assert
        assertEquals(expectedResult, actualResult);
    }
}