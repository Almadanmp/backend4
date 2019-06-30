package pt.ipp.isep.dei.project.controller.controllerweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.ApplicationScope;
import pt.ipp.isep.dei.project.dto.*;
import pt.ipp.isep.dei.project.model.bridgeservices.HouseRoomService;
import pt.ipp.isep.dei.project.model.room.RoomRepository;
import pt.ipp.isep.dei.project.model.sensortype.SensorTypeRepository;
import pt.ipp.isep.dei.project.model.user.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@ApplicationScope
@RequestMapping("/rooms")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"}, maxAge = 3600)
public class RoomsWebController {

    private static final String ADMIN = "admin";
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    SensorTypeRepository sensorTypeRepository; // This is an aggregate root.
    @Autowired
    UserService userService;
    @Autowired
    private HouseRoomService houseRoomService;

    /**
     * Shows all the Rooms present in the database.
     *
     * @return OK status and a list of Room Minimal DTO.
     */
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRooms() {
        List<RoomDTOMinimal> roomDTOList = roomRepository.getAllRoomsAsMinimalDTOs();
        if (roomDTOList == null) {
            List<RoomDTOMinimal> empty = new ArrayList<>();
            return new ResponseEntity<>(empty, HttpStatus.OK);
        }
        RoomSensorDTO roomSensorDTO = new RoomSensorDTO();
        for (RoomDTOMinimal r : roomDTOList) {
            if (userService.getUserRoleFromToken() == null) {
                break;
            }
            if (userService.getUserRoleFromToken().equals(ADMIN)) {
                Link roomSensors = linkTo(methodOn(RoomsWebController.class).getSensors(r.getName())).withRel("Get Room Sensors");
                Link deleteRoom = linkTo(methodOn(RoomsWebController.class).deleteRoom(r.getName())).withRel("Delete this Room");
                Link editRoom = linkTo(methodOn(RoomsWebController.class).configureRoom(r.getName(), new RoomDTOMinimal()))
                        .withRel("Edit this Room");
                Link addSensor = linkTo(methodOn(RoomsWebController.class).createRoomSensor(roomSensorDTO, r.getName())).withRel("Add a new Room Sensor");
                r.add(roomSensors);
                r.add(deleteRoom);
                r.add(addSensor);
                r.add(editRoom);
            } else if (userService.getUserRoleFromToken().equals("regularUser")) {
                Link roomTemp = linkTo(methodOn(RoomsWebController.class).getCurrentRoomTemperature(r.getName())).
                        withRel("Get Room Temperature");
                Link getRoomMaxTempInDay = linkTo(methodOn(RoomsWebController.class).getRoomMaxTempInDay(r.getName(), "")).withRel("Get Maximum Temperature In Day");
                r.add(getRoomMaxTempInDay);
                r.add(roomTemp);
            }
        }
        return new ResponseEntity<>(roomDTOList, HttpStatus.OK);
    }

    @GetMapping(path = "/{roomID}")
    public ResponseEntity<Object> getRoom(@PathVariable String roomID) {
        try {
            RoomDTOMinimal room = roomRepository.getRoomDTOMinimalByName(roomID);
            Link roomSensors = linkTo(methodOn(RoomsWebController.class).getSensors(room.getName())).withRel("Get Room" +
                    "Sensors");
            room.add(roomSensors);
            Link editRoom = linkTo(methodOn(RoomsWebController.class).configureRoom(room.getName(), null))
                    .withRel("Edit this Room");
            Link deleteRoom = linkTo(methodOn(RoomsWebController.class).deleteRoom(room.getName())).withRel("Delete this Room");
            room.add(deleteRoom);
            room.add(editRoom);
            return new ResponseEntity<>(room, HttpStatus.OK);
        } catch (NullPointerException ok) {
            return new ResponseEntity<>("There's no room with that ID.", HttpStatus.NOT_FOUND);
        }
    }

    //US 109 - As an Administrator, I want to edit the configuration of an existing room, so that I can reconfigure it

