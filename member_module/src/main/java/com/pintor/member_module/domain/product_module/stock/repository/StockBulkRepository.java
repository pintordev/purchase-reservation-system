package com.pintor.member_module.domain.product_module.stock.repository;

import com.pintor.member_module.domain.product_module.stock.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class StockBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Stock> stockList) {
        String sql = "UPDATE stock SET quantity = ? WHERE product_id = ?";

        this.jdbcTemplate.batchUpdate(sql,
                stockList,
                stockList.size(),
                (ps, stock) -> {
                    ps.setInt(1, stock.getQuantity());
                    ps.setLong(2, stock.getProduct().getId());
                }
        );
    }
}
