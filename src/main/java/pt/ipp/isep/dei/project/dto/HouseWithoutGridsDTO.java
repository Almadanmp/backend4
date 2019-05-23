package pt.ipp.isep.dei.project.dto;

import pt.ipp.isep.dei.project.model.device.devicetypes.DeviceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HouseWithoutGridsDTO {
    private String id;
    private AddressDTO address;
    private LocalDTO location;
    private int gridMeteringPeriod;
    private int deviceMeteringPeriod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setDeviceTypeList(List<DeviceType> deviceTypeList) {
        List<DeviceType> deviceTypeList1 = new ArrayList<>(deviceTypeList);
    }

    public void setAddressAndLocalToDTOWithoutGrids(AddressAndLocalDTO addressAndLocalDTO){
        setAddress(addressAndLocalDTO.getAddress());
        setLocation(addressAndLocalDTO.getLocal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HouseWithoutGridsDTO house = (HouseWithoutGridsDTO) o;
        return Objects.equals(this.address, house.address);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
