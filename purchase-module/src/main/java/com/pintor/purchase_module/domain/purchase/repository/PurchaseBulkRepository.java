package com.pintor.purchase_module.domain.purchase.repository;

import com.pintor.purchase_module.domain.purchase.entity.Purchase;
import com.pintor.purchase_module.domain.purchase.status.PurchaseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PurchaseBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAllWithStatus(List<Purchase> purchaseList, PurchaseStatus after, LocalDateTime now) {
        String sql = "UPDATE purchase SET status = ?, updated_at = ? WHERE id = ?";

        this.jdbcTemplate.batchUpdate(sql,
                purchaseList,
                purchaseList.size(),
                (PreparedStatement ps, Purchase purchase) -> {
                    ps.setString(1, after.name());
                    ps.setObject(2, now);
                    ps.setLong(3, purchase.getId());
                }
        );
    }
}
