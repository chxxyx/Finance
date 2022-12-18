package com.chxxyx.dividendproject.persist;

import com.chxxyx.dividendproject.persist.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

}
