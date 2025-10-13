package fr.celestialdreams.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.celestialdreams.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}