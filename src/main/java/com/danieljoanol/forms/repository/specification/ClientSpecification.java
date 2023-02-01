package com.danieljoanol.forms.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.repository.criteria.ClientCriteria;

public class ClientSpecification {

  public static Specification<Client> search(ClientCriteria criteria) {

    return new Specification<Client>() {

      @Override
      public Predicate toPredicate(
          Root<Client> root,
          CriteriaQuery<?> query,
          CriteriaBuilder cBuilder) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (criteria.getName() != null) {
          predicates.add(cBuilder.equal(root.get("name"), criteria.getName()));
        }

        if (criteria.getCity() != null) {
          predicates.add(cBuilder.equal(root.get("city"), criteria.getCity()));
        }

        if (criteria.getProvince() != null) {
          predicates.add(cBuilder.equal(root.get("province"), criteria.getProvince()));
        }

        if (criteria.getEmail() != null) {
          predicates.add(cBuilder.equal(root.get("email"), criteria.getEmail()));
        }

        if (criteria.getDocument() != null) {
          predicates.add(cBuilder.equal(root.get("document"), criteria.getDocument()));
        }

        if (criteria.getGroup() != null) {
          predicates.add(cBuilder.equal(root.get("group"), criteria.getGroup()));
        }

        if (criteria.getPhone() != null) {
          predicates.add(cBuilder.or(
              cBuilder.equal(root.get("phone1"), criteria.getPhone()),
              cBuilder.equal(root.get("phone2"), criteria.getPhone())));
        }

        if (criteria.getIsEnabled() != null) {
          predicates.add(cBuilder.equal(root.get("isEnabled"), criteria.getIsEnabled()));
        }

        return cBuilder.and(predicates.toArray(Predicate[]::new));

      }
    };
  }

}
