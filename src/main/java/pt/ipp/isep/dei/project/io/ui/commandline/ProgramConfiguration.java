package pt.ipp.isep.dei.project.io.ui.commandline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import pt.ipp.isep.dei.project.io.ui.utils.InputHelperUI;
import pt.ipp.isep.dei.project.io.ui.utils.MenuFormatter;
import pt.ipp.isep.dei.project.io.ui.utils.UtilsUI;
import pt.ipp.isep.dei.project.model.Local;
import pt.ipp.isep.dei.project.model.areatype.AreaTypeRepository;
import pt.ipp.isep.dei.project.model.device.config.DeviceTypeConfig;
import pt.ipp.isep.dei.project.model.house.House;
import pt.ipp.isep.dei.project.model.sensortype.SensorTypeRepository;
import pt.ipp.isep.dei.project.model.repository.HouseCrudRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class ProgramConfiguration implements CommandLineRunner {

    @Autowired
    private SensorTypeRepository sensorTypeRepository;
    @Autowired
    private AreaTypeRepository areaTypeRepository;
    @Autowired
    private HouseCrudRepo houseCrudRepo;
    @Autowired
    private HouseMonitoringUI houseMonitoringUI;
    @Autowired
    private HouseConfigurationUI houseConfigurationUI;
    @Autowired
    private RoomConfigurationUI roomConfigurationUI;
    @Autowired
    private EnergyConsumptionUI energyConsumptionUI;
    @Autowired
    private EnergyGridSettingsUI energyGridSettingsUI;
    @Autowired
    private RoomMonitoringUI roomMonitoringUI;
    @Autowired
    private GASettingsUI gaSettingsUI;
    @Autowired
    private SensorSettingsUI sensorSettingsUI;
    @Autowired
    private DataImporter dataImporter;


    @Override
    public void run(String... args) throws Exception {
        FileInputUtils fileUtils = new FileInputUtils();
        List<String> deviceTypeConfig = new ArrayList<>();

        int gridMeteringPeriod = configGridMeteringPeriod(fileUtils);

        int deviceMeteringPeriod;
        try {
            if (fileUtils.validDeviceMetering()) {
                deviceMeteringPeriod = fileUtils.deviceMeteringPeriod;
            } else return;
        } catch (IllegalArgumentException il) {
            return;
        }

        //DeviceTypeConfiguration - US70
        deviceTypeConfig = deviceTypeConfiguration(deviceTypeConfig);

        //Add Sensor and Area Types to Program
        addSensorAndAreaTypes(fileUtils);


        House house = mainHouse(houseCrudRepo, gridMeteringPeriod, deviceMeteringPeriod, deviceTypeConfig);
        dataImporter.importData(house);
        //LOAD PERSISTED GA DATA

        //MAIN CODE
//        Scanner enterToReturnToConsole = new Scanner(System.in);
//        int option;
//        while (true) {
//            System.out.println(
//                    "                      ______          ___ _    _____ _    _ \n" +
//                            "                    / ____\\ \\        / (_) |  / ____| |  | |\n" +
//                            "                   | (___  \\ \\  /\\  / / _| |_| |    | |__| |\n" +
//                            "                    \\___ \\  \\ \\/  \\/ / | | __| |    |  __  |\n" +
//                            "                    ____) |  \\  /\\  /  | | |_| |____| |  | |\n" +
//                            "                   |_____/    \\/  \\/   |_|\\__|\\_____|_|  |_|    2018/19\n" +
//                            "                          \n                                Smart Grid Menu \n"
//            );
//
//            // Submenus Input selection
//
//            List<String> mainMenuOptions = new ArrayList<>();
//            mainMenuOptions.add("Geographic Area Settings");
//            mainMenuOptions.add("House Settings.");
//            mainMenuOptions.add("Room Settings.");
//            mainMenuOptions.add("Sensor Settings.");
//            mainMenuOptions.add("Energy Grid Settings.");
//            mainMenuOptions.add("Room Monitoring.");
//            mainMenuOptions.add("House Monitoring.");
//            mainMenuOptions.add("Energy Consumption Management.");
//            mainMenuOptions.add("Re-import all data.");
//            mainMenuOptions.add("Exit Application");
//
//            MenuFormatter.showMenu("Main Menu ", mainMenuOptions);
//

//            boolean activeInput = true;
//
//            while (activeInput) {
//                option = InputHelperUI.getInputAsInt();
//                switch (option) {
//                    case 1:
//                        gaSettingsUI.runGASettings();
//                        returnToMenu(enterToReturnToConsole);
//                        activeInput = false;
//                        break;
//                    case 2:
//                        houseConfigurationUI.run(house);
//                        returnToMenu(enterToReturnToConsole);
//                        activeInput = false;
//                        break;
//                    case 3:
//                        roomConfigurationUI.run(house);
//                        returnToMenu(enterToReturnToConsole);
//                        activeInput = false;
//                        break;
//                    case 4:
//                        sensorSettingsUI.run();
//                        returnToMenu(enterToReturnToConsole);
//                        activeInput = false;
//                        break;
//                    case 5:
//                        energyGridSettingsUI.run(house);
//                        returnToMenu(enterToReturnToConsole);
//                        activeInput = false;
//                        break;
//                    case 6:
//                        roomMonitoringUI.run(house);
//                        returnToMenu(enterToReturnToConsole);
//                        activeInput = false;
//                        break;
//                    case 7:
//                        houseMonitoringUI.run(house);
//                        returnToMenu(enterToReturnToConsole);
//                        activeInput = false;
//                        break;
//                    case 8:
//                        energyConsumptionUI.run();
//                        returnToMenu(enterToReturnToConsole);
//                        activeInput = false;
//                        break;
//                    case 9:
//                        dataImporter.importData(house);
//                        returnToMenu(enterToReturnToConsole);
//                        activeInput = false;
//                        break;
//                    case 0:
//                        System.exit(0);
//                        return;
//                    default:
//                        System.out.println(UtilsUI.INVALID_OPTION);
//                        break;
//                }
//            }
    }
//    }

//    private static void returnToMenu(Scanner scanner) {
//        String pressEnter = "\nPress ENTER to return.";
//        System.out.println(pressEnter);
//        scanner.nextLine();
//    }

    private static House mainHouse(HouseCrudRepo houseCrudRepo, int gridMeteringPeriod, int deviceMeteringPeriod, List<String> deviceTypeConfig) {
        House house;
        Optional<House> aux = houseCrudRepo.findById("01");
        if (aux.isPresent()) {
            house = aux.get();
            house.setGridMeteringPeriod(gridMeteringPeriod);
            house.setDeviceMeteringPeriod(deviceMeteringPeriod);
            house.buildAndSetDeviceTypeList(deviceTypeConfig);
            houseCrudRepo.save(house);
            return house;
        }
        house = new House("01", new Local(0, 0, 0), gridMeteringPeriod, deviceMeteringPeriod, deviceTypeConfig);
        return houseCrudRepo.save(house);
    }

    private void addSensorAndAreaTypes(FileInputUtils fileUtils) {
        //Sensor Types
        try {
            fileUtils.getSensorTypeConfig();
            fileUtils.addSensorTypesToRepository(sensorTypeRepository);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);

        }
        //Area Types
        try {
            fileUtils.getAreaTypeConfig();
            fileUtils.addAreatypesToRepository(areaTypeRepository);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Method to create all devices types available
     *
     * @param deviceTypeConfig
     * @return string list representing all device types available
     */
    private List<String> deviceTypeConfiguration(List<String> deviceTypeConfig) {
        try {
            DeviceTypeConfig devTConfig = new DeviceTypeConfig();
            deviceTypeConfig = devTConfig.getDeviceTypeConfig();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return deviceTypeConfig;
    }


    private int configGridMeteringPeriod(FileInputUtils fileUtils) {
        int gridMeteringPeriod = 0;
        String fixConfigFile = "Please fix Configuration File before continuing.";
        try {
            if (fileUtils.gridMeteringPeriodIsValid()) {
                gridMeteringPeriod = fileUtils.gridMeteringPeriod;
            } else {
                System.out.println("ERROR: Configuration File values are incorrect. Energy Grids cannot be created.\n" +
                        fixConfigFile);
                System.exit(0);
            }
        } catch (IOException ioe) {
            System.out.println("ERROR: Unable to process configuration file.\n" +
                    fixConfigFile);
            System.exit(0);
        } catch (NumberFormatException nfe) {
            System.out.println("ERROR: Configuration File value is not a numeric value.\n" +
                    fixConfigFile);
            System.exit(0);
        }
        return gridMeteringPeriod;
    }

}