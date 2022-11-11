package com.Spring.Boot.Junit.Mockito.Fixture.Assesment.SpringBootJunitMockitoFixtureAssesment.controller;


import com.Spring.Boot.Junit.Mockito.Fixture.Assesment.SpringBootJunitMockitoFixtureAssesment.MockitoFixtureAssessment;
import com.Spring.Boot.Junit.Mockito.Fixture.Assesment.SpringBootJunitMockitoFixtureAssesment.model.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MockitoFixtureAssessment.class)
@SpringBootTest
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class MovieControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext movieContext;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(movieContext).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            System.out.println(jsonContent);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void printCheckJsonBody() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/movies/saveMovie")
                        .content(asJsonString(new Movie()))
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    @Test
    public void verifyAllMovies() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.
                        get("/movies/getMovies").
                        accept(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$",hasSize(7))).andDo(print());
    }

    @Test
    public void verifyMovieById() throws Exception{
//        {
//            "id": "636284b798908a02aa6f0062",
//                "name": "Iron Man",
//                "releaseDate": "2000-01-02"
//        },
        mockMvc.perform(
                        MockMvcRequestBuilders.
                                get("/movies/getMovie/636284b798908a02aa6f0062").
                                accept(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.releaseDate").exists())
                .andDo(print());
    }

    @Test
    public void verifyMovieBy_InValid_Id() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.
                        get("/movies/getMovie/1"). //
                        accept(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.message").value("THE REQUEST CANNOT BE PLACED DUE TO MALFUNCTION SYNTAX."))
                .andDo(print());

    }

    @Test
    public void verifyInValidMOVIE_NOT_FOUND_INVALID_ID() throws Exception{
//        {
//            "id": "636284b798908a02aa6f0062",
//                "name": "Thor",
//                "releaseDate": "2000-01-02"
//        },
        mockMvc.perform(
                        MockMvcRequestBuilders.
                                get("/movies/getMovie/636284b798908a02aa6f0069"). // id is not exist in MongoDB
                                accept(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value("MOVIE DOESN't EXIST WITH THIS ID"))
                .andDo(print());
    }


    @Test
    public void verifySaveMovie() throws Exception{
        Movie movie = new Movie(new ObjectId("636284b798908a02aa6f0069"), "Iron Man", "2040");
        mockMvc.perform(MockMvcRequestBuilders.post("/movies/saveMovie")
                .content(asJsonString(movie))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Iron Man"))
                .andExpect(jsonPath("$.releaseDate").value("2040"))

                .andDo(print());
    }

    @Test
    public void verifySaveMovie_Error_NullName() throws Exception{
        Movie movie = new Movie(new ObjectId("636284b798908a02aa6f0069"), null, "2040");
        mockMvc.perform(MockMvcRequestBuilders.post("/movies/saveMovie")
                        .content(asJsonString(movie))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value("Movie name must not be null."))
                .andDo(print());
    }

    @Test
    public void verifySaveMovie_Error_NullReleaseDate() throws Exception{
        Movie movie = new Movie(new ObjectId("636284b798908a02aa6f0069"), "Some Name", null);
        mockMvc.perform(MockMvcRequestBuilders.post("/movies/saveMovie")
                        .content(asJsonString(movie))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value("Release Date must not be null."))
                .andDo(print());
    }


    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% PATCH %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Test
    public void verifyupdateMovie() throws Exception{
        Movie movie = new Movie(new ObjectId("636284b798908a02aa6f0062"), "Update Name", null);
        mockMvc.perform(MockMvcRequestBuilders.patch("/movies/updateMovie")
                        .content(asJsonString(movie))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.id").value("636284b798908a02aa6f0062"))
                .andExpect(jsonPath("$.name").value("Update Name"))
                .andDo(print());
    }


    @Test
    public void verifyupdateMovie_Error_Invalid_ID() throws Exception{
        Movie movie = new Movie(new ObjectId("636284b798908a02aa6f0033"), "Some Name", "some year");
        mockMvc.perform(MockMvcRequestBuilders.patch("/movies/updateMovie")
                        .content(asJsonString(movie))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value("No value present"))
                .andDo(print());
    }


    @Test
    public void verifydeleteMovie() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/movies/deleteMovie/635b62d4d75c313263288f21")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.message").value("Successfully deleted."))
                .andDo(print());
    }
    @Test
    public void verifydeleteMovie_InvalidID() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/movies/deleteMovie/635b62d4d75c313263288f21")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value("MOVIE DOESN't EXIST WITH THIS ID"))
                .andDo(print());
    }

}

