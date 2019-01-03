package pt.ipp.isep.dei.project.io.ui;

import pt.ipp.isep.dei.project.model.*;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class MainUI {

    public static void main(String[] args) {

        //Program Variables

        boolean activeProgram = true;

        //TEST MAIN LISTS
        GeographicAreaList mGeographicAreaList = new GeographicAreaList();
        TypeAreaList mTypeAreaList = new TypeAreaList();

        //TEST READINGS
        ReadingList readingList = new ReadingList();
        Reading reading1 = new Reading(30, new GregorianCalendar(2018, 8, 6).getTime());
        Reading reading2 = new Reading(20, new GregorianCalendar(2018, 8, 5).getTime());
        Reading reading3 = new Reading(40, new GregorianCalendar(2018, 8, 5).getTime());
        readingList.addReading(reading1);
        readingList.addReading(reading2);
        readingList.addReading(reading3);

        //TEST ENERGYGRID
        EnergyGridList energyGridList1 = new EnergyGridList();
        EnergyGridList energyGridList2 = new EnergyGridList();
        EnergyGrid eg1 = new EnergyGrid("rede", 56789);
        energyGridList2.addEnergyGridToEnergyGridList(eg1);

        //TEST SENSORS
        SensorList sensorList1 = new SensorList();
        SensorList sensorList2 = new SensorList();
        Sensor sensor1 = new Sensor("sensor", new TypeSensor("temperature"), new Local(4, 4), new GregorianCalendar(8, 8, 8).getTime(), readingList);
        Sensor sensor2 = new Sensor("sensor2", new TypeSensor("Rain"), new Local(4, 4), new GregorianCalendar(8, 8, 8).getTime(), readingList);
        sensorList2.addSensor(sensor1);
        sensorList2.addSensor(sensor2);

        //TEST ROOMS
        RoomList roomList1 = new RoomList();
        RoomList roomList2 = new RoomList();
        Room room1 = new Room("room1", 19, 23456789, sensorList2);
        Room room2 = new Room("kitchen", 8, 2, sensorList2);
        roomList1.addRoom(room2);
        roomList1.addRoom(room1);
        RoomList roomListGrid = new RoomList();

        //TEST GEOGRAPHIC AREAS
        GeographicArea geoa1 = new GeographicArea("porto", new TypeArea("cidade"), new Local(4, 4));
        GeographicArea geoa2 = new GeographicArea("lisboa", new TypeArea("aldeia"), new Local(4, 4));
        geoa1.setSensorList(sensorList2);


        //TEST HOUSES
        HouseList houseList1 = new HouseList();
        HouseList houseList2 = new HouseList();

        House house1 = new House("houseOne", "Address1", new Local(22, 3), "4150-657");
        House house2 = new House("houseTwo", "Address2", new Local(4, 4), "3456-123");
        House house3 = new House("houseTwo", "Address3", new Local(18, 10), "3555-555");
        House house4 = new House("houseOne", "Address4", new Local(18, 10), "3555-555");
        house1.setmRoomList(roomList1);
        house2.setmMotherArea(geoa1);
        house4.setmMotherArea(geoa1);
        house2.setmRoomList(roomList1);
        house3.setmMotherArea(geoa1);
        house3.setmRoomList(roomList1);
        houseList1.addHouseToHouseList(house2);
        houseList1.addHouseToHouseList(house3);
        houseList1.addHouseToHouseList(house4);
        houseList1.addHouseToHouseList(house1);
        house1.setmMotherArea(geoa2);
        houseList2.addHouseToHouseList(house1);


        //TEST ORGANIZATION SETTERS
        eg1.setListOfRooms(roomListGrid);
        house1.setmEGList(energyGridList2);

        geoa1.setSensorList(sensorList2);
        geoa1.setHouseList(houseList1);
        geoa2.setSensorList(sensorList2);
        geoa2.setHouseList(houseList2);

        mGeographicAreaList.addGeographicAreaToGeographicAreaList(geoa1);
        mGeographicAreaList.addGeographicAreaToGeographicAreaList(geoa2);

        house2.setmEGList(energyGridList2);
        //MAIN CODE
        Scanner enterToReturnToConsole = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);

        int option;

        while (activeProgram) {

            System.out.println("\n*******************\n" +
                    "****** Main Menu Test *******\n" +
                    "****** sWitCh 2018 ********\n" +
                    "*****************\n");

            // Submenus Input selection

            String[] menu = {" 0. Exit.\n",
                    "1 Create a new Type of Geographic Areas\n",
                    "2 List The Existing Types of Geographic Areas\n",
                    "3 Create a new Geographic Area\n",
                    "4. List All Geographic Areas of a certain type.\n",
                    "5. Determine the type of a sensor.\n",
                    "6. Create a new Sensor and add it to a GA.\n",
                    "7. Say that an area is contained in another area.\n",
                    "8. See if an area is contained in another area.\n",
                    "9. Display all available sensors.\n",
                    "10. Display all available Geographic Areas.\n",
                    "11. Configure a new house.\n",
                    "12. Add a new room to a house.\n",
                    "13. Get Maximum Temperature in a Room in a Given Day.\n",
                    "14. Create an energy grid.\n",
                    "15. Add a power source to an energy grid.\n",
                    "16. See Room List.\n",
                    "17. Get Current Temperature in a House Area.\n",
                    "18. Change Room Settings.\n",
                    "19. Add A Room to a Energy Grid\n",
                    "20. Room Configuration.\n",
                    "21. House Monitoring.\n"};

            System.out.println("Select the task you want to do:");

            String formattedString = Arrays.toString(menu)
                    .replace(",", "")  //remove the commas
                    .replace("[", "")  //remove the right bracket
                    .replace("]", "");  //remove the left bracket

            System.out.print(formattedString);
            System.out.print("\nEnter option number:\n");
            String pressEnter = "\nPress ENTER to return.";
            boolean activeInput = false;
            while (!activeInput) {
                HouseMonitoringUI hMUi = new HouseMonitoringUI();
                option = hMUi.readInputNumberAsInt();
                switch (option) {
                    case 0:
                        activeProgram = false;
                        break;
                    case 1:
                        HouseConfigurationUI view1 = new HouseConfigurationUI();
                        view1.runUS01UI(mTypeAreaList);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 2:
                        HouseConfigurationUI view2 = new HouseConfigurationUI();
                        view2.runUS02(mTypeAreaList);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 3:
                        HouseConfigurationUI view3 = new HouseConfigurationUI();
                        view3.runUS03(mGeographicAreaList);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 4:
                        HouseConfigurationUI view4 = new HouseConfigurationUI();
                        view4.runUS04(mGeographicAreaList);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 5:
                        HouseConfigurationUI view5 = new HouseConfigurationUI();
                        view5.runUS05(sensorList1);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 6:
                        HouseConfigurationUI view6 = new HouseConfigurationUI(sensorList1, mGeographicAreaList);
                        view6.run06();
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 7:
                        HouseConfigurationUI view7 = new HouseConfigurationUI();
                        view7.runUS07(mGeographicAreaList);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 8:
                        HouseConfigurationUI view8 = new HouseConfigurationUI();
                        view8.runUS08(mGeographicAreaList);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 9:
                        if (sensorList1.checkIfListIsValid())
                            System.out.println("The list of sensors is empty!");
                        for (Sensor sensor : sensorList1.getSensorList()) {
                            System.out.println(sensor.getName());
                        }
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 10:
                        if (mGeographicAreaList.checkIfListIsValid())
                            System.out.println("The list of geographic areas is empty!");
                        for (GeographicArea ga : mGeographicAreaList.getGeographicAreaList()) {
                            System.out.println(ga.getName());
                        }
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 11:
                        HouseConfigurationUI view11 = new HouseConfigurationUI();
                        view11.runUS101(mGeographicAreaList);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 12:
                        HouseConfigurationUI view12 = new HouseConfigurationUI();
                        view12.runUS105(mGeographicAreaList);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 14:
                        HouseConfigurationUI view14 = new HouseConfigurationUI();
                        view14.runUS130(houseList1);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 15:
                        HouseConfigurationUI view15 = new HouseConfigurationUI();
                        view15.runUS135(houseList1);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 18:
                        US108UI view16 = new US108UI();
                        view16.runUS108UI(mGeographicAreaList);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 19:
                        HouseConfigurationUI view145 = new HouseConfigurationUI();
                        view145.runUS145(mGeographicAreaList);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 20:
                        RoomConfigurationUI viewRC = new RoomConfigurationUI();
                        viewRC.run(mGeographicAreaList, roomList1);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    case 21:
                        HouseMonitoringUI view623 = new HouseMonitoringUI();
                        view623.run(mGeographicAreaList, roomList1);
                        System.out.println(pressEnter);
                        enterToReturnToConsole.nextLine();
                        activeInput = true;
                        break;
                    default:
                        System.out.println("Please enter a valid option");
                        enterToReturnToConsole.nextLine();
                        break;
                }
            }
        }
    }
}