package pt.ipp.isep.dei.project.io.ui;

import pt.ipp.isep.dei.project.controller.HouseMonitoringController;
import pt.ipp.isep.dei.project.controller.RoomConfigurationController;
import pt.ipp.isep.dei.project.model.*;

import java.util.List;
import java.util.Scanner;

public class RoomConfigurationUI {

    private House mHouse;
    private GeographicArea mGeoArea;
    private String geoName;
    private RoomConfigurationController mRoomConfigurationController;
    private HouseMonitoringController mHouseMonitoringController;
    private static final String INVALID_OPTION = "Please enter a valid option";
    private Room mRoom;
    private Sensor mSensor;
    private String mNameSensor;
    private String mRoomName;

    RoomConfigurationUI() {
        this.mRoomConfigurationController = new RoomConfigurationController();
        this.mHouseMonitoringController = new HouseMonitoringController();
    }

    public void run(GeographicArea mGeo, House programHouse) {
        Scanner mScanner = new Scanner(System.in);
        this.mGeoArea=mGeo;
        this.mHouse = programHouse;
        boolean activeInput = false;
        int option;
        System.out.println("--------------\n");
        System.out.println("Room Configuration\n");
        System.out.println("--------------\n");
        if (mHouse == null) {
            System.out.println("Unable to select a house. Returning to main menu.");
            return;
        }
        getInputRoom();
        if (mRoom == null) {
            System.out.println("Unable to select a room. Returning to main menu.");
            return;
        }
        while (!activeInput) {
            printOptionMessage();
            option = UtilsUI.readInputNumberAsInt();
            switch (option) {
                case 1:
                    if (!getInputSensorName()) {
                        return;
                    }
                    updateModelRoomConfiguration();
                    displayStateRoomConfiguration();
                    activeInput = true;
                    break;
                case 0:
                    return;
                default:
                    System.out.println(INVALID_OPTION);
                    break;
            }
        }
    }

    /******************************************************************************
     ***************************** Auxiliary Methods ******************************
     ******************************************************************************/

    private void printOptionMessage() {
        System.out.println("Room Configuration Options:\n");
        System.out.println("1) Add a sensor to the room");
        System.out.println("0) (Return to main menu)");
    }

    private int readInputNumberAsInt() {
        Scanner mScanner = new Scanner(System.in);
        while (!mScanner.hasNextDouble()) {
            System.out.println(INVALID_OPTION);
            mScanner.next();
        }
        Double option = mScanner.nextDouble();
        return option.intValue();
    }

    public void updateModelRoomConfiguration() {
        RoomConfigurationController ctrl = new RoomConfigurationController();
        this.mRoom = mRoomConfigurationController.getRoomFromName(mRoomName, mHouse);
        this.mSensor = mRoomConfigurationController.getSensorFromName(mNameSensor, mGeoArea);
        ctrl.addSensorToRoom(mRoom, mSensor);
    }

    private void displayStateRoomConfiguration() {
        System.out.print("Sensor " + mSensor.getName() + " was successefully added to " + this.mRoomName);
    }

    /******************************************************************************
     ************************** Room Input Segment ********************************
     ******************************************************************************/

    private void getInputRoom() {
        System.out.println(
                "We need to know which one is your room.\n" + "Would you like to:\n" +
                        "1) Type the name of your Room;\n" +
                        "2) Choose it from a list;\n" +
                        "0) Return;");
        int option = readInputNumberAsInt();
        switch (option) {
            case 1:
                getInputRoomName();
                if (!getRoomByName()) {
                    System.out.println("Unable to select a Room. Returning to main menu.");
                    return;
                }
                break;
            case 2:
                getInputRoomByList();
                break;
            case 0:
                return;
            default:
                System.out.println(INVALID_OPTION);
                break;
        }
    }

    private boolean getInputRoomName() {
        Scanner mScanner = new Scanner(System.in);
        System.out.println("Please type the name of the Room you want to access.");
        this.mRoomName = mScanner.nextLine();
        return (!(this.mRoomName.equals("exit")));
    }

    private boolean getRoomByName() {
        List<Integer> listOfIndexesRoom = mRoomConfigurationController.matchRoomIndexByString(mRoomName, mHouse);
        while (listOfIndexesRoom.isEmpty()) {
            System.out.println("There is no Room with that name. Please insert the name of a Room" +
                    " that exists or  Type 'exit' to cancel and create a new Room on the Main Menu.");
            if (!getInputRoomName()) {
                return false;
            }
            listOfIndexesRoom = mRoomConfigurationController.matchRoomIndexByString(mRoomName, mHouse);
        }
        if (listOfIndexesRoom.size() > 1) {
            System.out.println("There are multiple Houses with that name. Please choose the right one.");
            System.out.println(mRoomConfigurationController.printRoomElementsByIndex(listOfIndexesRoom, mHouse));
            int aux = readInputNumberAsInt();
            if (listOfIndexesRoom.contains(aux)) {
                this.mRoom = mHouse.getRoomList().getRoomList().get(aux);
                this.mRoomName = mRoom.getRoomName();
                mHouse.getRoomList().getRoomList().get(aux);
                System.out.println("You have chosen the following Room:");
                System.out.println(mRoomConfigurationController.printRoom(mRoom));
            } else {
                System.out.println(INVALID_OPTION);
            }
        } else {
            this.mRoom = mHouse.getRoomList().getRoomList().get(listOfIndexesRoom.get(0));
            this.mRoomName = mRoom.getRoomName();
            System.out.println("You have chosen the following Room:");
            this.mHouse.getRoomList().getRoomList().get(0);
            System.out.println(mRoomConfigurationController.printRoom(mRoom));
        }
        return true;
    }

    private void getInputRoomByList() {
        if (mHouse.getRoomList().getRoomList().size() == 0) {
            System.out.print("Invalid Room List - List Is Empty\n");
            return;
        }
        boolean activeInput = false;
        System.out.println("Please select one of the existing rooms on the selected House: ");
        while (!activeInput) {
            System.out.println(mRoomConfigurationController.printRoomList(mHouse));
            int aux = readInputNumberAsInt();
            if (aux >= 0 && aux < mHouse.getRoomList().getRoomList().size()) {
                this.mRoom = mHouse.getRoomList().getRoomList().get(aux);
                this.mRoomName = mRoom.getRoomName();
                activeInput = true;
            } else {
                System.out.println(INVALID_OPTION);
            }
        }
    }

    /******************************************************************************
     ************************** Sensor Input Segment ******************************
     ******************************************************************************/

    private boolean getInputSensorName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please insert the name of the Sensor you want to add to " + mRoom.getRoomName() + " : ");
        this.mNameSensor = scanner.nextLine();
        RoomConfigurationController ctrl = new RoomConfigurationController();
        if (ctrl.doesSensorListInAGeoAreaContainASensorByName(this.mNameSensor, mGeoArea)) {
            System.out.println("You chose the Sensor " + this.mNameSensor);
        } else {
            System.out.println("This sensor does not exist in the list of sensors.");
            return false;
        }
        return true;
    }
}