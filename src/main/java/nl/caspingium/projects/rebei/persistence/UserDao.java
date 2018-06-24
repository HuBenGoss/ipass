package nl.caspingium.projects.rebei.persistence;

public interface UserDao {
    String findRoleForUser(String name, String password);
}
