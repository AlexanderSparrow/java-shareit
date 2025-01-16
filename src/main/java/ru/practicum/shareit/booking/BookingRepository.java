package ru.practicum.shareit.booking;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b WHERE b.itemId = :itemId " +
            "AND b.status = 'APPROVED' " +
            "AND (b.start < :endDate AND b.end > :startDate)")
    boolean existsByItemIdAndDateRange(long itemId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT b FROM Booking b WHERE b.bookerId = :userId ORDER BY b.start DESC")
    List<Booking> findAllByBookerId(long userId);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN (SELECT i.id FROM Item i WHERE i.ownerId = :ownerId) " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.bookerId = :userId AND b.status = :status ORDER BY b.start DESC")
    List<Booking> findByBookerIdAndStatus(long userId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN (SELECT i.id FROM Item i WHERE i.ownerId = :ownerId) " +
            "AND b.status = :status ORDER BY b.start DESC")
    List<Booking> findByOwnerIdAndStatus(long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.bookerId = :userId AND b.start < :now AND b.end > :now " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentByBookerId(long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN (SELECT i.id FROM Item i WHERE i.ownerId = :ownerId) " +
            "AND b.start < :now AND b.end > :now ORDER BY b.start DESC")
    List<Booking> findCurrentByOwnerId(long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.bookerId = :userId AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findPastByBookerId(long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN (SELECT i.id FROM Item i WHERE i.ownerId = :ownerId) " +
            "AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findPastByOwnerId(long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.bookerId = :userId AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findFutureByBookerId(long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN (SELECT i.id FROM Item i WHERE i.ownerId = :ownerId) " +
            "AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findFutureByOwnerId(long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.itemId = :itemId AND b.end < :now ORDER BY b.end DESC LIMIT 1")
    Booking findLastBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.itemId = :itemId AND b.start > :now ORDER BY b.start ASC LIMIT 1")
    Booking findNextBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);
}