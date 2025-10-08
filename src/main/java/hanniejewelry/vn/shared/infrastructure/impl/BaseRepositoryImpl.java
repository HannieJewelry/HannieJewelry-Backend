package hanniejewelry.vn.shared.infrastructure.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import hanniejewelry.vn.product.entity.SoftDeletable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public class BaseRepositoryImpl<T, K extends Serializable>
        extends SimpleJpaRepository<T, K>
        implements BaseRepository<T, K> {

  private final EntityManager entityManager;

  public BaseRepositoryImpl(
          JpaEntityInformation<T, ?> entityInformation,
          EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }


  @Override
  @Transactional
  public void softDeleteById(K id) {
    T entity = findById(id).orElseThrow(() -> new EntityNotFoundException("Not found"));
    if (!(entity instanceof SoftDeletable softDeletable)) {
      throw new IllegalArgumentException("Entity does not support soft delete");
    }
    softDeletable.setDeleted(true);
    save(entity);
    flush();
  }
}

