package online.mengxun.server.reposity;

import online.mengxun.server.entity.DbConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DbConfigRepository extends JpaRepository<DbConfig,String> {
    List<DbConfig> findAllByOpenId(String openId);
    boolean existsByDbIdAndOpenId(String dbId,String openId);
}
