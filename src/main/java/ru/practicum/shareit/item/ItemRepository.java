package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(long ownerId);

    @Query("select i from Item i " +
            "where i.available = true and " +
            "(upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> searchItems(String text);

    @Query("SELECT COUNT(i) > 0 FROM Item i WHERE i.id = :itemId AND i.ownerId = :userId")
    boolean isOwner(@Param("userId") long userId, @Param("itemId") long itemId);

    @Query("SELECT i.name FROM Item i WHERE i.id = :id")
    String findNameById(@Param("id") Long id);
}