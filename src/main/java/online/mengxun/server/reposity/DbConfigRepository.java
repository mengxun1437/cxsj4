package online.mengxun.server.reposity;

import online.mengxun.server.entity.DbConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbConfigRepository extends JpaRepository<DbConfig,String> {
}
