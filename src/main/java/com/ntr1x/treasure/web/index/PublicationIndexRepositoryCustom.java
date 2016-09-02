package com.ntr1x.treasure.web.index;

import java.util.List;

public interface PublicationIndexRepositoryCustom {
    
    List<PublicationIndex> search(String query, List<Long> categories);
}
