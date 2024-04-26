package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.repository;

import com.pintor.purchase_reservation_system.common.converter.AesConverter;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.status.PurchaseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PurchaseLogBulkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AesConverter aesConverter;

    @Transactional
    public void saveAll(List<Purchase> purchaseList, PurchaseStatus after, LocalDateTime now) {

        String sql = "INSERT INTO purchase_log (purchase_id, member_id, status, total_price, phone_number, zone_code, address, sub_address, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.batchUpdate(sql,
                purchaseList,
                purchaseList.size(),
                (ps, purchase) -> {
                    ps.setLong(1, purchase.getId());
                    ps.setLong(2, purchase.getMember().getId());
                    ps.setString(3, after.name());
                    ps.setLong(4, purchase.getTotalPrice());
                    ps.setString(5, this.aesConverter.convertToDatabaseColumn(purchase.getPhoneNumber()));
                    ps.setString(6, this.aesConverter.convertToDatabaseColumn(purchase.getZoneCode()));
                    ps.setString(7, this.aesConverter.convertToDatabaseColumn(purchase.getAddress()));
                    ps.setString(8, this.aesConverter.convertToDatabaseColumn(purchase.getSubAddress()));
                    ps.setObject(9, now);
                }
        );
    }
}
