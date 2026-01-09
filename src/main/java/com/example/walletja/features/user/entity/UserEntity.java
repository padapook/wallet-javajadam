package com.example.walletja.features.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import jakarta.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.example.walletja.features.account.entity.Account;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Getter @Setter

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountId;

    @Column(unique = true, nullable = false)

    private String username;
    private String password;
    private String titleNameTh;
    private String titleNameEn;
    private String nameTh;
    private String nameEn;
    private String lastNameTh;
    private String lastNameEn;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedDate;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    private LocalDateTime deletedDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts;

}
