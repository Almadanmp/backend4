package pt.ipp.isep.dei.project.model.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ipp.isep.dei.project.model.Reading;
import pt.ipp.isep.dei.project.model.ReadingUtils;
import pt.ipp.isep.dei.project.model.device.DeviceList;
import pt.ipp.isep.dei.project.model.sensortype.SensorType;
import pt.ipp.isep.dei.project.repository.RoomRepository;
import pt.ipp.isep.dei.project.repository.RoomSensorRepository;
import pt.ipp.isep.dei.project.repository.SensorTypeRepository;

import java.util.*;
import java.util.logging.Logger;

/**
 * Class that groups a number of Rooms in a House.
 */
@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomSensorRepository roomSensorRepository;

    @Autowired
    SensorTypeRepository sensorTypeRepository;

    private List<Room> rooms;

    private static final String STRING_BUILDER = "---------------\n";



    /**
     * RoomList() empty constructor that initializes an ArrayList of Rooms.
     */
    public RoomService() {
        this.rooms = new ArrayList<>();
    }

    /**
     * RoomList() empty constructor that initializes an ArrayList of Rooms.
     */
    public RoomService(RoomRepository roomRepository, RoomSensorRepository roomSensorRepository, SensorTypeRepository sensorTypeRepository) {
        this.roomRepository = roomRepository;
        this.roomSensorRepository = roomSensorRepository;
        this.sensorTypeRepository = sensorTypeRepository;
        this.rooms = new ArrayList<>();
    }


//TODO UPDATED METHODS

    public List<Room> getAllRooms() {
        List<Room> finalList = new ArrayList<>();
        Iterable<Room> roomsAux = roomRepository.findAll();
        if (roomsAux != null) {
            for (Room r : roomsAux) {
                finalList.add(r);
            }
        }
        return finalList;
    }


    /**
     * Method to check if a room with the given ID exists in the repository.
     *
     * @param idToCheck is the id that we want to check for being present.
     * @return is true if a room with the given ID exists, false if it doesn't.
     */
    boolean idExists(String idToCheck) {
        for (Room r : roomRepository.findAll()) {
            if (r.getId().equals(idToCheck)) {
                return true;
            }
        }
        return false;
    }

    public boolean addPersistence(Room room) {
        Optional<Room> room2 = roomRepository.findByRoomName(room.getId());
        if (room2.isPresent()) {
            Room room3 = room2.get();
            roomRepository.save(room3);
            add(room3);
        }
        roomRepository.save(room);
        add(room);
        return true;
    }

    public List<Room> getAllByEnergyGridName(String energyGridName) {
        return roomRepository.findAllByEnergyGridId(energyGridName);
    }


    /**
     * String Builder of the RoomList.
     *
     * @return a String of the Rooms in the RoomList.
     */
    public String buildRoomsAsString(List<Room> rooms) {
        StringBuilder result = new StringBuilder(STRING_BUILDER);
        if (rooms.isEmpty()) {
            return "Invalid List - List is Empty\n";
        }
        for (Room r : rooms) {
            result.append(r.getId()).append(") Description: ").append(r.getDescription()).append(" | ");
            result.append("House Floor: ").append(r.getFloor()).append(" | ");
            result.append("Width: ").append(r.getWidth()).append(" | ");
            result.append("Length: ").append(r.getLength()).append(" | ");
            result.append("Height: ").append(r.getHeight()).append("\n");

        }
        result.append(STRING_BUILDER);
        return result.toString();
    }

    /**
     * Method for creating a new room with all it's parameters,
     * the method checks the room name to see if it already exists before creating it
     *
     * @param roomDesignation room name
     * @param roomHouseFloor  floor of the house where room is located
     * @param width           from room sizeDB
     * @param length          from room sizeDB
     * @param height          from room sizeDB
     * @return new created room
     */

    public Room createRoom(String roomDesignation, String roomDescription, int roomHouseFloor, double width, double length, double height, String houseID, String energyGridID) {
        for (Room r : getAllRooms()) {
            String designation = r.getId();
            if (roomDesignation.equals(designation)) {
                return r;
            }
        }
        return new Room(roomDesignation, roomDescription, roomHouseFloor, width, length, height, houseID, energyGridID);
    }

    /**
     * Method that save a Room to the RoomRepository.
     * <p>
     * It is also adding to the local list while the project is being refactored an lists removed
     *
     * @param room is the room we want to save.
     * @return true if the room was successfully saved to the repository, false otherwise.
     */
    void addRoomToDb(Room room) {
        roomRepository.save(room);
    }

    /**
     * Method that removes a Room from the RoomList.
     *
     * @param room is the room we want to removeGeographicArea from the roomList.
     * @return true if room was successfully removed from the roomList, false otherwise.
     */
    public boolean removeRoom(Room room) {
        Optional<Room> aux = roomRepository.findById(room.getId());
        if (aux.isPresent()) {
            roomRepository.delete(room);
            return true;
        }
        return false;
    }

    /**
     * This method checks if room list is empty.
     *
     * @return true if list is empty, false otherwise.
     **/
    public boolean isEmptyRooms() {
        return getAllRooms().isEmpty();
    }

    /**
     * Checks the room list sizeDB and returns the sizeDB as int.\
     *
     * @return RoomList sizeDB as int
     **/
    public int sizeDB() {
        return getAllRooms().size();
    }

    /**
     * This method receives an index as parameter and gets a room from room list.
     *
     * @param name the name of the room
     * @return returns room that corresponds to index.
     */
    Room getDB(String name) {
        Room room;
        Optional<Room> aux = roomRepository.findById(name);
        if (aux.isPresent()) {
            room = aux.get();
            return room;
        }
        throw new IndexOutOfBoundsException("ERROR: No Room was file with the following name: " + name + " .");
    }

