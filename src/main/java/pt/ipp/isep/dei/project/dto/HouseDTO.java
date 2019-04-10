package pt.ipp.isep.dei.project.dto;

import pt.ipp.isep.dei.project.model.device.devicetypes.DeviceType;

import java.util.List;
import java.util.Objects;

public class HouseDTO {
    private String name;
    private AddressDTO address;
    private LocalDTO location;
    private List<EnergyGridDTO> energyGridList;
    private List<RoomDTO> roomList;
    private GeographicAreaDTO motherArea;
    private int gridMeteringPeriod;
    private int deviceMeteringPeriod;
    private List<DeviceType> deviceTypeList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public LocalDTO getLocation() {
        return location;
    }

    public void setLocation(LocalDTO location) {
        this.location = location;
    }

    public List<EnergyGridDTO> getEnergyGridList() {
        return energyGridList;
    }

    public void setEnergyGridList(List<EnergyGridDTO> energyGridList) {
        this.energyGridList = energyGridList;
    }

    public List<RoomDTO> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomDTO> roomList) {
        this.roomList = roomList;
    }

    public GeographicAreaDTO getMotherArea() {
        return motherArea;
    }

    public void setMotherArea(GeographicAreaDTO motherArea) {
        this.motherArea = motherArea;
    }

    public int getGridMeteringPeriod() {
        return gridMeteringPeriod;
    }

    public void setGridMeteringPeriod(int gridMeteringPeriod) {
        this.gridMeteringPeriod = gridMeteringPeriod;
    }

    public int getDeviceMeteringPeriod() {
        return deviceMeteringPeriod;
    }

    public void setDeviceMeteringPeriod(int deviceMeteringPeriod) {
        this.deviceMeteringPeriod = deviceMeteringPeriod;
    }

    public List<DeviceType> getDeviceTypeList() {
        return deviceTypeList;
    }

    public void setDeviceTypeList(List<DeviceType> deviceTypeList) {
        this.deviceTypeList = deviceTypeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HouseDTO house = (HouseDTO) o;
        return Objects.equals(this.address, house.address);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
