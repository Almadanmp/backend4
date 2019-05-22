package pt.ipp.isep.dei.project.controller.controllerweb;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.ipp.isep.dei.project.dto.GeographicAreaDTO;
import pt.ipp.isep.dei.project.dto.LocalDTO;
import pt.ipp.isep.dei.project.model.geographicarea.GeographicAreaRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({MockitoExtension.class})
class GASettingsWebControllerTest {

    @Autowired
    private MockMvc mvc;
    @Mock
    private GeographicAreaRepository geographicAreaRepository;
    @InjectMocks
    private GASettingsWebController gaSettingsWebController;

    @Before
    public void insertData() {
        MockitoAnnotations.initMocks(this);

        this.mvc = MockMvcBuilders.standaloneSetup(gaSettingsWebController).build();

    }

//    @Test
//    void createGeoAreaDTO() throws Exception {
//
//        mvc.perform(post("/geographic_area_settings/areas")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\n" +
//                        "  \"id\": 66,\n" +
//                        "  \"name\": \"Gaia\",\n" +
//                        "  \"typeArea\": \"urban area\",\n" +
//                        "  \"length\": 500,\n" +
//                        "  \"width\": 100,\n" +
//                        "  \"localDTO\": {\n" +
//                        "    \"latitude\": 41,\n" +
//                        "    \"longitude\": -8,\n" +
//                        "    \"altitude\": 100,\n" +
//                        "    \"id\": 0\n" +
//                        "  },\n" +
//                        "  \"description\": \"3rd biggest city\"\n" +
//                        "}"))
//                .andExpect(status().isCreated());
//    }

