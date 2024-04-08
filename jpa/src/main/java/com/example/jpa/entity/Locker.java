package com.example.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString(exclude = "sportsMember")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Locker extends BaseEntity {

    @SequenceGenerator(name = "locker_id_seq_gen", sequenceName = "locker_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locker_id_seq_gen")
    @Column(name = "locker_id")
    @Id
    private Long id;

    private String name;

    @OneToOne(mappedBy = "locker")
    private SportsMember sportsMember;

}
