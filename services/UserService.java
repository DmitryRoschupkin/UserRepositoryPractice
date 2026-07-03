package services;

import java.util.UUID;

import annotations.Component;
import annotations.Inject;
import annotations.PostConstruct;
import repository.UserRepository;
import validators.Validator;
import models.User;

@Component
public class UserService {
    private UserRepository<UUID, User> repo;

    @Inject
    public UserService(UserRepository<UUID, User> repo){
        this.repo = repo;
    }

    @PostConstruct
    private void init(){
        System.out.println("UserService builded succesfully and ready to work!");
    }

    public void registerUser(UUID id, User user) throws Exception{
        repo.save(id, user);
        Validator.validate(user);
        System.out.println(user.getName()+" registered and saved succesfully with ID "+id);
    }

}
