package com.ntr1x.treasure.web.resources;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.purchase.ResourceType;
import com.ntr1x.treasure.web.model.security.SecurityResource;

import io.swagger.annotations.Api;

@Api("Resource")
@Component
@Path("/ws/resources")
public class SecurityResourceResource {

	@PersistenceContext
	private EntityManager em;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<SecurityResource> resourcesQuery(
			@QueryParam("resType") ResourceType type,
			@QueryParam("limit") int limit,
			@QueryParam("offset") int offset
	){

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
		Root res = criteriaQuery.from(SecurityResource.class);
		if (type != null)
			criteriaQuery.where(criteriaBuilder.equal(res.get("resType"), type));
		Query query = em.createQuery(criteriaQuery);
		query.setMaxResults(limit);
		query.setFirstResult(offset);

		return query.getResultList();
	}
}
