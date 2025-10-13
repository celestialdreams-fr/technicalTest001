	package fr.celestialdreams.api.model;
	
	import java.time.LocalDate;
	import jakarta.persistence.Column;
	import jakarta.persistence.Entity;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.persistence.Table;
	import jakarta.validation.constraints.*;
	import lombok.Data;
	
	@Data
	@Entity
	@Table(name = "users")
	public class User {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	
	    @NotNull
	    @NotBlank
	    @Pattern(regexp = "^[a-zA-Z]+$", message = "has to contain only alphabetic characters")
	    @Size(min = 2, max = 40, message = "has to contain between 2 and 128 alphabetic characters")
	    @Column(name = "name", nullable = false, length = 40)
	    private String name;
	
	    @NotNull
	    @Past(message = "the birtday date has to be in the past")
	    @Column(name = "birth_date", nullable = false)
	    private LocalDate birthDate;
	
	    @Pattern(regexp = "^[a-zA-Z]{2}$", message = "2 alphabetic characters only")
	    @Column(name = "country_code", length = 2)
	    private String countryCode;
	
	    @Pattern(regexp = "^[fmFM]$", message = "F or M only")   
	    @Column(name = "gender", length = 1)
	    private String gender;
	}