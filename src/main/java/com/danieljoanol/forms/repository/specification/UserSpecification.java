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
import com.danieljoanol.forms.repository.criteria.UserCriteria;

public class UserSpecification {

  public static Specification<User> search(UserCriteria criteria) {

    return new Specification<User>() {

      @Override
      public Predicate toPredicate(
          Root<User> root,
          CriteriaQuery<?> query,
          CriteriaBuilder cBuilder) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (criteria.getFirstName() != null) {
          predicates.add(cBuilder.equal(root.get("firstName"), criteria.getFirstName()));
        }

        if (criteria.getLastName() != null) {
          predicates.add(cBuilder.equal(root.get("lastName"), criteria.getLastName()));
        }

        if (criteria.getUsername() != null) {
          predicates.add(cBuilder.equal(root.get("totalUsers"), criteria.getUsername()));
        }

        if (criteria.getMaxLastPayment() != null) {
          predicates.add(cBuilder.lessThanOrEqualTo(root.get("lastPayment"), criteria.getMaxLastPayment()));
        }

        if (criteria.getMinLastPayment() != null) {
          predicates.add(cBuilder.greaterThanOrEqualTo(root.get("lastPayment"), criteria.getMinLastPayment()));
        }

        if (criteria.getIsEnabled() != null) {
          predicates.add(cBuilder.equal(root.get("isEnabled"), criteria.getIsEnabled()));

          if (criteria.getIsEnabled() == false) {
            if (criteria.getMaxDisabledDate() != null) {
              predicates.add(cBuilder.lessThanOrEqualTo(root.get("disabledDate"), criteria.getMaxDisabledDate()));
            }

            if (criteria.getMinDisabledDate() != null) {
              predicates.add(cBuilder.greaterThanOrEqualTo(root.get("disabledDate"), criteria.getMinDisabledDate()));
            }
          }

        }

        if (criteria.getGroupName() != null) {
          Join<User, Group> groupJoin = root.join("group", JoinType.INNER);
          predicates.add(cBuilder.equal(groupJoin.get("name"), criteria.getGroupName()));
        }

        return cBuilder.and(predicates.toArray(Predicate[]::new));
      }

    };

  }

}
