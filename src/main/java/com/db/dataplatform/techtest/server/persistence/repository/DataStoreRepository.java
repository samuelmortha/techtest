package com.db.dataplatform.techtest.server.persistence.repository;

import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;

@Repository
public interface DataStoreRepository extends JpaRepository<DataBodyEntity, Long> {
    List<DataBodyEntity> findByDataHeaderEntityBlocktype(BlockTypeEnum blocktype);
}
