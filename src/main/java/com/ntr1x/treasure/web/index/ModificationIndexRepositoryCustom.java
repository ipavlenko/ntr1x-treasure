package com.ntr1x.treasure.web.index;

import java.util.List;

public interface ModificationIndexRepositoryCustom {
    
    List<ModificationIndex> search(String query, Long purchase, Long good, List<Long> categories, List<String> attributes);
}
