package com.dennis.ecommerce.catalogService.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name="categories")
public class Category (
        @field: Id
        @field: GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:Column
        var name: String,

        @field:ManyToOne(fetch = FetchType.LAZY)
        @field:JoinColumn(name = "parent_id")
        var parent: Category? = null,

        @field:OneToMany(mappedBy = "parent", cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
        val children: MutableList<Category> = mutableListOf()
){
        override fun toString() = "Category(id=$id, name='$name')"
        override fun equals(other: Any?) = (other is Category) && id == other.id// sin el override se compara los objetos en memoria y siempre daran false (son objetos distintos en memoria)
                                                                                // con override se comparan los valores de cada objeto y si son del mismo tipo de objeto (Category) y los id coinciden sera true
        override fun hashCode() = id.hashCode()
}
