package fr.celestialdreams.api;

import fr.celestialdreams.api.model.User;
import fr.celestialdreams.api.controller.UserController;
import fr.celestialdreams.api.repository.UserRepository;
import fr.celestialdreams.api.service.UserService;

import org.hamcrest.core.IsNull;
import org.hamcrest.text.IsEmptyString;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PutMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UserController.class)

public class UserControllerWriteTest {

	@Autowired
	private MockMvc mockMvc;
	
    @Autowired
    private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;


	@Test
	public void testCreateUserOptional() throws Exception {
        User saved = new User();
        saved.setId(1L);
        saved.setName("Arnaud");
        saved.setBirthDate(LocalDate.parse("1975-01-01"));
        saved.setCountryCode("FR");
        saved.setGender("M");
        saved.setPhoneNumber("+33");

        when(userService.saveUser(any(User.class))).thenReturn(saved);

        User payload = new User();
        payload.setName("Arnaud");
        payload.setBirthDate(LocalDate.parse("1975-01-01"));
        payload.setCountryCode("FR");
        payload.setGender("M");
        payload.setPhoneNumber("+33");

        String userJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Arnaud"))
            .andExpect(jsonPath("$.birthDate").value("1975-01-01"))
            .andExpect(jsonPath("$.countryCode").value("FR"))
            .andExpect(jsonPath("$.gender").value("M"))
            .andExpect(jsonPath("$.phoneNumber").value("+33"));

        verify(userService).saveUser(any(User.class));
	}

	@Test
	public void testCreateUserMandatory() throws Exception {
	     User saved = new User();
	        saved.setId(1L);
	        saved.setName("Arnaud");
	        saved.setCountryCode("FR");
	        saved.setBirthDate(LocalDate.parse("1975-01-01"));

	        when(userService.saveUser(any(User.class))).thenReturn(saved);

	        User payload = new User();
	        payload.setName("Arnaud");
	        payload.setCountryCode("FR");
	        payload.setBirthDate(LocalDate.parse("1975-01-01"));

	        String userJson = objectMapper.writeValueAsString(payload);

	        mockMvc.perform(post("/user")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(userJson))
	            .andExpect(status().isOk())
	            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	            .andExpect(jsonPath("$.id").value(1))
	            .andExpect(jsonPath("$.name").value("Arnaud"))
	            .andExpect(jsonPath("$.birthDate").value("1975-01-01"))
	            .andExpect(jsonPath("$.countryCode").value("FR"))
	            .andExpect(jsonPath("$.gender").doesNotExist())
	            .andExpect(jsonPath("$.phoneNumber").doesNotExist());

	        verify(userService).saveUser(any(User.class));
	}

