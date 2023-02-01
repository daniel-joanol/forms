package com.danieljoanol.forms.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.criteria.GroupCriteria;

public class GroupSpecification {

  public static Specification<Group> search(GroupCriteria criteria) {

    return new Specification<Group>() {

      @Override
      public Predicate toPredicate(
          Root<Group> root,
          CriteriaQuery<?> query,
          CriteriaBuilder cBuilder) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (criteria.getName() != null) {
          predicates.add(cBuilder.equal(root.get("name"), criteria.getName()));
        }

        if (criteria.getMaxUsers() != null) {
          predicates.add(cBuilder.equal(root.get("maxUsers"), criteria.getMaxUsers()));
        }

        if (criteria.getTotalUsers() != null) {
          predicates.add(cBuilder.equal(root.get("totalUsers"), criteria.getTotalUsers()));
        }

        if (criteria.getUsername() != null) {
          Join<Group, User> join = root.join("_user", JoinType.INNER);
          predicates.add(cBuilder.equal(join.get("username"), criteria.getUsername()));
        }

        return cBuilder.and(predicates.toArray(Predicate[]::new));
      }

    };
  }

}
