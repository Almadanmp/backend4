package pt.ipp.isep.dei.project.model;

/**
 * Class that represents a Geographical Area.
 */

public class GeographicArea {

    private String id;
    private TypeArea typeArea;
    private double length;
    private double width;
    private GeographicArea motherArea;
    private Local location;
    private SensorList areaSensors;
    private String description;


    // GeoArea constructors. The minimum amount of data for a GeoArea is a place and a type of area.
    // They can be made with or without a sensor list.

    /**
     * empty constructor to use on UIs
     */

    public GeographicArea(String id, TypeArea typeArea, double length, double width, Local location) {
        setId(id);
        setTypeArea(typeArea);
        setLength(length);
        setWidth(width);
        setLocal(location);
        this.areaSensors = new SensorList();
    }

    // Setters and Getters for all the parameters.

    public String getId() {
        return this.id;
    }

    void setId(String name) {
        if (isGeographicNameValid(name)) {
            this.id = name;
        } else {
            throw new IllegalArgumentException("Please Insert Valid Name");
        }
    }

    /**
     * Sets the width attribute
     *
     * @param width related to longitude
     */

    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Sets the length attribute
     *
     * @param length related to latitude
     */

    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Sets the description attribute
     *
     * @param description String is a short description of the Geographical Area.
     */

    public void setDescription(String description) {
        this.description = description;
    }

    String getDescription() {
        return this.description;
    }

    /**
     * Setter for Geographic Area type.
     *
     * @param typeArea Type area is determined by a string - e.g. "Street", "City", etc.
     */
    void setTypeArea(TypeArea typeArea) {
        this.typeArea = typeArea;
    }

    /**
     * Setter for Geographic Area localization.
     *
     * @param local Localization is defined by three doubles (longitude, latitude and altitude).
     */
    void setLocal(Local local) {
        this.location = local;
    }


    /**
     * Setter for Sensor List.
     *
     * @param listToSet list to set
     */

    public void setSensorList(SensorList listToSet) {
        this.areaSensors = listToSet;
    }

    public boolean setMotherArea(GeographicArea geoArea) {
        if (geoArea != null) {
            this.motherArea = geoArea;
            return true;
        } else {
            return false;
        }
    }

    GeographicArea getMotherArea() {
        return this.motherArea;
    }

    /**
     * This method is used to check if the daughter area attribute mother area corresponds to the testes mother area.
     *
     * @param daughterArea - Geographic Area being tested according to its mother area attribute.
     * @param motherArea   - Geographic Area being compared to daughter area mother area attribute.
     * @return boolean
     */

    public boolean checkIfAreaIsContained(GeographicArea daughterArea, GeographicArea motherArea) {
        GeographicArea tempMotherArea = daughterArea;
        while (tempMotherArea.getMotherArea() != null) {
            if (tempMotherArea.getMotherArea().equals(motherArea)) {
                return true;
            } else {
                tempMotherArea = daughterArea.getMotherArea();
            }
        }
        return false;
    }

    /**
     * Getter for type of Geographic Area.
     *
     * @return returns the attribute TypeArea from an object of the class Geographic Area
     */
    TypeArea getTypeArea() {
        return this.typeArea;
    }

    /**
     * Getter for Geographic Area localization.
     *
     * @return returns the attribute local from an object of the class Geographic Area
     */
    public Local getLocal() {
        return this.location;
    }

    /**
     * Getter for Geographic Area sensor list.
     *
     * @return returns the attribute sensorList from an object of the class Geographic Area
     */
    public SensorList getSensorList() {
        return this.areaSensors;
    }

    /**
     * Method to print details that are required for a Geographic Area to be different from another GA (equals -
     * name, type area and local).
     *
     * @return returns a string with Geographic Area Parameters
     */

    public String buildGeographicAreaString() {
        String result;
        result = this.id + ", " + this.typeArea.getTypeOfGeographicArea() + ", " +
                this.location.getLatitude() + "º lat, " + this.location.getLongitude() + "º long\n";
        return result;
    }

