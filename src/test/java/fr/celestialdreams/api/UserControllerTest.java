package fr.celestialdreams.api;

import fr.celestialdreams.api.model.User;
import fr.celestialdreams.api.controller.UserController;
import fr.celestialdreams.api.repository.UserRepository;
import fr.celestialdreams.api.service.UserService;

import org.hamcrest.core.IsNull;
import org.hamcrest.text.IsEmptyString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PutMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UserController.class)

public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
    @Autowired
    private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@Test
	public void testGetUserVoid() throws Exception {
		when(userService.getUser(1L)).thenReturn(Optional.empty());
		mockMvc.perform(get("/user/1")).andExpect(status().isNotFound());
	}

	@Test
	public void testGetUserNotFound() throws Exception {
		mockMvc.perform(get("/user/999")).andExpect(status().isNotFound());
	}

	@Test
	public void testGetUsersEmpty() throws Exception {
		when(userService.getUsers()).thenReturn(Collections.emptyList());
		mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(0)));
	}

	@Test
	public void testGetUserMandatory() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Stephane");
		user.setBirthDate(LocalDate.parse("1976-01-01"));
		user.setGender(String.valueOf('M'));
		user.setCountryCode("FR");

		when(userService.getUser(1L)).thenReturn(Optional.of(user));
		mockMvc.perform(get("/user/1")).andExpect(status().isOk()).andExpect(jsonPath("name", is("Stephane")))
				.andExpect(jsonPath("birthDate", is("1976-01-01"))).andExpect(jsonPath("countryCode", is("FR")))
				.andExpect(jsonPath("gender", is("M")));
	}

	@Test
	public void testGetUserOptional() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Stephane");
		user.setBirthDate(LocalDate.parse("1976-01-01"));
		user.setGender(null);
		user.setCountryCode(null);

		when(userService.getUser(1L)).thenReturn(Optional.of(user));
		mockMvc.perform(get("/user/1")).andExpect(status().isOk()).andExpect(jsonPath("name", is("Stephane")))
				.andExpect(jsonPath("birthDate", is("1976-01-01"))).andExpect(jsonPath("$.countryCode").doesNotExist())
				.andExpect(jsonPath("$.gender").doesNotExist());
	}

	@Test
	public void testGetUsers() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Arnaud");
		user.setBirthDate(LocalDate.parse("1975-01-01"));
		when(userService.getUsers()).thenReturn(Collections.singletonList(user));
		mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name", is("Arnaud")));
	}

	@Test
	public void testCreateUserOptional() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Arnaud");
		user.setBirthDate(LocalDate.parse("1975-01-01"));
		user.setCountryCode("FR");
		user.setGender("M");

		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": "Arnaud",
				    "birthDate": "1975-01-01",
				    "countryCode": "FR",
				    "gender": "M"
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Arnaud")).andExpect(jsonPath("$.birthDate").value("1975-01-01"))
				.andExpect(jsonPath("$.countryCode").value("FR")).andExpect(jsonPath("$.gender").value("M"));
	}

	@Test
	public void testCreateUserMandatory() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Arnaud");
		user.setBirthDate(LocalDate.parse("1975-01-01"));
		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": "Arnaud",
				    "birthDate": "1975-01-01"
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Arnaud")).andExpect(jsonPath("$.birthDate").value("1975-01-01"))
				.andExpect(jsonPath("$.countryCode").doesNotExist()).andExpect(jsonPath("$.gender").doesNotExist());
	}

	@Test
	public void testCreateUserErrKeyNameEmpty() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("");
		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": ""
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateUserErrKeyNameNull() throws Exception {
		User user = new User();
		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateUserErrKeyDateNull() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Arnaud");
		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": "Arnaud"
				}
				""";
		mockMvc.perform(post("/user").contentType("application/json").content(userJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateUserErrKeyDateEmptyOrMalformed() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Arnaud");
		user.setBirthDate(null);
		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": "Arnaud"
				    "birthDate": "abcdef"
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateUserErrKeyDateFutur() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Arnaud");
		user.setBirthDate(LocalDate.parse("2075-01-01"));
		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": "Arnaud",
				    "birthDate": "2075-01-01"
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.birthDate").value("the birtday date has to be in the past"));
		;

	}

	@Test
	public void testCreateUserNameMalformed() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Arn@ud");
		user.setBirthDate(LocalDate.parse("1975-01-01"));
		user.setCountryCode("FR");
		user.setGender("M");

		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": "Arn@ud",
				    "birthDate": "1975-01-01",
				    "countryCode": "FR",
				    "gender": "M"
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.name").value("has to contain only alphabetic characters"));
	}

	@Test
	public void testCreateUserSizeNameConstraintL() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("A");
		user.setBirthDate(LocalDate.parse("1975-01-01"));
		user.setCountryCode("FR");
		user.setGender("M");

		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": "A",
				    "birthDate": "1975-01-01",
				    "countryCode": "FR",
				    "gender": "M"
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.name").value("has to contain between 2 and 128 alphabetic characters"));
	}

	@Test
	public void testCreateUserSizeNameConstraintH() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("ABCDEFGHIJKLMNOPQRSTUVHXYZABCDEFGHIJKLMNOPQRSTUVWXYZ");
		user.setBirthDate(LocalDate.parse("1975-01-01"));
		user.setCountryCode("FR");
		user.setGender("M");

		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": "ABCDEFGHIJKLMNOPQRSTUVHXYZABCDEFGHIJKLMNOPQRSTUVWXYZ",
				    "birthDate": "1975-01-01",
				    "countryCode": "FR",
				    "gender": "M"
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.name").value("has to contain between 2 and 128 alphabetic characters"));
	}

	@Test
	public void testCreateUserContryCode() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Arnaud");
		user.setBirthDate(LocalDate.parse("1975-01-01"));
		user.setCountryCode("12");
		user.setGender("M");

		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": "Arnaud",
				    "birthDate": "1975-01-01",
				    "countryCode": "12",
				    "gender": "M"
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.countryCode").value("2 alphabetic characters only"));
	}

	@Test
	public void testCreateUserGender() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Arnaud");
		user.setBirthDate(LocalDate.parse("1975-01-01"));
		user.setCountryCode("FR");
		user.setGender("N");

		when(userService.saveUser(any(User.class))).thenReturn(user);

		String userJson = """
				{
				    "name": "Arnaud",
				    "birthDate": "1975-01-01",
				    "countryCode": "FR",
				    "gender": "N"
				}
				""";

		mockMvc.perform(post("/user").contentType("application/json").content(userJson))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.gender").value("F or M only"));
	}

	@Test
	public void testDeleteUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Stephane");
        user.setBirthDate(LocalDate.parse("1976-01-01"));
        user.setGender("M");
        user.setCountryCode("FR");
        when(userService.getUser(1L)).thenReturn(Optional.of(user));
        mockMvc.perform(delete("/user/{id}", 1L))
               .andExpect(status().isNoContent());
        verify(userService).deleteUser(1L);
	}

	@Test
	public void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/user/{id}", 999L))
        .andExpect(status().isNotFound());
        verify(userService, never()).deleteUser(999L);
	}

	@Test
	public void testUpdateUser() throws Exception {
		User user = new User();
        user.setId(1L);
        user.setName("Stephane");
        user.setBirthDate(LocalDate.parse("1976-01-01"));
        user.setGender("M");
        user.setCountryCode("FR");
        
		User updatedUser = new User();
		updatedUser.setId(1L);
		updatedUser.setName("Stephane");
		updatedUser.setBirthDate(LocalDate.parse("1976-01-01"));
		updatedUser.setGender("F");
		updatedUser.setCountryCode("FR");
        
        when(userService.getUser(1L)).thenReturn(Optional.of(user));
        when(userService.saveUser(any(User.class))).thenReturn(updatedUser);
        mockMvc.perform(put("/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Stephanie"))
               .andExpect(jsonPath("$.gender").value("F"));

        verify(userService).saveUser(any(User.class));   
	}
}