    @Test
    void seeIfCreateGeoAreaWorks() {
        //Arrange

        GeographicAreaDTO validGeographicAreaDTO = new GeographicAreaDTO();
        LocalDTO localDTO = new LocalDTO();

        localDTO.setLatitude(41);
        localDTO.setLongitude(-8);
        localDTO.setAltitude(100);

        validGeographicAreaDTO.setLocal(localDTO);
        validGeographicAreaDTO.setDescription("3rd biggest city");
        validGeographicAreaDTO.setName("Gaia");
        validGeographicAreaDTO.setId(66L);
        validGeographicAreaDTO.setWidth(100);
        validGeographicAreaDTO.setLength(500);
        validGeographicAreaDTO.setTypeArea("urban area");

        Mockito.doReturn(true).when(geographicAreaRepository).addAndPersistDTO(any(GeographicAreaDTO.class));

        ResponseEntity<String> expectedResult = new ResponseEntity<>("The Geographic Area has been created.", HttpStatus.CREATED);

        //Act
        ResponseEntity<Object> actualResult = gaSettingsWebController.createGeoArea(validGeographicAreaDTO);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfCreateGeoAreaDoesntWorkIDNull() {
        //Arrange

        GeographicAreaDTO validGeographicAreaDTO = new GeographicAreaDTO();
        LocalDTO localDTO = new LocalDTO();

        localDTO.setLatitude(41);
        localDTO.setLongitude(-8);
        localDTO.setAltitude(100);

        validGeographicAreaDTO.setLocal(localDTO);
        validGeographicAreaDTO.setDescription("3rd biggest city");
        validGeographicAreaDTO.setName("Gaia");
        validGeographicAreaDTO.setWidth(100);
        validGeographicAreaDTO.setLength(500);
        validGeographicAreaDTO.setTypeArea("urban area");

        Mockito.doReturn(false).when(geographicAreaRepository).addAndPersistDTO(any(GeographicAreaDTO.class));

        ResponseEntity<String> expectedResult = new ResponseEntity<>("The Geographic Area hasn't been created. You have entered a repeated or invalid Area.", HttpStatus.CONFLICT);

        //Act
        ResponseEntity<Object> actualResult = gaSettingsWebController.createGeoArea(validGeographicAreaDTO);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfCreateGeoAreaDoesntWorkNameNull() {
        //Arrange

        GeographicAreaDTO validGeographicAreaDTO = new GeographicAreaDTO();
        LocalDTO localDTO = new LocalDTO();

        localDTO.setLatitude(41);
        localDTO.setLongitude(-8);
        localDTO.setAltitude(100);

        validGeographicAreaDTO.setLocal(localDTO);
        validGeographicAreaDTO.setDescription("3rd biggest city");
        validGeographicAreaDTO.setId(2L);
        validGeographicAreaDTO.setWidth(100);
        validGeographicAreaDTO.setLength(500);
        validGeographicAreaDTO.setTypeArea("urban area");

        Mockito.doReturn(false).when(geographicAreaRepository).addAndPersistDTO(any(GeographicAreaDTO.class));

        ResponseEntity<String> expectedResult = new ResponseEntity<>("The Geographic Area hasn't been created. You have entered a repeated or invalid Area.", HttpStatus.CONFLICT);

        //Act
        ResponseEntity<Object> actualResult = gaSettingsWebController.createGeoArea(validGeographicAreaDTO);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfCreateGeoAreaDoesntWorkTypeNull() {
        //Arrange

        GeographicAreaDTO validGeographicAreaDTO = new GeographicAreaDTO();
        LocalDTO localDTO = new LocalDTO();

        localDTO.setLatitude(41);
        localDTO.setLongitude(-8);
        localDTO.setAltitude(100);

        validGeographicAreaDTO.setLocal(localDTO);
        validGeographicAreaDTO.setDescription("3rd biggest city");
        validGeographicAreaDTO.setId(2L);
        validGeographicAreaDTO.setName("Porto");
        validGeographicAreaDTO.setWidth(100);
        validGeographicAreaDTO.setLength(500);

        Mockito.doReturn(false).when(geographicAreaRepository).addAndPersistDTO(any(GeographicAreaDTO.class));

        ResponseEntity<String> expectedResult = new ResponseEntity<>("The Geographic Area hasn't been created. You have entered a repeated or invalid Area.", HttpStatus.CONFLICT);

        //Act
        ResponseEntity<Object> actualResult = gaSettingsWebController.createGeoArea(validGeographicAreaDTO);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfCreateGeoAreaDoesntWorkLocalNull() {
        //Arrange

        GeographicAreaDTO validGeographicAreaDTO = new GeographicAreaDTO();

        validGeographicAreaDTO.setDescription("3rd biggest city");
        validGeographicAreaDTO.setId(2L);
        validGeographicAreaDTO.setWidth(100);
        validGeographicAreaDTO.setLength(500);
        validGeographicAreaDTO.setTypeArea("urban area");
        Mockito.doReturn(false).when(geographicAreaRepository).addAndPersistDTO(any(GeographicAreaDTO.class));

        ResponseEntity<String> expectedResult = new ResponseEntity<>("The Geographic Area hasn't been created. You have entered a repeated or invalid Area.", HttpStatus.CONFLICT);

        //Act
        ResponseEntity<Object> actualResult = gaSettingsWebController.createGeoArea(validGeographicAreaDTO);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void seeIfCreateGeoAreaDoesntWorkNull() {
        //Arrange

        ResponseEntity<String> expectedResult = new ResponseEntity<>("The Geographic Area hasn't been created. You have entered a repeated or invalid Area.", HttpStatus.CONFLICT);

        //Act
        ResponseEntity<Object> actualResult = gaSettingsWebController.createGeoArea(null);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAllGeoAreasDTO() {
    }

    @Test
    void addDaughterAreaInvalidMother() {
        GeographicAreaDTO validGeographicAreaDTO = new GeographicAreaDTO();

        validGeographicAreaDTO.setDescription("3rd biggest city");
        validGeographicAreaDTO.setId(2L);
        validGeographicAreaDTO.setWidth(100);
        validGeographicAreaDTO.setLength(500);
        validGeographicAreaDTO.setTypeArea("urban area");


        ResponseEntity<String> expectedResult = new ResponseEntity<>("The Geographic Area hasn't been added. You have entered a repeated or invalid Area.", HttpStatus.CONFLICT);

        //Act
        ResponseEntity<Object> actualResult = gaSettingsWebController.addDaughterArea(1L, 3L);

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void addDaughterAreaInvalidDaughter() {
        GeographicAreaDTO validGeographicAreaDTO = new GeographicAreaDTO();

        validGeographicAreaDTO.setDescription("3rd biggest city");
        validGeographicAreaDTO.setId(2L);
        validGeographicAreaDTO.setWidth(100);
        validGeographicAreaDTO.setLength(500);
        validGeographicAreaDTO.setTypeArea("urban area");

        ResponseEntity<String> expectedResult = new ResponseEntity<>("The Geographic Area hasn't been added. You have entered a repeated or invalid Area.", HttpStatus.CONFLICT);

        //Act
        ResponseEntity<Object> actualResult = gaSettingsWebController.addDaughterArea(6L, validGeographicAreaDTO.getId());

        //Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void addDaughterArea() {
        GeographicAreaDTO motherDTO = new GeographicAreaDTO();

        motherDTO.setDescription("3rd biggest city");
        motherDTO.setId(1L);
        motherDTO.setWidth(100);
        motherDTO.setLength(500);
        motherDTO.setTypeArea("urban area");

        GeographicAreaDTO daughterDTO = new GeographicAreaDTO();

        daughterDTO.setDescription("3rd biggest city");
        daughterDTO.setId(2L);
        daughterDTO.setWidth(100);
        daughterDTO.setLength(500);
        daughterDTO.setTypeArea("urban area");

        Mockito.doReturn(motherDTO).when(geographicAreaRepository).getDTOByIdWithMother(1L);

        Mockito.doReturn(daughterDTO).when(geographicAreaRepository).getDTOByIdWithMother(2L);

        Mockito.doReturn(true).when(geographicAreaRepository).addDaughterDTO(motherDTO, daughterDTO);

        Mockito.doReturn(true).when(geographicAreaRepository).updateAreaDTOWithMother(motherDTO);


        ResponseEntity<String> expectedResult = new ResponseEntity<>("The Geographic Area has been added.", HttpStatus.CREATED);

        //Act
        ResponseEntity<Object> actualResult = gaSettingsWebController.addDaughterArea(daughterDTO.getId(), motherDTO.getId());

        //Assert
        assertEquals(expectedResult, actualResult);
    }

}