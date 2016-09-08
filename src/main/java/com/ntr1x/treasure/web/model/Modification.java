package com.ntr1x.treasure.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "modifications")
public class Modification {

    public enum Status {
        DELETED,
        ACTIVE
    }
    
    @Column(name = "Status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Column(name = "Price", nullable = false)
    private Float price;

    @Column(name = "Quantity", nullable = false)
    private Float quantity;

    @Column(name = "SizeRange", nullable = false)
    private Float sizeRange = 0f;
}
