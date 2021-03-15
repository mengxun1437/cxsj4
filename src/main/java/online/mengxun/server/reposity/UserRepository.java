package online.mengxun.server.reposity;

import online.mengxun.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String>{
}
