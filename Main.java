import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        UserRepository<User, UUID> repo = new Repository<>();
        User user = new User("Dmitriy", 18);
        UUID id = UUID.randomUUID();
        repo.save(id, user);
        System.out.println(repo.findById(id));
    }
}
