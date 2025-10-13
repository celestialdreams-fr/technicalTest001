package fr.celestialdreams.api.controller;
import fr.celestialdreams.api.model.User;
import fr.celestialdreams.api.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * Create - Add a new User
	 * @param user An object user
	 * @return The user object saved
	 */
	@PostMapping("/user")
	public User createUser(@Valid @RequestBody User user) {
		return userService.saveUser(user);
	}
	
	/**
	 * Read - Get one user 
	 * @param id The id of the user
	 * @return An User object full filled
	 */
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") final Long id) {
	    Optional<User> user = userService.getUser(id);
	    return user.map(ResponseEntity::ok)
	               .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	/**
	 * Read - Get all users
	 * @return - An Iterable object of User full filled
	 */
	@GetMapping("/users")
	public Iterable<User> getUsers() {
		return userService.getUsers();
	}
	
	/**
	 * Update - Update an existing user
	 * @param id - The id of the user to update
	 * @param user - The user object updated
	 * @return
	 */
	@PutMapping("/user/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") final Long id, @Valid @RequestBody User user) {
	    Optional<User> e = userService.getUser(id);
	    if (e.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	    User currentUser = e.get();

	    String name = user.getName();
	    if (name != null) {
	        currentUser.setName(name);
	    }
	    LocalDate birthDate = user.getBirthDate();
	    if (birthDate != null) {
	        currentUser.setBirthDate(birthDate);
	    }
	    String countryCode = user.getCountryCode();
	    if (countryCode != null) {
	        currentUser.setCountryCode(countryCode);
	    }
	    String gender = user.getGender();
	    if (gender != null) {
	        currentUser.setGender(gender);
	    }

	    User updated = userService.saveUser(currentUser);
	    return ResponseEntity.ok(updated);
	}
	
	
	/**
	 * Delete - Delete an user
	 * @param id - The id of the user to delete
	 */
	@DeleteMapping("/user/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") final Long id) {
	    Optional<User> user = userService.getUser(id);
	    if (user.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
	    }
	    userService.deleteUser(id);
	    return ResponseEntity.noContent().build();
	}
	

}