package pt.ipp.isep.dei.project.model.device;

import pt.ipp.isep.dei.project.model.Metered;
import pt.ipp.isep.dei.project.model.device.devicespecs.DeviceSpecs;
import pt.ipp.isep.dei.project.model.device.programs.ProgramList;
import pt.ipp.isep.dei.project.model.device.programs.Programmable;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Class that represents device present in a Room.
 */

public class DeviceTemporary implements Metered {
    private String mName;
    private double mNominalPower;
    private DeviceSpecs mDeviceSpecs;
    private LogList mLogList;
    private boolean mActive;



    /**
     * Constructor with path by configuration file approach
     * parameters (ex. name) are not received. setter methods must be called after constructor
     *
     * @param deviceSpecs device specs
     */
    public DeviceTemporary(DeviceSpecs deviceSpecs) {
        this.mLogList = new LogList();
        this.mActive = true;
        this.mDeviceSpecs = deviceSpecs;

    }

    public void setNominalPower(Double nomPower) {
        this.mNominalPower = nomPower;
    }

    public double getNominalPower() {
        return this.mNominalPower;
    }

    /**
     * Boolean to get DeviceTemporary Status. Either if is Active or Not.
     *
     * @return true if device is active
     */
    private boolean isActive() {
        return this.mActive;
    }

        public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public List<String> getAttributeNames() {
        return mDeviceSpecs.getAttributeNames();
    }

    public Object getAttributeValue(String attributeName) {
        return mDeviceSpecs.getAttributeValue(attributeName);
    }

    public boolean setAttributeValue(String attributeName, Object attributeValue) {
        return mDeviceSpecs.setAttributeValue(attributeName, attributeValue);
    }

    public Object getAttributeUnit(String attributeName) {
        return mDeviceSpecs.getAttributeUnit(attributeName);
    }

    public String buildDeviceString() {
        String result;
        result = "The device Name is " + this.mName + ", and its NominalPower is " +
                getNominalPower() + " kW.\n";
        return result;
    }

    @Override
    public double getEnergyConsumption(float time) {
        return 0;
    }

    /**
     * get daily estimate consumption on a day (24hours)
     *
     * @return the estimateConsumption/24 hours
     */
    double getDailyEstimateConsumption() {
        return (mDeviceSpecs.getConsumption()) * 24;
    }


    /**
     * Method that will check if a DeviceTemporary is Programmable
     *
     * @return true if programmable, false if not programmable
     */
    public boolean isProgrammable() {
        return mDeviceSpecs instanceof Programmable;
    }

    /**
     * Method to get the programList if a DeviceTemporary is programmable
     *
     * @return program list
     * @throws NullPointerException - if a DeviceTemporary is not programmable, it will throw an exception so we can handle
     * with the exception on the ui and return choose a specific response.
     */
    public ProgramList getProgramList()throws IncompatibleClassChangeError {
        if (isProgrammable()) {
            Programmable aux = (Programmable) mDeviceSpecs;
            return aux.getProgramList();
        } else {
            throw new IncompatibleClassChangeError("ERROR: Unable to get list. DeviceTemporary is not programmable.");
        }
    }

    public String getType() {
        return mDeviceSpecs.getType();
    }

    /**
     * This method returns the DeviceTemporary LogList.
     *
     * @return DeviceTemporary LogList.
     */
    public LogList getLogList() {
        return this.mLogList;
    }

    /**
     * This method checks the Logs registered in a periods which are totally contained in the defined interval.
     *
     * @param initialTime - Beginning of the interval
     * @param finalTime   - Ending of the interval
     * @return total consumption within the defined interval
     */

    public double getConsumptionWithinGivenInterval(Date initialTime, Date finalTime) {
        double result = 0;
        for (Log l : mLogList.getLogList()) {
            if ((l.getInitialDate().after(initialTime) || l.getInitialDate().equals(initialTime)) &&
                    ((l.getFinalDate().before(finalTime)) || l.getFinalDate().equals(finalTime))) {
                result += l.getValue();
            }
        }
        return result;
    }

    /**
     * Method determines the amount of data logs that fall within a given time interval.
     *
     * @param initialTime is the start time of the interval.
     * @param finalTime   is the end time of the interval.
     * @return is the number of valid data logs in the given interval.
     */

    public int countLogsInInterval(Date initialTime, Date finalTime) {
        int counter = 0;
        for (Log l : mLogList.getLogList()) {
            if ((l.getInitialDate().after(initialTime) || l.getInitialDate().equals(initialTime)) &&
                    ((l.getFinalDate().before(finalTime)) || l.getFinalDate().equals(finalTime))) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * This method adds a Log to the device LogList, if the Log isn't already in the LogList.
     *
     * @param log - Parameter which will be used to add to the DeviceTemporary LogList.
     * @return true if log was added
     */

    public boolean addLog(Log log) {
        if (!(mLogList.getLogList().contains(log)) && this.mActive) {
            mLogList.getLogList().add(log);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method deactivates the device so it no longer accepts logs.
     *
     * @return true if deactivated
     */
    public boolean deactivate() {
        if (isActive()) {
            this.mActive=false;
            return true;
        } else {
            return false;
        }
    }


    public LogList getLogsInInterval(Date startDate, Date endDate) {
        LogList result = new LogList();
        for (Log l : this.getLogList().getLogList()) {
            if ((l.getInitialDate().after(startDate) || l.getInitialDate().equals(startDate)) &&
                    ((l.getFinalDate().before(endDate)) || l.getFinalDate().equals(endDate))) {
               result.addLog(l);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceTemporary device = (DeviceTemporary) o;
        return Objects.equals(mName, device.mName);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}



