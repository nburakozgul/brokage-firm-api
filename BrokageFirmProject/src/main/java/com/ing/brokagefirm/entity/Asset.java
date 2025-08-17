package com.ing.brokagefirm.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "asset")
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
    private Double size;
    @Column(name = "usable_size")
    private Double usableSize;

    @OneToMany(mappedBy="asset")
    @JsonManagedReference
    private List<Order> orders;

    public Asset(LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Long id, String customerId, String assetId, String assetName, String assetDescription, Double size, Double usableSize) {
        super(createdAt, updatedAt, createdBy, updatedBy);
        this.id = id;
        this.customerId = customerId;
        this.assetId = assetId;
        this.assetName = assetName;
        this.assetDescription = assetDescription;
        this.size = size;
        this.usableSize = usableSize;
    }

    public Asset() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Double getUsableSize() {
        return usableSize;
    }

    public void setUsableSize(Double usableSize) {
        this.usableSize = usableSize;
    }
}