	@Test
	public void testCreateUserErrKeyNameEmpty() throws Exception {
	    when(userService.saveUser(any(User.class))).thenReturn(new User());

	    Map<String,Object> payload = Map.of("name", "");
	    String userJson = objectMapper.writeValueAsString(payload);

	    mockMvc.perform(post("/user")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(userJson))
	        .andExpect(status().isUnprocessableEntity())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("$.name").value("has to contain between 2 and 40 alphabetic characters"));
	    verify(userService, never()).saveUser(any(User.class));
	}

	@Test
	public void testCreateUserErrKeyNameNull() throws Exception {
		when(userService.saveUser(any(User.class))).thenReturn(new User());
	    String userJson = "{}";
	    mockMvc.perform(post("/user")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(userJson))
	        .andExpect(status().isUnprocessableEntity())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("$.name").value("must not be null"));
	    verify(userService, never()).saveUser(any(User.class));
	}

	@Test
	public void testCreateUserErrKeyDateNull() throws Exception {
	    when(userService.saveUser(any(User.class))).thenReturn(new User());
	    Map<String,Object> payload = Map.of("name", "Arnaud");
	    String userJson = objectMapper.writeValueAsString(payload);
	    mockMvc.perform(post("/user")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(userJson))
	        .andExpect(status().isUnprocessableEntity())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("$.birthDate").value("age must be between 18 and 120 years"));
	    verify(userService, never()).saveUser(any(User.class));
	}

	@Test
	public void testCreateUserErrKeyDateEmptyOrMalformed() throws Exception {
	    when(userService.saveUser(any(User.class))).thenReturn(new User());
	    String userJson = """
	            {
	                "name": "Arnaud",
	                "birthDate": "abcdef"
	            }
	            """;
	    mockMvc.perform(post("/user")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(userJson))
	        .andExpect(status().isBadRequest())
	        .andExpect(jsonPath("$.birthDate").doesNotExist());
	    verify(userService, never()).saveUser(any(User.class));
	}

	@Test
	public void testCreateUserErrKeyDateChild() throws Exception {
		 LocalDate fixedNow = LocalDate.of(1950, 1, 1);
		    try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
		    	mocked.when(LocalDate::now).thenReturn(fixedNow);
		        mocked.when(() -> LocalDate.now(ArgumentMatchers.<ZoneId>any())).thenAnswer(invocation -> {
		            return fixedNow;
		        });
		        mocked.when(() -> LocalDate.now(ArgumentMatchers.<Clock>any())).thenAnswer(invocation -> {
		            return fixedNow;
		        });
			    when(userService.saveUser(any(User.class))).thenReturn(new User());
			    String userJson = """
			            {
			                "name": "Arnaud",
			                "birthDate": "1948-01-01",
			                "countryCode": "FR"
			            }
			            """;
			    mockMvc.perform(post("/user")
			            .contentType(MediaType.APPLICATION_JSON)
			            .content(userJson))
			        .andExpect(status().isUnprocessableEntity())
			        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			        .andExpect(jsonPath("$.birthDate").value("age must be between 18 and 120 years"));
			    verify(userService, never()).saveUser(any(User.class));
		    }
	}

	@Test
	void testCreateUserNameMalformed() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(new User());
        Map<String, Object> payload = Map.of(
            "name", "Arn@ud",
            "birthDate", "1975-01-01",
            "countryCode", "FR",
            "gender", "M"
        );
        String userJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("has to contain between 2 and 40 alphabetic characters"));
        verify(userService, never()).saveUser(any(User.class));
    }

	@Test
	public void testCreateUserSizeNameTooShort() throws Exception {
		 when(userService.saveUser(any(User.class))).thenReturn(new User());

	        Map<String, Object> payload = Map.of(
	            "name", "A",
	            "birthDate", "1975-01-01",
	            "countryCode", "FR",
	            "gender", "M"
	        );
	        String userJson = objectMapper.writeValueAsString(payload);

	        mockMvc.perform(post("/user")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(userJson))
	            .andExpect(status().isUnprocessableEntity())
	            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	            .andExpect(jsonPath("$.name").value("has to contain between 2 and 40 alphabetic characters"));

	        verify(userService, never()).saveUser(any(User.class));
	}

	void testCreateUserSizeNameTooLong() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(new User());
        String longName = "ABCDEFGHIJKLMNOPQRSTUVHXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Map<String, Object> payload = Map.of(
            "name", longName,
            "birthDate", "1975-01-01",
            "countryCode", "FR",
            "gender", "M"
        );
        String userJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("has to contain between 2 and 40 alphabetic characters"));
        verify(userService, never()).saveUser(any(User.class));
    }


	@Test
    void testCreateUserCountryCodeInvalid() throws Exception {
        when(userService.saveUser(any())).thenReturn(new fr.celestialdreams.api.model.User());
        Map<String, Object> payload = Map.of(
            "name", "Arnaud",
            "birthDate", "1975-01-01",
            "countryCode", "12",
            "gender", "M"
        );
        String userJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.countryCode").value("invalid country code, only french country code are allowed"));
        verify(userService, never()).saveUser(any());
    }

	@Test
	public void testCreateUserGender() throws Exception {
		when(userService.saveUser(any(User.class))).thenReturn(new User());
        Map<String, Object> payload = Map.of(
            "name", "Arnaud",
            "birthDate", "1975-01-01",
            "countryCode", "FR",
            "gender", "N"
        );
        String userJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.gender").value("F or M only"));
        verify(userService, never()).saveUser(any(User.class));
    }

	@Test
    void testDeleteUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Stephanie");
        user.setBirthDate(LocalDate.parse("1976-01-01"));
        user.setGender("M");
        user.setCountryCode("FR");

        when(userService.getUser(1L)).thenReturn(Optional.of(user));
        mockMvc.perform(delete("/user/{id}", 1L))
               .andExpect(status().isNoContent());
        verify(userService).deleteUser(1L);
    }

	@Test
    void testDeleteUserNotFound() throws Exception {
        when(userService.getUser(999L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/user/{id}", 999L))
               .andExpect(status().isNotFound());
        verify(userService, never()).deleteUser(999L);
    }

    @Test
    void testUpdateUser() throws Exception {
        User existing = new User();
        existing.setId(1L);
        existing.setName("Stephane");
        existing.setBirthDate(LocalDate.parse("1976-01-01"));
        existing.setGender("M");
        existing.setCountryCode("FR");
        
        User saved = new User();
        saved.setId(1L);
        saved.setName("Stephanie");
        saved.setBirthDate(LocalDate.parse("1976-01-01"));
        saved.setGender("F");
        saved.setCountryCode("FR");
        when(userService.getUser(1L)).thenReturn(Optional.of(existing));
        when(userService.saveUser(any(User.class))).thenReturn(saved);
        
        String payloadJson = objectMapper.writeValueAsString(saved);

        mockMvc.perform(put("/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(payloadJson))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Stephanie"))
               .andExpect(jsonPath("$.gender").value("F"));

        verify(userService).saveUser(any(User.class));
    }
}