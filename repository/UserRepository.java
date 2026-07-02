package repository;
public interface UserRepository<ID, T> {
    public void save(ID id, T t);
    public T findById(ID id);
    public void delete(ID id);
}
