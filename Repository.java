import java.util.HashMap;
import java.util.Map;

public class Repository<T, ID> implements UserRepository<T, ID>{
    public Map<ID, T> repo = new HashMap<>();

    @Override
    public void save(ID id, T t){
        repo.put(id, t);
        System.out.println("Object "+t.getClass()+" saved to the repository succesfully!");
    }
    @Override
    public T findById(ID id){
        return repo.getOrDefault(id, null);
    }
    @Override
    public void delete(ID id){
        repo.remove(id);
    }
}
