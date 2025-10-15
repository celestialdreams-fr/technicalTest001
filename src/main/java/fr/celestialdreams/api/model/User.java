	package fr.celestialdreams.api.model;
	
	import java.time.LocalDate;
    import fr.celestialdreams.api.validation.CCCheck;
	import fr.celestialdreams.api.validation.MinAge;
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
	    @Pattern(regexp = "^[a-zA-Z]{2,40}$", message = "has to contain between 2 and 40 alphabetic characters")
	    @Column(name = "name", nullable = false, length = 40)
	    private String name;
		
		// todo add min-max date into .properties 
	    @MinAge(min = 18, max = 120, message = "age must be between {min} and {max} years")
	    @Column(name = "birth_date", nullable = false)
	    private LocalDate birthDate;

		// todo add allowed countries into .properties
	    // French-related ISO 3166-1 alpha-2 country/territory codes
	    @CCCheck(allowed = {"FR","GP","MQ","GF","RE","YT","NC","PF","WF","PM","BL","MF","TF"}, message = "invalid country code, only french country code are allowed")
	    @Column(name = "country_code", nullable = false, length = 2)
	    private String countryCode;
	
	    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "invalid phone number format, must be a valid E.164 phone number")
	    @Column(name = "phoneNumber", length = 16)
	    private String phoneNumber;
	
	    @Pattern(regexp = "^[FM]$", message = "F or M only")   
	    @Column(name = "gender", length = 1)
	    private String gender;
	}
