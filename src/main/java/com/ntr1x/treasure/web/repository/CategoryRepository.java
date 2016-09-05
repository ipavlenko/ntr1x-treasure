//package com.ntr1x.treasure.web.repository;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import com.ntr1x.treasure.web.model.Category;
//
//public interface CategoryRepository extends JpaRepository<Category, Long> {
//    
//    @Query(
//        " SELECT DISTINCT c"
//      + " FROM"
//      + "   Category c"
//      + "   JOIN c.aspects a"
//      + " WHERE (:relate IS NULL OR c.relate.id = :relate)"
//      + "   AND (:aspect IS NULL OR a = :aspect)"
//    )
//    Page<Category> findByRelateAndAspect(
//            @Param("relate") Long relate,
//            @Param("aspect") String aspect,
//            Pageable pageable
//    );
//}
