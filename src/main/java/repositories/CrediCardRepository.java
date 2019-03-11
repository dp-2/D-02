
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.CreditCard;

@Repository
public interface CrediCardRepository extends JpaRepository<CreditCard, Integer> {

}
