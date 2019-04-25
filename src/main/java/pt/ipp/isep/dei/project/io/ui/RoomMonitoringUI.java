package pt.ipp.isep.dei.project.io.ui;

import pt.ipp.isep.dei.project.controller.HouseMonitoringController;
import pt.ipp.isep.dei.project.controller.RoomMonitoringController;
import pt.ipp.isep.dei.project.dto.RoomDTO;
import pt.ipp.isep.dei.project.io.ui.utils.DateUtils;
import pt.ipp.isep.dei.project.io.ui.utils.InputHelperUI;
import pt.ipp.isep.dei.project.io.ui.utils.MenuFormatter;
import pt.ipp.isep.dei.project.io.ui.utils.UtilsUI;
import pt.ipp.isep.dei.project.model.geographicarea.GeographicAreaService;
import pt.ipp.isep.dei.project.model.room.RoomService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RoomMonitoringUI {
    private final HouseMonitoringController houseMonitoringController;
    private RoomMonitoringController roomMonitoringController;
    private List<String> menuOptions;

    public RoomMonitoringUI() {
        this.houseMonitoringController = new HouseMonitoringController();
        this.roomMonitoringController = new RoomMonitoringController();
        menuOptions = new ArrayList<>();
        menuOptions.add("Get the instants where the temperature fell bellow the comfort level in a given time interval and category (US440).");
        menuOptions.add("Get the instants where the temperature rose above the comfort level in a given time interval and category (US445).");
        menuOptions.add("(Return to main menu)");
    }

    void run(GeographicAreaService geographicAreaService, RoomService roomService) {
        boolean activeInput = false;
        int option;
        System.out.println("--------------\n");
        System.out.println("Room Monitoring\n");
        System.out.println("--------------\n");
        while (!activeInput) {
            MenuFormatter.showMenu("Room Monitoring", menuOptions);
            option = InputHelperUI.getInputAsInt();
            switch (option) {
                case 1:
                    runUS440(roomService);
                    activeInput = true;
                    break;
                case 2:
                    runUS445(roomService);
                    activeInput = true;
                    break;
                case 0:
                    return;
                default:
                    System.out.println(UtilsUI.INVALID_OPTION);
                    break;
            }
        }
    }

    /**
     * US440
     * As a Power User or as a Room Owner, I want to have a list of the instants in which the temperature fell below the
     * comfort level in a given time interval and category (annex A.2 of EN 15251).
     */
    private void runUS440(RoomService roomService){
//        if (!houseMonitoringController.isMotherAreaValid(house)) {
//            return;
//        }
        displayState440(roomService);
    }

    private void displayState440(RoomService roomService) {
        System.out.println("Please select a room:");
        RoomDTO roomDTO = roomMonitoringController.getRoomDTOByList(roomService);
        System.out.println("Please enter the starting date.");
        Date startDate = DateUtils.getInputYearMonthDayHourMin();
        System.out.println("Please enter the ending date.");
        Date endDate = DateUtils.getInputYearMonthDayHourMin();
        int category = roomMonitoringController.getCategoryFromList();
        if (category == 0){
            roomMonitoringController.categoryICalculus();
        }
        if (category == 1){
            roomMonitoringController.categoryIICalculus();
        }
        if (category == 2){
            roomMonitoringController.categoryIIICalculus();
        }
    }


    /**
     * US445
     * As a Power User or as a Room Owner, I want to have a list of
     * the instants in which the temperature rose above the comfort
     * level in a given time interval and category (annex A.2 of EN 15251).
     */
    private void runUS445(RoomService roomService){}


}