    /**
     * Method to restrain input name so they cant be null or empty.
     *
     * @param name name inserted by user
     * @return will return true if the name is valid or it will throw an exception if Invalid
     */
    private boolean isGeographicNameValid(String name) {
        return (name != null && !name.isEmpty());
    }


    /**
     * Method will go through Geographic Area's sensor list, create a second list with the type
     * of sensors defined by the parameter and finally return the most recent value recorded in that list.
     *
     * @param typeOfSensor Type sensor is determined by a string - e.g. "Temperature", "Rain", etc.
     * @return returns a double of the most recent value recorded in every type sensor given
     */
    double getMostRecentReadingValue(String typeOfSensor) {
        SensorList listToTest = this.areaSensors;
        for (int i = 0; i < listToTest.size(); i++) {
            if (!(listToTest.get(i).getTypeSensor().getName().equals(typeOfSensor))) {
                listToTest.removeSensor(listToTest.get(i));
            }
        }
        return listToTest.getMostRecentlyUsedSensor().getReadingList().getMostRecentReading().getValue();
    }

    double getWidth() {
        return this.width;
    }

    public double getLength() {
        return this.length;
    }

    boolean isAreaContainedInAnotherArea(GeographicArea smallerAG, GeographicArea biggestAG) {
        double latTopVert1 = smallerAG.getLocal().getLatitude() + (smallerAG.getWidth() / 2);
        double longTopVert1 = smallerAG.getLocal().getLongitude() - (smallerAG.getLength() / 2);
        double latBotVert1 = smallerAG.getLocal().getLatitude() - (smallerAG.getWidth() / 2);
        double longBotVert1 = smallerAG.getLocal().getLongitude() + (smallerAG.getLength() / 2);
        double latTopVert2 = biggestAG.getLocal().getLatitude() + (biggestAG.getWidth() / 2);
        double longTopVert2 = biggestAG.getLocal().getLongitude() - (biggestAG.getLength() / 2);
        double latBotVert2 = biggestAG.getLocal().getLatitude() - (biggestAG.getWidth() / 2);
        double longBotVert2 = biggestAG.getLocal().getLongitude() + (biggestAG.getLength() / 2);
        return (latTopVert2 <= latTopVert1 && longTopVert2 >= longTopVert1 && latBotVert2 >= latBotVert1 && longBotVert2 <= longBotVert1);
    }

    /**
     * Method will calculate the distance between two different Geographic Areas.
     *
     * @param ga object of the class GeographicAreaController
     * @return returns a double of the distance between Geographic Areas.
     */
    double calculateDistanceToGA(GeographicArea ga) {
        Local l = ga.getLocal();
        return this.location.getLinearDistanceBetweenLocalsInKm(l);
    }

    public boolean addSensorToSensorList(Sensor sensor) {
        String sensorToAddName = sensor.getName();
        for (Sensor s : this.areaSensors.getListOfSensors()) {
            String sensorNameTest = s.getName();
            if (sensorNameTest.equals(sensorToAddName))
                return false;
        }
        this.areaSensors.addSensor(sensor);
        return true;
    }

    /**
     * Method checks if geographic area's sensor list is empty.
     * @return true if SensorList is empty, false otherwise
     */
    public boolean isSensorListEmpty() {
        return areaSensors.isEmpty();
    }


    /**
     * Method 'equals' is required so that each 'Geographic Area' can be added to a 'Geographic Area List'. Two
     * Geographic Areas cannot have the same Localization, name and TypeArea
     */
    @Override
    public boolean equals(Object testObject) {
        if (this == testObject) {
            return true;
        }
        if (!(testObject instanceof GeographicArea)) {
            return false;
        }
        GeographicArea gA = (GeographicArea) testObject;
        return (this.getLocal().equals(gA.getLocal()) && (this.getId().equals(gA.getId()) && (this.getTypeArea().getTypeOfGeographicArea().equals(gA.getTypeArea().getTypeOfGeographicArea()))));
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
