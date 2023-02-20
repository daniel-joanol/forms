package com.danieljoanol.forms.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.repository.criteria.ShopCriteria;

public class ShopSpecification {

  public static Specification<Shop> search(ShopCriteria criteria) {

    return new Specification<Shop>() {

      @Override
      public Predicate toPredicate(
          Root<Shop> root,
          CriteriaQuery<?> query,
          CriteriaBuilder cBuilder) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (criteria.getShopName() != null) {
          predicates.add(cBuilder.equal(root.get("shopName"), criteria.getShopName()));
        }

        if (criteria.getOwnerName() != null) {
          predicates.add(cBuilder.equal(root.get("ownerName"), criteria.getOwnerName()));
        }

        if (criteria.getCity() != null) {
          predicates.add(cBuilder.equal(root.get("city"), criteria.getCity()));
        }

        if (criteria.getProvince() != null) {
          predicates.add(cBuilder.equal(root.get("province"), criteria.getProvince()));
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

        return cBuilder.and(predicates.toArray(Predicate[]::new));

      }
    };
  }

}
