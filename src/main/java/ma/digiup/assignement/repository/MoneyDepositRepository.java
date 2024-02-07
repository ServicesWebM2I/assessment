package ma.digiup.assignement.repository;

import ma.digiup.assignement.domain.MoneyDeposit;
import ma.digiup.assignement.domain.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyDepositRepository extends JpaRepository<MoneyDeposit, Long> {
}
