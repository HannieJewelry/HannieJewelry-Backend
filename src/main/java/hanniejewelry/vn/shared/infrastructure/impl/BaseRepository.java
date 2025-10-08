package hanniejewelry.vn.shared.infrastructure.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, K extends Serializable> extends JpaRepository<T, K> {
    void softDeleteById(K id);
}
