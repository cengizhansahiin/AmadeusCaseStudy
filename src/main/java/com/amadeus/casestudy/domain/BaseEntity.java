package com.amadeus.casestudy.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_entity_gen")
    @SequenceGenerator(name = "base_entity_gen", sequenceName = "base_entity_seq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
