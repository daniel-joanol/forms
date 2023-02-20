package com.danieljoanol.forms.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.repository.criteria.FormCriteria;

public class FormSpecification {

  public static Specification<Form> search(FormCriteria criteria) {

    return new Specification<Form>() {

      @Override
      public Predicate toPredicate(
          Root<Form> root,
          CriteriaQuery<?> query,
          CriteriaBuilder cBuilder) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (criteria.getPlate() != null) {
          predicates.add(cBuilder.equal(root.get("plate"), criteria.getPlate()));
        }

        if (criteria.getModel() != null) {
          predicates.add(cBuilder.equal(root.get("model"), criteria.getModel()));
        }

        if (criteria.getBrand() != null) {
          predicates.add(cBuilder.equal(root.get("brand"), criteria.getBrand()));
        }

        if (criteria.getAgent() != null) {
          predicates.add(cBuilder.equal(root.get("agent"), criteria.getAgent()));
        }

        if (criteria.getGroup() != null) {
          predicates.add(cBuilder.equal(root.get("group"), criteria.getGroup()));
        }

        if (criteria.getFrame() != null) {
          predicates.add(cBuilder.or(
              cBuilder.equal(root.get("frame"), criteria.getFrame()),
              cBuilder.equal(root.get("chassis"), criteria.getFrame())));
        }

        if (criteria.getOpenOrder() != null) {
          predicates.add(cBuilder.equal(root.get("openOrder"), criteria.getOpenOrder()));
        }

        if (criteria.getMaxDate() != null) {
          predicates.add(cBuilder.lessThanOrEqualTo(root.get("date"), criteria.getMaxDate()));
        }

        if (criteria.getMinDate() != null) {
          predicates.add(cBuilder.greaterThanOrEqualTo(root.get("date"), criteria.getMinDate()));
        }

        return cBuilder.and(predicates.toArray(Predicate[]::new));
      }
    };
  }

}
