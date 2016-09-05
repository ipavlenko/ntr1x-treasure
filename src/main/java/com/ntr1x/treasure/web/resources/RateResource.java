package com.ntr1x.treasure.web.resources;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.msg.CommentEntity;
import com.ntr1x.treasure.web.model.security.SecuritySession;
import com.ntr1x.treasure.web.model.sociality.LikeEntity;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api("Rate")
@Component
@Path("/ws/rate")
@Slf4j
public class RateResource {

	@Inject
	private SecuritySession session;

	@PersistenceContext
	private EntityManager em;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddLike {
		private long resourceId;
		private int rate;
		private String message;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddComment {
		private long resourceId;
		private String message;
		private boolean confidential;
		private long parentId;
	}

	@POST
	@Path("/like")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("user")
	public Response doLike(AddLike add) {
		if (session != null){
			LikeEntity like = new LikeEntity();

			com.ntr1x.treasure.web.model.security.SecurityResource res = em.find(com.ntr1x.treasure.web.model.security.SecurityResource.class, add.getResourceId());

			if (res != null){
				like.setMessage(add.getMessage());
				like.setRate(add.getRate());
				like.setRelate(res);
				like.setUser(session.getUser());

				em.persist(like);
				em.flush();

				return Response.ok(em.find(LikeEntity.class, like.getId())).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@DELETE
	@Path("/like/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("admin, moderator")
	public Response deleteLike(
			@PathParam("id") long id
	) {
		if (session != null){
			em.remove(em.find(LikeEntity.class, id));
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@POST
	@Path("/comment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	//	@RolesAllowed("user")
	public Response doComment(AddComment add) {
		try {
			if (session != null){

				com.ntr1x.treasure.web.model.security.SecurityResource res = em.find(com.ntr1x.treasure.web.model.security.SecurityResource.class, add.getResourceId());

				if (res != null){
					CommentEntity comment = new CommentEntity();
					comment.setRelate(res);
					comment.setConfidential(add.isConfidential());
					comment.setUser(session.getUser());
					comment.setBody(add.getMessage());

					em.persist(comment);
					em.flush();

					return Response.ok(comment).build();
				}
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (Exception e){
			log.error("{}", e);
			throw e;
		}
	}
}
