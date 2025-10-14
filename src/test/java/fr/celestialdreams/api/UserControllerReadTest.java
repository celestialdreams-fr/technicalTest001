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

public class UserControllerReadTest {

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
	public void testGetUserOptional() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Stephane");
		user.setBirthDate(LocalDate.parse("1976-01-01"));
		user.setGender(String.valueOf('M'));
		user.setCountryCode("FR");
		user.setPhoneNumber("+33777777777");

		when(userService.getUser(1L)).thenReturn(Optional.of(user));
		mockMvc.perform(get("/user/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name", is("Stephane")))
			.andExpect(jsonPath("birthDate", is("1976-01-01")))
			.andExpect(jsonPath("countryCode", is("FR")))
			.andExpect(jsonPath("gender", is("M")))
			.andExpect(jsonPath("phoneNumber", is("+33777777777")));
	}

	@Test
	public void testGetUserMandatory() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Stephane");
		user.setCountryCode("FR");
		user.setBirthDate(LocalDate.parse("1976-01-01"));
		user.setGender(null);
		user.setPhoneNumber(null);
		

		when(userService.getUser(1L)).thenReturn(Optional.of(user));
		mockMvc.perform(get("/user/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name", is("Stephane")))
			.andExpect(jsonPath("birthDate", is("1976-01-01")))
			.andExpect(jsonPath("$.countryCode", is("FR")))
			.andExpect(jsonPath("$.gender").doesNotExist())
			.andExpect(jsonPath("$.phoneNumber").doesNotExist());
	}

	@Test
	public void testGetUsers() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Arnaud");
		user.setCountryCode("FR");
		user.setBirthDate(LocalDate.parse("1975-01-01"));
		when(userService.getUsers()).thenReturn(Collections.singletonList(user));
		mockMvc.perform(get("/users"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name", is("Arnaud")));
	}
}