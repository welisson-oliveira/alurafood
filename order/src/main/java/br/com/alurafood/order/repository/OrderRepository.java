package br.com.alurafood.order.repository;

import br.com.alurafood.order.model.Order;
import br.com.alurafood.order.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Order p set p.status = :status where p = :order")
    void updateStatus(Status status, Order order);

    @Query(value = "SELECT p from Order p LEFT JOIN FETCH p.items where p.id = :id")
    Order getByIdWithItems(Long id);
}
