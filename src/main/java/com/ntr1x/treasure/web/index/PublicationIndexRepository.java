package com.ntr1x.treasure.web.index;

import org.springframework.data.solr.repository.SolrCrudRepository;

public interface PublicationIndexRepository extends SolrCrudRepository<PublicationIndex, String>, PublicationIndexRepositoryCustom {
    
//    Page<PublicationIndex> findByTitleOrPromoOrContent(@Boost(8) String title, @Boost(4) String promo, String content, Pageable pageable);
//
//    
//    @Query(
//        "category_ls:\"?1\" AND (title_txt_ru:\"?0\" OR promo_txt_ru:\"?0\" OR content_txt_ru:\"?0\")"
//    )
//    Page<PublicationIndex> findByQueryFilterByCategories(String query, List<Long> categories, Pageable pageable);
//    
//    @Query(
//        "title_txt_ru:\"?0\" OR promo_txt_ru:\"?0\" OR content_txt_ru:\"?0\""
//    )
//    Page<PublicationIndex> findByQuery(String query, Pageable pageable);
}
