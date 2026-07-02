import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        UserRepository<User, UUID> repo = new Repository<>();
        UUID id = UUID.randomUUID();
        try{
            User user = new User("Dmitriy", 18);
            Validator.validate(user);
            repo.save(id, user);
        }catch(Exception e){
            System.out.println("Validation error: "+e.getMessage());
        }
        User requiredUser = repo.findById(id);
        if(requiredUser != null){
            System.out.println(requiredUser);
        }
    }
}