    /**
     * This method receives a room DTO and tries to edit the room with the corresponding ID in the repository
     * with the information provided in the DTO.
     *
     * @param roomDTOMinimal with the room ID and the information to be altered
     * @return the room DTO and Http Status OK in case the room is edited in the repository,
     * an error message and Http Status NOT FOUND in case the room does not exist
     * *
     **/
    @PutMapping(value = "/{roomID}")
    public ResponseEntity<Object> configureRoom(@PathVariable String roomID, @RequestBody RoomDTOMinimal roomDTOMinimal) {
        if (!roomDTOMinimal.areDimensionsValid()) {
            return new ResponseEntity<>("The room you entered has invalid parameters.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (roomRepository.configureRoom(roomDTOMinimal, roomID)) {
            return new ResponseEntity<>(roomDTOMinimal, HttpStatus.OK);
        }
        return new ResponseEntity<>("The room you are trying to edit does not exist in the database.", HttpStatus.NOT_FOUND);
    }

    /**
     * US605 As a Regular User, I want to get the current temperature in a room, in order to check
     * if it meets my personal comfort requirements.
     *
     * @param roomId string so we can identify the room.
     * @return the current room temperature from its most recent temperature reading.
     */

    @GetMapping("/currentRoomTemperature/{roomId}")
    public ResponseEntity<Object> getCurrentRoomTemperature(@PathVariable String roomId) {
        double currentRoomTemperature;
        try {
            currentRoomTemperature = roomRepository.getCurrentRoomTempByRoomId(roomId);
            currentRoomTemperature = currentRoomTemperature*100;
            currentRoomTemperature = Math.round(currentRoomTemperature);
            currentRoomTemperature = currentRoomTemperature /100;
            return new ResponseEntity<>(currentRoomTemperature, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("The room does not exist.", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("There are no temperature readings for that room", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * US610 - WEB Controller Methods
     * Get Max Temperature in a room in a specific day.
     */

    @GetMapping("/dayMaxTemperature/{roomId}")
    public ResponseEntity<Object> getRoomMaxTempInDay(@PathVariable String roomId, @RequestParam("initialDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String day) {
        double result;
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = df.parse(day);
        } catch (ParseException c) {
            c.printStackTrace();
            return new ResponseEntity<>("This date is not valid.", HttpStatus.OK);
        }
        try {
            result = roomRepository.getRoomMaxTempById(roomId, date);
            result = result*100;
            result = Math.round(result);
            result = result /100;
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("This room does not exist.", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("There are no readings for the given date.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method receives a room DTO Web and will try to remove the corresponding
     * room from repository
     *
     * @param roomId roomDTOWeb to be deleted from repository
     * @return the Room DTO Web and HTTP status OK in case the room is deleted, an error
     * message and HTTP status NOT FOUND in case the room is not deleted
     **/
    @PutMapping(value = "/delete/{roomId}")
    public ResponseEntity<Object> deleteRoom(@PathVariable String roomId) {
        RoomDTO roomDTO = roomRepository.getRoomDTOByName(roomId);
        if (roomRepository.deleteRoom(roomDTO)) {
            return new ResponseEntity<>(roomDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>("The room you are trying to delete does not exist in the database.", HttpStatus.NOT_FOUND);
    }

    /**
     * This method creates receives a roomDTOMinimal and tries to add the corresponding
     * room to the repository. The method will return a success message in case the
     * room is added, and a failure message in case it is not.
     *
     * @param roomDTOMinimal roomDTOWeb to be added to repository
     * @return the room DTO in case the corresponding room was created in database
     * and HTTP Status OK, an error message and an error HTTP status otherwise.
     **/
    @PostMapping(value = "/")
    public ResponseEntity<Object> createRoom(@RequestBody RoomDTOMinimal roomDTOMinimal) {
        if (!roomDTOMinimal.isNameValid()) {
            return new ResponseEntity<>("The room you introduced is invalid.", HttpStatus.BAD_REQUEST);
        }
        if (!roomDTOMinimal.areDimensionsValid()) {
            return new ResponseEntity<>("The room you introduced is invalid.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (houseRoomService.addMinimalRoomDTOToHouse(roomDTOMinimal)) {
            Link link = linkTo(methodOn(RoomsWebController.class).deleteRoom(roomDTOMinimal.getName())).withRel("Delete the created room.");
            roomDTOMinimal.add(link);
            return new ResponseEntity<>(roomDTOMinimal, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("The room you are trying to create already exists.", HttpStatus.CONFLICT);
    }

    // RoomSensors are part of the Room aggregate. RoomSensor methods therefore belong in the RoomsWebController.

    /**
     * Shows all the Sensor Types present in the database.
     *
     * @return OK status and a list of sensor Types.
     */
    @GetMapping(path = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getSensorTypes() {
        List<SensorTypeDTO> sensorTypeDTOS = sensorTypeRepository.getAllSensorTypeDTO();
        return new ResponseEntity<>(sensorTypeDTOS, HttpStatus.OK);
    }

    /**
     * US250 Web Controller:
     * Shows the Room Sensors present in a given Room
     *
     * @param roomId is the geographical area id.
     * @return OK status and a list of Area Sensor DTOs.
     */
    @GetMapping("/{roomId}/sensors")
    public ResponseEntity<List<RoomSensorDTOMinimal>> getSensors(@PathVariable String roomId) {
        List<RoomSensorDTOMinimal> roomSensorDTOList = roomRepository.getRoomDTOByName(roomId).getSensorDTOMinimalistList();
        for (RoomSensorDTOMinimal s : roomSensorDTOList) {
            if (userService.getUserRoleFromToken().equals(ADMIN)) {
                Link deleteSelf = linkTo(methodOn(RoomsWebController.class).removeRoomSensor(roomId, s.getSensorID())).
                        withRel("Delete this Sensor");
                s.add(deleteSelf);
            }
        }
        return new ResponseEntity<>(roomSensorDTOList, HttpStatus.OK);
    }

    /**
     * US253 Web Controller:
     * Creates a new sensor and adds it to a Room.
     *
     * @param idRoom is the room id.
     * @return OK status if the area sensor is successfully created. Returns htttp status 'not acceptable' if the sensor already exists.
     */
    @PostMapping("/{idRoom}/sensors")
    public ResponseEntity<Object> createRoomSensor(@RequestBody RoomSensorDTO roomSensorDTO,
                                                   @PathVariable String idRoom) {
        RoomDTO roomDTO;
        List<SensorTypeDTO> sensorTypes = sensorTypeRepository.getAllSensorTypeDTO();
        List<String> typeNames = sensorTypeRepository.getTypeNames(sensorTypes);
        try {
            roomDTO = roomRepository.getRoomDTOByName(idRoom);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("That ID does not belong to any Room", HttpStatus.NOT_FOUND);
        }
        if (roomRepository.roomSensorDTOIsValid(roomSensorDTO)) {
            if (roomSensorDTO.getName().equals("")) {
                return new ResponseEntity<>("The sensor name is not valid.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (roomSensorDTO.getDateStartedFunctioning().equals("")) {
                return new ResponseEntity<>("The date is not valid.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!typeNames.contains(roomSensorDTO.getType())) {
                return new ResponseEntity<>("The sensor type is not valid.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if(roomRepository.sensorExists(roomSensorDTO.getSensorId())){
                return new ResponseEntity<>("The sensor already exists in the database", HttpStatus.CONFLICT);
            }
            if (roomRepository.addSensorDTO(roomDTO, roomSensorDTO)) { //esta validação só está a verificar os sensores dentro do quarto
                roomRepository.updateRoomDTO(roomDTO);
                Link link = linkTo(methodOn(RoomsWebController.class).removeRoomSensor(idRoom, roomSensorDTO.getSensorId()
                )).withRel("Delete the created sensor");
                roomSensorDTO.add(link);
                return new ResponseEntity<>(roomSensorDTO, HttpStatus.CREATED);
            }
            return new ResponseEntity<>("The sensor already exists in this room", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("There was a problem creating the Room Sensor, because one or more components are missing!",
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{idRoom}/sensors/{idSensor}")
    public ResponseEntity<String> removeRoomSensor(@PathVariable String idRoom, @PathVariable String idSensor) {
        RoomDTO roomDTO = roomRepository.getRoomDTOByName(idRoom);
        if (roomRepository.removeSensorDTO(roomDTO, idSensor)) {
            roomRepository.updateRoomDTO(roomDTO);
            return new ResponseEntity<>("Sensor was removed successfully from geographic area", HttpStatus.OK);
        }
        return new ResponseEntity<>("Sensor doesn't exist or wasn't found.", HttpStatus.NOT_FOUND);
    }
}
