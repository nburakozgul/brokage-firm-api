package com.ing.brokagefirm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asset")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Asset extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "asset_id")
    private String assetId;
    @Column(name = "asset_name")
    private String assetName;
    @Column(name = "asset_desc")
    private String assetDescription;
}
