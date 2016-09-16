package com.ntr1x.treasure.web.index;

import org.springframework.data.solr.repository.SolrCrudRepository;

public interface ModificationIndexRepository extends SolrCrudRepository<ModificationIndex, Long>, ModificationIndexRepositoryCustom {
}