//TODO OLD METHODS

    /**
     * String Builder of the RoomList.
     *
     * @return a String of the Rooms in the RoomList.
     */
    public String buildString() {
        StringBuilder result = new StringBuilder(STRING_BUILDER);
        if (this.isEmptyRooms()) {
            return "Invalid List - List is Empty\n";
        }
        for (int i = 0; i < this.sizeDB(); i++) {
            Room aux = this.get(i);
            result.append(i).append(") Designation: ").append(aux.getId()).append(" | ");
            result.append("Description: ").append(aux.getDescription()).append(" | ");
            result.append("House Floor: ").append(aux.getFloor()).append(" | ");
            result.append("Width: ").append(aux.getWidth()).append(" | ");
            result.append("Length: ").append(aux.getLength()).append(" | ");
            result.append("Height: ").append(aux.getHeight()).append("\n");

        }
        result.append(STRING_BUILDER);
        return result.toString();
    }

    /**
     * Method that checks if a Room is contained in the RoomList.
     *
     * @param room is the room that we want to see if it's contained in the roomList.
     * @return true if room is contained in the RoomList, false otherwise.
     */
    public boolean contains(Room room) {
        return (this.rooms.contains(room));
    }

    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Method that adds a Room to the RoomList.
     *
     * @param room is the room we want to addWithoutPersisting.
     * @return true if the room was successfully added to the RoomList, false otherwise.
     */
    public boolean add(Room room) {
        if (!(rooms.contains(room))) {
            rooms.add(room);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method that returns a DeviceList with all the devices of the RoomList.
     *
     * @return a DeviceList of all the devices in the RoomList.
     */
    public DeviceList getDeviceList() {
        DeviceList finalList = new DeviceList();
        for (Room r : this.rooms) {
            finalList.appendListNoDuplicates(r.getDeviceList());
        }
        return finalList;
    }

    /**
     * Returns the daily estimate of the consumption of all devices of a given type, in all rooms on this list.
     *
     * @param deviceType the device type
     * @param time       represents a day in minutes
     * @return the sum of all daily estimate consumptions of that type
     */
    public double getDailyConsumptionByDeviceType(String deviceType, int time) {
        double result = 0;
        for (Room r : rooms) {
            result += r.getEstimateConsumptionOverTimeByDeviceType(deviceType, time);
        }
        return Math.floor(result * 10) / 10;
    }

    /**
     * Method accesses the sum of nominal powers of all devices connected to every room in room list.
     *
     * @return is the sum of nominal powers of all rooms.
     */

    public double getNominalPower() {
        double result = 0;
        for (Room r : rooms) {
            result += r.getNominalPower();
        }
        return result;
    }

    /**
     * Method for building string to be displayed to user so he can see Devices of a certain type listed
     *
     * @param deviceType type of device user wants to list
     * @return list of devices of that type param
     */

    public StringBuilder buildDeviceListByType(String deviceType) {
        StringBuilder result = new StringBuilder();
        for (Room r : this.rooms) {
            if (r != null) {
                result.append(r.buildDevicesStringByType(deviceType));
            }
        }
        return result;
    }

    /**
     * Getter (array of rooms)
     *
     * @return array of rooms
     */
    public Room[] getElementsAsArray() {
        int sizeOfResultArray = rooms.size();
        Room[] result = new Room[sizeOfResultArray];
        for (int i = 0; i < rooms.size(); i++) {
            result[i] = rooms.get(i);
        }
        return result;
    }

    public boolean saveRoom(Room room) {
        Optional<Room> room1 = roomRepository.findByRoomName(room.getId());
        if (room1.isPresent()) {
            return false;
        }
        roomRepository.save(room);
        return true;
    }

    /**
     * This method receives an index as parameter and gets a room from room list.
     *
     * @param index the index of the room
     * @return returns room that corresponds to index.
     */
    public Room get(int index) {
        if (this.rooms.isEmpty()) {
            throw new IndexOutOfBoundsException("The room list is empty.");
        }
        return this.rooms.get(index);
    }

    /**
     * Method that finds a given object in the repository by its ID
     *
     * @param idToFind is the ID that we want to look for.
     * @return is an Optional that either contains the object if it existed, or a null.
     */

    public Optional<Room> findByID(String idToFind) {
        return roomRepository.findById(idToFind);
    }


    //Methods from RoomSensorService

    public void save(RoomSensor sensor) {
        Optional<SensorType> sensorType = sensorTypeRepository.findByName(sensor.getSensorType().getName());

        if (sensorType.isPresent()) {
            sensor.setSensorType(sensorType.get());
        } else {
            SensorType newSensorType = sensor.getSensorType();
            sensorTypeRepository.save(newSensorType);
            sensor.setSensorType(newSensorType);
        }
        this.roomSensorRepository.save(sensor);
    }

    public List<RoomSensor> getAllSensor() {
        return roomSensorRepository.findAll();
    }

    public List<RoomSensor> getAllByRoomId(String roomName) {
        return roomSensorRepository.findAllByRoomId(roomName);
    }


    private RoomSensor updateSensor(RoomSensor roomSensor) {
        return roomSensorRepository.save(roomSensor);
    }

    /**
     * Method to get a TypeSensor from the Repository through a given id
     *
     * @param name selected name
     * @return Type Sensor corresponding to the given id
     */
    private SensorType getTypeSensorByName(String name) {
        Optional<SensorType> value = sensorTypeRepository.findByName(name);
        if (value.isPresent()) {
            return value.get();
        }
        return null;
    }

    /**
     * Method to check if an instance of this class is equal to another object.
     * Necessary for adding rooms to list.
     *
     * @return is true if the object is a power source list with the same contents.
     */
    public RoomSensor createRoomSensor(String id, String name, SensorType sensorType, Date dateStartedFunctioning, String roomId) {

        SensorType aux = getTypeSensorByName(sensorType.getName());
        if (aux != null) {
            sensorType = aux;
        }
        return new RoomSensor(id, name, sensorType, dateStartedFunctioning, roomId);
    }


    /**
     * Method to Add a sensor only if it's not contained in the list already.
     *
     * @param sensorToAdd is the sensor we want to addWithoutPersisting to the sensorList.
     * @return true if sensor was successfully added to the AreaSensorList, false otherwise.
     */

    public boolean addRoomToDb(RoomSensor sensorToAdd) {
        Optional<RoomSensor> aux = roomSensorRepository.findById(sensorToAdd.getId());
        if (aux.isPresent()) {
            RoomSensor sensor = aux.get();
            sensor = sensorToAdd;
            roomSensorRepository.save(sensor);
        }
        roomSensorRepository.save(sensorToAdd);
        return true;
    }


    /**
     * This method receives a sensor ID, checks if that sensor exists in the repository.
     *
     * @param sensorID String of sensor ID
     * @return true in case the sensor exists, false otherwise.
     **/
    boolean sensorExistsInRepository(String sensorID) {
        Optional<RoomSensor> value = roomSensorRepository.findById(sensorID);
        return value.isPresent();
    }

    /**
     * This method receives an index as parameter and gets a sensor from sensor list.
     *
     * @param id the index of the Sensor.
     * @return returns sensor that corresponds to index.
     */
    public RoomSensor getById(String id) {
        Optional<RoomSensor> value = roomSensorRepository.findById(id);
        return value.orElse(null);
    }


    /**
     * This method receives a string of a sensor ID and a Date that corresponds to a
     * reading Date.
     * The method will look for the sensor with the corresponding ID in repository
     * and check if the sensor's was active when the reading was recorded.
     *
     * @param sensorID string of the sensor's ID
     * @param date     reading Date
     * @return true in case the sensor was active when the reading was created, false otherwise.
     **/
    boolean sensorFromRepositoryIsActive(String sensorID, Date date) {
        Optional<RoomSensor> value = roomSensorRepository.findById(sensorID);
        if (value.isPresent()) {
            RoomSensor roomSensor = value.get();
            Date startDate = roomSensor.getDateStartedFunctioning();
            return date.equals(startDate) || date.after(startDate);
        }
        return false;
    }

    //METHODS FROM READING SERVICE

    /**
     * This method receives the parameters to create a reading and tries to add that
     * reading to the repository. It also receives a Logger so that it can register every
     * reading that was not added to its corresponding sensor.
     * This method will look for the house sensor in the repository by its ID.
     *
     * @param readingValue is the value of the reading we want to add.
     * @param readingDate  is the date of the reading we want to add.
     * @param unit         is the unit of the reading we want to add.
     * @return true in case the reading was added false otherwise.
     */
    public boolean addHouseReadingToRepository(RoomSensor sensor, Double readingValue, Date readingDate, String unit, Logger logger) {
        if (sensor != null && sensorExistsInRepository(sensor.getId())) {

            if (sensorFromRepositoryIsActive(sensor.getId(), readingDate)) {
                if (sensor.readingExists(readingDate)) {
                    logger.fine("The reading " + readingValue + " " + unit + " from " + readingDate + " with a sensor ID "
                            + sensor.getId() + " wasn't added because it already exists.");
                    return false;
                }
                Reading reading = new Reading(readingValue, readingDate, unit, sensor.getId());
                sensor.getHouseReadings().add(reading);
                updateSensor(sensor);
                return true;
            }
            logger.fine("The reading " + readingValue + " " + unit + " from " + readingDate + " with a sensor ID "
                    + sensor.getId() + " wasn't added because the reading is from before the sensor's starting date.");
            return false;
        }
        logger.fine("The reading " + readingValue + " " + unit + " from " + readingDate + " because a sensor with that ID wasn't found.");
        return false;
    }

}
