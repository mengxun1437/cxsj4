package online.mengxun.server.reposity;

import online.mengxun.server.entity.DbDriver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbDriverRepository extends JpaRepository<DbDriver,String> {
}
