package ru.otus.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "ingredient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Float quantity;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    public Ingredient(UUID id, Product product, Float quantity, Unit unit) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.unit = unit;
    }
}
