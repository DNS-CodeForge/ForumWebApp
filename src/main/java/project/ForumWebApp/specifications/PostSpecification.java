package project.ForumWebApp.specifications;

import org.springframework.data.jpa.domain.Specification;
import project.ForumWebApp.models.Post;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PostSpecification {

    public static Specification<Post> withFiltersAndSort(String title, String description, String user, List<String> tags, String sort) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Stream.of(
                            Optional.ofNullable(title)
                                    .filter(t -> !t.isEmpty())
                                    .map(t -> criteriaBuilder.like(root.get("title"), "%" + t + "%")),
                            Optional.ofNullable(description)
                                    .filter(d -> !d.isEmpty())
                                    .map(d -> criteriaBuilder.like(root.get("description"), "%" + d + "%")),
                            Optional.ofNullable(user)
                                    .filter(u -> !u.isEmpty())
                                    .map(u -> criteriaBuilder.like(root.get("user").get("username"), "%" + u + "%")),

                            Optional.ofNullable(tags)
                                    .filter(t -> !t.isEmpty())
                                    .map(t -> root.join("tags").get("name").in(t))
                    )
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(predicates::add);

            if (sort != null) {
                if ("commentsAsc".equals(sort)) {
                    query.orderBy(criteriaBuilder.asc(criteriaBuilder.size(root.get("comments"))));
                } else if ("commentsDesc".equals(sort)) {
                    query.orderBy(criteriaBuilder.desc(criteriaBuilder.size(root.get("comments"))));
                } else if ("dateDesc".equals(sort)) {
                    query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
